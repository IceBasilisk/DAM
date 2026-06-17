# 09 — APIs and External Services

Includes the AI integration and cloud services used by the application.

## Services used

| Service | Purpose | Authentication | Free tier? |
|---|---|---|---|
| Google Gemini API | AI-generated task and roadmap suggestions | API key | Yes (quota-limited) |
| Firebase Authentication | User registration and login | Firebase Auth | Yes |
| Cloud Firestore | User roadmap and task persistence | Firebase Auth | Yes |
| Android Media APIs | Background music and audio playback | None | Yes |

## AI integration

- **Mode:** Remote API.
- **Implementation:** `GeminiService.kt`
- **Provider:** Google Gemini.
- **Input:** User roadmap context, goals, tasks, and planning requests.
- **Output:** AI-generated task suggestions and roadmap recommendations.
- **Offline degradation:** AI features are unavailable without network connectivity. The UI relies on `NetworkMonitor` to detect connectivity and disable network-dependent functionality.
- **Cost / limits:**
  - Subject to Gemini API quotas and rate limits.
  - AI functionality may be restricted for free users through `PremiumManager`.
  - Premium features are managed through the application's freemium model.
  
## API contracts (summary)

The project does not expose a custom REST backend. Communication is performed through Firebase SDKs and the Gemini SDK/API.

### Firebase Authentication

| Method | Operation | Request | Response | Errors |
|---|---|---|---|---|
| POST | Sign Up | Email, password | User account | Invalid credentials, duplicate account |
| POST | Sign In | Email, password | Authenticated session | Invalid credentials |
| POST | Sign Out | — | Success | Authentication failure |

### Firestore

| Method | Collection | Request | Response | Errors |
|---|---|---|---|---|
| READ | User roadmaps/tasks | User ID | Roadmap and task data | Permission denied, network error |
| WRITE | User roadmaps/tasks | Roadmap/Task model | Success | Permission denied, network error |


## Network error handling

- Connectivity monitoring implemented through `NetworkMonitor`.
- Firebase and Gemini requests execute asynchronously.
- Network failures prevent AI generation and cloud synchronization.
- Authentication and Firestore errors are surfaced to the UI.
- Offline mode allows local UI interaction while cloud-dependent features are unavailable.
- AI requests fail gracefully when connectivity is unavailable.

## Secrets

- Gemini API credentials should not be committed to source control.
- Firebase configuration is provided through the Firebase project configuration files.
- Production secrets should be stored outside the repository.
- Recommended approach: define API keys in `local.properties` and expose them through `BuildConfig`.