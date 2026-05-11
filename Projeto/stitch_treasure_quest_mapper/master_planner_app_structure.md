# Master Planner - Structural Requirements & App Architecture

## Core Identity
- **App Name:** Master Planner
- **Visual Theme:** Cartoony Treasure Map / High Seas Adventure (vibrant colors, rounded corners, playful pirate aesthetics).
- **Mode Support:** Designed for both Light and Dark modes.

## App Structure & Flow

### 1. Main Menu (Unified Dashboard)
- **Primary View:** A single page displaying a list of all active Roadmaps.
- **Layout:** Roadmap items are displayed as cards without progress bars.
- **Action:** Includes a prominent button titled "Make new roadmap".
- **Roadmap Creation Pop-up:** Triggered by the "Make new roadmap" button. Requires only a Title and a Logo (where users can pick a symbol and color).

### 2. Roadmap Editor (The Treasure Map)
- **Canvas:** A visual space where tasks are arranged as "blocks of text."
- **Task Connections:** Arrows point from task to task to indicate the workflow order.
- **Transitions:** Arrows can contain small info boxes for transition details (e.g., "10/15 min").
- **Transition Style:** Timers/info boxes are simplified, stylized "stickers" rather than realistic elements.
- **Interaction:** Uses a drag-and-drop system for arranging tasks.

### 3. Task Management & Creation
- **Add Action:** A "+" button within the Roadmap Editor opens two options:
    1. **Make a New Task:** Opens a basic creation pop-up requiring only a Title and a Logo (symbol/color selection).
    2. **Import Previously Created Task:** Opens the "Booty Bag" pop-up.
- **Task Library (The Booty Bag):** No longer a standalone page. It is a pop-up/overlay that allows users to drag-and-drop saved tasks directly onto the roadmap.
- **Persistence:** Tasks are saved for reuse across different roadmaps.

## Removed Elements (Negative Constraints)
- No "Loot Value" or "XP Rewards" terminology.
- No "Active Voyages" flavor text.
- No progress bars on the Main Menu roadmap list.
- No standalone Task Library page.
