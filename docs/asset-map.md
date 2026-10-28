# Recover — Asset Audit, Focus Flow Mapping & Enforcement Architecture (Phase 0)

## 1. Executive Summary of Asset Audit

A recursive audit of the workspace identified **41 image files** (1 root file `card.png` and 40 files inside `assets/` across 5 top-level directories and 1 subdirectory). All original files, directory names, casing, and spacing have been preserved untouched.

### Classification Counts
- **PRODUCTION ASSET**: **9 files** (`assets/app logos/recover-logo-winged-black.png`, `assets/app logos/recover-wordmark.png`, `assets/app logos/splash-winged.png`, `assets/focus session/box-3d.png`, `assets/onboarding/onboarding assets/onboarding-sky-bg.png`, `assets/onboarding/onboarding assets/onboarding-phone-1.png`, `assets/onboarding/onboarding assets/onboarding-phone-2.png`, `assets/onboarding/onboarding assets/onboarding-phone-3.png` [confirmed by user for onboarding slides 1–3], and root `card.png` as the base white Focus Card design with dynamic text overlay handling).
- **REFERENCE SCREENSHOT/FRAME**: **32 files** (`assets/splash screen/*`, `assets/onboarding/3.png`–`10.png`, `assets/dashboard/*`, `assets/focus session/focustimer.png`, and `assets/focus session/ezgif-frame-001.jpg`–`ezgif-frame-016.jpg`).
- **AMBIGUOUS — DO NOT USE UNTIL CONFIRMED** (`AMBIGUOUS — CONFIRM`): **0 files** (all 3 previously ambiguous onboarding phone assets were explicitly confirmed by the user as production assets for the onboarding carousel).

---

## 2. Complete Asset Inventory & Classification Table

