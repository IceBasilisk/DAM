# 08 — State Management

State is primarily managed through `RoadmapViewModel`, which serves as the central state holder for roadmaps, tasks, AI suggestions, and user interactions.

## UI state pattern

The project does not currently define dedicated `UiState` classes per screen. Instead, screen state is managed directly through `RoadmapViewModel` and Compose state.

Typical state includes:

```kotlin
data class RoadmapUiState(
    val roadmaps: List<Roadmap> = emptyList(),
    val tasks: List<Task> = emptyList(),
    val currentRoadmap: Roadmap? = null,
    val aiSuggestedTasks: List<Task> = emptyList(),
    val isOffline: Boolean = false,
    val isPremium: Boolean = false,
    val error: String? = null,
)
```

## State sources

| State | Source of truth | Observed by |
|---|---|---|
| Roadmaps | RoadmapViewModel | RoadMapEditorScreen |
| Tasks | RoadmapViewModel | TaskLibraryScreen, CreateTaskScreen |
| Current roadmap | RoadmapViewModel | RoadMapEditorScreen |
| AI task suggestions | GeminiService → RoadmapViewModel | RoadMapEditorScreen |
| Connectivity / offline | NetworkMonitor | ViewModel / UI |
| Premium entitlement | PremiumManager | FreemiumScreen and premium-gated features |
| Authentication session | Firebase Auth | LoginScreen, CreateAccountScreen, MainActivity |

## State sharing between users

- The application uses Firebase services for authentication and data persistence.
- User-specific roadmap and task data is synchronized through Firebase/Firestore.
- AI-generated roadmap recommendations are requested from Gemini on demand.
- Changes made by a user are reflected in the ViewModel state and propagated to Compose screens through reactive state observation.

## Concurrency

- ViewModel-driven asynchronous operations.
- Network operations performed through `GeminiService`.
- Firebase operations execute asynchronously and update ViewModel-managed state.
- Compose recomposition is triggered when observed state changes.

## Offline in state

- Connectivity status is provided by NetworkMonitor.
- The UI can react to offline conditions and disable network-dependent functionality.
- AI task generation requires network connectivity.
- Firebase synchronization may be unavailable while offline, with local UI state remaining available until connectivity returns.