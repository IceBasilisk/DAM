# Prompts — Architecture

Prompts about layers, Repository, UseCases, DI, offline, synchronization.

## Good practices

- Always point to `AGENTS.md` and `docs/06_architecture.md` (dependency rule).
- Require SSOT = Room for offline data.
- Ask for interfaces in `domain`, implementations in `data`.
- Forbid exposing DTO/Entity to the UI (domain models only).

---

### #p1 — (example) Repository with offline cache

- Tool: <...>
- Date: 2026-05-19
- Context given to the AI: `docs/06`, `docs/07`, `docs/08`, `data/`

**Prompt:**
> Implement `XxxRepositoryImpl` that fulfills the `XxxRepository` interface (domain).
> Single Source of Truth = Room: expose `Flow<List<Xxx>>` from the DB; a `refresh()` fetches
> from the API and updates the DB. Without network, serve the DB. Map DTO→Entity→domain in `data/mapper`.
> Follow `docs/06_architecture.md`. Do not access Retrofit from the UI.

**Result:** <fill in>
**Assessment:** <...>

---

### #p2 — Identify and fix Compose performance bottlenecks

- Tool: Claude (Sonnet 4.6)
- Date: 2026-06-15
- Context given to the AI: Full source zip (`masterplanner.zip`) — `RoadMapEditorScreen.kt`, `TaskLibraryScreen.kt`, `RoadmapViewModel.kt`, all card composables

**Prompt:**
> App too laggy, how to make it less performance intensive

**Result:** Five structural issues identified: (1) `DottedBackground` Canvas redraws on every recomposition — fix with `drawBehind` on the parent modifier; (2) `listenToTaskLibrary()` called multiple times from different screens, stacking Firestore listeners — fix by moving to `ViewModel.init`; (3) `colorResource()` called inside every list item on recompose — fix with `CompositionLocal`; (4) `currentItems` fully rebuilt on every Firestore snapshot — fix by keying `remember` on `itemEntries` only; (5) `saveRoadmapItems` fires eagerly on every interaction.

**Assessment:** Accepted — each fix was independently verifiable and correctly targeted the Compose recomposition model.

---

### #p3 — Eliminate duplicate Firestore snapshot listeners

- Tool: Claude (Sonnet 4.6)
- Date: 2026-06-15
- Context given to the AI: `RoadmapViewModel.kt`, `TaskLibraryScreen.kt`, `RoadMapEditorScreen.kt` (BootyBagDialog)

**Prompt:**
> Where should I do this on MainActivity? [referring to moving listenToTaskLibrary() to a single call site]

**Result:** Clarified that `MainActivity` requires no changes. The fix belongs entirely in `RoadmapViewModel` — add an `init` block calling `listenToTaskLibrary()` once, then remove both `LaunchedEffect(Unit)` calls from `TaskLibraryScreen` and `BootyBagDialog`. The ViewModel is instantiated via `by viewModels()` so `init` runs automatically before any screen loads.

**Assessment:** Accepted — clean architectural separation; listener lifecycle correctly tied to ViewModel, not to composable lifecycle.

---

### #p4 — Replace colorResource calls with CompositionLocal theme colors

- Tool: Claude (Sonnet 4.6)
- Date: 2026-06-15
- Context given to the AI: `ui/theme/`, all card composables, `RoadMapEditorScreen.kt`

**Prompt:**
> How to do the CompositionLocal? [referring to hoisting colorResource out of list item recompositions]

**Result:** Full implementation: (1) `AppColors` data class in `ui/theme/AppColors.kt` holding all custom colors; (2) `LocalAppColors = compositionLocalOf<AppColors>`; (3) single `colorResource()` resolution inside `MasterPlannerTheme`, provided via `CompositionLocalProvider`; (4) all composables replace `colorResource(R.color.x)` with `LocalAppColors.current.x`. Also caught a variable swap bug introduced during migration in `EditorTaskCard` where `brown` was assigned `colors.cream` and `cheesecake` was assigned `colors.gold`.

**Assessment:** Accepted — eliminates resource lookups from the draw/composition hot path entirely.

---

### #p5 — Cascade delete from Task Library through all Roadmaps

