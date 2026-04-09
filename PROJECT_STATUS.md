# Project Status — What's Done, What's Left, What Was Unnecessary

**Last updated:** 2026-04-09

---

## ✅ DONE — Implementation Completed

### Core TOC Feature (the assigned Extra Feature)
| Item | Status | Files |
|------|--------|-------|
| `Subject` value object with factory methods (`of`, `fromNullable`, `unknown`) | ✅ Done | `domain/model/Subject.java` |
| Default subject "Onbekend onderwerp" for slides without subject | ✅ Done | `domain/model/Subject.java` |
| `TocMarkerSlide` — placeholder in XML for where TOC should appear | ✅ Done | `domain/model/TocMarkerSlide.java` |
| `TocSlide` — generated slide with rendered TOC entries | ✅ Done | `domain/model/TocSlide.java` |
| `TocEntry` record (slide number + subject) | ✅ Done | `domain/toc/TocEntry.java` |
| `TocGenerator` interface (domain service contract) | ✅ Done | `domain/toc/TocGenerator.java` |
| `ConsecutiveSubjectTocGenerator` — groups consecutive subjects, handles repeated subjects | ✅ Done | `domain/toc/ConsecutiveSubjectTocGenerator.java` |
| `TocApplicationService` — orchestrates TOC generation and marker replacement | ✅ Done | `application/service/TocApplicationService.java` |
| `BuildSlideShowWithTocUseCase` — inbound port for building slideshow with TOC | ✅ Done | `application/port/in/BuildSlideShowWithTocUseCase.java` |
| DTD updated with `<subject>` and `<toc>` elements | ✅ Done | `jabberpoint.dtd` |
| XML parsing reads `<subject>` and `<toc>` from slides | ✅ Done | `infrastructure/repository/XmlSlideShowRepository.java` |
| XML writing persists `<toc/>` marker but skips generated `TocSlide` | ✅ Done | `infrastructure/persistence/XmlSlideShowPersister.java` |
| Active subject highlighting in TOC slide (bold + blue for current section) | ✅ Done (optional) | `ui/view/SlideShowFrame.java` |
| Subject displayed on every slide (in the UI header) | ✅ Done (optional) | `ui/view/SlideShowFrame.java` |
| TOC entries are numbered (slide number prefix) | ✅ Done (optional) | `domain/model/TocSlide.java` |

### Architecture & Infrastructure
| Item | Status | Files |
|------|--------|-------|
| Onion architecture: domain is independent of infrastructure/UI | ✅ Done | Package structure |
| `SlideShowRepository` outbound port (interface) | ✅ Done | `application/port/out/SlideShowRepository.java` |
| `SlideShowPersister` outbound port (interface) | ✅ Done | `application/port/out/SlideShowPersister.java` |
| `XmlSlideShowRepository` — reads slide show from XML | ✅ Done | `infrastructure/repository/XmlSlideShowRepository.java` |
| `XmlSlideShowPersister` — writes slide show to XML | ✅ Done | `infrastructure/persistence/XmlSlideShowPersister.java` |
| `Slide` sealed interface (exhaustive switch support) | ✅ Done | `domain/model/Slide.java` |
| `SlideItem` sealed interface | ✅ Done | `domain/model/SlideItem.java` |
| Builder pattern for `SlideShow`, `TitleSlide`, `OrdinarySlide`, `SpecialSlide` | ✅ Done | Various domain model files |
| `App.java` wires everything together (object creation in one place) | ✅ Done | `App.java` |
| Eclipse project files (`.classpath`, `.project`) | ✅ Done | Root directory |

### UI & State Machine
| Item | Status | Files |
|------|--------|-------|
| Swing UI displaying slides with navigation | ✅ Done | `ui/view/SlideShowFrame.java` |
| Previous / Next buttons | ✅ Done | `ui/view/SlideShowFrame.java` |
| Go To Slide (text field + Enter button) | ✅ Done | `ui/view/SlideShowFrame.java` |
| Keyboard navigation (UP/DOWN arrows) | ✅ Done | `ui/view/SlideShowFrame.java` |
| Rendering for all slide types (TitleSlide, OrdinarySlide, SpecialSlide, TocSlide) | ✅ Done | `ui/view/SlideShowFrame.java` |
| Rendering for all item types (TextItem, ListItem, FigureItem, PositionItem) | ✅ Done | `ui/view/SlideShowFrame.java` |
| UI uses domain state machine (`start()`, `showNextSlide()`, `showPreviousSlide()`, `goToSlide()`) | ✅ Done | `ui/view/SlideShowFrame.java` |

