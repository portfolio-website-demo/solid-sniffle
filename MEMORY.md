# Recover — Project Memory

## Current state
Fresh project; no source code/Gradle structure assumed. Supplied root docs and `assets/` folder. Visible folders: `app logos/`, `dashboard/`, `focus session/`, `onboarding/`, `splash screen/`. The focus-session folder visibly contains 16 JPEG frames named like `ezgif-frame-001.jpg` through `ezgif-frame-016.jpg`.

## Locked decisions
- One standalone Android app; recommended Kotlin + Jetpack Compose.
- Every asset must be audited and classified in `docs/asset-map.md`.
- Production assets are used directly; reference screenshots are recreated natively; ambiguous files require confirmation.
- App selector uses actual eligible installed apps, never guessed/hardcoded names; users can edit selection later.
- Mandatory AR/presence flow; request camera permission in context.
- Real blocking is required and must be tested on a physical device.
- One credit unlocks one selected app for 60 seconds.
- Ledger is source of truth; reward/debit operations are idempotent.
- All-or-nothing reward: only planned completion earns credits; early end earns zero.
- `ezgif-frame-012.jpg` is a reference for the result screen after explicit End Session tap and confirmation during an active session; screen shows actual credits and Return to Dashboard. It is not a background and is not the normal completion route.
- Only one supplied white Focus Card asset in v1; no card gallery.
- No fake auth, billing, ads, rankings, permissions, analytics or enforcement.

## Next step
Awaiting user's GitHub repository URL and token to push the completed Foundation + Splash & Onboarding (`1` to `10`) implementation to GitHub, then proceed to Focus Session / AR / Dashboard implementation.

## Phase 1–3 (Foundation + Splash & Onboarding `1`–`10`) Completion Log (2026-10-28)
- **Completed Tasks**:
  - Updated [`docs/asset-map.md`](docs/asset-map.md) per user confirmation that `onboarding-phone-1.png`, `onboarding-phone-2.png`, and `onboarding-phone-3.png` under `assets/onboarding/onboarding assets/` are confirmed `PRODUCTION ASSET`s for the onboarding pages.
  - Created single-app Kotlin + Jetpack Compose project (`com.recover.app`, `minSdk = 26`, `targetSdk = 35`, `compileSdk = 35`) with layered `domain/`, `data/`, `platform/`, and `ui/` packages.
  - Implemented Room persistence (`RecoverDatabase`, `RecoverDao`, `FocusSessionEntity`, `CreditTransactionEntity`, `BlockedAppEntity`, `UnlockGrantEntity`) and DataStore preferences (`UserPreferencesRepository`).
  - Implemented the complete Splash and Onboarding flow matching references `1` through `10`:
    - `1.png` (`SplashLogoScreen`): Electric blue screen with centered white winged Recover emblem.
    - `2.jpg` (`SplashWelcomeScreen`): Electric blue screen with centered white orb and bottom black pill `"Get Started →"` button.
    - `3.png`–`6.png` (`HowItWorksOnboardingScreen`): Swipeable 4-page `HorizontalPager` over `onboarding_sky_bg.png` using `onboarding_phone_1.png`, `onboarding_phone_2.png`, `onboarding_phone_3.png`, the single v1 `WhiteFocusCard` hero fan on page 4, cumulative magenta pill indicators, and `"Next"` / `"Continue"` buttons.
    - `7.png` (`AuthScreen`): Deep royal blue upper area with centered `recover_wordmark.png` and bottom black rounded sheet with `"Continue with Google"`, `"Sign up with email"`, `"Log in"`, and honest local-profile / unconfigured cloud auth disclosure.
    - `8.png` (`ProOfferScreen`): Recover Pro offer screen with working top-right `"Skip →"` button, black rounded-square app icon, dark glassmorphic membership card (`₹199 /month`), 4 feature checkmarks, and honest unconfigured Play Billing disclosure.
    - `9.png` (`PermissionsSetupScreen`): Consolidated App Permissions screen (`Accessibility Service`, `Notifications`, `Display over other apps`) bound strictly to real Android OS permission APIs (`PermissionChecker`), re-checked automatically on `ON_RESUME`, with prominent Accessibility disclosure and `"Finish"` button.
    - `10.png` (`DistractingAppsScreen`): Real installed-app selector querying `PackageManager.queryIntentActivities(MAIN/LAUNCHER)` via `InstalledAppDiscovery` (no hardcoded apps), displaying real app icons and labels, persisting selected packages in Room, and routing to `DashboardShellScreen` via `"Continue (X selected)"`.
