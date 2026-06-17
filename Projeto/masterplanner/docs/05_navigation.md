# 05 — Navigation

> Navigation Compose. Type-safe routes. Define the graph before implementing screens.

## Navigation graph

```
Splash ──> Login (if unauthenticated)
Splash ──> MainMenu (if authenticated)

Login ──> SignUp
SignUp ──> Login

MainMenu ──> RoadMapEditor/{roadmapId}
MainMenu ──> TaskLibrary
MainMenu ──> Freemium (if limit reached)
MainMenu ──> Login (on logout)

RoadMapEditor ──> CreateTask/{roadmapId}
RoadMapEditor ──> MainMenu
RoadMapEditor ──> TaskLibrary

TaskLibrary ──> CreateTask/"library"
TaskLibrary ──> MainMenu
TaskLibrary ──> Login (on logout)

CreateTask ──> RoadMapEditor (back)
CreateTask ──> TaskLibrary (back)
```

## Routes

| Route | Arguments | Screen | Access |
|---|---|---|---|
| `Splash` | — | `SplashScreen` | Public |
| `Login` | — | `LoginScreen` | Public |
| `SignUp` | — | `CreateAccountScreen` | Public |
| `MainMenu` | — | `MainMenuScreen` | Authenticated |
| `TaskLibrary` | — | `TaskLibraryScreen` | Authenticated |
| `RoadMapEditor` | `roadmapId: String` | `RoadMapEditorScreen` | Authenticated |
| `CreateTask` | `roadmapId: String` | `CreateTaskScreen` | Authenticated |
| `Freemium` | — | `FreemiumScreen` | Authenticated |

## Decisions

- **State-driven Navigation**: Instead of the standard `NavController`, the app currently uses a centralized `currentScreen` state in `MainActivity` with an `enum class Screen`. This provides a single source of truth for the active destination.
- **Deep links**: Not implemented in the current version.
- **Back stack**: Handled manually via screen transitions. `onBack` callbacks are passed to detail screens to return to their parent (e.g., `CreateTask` returns to `RoadMapEditor`).
- **Single Top**: The navigation logic ensures that switching between `MainMenu` and `TaskLibrary` via the bottom bar resets the current destination state.
- **Plan-conditional navigation**: Logic for showing the `Freemium` screen is contained within the `MainMenuScreen` and `RoadMapEditorScreen` logic, checking the count of existing roadmaps/tasks against the free tier limits (3 roadmaps, 6 tasks).
