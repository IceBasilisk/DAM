---
name: The Design System
colors:
  surface: '#fff8f0'
  surface-dim: '#e8d9b2'
  surface-bright: '#fff8f0'
  surface-container-lowest: '#ffffff'
  surface-container-low: '#fff3d5'
  surface-container: '#fcedc5'
  surface-container-high: '#f6e7bf'
  surface-container-highest: '#f0e2ba'
  on-surface: '#221b03'
  on-surface-variant: '#504442'
  inverse-surface: '#383015'
  inverse-on-surface: '#fff0c8'
  outline: '#827471'
  outline-variant: '#d4c3bf'
  surface-tint: '#755750'
  primary: '#361f1a'
  on-primary: '#ffffff'
  primary-container: '#4e342e'
  on-primary-container: '#c19c94'
  inverse-primary: '#e5beb5'
  secondary: '#00629e'
  on-secondary: '#ffffff'
  secondary-container: '#62b4fe'
  on-secondary-container: '#004470'
  tertiary: '#705d00'
  on-tertiary: '#ffffff'
  tertiary-container: '#c9a900'
  on-tertiary-container: '#4c3e00'
  error: '#ba1a1a'
  on-error: '#ffffff'
  error-container: '#ffdad6'
  on-error-container: '#93000a'
  primary-fixed: '#ffdad2'
  primary-fixed-dim: '#e5beb5'
  on-primary-fixed: '#2b1611'
  on-primary-fixed-variant: '#5c403a'
  secondary-fixed: '#cfe5ff'
  secondary-fixed-dim: '#99cbff'
  on-secondary-fixed: '#001d34'
  on-secondary-fixed-variant: '#004a78'
  tertiary-fixed: '#ffe16d'
  tertiary-fixed-dim: '#e9c400'
  on-tertiary-fixed: '#221b00'
  on-tertiary-fixed-variant: '#544600'
  background: '#fff8f0'
  on-background: '#221b03'
  surface-variant: '#f0e2ba'
typography:
  headline-lg:
    fontFamily: epilogue
    fontSize: 32px
    fontWeight: '800'
    lineHeight: 40px
    letterSpacing: -0.02em
  headline-md:
    fontFamily: epilogue
    fontSize: 24px
    fontWeight: '700'
    lineHeight: 32px
  body-lg:
    fontFamily: newsreader
    fontSize: 18px
    fontWeight: '400'
    lineHeight: 28px
  body-md:
    fontFamily: newsreader
    fontSize: 16px
    fontWeight: '400'
    lineHeight: 24px
  label-caps:
    fontFamily: newsreader
    fontSize: 12px
    fontWeight: '700'
    lineHeight: 16px
    letterSpacing: 0.1em
rounded:
  sm: 0.125rem
  DEFAULT: 0.25rem
  md: 0.375rem
  lg: 0.5rem
  xl: 0.75rem
  full: 9999px
spacing:
  base: 8px
  margin-mobile: 24px
  gutter: 16px
  touch-target: 48px
---

## Brand & Style

This design system is built to evoke the Golden Age of Piracy, reimagining a mobile interface as a physical artifact—a captain's log or a long-lost treasure map. The brand personality is adventurous, rugged, and high-stakes. It targets users who value immersive storytelling and tactile feedback over clinical efficiency.

The visual style is **Tactile / Skeuomorphic**, leaning heavily into physical metaphors. Every element is designed to feel like a real object: ink on parchment, carved wood, or stamped wax. By utilizing weathered textures and organic imperfections, the UI creates an emotional connection to the thrill of discovery and the grit of the high seas.

## Colors

The palette is rooted in natural, period-accurate materials. 

- **Primary (Weathered Brown):** Represents the ship’s hull and leather bindings. Used for structural elements, heavy borders, and primary text.
- **Secondary (Deep Sea Blue):** Represents the treacherous waters. Used for interactive "discovery" elements and navigational accents.
- **Tertiary (Gold):** Used sparingly for high-value actions, rewards, and "X marks the spot" moments.
- **Neutral (Parchment):** The foundation of the design system. This serves as the global background color, mimicking the texture of aged paper.

Backgrounds should always utilize a subtle grain or "coffee-stain" texture overlay to reinforce the weathered aesthetic.

## Typography

This design system employs a dual-font strategy to balance theme with legibility.

- **Headlines:** Uses **Epilogue** in heavy weights. To achieve the "handwritten" look requested, headings should be styled with a 1-2 degree random rotation and a slightly uneven baseline where possible.
- **Body & Labels:** Uses **Newsreader**, a serif font that provides the literary feel of a printed logbook. It ensures long-form content remains readable against textured backgrounds.

Text should rarely be pure black; use the Weathered Brown (#4E342E) to simulate aged ink.

## Layout & Spacing

The layout philosophy follows a **Fluid Grid** with generous safe areas. Because cards and containers feature tattered, irregular "deckle" edges, a standard 24px margin is required to prevent visual clipping against the screen edge.

The rhythm is organic rather than clinical. Elements should be grouped in "clusters" that resemble items spread out on a navigator's table. High-density information is avoided; whitespace (parchment) is used to give the eye room to "explore" the interface.

## Elevation & Depth

Depth in this design system is achieved through physical layering rather than modern light-source shadows.

- **Level 0 (The Map):** The base parchment background.
- **Level 1 (The Log):** Cards with "tattered" edges. Use a thin, dark inner-stroke to simulate the thickness of heavy paper and a very soft, low-spread ambient shadow.
- **Level 2 (The Tools):** Buttons and interactive icons. These use high-contrast shadows to appear lifted off the page.
- **Level 3 (The Seals):** Overlays and primary actions. These use deep, multi-layered shadows to look like 3D objects (wax or wood) placed on top of the documents.

## Shapes

The shape language is defined by **organic irregularity**. While the base containers use a 0.25rem (Soft) roundedness for technical stability, the visual appearance must be modified by SVG masks.

- **Containers:** Must feature "torn" or jagged edges, especially on the bottom and right sides.
- **Buttons:** Circular buttons should be slightly "lumpy" to resemble hand-pressed wax seals. Rectangular buttons should have wood-grain textures and straight but "chipped" corners.

## Components

### Buttons
- **Primary (The Wax Seal):** Circular, Deep Sea Blue or Crimson, with a raised Gold icon (skull or anchor). It uses a heavy drop shadow and a high-shine bevel.
- **Secondary (The Wooden Plank):** Rectangular, Weathered Brown. Text is "branded" (inset shadow) into the wood surface.

### Cards
Cards resemble fragments of maps. They feature a parchment background, tattered edges, and a faint grid of latitude/longitude lines. Headers within cards are separated by a "scored" line (a thin, dark brown stroke with varied opacity).

### Inputs & Selection
- **Input Fields:** Styled as underlined ink-strokes or framed by ornate, hand-drawn flourishes at the corners.
- **Checkboxes:** Represented by an "X" mark, as if scratched onto a map with a quill.
- **Radio Buttons:** Small compass roses where the needle points to the selected option.

### Icons
All icons must be illustrative and "hand-drawn." Use variable stroke weights and avoid perfect circles or squares. Common metaphors include a **Sextant** for settings, a **Scrolled Map** for the menu, and a **Dagger** for "Delete" or "Back" actions.

### Additional Elements
- **Progress Bars:** A burning fuse moving toward a powder keg.
- **Loading States:** A spinning compass needle or a ship sailing across a dotted line.