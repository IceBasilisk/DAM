# Prompts — Jetpack Compose / UI

Format: see `prompts/README.md`. Record UI prompts (Composables, state, theme, navigation).

## Good prompt practices for Compose

- State the `UiState` and the `Screen(state, onAction)` contract — see `docs/08`.
- Ask for *stateless composables* + preview; state in the ViewModel.
- Mention Material 3 and the states (loading/empty/error/offline/content).
- Point the tool to `docs/04_screens_and_ui.md` and to the existing component to reuse.

---

### #p1 — (example) Stateless list screen

- Tool: Android Studio AI
- Date: 2026-05-19
- Context given to the AI: `docs/04`, `docs/08`, `ui/components/`, `SampleScreen.kt`

**Prompt:**
> Create a Composable `XxxScreen(state: XxxUiState, onAction: (XxxAction) -> Unit)` in
> Material 3, stateless, with loading/empty/error/content states, reusing
> `LoadingIndicator` and `ErrorView` from `ui/components`. Include `@Preview`. Follow the
> pattern of `SampleScreen.kt`. Do not access repositories directly.

**Result:** Created skeleton with state-driven content switching.
**Assessment:** Accepted.

---

### #p2 — Display real roadmap title in editor header and persist task/duration order

- Tool: Claude (Sonnet 4.6)
- Date: 2026-06-15
- Context given to the AI: `masterplanner.zip` (full source) — `RoadMapEditorScreen.kt`, `MainActivity.kt`

**Prompt:**
> Change RoadmapEditor screen title from "Morning Bakery Routine" to the title of the roadmap at hand. How to save the times and order of tasks in each roadmap

**Result:** Replaced the hardcoded `stringResource(R.string.roadmap_title_test1)` with a `currentRoadmap` StateFlow collected from a second `addSnapshotListener` on the roadmap document. For ordering, introduced a `RoadmapItemEntry` data class embedded as `Roadmap.itemEntries: List<RoadmapItemEntry>`, with `currentItems` in the composable loading from `itemEntries` on open and falling back to timestamp-sorted tasks when empty. `saveRoadmapItems()` added to persist the full list (tasks + duration badges) on every mutation.

**Assessment:** Accepted — became the standard pattern for representing ordered mixed-type lists (`RoadmapItem.Task` / `RoadmapItem.Duration`) in a single Firestore field for the rest of the project.

---

### #p3 — Replace generic top bar with a dedicated save-on-back editor top bar

- Tool: Claude (Sonnet 4.6)
- Date: 2026-06-15
- Context given to the AI: `RoadMapEditorScreen.kt`

**Prompt:**
> It's best to make a Top Bar for RoadMapEditorScreen, on that case.

**Result:** New `RoadMapEditorTopBar` composable: a back `IconButton` whose `onClick` first calls `saveRoadmapItems()` then `onBack()`, a centered `Surface` badge showing the live roadmap title with `TextOverflow.Ellipsis`, and a balancing `Spacer` to keep the title visually centered. Replaced the generic `MasterPlannerTopBar()` call and the old hardcoded header `Surface`/`Text` block beneath it.

**Assessment:** Accepted — cleaner separation of concerns; save-before-navigate logic moved out of the screen body and into the top bar's single responsibility.

---

### #p4 — Make duration badges tappable-to-delete

- Tool: Claude (Sonnet 4.6)
- Date: 2026-06-15
- Context given to the AI: `RoadMapEditorScreen.kt`

**Prompt:**
> Make clicking on top of the duration bubbles within roadmap editors delete those duration bubbles from the roadmap and from the database

**Result:** Added `onDelete: () -> Unit = {}` to `TimerBadge`, wrapped the `Surface` in `Modifier.clickable { onDelete() }`, and added a small `Close` icon to signal the affordance. The `is RoadmapItem.Duration ->` branch in the items loop now passes a lambda that removes the item from `currentItems` and immediately calls `saveRoadmapItems()`.

**Assessment:** Accepted — minimal, self-contained change reusing the existing `currentItems`/`saveRoadmapItems` plumbing.

---

### #p5 — Replace static time picker with a native rotating wheel

- Tool: Claude (Sonnet 4.6)
- Date: 2026-06-15
- Context given to the AI: `RoadMapEditorScreen.kt` (`AddDurationDialog`, `TimeReel`)

