# Prompts — Debugging

Prompts to diagnose errors. Include the **exact error/stacktrace** in the prompt.

## Good practices

- Paste the full error message / stacktrace and the relevant code.
- State what you have already tried and the expected vs observed behavior.
- Ask for the **root cause**, not just the patch. Be suspicious of "fixes" that silence the symptom.
- Always verify the explanation — the AI may hallucinate plausible but wrong causes
  (record it in `docs/14_ai_usage_log.md`).

---

### #p1 — (example) Crash when opening a screen

- Tool: Android Studio AI
- Date: 2026-05-19

**Prompt:**
> The app crashes when navigating to DetailScreen. Stacktrace:
> ```
> <paste stacktrace>
> ```
> Relevant code: `<paste>`. Expected: open the detail. I already tried <...>.
> Identify the root cause and propose the minimal fix, without changing the architecture.

**Identified cause:** Incorrect intent extras key used in the destination activity.
**Fix:** Unified the key string in a shared constant.
**Did the AI get the cause right?** Yes.

---

### #p2 — Compile error: Unresolved `setContent` after Activity refactor

- Tool: Android Studio AI
- Date: 2026-06-20

**Prompt:**
> Line 17: Error: Unresolved reference 'setContent' for the text `setContent` on the line `setContent {`
> Line 10: Error: Unresolved reference 'compose' for the text `compose` on the line `import androidx.activity.compose.setContent`

**Identified cause:** The `androidx.activity:activity-compose` dependency was missing from `build.gradle.kts`, and Jetpack Compose features were not enabled in the build configuration.
**Fix:** Enabled `buildFeatures { compose = true }` and added the `activity-compose` library to the dependencies block.
**Did the AI get the cause right?** Yes.

---

### #p3 — Build error: Kotlin 2.0 requires Compose Compiler plugin

- Tool: Android Studio AI
- Date: 2026-06-20

**Prompt:**
> ERROR: A problem occurred configuring project ':app'.
> > Starting in Kotlin 2.0, the Compose Compiler Gradle plugin is required when compose is enabled. See the following link for more information: https://d.android.com/r/studio-ui/compose-compiler

**Identified cause:** The project moved to Kotlin 2.0, which decouples the Compose compiler from the library version and requires a dedicated Gradle plugin.
**Fix:** Added `org.jetbrains.kotlin.plugin.compose` to `libs.versions.toml` and applied the plugin in `build.gradle.kts`.
**Did the AI get the cause right?** Yes.

---

### #p4 — Google Services error: Package name mismatch

- Tool: Android Studio AI
- Date: 2026-06-20

**Prompt:**
> ERROR: Execution failed for task ':app:processDebugGoogleServices'.
> > No matching client found for package name 'a47514.masterplanner' in ... google-services.json

**Identified cause:** The `applicationId` was changed to `a47514.masterplanner` to match the project package, but the existing `google-services.json` from Firebase was configured for `com.app.masterplanner`.
**Fix:** Reverted `applicationId` to `com.app.masterplanner` to maintain synchronization with the Firebase configuration file.
**Did the AI get the cause right?** Yes.

---

### #p5 — Compile error: Mass unresolved Icon references

- Tool: Android Studio AI
- Date: 2026-06-20

**Prompt:**
> ERROR: ... Unresolved reference 'DirectionsBoat'.
> ERROR: ... Unresolved reference 'VpnKey'.
> ERROR: ... Unresolved reference 'BakeryDining'.

**Identified cause:** The UI code referenced icons that belong to the "Extended" Material Icons set, which is not part of the core Compose Material library.
**Fix:** Added `implementation("androidx.compose.material:material-icons-extended")` to the app's dependencies.
**Did the AI get the cause right?** Yes.

---

### #p6 — Compile error: Invalid `Modifier.padding` overload

- Tool: Android Studio AI
- Date: 2026-06-20