| # | Relative Path | Dimensions (W×H) | Format / Pixel Format | Size (Bytes) | Classification | Target Screen / Component | Sequence | Confidence | Visual Audit Findings & Notes |
|---|---|---|---|---|---|---|---|---|---|
| 1 | `card.png` | `1674×940` | PNG (`32bpp ARGB`) | 1,544,273 | **PRODUCTION ASSET** *(with dynamic text caveat)* | Wallet / Dashboard / Active Session — Single White Focus Card | Global v1 Card | High | High-res white Focus Card artwork with black winged logo and silver EMV chip. **Note:** Contains baked-in raster text (`CARD NO. 0116931`, `182.00 credits`, `ZENDAYA'S CARD`) on a solid white outer canvas. Original file must be preserved; UI must render live ledger balance and user name natively over a clean derived/composable white card surface. |
| 2 | `assets/app logos/recover-logo-winged-black.png` | `120×92` | PNG (`32bpp ARGB`) | 1,991 | **PRODUCTION ASSET** | White Focus Card top-left mark, light-surface brand badge | Global Brand | High | Clean black 3-bar winged "R" mark on a transparent background. Ready for direct use on light surfaces. |
| 3 | `assets/app logos/recover-wordmark.png` | `285×41` | PNG (`32bpp ARGB`) | 2,942 | **PRODUCTION ASSET** | Auth screen hero wordmark, top app bar brand header | Global Brand | High | White `RECOVER` wordmark (winged leading `R`) on a transparent background. Ready for direct use on dark/blue surfaces. |
| 4 | `assets/app logos/splash-winged.png` | `400×400` | PNG (`32bpp ARGB`) | 10,650 | **PRODUCTION ASSET** | Splash screen centered logo, Pro offer app icon mark, launcher icon source | `01-Splash` / Global | High | White 3-bar winged "R" mark with subtle gray outline on a transparent `400×400` canvas. |
| 5 | `assets/splash screen/1.png` | `1254×1254` | PNG (`24bpp RGB`) | 1,064,587 | **REFERENCE SCREENSHOT/FRAME** | Splash Screen (`SplashRoute`) | `01-Splash-A` | High | Hardware phone frame mockup showing solid bright-blue splash screen (`#007AFF`/`#0080FF`) with centered white winged logo. Recreate natively in Compose. |
| 6 | `assets/splash screen/2.jpg` | `736×736` | JPEG (`24bpp RGB`) | 17,302 | **REFERENCE SCREENSHOT/FRAME** | Splash / Welcome CTA State (`SplashGetStartedRoute`) | `01-Splash-B` | High | Hardware phone frame mockup showing solid blue background, centered white circle/dot, and bottom black pill CTA `"Get Started →"`. Recreate natively in Compose. |
| 7 | `assets/onboarding/3.png` | `851×1848` | PNG (`24bpp RGB`) | 1,376,796 | **REFERENCE SCREENSHOT/FRAME** | Onboarding "How It Works" Carousel — Slide 1 of 4 | `02-Onboarding-1` | High | Sky background, `"HOW IT WORKS"` pill, headline `"How Recover Helps You Overcome Social-Media Addiction"`, central phone preview (`25:00 Focus Timer` + white card), caption `"START A FOCUS SESSION AND EARN SCREEN TIME BY THE MINUTE."`, 4-dot indicator (dot 1 magenta), black `"Next"` button. |
| 8 | `assets/onboarding/4.png` | `852×1846` | PNG (`24bpp RGB`) | 1,391,353 | **REFERENCE SCREENSHOT/FRAME** | Onboarding "How It Works" Carousel — Slide 2 of 4 | `02-Onboarding-2` | High | Same header/sky layout; central phone preview shows crimson screen `"Motion Detected! You can't move your phone during this session / Strike 1/3"`, caption `"PUT YOUR PHONE DOWN - MOTION DETECTION KEEPS YOU HONEST."`, dots 1–2 magenta, black `"Next"` button. |
| 9 | `assets/onboarding/5.png` | `852×1846` | PNG (`24bpp RGB`) | 1,322,287 | **REFERENCE SCREENSHOT/FRAME** | Onboarding "How It Works" Carousel — Slide 3 of 4 | `02-Onboarding-3` | High | Same header/sky layout; central phone preview shows white `"Withdraw"` screen (`15 min / 15 credits`, quick chips `15m 30m 1h 2h 4h`, numeric keypad, black `"Withdraw"` button), caption `"READY TO SCROLL? WITHDRAW TO UNLOCK YOUR APPS - ANYTIME."`, dots 1–3 magenta, black `"Next"` button. |
| 10 | `assets/onboarding/6.png` | `851×1849` | PNG (`24bpp RGB`) | 1,477,600 | **REFERENCE SCREENSHOT/FRAME** | Onboarding "How It Works" Carousel — Slide 4 of 4 | `02-Onboarding-4` | High | Headline `"Your Focus Card is how you pay for screen time."`, 4 bullet points, dots 1–4 magenta, black `"Continue"` button. **Spec Override:** Reference illustration depicts a fan of colored/wooden cards (`PETER'S CARD`), but v1 rules mandate using only the single white Focus Card and no card gallery. |
| 11 | `assets/onboarding/7.png` | `1111×1416` | PNG (`24bpp RGB`) | 1,049,199 | **REFERENCE SCREENSHOT/FRAME** | Sign-In / Sign-Up Screen (`AuthRoute`) | `03-Auth` | High | Phone mockup with deep blue upper canvas + centered `RECOVER` wordmark, and bottom black sheet containing `"Continue with Google"`, `"Sign up with email"`, `"Log in"`, and Terms/Privacy links. Real auth only if configured; otherwise honest unconfigured/offline state. |
| 12 | `assets/onboarding/8.png` | `941×1672` | PNG (`24bpp RGB`) | 1,533,144 | **REFERENCE SCREENSHOT/FRAME** | Recover Pro Offer Screen (`ProOfferRoute`) | `04-ProOffer` | High | Dark cloud sky background, top-right `"Skip →"` action, black rounded-square app icon with white winged logo, `"Recover Pro"`, glassmorphic card with plan dropdown, `"₹199 /month"`, white pill `"Upgrade to Recover Pro"`, 4 feature bullets, Terms/Privacy/Restore links. `"Skip →"` must work; no fake billing. |
| 13 | `assets/onboarding/9.png` | `941×1672` | PNG (`24bpp RGB`) | 1,346,411 | **REFERENCE SCREENSHOT/FRAME** | Consolidated Permissions Setup (`PermissionsSetupRoute`) | `05-Permissions` | High | Black surface, `"App Permissions"` title, 3 dark cards with toggles: `Accessibility Service` (blue shield icon), `Notifications` (yellow bell icon), `Display over other apps` (purple overlapping squares icon), `"why these permissions are needed"` link, white `"Finish"` pill button. Switches must reflect real OS state, never local pretend state. |
| 14 | `assets/onboarding/10.png` | `1080×2400` | PNG (`32bpp ARGB`) | 179,395 | **REFERENCE SCREENSHOT/FRAME** | Installed Distracting Apps Selector (`AppSelectorRoute`) | `06-AppSelector` | High | Dark surface, `"Distracting Apps"` header with close `"×"` button, category/app rows with circular selection checkboxes, bottom white pill `"Continue (3 selected)"`. **Spec Override:** Reference lists hardcoded/iOS apps (`FaceTime`, `Instagram`, `TikTok`, `YouTube`); implementation must query actual eligible installed packages on the device. |
| 15 | `assets/onboarding/onboarding assets/onboarding-sky-bg.png` | `576×1024` | PNG (`32bpp ARGB`) | 969,120 | **PRODUCTION ASSET** | Background texture for Onboarding "How It Works" Carousel (`02-Onboarding-1..4`) | `02-Onboarding-BG` | High | Clean blue sky with white clouds artwork (`576×1024`), free of baked text or device frames. Can be used directly as the background layer for the onboarding carousel. |
| 16 | `assets/onboarding/onboarding assets/onboarding-phone-1.png` | `475×903` | PNG (`32bpp ARGB`) | 463,092 | **PRODUCTION ASSET** *(User Confirmed)* | Center illustration for Onboarding Slide 1 (`assets/onboarding/3.png`) | `02-Onboarding-1-Asset` | High | Confirmed by user as production asset for Onboarding Slide 1 (`3.png`). Rendered centered over `onboarding-sky-bg.png`. |
| 17 | `assets/onboarding/onboarding assets/onboarding-phone-2.png` | `472×911` | PNG (`32bpp ARGB`) | 423,748 | **PRODUCTION ASSET** *(User Confirmed)* | Center illustration for Onboarding Slide 2 (`assets/onboarding/4.png`) | `02-Onboarding-2-Asset` | High | Confirmed by user as production asset for Onboarding Slide 2 (`4.png`). Rendered centered over `onboarding-sky-bg.png`. |
| 18 | `assets/onboarding/onboarding assets/onboarding-phone-3.png` | `475×941` | PNG (`32bpp ARGB`) | 342,215 | **PRODUCTION ASSET** *(User Confirmed)* | Center illustration for Onboarding Slide 3 (`assets/onboarding/5.png`) | `02-Onboarding-3-Asset` | High | Confirmed by user as production asset for Onboarding Slide 3 (`5.png`). Rendered centered over `onboarding-sky-bg.png`. |
| 19 | `assets/dashboard/dashboard1.png` | `1080×2400` | PNG (`32bpp ARGB`) | 319,089 | **REFERENCE SCREENSHOT/FRAME** | Main Dashboard (`DashboardRoute` — Card tab) | `07-Dashboard` | High | Black/charcoal UI: top bar (`Settings` gear, `recover` title, `Pro` pill), Focus Card (`15 credits`, `JJ'S CARD` — note: v1 uses the white Focus Card design), circular actions (`Transactions`, `Withdraw`, `Analytics`), 2×2 bento widgets (`Recover Pro`, `Time Focused`, `Choose distracting ...`, `Focus Streak`), `+ Edit widgets` pill, white primary CTA `"Add funds / Start a focus session to earn"`, bottom nav (`Card`, `Rankings`, `Account`). |
| 20 | `assets/dashboard/transcation5.png` | `1080×2400` | PNG (`32bpp ARGB`) | 153,692 | **REFERENCE SCREENSHOT/FRAME** | Wallet & Transactions (`TransactionsRoute`) | `08-Transactions` | High | Back chevron, avatar + `"Welcome, Jj!"`, Focus Card at top, bottom sheet `"Operations"` with `"Full records"` action and date-grouped ledger entries (`Welcome Reward / +15 credits`). Note: filename is spelled `transcation5.png` on disk. |
| 21 | `assets/dashboard/leaderboard2.png` | `1080×2400` | PNG (`32bpp ARGB`) | 247,200 | **REFERENCE SCREENSHOT/FRAME** | Rankings Screen (`RankingsRoute` — Rankings tab) | `09-Rankings` | High | `"Recover Ranks"` top bar, `"Results • Top 100 card holders"` summary card (`Resets in`, `Global rank`, `Time`), and ranked list (`#1`–`#8`). Without a configured live backend, must show honest unconfigured/offline state rather than hardcoded fake rankings (`Malo`, `Tonkster`, etc.) or `"Test 1 User"` debug button. |
| 22 | `assets/dashboard/profile3.png` | `1080×2400` | PNG (`32bpp ARGB`) | 220,039 | **REFERENCE SCREENSHOT/FRAME** | Profile & Settings (`AccountRoute` — Account tab) | `10-Profile` | High | `"My profile"` header, avatar, name, `@handle`, bio, `"15m focused time • 15 credits"` pill, `"Edit profile"` button, `"Recover Pro"` status card, `"Legal & Policies"` section (`FAQ & How it works`, `Terms & Conditions`, `Privacy Policy`, `Legal Notices & Licenses`), red `"Delete Account"` button. **Note:** Reference shows `"DEVELOPER OPTIONS: Pro User Mode"` toggle; must not ship fake entitlement bypass in production. |
| 23 | `assets/dashboard/permissionpopup3.png` | `1080×2400` | PNG (`32bpp ARGB`) | 163,619 | **REFERENCE SCREENSHOT/FRAME** | Subscription Management & Delete Account Confirmation Dialog (`SubscriptionRoute` / `DeleteAccountDialog`) | `11-Subscription-DeleteModal` | High | **Misleading filename alert:** Despite the name `permissionpopup3.png`, visual inspection shows the `"Subscription — Manage your plan and billing"` screen (`CURRENT PLAN: Recover Pro`, `Billing History`, `Cancel Subscription`, `Delete Account`, `Need help? FAQ`) overlaid by a white modal dialog `"Delete Account — All your data will be permanently deleted from the database. [Cancel] [Delete]"`. |
| 24 | `assets/focus session/focustimer.png` | `1080×2400` | PNG (`32bpp ARGB`) | 84,625 | **REFERENCE SCREENSHOT/FRAME** | Focus Duration Setup (`FocusDurationSetupRoute` — Step 1) | `12-FocusSetup-Duration` | High | White background screen: top-left `"×"` close button, `"Step 1"`, title `"Select session time"`, circular slider dial showing `"20:00 / Minutes"`, `-5m` and `+5m` adjustment pills, bottom blue pill button `"Next step"`. |
| 25 | `assets/focus session/box-3d.png` | `499×857` | PNG (`32bpp ARGB`) | 453,598 | **PRODUCTION ASSET** | AR / Camera Focus Zone 3D Lock-Box Overlay (`ArSetupRoute`) | `13-FocusSetup-AR-Asset` | High | Transparent PNG of the translucent blue 3D gridded lock-box with a white padlock icon on top. Directly rendered over the live camera preview during mandatory AR/presence zone placement. |
| 26 | `assets/focus session/ezgif-frame-001.jpg` | `720×1280` | JPEG (`24bpp RGB`) | 64,633 | **REFERENCE SCREENSHOT/FRAME** | AR Zone Placement Flow — Initial Box Placement | `13-FocusFlow-Frame-001` | High | External POV photo of user holding phone with live camera feed over desk; blue 3D lock-box placed on laptop; bottom overlay reads `"Focus zone placed / Drag to reposition. Pinch to resize. Rotate to align. Place your phone face-down inside to begin."` |
| 27 | `assets/focus session/ezgif-frame-002.jpg` | `720×1280` | JPEG (`24bpp RGB`) | 73,976 | **REFERENCE SCREENSHOT/FRAME** | AR Zone Placement Flow — Dragging Box Begin | `13-FocusFlow-Frame-002` | High | Continuation of frame 001 as user's finger touches the blue 3D lock-box to drag it from the laptop toward the desk mat. |
| 28 | `assets/focus session/ezgif-frame-003.jpg` | `720×1280` | JPEG (`24bpp RGB`) | 87,433 | **REFERENCE SCREENSHOT/FRAME** | AR Zone Placement Flow — Repositioning to Desk Mat | `13-FocusFlow-Frame-003` | High | User drags the blue 3D lock-box onto the right side of the desk mat. |
| 29 | `assets/focus session/ezgif-frame-004.jpg` | `720×1280` | JPEG (`24bpp RGB`) | 88,182 | **REFERENCE SCREENSHOT/FRAME** | AR Zone Placement UI Reference (Direct Screen View) | `13-FocusFlow-Frame-004` | High | Full-screen direct capture of the camera preview UI: top-left circular down-chevron button, perspective grid on desk surface, blue 3D lock-box (`box-3d.png`) with white padlock, bottom dark gradient with `"Focus zone placed / Drag to reposition. Pinch to resize. Rotate to align. Place your phone face-down inside to begin."` |
| 30 | `assets/focus session/ezgif-frame-005.jpg` | `720×1280` | JPEG (`24bpp RGB`) | 73,385 | **REFERENCE SCREENSHOT/FRAME** | AR Zone Placement Flow — Pinch/Resize Interaction | `13-FocusFlow-Frame-005` | High | External POV showing two-finger pinch/rotate gesture adjusting the blue 3D lock-box on the desk mat. |
| 31 | `assets/focus session/ezgif-frame-006.jpg` | `720×1280` | JPEG (`24bpp RGB`) | 60,112 | **REFERENCE SCREENSHOT/FRAME** | AR Zone Placement Flow — Zone Aligned on Desk | `13-FocusFlow-Frame-006` | High | User releases fingers after aligning the 3D blue lock-box on the desk mat; ready to place phone into zone. |
| 32 | `assets/focus session/ezgif-frame-007.jpg` | `720×1280` | JPEG (`24bpp RGB`) | 46,263 | **REFERENCE SCREENSHOT/FRAME** | AR Zone Placement Flow — Moving Phone Toward Zone | `13-FocusFlow-Frame-007` | High | User tilts and lowers the phone toward the designated desk zone. |
| 33 | `assets/focus session/ezgif-frame-008.jpg` | `720×1280` | JPEG (`24bpp RGB`) | 27,854 | **REFERENCE SCREENSHOT/FRAME** | AR Zone Placement Flow — Entering Lock-Box Zone | `13-FocusFlow-Frame-008` | High | Phone is lowered directly onto the desk surface inside the virtual blue lock-box, which expands to fill the viewport. |
| 34 | `assets/focus session/ezgif-frame-009.jpg` | `720×1280` | JPEG (`24bpp RGB`) | 20,793 | **REFERENCE SCREENSHOT/FRAME** | Presence Lock-In / Countdown Ring (`FocusLockingInState`) | `13-FocusFlow-Frame-009` | High | Phone resting on desk mat showing bright blue screen, top-left collapse chevron, centered white padlock inside a circular countdown/progress ring, and `"Locked in"` label below. |
| 35 | `assets/focus session/ezgif-frame-010.jpg` | `720×1280` | JPEG (`24bpp RGB`) | 20,001 | **REFERENCE SCREENSHOT/FRAME** | Session Pre-Start / Lock Confirmation Summary (`FocusReadyState`) | `13-FocusFlow-Frame-010` | High | Blue screen showing tilted white Focus Card (`702 credits`), brain/focus icon, session title & duration (`Deep Work`), white pill badge (`+90 credits` potential reward), and bottom metadata row: `"AR lock box — AR containment • forfeit on 3rd strike"`. |
| 36 | `assets/focus session/ezgif-frame-011.jpg` | `720×1280` | JPEG (`24bpp RGB`) | 20,195 | **REFERENCE SCREENSHOT/FRAME** | Session Start Ready State (`FocusStartConfirmState`) | `13-FocusFlow-Frame-011` | High | Dark-top to blue-bottom vertical gradient screen showing the white Focus Card (`702`) in the lower half and a full-width white pill button `"Start session"` at the bottom. |
| 37 | `assets/focus session/ezgif-frame-012.jpg` | `720×1280` | JPEG (`24bpp RGB`) | 19,225 | **REFERENCE SCREENSHOT/FRAME** *(Special Mandatory Rule)* | **User-Confirmed Early-End-Session Result Screen** (`EarlyEndResultRoute`) & Active Session Visual Layout Reference | `13-FocusFlow-Frame-012` | High | Depicts a dark-to-blue vertical gradient screen with top status banner card, large centered readout (`1:29:59` / subtitle), white Focus Card (`702.01`), and bottom white pill button. **Mandatory Spec Rule:** `ezgif-frame-012.jpg` is the visual reference for the native result screen displayed **ONLY** when the user taps **End Session** during an active focus session and confirms ending early (showing `0 credits` earned under all-or-nothing policy and a **Return to Dashboard** button). Never display the JPEG itself. |
| 38 | `assets/focus session/ezgif-frame-013.jpg` | `720×1280` | JPEG (`24bpp RGB`) | 23,126 | **REFERENCE SCREENSHOT/FRAME** | Active Focus Session — Stationary Desk State | `13-FocusFlow-Frame-013` | High | Wide shot of workspace while phone rests untouched on the desk mat displaying the active session interface. |
| 39 | `assets/focus session/ezgif-frame-014.jpg` | `720×1280` | JPEG (`24bpp RGB`) | 25,687 | **REFERENCE SCREENSHOT/FRAME** | Active Focus Session — User Reaches for Phone | `13-FocusFlow-Frame-014` | High | User's hand grabs the phone on the desk mat during an active timer (`29:56` visible on screen). |
| 40 | `assets/focus session/ezgif-frame-015.jpg` | `720×1280` | JPEG (`24bpp RGB`) | 27,426 | **REFERENCE SCREENSHOT/FRAME** | Presence Violation / Motion Warning (`FocusMotionWarningState`) | `13-FocusFlow-Frame-015` | High | User lifts phone; screen switches to dark charcoal interception UI: `"No phone allowed"` header, explanatory subtitle, and a circular progress countdown ring around a vibrating phone icon prompting the user to put the phone back down before a strike is committed. |
| 41 | `assets/focus session/ezgif-frame-016.jpg` | `720×1280` | JPEG (`24bpp RGB`) | 27,753 | **REFERENCE SCREENSHOT/FRAME** | Presence Violation / Motion Warning Continuation | `13-FocusFlow-Frame-016` | High | Subsequent frame of the `"No phone allowed"` circular countdown ring as the user holds the phone. If not returned flat within the grace countdown, records a strike (`Strike 1/3`, `2/3`, `3/3` as shown in `assets/onboarding/4.png`). |