**Prompt:**
> RoadmapEditor, the duration dialog. I want the dialog to have a rotating hours and minutes wheel, like most timers on android apps.

**Result:** Replaced the custom `TimeReel` text-based picker with `HourPicker`/`MinutePicker` composables wrapping Android's native `NumberPicker` via `AndroidView`, with `wrapSelectorWheel = true` for the rotating effect, `minValue`/`maxValue` set per field, and `setOnValueChangedListener` bridging back into Compose state.

**Assessment:** Accepted — native `NumberPicker` gives the expected platform rotating-wheel feel with far less custom drawing code than the original `TimeReel` approach.

---

### #p6 — Add search/filter to Task Library matching an existing dialog's pattern

- Tool: Claude (Sonnet 4.6)
- Date: 2026-06-15
- Context given to the AI: `TaskLibraryScreen.kt`, `RoadMapEditorScreen.kt` (`BootyBagDialog` as the reference)

**Prompt:**
> On Task Library, the search system should have a filtering system similar to the one on the BootyBag.

**Result:** Added `searchQuery` state and a `filteredTasks = remember(libraryTasks, searchQuery) { ... }` derived list using `.contains(searchQuery, ignoreCase = true)`, mirroring the exact pattern already used inside `BootyBagDialog`. Wired the previously non-functional `OutlinedTextField` (`value = ""`, no `onValueChange`) to the new state, and added a result-count label and empty-state messaging.

**Assessment:** Accepted — direct reuse of a pattern already proven elsewhere in the same file tree kept the implementation consistent.

---

### #p7 — Add per-item edit/delete menus to Task Library and a cascading delete-confirmation dialog to Main Menu

