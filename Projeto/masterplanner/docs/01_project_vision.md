# 01 — Project Vision

> Fill in before writing code. 1 page is enough. Update if the vision changes.

## Topic agreed with the instructor

Master Planner is a thematic routine and task management application that uses a pirate-inspired "Archipelago" aesthetic. Users organize their lives by creating "Roadmaps" (routes) composed of specific "Tasks" (waypoints), emphasizing reuse through a global "Task Library" (The Booty Bag).

## Problem / opportunity

- **What problem does it solve?** Standard planning apps are often visually dry and don't explicitly distinguish between a high-level routine (Roadmap) and the individual reusable steps (Tasks).
- **For whom (target user)?** Productivity enthusiasts who enjoy gamified or themed interfaces and need a structured way to manage recurring procedures (e.g., bakery routines, morning rituals).

## Value proposition (1 sentence)

"An app that lets captains of their own lives chart their daily course through themed roadmaps in a gamified, pirate-inspired way."

## How it meets the mandatory requirements

| Requirement | How it will be met |
|---|---|
| 3–5 main screens | Main Menu (Roadmap list), Task Library, Roadmap Editor, Create Task, and Auth screens. |
| State sharing between users | Handled via Firebase Firestore, allowing real-time synchronization of roadmaps and the "Booty Bag" task library across devices. |
| AI (remote or local API) | AI-powered task suggestions based on Roadmap titles to help users "discover" waypoints for their journey. |
| Image / audio / video | Integration of custom pirate-themed iconography and the treasure map logo across the interface. |
| Freemium (free + paid) | Free users are limited to 3 roadmaps and 6 tasks per roadmap; premium "Admiral" status removes these limits. |
| Offline | Firestore offline persistence is enabled, with a specialized "Sailing without signal" UI banner to inform the user. |

## Out of scope (what we will NOT do)

- Integration with external calendars (Google/Outlook).
- Real-time social collaboration or "crew" multiplayer features.
- Advanced GPS/Location-based task triggering.

## Success metric

We know it worked if a user can successfully "Set Sail" by creating a multi-task roadmap, reorder their journey using drag-and-drop, and see their data survive a simulated shipwreck (logout/login or offline toggle).