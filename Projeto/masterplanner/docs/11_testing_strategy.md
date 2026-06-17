# 11 — Testing Strategy

> "Tests pass" is part of the Definition of Done. CI runs `./gradlew test`.

## Pyramid

| Level | Target | Tools |
|---|---|---|
| Unit (majority) | UseCases, ViewModels, Mappers, Repository (with fakes) | JUnit, kotlinx-coroutines-test, Turbine, MockK |
| Integration | Room DAO, DTO serialization | Room in-memory, JUnit |
| UI (some) | Critical Compose flows | Compose UI Test |

## What must be tested

- [ ] **Freemium** logic (`PremiumManager`)
  - Premium feature access
  - Free-tier limitations
  - Upgrade state changes

- [ ] **Offline** behavior (`NetworkMonitor`)
  - Connectivity loss detection
  - Disabling AI features when offline
  - Handling Firebase synchronization failures

- [ ] **AI integration**
  - Gemini request generation
  - Gemini response parsing
  - Error handling for quota, timeout, and invalid responses

- [ ] **Roadmap management**
  - Creating roadmaps
  - Editing roadmaps
  - Task ordering and drag-and-drop persistence

- [ ] **Task management**
  - Creating tasks
  - Updating tasks
  - Deleting tasks

- [ ] **Authentication**
  - Login success/failure
  - Account creation
  - Logout flow

- [ ] **ViewModel**
  - Loading state
  - Content state
  - Error state
  - AI suggestion state updates

- [ ] **State synchronization**
  - Firestore read/write operations
  - User-specific data isolation
  - Synchronization failure handling
  
## Conventions

- Name tests using:
  - `method_condition_expectedResult`
  - Example: `generateSuggestions_validPrompt_returnsTasks`

- Use coroutine test dispatchers for asynchronous code.

- Mock external dependencies:
  - GeminiService API calls
  - Firebase Authentication
  - Firestore operations

- Prefer fakes for data providers where practical.
## Example (included in the skeleton)

See `app/src/test/.../SampleViewModelTest.kt` and `.../GetSampleItemsUseCaseTest.kt`.

## Coverage

- Focus on domain logic, not blind %. Indicate here what is covered and what is missing.
