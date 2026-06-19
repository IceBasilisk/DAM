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

### #p16 — Three independent bugs: roadmap task leakage, wrong post-create navigation, mismatched icon

- Tool: Claude (Sonnet 4.6)
- Date: 2026-06-18
- Context given to the AI: `masterplanner.zip` (full source)

**Prompt:**
> Let's say I have Roadmap 1 and Roadmap 2. Roadmap 1 has two task. Roadmap 2 was just created, so it's supposed to be empty. The Roadmap 2 has already the same two tasks as Roadmap 1.
> In the Booty Bag (the task creation dialog) that shows up when I try making a task inside a Roadmap, the user is returned to the Task Library, not the Roadmap editor of the current Roadmap.
> The second icon for the Roadmap creation dialog is return a flag, not the pictured map.

**Identified cause:** Three unrelated root causes. (1) `RoadmapViewModel.listenToTasks(roadmapId)` attached a new Firestore snapshot listener on every call without detaching the previous one or clearing `_tasks`/`_currentRoadmap` first, so a newly opened roadmap's `currentItems` fallback briefly (or persistently, depending on listener timing) read the *previous* roadmap's tasks. (2) `MainActivity`'s `onCreateTask` callback passed to `RoadMapEditorScreen` hardcoded `previousScreen = Screen.TaskLibrary` regardless of where it was triggered from, so `CreateTaskScreen`'s `onBack = { currentScreen = previousScreen }` always landed on the Library. (3) `CreateRoadmapDialog` had two separately-ordered lists — one `List<ImageVector>` for rendering (`Map` in position 2) and one `List<String>` for saving (`"Flag"` in position 2) — that had drifted out of sync; compounding this, `Utility.iconFromName` had no `"Map"` case at all.

**Fix:** Added listener-registration tracking (`roadmapDocListener`/`tasksListener`/`listeningToRoadmapId`) to `RoadmapViewModel` so `listenToTasks()` detaches old listeners and synchronously clears state before attaching new ones; added `stopListeningToTasks()` wired via `DisposableEffect` in `MainActivity`. Changed the editor's `onCreateTask` to set `previousScreen = Screen.RoadMapEditor`. Merged `CreateRoadmapDialog`'s two icon lists into a single `List<Pair<String, ImageVector>>` used for both display and the saved name, and added the missing `"Map"` case to `iconFromName`.

**Did the AI get the cause right?** Yes, confirmed by tracing each symptom to its exact code location before patching (listener lifecycle, hardcoded `previousScreen`, and the two divergent icon/name lists) rather than guessing at a single shared cause.

---

### #p17 — Regression: AI suggestion button visible but non-functional after a previous fix

- Tool: Claude (Sonnet 4.6)
- Date: 2026-06-18
- Context given to the AI: `CreateTaskScreen.kt`, `MainActivity.kt`, `RoadmapViewModel.kt` (post-#p16 state)

**Prompt:**
> The Ai suggestion button is there, but it no longer works.

**Identified cause:** A self-inflicted regression from the #p16 listener-lifecycle fix. `CreateTaskScreen`'s `effectiveTitle` depended on `roadmapViewModel.currentRoadmap.value?.title`, but `MainActivity` never actually passed the `roadmapTitle` parameter the screen also accepted. The newly added `stopListeningToTasks()` resets `_currentRoadmap` to `null` the moment `RoadMapEditorScreen` leaves composition — which happens immediately on navigating to `CreateTaskScreen` from the Booty Bag. By the time `CreateTaskScreen` rendered, `currentRoadmap` was already `null`, so `effectiveTitle` was `""` and `fetchTaskSuggestions("")` silently no-op'd (it returns early on a blank title).

**Fix:** Added a `currentRoadmapTitle` state holder in `MainActivity` that captures `currentRoadmap?.title` via a `LaunchedEffect` while the editor is open, independent of the ViewModel listener's lifecycle, and passed it into `CreateTaskScreen`'s existing-but-unused `roadmapTitle` parameter (which already took priority over `currentRoadmap?.title` in `effectiveTitle`'s `when` block).

**Did the AI get the cause right?** Yes — correctly traced the failure to the interaction between the prior turn's own listener-cleanup fix and this screen's title-resolution logic, rather than treating it as an unrelated new bug.

---

### #p18 — Freemium task limit enforced for roadmap creation but not for task creation

- Tool: Claude (Sonnet 4.6)
- Date: 2026-06-19
- Context given to the AI: `RoadMapEditorScreen.kt`, `CreateTaskScreen.kt`, `PremiumManager.kt`

**Prompt:**
> Freemium: Number of tasks are not being limited on Free Trial (in free trial mode, if the user creates a fourth roadmap, he gets blocked by the freemium page, but the same won't happen with 7th task)

**Identified cause:** The task-limit check (`currentItems.count { it is RoadmapItem.Task } >= PremiumManager.taskLimit`) only existed at two of the three places a task can be added: the "+" FAB and the Booty Bag's "Import Selected" button, both inside `RoadMapEditorScreen`. The Booty Bag's "Create New Task" link bypasses both of those checks entirely — it navigates straight to `CreateTaskScreen`, whose save handler had no limit check of its own and would write the task and append it to the roadmap regardless of count.

**Fix:** Added the same check directly in `CreateTaskScreen`'s save handler: `currentTaskCount >= PremiumManager.taskLimit` (computed with the same itemEntries-with-fallback-to-tasks logic used in the editor) routes to `Screen.Freemium` via `onNavigate` instead of saving. Placing the gate at the actual write site, rather than only at upstream entry points, covers every current and future path that reaches this screen.

**Did the AI get the cause right?** Yes — the FAB/Booty Bag checks were real and correctly implemented for their own paths; the bug was a genuinely missing check at a third, separate entry point rather than a flaw in the existing two.

---
### #p19 — Free task cap bypassed by task-creation entry buttons

- Tool: ChatGPT (GPT-5.5 Thinking)
- Date: 2026-06-19
- Context given to the AI: `MainActivity.kt`, `TaskLibraryScreen.kt`, `RoadMapEditorScreen.kt`, `CreateTaskScreen.kt`, `PremiumManager.kt`, `RoadmapViewModel.kt`

**User report:**
> The number of tasks based on the free trial mode are not being limited. What I mean is, when the number of total tasks is 7, the freemium screen is not showing whenever the user attempts to create a new task via RoadmapEditor screen create new task button or via the TaskLibraryScreen create new task button.

**Identified cause:** The prior fix only blocked saves inside `CreateTaskScreen`, and the check was scoped to roadmap context (`isInsideRoadmap && currentTaskCount >= PremiumManager.taskLimit`). That left two gaps: the Task Library path had no creation-entry guard at all, and the Roadmap Editor's Booty Bag "Create New Task" link could still navigate into task creation even when the global total task count was already at the free limit.

**Fix:** Added an app-level `hasReachedTaskLimit` in `MainActivity` using `roadmapViewModel.libraryTasks.size`, `PremiumManager.isPremium`, and `PremiumManager.taskLimit`. The Task Library and Roadmap Editor `onCreateTask` callbacks now send free users to `Screen.Freemium` instead of `Screen.CreateTask` when the total task cap has been reached. Replaced `CreateTaskScreen`'s per-roadmap save guard with the same global library-count guard so the write path remains protected.

**Did the AI get the cause right?** Yes — the missing check was at the navigation entry points, and the remaining save guard needed to use total library task count rather than only roadmap-local count.

---