### Demo & Test Data
| Item | Status | Files |
|------|--------|-------|
| Comprehensive demo XML (16 slides, multiple subjects, 2 TOC markers, repeated subjects, missing subjects, all item types, OU logo on final slide) | ✅ Done | `demo.xml` |

### Ubiquitous Language
| Item | Status | Files |
|------|--------|-------|
| Glossary with all concepts | ✅ Done | `UBIQUITOUS_LANGUAGE.md` |
| Relationships section | ✅ Done | `UBIQUITOUS_LANGUAGE.md` |
| Domain rules (including TOC-specific rules) | ✅ Done | `UBIQUITOUS_LANGUAGE.md` |
| Slide show states | ✅ Done | `UBIQUITOUS_LANGUAGE.md` |
| Presenter actions | ✅ Done | `UBIQUITOUS_LANGUAGE.md` |

### Cleanup Completed
| Item | Status | Notes |
|------|--------|-------|
| Removed `StartupFrame` + `StartupController` (dead code) | ✅ Done | Were never used by `App.java` |
| Removed `BuildTocUseCase` (redundant interface) | ✅ Done | Was only used by deleted `StartupController` |
| Removed `InMemorySlideShowRepository` (unused) | ✅ Done | `App.java` uses `XmlSlideShowRepository` |
| `demo-export.xml` added to `.gitignore` | ✅ Done | Auto-generated on startup |
| `bin/` in `.gitignore` (not tracked by git) | ✅ Done | Build output only |
| `.classpath` and `.project` kept in git (not in `.gitignore`) | ✅ Done | Required for Eclipse submission |
| OU logo (`OU_Logo.jpg`) added to final slide in `demo.xml` as `FigureItem` | ✅ Done | Image stays at project root |

---

## ❌ TODO — Still Needs to Be Done

### Report (this is the bulk of remaining work)

| # | Task | Priority | Notes |
|---|------|----------|-------|
| 1 | **Write OPDRACHT 1 section** — UL changes for the TOC feature | HIGH | Show what was added/modified in the UL compared to the original. Include the original UL in the appendix. |
| 2 | **Write OPDRACHT 2 section** — Domain design | HIGH | Create class diagrams (specification-level). Describe every interface (`Slide`, `SlideItem`, `TocGenerator`, `SlideShowRepository`, `SlideShowPersister`) in terms of **responsibilities**. Include original domain design in appendix. |
| 3 | **Write OPDRACHT 3 section** — UI and other layers | HIGH | Describe architecture layers, communication flow. Include original design in appendix. |
| 4 | **Write OPDRACHT 4 section** — Object creation | HIGH | Describe Builder pattern, factory methods, `App.java` as composition root. Show creation separated from use. Include original design in appendix. |
| 5 | **Write OPDRACHT 5 section** — Design patterns | **CRITICAL** (3x weight!) | Document ALL patterns. See detailed breakdown below. |
| 6 | **Write OPDRACHT 6 section** — Individual reflection | MEDIUM | Each team member writes their own reflection. |
| 7 | **Create appendices** — original UL, original domain, original UI, original object creation | HIGH | You need the "before" versions of all designs. |

### Design Patterns to Document (OPDRACHT 5 — 3x weight!)

For **each** pattern: name, class diagram, role table, reasoning, and alternatives considered.