- Tool: Claude (Sonnet 4.6)
- Date: 2026-06-15
- Context given to the AI: `RoadmapViewModel.kt`, `TaskLibraryScreen.kt`, Firestore data model (`Roadmap`, `ItemEntry`)

**Prompt:**
> Task Library: delete task function not executed. Also, deleting the Task from the Task Library should delete the corresponding task from all Roadmaps that have it, and from the database.

**Result:** Rewrote `deleteLibraryTask` in `RoadmapViewModel` to: (1) delete the document from `task_library`; (2) fetch all roadmap documents; (3) filter each roadmap's `itemEntries` to remove entries where `taskId` matches; (4) batch-write only the roadmaps that actually changed. UI update is handled automatically by the existing `listenToTaskLibrary` snapshot listener — no manual list manipulation needed in the composable.

**Assessment:** Accepted — Firestore batch write is the correct approach for cross-collection consistency without transactions.

---

### #p6 — PremiumManager: free/paid mode toggle with persisted state

- Tool: Claude (Sonnet 4.6)
- Date: 2026-06-15
- Context given to the AI: `FreemiumScreen.kt`, `MainMenuScreen.kt`, `RoadMapEditorScreen.kt`, `CommonUI.kt`, `MainActivity.kt`

**Prompt:**
> Can you put on the freemium page a button that lets the user switch between "free mode" and "paid mode"? Put a small freemium icon on the app, so that it leads to the freemium page, when the user can switch on and off the button I just mention, and the only differences between freemium being on or off is the limits of number of roadmaps and tasks.

**Result:** `PremiumManager` singleton object in `data/` with: `MutableStateFlow<Boolean>` for reactive state; `SharedPreferences` for persistence across sessions; `roadmapLimit` and `taskLimit` computed getters returning `Int.MAX_VALUE` when premium or `3`/`6` when free. Initialised once in `MainActivity.onCreate`. `FreemiumScreen` gets a `Switch` composable reading/writing the flow. Bottom bar gets a `WorkspacePremium` icon as third tab navigating to `Screen.Freemium`. Hardcoded limit checks in `MainMenuScreen` and `RoadMapEditorScreen` replaced with `PremiumManager.roadmapLimit` and `PremiumManager.taskLimit`. Limits react to toggle immediately via `collectAsState()`.

**Assessment:** Accepted — object singleton is appropriate here given single-user local state; StateFlow ensures all screens recompose on toggle without manual callbacks.

---

### #p7 — Debug non-functional AI name suggestion button

- Tool: Claude (Sonnet 4.6)
- Date: 2026-06-15
- Context given to the AI: `CreateTaskScreen.kt`, `GeminiService.kt`, `RoadmapViewModel.kt`, `MainActivity.kt`

**Prompt:**
> The current AI name suggesting button doesn't seem to be working.

**Result:** Three independent bugs found: (1) button gated on `isInsideRoadmap = roadmapId != "library" && roadmapTitle.isNotBlank()` — always false when entering from TaskLibrary since `roadmapId == "library"`; fix by reading `currentRoadmap` reactively and falling back to `"general productivity"` for library context; (2) `roadmapTitle` passed from `MainActivity` at navigation time hits a race condition where `currentRoadmap` hasn't loaded yet — fix by collecting `roadmapViewModel.currentRoadmap` as state inside `CreateTaskScreen`; (3) Gemini API called at `v1` endpoint but `gemini-2.5-flash-lite` requires `v1beta` — silent HTTP failure swallowed by catch block.

