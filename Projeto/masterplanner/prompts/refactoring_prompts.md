# Prompts — Refactoring

Prompts to improve existing code **without changing behavior**.

## Good practices

- Require: behavior preserved, tests still green, small and cohesive change.
- One refactor at a time (do not mix with a feature — see the commit convention).
- Point to the convention/architecture to preserve (`AGENTS.md`, `docs/06`).
- Ask the AI to explain the *why* of the refactor (candidate for an ADR if structural).

---

### #p1 — (example) Extract logic from the ViewModel

- Tool: <...>
- Date: 2026-05-19
- Context: `docs/06`, `docs/08`, target file

**Prompt:**
> Extract the <X> logic from `XxxViewModel` into a UseCase in `domain/usecase`,
> keeping the behavior and the tests passing. Do not change the screen's public API.
> Explain why it improves testability. Small change, only this refactor.

**Result:** <fill in>
**Did the tests stay green?** <yes/no>
**Assessment:** <...>

---

<!-- new entries below -->

### #p2 — Centralize hex-color-to-Compose-Color conversion and icon-name-to-ImageVector mapping

- Tool: Claude (Sonnet 4.6)
- Date: 2026-06-15
- Context: `TaskLibraryScreen.kt`, `Utility.kt`

**Prompt:**
> In "Box( modifier = Modifier .size(60.dp) .clip(RoundedCornerShape(16.dp)) .background(Color(task.colorHex)) .border(2.dp, lighterBrown, RoundedCornerShape(16.dp)), contentAlignment = Alignment.Center ) { Icon(task.iconName, contentDescription = null, tint = brown, modifier = Modifier.size(32.dp)) }", I need to use the HEX color for background, and the Icon is not matching any class.

**Result:** Two small, isolated conversions rather than a structural change: `Color(android.graphics.Color.parseColor(task.colorHex))` replacing the invalid direct `Color(String)` call (with a `try/catch` fallback to a safe default), and a new `iconFromName(name: String): ImageVector` mapping function added to `Utility.kt` so the `String → ImageVector` lookup lives in one place rather than being re-implemented per card. The same `iconFromName()` was later reused unchanged in `RoadMapEditorScreen`'s `EditorTaskCard`, `BootyBagDialog`'s `BootyTaskCard`, and `MainMenuScreen`'s `RoadmapCard` — confirming the extraction paid off across the codebase rather than just the one call site.
**Did the tests stay green?** No automated test suite exists for this project; correctness was verified by visual inspection of rendered icons/colors across all reuse sites after the change.
**Assessment:** Accepted — turning a one-off inline fix into a shared utility function avoided three near-duplicate `when` blocks appearing later in the project.

---

### #p3 — Unify the navigation callback signature across all screens instead of patching each call site individually

- Tool: Claude (Sonnet 4.6)
- Date: 2026-06-15
- Context: `MainActivity.kt`, all screen composables taking an `onNavigate` parameter

**Prompt:**
> (raised across two related debugging turns, then explicitly proposed as the long-term fix)
> The cleanest permanent fix is to change the navigate lambda itself so both signature styles unify — pass `navigate` everywhere instead of wrapping it per-screen.