- **Build & Test Commands Run and Results**:
  - Command: `.\gradlew.bat assembleDebug testDebugUnitTest`
  - Result: **`BUILD SUCCESSFUL in 1m 52s`** (Exit code `0`).
  - Debug APK produced: `app/build/outputs/apk/debug/app-debug.apk` (`20,440,953` bytes).
  - Unit Tests (`com.recover.app.domain.RecoverDomainRulesTest`): **8 tests run, 8 passed, 0 failures, 0 errors**.

## Phase 0 Completion Log (2026-10-28)
- **Completed Tasks**:
  - Read and audited all 6 root specifications (`PRD.md`, `ARCHITECTURE.md`, `RULES.md`, `PHASES.md`, `DESIGN.md`, `MEMORY.md`).
  - Recursively inspected all 41 image files across root (`card.png`) and `assets/` (`app logos/`, `dashboard/`, `focus session/`, `onboarding/`, `onboarding/onboarding assets/`, `splash screen/`), extracting exact dimensions, pixel formats, and byte sizes without modifying any original files.
  - Created [`docs/asset-map.md`](docs/asset-map.md) classifying all 41 files into `PRODUCTION ASSET` (6), `REFERENCE SCREENSHOT/FRAME` (32), and `AMBIGUOUS — DO NOT USE UNTIL CONFIRMED` (3).
  - Inspected `assets/focus session/ezgif-frame-001.jpg` through `ezgif-frame-016.jpg` chronologically, identified the 3rd-strike forfeiture rule shown on `ezgif-frame-010.jpg` (`"AR containment • forfeit on 3rd strike"`), and documented the special trigger rule for `ezgif-frame-012.jpg` (user-confirmed early End Session native result screen showing `0` credits earned and `Return to Dashboard`).
  - Identified misleading filename `assets/dashboard/permissionpopup3.png` (actually depicts Subscription Management + Delete Account confirmation dialog).
  - Documented the Android Enforcement & Permission Architecture Decision Record in `docs/asset-map.md`.
- **Build / Test Commands Run**:
  - None (Phase 0 is documentation and asset audit only; no Gradle project generated yet).

## Open decisions
- Confirm proposed handling of `onboarding-phone-1.png`..`3.png` (marked `AMBIGUOUS — DO NOT USE UNTIL CONFIRMED`; proposed native Compose recreation without iPhone bezels).
- Confirm dynamic Compose rendering for the white Focus Card (`card.png` has baked-in `182.00 credits` / `ZENDAYA'S CARD` text; proposed preserving `card.png` untouched and rendering live Room ledger balance on a white Focus Card component matching `card.png` with `recover-logo-winged-black.png`).
- Confirm 3rd-strike outcome (`ezgif-frame-010.jpg` specifies `"forfeit on 3rd strike"` → session terminates as `FAILED_STRIKES` with `0` credits earned and dedicated forfeiture screen).
- Auth/backend and billing/ads configuration are currently unconfigured (will show honest unconfigured/offline states).
- Physical-device validation required in later phases for app discovery, camera/AR, blocking, overlays and OEM behavior.

## Handoff
After each phase record completed tasks, actual build/test commands and results, unresolved issues, and next phase. Never claim unrun tests passed.
