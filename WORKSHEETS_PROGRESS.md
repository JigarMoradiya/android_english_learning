# Phonics Worksheets — Android Port (Progress & Plan)

_Last updated: 2026-11-06. Status: **PAUSED** (holding for an Android branch switch — last Play update was rejected). Nothing committed._

---

## 0. Why this doc
Capturing exactly where the worksheet port stands so we can resume cleanly after switching the Android branch. The worksheet work is a set of **new untracked files** plus edits to **3 tracked files** (listed in §4). Untracked files travel across a `git checkout`; the 3 tracked-file edits may need re-applying or carry as local changes — see §7 (Git).

---

## 1. Decision (settled)
Render worksheets **in-app with Jetpack Compose** (NOT bundle the pre-made JPGs).

- Bundling `~/Downloads/Phonics_Worksheets/` (283 JPGs, 107 MB) was rejected: too much app size.
- On-demand JPG download was considered but needs hosting + network; worksheet designs are **still being finalized**, so a code render that stays in sync with data is preferred.
- Compose render adds **~0 MB** (picture drawables already ship with the app; it's just code).
- Tradeoffs accepted: (a) a real multi-unit port to cover all levels, (b) must match the JPG polish by hand, (c) grid thumbnails need a perf fix (see §3).

Reference designs: `~/Downloads/Phonics_Worksheets/Level 1 - Letter Sounds/…` (these JPGs are exports of the iOS SwiftUI render — the "generator code" Jigar mentioned = the iOS worksheet code rendered via `WorksheetRenderHarness`).

---

## 2. DONE (verified on a physical device, screenshots taken)
New package: `app/src/main/java/com/example/myapplication/main/age_group/phonics/worksheets/`

**Unit 1 — scaffolding + Level-1 Learn letter page**
- A4 canvas (`WorksheetPage`) + header/footer/rainbow/watermark + `ScaledWorksheet` scale-to-fit.
- `WorksheetLetterPageBody`: "Aa says aaa" + picture-word grid (first letter tinted), tap-a-word audio.
- Grid screen (`LevelWorksheetsPage`) + pinch-zoom viewer (`WorksheetViewerPage`).
- Entry button "🖨️ Printable Worksheets" on `LetterSoundsIntroPage`.

**Unit 2 — completed Level 1 (minus the combined "mixed" sheet)**
- Reading ×2 (`LetterRead` — whole alphabet, plain + with-sounds).
- Activities: Circle the First Letter, Letter Matcher (+ Tricky Ones), Letter & Picture match.
- Ported `SeededGenerator` (xorshift) + `deranged` + `balancedChunks` for deterministic sheet generation.
- Per-sheet palette colours cycle (like iOS).

**Verified:** `compileDev1DebugKotlin` green; installed on device (`com.vedaavi.english.learning`, variant `dev1Debug`); Learn pages (Aa–Zz), Reading, and Activity sheets render correctly; full-screen viewer works.

**One bug fixed this session:** sheets were blank — `ScaledWorksheet` used an extra sized box + top-left transform origin. Fixed to the canonical pattern (center the fixed-size page in a `BoxWithConstraints`, scale via `graphicsLayer` around its centre).

---

## 3. KNOWN ISSUES — do these NEXT (parked)
**(A) Grid scroll is NOT smooth** ← Jigar flagged.
- Cause: each grid thumbnail re-renders a *full* worksheet (text + 6 images) scaled down; `LazyVerticalGrid` builds several at once → jank on fast scroll.
- Fix: render each sheet **once to a Bitmap** and show that bitmap in the grid (cache by spec id). Keep the live Compose render only in the full-screen `WorksheetViewerPage`. (Options: draw into an `android.graphics.Picture`/`Canvas`, or `ComposeView`→bitmap, or an offscreen `GraphicsLayer.toImageBitmap()`.)

**(B) Design fidelity — make sheets match the JPG** ← Jigar's main ask.
- Current Compose sheet ≈ the JPG but simplified:
  - Border = plain tinted frame → JPG has a **candy-stripe / per-level patterned border**.
  - Glyph "Aa" = styled system Text → JPG uses a **polished display glyph**.
- Fix: port the real **`WorksheetDecoration.swift`** (per-level border patterns, ~472 lines) and the glyph look from the iOS code (`WorksheetGlyph.swift` — `SolidGlyph`/`FilledGlyph`). Match spacing/weights to the JPG.

---

## 4. Files touched (for the branch switch)
**New (untracked) — all under `app/src/main/java/com/example/myapplication/main/age_group/phonics/worksheets/`:**
`WorksheetModels.kt`, `WorksheetMetrics.kt`, `WorksheetTheme.kt`, `WorksheetLetters.kt`, `WorksheetCatalog.kt`, `WorksheetChrome.kt`, `WorksheetLetterBodies.kt`, `WorksheetLetterActivityBodies.kt`, `WorksheetRandom.kt`, `WorksheetSheet.kt`, `WorksheetViewModel.kt`, `LevelWorksheetsPage.kt`, `WorksheetViewerPage.kt`

**Modified (tracked) — 3 files:**
- `app/src/main/java/com/example/myapplication/main/base/nav/RouteNavigation.kt` — added `LevelWorksheets` + `WorksheetViewer` route objects.
- `app/src/main/java/com/example/myapplication/main/base/nav/AppNavGraph.kt` — wired those two `composable(...)` routes into the NavHost.
- `app/src/main/java/com/example/myapplication/main/age_group/from_3_to_5/letter_phonics_sound/LetterSoundsIntroPage.kt` — added the "🖨️ Printable Worksheets" button + 2 imports (`clickable`, `draw.clip`).

---

## 5. ROADMAP (later units)
- **Unit 3:** the combined "Letter Practice" mixed sheet (`letterMixed` body); then design-fidelity pass (§3B) + scroll fix (§3A) if not already done.
- **Unit 4+:** Level 2 (Short Vowels) onward — needs porting the **segmented word bank** (`phonicsListenConfigs` / `ListenWord` with `.segments`) from iOS `PhonicsListenData.swift` to Kotlin, plus each new `WorksheetBody` type (shortVowelPage, missingVowel, soundBoxes, ruleGroups, sentences, sort, trace, etc.) and that level's catalog entries from iOS `WorksheetCatalog.swift` (3,764 lines).
- Per-level entry buttons on each level's intro screen (mirror the Level-1 button).
- Deferred iOS-only dev tooling is NOT needed: `WorksheetLevelGuard` (vocab/coverage) and `WorksheetRenderHarness` (PNG dump).

---

## 6. How to build / run / test
```bash
cd /Users/jigarmoradiya/Documents/newProject/eng/android
./gradlew compileDev1DebugKotlin      # fast compile check
./gradlew installDev1Debug            # build + install on device/emulator
```
- Package: `com.vedaavi.english.learning` · variant `dev1Debug` · launcher `com.example.myapplication.main.AgeCategoryActivity`.
- Nav to the feature: Home → **Phonics Journey** banner → **Letter Sounds** (Level 1) → **🖨️ Printable Worksheets**.

## 7. Git (Jigar handles commits himself)
- Nothing committed. Switching branches: the 13 new files are **untracked** and will follow you to the new branch. The 3 modified tracked files (§4) will either carry as local changes or need re-applying after checkout — check `git status` on the new branch.
- If you want to park the work cleanly before switching: `git stash -u` (includes untracked) on this branch, or commit to a WIP branch. Your call — I won't run git.

## 8. Source of truth (iOS)
- iOS worksheet code: `eng/iOS/Learn English/UI/Phonics/Worksheets/` (`WorksheetSpec/Kind/Page/Header/Footer/Theme/Metrics/Decoration/Glyph/LetterBodies/Catalog`, etc.).
- iOS data: `eng/iOS/Learn English/UI/Phonics/Phonics Listen/PhonicsListenData.swift`.
- Finished JPG designs: `~/Downloads/Phonics_Worksheets/`.