- Tool: Claude (Sonnet 4.6)
- Date: 2026-06-15
- Context given to the AI: `TaskLibraryScreen.kt`, `MainMenuScreen.kt`, `RoadMapEditorScreen.kt` (`EditorTaskCard`'s pencil menu as the reference)

**Prompt:**
> On Task Library, similar to RoadmapEditor, had a small pencil icon to every task, with the option to delete it, removing it from the database. Do the same for the roadmaps in the main menu, but every time a roadmap is deleted, make a popup message asking if the user would like to delete the tasks as well. If yes, delete them all. If not, delete only the roadmap.

**Result:** Added a `Box { IconButton(Edit) + DropdownMenu("Delete Task") }` pattern to `LibraryTaskCard`, matching `EditorTaskCard`'s existing pencil menu exactly. For `RoadmapCard`, added the same pencil menu plus a separate `showDeleteDialog` boolean driving an `AlertDialog` with three explicit actions — "Delete All" (calls `deleteRoadmapWithTasks(true)`), "Keep Tasks" (`deleteRoadmapWithTasks(false)`), and "Cancel".

**Assessment:** Accepted — reusing the established pencil-menu visual pattern kept the three screens visually and behaviorally consistent.

---

### #p8 — Add a custom widget tool call for an existing screen layout question

- Tool: Claude (Sonnet 4.6)
- Date: 2026-06-15
- Context given to the AI: Screenshot of the running `MainMenuScreen`, `MainMenuScreen.kt` source

**Prompt:**
> Does this look right? The top bar area is too big and empty

**Result:** Diagnosed two compounding issues from the screenshot: (1) `.padding(innerPadding)` was applied to both the outer `Column` and again to the inner `LazyColumn`, doubling the Scaffold's top inset; (2) `MasterPlannerTopBar`'s `Row` used a flat `padding(16.dp)` on top of `statusBarsPadding()`, adding more vertical space than needed. Fix: removed the duplicate `innerPadding` from the `LazyColumn`, and reduced the top bar's padding to `padding(horizontal = 16.dp, vertical = 8.dp)`.

**Assessment:** Accepted — confirmed visually correct from a follow-up screenshot; both fixes were independently necessary, removing either alone would have left a residual gap.

---

### #p9 — Add a Composable AI "suggest names" affordance with multiple visual states

- Tool: Claude (Sonnet 4.6)
- Date: 2026-06-15
- Context given to the AI: `CreateTaskScreen.kt`, `RoadmapViewModel.kt`

**Prompt:**
> Instead of ClaudeService, can it use the Gemini-App services from the SimpleAICalls folder?
>
> (related follow-up) Whenever a task is created outside of a roadmap (example, on the task library) or it can't suggest a proper task name, make it give a message like "The Oracle was unable to suggest task names" or "No suggestions available"

**Result:** Added a single `Box`/`Surface` "Suggest Task Names" affordance to `CreateTaskScreen` driven by a `when` over four UI states collected from the ViewModel: not-yet-tried (default button), `isSuggesting` (spinner + "Consulting the oracle…"), `hasSuggestionBeenAttempted && suggestedNames.isEmpty()` ("The Oracle was unable to suggest task names."), and `suggestedNames.isNotEmpty()` (tappable chip list). The whole block is conditionally rendered only when `roadmapId != "library" && roadmapTitle.isNotBlank()`, hiding it entirely in library-only task creation.

**Assessment:** Accepted — the four-state `when` block kept the composable's logic readable despite covering loading, success, empty, and hidden cases in one place.

---

### #p10 — Refactor Login/Signup to Compose

- Tool: Android Studio AI
- Date: 2026-06-20
- Context given to the AI: `LoginActivity.kt`, `LoginScreen.kt`, `CreateAccountScreen.kt`

**Prompt:**
> Alter main activity so that: The current code on LoginActivity is based on the code of another project that allows the usage of Firebase login. Keeping in mind the files LoginScreen, Create AccountScreen, and the MainActivity, alter the code to allow Login and Signup

**Result:** Converted legacy Activity logic to Compose-based UI. Handled the transition from static screens to functional Firebase integration using state callbacks.

**Assessment:** Accepted.

---

### #p11 — Unified Navigation Component (Bottom Bar)

- Tool: Android Studio AI
- Date: 2026-06-20
- Context given to the AI: `MainMenuScreen.kt`, `TaskLibraryScreen.kt`, `RoadMapEditorScreen.kt`, `CreateTaskScreen.kt`, `CommonUI.kt`

**Prompt:**
> Alter the Bottom Bar on CreateTaskScreen, MainMenuScreen, RoadMapEditorScreen and TaskLibraryScreen. They should all share the same bottom bar, except: There shouldn't be a Settings button; ROADMAPS button should lead to MainMenuScreen (only button highlighted when on said page); LIBRARY button should lead to TaskLibraryScreen (only button highlighted when on said page); CreateTaskScreen and RoadMapEditorScreen show the bottom bar, but have no button highlighted.

**Result:** Created `MasterPlannerBottomBar` in `CommonUI.kt` with conditional highlighting logic based on the current `Screen` state.

**Assessment:** Accepted — significantly improved code reuse and visual consistency.

---

### #p12 — Native Duration Wheel Picker

- Tool: Android Studio AI
- Date: 2026-06-20
- Context given to the AI: `RoadMapEditorScreen.kt`, `AddDurationDialog`

**Prompt:**
> RoadmapEditor, the duration dialog. I want the dialog to have a rotating hours and minutes wheel, like most timers on android apps.

**Result:** Integrated `AndroidView` to host native `NumberPicker` widgets within the Compose dialog, providing the requested "rotating wheel" UX.

**Assessment:** Accepted — correctly bridged the gap where Compose lacks a native wheel picker.

---

### #p13 — Task Library Search and Cascade Deletion

- Tool: Android Studio AI
- Date: 2026-06-20
- Context given to the AI: `TaskLibraryScreen.kt`, `MainMenuScreen.kt`

**Prompt:**
> On Task Library, the search system should have a filtering system similar to the one on the BootyBag. On Task Library, similar to RoadmapEditor, had a small pencil icon to every task, with the option to delete it, removing it from the database. Do the same for the roadmaps in the main menu, but every time a roadmap is deleted, make a popup message asking if the user would like to delete the tasks as well. If yes, delete them all. If not, delete only the roadmap.

**Result:** Implemented `searchQuery` filtering for the library and added `AlertDialog` confirmation logic for roadmap deletion to handle associated tasks.

**Assessment:** Accepted.

---

### #p14 — Add drag-and-drop reordering to Task Library and Roadmap Editor

* Tool: ChatGPT (GPT-5.5)
* Date: 2026-06-17
* Context given to the AI: `MainMenuScreen.kt`, `TaskLibraryScreen.kt`, `RoadMapEditorScreen.kt`, existing use of `sh.calvin.reorderable`

**Prompt:**

> Using the MainMenuScreen logic of drag and drop, do the same logic for TaskLibraryScreen and RoadmapEditorScreen to allow drag and drop of tasks and time durations.

**Result:** Proposed reuse of the existing reorderable infrastructure by introducing `rememberReorderableLazyListState`, `ReorderableItem`, drag handles, and persistence hooks. Suggested synchronizing a mutable list with the ViewModel data source, persisting task order through `reorderLibraryTasks()` and roadmap order through `saveRoadmapItems()`, and enabling reordering for both `RoadmapItem.Task` and `RoadmapItem.Duration`.

**Assessment:** Edited — the overall approach was adopted, but several generated snippets required manual correction to match the installed version of the reorderable library and the project's existing model structure.

---

### #p15 — Enable drag-and-drop reordering for roadmap tasks and duration badges

* Tool: ChatGPT (GPT-5.5)
* Date: 2026-06-17
* Context given to the AI: `MainMenuScreen.kt`, `RoadMapEditorScreen.kt`, existing implementation using `sh.calvin.reorderable`

**Prompt:**

> Using the MainMenuScreen logic of drag and drop, do the same logic for TaskLibraryScreen and RoadMapEditorScreen to allow drag and drop of tasks and time durations.

**Result:** Added a reorderable `LazyColumn` architecture using `rememberReorderableLazyListState`, `ReorderableItem`, and drag handles attached to task cards and duration badges. Reordering updates a mutable list representing the roadmap contents and persists changes using `saveRoadmapItems()` when dragging ends.

**Assessment:** Edited — the proposed UI structure was adopted, but required additional debugging due to dependency-version differences and model-type issues.

---

### #p16 — Center Splash Screen title and slogan

* Tool: ChatGPT (GPT-5.5)
* Date: 2026-06-17
* Context given to the AI: `SplashScreen.kt`

**Prompt:**

> Splash Screen: Master Planner title needs to be centered

**Result:** Suggested applying `Modifier.fillMaxWidth()` and `textAlign = TextAlign.Center` to the title `Text` composable, and optionally to the slogan, ensuring visual centering regardless of screen size.

**Assessment:** Accepted — simple Compose layout correction with no architectural impact.

---

### #p17 — Remove search icon from Main Menu top bar

* Tool: ChatGPT (GPT-5.5)
* Date: 2026-06-17
* Context given to the AI: `MainMenuScreen.kt`

**Prompt:**

> Main menu: remove search icon on top bar, but keep the structure of the other components

**Result:** Identified the trailing `Icons.Default.Search` block in the top bar and removed it while preserving the existing `Row`, avatar menu, title, spacing, and alignment structure.

**Assessment:** Accepted — UI simplification with no behavior changes.

---

### #p18 — Fix visual overflow on Before Task / After Task selector

* Tool: ChatGPT (GPT-5.5)
* Date: 2026-06-17
* Context given to the AI: `AddDurationDialog`

**Prompt:**

> Add Duration: "before task" and "after task", when selecting the correspondent tab, the borders of the colored rectangle are shown outside the main border

**Result:** Diagnosed the overflow as a nested-border rendering issue and recommended removing the inner selected-tab border, relying on the colored background while keeping the parent container border.

**Assessment:** Accepted — resolved visual artifact without changing interaction logic.

---

### #p19 — Wheel-style duration picker
- Tool: Android Studio AI
- Date: 2026-06-03

**Prompt:**
> Replace the static duration selector with a native rotating wheel picker.

**Result:** Implemented NumberPicker wrapped in AndroidView.

**Assessment:** Accepted after minor manual fixes.

---

### #p20 — Roadmap drag-and-drop ordering
- Tool: Claude (Sonnet 4.6)
- Date: 2026-06-13

**Prompt:**
> Add drag-and-drop reordering for roadmap tasks and persist the resulting order.

**Result:** Reorderable list integration and persistence through roadmap item entries.

**Assessment:** Accepted.

---

### #p21 — Task Library drag-and-drop
- Tool: Claude (Sonnet 4.6)
- Date: 2026-06-14

**Result:** Consistent drag-and-drop behavior between editor and library views.

**Assessment:** Accepted.

---