| Pattern | Where used | Classes involved |
|---------|-----------|-----------------|
| **Sealed Interface / Exhaustive Switch** | `Slide` hierarchy, `SlideItem` hierarchy | `Slide`, `TitleSlide`, `OrdinarySlide`, `TocSlide`, `TocMarkerSlide`, `SpecialSlide`; `SlideItem`, `TextItem`, `FigureItem`, `ListItem`, `PositionItem` |
| **Strategy Pattern** | TOC generation | `TocGenerator` (interface), `ConsecutiveSubjectTocGenerator` (concrete strategy), `TocApplicationService` (context) |
| **Repository Pattern** | Slide show loading | `SlideShowRepository` (interface), `XmlSlideShowRepository` (concrete adapter) |
| **Builder Pattern** | Object construction | `SlideShow.Builder`, `TitleSlide.Builder`, `OrdinarySlide.Builder`, `SpecialSlide.Builder` |
| **Ports & Adapters (Hexagonal)** | Application layer boundaries | `BuildSlideShowWithTocUseCase` (inbound port); `SlideShowRepository`, `SlideShowPersister` (outbound ports); infrastructure classes (adapters) |
| **Value Object** | Domain modeling | `Subject`, `TocEntry` |
| **Marker / Placeholder Pattern** | TOC insertion point | `TocMarkerSlide` → replaced by `TocSlide` at runtime |
| **Factory Method** | Subject creation | `Subject.of()`, `Subject.fromNullable()`, `Subject.unknown()` |
| **Immutable Object / State Machine** | Domain state transitions | `SlideShow` — all navigation methods return new instances; `SlideShowState` enum |

### Deliverables Checklist

| # | Item | Status |
|---|------|--------|
| 8 | PDF report submitted to ANS (per student) | ❌ Not started |
| 9 | Source code submitted to ANS (per student) | ❌ Not packaged |
| 10 | Test presentation XML file included | ✅ `demo.xml` (16 slides) |
| 11 | Participate in 1-hour discussion with teachers | ❌ Scheduled? |

---

## ⚠️ PREVIOUSLY UNNECESSARY — Now Removed or Justified

Items that were in the codebase but weren't necessary. Dead code has been **removed**; remaining items are justified below.

### Removed (no longer in codebase)
| What | Why removed |
|------|-------------|
| `StartupFrame` + `StartupController` | Dead code — never used by `App.java` |
| `BuildTocUseCase` | Only used by deleted `StartupController` |
| `InMemorySlideShowRepository` | Never instantiated — `App.java` uses `XmlSlideShowRepository` |
| `demo-export.xml` in git | Auto-generated on every startup; now in `.gitignore` |

### Still present — justify in report
| What | Why it's there | Report action needed |
|------|---------------|---------------------|
| `SpecialSlide` | In the sealed `Slide` hierarchy for future extensibility (UL concept) | Explain as future-proofing in OPDRACHT 2 |
| `PositionItem` | UL defines "Position"; implemented for UL alignment | Justify in OPDRACHT 2 as UL-driven design |
| `ListItem` | UL defines "List"; distinct from text with levels | Justify in OPDRACHT 2 as UL-driven design |

---

## Priority Summary

| Priority | Action |
|----------|--------|
| 🔴 **CRITICAL** | Write OPDRACHT 5 (patterns) — highest weight in grading (3x) |
| 🟠 **HIGH** | Write OPDRACHT 1-4 (UL, domain, UI, object creation sections) |
| 🟠 **HIGH** | Create appendices with original designs |
| 🟡 **MEDIUM** | Write individual reflection (OPDRACHT 6) |
| 🟢 **LOW** | Package source code for ANS submission |

---

## 🔍 CODE REVIEW — Issues, Misinterpretations & Improvements Found

*Added 2026-04-07 after comparing refactored code against the original JabberPoint source and the assignment description.*

---

### 🐛 Bugs

| # | Issue | Where | Impact | Status |
|---|-------|--------|--------|--------|
| 1 | **`XmlSlideShowPersister` always wrote a `<slide>` element for `TocSlide`** — ghost entries on unexpected save. | `XmlSlideShowPersister.save()` | Medium | ✅ **Fixed** — early `continue` skips `TocSlide` entirely before creating `slideElement`. |
| 2 | **`TocMarkerSlide` leaked its synthetic title `"TOC Marker"` to XML** — noisy round-trip XML. | `XmlSlideShowPersister.save()` | Low | ✅ **Fixed** — early `continue` after writing a minimal `<slide><toc/></slide>` with no title or subject. |
| 3 | **Active-subject highlighting used fragile string-suffix matching** — `endsWith(activeSubject.value())` would fail if one subject name is a suffix of another. | `SlideShowFrame.renderTocItems()` | Low | ✅ **Fixed** — `renderTocItems` now iterates `slide.entries()` directly and uses `entry.subject().equals(activeSubject)`. |
| 4 | **`TextItem` rejected whitespace-only text** — broke backward compatibility with original XML spacer items (`<item kind="text" level="1"> </item>`). | `TextItem` constructor | Medium | ✅ **Fixed** — removed the `isEmpty` guard; whitespace-only text is now allowed as a vertical spacer (text stored after trim, which results in empty string for spacers). |

