# 10 — Security and Permissions

## Secrets management

- The project integrates external services including Google Gemini and Firebase.
- API keys and service credentials should not be committed to source control.
- Recommended flow:
  - `local.properties` (not versioned)
  - Read in `build.gradle.kts`
  - Exposed through `BuildConfig`
- Firebase configuration is provided through the Firebase configuration files (`google-services.json`), which should be managed according to deployment requirements.
- A template file should be provided for developers:

```properties
# local.properties.example
GEMINI_API_KEY=key_here
```

## Android permissions

| Permission | Why | Runtime? |
|---|---|---|
| `INTERNET` | Firebase synchronization and Gemini API access | No |
| `ACCESS_NETWORK_STATE` | Used by NetworkMonitor to detect connectivity changes | No |
| `WAKE_LOCK` | Firebase/network operations (if required by dependencies) | No |
| `POST_NOTIFICATIONS` | Not currently used | Yes (Android 13+) |
| `RECORD_AUDIO` | Not used in current implementation | Yes |
| `CAMERA` | Not used in current implementation | Yes |

- The application currently relies primarily on network-related permissions.
- No camera or microphone functionality was identified in the project structure.
- Any future runtime permissions should be requested only when the feature is used and should include denial handling.

## User data

### Stored data

| Data | Location |
| User account information | Firebase Authentication |
| Roadmaps | Cloud Firestore |
| Tasks | Cloud Firestore |
| Premium status | PremiumManager / application state |
| AI-generated suggestions | Generated on demand via Gemini |

### Local Storage

- No Room database was identified.
- No DataStore implementation was identified.
- Application state appears to be managed primarily through `RoadmapViewModel`.
- Temporary UI state exists only during application execution.

### Encryption

- Firebase provides encrypted transport (HTTPS/TLS).
- No custom local encryption layer (EncryptedDataStore or SQLCipher) was identified.
- Sensitive authentication credentials are managed by Firebase Authentication.

## Authentication / authorization

### Authentication

- User accounts are managed through Firebase Authentication.
- Users must sign in through the application's authentication screens:
	- LoginScreen
	- CreateAccountScreen

### Authorization

- User-specific roadmap and task data is associated with the authenticated Firebase user.
- Access control is enforced through Firebase Authentication and Firestore security rules.

### Premium Features

- Premium entitlement is managed through `PremiumManager`.
- Premium-gated functionality controls access to certain application features.
- Validation appears to be application-managed rather than fully server-side.
- If premium checks are performed only locally, they can be bypassed on modified clients; server-side validation would provide stronger protection.

## Checklist

- [x] No hardcoded credentials identified in the documented architecture
- [ ] `local.properties.example` present and up to date
- [x] Minimum necessary permissions (network-related only)
- [x] Authentication handled through Firebase Authentication
- [x] Connectivity monitoring implemented through NetworkMonitor
- [ ] Runtime permission denial handling documented (if new permissions are added)
- [ ] Verify Gemini API key is loaded through BuildConfig rather than source code
- [ ] Verify Firestore security rules are configured for authenticated access only