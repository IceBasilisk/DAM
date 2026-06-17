# 03 — User Stories

> Format: **As** \<role\>, **I want** \<action\>, **so that** \<benefit\>.
> Each story has verifiable acceptance criteria (the basis for the tests).

## User roles

- **Sailor (Free Plan)**: A user who can create up to 3 roadmaps with up to 6 tasks each. They have access to the global Booty Bag library.
- **Admiral (Subscriber)**: A simulated paid user with no limits on roadmaps or tasks per roadmap.

## Stories

### US-01 — Roadmap Management
**As** a Sailor, **I want** to create and name my own journey routes, **so that** I can organize my procedures.
- Covers: RF-01
- Acceptance criteria:
  - [ ] Given I am logged in, when I click "Make New Roadmap", then a dialog appears to set a title.
  - [ ] Given I have created a roadmap, when I click on it, then the Roadmap Editor opens.
  - [ ] Given I have multiple roadmaps, when I drag one, then I can change its position in the list.
- Priority: Must

### US-02 — Task Reordering (Waypoint Order)
**As** a Sailor, **I want** to drag and drop tasks within a roadmap, **so that** I can define the chronological order of my routine.
- Covers: RF-01
- Acceptance criteria:
  - [ ] Given I am in the Roadmap Editor, when I long-press a task card, then I can drag it to a new position.
  - [ ] Given I move a task, when I release it, then the new order is saved to Firestore.
- Priority: Must

### US-03 — Cloud Synchronization
**As** a Sailor, **I want** my roadmaps to be available on all my devices, **so that** I can access my plans anywhere.
- Covers: RF-02
- Criteria:
  - [ ] When I create a roadmap on Device A, then it appears in real-time on Device B logged into the same account.
  - [ ] When I delete a task, then it is removed from the cloud immediately.
- Priority: Must

### US-04 — Discovery Compass (AI)
**As** a Sailor, **I want** the system to suggest task names based on my roadmap's title, **so that** I don't have to think of every step manually.
- Covers: RF-03
- Criteria:
  - [ ] Given I have named a roadmap "Morning Bakery", when I click the "Discovery" button, then the system suggests tasks like "Preheat Oven" or "Knead Dough".
  - [ ] Given the AI is working, when it finishes, then the suggestions are displayed as selectable cards.
- Priority: Must

### US-05 — Admiral Rank Paywall
**As** a Sailor, **I want** to be notified when I reach my free limit, **so that** I know I can upgrade to Admiral rank.
- Covers: RF-05
- Criteria:
  - [ ] Given I have 3 roadmaps, when I try to create a 4th, then I see a "Join the Admirals" paywall screen.
  - [ ] Given I "Simulate Purchase", when the process completes, then the 3-roadmap limit is removed.
- Priority: Must

### US-06 — Sailing without Signal (Offline)
**As** a user without network, **I want** to continue editing my roadmaps, **so that** my productivity isn't interrupted by bad signal.
- Covers: RF-06
- Criteria:
  - [ ] Given I am offline, when I create a task, then it is saved to the local cache and shown with a "pending" or offline indicator.
  - [ ] Given I go back online, when the app reconnects, then all local changes are pushed to Firestore automatically.
- Priority: Must

### US-07 — The Booty Bag (Task Library)
**As** a Sailor, **I want** to save my common tasks into a global library, **so that** I can reuse them in different roadmaps without re-typing.
- Covers: RF-07
- Criteria:
  - [ ] Given I am in the "Booty Bag" screen, when I create a task, then it stays there permanently.
  - [ ] Given I am in the Roadmap Editor, when I click "Import", then I can select multiple tasks from the Booty Bag to add to my current roadmap.
- Priority: Should