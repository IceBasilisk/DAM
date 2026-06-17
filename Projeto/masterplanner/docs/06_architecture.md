# 06 — Architecture

> **This document is binding.** The AI must follow it (see `AGENTS.md` §2).
> Non-trivial decisions → ADR in `docs/adr/`.

## Pattern

**MVVM + Repository Pattern**

The application follows the clean architecture principles, separating the UI, Domain, and Data layers.

```
ui (Compose, RoadmapViewModel, StateFlow)
        │  (Uses Domain models)
        ▼
domain (models: Roadmap, Task; repository interfaces)
        ▲
        │
data (implementation of repositories using Firebase Firestore SDK)
```

**Dependency Rule**: `ui → domain → data`. The UI interacts with the `RoadmapViewModel`, which in turn coordinates with the Repository (currently abstracted within the data package).

## Package structure (follow this)

```
a47514.masterplanner
├── data/               # Models (Task, Roadmap) and Firestore logic (Utility, ViewModel)
├── ui/
│   ├── theme/          # MasterPlannerTheme, Colors, Typography
│   ├── components/     # Reusable UI (MasterPlannerBottomBar, DottedBackground)
│   └── screens/        # MainMenuScreen, RoadMapEditorScreen, etc.
```

## Dependency injection

- **Choice**: Standard Android ViewModel factory for simplicity, transitioning to **Hilt** for scalability (ADR-0001 pending).
- ViewModels are provided to Composables via `activity-viewmodels` in the `MainActivity` entry point.

## Networking

- **Choice**: **Firebase Firestore SDK**.
- **Reasoning**: Real-time synchronization and automatic state sharing between users (multi-device) are core requirements easily satisfied by Firestore's document-listener architecture.

- **Single Source of Truth**: Firestore's **Offline Persistence**.
- **Behavior**:
    - The `RoadmapViewModel` attaches listeners to Firestore collection references.
    - Firestore automatically caches data locally. The UI observes these listeners and renders the local cache immediately (RNF-01).
    - Writes made while the "Sailing without signal" banner is active are queued locally and pushed to the cloud upon reconnection.
- **UI Feedback**: `OfflineBanner` in `CommonUI.kt` informs the user when they are operating in local-only mode.

## State sharing between users (mandatory)

- **Service**: Firebase Firestore.
- **Strategy**: Data is partitioned by User ID (`/users/{uid}/roadmaps/...`). Multi-device sync is achieved by logging into the same Firebase account on different devices.
- **Conflict Resolution**: Last-write-wins (Firestore default).

## Freemium (mandatory)

- **Subscription State**: Currently derived from the user's roadmap and task count in the `RoadmapViewModel`.
- **Planned**: Move `isPremium` state to **Jetpack DataStore** and centralize check in a `ValidatePlanUseCase`.
- **Limits**:
    - Sailor (Free): 3 Roadmaps, 6 Tasks/Roadmap.
    - Admiral (Paid): Unlimited.

## Environment

- **Android Studio**: Ladybug (2024.2.1)
- **JDK**: 17
- **minSdk**: 24
- **targetSdk**: 35
- **Kotlin**: 2.0.0
- **Compose**: 2024.02.01 (BOM)