**Result:** Replaced the original `val navigate: (Screen) -> Unit` plus several ad hoc `{ screen -> onNavigate(screen, null) }` wrapper closures scattered across `MainMenuScreen`, `RoadMapEditorScreen`, `TaskLibraryScreen`, and `CreateTaskScreen` with a single unified lambda: `val navigate: (Screen, String?) -> Unit = { screen, roadmapId -> currentRoadmapId = roadmapId ?: currentRoadmapId; currentScreen = screen }`. Every screen's `onNavigate` parameter was changed to accept this one signature, removing the need for any per-call-site wrapping.
**Did the tests stay green?** No automated test suite; verified by confirming every navigation path (Main Menu ↔ Roadmap Editor ↔ Task Library ↔ Create Task ↔ Freemium) still landed on the correct screen with the correct `roadmapId` carried over.
**Assessment:** Accepted — eliminated a class of recurring type-mismatch compiler errors (see `debugging_prompts.md` #p2) at the root rather than continuing to patch each new screen's call site as it was added.

---

### #p4 — Extract a reusable drag-list state helper instead of duplicating reorder boilerplate per screen

- Tool: Claude (Sonnet 4.6)
- Date: 2026-06-15
- Context: `MainMenuScreen.kt`, `RoadMapEditorScreen.kt`, `TaskLibraryScreen.kt`

**Prompt:**
> Using the implementation on MainMenuScreen, give me the code to make the tasks and time durations draggable as well, updating the corresponding database.
> [provided a `rememberDragListState()` helper as a candidate extraction]

**Result:** A `rememberDragListState(lazyListState, onMove): Pair<LazyListState, ReorderableLazyListState>` helper was proposed to wrap the repeated `rememberLazyListState()` + `rememberReorderableLazyListState()` pairing used identically across all three screens. In the actual implementation that followed, the screens continued to call `rememberLazyListState()`/`rememberReorderableLazyListState()` directly inline (matching the already-working `MainMenuScreen` reference exactly, to minimize risk of introducing a new untested abstraction across three screens at once), but the helper remains a documented candidate for a follow-up consolidation pass.
**Did the tests stay green?** No automated test suite; the inline approach actually taken was verified screen-by-screen against the working `MainMenuScreen` reference.
**Assessment:** Edited — the proposed helper extraction was not adopted in the final code (judged as premature given three call sites with screen-specific quirks, e.g. Task Library's search-index offset), but is recorded here as a legitimate refactor candidate rather than discarded silently.

---

### #p5 — Disable drag-and-drop during active search instead of letting index math silently corrupt order

- Tool: Claude (Sonnet 4.6)
- Date: 2026-06-15
- Context: `TaskLibraryScreen.kt`

**Prompt:**
> (raised as part of the same TaskLibraryScreen drag-and-drop implementation turn)
> Now do the exact same drag and drop system on RoadMapEditorScreen and TaskLibraryScreen — Remember, the updates must also be done on the database.

**Result:** While implementing the Task Library drag feature, a structural risk was identified and refactored before it could surface as a bug: dragging while a search filter is active would compute `onMove` index offsets against the *filtered* list position, while the underlying `taskList` mutation needs the *unfiltered* index — silently producing wrong reorders. Rather than attempting to reconcile filtered/unfiltered indices, drag was deliberately disabled via an `isDragEnabled = searchQuery.isBlank()` guard passed into `ReorderableItem(enabled = isDragEnabled)`, with the drag handle visually hidden in that state.
**Did the tests stay green?** No automated test suite; verified manually by confirming drag attempts during an active search produce no movement and no Firestore write, while drag with an empty search field reorders correctly.
**Assessment:** Accepted — choosing to disable a feature combination cleanly is preferable to shipping index-math that could silently corrupt persisted order under a specific user flow.

---

### #p6 — Reuse MainMenuScreen drag-and-drop architecture across additional screens

* Tool: ChatGPT (GPT-5.5)
* Date: 2026-06-17
* Context: `MainMenuScreen.kt`, `TaskLibraryScreen.kt`, `RoadMapEditorScreen.kt`

**Prompt:**

> Using the MainMenuScreen logic of drag and drop, do the same logic for TaskLibraryScreen and RoadmapEditorScreen to allow drag and drop of tasks and time durations.

**Result:** Proposed extracting the same reorderable-list pattern already used in `MainMenuScreen` and applying it consistently across the Task Library and Roadmap Editor. The suggested structure reused existing ViewModel persistence methods (`reorderLibraryTasks()` and `saveRoadmapItems()`), reducing the need for screen-specific ordering implementations and encouraging a shared interaction model throughout the application.

**Did the tests stay green?** No automated test suite exists; implementation required manual verification and subsequent debugging due to library-version differences.

**Assessment:** Edited — the architectural direction was retained, but the generated implementation required manual adaptation and debugging before integration.

---

### #p7 — Move RoadmapItem to a shared top-level model and remove redundant casts

- Tool: ChatGPT (GPT-5.5)
- Date: 2026-06-17
- Context: `RoadMapEditorScreen.kt`

**Prompt:**
> RoadMapEditorScreen reports:
> - Cannot infer type for type parameter 'T'
> - Unresolved reference 'RoadmapItem'
>
> How should the structure be changed?

**Result:** Recommended moving `sealed class RoadmapItem` from inside `RoadMapEditorScreen` to a top-level declaration (or dedicated file), allowing all composables and state initialization code to reference the same model. Also removed unnecessary `as RoadmapItem` casts and replaced them with explicit generic typing (`map<RoadmapItem> { ... }`).

**Did the tests stay green?** No automated test suite exists; correctness verified through successful compilation and runtime validation of roadmap loading and drag-and-drop functionality.

**Assessment:** Accepted — improved model visibility, reduced casting, and resolved both scope and type-inference issues without changing application behavior.

---

### #p8 — Remove redundant inner border from segmented selector component

* Tool: ChatGPT (GPT-5.5)
* Date: 2026-06-17
* Context: `AddDurationDialog`

**Prompt:**

> Add Duration: "before task" and "after task", when selecting the correspondent tab, the borders of the colored rectangle are shown outside the main border

**Result:** Proposed simplifying the segmented-control implementation by keeping only the outer container border and removing the selected-tab border. The selected state is represented solely through background color, reducing duplicated decoration logic and preventing border-overflow artifacts.

**Did the tests stay green?** No automated test suite exists; verified visually in the Compose preview/runtime UI.

**Assessment:** Accepted — simpler styling hierarchy and fewer overlapping borders while preserving behavior.

---


### #p9 — Separate current project structure from proposed clean architecture layout

- Tool: ChatGPT (GPT-5.5)
- Date: 2026-06-17
- Context: Current package layout, `data/`, `ui/`, `RoadmapViewModel`, Compose screens, Firebase/Gemini services

**Prompt:**
> More like this:
> ```
> com.example.app
> ├── di/
> ├── data/
> ├── domain/
> └── ui/
> ```

**Result:** Reframed the project diagram from a flow-style architecture chart into a package-tree diagram. The response first documented the current structure (`MainActivity.kt`, `data/`, `ui/`, `theme/`, test folders), then suggested a more maintainable organization by moving models under `data/model`, services under `data/service`, billing under `data/billing`, reusable UI under `ui/components`, screens under feature folders, and `MusicManager` under `media`.

**Did the tests stay green?** No code was changed; this was a documentation/refactoring recommendation only.

**Assessment:** Accepted — useful as a low-risk refactor plan because it clearly distinguishes current implementation from a possible future package reorganization.

---

### #p10 — Avoid inventing unavailable layers when documenting DTOs and state

- Tool: ChatGPT (GPT-5.5)
- Date: 2026-06-17
- Context: Project documentation templates for DTOs, state management, APIs, security, and testing

**Prompt:**
> Fill in this text with info from the project.

**Result:** For sections whose template assumed a layered architecture with DTOs, Room, DataStore, repositories, use cases, `StateFlow`, and immutable `UiState`, the generated documentation explicitly stated that these layers were not identified in the current project structure. It then documented the closest actual equivalents: `GeminiService` for AI network interaction, Firebase Authentication/Firestore for backend state, `NetworkMonitor` for connectivity, `PremiumManager` for freemium logic, and `RoadmapViewModel` as the main state holder.

**Did the tests stay green?** No code was changed; documentation-only update.

**Assessment:** Accepted — prevented the documentation from describing an idealized architecture that the project does not currently implement.

---

### #p11 — Keep Firestore ordering while safely handling pending ServerTimestamp values

- Tool: ChatGPT (GPT-5.5)
- Date: 2026-06-17
- Context: `RoadmapViewModel.kt`, Firestore listeners, `@ServerTimestamp`

**Prompt:**
> Update the orderBy("timestamp", ...) queries in RoadmapViewModel to handle the nullable timestamp safely.

**Result:** Recommended preserving Firestore's existing server-side ordering while adding a client-side fallback:
>
> ```kotlin
> sortedByDescending { it.timestamp?.seconds ?: 0L }
> ```
>
> and
>
> ```kotlin
> sortedBy { it.timestamp?.seconds ?: 0L }
> ```
>
> after deserialization. This prevents temporary null timestamps generated by pending `@ServerTimestamp` writes from causing ordering issues.

**Did the tests stay green?** No automated test suite exists; verified through successful compilation and runtime Firestore synchronization.

**Assessment:** Accepted — behavior remains unchanged once timestamps are populated while making pending-write states safer.

---

### #p12 — Move Firestore listener initialization to ViewModel
- Tool: Claude (Sonnet 4.6)
- Date: 2026-06-15

**Prompt:**
> Remove duplicate listener registrations across screens without changing behavior.

**Result:** Listener moved into ViewModel init block and screen-level registrations removed.

**Assessment:** Accepted.

### #p13 — CompositionLocal theme migration
- Tool: Claude (Sonnet 4.6)
- Date: 2026-06-15

**Prompt:**
> Eliminate repeated colorResource lookups across composables.

**Result:** Introduced AppColors and LocalAppColors CompositionLocal.

**Assessment:** Accepted.

### #p14 — Extract RoadmapItem model and shared utilities
- Tool: ChatGPT (GPT‑5.5)
- Date: 2026-06-04

**Result:** Shared declarations moved to reusable top-level structures and utility helpers.

**Assessment:** Accepted.

---

### #p15 — Widen `saveTask`'s callback to return the saved Task instead of a bare Boolean

- Tool: Claude (Sonnet 4.6)
- Date: 2026-06-19
- Context: `RoadmapViewModel.kt`, `CreateTaskScreen.kt` (sole call site)

**Prompt:**
> (raised as a supporting change while fixing the "tasks created in a roadmap aren't added automatically" bug — see `architecture_prompts.md` #p32)
> The appended `itemEntries` entry needs the task's real Firestore-generated id, which the boolean-only callback was discarding.

**Result:** Changed `saveTask(roadmapId, task, onResult: (Boolean) -> Unit)` to `saveTask(roadmapId, task, onResult: (Task?) -> Unit)`, returning the `taskToSave` (with its `docRef.id` already copied in) on success and `null` on failure, instead of `true`/`false`. Checked for other call sites first — `CreateTaskScreen.kt` was the only one — so the signature could be changed directly rather than needing an overload or a deprecation shim.

**Did the tests stay green?** No automated test suite exists; verified by confirming the appended `RoadmapItemEntry.taskId` was non-empty after creating a task from inside a roadmap, and that deleting that task afterward from the editor correctly removed the underlying Firestore document (which depends on `taskId` being populated).

**Assessment:** Accepted — a callback that only reports success/failure is insufficient once a caller needs data generated during the write (the document id); returning the saved entity itself is more useful than adding a second out-parameter or a follow-up read.

---
### #p18 — Replace duplicated task-limit checks with a shared total-count gate

- Tool: ChatGPT (GPT-5.5 Thinking)
- Date: 2026-06-19
- Context: `MainActivity.kt`, `CreateTaskScreen.kt`, `PremiumManager.kt`, `RoadmapViewModel.kt`

**Prompt:**
> The number of tasks based on the free trial mode are not being limited. What I mean is, when the number of total tasks is 7, the freemium screen is not showing whenever the user attempts to create a new task via RoadmapEditor screen create new task button or via the TaskLibraryScreen create new task button.

**Result:** Moved the total-task limit decision into `MainActivity`, where both task-creation navigation paths are wired, by observing the global `libraryTasks` list and `PremiumManager.isPremium`. Removed `CreateTaskScreen`'s previous roadmap-local `currentTaskCount` calculation and replaced it with a single `hasReachedTaskLimit` value derived from `libraryTasks.size`. This keeps the entry gate and the save gate aligned with the same source of truth.

**Did the tests stay green?** Automated compilation could not be completed in the sandbox because Gradle attempted to download its distribution from `services.gradle.org`, but network access was unavailable. Static inspection confirmed the changed symbols are imported through existing `androidx.compose.runtime.*` wildcard imports and that removed variables are no longer referenced.

---
