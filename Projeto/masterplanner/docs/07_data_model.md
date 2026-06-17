# 07 — Data Model

> Domain model + persistence (Room) + DTOs (network) + DataStore. Includes mappings.

## Domain model

```
**Roadmap**
- `id`: String (Firestore Document ID)
- `title`: String
- `iconName`: String (e.g., "Ship", "Anchor")
- `colorHex`: String (UI theme color)
- `itemEntries`: List<RoadmapItemEntry> (The ordered sequence of tasks and gaps)
- `sortOrder`: Int
- `timestamp`: Timestamp

**Task**
- `id`: String
- `name`: String
- `iconName`: String
- `colorHex`: String
- `durationMinutes`: Int
- `sortOrder`: Int
- `timestamp`: Timestamp

**RoadmapItemEntry** (UI Layout helper)
- `type`: String ("task" or "duration")
- `taskId`: String (Link to the task sub-collection)
- `taskName`: String (Cached name for display)
- `durationLabel`: String (e.g., "15 MIN")
```

### Entity-relationship (ER) diagram

```
[ User ]
    │
    ├── (1:N) ── [ Roadmap ]
    │              │
    │              ├── (embedded) ── [ RoadmapItemEntry ] (UI Order)
    │              │
    │              └── (1:N sub-col) ── [ Task ] (Actual waypoints)
    │
    └── (1:N) ── [ Task Library ] (Global "Booty Bag")
                   │
                   └── [ Task ]
```

## Local persistence — Room

Since the project uses the **Firebase Firestore SDK**, the specialized local persistence is handled by Firestore's internal SQLite cache.

- **Single Source of Truth**: Firestore.
- **Synchronization**: Automatic via document listeners. Changes made offline are queued and pushed when the connection returns.

## DTOs — network

| DTO | Endpoint | Maps to |
|------|----------|----------|
| No dedicated DTO classes found | N/A | Network responses appear to be handled directly by `GeminiService` |
## DataStore (preferences / lightweight state)

| Key | Type | Purpose |
|---|---|---|
| `is_premium` | Boolean | Simulated subscription state (Admiral Rank) |
| `last_sync_timestamp` | Long | For manual check of data freshness |
| `onboarding_seen` | Boolean | Skips the tutorial splash if true |

## Mappings

- **Internal Mapping**: `RoadmapItemEntry` acts as a DTO-to-UI mapper, allowing the `RoadmapEditorScreen` to reconstruct the complex "Task -> Duration -> Task" visual timeline from a flat list in Firestore.
- **Rule**: The UI mainly observes `StateFlow<List<Roadmap>>` which contains clean domain models.

## Multimedia data

- **Icons**: SVG-based `ImageVector` symbols mapped from strings via `Utility.iconFromName`.
- **Images**: App logo (`treasure_map_icon.png`) and pirate avatars loaded from local drawable resources for immediate offline availability.