**Assessment:** Accepted — the API path bug (#3) was the most likely cause of complete silence; the race condition (#2) explains intermittent failures.

---

### #p8 — Document state management architecture

- Tool: ChatGPT (GPT-5.5)
- Date: 2026-06-16
- Context given to the AI: Project structure (`RoadmapViewModel`, `NetworkMonitor`, `PremiumManager`, Compose screens, Firebase, Gemini integration)

**Prompt:**
> Fill in the state management section of the architecture documentation.

**Result:** Documented the application's current state management model. Identified `RoadmapViewModel` as the primary state holder for roadmaps, tasks, AI-generated suggestions, and editor state. Documented state sources including Firebase Authentication, Firestore, `NetworkMonitor`, and `PremiumManager`. Clarified that the project currently uses ViewModel-managed Compose state rather than a formal `UiState` + `StateFlow` architecture and that connectivity and premium state are propagated to the UI through observable state.

**Assessment:** Accepted — accurately reflects the current implementation rather than describing an architecture that does not yet exist.

---

### #p9 — Document APIs and external services

- Tool: ChatGPT (GPT-5.5)
- Date: 2026-06-16
- Context given to the AI: `GeminiService.kt`, `PremiumManager.kt`, Firebase Authentication, Firestore, `NetworkMonitor.kt`

**Prompt:**
> Fill in the APIs and external services section of the architecture documentation.

**Result:** Produced service inventory documenting: Google Gemini API for AI-generated task and roadmap suggestions; Firebase Authentication for login and account creation; Cloud Firestore for roadmap and task persistence; Android media APIs for music playback. Documented AI integration mode (remote Gemini API), network dependency, offline degradation behaviour, and freemium limitations enforced through `PremiumManager`.

**Assessment:** Accepted — accurately captures all externally visible services discovered in the codebase.

---

### #p10 — Document security and permissions

- Tool: ChatGPT (GPT-5.5)
- Date: 2026-06-16
- Context given to the AI: Firebase-based authentication, Gemini integration, `NetworkMonitor`, Android project structure

**Prompt:**
> Fill in the security and permissions section of the architecture documentation.

**Result:** Documented secure API key handling recommendations (`local.properties` → `BuildConfig`), Firebase Authentication as the identity provider, Firestore as the persistent data store, and the minimal permission set (`INTERNET`, `ACCESS_NETWORK_STATE`). Clarified that no Room database, encrypted local storage, camera usage, or audio-recording functionality was present in the inspected project structure. Added guidance for premium feature validation and Firestore security rules.

**Assessment:** Accepted — aligns with the implementation and identifies remaining verification tasks for production readiness.

---

### #p11 — Define testing strategy for current architecture

- Tool: ChatGPT (GPT-5.5)
- Date: 2026-06-16
- Context given to the AI: Project structure, `RoadmapViewModel`, `PremiumManager`, `GeminiService`, Firebase integration, existing test directories

**Prompt:**
> Fill in the testing strategy section of the architecture documentation.

**Result:** Created a testing plan centered on the current architecture. Prioritized unit tests for `RoadmapViewModel`, `PremiumManager`, `GeminiService`, and `NetworkMonitor`; integration tests for Firebase and Gemini interactions; and Compose UI tests for login, roadmap editing, task management, and freemium flows. Identified that the repository currently only contains the default Android Studio test templates (`ExampleUnitTest` and `ExampleInstrumentedTest`) and recommended specific test classes to add.

**Assessment:** Accepted — distinguishes clearly between existing coverage and recommended future coverage.

---

### #p12 — Generate architecture documentation from project structure

- Tool: ChatGPT (GPT-5.5)
- Date: 2026-06-16
- Context given to the AI: Full project package structure, screens, ViewModel, Gemini integration, Firebase services

**Prompt:**
> Create architecture summaries and package diagrams for the project.

**Result:** Produced package-level architecture diagrams showing the relationship between UI screens, `RoadmapViewModel`, Firebase services, Gemini integration, premium management, and task/roadmap models. Also generated a concise project description describing Master Planner as a Jetpack Compose roadmap-planning application with AI-assisted task generation, Firebase-backed persistence, authentication, freemium support, and drag-and-drop roadmap editing.

**Assessment:** Accepted — useful high-level architectural overview for onboarding and documentation purposes.

---

### #p13 — Handle nullable Firestore timestamps safely

- Tool: ChatGPT (GPT-5.5)
- Date: 2026-06-15
- Context given to the AI: `RoadmapViewModel.kt`, `Roadmap.kt`, `Task.kt`, Firestore snapshot listeners

**Prompt:**
> Update the orderBy("timestamp", ...) queries in RoadmapViewModel to handle the nullable timestamp safely. Since @ServerTimestamp fields can briefly be null on the client before the server confirms, add a null check fallback in the snapshot listener.

**Result:** Recommended leaving Firestore `orderBy("timestamp")` unchanged while adding defensive sorting after deserialization using:
`sortedByDescending { it.timestamp?.seconds ?: 0L }`
and
`sortedBy { it.timestamp?.seconds ?: 0L }`.
Explained that Firestore ordering remains authoritative server-side while preventing client-side issues during pending writes.

**Assessment:** Accepted — preserves Firestore query behavior while safely handling temporary null values generated by `@ServerTimestamp`.

---

### #p14 — Diagnose Firestore write failures

- Tool: ChatGPT (GPT-5.5)
- Date: 2026-06-15
- Context given to the AI: `RoadmapViewModel.kt`, `Utility.kt`, Firestore write logs

**Prompt:**
> Why are tasks and roadmaps not being saved?

**Result:** Analysis initially focused on save methods, timestamp handling, authentication state, and Firestore configuration. After reviewing Logcat output showing:
`PERMISSION_DENIED: Missing or insufficient permissions`,
the root cause was identified as Firestore Security Rules blocking writes to:
`users/{uid}/roadmaps/{roadmapId}`.
Suggested temporary open rules for verification and user-scoped authenticated rules for production.

**Assessment:** Accepted — Firestore Security Rules were confirmed as the actual cause of failed roadmap/task persistence.

---

### #p15 — Roadmap deletion with optional cascade deletion

- Tool: ChatGPT (GPT-5.5)
- Date: 2026-06-15
- Context given to the AI: `RoadmapViewModel.kt`, Firestore roadmap/task structure, Main Menu roadmap cards

**Prompt:**
> Add roadmap deletion with a popup asking whether associated tasks should also be deleted.

**Result:** Proposed a two-path deletion architecture:
(1) Delete roadmap document only.
(2) Delete roadmap document and all task documents in its `tasks` subcollection.
Explained that Firestore does not automatically delete subcollections and recommended batch deletion of task documents before deleting the roadmap document. Also proposed a confirmation dialog exposing both choices.

**Assessment:** Accepted — correctly reflects Firestore subcollection deletion behavior and preserves user control over destructive operations.

---

### #p16 — Task Library cascading consistency

- Tool: ChatGPT (GPT-5.5)
- Date: 2026-06-15
- Context given to the AI: `TaskLibraryScreen.kt`, `RoadmapViewModel.kt`, Firestore data model

**Prompt:**
> On Task Library, add edit/delete actions and ensure deleted tasks are removed from persistence.

**Result:** Proposed adding ViewModel-level delete operations for Task Library items and integrating them through composable action menus rather than directly manipulating UI state. Emphasized keeping Firestore as the source of truth and relying on existing snapshot listeners to propagate changes back to Compose.

**Assessment:** Accepted — maintains unidirectional data flow and avoids UI-level state divergence from Firestore.

---

<!-- new entries below -->

### #p17 — Refactor Login/Signup to Compose (MVVM transition)

- Tool: Android Studio AI
- Date: 2026-06-20
- Context given to the AI: `LoginActivity.kt`, `CreateAccountActivity.kt`, `LoginScreen.kt`, `CreateAccountScreen.kt`, `MainActivity.kt`

**Prompt:**
> The current code on LoginActivity is based on the code of another project that allows the usage of Firebase login. Keeping in mind the files LoginScreen, Create AccountScreen, and the MainActivity, alter the code to allow Login and Signup

**Result:** Converted legacy XML-based Activities to `ComponentActivity` using Jetpack Compose `setContent`. Integrated Firebase `FirebaseAuth` logic directly into the Activities (later moved to unified `MainActivity` state flow).

**Assessment:** Accepted — successfully bridged the gap between static Compose UI files and Firebase functional requirements.

---

### #p18 — State-driven Navigation logic

- Tool: Android Studio AI
- Date: 2026-06-20
- Context given to the AI: `MainActivity.kt`, existing Compose screens

**Prompt:**
> The first page that should show up after the SplashScreen should be the LoginScreen. Pressing the "Join the Crew (Sign up)" button should lead to the CreateAccountScreen, and "Already part of the crew? Log in" should lead back to the LoginScreen. After a successful login, it should lead to the MainMenuScreen. Also need for the Roadmap and Task classes to be created.

**Result:** Replaced Activity-based navigation with a `Screen` enum and `mutableStateOf(Screen.Splash)` in `MainActivity`. Created `Task.kt` and `Roadmap.kt` data classes in the `data` package.

**Assessment:** Accepted — established a single-activity architecture which is the modern Compose standard.

---

### #p19 — Unified Navigation component (Bottom Bar)

- Tool: Android Studio AI
- Date: 2026-06-20
- Context given to the AI: `CreateTaskScreen.kt`, `MainMenuScreen.kt`, `RoadMapEditorScreen.kt`, `TaskLibraryScreen.kt`

**Prompt:**
> Alter the Bottom Bar on CreateTaskScreen, MainMenuScreen, RoadMapEditorScreen and TaskLibraryScreen. They should all share the same bottom bar, except: There shouldn't be a Settings button; ROADMAPS button should lead to MainMenuScreen (only button highlighted when on said page); LIBRARY button should lead to TaskLibraryScreen (only button highlighted when on said page); CreateTaskScreen and RoadMapEditorScreen show the bottom bar, but have no button highlighted.

**Result:** Created `MasterPlannerBottomBar` in `CommonUI.kt`. Centralized the highlighting logic using `currentScreen == Screen.X` checks. Removed localized bottom bar implementations from individual screen files.

**Assessment:** Accepted — significantly reduced code duplication and unified the app's visual navigation.

---

### #p20 — Implement AndroidView NumberPicker for duration

- Tool: Android Studio AI
- Date: 2026-06-20
- Context given to the AI: `RoadMapEditorScreen.kt`, `AddDurationDialog`

**Prompt:**
> RoadmapEditor, the duration dialog. I want the dialog to have a rotating hours and minutes wheel, like most timers on android apps.

**Result:** Integrated `AndroidView` to wrap the traditional `NumberPicker` view, as Compose didn't have a native wheel picker in the included library version. Added `HourPicker` and `MinutePicker` composables.

**Assessment:** Accepted — correctly identified the need for `AndroidView` for this specific legacy UI widget.

---

### #p21 — Task Library search filtering and cascade deletion

- Tool: Android Studio AI
- Date: 2026-06-20
- Context given to the AI: `TaskLibraryScreen.kt`, `MainMenuScreen.kt`, `RoadmapViewModel.kt`

**Prompt:**
> On Task Library, the search system should have a filtering system similar to the one on the BootyBag. On Task Library, similar to RoadmapEditor, had a small pencil icon to every task, with the option to delete it, removing it from the database. Do the same for the roadmaps in the main menu, but every time a roadmap is deleted, make a popup message asking if the user would like to delete the tasks as well. If yes, delete them all. If not, delete only the roadmap.

**Result:** Added `searchQuery` state and `.filter` logic to `TaskLibraryScreen`. Implemented `AlertDialog` in `MainMenuScreen` for roadmap deletion with a `deleteTasks: Boolean` parameter passed to `roadmapViewModel.deleteRoadmapWithTasks`.

**Assessment:** Accepted — fulfilled complex data management requirements and improved UI consistency.

---

### #p22 — Firestore write consistency (ID synchronization)

- Tool: Android Studio AI
- Date: 2026-06-20
- Context given to the AI: `RoadmapViewModel.kt`, `Roadmap.kt`, `Task.kt`

**Prompt:**
> Roadmaps are not being saved on Firestore. Pressing the Set Sail button to create a new Roadmap does nothing

**Result:** Fixed a bug in `saveRoadmap` and `saveTask` where the `id` field inside the object was not being updated with the auto-generated Firestore `docRef.id` before the `.set()` operation.

**Assessment:** Accepted — critical fix for data integrity; ensured that the object's `id` property matches its Firestore document ID.

### #p23 — Establish Roadmap and Task Firestore architecture from a reference pattern
 
- Tool: Claude (Sonnet 4.6)
- Date: 2026-06-15
- Context given to the AI: `masterplanner.zip` (full source), `NotesProXMLViews3.zip` (reference Firestore CRUD sample)
**Prompt:**
> Guide me on how to make the Roadmap and Task logic for the masterplanner file. And then how to apply it to the already made Jetpack Compose UI. Use the notepad making logic from the NotesProXMLViews3.zip file in order to make the above logic.
 
**Result:** Full architecture guide mapping the Notes app's Firestore pattern onto MasterPlanner: nested collection structure `users/{uid}/roadmaps/{roadmapId}/tasks/{taskId}`; `Utility.kt` rewritten with `collectionReferenceForRoadmaps`/`collectionReferenceForTasks(id)` mirroring `getCollectionReferenceForNotes()`; `Roadmap.kt`/`Task.kt` given Firestore-safe default-value constructors (mirroring the Notes app's Java POJO no-arg constructor requirement); new `RoadmapViewModel.kt` introduced as the StateFlow/`addSnapshotListener` equivalent of `NoteAdapter` + `FirestoreRecyclerOptions`; CRUD methods (`saveTask`, `deleteTask`, `saveRoadmap`, `deleteRoadmap`) modeled directly on `NoteDetailsActivity.saveNoteToFirebase()`/`deleteNoteFromFirebase()`.
 