---

## 3. Production Logo & Single White Focus Card Identification

### 3.1 Approved Production Logo Artwork
1. **`assets/app logos/recover-logo-winged-black.png` (`120×92` RGBA PNG)**:
   - The black 3-bar winged "R" emblem. Used on light surfaces, specifically the top-left corner of the white Focus Card.
2. **`assets/app logos/recover-wordmark.png` (`285×41` RGBA PNG)**:
   - The white `RECOVER` wordmark incorporating the winged "R". Used on the Sign-In/Sign-Up screen and dark/blue brand headers.
3. **`assets/app logos/splash-winged.png` (`400×400` RGBA PNG)**:
   - The white 3-bar winged "R" emblem with a subtle outline on a transparent canvas. Used on the blue splash screen, Pro offer badge, and app icon artwork.

### 3.2 Single Supplied White Focus Card (`card.png`)
- **File**: `card.png` at workspace root (`1674×940` RGBA PNG).
- **Audit Finding**: While `card.png` is the authoritative visual source for the v1 white Focus Card, the raster image contains baked-in sample strings (`CARD NO. 0116931`, `182.00 credits`, `ZENDAYA'S CARD`) and a non-transparent white outer margin.
- **Implementation Plan**:
  - Preserve `card.png` untouched at root.
  - Do not display hardcoded `182.00 credits` or `ZENDAYA'S CARD` in the app.
  - Render the white Focus Card component in Compose matching `card.png`'s exact off-white/silver gradient card body, rounded corners, metallic EMV chip, and `recover-logo-winged-black.png` top-left emblem, while binding the credit balance strictly to the Room `CreditTransaction` ledger and the cardholder label/number to actual user profile/settings state.
  - Per requirement, v1 ships **only** this white Focus Card style (no wood/gold/black card gallery).

