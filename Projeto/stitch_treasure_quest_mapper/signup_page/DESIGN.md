---
name: High Seas Adventure
colors:
  surface: '#fff9ed'
  surface-dim: '#e2dabf'
  surface-bright: '#fff9ed'
  surface-container-lowest: '#ffffff'
  surface-container-low: '#fcf3d8'
  surface-container: '#f7eed2'
  surface-container-high: '#f1e8cd'
  surface-container-highest: '#ebe2c8'
  on-surface: '#1f1c0b'
  on-surface-variant: '#4d4732'
  inverse-surface: '#35301e'
  inverse-on-surface: '#faf0d5'
  outline: '#7e775f'
  outline-variant: '#d0c6ab'
  surface-tint: '#705d00'
  primary: '#705d00'
  on-primary: '#ffffff'
  primary-container: '#ffd700'
  on-primary-container: '#705e00'
  inverse-primary: '#e9c400'
  secondary: '#00658d'
  on-secondary: '#ffffff'
  secondary-container: '#3dbeff'
  on-secondary-container: '#004a69'
  tertiary: '#a63b00'
  on-tertiary: '#ffffff'
  tertiary-container: '#ffd0bf'
  on-tertiary-container: '#a83b00'
  error: '#ba1a1a'
  on-error: '#ffffff'
  error-container: '#ffdad6'
  on-error-container: '#93000a'
  primary-fixed: '#ffe16d'
  primary-fixed-dim: '#e9c400'
  on-primary-fixed: '#221b00'
  on-primary-fixed-variant: '#544600'
  secondary-fixed: '#c6e7ff'
  secondary-fixed-dim: '#83cfff'
  on-secondary-fixed: '#001e2d'
  on-secondary-fixed-variant: '#004c6b'
  tertiary-fixed: '#ffdbce'
  tertiary-fixed-dim: '#ffb599'
  on-tertiary-fixed: '#370e00'
  on-tertiary-fixed-variant: '#7f2b00'
  background: '#fff9ed'
  on-background: '#1f1c0b'
  surface-variant: '#ebe2c8'
typography:
  headline-xl:
    fontFamily: Plus Jakarta Sans
    fontSize: 48px
    fontWeight: '800'
    lineHeight: 56px
    letterSpacing: -0.02em
  headline-lg:
    fontFamily: Plus Jakarta Sans
    fontSize: 32px
    fontWeight: '800'
    lineHeight: 40px
  headline-md:
    fontFamily: Plus Jakarta Sans
    fontSize: 24px
    fontWeight: '700'
    lineHeight: 32px
  body-lg:
    fontFamily: Plus Jakarta Sans
    fontSize: 18px
    fontWeight: '500'
    lineHeight: 28px
  body-md:
    fontFamily: Plus Jakarta Sans
    fontSize: 16px
    fontWeight: '400'
    lineHeight: 24px
  label-bold:
    fontFamily: Lexend
    fontSize: 14px
    fontWeight: '600'
    lineHeight: 20px
    letterSpacing: 0.05em
  label-sm:
    fontFamily: Lexend
    fontSize: 12px
    fontWeight: '500'
    lineHeight: 16px
rounded:
  sm: 0.25rem
  DEFAULT: 0.5rem
  md: 0.75rem
  lg: 1rem
  xl: 1.5rem
  full: 9999px
spacing:
  base: 8px
  xs: 4px
  sm: 12px
  md: 24px
  lg: 40px
  xl: 64px
  gutter: 20px
  margin: 24px
---

## Brand & Style
This design system captures the adventurous spirit of a modern animated pirate epic. It targets a playful, energetic audience that appreciates high-fidelity stylized visuals over realism. The personality is "Epic Casual"—combining the grandeur of a treasure hunt with the accessibility of a mobile game.

The design style is **High-Contrast / Bold** mixed with **Tactile** elements, though executed through clean, vector-based shapes rather than photographic textures. It utilizes exaggerated proportions, thick strokes, and vibrant color blocks to evoke a sense of tangible "clickability." The UI should feel like a polished, interactive map where every button feels like a gold coin and every surface feels like a stylized piece of lore.

## Colors
The palette is built on high-contrast "hero" colors. The primary **Bright Gold** represents treasure and primary actions. In light mode, the surface uses a **Clean Parchment** (#F4EBD0) to mimic a map without using grainy textures. The secondary **Caribbean Blue** provides a cooling contrast for supporting elements.

In Dark Mode, the interface shifts to a "Midnight Navigation" theme. The surface becomes a **Deep Navy**, while interactive elements use **Glowing Amber** accents to simulate lantern light. All colors maintain high saturation to ensure the "cartoony" energy is preserved even in lower light settings. Avoid muddy tones; every hex should feel "pure" and vibrant.

## Typography
We use **Plus Jakarta Sans** for headlines and body text to provide a soft, welcoming, and inherently "bouncy" feel that fits the animated movie aesthetic. Headlines are set with extra-bold weights and tight letter-spacing to create a "blocky" comic-book feel. 

**Lexend** is utilized for labels and UI micro-copy to ensure maximum readability, especially for stats or navigation items that require a more structured, "athletic" clarity. Type should always feel large and legible; avoid thin weights entirely.

## Layout & Spacing
The design system employs a **Fluid Grid** with generous padding to prevent the UI from feeling "cramped" or "corporate." The spacing rhythm is based on an 8px scale, but emphasizes larger gaps (24px+) to allow the rounded corners of containers to breathe.

Layouts should prioritize a single clear focal point—much like a scene in a movie. Elements are grouped into "Islands" (containers) that use dynamic padding. Use asymmetrical layouts occasionally to mimic the hand-drawn nature of a pirate map, while maintaining functional alignment.

## Elevation & Depth
Elevation is achieved through **Tonal Layers** and **Hard Stylized Shadows** rather than realistic blurs. Instead of traditional soft drop shadows, use "Block Shadows"—solid color offsets (usually a darker shade of the surface color) to create a 3D "sticker" or "pop-up book" effect.

In Dark Mode, elevation is signaled through **Inner Glows**. Higher elevation surfaces should have a subtle amber inner stroke to appear as if they are catching light from a campfire. Surfaces do not use backdrop blurs; they remain opaque and bold to maintain the clean vector aesthetic.

## Shapes
The shape language is extremely organic and "bubbly." The **Roundedness level is set to 2**, but for primary action buttons and floating headers, use the **Pill-shaped (3)** logic. There should be no sharp 90-degree angles in the entire system.

Containers should feel "squishy." When possible, use slightly non-uniform corner radii (e.g., top-left and bottom-right being more rounded than the opposites) to enhance the stylized, hand-drawn vector look without sacrificing digital precision.

## Components
- **Buttons:** Large, chunky, and high-contrast. Use a "Pressed" state where the button physically shifts 2px down, hiding its block shadow to simulate a tactile click.
- **Chips:** Highly rounded with thick 2px borders. Use vibrant secondary colors (Blue/Orange) to denote categories.
- **Lists:** Cards within lists should have generous vertical spacing. Use "Compass" style icons for chevron replacements.
- **Checkboxes & Radios:** Exaggerated sizes. A "Checked" state should use a thick, stylized "X" (like a treasure spot) instead of a standard checkmark.
- **Input Fields:** Thick borders with a parchment-colored fill in light mode. On focus, the border should glow in the Primary Gold.
- **Cards:** Use a "Plank" or "Scroll" metaphor. The top of the card can feature a "Header Ribbon" in a contrasting color.
- **Additional Suggestion:** **"Loot Progress Bars"**—instead of a flat line, use a thick, rounded track with a "Golden Coin" or "Skull" as the progress indicator head.