**Prompt:**
> Error: None of the following candidates is applicable: ... No parameter with name 'horizontal' found. No parameter with name 'top' found. ... for the text `padding` on the line `Text(":", ..., modifier = Modifier.padding(horizontal = 16.dp, top = 20.dp))`

**Identified cause:** `Modifier.padding` does not support a combination of `horizontal` and `top` in the same call. It requires either `(horizontal, vertical)` or `(start, top, end, bottom)`.
**Fix:** Changed to `Modifier.padding(start = 16.dp, end = 16.dp, top = 20.dp)`.
**Did the AI get the cause right?** Yes, after a retry.

---

### #p7 — Runtime bug: Roadmaps not saving to Firestore

- Tool: Android Studio AI
- Date: 2026-06-20

**Prompt:**
> Roadmaps are not being saved on Firestore. Pressing the Set Sail button to create a new Roadmap does nothing

**Identified cause:** The `saveRoadmap` function was attempting to set the document but was not ensuring that the `id` field within the `Roadmap` object itself was populated with the new Firestore document ID before writing.
**Fix:** Modified the ViewModel to generate a `docRef` first, then copy that ID into the `Roadmap` object using `.copy(id = docRef.id)` before calling `.set()`.
**Did the AI get the cause right?** Yes.

---

### #p8 — Compile error cascade after drag-and-drop integration

* Tool: ChatGPT (GPT-5.5)
* Date: 2026-06-17

**Prompt:**

> RoadMapEditorScreen now reports:
>
> * Unresolved reference 'RoadMapEditorTopBar'
> * Unresolved reference 'BootyBagDialog'
> * Unresolved reference 'AddDurationDialog'
> * Unresolved reference 'task'
> * Unresolved reference 'EditorTaskCard'
> * Unresolved reference 'TimerBadge'
> * Unresolved reference 'HourPicker'
> * Unresolved reference 'MinutePicker'
> * Unresolved reference 'BootyTaskCard'
> * Unresolved reference 'BootyTaskData'

**Identified cause:** The first actual error was not the missing composables. The file contained an invalid `ReorderableItem` block using `key = task.id` where no `task` variable existed in scope. In addition, the file simultaneously imported `a47514.masterplanner.data.RoadmapItem` and declared a second local `sealed class RoadmapItem`, causing type conflicts. These parser/type errors caused Android Studio to report many later declarations as unresolved.

**Fix:** Replace the invalid `task.id` reference with the actual item key used in the list, verify the correct `ReorderableItem` API for the installed library version, and remove the duplicate `RoadmapItem` definition/import conflict.

**Did the AI get the cause right?** Partially. The explanation correctly identified the undefined `task` reference and duplicate `RoadmapItem` declaration, but the exact reorderable API still needed verification against the project's dependency version.

---

### #p9 — Compile error after introducing drag handles

* Tool: ChatGPT (GPT-5.5)
* Date: 2026-06-17

**Prompt:**

> There seem to be errors with these files after adding drag-and-drop support.

**Identified cause:** Generated code mixed APIs from different versions of `sh.calvin.reorderable`, omitted required imports (`items`, `draggableHandle`), and introduced references that were not valid for the project's dependency version.

**Fix:** Verify the installed reorderable library version, update `ReorderableItem` calls to the correct signature, add missing imports, and replace generated placeholder references with project-specific keys.

**Did the AI get the cause right?** Yes, but additional manual verification against the actual dependency version was required.

---

### #p10 — Compile error: Cannot infer type parameter 'T' and unresolved `RoadmapItem`

* Tool: ChatGPT (GPT-5.5)
* Date: 2026-06-17

**Prompt:**