---

## 4. Chronological Mapping of the Focus-Session Reference Sequence

Inspecting `assets/focus session/` in full reveals the complete end-to-end focus session user journey:

1. **Step 1 — Duration Selection (`focustimer.png`)**:
   - User selects planned session duration using the circular dial and `-5m` / `+5m` buttons (e.g., `20:00 Minutes`) and taps **Next step**.
2. **Step 2 — Contextual Camera Permission & AR Zone Placement (`ezgif-frame-001.jpg` – `ezgif-frame-006.jpg`, using `box-3d.png`)**:
   - App requests `android.permission.CAMERA` contextually before opening the AR/camera surface.
   - Live camera preview renders the 3D blue lock-box (`box-3d.png`) over a surface grid with gesture support (drag to reposition, pinch to resize, rotate to align) and bottom instructions: *"Focus zone placed — Drag to reposition. Pinch to resize. Rotate to align. Place your phone face-down inside to begin."*
3. **Step 3 — Phone Placement & Lock-In Countdown (`ezgif-frame-007.jpg` – `ezgif-frame-009.jpg`)**:
   - User places the phone flat onto the desk target zone (`007`–`008`).
   - Accelerometer/gravity/proximity sensors detect flat stationary placement and trigger the blue `"Locked in"` circular ring countdown (`009`).