**Assessment:** Accepted — became the foundational data-layer architecture for the rest of the project; every later Firestore feature (drag-and-drop persistence, freemium gating, AI suggestions) was built on top of this collection structure and ViewModel.
 
---
 
### #p24 — Debug root cause of all Firestore write failures
 
- Tool: Claude (Sonnet 4.6)
- Date: 2026-06-15
- Context given to the AI: `masterplanner.zip` (full source)
**Prompt:**
> Trying to create a new Roadmap always leads to "Failed to create roadmap" and trying to create a Task never works either
 
**Result:** Identified that `Timestamp.now()` used as a Kotlin data class default parameter value breaks Firestore's reflection-based deserializer, since it evaluates at construction time and conflicts with the no-arg constructor contract Firestore relies on. Fix: replace with `@ServerTimestamp val timestamp: Timestamp? = null` on both `Roadmap` and `Task`; remove manual `Timestamp.now()` assignment from all save call sites; also found a secondary bug — malformed 8-digit ARGB hex strings (`#FFFFD700`) in `CreateRoadmapDialog` that crashed `parseColor()` on read, fixed to standard 6-digit hex.
 
**Assessment:** Accepted — this was the single most impactful architectural fix in the project; every roadmap/task creation feature was non-functional until this was resolved.
 
