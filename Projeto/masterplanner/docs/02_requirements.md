# 02 — Requirements

> Each requirement has a stable ID (`RF-xx` / `RNF-xx`). The IDs are referenced
> in user stories, tests and commits.

## Functional Requirements

| ID | Requirement | Priority (MoSCoW) | Mandatory requirement covered |
|---|---|---|---|
| RF-01 | **Core Journey Management**: User can create, view, reorder, and delete Roadmaps (routes) containing multiple Tasks (waypoints). | Must | 3–5 screens |
| RF-02 | **Cloud Synchronization**: Roadmap and Task data must sync in real-time to Firebase Firestore under the user's UID. | Must | State sharing between users |
| RF-03 | **Discovery Compass (AI)**: System suggests potential Task names based on the Roadmap title using a remote AI API. | Must | AI |
| RF-04 | **Thematic Iconography**: Users can choose pirate-themed marks (Anchor, Ship, Compass) and guild colors for their entries. | Must | Multimedia |
| RF-05 | **Admiral Rank (Freemium)**: Simulate a subscription that unlocks unlimited roadmaps/tasks and removes the 3-roadmap limit. | Must | Freemium |
| RF-06 | **Sailing without Signal**: The app must allow viewing and editing of roadmaps while offline, syncing changes once back online. | Must | Offline |
| RF-07 | **The Booty Bag (Library)**: A global library of reusable tasks that can be "looted" (imported) into any roadmap. | Should | |


## Non-Functional Requirements

| ID | Category | Requirement |
|---|---|---|
| RNF-01 | Performance | The Main Menu should load cached roadmaps from Firestore local persistence in under 500ms. |
| RNF-02 | Offline | Changes made offline (CRUD on roadmaps/tasks) must persist locally and automatically push to the cloud on connection recovery. |
| RNF-03 | Security | Authentication handled via Firebase Auth. No sensitive user data (passwords) stored in Firestore; only references to UID. |
| RNF-04 | Accessibility | All pirate icons must have meaningful content descriptions; maintain contrast ratios for readability on aged parchment backgrounds. |
| RNF-05 | Compatibility | Minimum SDK 24 (Android 7.0); Target SDK 35 (Android 15). |

## Freemium Model (detail)

| Feature | Free Plan | Paid Plan (simulated) |
|---|---|---|
| Roadmap Limit | Maximum of 3 active roadmaps | Unlimited |
| Tasks per Roadmap | Maximum of 6 tasks per roadmap | Unlimited |
| AI Suggestions | Standard speed | Priority / Unlimited |
| Thematic Customization | Basic colors | "Gold" and "Deep Sea" premium themes |

> The subscription is **simulated** (no real payment). How it is simulated and where the state
> is stored: see `docs/06_architecture.md` and `docs/08_state_management.md`.

## Business rules

- **RN-01 (Ownership)**: A user can only see and edit roadmaps they created (linked to their UID).
- **RN-02 (Task Reusability)**: Deleting a task from the "Booty Bag" does not remove it from roadmaps that have already imported it, but deleting it from a roadmap only removes that specific instance.
- **RN-03 (Account Deletion)**: When an account is deleted, all linked roadmaps, tasks, and library items must be purged from Firestore.