4. **Step 4 — AR Containment Summary & Session Start (`ezgif-frame-010.jpg` – `ezgif-frame-011.jpg`)**:
   - Screen displays the white Focus Card, session mode, potential reward badge, and the rule notice: `"AR lock box — AR containment • forfeit on 3rd strike"` (`010`), transitioning to the `"Start session"` confirmation state (`011`).
5. **Step 5 — Active Focus Session (`ezgif-frame-012.jpg` layout / `ezgif-frame-013.jpg`)**:
   - Monotonic timer counts down while the phone remains stationary on the desk; selected distracting apps are enforced as blocked by the OS enforcement layer.
   - **Note on reward display**: Even though the video frame `012` shows a ticking decimal on the card (`702.01`), our specification strictly forbids showing provisional rewards as wallet balance (`PRD.md`: *"Never show provisional rewards as wallet balance"*). The card on the active session screen must show only the committed ledger balance (any potential session reward is shown separately as `"Potential +X credits on completion"`).
6. **Step 6 — Pickup / Motion Detection, Grace Countdown & Strikes (`ezgif-frame-014.jpg` – `ezgif-frame-016.jpg` + `assets/onboarding/4.png`)**:
   - When device motion/pickup is detected (`014`), the screen immediately shows the dark `"No phone allowed"` warning with a circular grace countdown ring around a vibrating phone icon (`015`–`016`), instructing the user to place the phone back flat.
   - If the user does not restore flat placement before the grace ring expires (or exceeds the motion threshold), a strike is recorded and displayed on the crimson strike screen (`"Motion Detected! You can't move your phone during this session — Strike X/3"`, per `assets/onboarding/4.png`).
   - **Third-Strike Outcome (`Strike 3/3`)**: As revealed directly on the phone screen in `ezgif-frame-010.jpg` (*"AR containment • forfeit on 3rd strike"*), reaching the 3rd strike **terminates/forfeits the active focus session immediately as failed/interrupted (`FAILED_STRIKES`) with `0` credits earned**, displays a clear 3rd-strike forfeiture summary screen (distinct from `ezgif-frame-012.jpg`), and provides a button to return to the dashboard.
   - **Emergency Exit**: Always accessible during setup, warning, and active states via explicit action (and system navigation) so the user is never trapped on a lock screen.