---
 
### #p25 — Persist drag-and-drop order to Firestore across three screens
 
- Tool: Claude (Sonnet 4.6)
- Date: 2026-06-15
- Context given to the AI: `masterplanner.zip` (full source), `MainMenuScreen.kt`, `RoadMapEditorScreen.kt`, `TaskLibraryScreen.kt`
**Prompt:**
> I need you to implement a mechanism to allow the tasks and roadmaps to be dragable via the user holding them with their finger, a drag and drop system if you will, so that the user can rearrange them as they wish. The screens that need this are: MainMenuScreen, the roadmaps; RoadmapEditorScreen, the tasks and time durations; TaskLibraryScreen, the tasks. Their position on the database should also be changed to correspond.
 
**Result:** Added `sh.calvin.reorderable:reorderable` dependency; introduced `sortOrder: Int` field on `Roadmap` and `Task`; designed a consistent pattern across all three screens — a local `mutableStateListOf` mirror kept in sync with Firestore via `LaunchedEffect`, `rememberReorderableLazyListState` driving in-memory reordering during the drag, and a `LaunchedEffect(isAnyItemDragging)` that fires a single batched Firestore write only when the finger lifts. `RoadMapEditorScreen` reused the existing `itemEntries`/`saveRoadmapItems()` mechanism rather than introducing a new field, since order there already had a persistence path.
 