---

### ⚠️ Design Issues & Things to Do Better

| # | Issue | Where | Status |
|---|-------|--------|--------|
| 5 | **`SlideItem.renderText()` investigated** — The method is **not** dead code: `XmlSlideShowPersister.appendItems()` calls `item.renderText()` to write the text content of each `<item>` element. No action needed. | `SlideItem` interface + `XmlSlideShowPersister` | ✅ **Investigated — method is in use, kept as-is** |
| 6 | **`TocSlide.entries()` was ignored in rendering** — UI used raw `TextItem` strings with fragile suffix matching instead of typed `TocEntry` list. | `SlideShowFrame.renderTocItems()` | ✅ **Fixed** — `renderTocItems` replaced to iterate `slide.entries()` with `Subject.equals()` comparison. |
| 7 | **`renderRegularItems` code was repeated three times** — identical rendering loops in `renderRegularItems`, `renderTitleSlide`, and part of `renderTocItems`. | `SlideShowFrame` | ✅ **Fixed** — extracted `renderItemList(List<SlideItem>, int startY)` helper; `renderRegularItems` and `renderTitleSlide` now delegate to it. |
| 8 | **`ListItem` XML format was ambiguous** — single-line text blob parsed as one entry instead of multiple bullets. | `demo.xml` | ✅ **Fixed** — both list items in `demo.xml` now use newline-separated entries, one bullet per line. |
| 9 | **No unit tests** — domain model and services are untested. | All layers | ❌ **Still pending** — add tests for `ConsecutiveSubjectTocGenerator` and XML round-trip. |
| 10 | **Stale Javadoc in `SlideShowRepository`** — referenced deleted `InMemorySlideShowRepository`. | `application/port/out/SlideShowRepository.java` | ✅ **Fixed** — updated to reference `XmlSlideShowRepository`. |
| 11 | **`TocSlide` constructed `TextItem(0, ...)` for every entry** — level 0 is the title style; semantically wrong for TOC entries. | `TocSlide` constructor | ✅ **Fixed** — changed to `TextItem(1, ...)`. |

---

### 🤔 Possible Misinterpretations of the Original

| # | Topic | Original behaviour | Your interpretation | Assessment |
|---|-------|--------------------|---------------------|------------|
| 12 | **`TocMarkerSlide` as a full slide** | The assignment says place `<toc/>` somewhere in the XML. No specific structure is prescribed. | Wrapped inside a `<slide>` element, making the marker occupy a slide position in the sequence. | Defensible — the TOC *replaces* a slide, so modelling it as a slide-position placeholder is consistent. Must be explained in the report. |
| 13 | **`TitleSlide` and `SpecialSlide` as distinct types** | The original `Slide` class had no subtypes — all slides were the same class. | Added `TitleSlide`, `OrdinarySlide`, `TocSlide`, `TocMarkerSlide`, `SpecialSlide` as sealed types. | Fine as a design choice, but `TitleSlide` and `SpecialSlide` go beyond the original's UL. The Ubiquitous Language identifies them, which justifies their existence; explain this explicitly in OPDRACHT 2. |
| 14 | **`BitmapItem` → `FigureItem` rename** | The original named it `BitmapItem` and it loaded a `BufferedImage` directly in the constructor (side effect). | Renamed to `FigureItem` holding only the source path; the UI loads the image lazily. | The rename aligns with the UL ("Figure"). Lazy loading in the UI is better design. Correct interpretation. |
| 15 | **Style system removed entirely** | The original `Style` class drove all font sizes, colours, and indentation. The entire visual rendering was driven by `Style.getStyle(level)`. | Replaced with hardcoded font sizes and `level` used only for text indentation via `"  ".repeat(level)`. | The Style system performed a separate concern (presentation styling). Removing it simplifies the code but loses the configurable styling. For this assignment this is acceptable, but note the regression in the report. |
| 16 | **`Observer/update` pattern removed** | The original `Presentation` used an observer—`setShowView`/`update`—to notify `SlideViewerComponent` on slide change. | Replaced with an immutable `SlideShow` + re-assignment in the frame. No observer needed. | The immutable re-assignment pattern is architecturally cleaner. Document the deliberate removal of the observer in OPDRACHT 3/5. |
