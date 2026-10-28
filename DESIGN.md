# Recover — Design and Reference Mapping

## File roles
Inspect all files under `assets/`. The visible top-level folders are `app logos/`, `dashboard/`, `focus session/`, `onboarding/`, and `splash screen/`; actual paths on disk take precedence. Each file must be classified in `docs/asset-map.md`:
- **PRODUCTION ASSET**: actual logo/artwork/card/icon to use.
- **REFERENCE SCREENSHOT/FRAME**: visual or flow evidence to recreate natively.
- **AMBIGUOUS — CONFIRM**: do not ship until clarified.
File extension does not determine role. A photo of a physical phone is not the finished app UI.

## Visual direction
Premium minimal mobile UI. Dashboard/focus: black and charcoal surfaces, white text, restrained blue. Onboarding/splash: supplied blue/white style where shown. Use white surfaces only on reference screens that use them. Avoid excessive gradients, neon glow and generic redesigns. Match hierarchy, spacing, proportions, colors, radii, type and button placement. Respect system bars, cutouts, accessibility and varied screen sizes.

Use supplied logo artwork directly when classified as production asset. Use only the single supplied white Focus Card for the first release; no card gallery. Preserve original files; derived optimized copies must be separate.

## Flow
Follow the actual supplied references, including:
Splash → swipeable How It Works → auth UI if shown → Pro offer/Skip → consolidated permission setup → real installed-app selector → dashboard → focus duration → contextual camera permission → camera/AR setup → countdown → active session. Then use separate routes for normal completion and user-confirmed early end. Include wallet/transactions/unlock, analytics, rankings, account/settings and edit-blocked-apps.

## Focus-session frames
Inspect `ezgif-frame-001.jpg` through `ezgif-frame-016.jpg` in chronological order, confirming actual paths. Treat them as reference frames unless audit proves otherwise. Recreate each depicted interface natively; do not place the phone photograph in the app.

## Frame 012 special rule
`assets/focus session/ezgif-frame-012.jpg` is the reference for a native result screen shown only after an active-session user taps End Session and confirms. Cancel keeps the session active. Confirm ends early, finalizes once, earns zero credits under the all-or-nothing rule, and displays actual earned amount plus **Return to Dashboard**. Do not show it for natural timer expiry or interruptions.

Test on at least two screen sizes. Use responsive layouts rather than fixed absolute coordinates except where necessary to preserve a reference composition.
