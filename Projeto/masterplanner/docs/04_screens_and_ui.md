# 04 — Screens and UI

> 3 to 5 main screens. Material 3. Think about states: *loading*, *empty*, *error*, *content*, *offline*.

## Screen inventory

| Screen | Goal | Inputs | Main actions | Shared state? |
|---|---|---|---|---|
| Splash | Branding & Auth Check | N/A | Pulse animation, auto-navigate | N/A |
| Login / Sign Up | Authentication | Email, Password | Board the Ship, Join the Crew | Firebase User |
| Main Menu | Roadmap list | List of Roadmaps | Create/Delete Roadmap, Navigate to Editor | Firestore Roadmaps |
| Roadmap Editor | Journey ordering | Roadmap tasks | Reorder, Delete, Add Time, Add Tasks | Firestore Tasks |
| Create Task | Adding waypoints | Task details | Choose icon/color, Save to Roadmap/Library | Firestore |
| Task Library | "The Booty Bag" | Library tasks | Search, Delete, Create new reusable task | Firestore Library |

---

## For each screen

### Screen: Main Menu
- **User stories:** US-01, US-05
- **Compose components:** `MainMenuScreen`, `RoadmapCard`, `CreateRoadmapDialog`, `RoadmapViewModel`
- **UI states:**
  - **Loading:** Skeleton/Shimmer effect (TODO) or ProgressBar.
  - **Empty:** "No course charted" placeholder image.
  - **Error:** Toast message with failure reason.
  - **Offline:** Top banner "Sailing without signal".
  - **Content:** List of reorderable roadmap cards.
- **Paywall?** Yes. Clicking "Make New Roadmap" shows paywall if count ≥ 3.
- **Multimedia:** Pirate-themed icons (Anchor, Map).

### Screen: Roadmap Editor
- **User stories:** US-02, US-04
- **Compose components:** `RoadMapEditorScreen`, `EditorTaskCard`, `AddDurationDialog`, `BootyBagDialog`
- **UI states:**
  - **Loading:** CircularProgressIndicator while fetching tasks.
  - **Empty:** Dotted path with "Add a waypoint" text.
  - **Offline:** Reordering saved locally immediately.
  - **Content:** Vertical timeline with tasks and duration badges.
- **Multimedia:** `DirectionsBoat` icon moving along a dashed vertical path.

### Screen: Task Library (The Booty Bag)
- **User stories:** US-07
- **Compose components:** `TaskLibraryScreen`, `LibraryTaskCard`
- **Multimedia:** `treasure_map_icon` loaded via `painterResource`.

---

## Design system

### Colors & Theme
- **Background:** `fresh_cream` (#FFFFF9ED) - Aged parchment feel.
- **Primary:** `cigar` (#FF783A21) - Burnt wood / tobacco brown.
- **Accent:** `gold` (#FFFFD700) - Treasure/Coin gold.
- **Secondary:** `cheesecake` (#FFF5E6D3) - Lighter parchment.

### Reusable components
- `MasterPlannerBottomBar`: Navigation between Roadmaps and Library.
- `MasterPlannerTopBar`: Branding + Account/Logout menu.
- `PirateButton`: Hard-shadowed button style.
- `PirateTextField`: Outlined field with background offset.
- `DottedBackground`: Canvas-drawn grid for all internal screens.

---

## Accessibility

- [x] `contentDescription` on `Person`, `Search`, `Add`, and `DirectionsBoat` icons.
- [x] Touch targets ≥ 48dp (All Cards and Buttons).
- [ ] Sufficient contrast: Checked via Visual Lint; some slogan text contrast requires adjustment (Issue #1 fixed in implementation).
- [x] Font weights: Uses `ExtraBold` and `Black` for maximum readability against background.