---

## 5. Exact `ezgif-frame-012.jpg` Trigger & State Machine Contract

Per `PRD.md`, `ARCHITECTURE.md`, `RULES.md`, `PHASES.md`, `DESIGN.md`, and `MEMORY.md`:

1. **Exclusive Trigger Condition**:
   - Session is in `ACTIVE` state → user explicitly taps **End Session** → app shows a **Confirmation Dialog** warning that ending early forfeits all session credits under the all-or-nothing reward policy.
   - **If user taps Cancel**: Dialog dismisses and session remains in `ACTIVE` state without pausing, resetting, or finalizing.
   - **If user taps Confirm**:
     1. Session state machine transitions atomically and idempotently from `ACTIVE` → `ENDED_EARLY` (persisted in Room with `endReason = USER_CONFIRMED_EARLY_END`, `rewardStatus = ZERO_EARNED_EARLY_END`, `creditsEarned = 0`).
     2. Under the **all-or-nothing reward policy**, `0` credits are awarded (no positive credit ledger entry is created; wallet balance remains unchanged).
     3. Navigation routes to `EarlyEndResultRoute`, which renders a **native Jetpack Compose screen visually modeled on `assets/focus session/ezgif-frame-012.jpg`** (dark-to-blue vertical gradient surface, top status banner, large central result readout showing **`0 credits earned`** / ended early status, the user's white Focus Card with their actual committed ledger balance, and a primary bottom white pill button labeled **"Return to Dashboard"**).
2. **Prohibited Uses**:
   - Never display `ezgif-frame-012.jpg` itself as a bitmap or background image.
   - Never route to `EarlyEndResultRoute` on normal timer completion (`COMPLETED_NORMALLY` uses its own `NormalCompletionRoute` which banks `1 credit × full planned focused minutes` idempotently).
   - Never route to `EarlyEndResultRoute` on 3rd-strike forfeiture, permission revocation, app launch, or dialog cancellation.
   - Repeated taps, back navigation, or configuration/process recreation must not re-run finalization or duplicate transactions.

---

## 6. Proposed Enforcement & Permission Architecture (Decision Record)

### 6.1 Target SDK & Package Discovery (`<queries>`)
- **`minSdk = 26` (Android 8.0)**, **`targetSdk = 34` (Android 14 / ready for 35)**.
- **Installed-App Discovery without `QUERY_ALL_PACKAGES`**:
  - Under Android 11+ (API 30+) package visibility rules, declaring `<queries><intent><action android:name="android.intent.action.MAIN" /><category android:name="android.intent.category.LAUNCHER" /></intent></queries>` in `AndroidManifest.xml` allows `PackageManager.queryIntentActivities()` / `LauncherApps.getActivityList()` to discover all launchable user-facing installed apps **without** requesting the restricted `QUERY_ALL_PACKAGES` permission (which Google Play restricts for focus apps).
  - Exclude Recover's own package name, system dialer/emergency packages (`com.android.phone`, `com.android.server.telecom`), and system settings/launcher packages where appropriate so the user cannot accidentally lock themselves out of emergency calls or OS settings.
  - Persist selected apps by `packageName` in Room (`blocked_apps` table) and allow editing from both Onboarding and the Dashboard `"Choose distracting..."` widget.

### 6.2 Real OS Blocking & Foreground Detection Mechanism
- **Selected Primary Mechanism**: **`AccessibilityService` (`RecoverEnforcementAccessibilityService`) + Activity/Window Interception + Optional System Alert Overlay (`SYSTEM_ALERT_WINDOW`)**.
  - **Why `AccessibilityService`**: Confirmed by reference screenshot `assets/onboarding/9.png`, which explicitly requests:
    1. **Accessibility Service**: *"Allow Recover to detect and restrict selected distracting apps during focus sessions."*
    2. **Notifications** (`POST_NOTIFICATIONS` on API 33+): *"Receive focus session updates, reminders, and completion alerts."*
    3. **Display over other apps** (`SYSTEM_ALERT_WINDOW`): *"Allow Recover to show overlays like blocks, timers, and important alerts on top of other apps."*
  - **How Enforcement Works (Not Just a Visual Overlay)**:
    1. `RecoverEnforcementAccessibilityService` listens for `TYPE_WINDOW_STATE_CHANGED` (and `TYPE_WINDOWS_CHANGED` fallback for split-screen/multi-window) to identify the active foreground `packageName` in real time.
    2. **Policy Evaluation (`EnforcementEngine`)**:
       - Ignore system UI, input methods (keyboards), permission controllers, phone/emergency dialer, and Recover itself.
       - Check if `packageName` is in the persisted `BlockedApp` set (and whether blocking applies during active sessions and/or persistent "Withdraw to unlock" mode — see Section 7).
       - Query Room/in-memory cache for an active `UnlockGrant` for `packageName` where `status == ACTIVE` and `currentMonotonicElapsedRealtime < expiryElapsedRealtime` (with wall-clock fallback cross-checked across reboots).
       - **If a valid unexpired `UnlockGrant` exists**: Allow foreground execution and schedule/maintain an exact monotonic expiry callback.
       - **If NO valid `UnlockGrant` exists**:
         - Immediately enforce blocking by launching `BlockingInterceptionActivity` (`FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_SINGLE_TOP`, task-affinity isolated) **and/or** executing `AccessibilityService.performGlobalAction(GLOBAL_ACTION_HOME)` followed by presenting the native blocking/unlock screen, reinforced by the window overlay if `SYSTEM_ALERT_WINDOW` is granted.
         - Because enforcement redirects the foreground task away from the blocked package (`GLOBAL_ACTION_HOME` + `BlockingInterceptionActivity`) and re-evaluates on every window state transition, **dismissing or hiding an overlay never bypasses the block**.

### 6.3 1-Credit / 60-Second Temporary Unlock & Expiry Re-Block
- **Atomic Unlock Flow**:
  1. User requests a 60-second unlock for a specific blocked `packageName` (cost = `1` credit).
  2. Domain generates a deterministic `idempotencyKey` (e.g., `unlock:${packageName}:${requestUuid}`).
  3. Inside a single Room `@Transaction`:
     - Verify `idempotencyKey` does not already exist in `credit_transactions`. If it exists, return the existing `UnlockGrant` without double-charging.
     - Query `SUM(delta)` from `credit_transactions`. If balance `< 1`, abort with `InsufficientCredits` (no debit, no grant).
     - Insert `CreditTransaction(delta = -1, type = UNLOCK_DEBIT, relatedPackage = packageName, idempotencyKey = key)`.
     - Insert `UnlockGrant(packageName = packageName, grantedAtEpochMs = nowWall, grantedAtElapsedMs = nowElapsed, durationMs = 60_000, expiryElapsedMs = nowElapsed + 60_000, expiryEpochMs = nowWall + 60_000, bootCount = currentBootCount, transactionId = txId, state = ACTIVE)`.
  4. If any step before grant activation fails, the Room transaction rolls back so **failed unlocks never charge**.
- **Independent Expiry & Re-Block**:
  - A foreground/enforcement coroutine timer using `SystemClock.elapsedRealtime()` tracks the exact 60,000 ms window.
  - At `expiryElapsedMs`, the service marks the `UnlockGrant` as `EXPIRED` in Room, removes the package exception, and immediately checks the current foreground package via `AccessibilityService.rootInActiveWindow?.packageName` / last known foreground package.
  - If the user is still inside that package when the 60 seconds expire, `RecoverEnforcementAccessibilityService` immediately triggers `GLOBAL_ACTION_HOME` + `BlockingInterceptionActivity` to re-block the app, **even if the countdown overlay was hidden or dismissed**.

### 6.4 Process Death, Reboot, Clock-Tampering & Permission Revocation Recovery
- **Monotonic Time + Boot Count**:
  - Active sessions and unlock grants persist both `SystemClock.elapsedRealtime()` and `Settings.Global.BOOT_COUNT` alongside wall-clock `System.currentTimeMillis()`.
  - Within the same boot (`bootCount` unchanged), elapsed duration is computed strictly from `SystemClock.elapsedRealtime()`, making timers immune to user manual clock changes.
  - Across a device reboot (`BOOT_COMPLETED` broadcast receiver), `bootCount` increments; recovery reconciles remaining time using persisted timestamps and immediately expires any 60-second unlock grant whose window has passed.
- **Permission Revocation Handling**:
  - On every `ON_RESUME`, service unbind, and periodic enforcement heartbeat, `PermissionMonitor` queries real Android APIs:
    - Accessibility: `AccessibilityManager.getEnabledAccessibilityServiceList()` / `Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES`
    - Overlay: `Settings.canDrawOverlays(context)`
    - Notifications: `ContextCompat.checkSelfPermission(POST_NOTIFICATIONS)` / `NotificationManagerCompat.areNotificationsEnabled()`
    - Camera: `ContextCompat.checkSelfPermission(CAMERA)`
  - If Accessibility is revoked during an active session, session status transitions to an explicit degraded/interrupted warning banner and prompts re-enablement rather than pretending blocking is still active.

### 6.5 Google Play Policy & OEM Constraints Disclosure
- **Google Play Accessibility Policy**: Apps using `AccessibilityService` must present a prominent in-app disclosure explaining what data is accessed (window package names to detect selected distracting apps) and that no screen content or personal data is collected or transmitted off-device. Our onboarding permission screen (`assets/onboarding/9.png`) and `"why these permissions are needed"` modal will include this exact prominent disclosure.
- **OEM Background Restrictions (Xiaomi/MIUI, Samsung OneUI, Oppo/ColorOS, Vivo, OnePlus)**: Aggressive OEM battery killers can stop accessibility/foreground services. We run an active foreground service (`specialUse` / `systemExempted` where compliant with notification) during active sessions/unlocks and surface an honest OEM battery-optimization troubleshooting guide in Settings without requesting blanket `REQUEST_IGNORE_BATTERY_OPTIMIZATIONS` unless verified necessary.
- **Physical-Device Test Plan**: All enforcement, app discovery, overlay, 60-second unlock expiry, app switching, and AR/sensor flows will be verified via `adb` on a physical Android device or documented honestly if no physical device is attached during automated builds.

---

## 7. Uncertainties, Reference Discrepancies & Open Decisions

Before starting Phase 1, the following findings from comparing the visual references against the written specifications are recorded for confirmation:

1. **Onboarding Phone Cutouts (`onboarding-phone-1.png`, `2.png`, `3.png`)**:
   - Marked `AMBIGUOUS — DO NOT USE UNTIL CONFIRMED` because they include iPhone hardware bezels, iOS status bars (`9:41`, Dynamic Island), and static sample data (`182.00 credits`, `ZENDAYA'S CARD`).
   - **Proposed Resolution**: Recreate those three inner preview cards natively in Jetpack Compose inside the onboarding carousel slides (using `onboarding-sky-bg.png` as the slide background), avoiding any iPhone bezel bitmaps.
2. **White Focus Card (`card.png`) Baked-In Text**:
   - `card.png` has `CARD NO. 0116931`, `182.00 credits`, and `ZENDAYA'S CARD` baked into the image pixels, and `dashboard1.png`/`transcation5.png` show a dark gray card while `onboarding/6.png` shows wooden/colored cards.
   - **Proposed Resolution**: Per spec (*"Use only the single supplied white Focus Card asset in v1; no gallery"* and *"Ledger is balance source of truth"*), preserve `card.png` unmodified on disk, and build a reusable `WhiteFocusCard` composable that matches `card.png`'s exact white/silver card surface, EMV chip, and `recover-logo-winged-black.png` logo while rendering live Room ledger credits and user name dynamically.
3. **Third-Strike Outcome (`Strike 3/3`)**:
   - `ezgif-frame-010.jpg` explicitly states on screen: **`"AR lock box — AR containment • forfeit on 3rd strike"`**.
   - **Proposed Resolution**: Lock the 3rd-strike behavior to **immediate session forfeiture (`FAILED_STRIKES`) with `0` credits earned**, navigating to a dedicated Strike-Forfeiture Result Screen (separate from the `ezgif-frame-012.jpg` user-confirmed early-end screen and separate from normal completion).
4. **1-Credit / 60-Second Unlock vs. `onboarding/5.png` ("Withdraw 15 min / 15 credits")**:
   - `onboarding/5.png` and `onboarding-phone-3.png` depict a `"Withdraw"` keypad with `15m / 30m / 1h / 2h / 4h` chips (`15 min = 15 credits`), whereas `PRD.md` and `RULES.md` lock: **"One credit unlocks one selected app for 60 seconds."**
   - **Proposed Resolution**: Enforce the locked rule from `PRD.md`/`RULES.md`: **1 credit unlocks 1 selected blocked app for 60 seconds** (while keeping the clean white "Withdraw / Unlock" visual aesthetic from `onboarding/5.png` adapted to selecting a blocked app and unlocking it for 60 seconds for 1 credit).
5. **When Blocking Applies (During Active Focus Session vs. Always-On Until Unlocked)**:
   - `PRD.md` states *"Block selected apps during focus; one credit/60 seconds unlock for one selected package"*, while `onboarding/5.png` states *"READY TO SCROLL? WITHDRAW TO UNLOCK YOUR APPS - ANYTIME."*
   - **Proposed Resolution**: Selected distracting apps are blocked whenever blocking is active (always enforced during an active Focus Session, and optionally enforced on the home/outside-session state so users spend 1 earned credit for a 60-second unlock of a selected app).
6. **AR / Presence Implementation on Devices Without ARCore**:
   - `ezgif-frame-001`–`008` show a camera preview with a perspective grid and the 3D lock-box (`box-3d.png`) that can be dragged, pinched, and rotated before placing the phone flat on the desk (`ezgif-frame-009`–`016`).
   - **Proposed Resolution**: Implement the camera preview using **CameraX** overlaid with an interactive Compose canvas rendering the perspective surface grid and draggable/pinch-resizable/rotatable `box-3d.png` lock-box, paired with hardware `SensorManager` (`TYPE_GRAVITY` / `TYPE_ACCELEROMETER` + `TYPE_PROXIMITY`) to verify flat face-down/desk placement and detect motion/pickups for the `"No phone allowed"` grace countdown and 3-strike system. This works across all physical Android devices with a camera and accelerometer without failing on non-ARCore devices, while showing an explicit error/fallback if Camera permission is denied or hardware is missing.