**Assessment:** Accepted, with two follow-up corrections — the initially suggested `ReorderableLazyColumn` wrapper composable does not exist in the current library version (verified against the library's GitHub repo), and was replaced with a plain `LazyColumn` + `ReorderableItem` per row, which is the correct current API.
 
---
 
### #p26 — Architect Gemini AI integration to match an existing reference sample
 
- Tool: Claude (Sonnet 4.6)
- Date: 2026-06-15
- Context given to the AI: `AISimpleCalls.zip` (reference AI client sample), `masterplanner.zip`
**Prompt:**
> Using this as a basis, or perhaps something even simpler, I would like to integrate AI into my app so that it can recommend titles for Tasks based on the title of the current Roadmap.
>
> (follow-up) Instead of ClaudeService, can it use the Gemini-App services from the SimpleAICalls folder?
 
**Result:** Initial design used a `ClaudeService.kt` with raw `HttpURLConnection` to avoid adding a new dependency. After the follow-up, this was discarded in favor of `GeminiService.kt`, rebuilt to mirror the sample's `AIAssistantGemini` class structure: OkHttp client with timeouts, a `buildRequest()` helper constructing the Gemini `generateContent` JSON body, and a `parseResponse()` helper navigating the `candidates[0].content.parts[0].text` response shape. `RoadmapViewModel` gained `fetchTaskSuggestions()`, `suggestedTaskNames`, `isSuggesting`, and `hasSuggestionBeenAttempted` StateFlows to drive the UI through loading/success/empty states.
 
**Assessment:** Edited — the architecture was accepted, but the AI's first draft used `v1`-style endpoint conventions and `HttpURLConnection`/`org.json` only; once corrected to follow the sample's OkHttp pattern exactly, it was accepted without further changes.
 
---
 
### #p27 — Design offline-mode architecture across persistence, connectivity, and UI layers
 
- Tool: Claude (Sonnet 4.6)
- Date: 2026-06-15
- Context given to the AI: `masterplanner.zip` (full source)
**Prompt:**
> Is there anything that could be done to this app to give a offline mode logic?
 
**Result:** Proposed a six-layer architecture: (1) Firestore `PersistentCacheSettings` (50MB) enabled once at `MainActivity.onCreate`; (2) a new `NetworkMonitor` object exposing a `Flow<Boolean>` built from `ConnectivityManager.NetworkCallback` via `callbackFlow`; (3) `_isOnline` StateFlow added to `RoadmapViewModel`, started once via `startMonitoringNetwork(context)`; (4) a reusable `OfflineBanner` composable shown conditionally at the top of each screen's content area; (5) the Gemini suggestion button disabled with an explicit "Oracle unreachable offline" state rather than failing silently; (6) a small `CloudOff` indicator added to `RoadMapEditorTopBar`. Each layer was designed to be independently optional so the app degrades gracefully even if only layer 1 is adopted.
 
**Assessment:** Accepted in full — all six layers were implemented across the following turns of the conversation.

---

### #p28 — Firestore architecture for Roadmaps and Tasks
- Tool: Claude (Sonnet 4.6)
- Date: 2026-06-09
- Context: Existing NotesProXMLViews3 project, Roadmap.kt, Task.kt, RoadmapViewModel.kt

**Prompt:**
> Use the NotesProXMLViews3 Firebase pattern as reference and design the Firestore architecture for MasterPlanner.

**Result:** Defined users/{uid}/roadmaps/{roadmapId} and nested task collections, repository flow, synchronization strategy, and CRUD operations.

**Assessment:** Accepted — became the foundation of Firestore integration.

---

### #p29 — Gemini API integration architecture
- Tool: ChatGPT (GPT‑5.5)
- Date: 2026-06-10

**Prompt:**
> Adapt the AISimpleCalls sample into the MasterPlanner architecture while keeping ViewModels independent from networking details.

**Result:** Repository-mediated Gemini integration, prompt generation layer, and response mapping.

**Assessment:** Accepted.

---

### #p30 — Offline-first synchronization strategy
- Tool: ChatGPT (GPT‑5.5)
- Date: 2026-06-11

**Prompt:**
> Design an offline-first approach using Room as the primary data source with Firestore synchronization.

**Result:** Room as SSOT, sync queues, conflict minimization, and repository coordination.

**Assessment:** Accepted.

---

### #p31 — Tie per-roadmap Firestore listener lifecycle to the screen, not just the roadmap ID

- Tool: Claude (Sonnet 4.6)
- Date: 2026-06-18
- Context given to the AI: `masterplanner.zip` (full source) — `RoadmapViewModel.kt`, `MainActivity.kt`, `RoadMapEditorScreen.kt`

**Prompt:**
> Roadmap 2 was just created, so it's supposed to be empty. The Roadmap 2 has already the same two tasks as Roadmap 1.

**Result:** Diagnosed as a state-ownership problem rather than a UI bug: `_tasks`/`_currentRoadmap` in `RoadmapViewModel` are shared singletons fed by `listenToTasks(roadmapId)`, but nothing ever detached the listener from the previously open roadmap or reset the StateFlows before attaching new ones. Introduced explicit listener-registration tracking (`ListenerRegistration` fields) and a paired `stopListeningToTasks()` lifecycle method, then moved the attach/detach decision out of the screen's `LaunchedEffect` and into a `DisposableEffect` in `MainActivity` keyed on `currentRoadmapId`, so the listener's lifetime is now explicitly scoped to "this roadmap is the one currently open in the editor" rather than implicitly relying on recomposition timing. As a secondary defensive measure, also keyed `RoadMapEditorScreen`'s `currentItems` `remember` block on `roadmapId` itself, so the UI recomputes on roadmap switch even in the edge case where two roadmaps both have empty `itemEntries`.

**Assessment:** Accepted — but produced a follow-up regression (see `debugging_prompts.md` #p17): resetting `_currentRoadmap` to `null` on dispose broke `CreateTaskScreen`'s reliance on that same StateFlow for its title, since navigating to Create Task also disposes the editor. Resolved by having `MainActivity` snapshot the title into its own state before the listener tears down, rather than relying on a single shared ViewModel field to outlive the screen that populated it.

---

### #p32 — Make a task created inside a Roadmap append to that Roadmap's persisted order automatically

- Tool: Claude (Sonnet 4.6)
- Date: 2026-06-19
- Context given to the AI: `RoadmapViewModel.kt`, `CreateTaskScreen.kt`, `RoadMapEditorScreen.kt`

**Prompt:**
> First Roadmap Task is added automatically to the roadmap, but the others need to be imported. If a task is created inside a Roadmap, make it be added automatically.

**Result:** Traced the gap to a missing write path: `saveTask()` only ever wrote to the roadmap's `tasks` sub-collection, never to `Roadmap.itemEntries` — the field `RoadMapEditorScreen.currentItems` actually renders once non-empty. The very first task "worked" only because `currentItems` falls back to the raw `tasks` StateFlow while `itemEntries` is still empty (see #p31); any task created after the first Booty Bag import silently landed in the sub-collection only. Added `addTaskToRoadmapItems(roadmapId, task)` to `RoadmapViewModel`, using Firestore's `FieldValue.arrayUnion` for an atomic append rather than a read-then-overwrite, and called it from `CreateTaskScreen` right after a successful `saveTask()`. This in turn required changing `saveTask`'s callback from `(Boolean) -> Unit` to `(Task?) -> Unit`, since the appended `RoadmapItemEntry` needs the task's real Firestore-generated id (previously discarded by the boolean-only callback) to remain deletable later from the editor.

**Assessment:** Accepted — chose `arrayUnion` over reading and rewriting the full `itemEntries` list to avoid a race with any concurrent reorder/delete already in flight from the editor screen.

---
---

### #p33 — Centralize free-tier total task gating at navigation and save boundaries

- Tool: ChatGPT (GPT-5.5 Thinking)
- Date: 2026-06-19
- Context given to the AI: `MainActivity.kt`, `CreateTaskScreen.kt`, `FreemiumScreen.kt`, `PremiumManager.kt`, `RoadmapViewModel.kt`

**Prompt:**
> The number of tasks based on the free trial mode are not being limited. What I mean is, when the number of total tasks is 7, the freemium screen is not showing whenever the user attempts to create a new task via RoadmapEditor screen create new task button or via the TaskLibraryScreen create new task button.

**Result:** Reframed the task cap as an account-level total task limit rather than a per-roadmap-only check. `MainActivity` now observes `roadmapViewModel.libraryTasks` and `PremiumManager.isPremium` at the app routing layer and computes `hasReachedTaskLimit` from the global library task count. Both task-creation entry points — Task Library's "Create New Task" button and the Roadmap Editor / Booty Bag "Create New Task" path — now route directly to `Screen.Freemium` when a free user has already reached `PremiumManager.taskLimit`. `CreateTaskScreen` keeps a second guard at the write boundary using the same global library count, so stale navigation state or future entry points cannot bypass the cap.

**Assessment:** Accepted — library tasks are the correct single source of truth because every forged task, whether created from a roadmap or directly from the Task Library, is saved to the global task library.