> RoadMapEditorScreen reports:
>
> * Cannot infer type for type parameter 'T'. Specify it explicitly.
> * Unresolved reference 'RoadmapItem'
>
> Relevant code:
>
> ```kotlin
> val currentItems = remember(currentRoadmap, tasks) {
>     val entries = currentRoadmap?.itemEntries ?: emptyList()
>     if (entries.isNotEmpty()) {
>         entries.map { entry ->
>             if (entry.type == "duration") {
>                 RoadmapItem.Duration(entry.durationLabel) as RoadmapItem
>             } else {
>                 RoadmapItem.Task(...)
>             }
>         }.toMutableStateList()
>     }
> }
> ```

**Identified cause:** `RoadmapItem` was declared later inside `RoadMapEditorScreen`, making it unavailable when referenced by the earlier `remember {}` block. Additionally, the compiler could not infer the generic type of the mapped list because the lambda returned different subclasses and relied on explicit casts.

**Fix:** Move `sealed class RoadmapItem` to file scope (or above its first usage) and explicitly type the mapped collection using `map<RoadmapItem> { ... }`, removing unnecessary casts.

**Did the AI get the cause right?** Yes.

---

### #p11 — Runtime bug: Task Library delete action appears non-functional

* Tool: ChatGPT (GPT-5.5)
* Date: 2026-06-17

**Prompt:**

> Task Library: delete task function not executed

**Identified cause:** The UI callback correctly invoked `deleteLibraryTask(task.id)`, but the most likely failure point was a mismatch between Firestore document IDs and the IDs stored in the loaded `Task` objects. A secondary possibility was stale UI state caused by the snapshot listener repopulating deleted items.

**Fix:** Ensure Firestore document IDs are copied into each loaded `Task` object (`copy(id = doc.id)`), and add temporary logging to confirm whether the deletion callback succeeds or fails before investigating Firestore rules or listener behavior.

**Did the AI get the cause right?** Not fully verified. The diagnosis was plausible but required inspection of the actual Firestore model and listener implementation before confirmation.

---

### #p12 — Firestore writes rejected with PERMISSION_DENIED

* Tool: ChatGPT (GPT-5.5)
* Date: 2026-06-17

**Prompt:**

> Roadmaps and tasks are not being saved. Logcat:
>
> ```
> PERMISSION_DENIED: Missing or insufficient permissions.
> Write failed at users/{uid}/roadmaps/{roadmapId}
> ```

**Identified cause:** The ViewModel save methods were functioning correctly and Firebase Authentication was providing a valid UID. The actual failure originated from Firestore Security Rules rejecting writes to the user-scoped roadmap path.

**Fix:** Review Firestore Security Rules, temporarily allow writes for verification, then create authenticated user-scoped rules matching:
>
> ```
> users/{userId}/roadmaps/{roadmapId}
> users/{userId}/roadmaps/{roadmapId}/tasks/{taskId}
> users/{userId}/task_library/{taskId}
> ```

**Did the AI get the cause right?** Yes. The Logcat output explicitly confirmed Security Rules as the root cause.

---

### #p13 — Firestore permission denied
- Tool: ChatGPT (GPT‑5.5)
- Date: 2026-05-24

**Prompt:**
> PERMISSION_DENIED: Missing or insufficient permissions when saving roadmaps.

**Identified cause:** Firestore Security Rules blocked authenticated writes.

**Fix:** Updated rules to allow user-scoped document access.

**Did the AI get the cause right?** Yes.

---

### #p14 — Firestore timestamp deserialization
- Tool: ChatGPT (GPT‑5.5)
- Date: 2026-06-09

**Prompt:**
> Timestamp sorting fails while waiting for @ServerTimestamp synchronization.

**Identified cause:** Null timestamps during synchronization.

**Fix:** Defensive timestamp fallback using timestamp?.seconds ?: 0L.

**Did the AI get the cause right?** Yes.

---

### #p15 — Navigation lambda mismatch
- Tool: Claude (Sonnet 4.6)
- Date: 2026-06-09

**Identified cause:** Inconsistent navigation callback signatures.

**Fix:** Standardized on (Screen, String?) -> Unit.

**Did the AI get the cause right?** Yes.

---