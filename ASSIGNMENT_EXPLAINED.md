# Assignment Explanation — IM0603 Design for Change, Extra Feature

## Overview

This is the **first assignment** of the Open Universiteit course "Design for Change" (IM0603). It counts for **2/3 of the final grade**. It is a **team project (2 students)**. Each team implements a separate "Extra Feature" on top of the existing JabberPoint slide show software.

Your assigned extra feature is: **Inhoudsopgave (Table of Contents)**.

---

## The Extra Feature: Table of Contents (Inhoudsopgave)

### What it does

JabberPoint must **automatically generate** a Table of Contents (TOC) slide and insert it wherever the user places a `<toc/>` marker in the XML. The TOC lists the **subjects** of the presentation.

### How subjects work

- Every slide gets a **subject** (onderwerp).
- Multiple consecutive slides with the **same subject** form a single **subject block** → listed **once** in the TOC.
- If a subject **reappears after an interruption** by a different subject, it appears **again** in the TOC.
- If a slide has **no subject**, it is categorized under a default subject (e.g. "Onbekend onderwerp").

### Optional features

- Highlight the **currently active subject** in the TOC (different style).
- Show the subject on every slide (except TOC slides).
- Number the TOC entries.
- Custom styling for the TOC (Figure 1 in the assignment is not prescriptive).

### DTD changes required

The assignment specifies extending the DTD with at least:

```xml
<!ELEMENT subject (#PCDATA)>
<!ELEMENT toc (#PCDATA)>
```

You are free to design a completely different DTD or use XML Schema instead.

### Important considerations

- **Future-proofing**: What happens when slides are added or removed? The TOC should be dynamically generated, not static.
- **Persistence**: Generated TOC slides should **not** be saved when writing to XML. Only the `<toc/>` marker is persisted, the actual TOC is regenerated on load.
- **New concept**: The TOC introduces a new concept into the domain. Think about what responsibilities belong to it.

---

## The 7 Opdrachten (Tasks)

### OPDRACHT 1 — Ubiquitous Language (UL)

**What to do:**
- Review and update the Ubiquitous Language to include concepts needed for the TOC feature (e.g., Subject, Subject Block, Table of Contents Slide, TOC Marker).
- Include the **original UL in the appendix** of your report.
- In the **report body**, show only the parts **relevant to the extra feature**.
- Explicitly describe what changed, what was added, and what didn't need to change.

**Weight:** Counts **2x** in grading.

---

### OPDRACHT 2 — Domain Design

**What to do:**
- Adapt the domain design (class diagrams) so the TOC feature is included.
- The design must be **flexible** and prepared for future changes (even if that requires major refactoring of the original design).
- Include the **original domain design in the appendix**.
- In the **report body**, describe everything relevant to the extra feature.
- Describe every **abstract class** and every **interface** in terms of **responsibilities** (verantwoordelijkheden).
- Do NOT include object creation yet (that's Opdracht 4).

**Weight:** Counts **2x** in grading.

---

### OPDRACHT 3 — UI and Other Layers

**What to do:**
- Adapt the user interface and other architectural layers for the extra feature.
- Include the **original design in the appendix**.
- In the **report body**, show the parts relevant to the extra feature.
- Describe what you had to change, or how the existing design already accommodated the feature.

**Weight:** Counts **2x** in grading.

---

### OPDRACHT 4 — Object Creation

**What to do:**
- Adapt object creation for the new feature.
- Include the **original design in the appendix**.
- In the **report body**, describe everything relevant to the extra feature and what had to change.

**Key constraints from the additional assignment text:**
- The **domain must not know about data formats** (XML knowledge stays in the infrastructure layer).
- The **services layer** should offer the ability to create a slide show (e.g., create empty slideshow, add slide, add item to last slide).
- The **infrastructure layer** drives the creation (reading XML), but the domain objects are created via the services layer.
- Alternatively, a **controller** can act as intermediary (knows both infrastructure and domain, while infrastructure itself doesn't know the domain).
- For **writing**: the services layer can only deliver data; you must design how to query the needed data for export.

**Weight:** (part of normal grading)

---

### OPDRACHT 5 — Design Patterns

**What to do:**
For each pattern used in the **entire design**, document:
1. **Name** of the pattern
2. **Class diagram** (for each occurrence of the pattern)
3. **Table** with each class/interface and its **role** in the pattern (for each occurrence)
4. **Reasoning** why you chose that pattern in that location (for each occurrence)

Also discuss **alternative patterns** you considered but rejected, and explain why.

**Weight:** Counts **3x** in grading (heaviest weight!).

---

### OPDRACHT 6 — Individual Reflection

**What to do:** Each team member reflects **individually** on:
- How well was the original design prepared for incorporating the feature?
- How is the design prepared for future changes?
- How was the experience of starting from a ubiquitous language and working from there?
- How was the collaboration in the team?
- How was the method of sharing results?

**Weight:** (part of normal grading)

---

### OPDRACHT 7 — Working Program

**What to do:**
- Deliver a **working program** with source code as an Eclipse project.
- The UI must be implemented in **Swing and/or AWT** (not JavaFX — teachers need to review it).
- A **test presentation** (demo XML) is recommended for higher scores.

**Weight:** Counts **1x** in grading.

---

## Architecture Constraints

The assignment enforces an **Onion Architecture**:

```
┌─────────────────────────────────────────┐
│              UI Layer                   │
│  (Swing frames, controllers)           │
├─────────────────────────────────────────┤
│          Infrastructure Layer           │
│  (XML persistence, repositories)       │
├─────────────────────────────────────────┤
│          Application Layer              │
│  (Services, Use Cases / Ports)         │
├─────────────────────────────────────────┤
│           Domain Layer                  │
│  (Entities, Value Objects, Rules)      │
│  *** Must be independent ***           │
└─────────────────────────────────────────┘
```

- The **domain** (including services) must be **independent** of all other layers — no imports from infrastructure, UI, or external frameworks.
- **Object creation** must be **separated from object usage**.
- The domain should **not know about XML** or any specific data format.

---

## Grading Rubric Summary

| Criterion | Weight | Key to scoring "Goed" (7-8) |
|-----------|--------|-------------------------------|
| Problem Analysis & UL | 2x | Clear definitions, clear relationships, explicit decisions on missing info |
| Design | 2x | Flexible domain; UI separated from domain; clear package structure; creation separated from use; well-placed responsibilities |
| Description (diagrams + text) | 2x | Clean class diagrams at specification level; descriptions in terms of responsibilities |
| Patterns + argumentation | **3x** | Patterns correctly applied; reasoned arguments for each; alternatives discussed and argued away |
| Implementation | 1x | Code matches design; code is clean; program works as requested; test presentation included |

---

## Deliverables Checklist

- [ ] PDF report submitted to ANS (per student)
- [ ] Source code submitted to ANS (per student)
- [ ] Report appendix: original UL
- [ ] Report appendix: original domain design
- [ ] Report appendix: original UI design
- [ ] Report appendix: original object creation design
- [ ] Report body: UL changes for extra feature
- [ ] Report body: domain design for extra feature (class diagrams + responsibility descriptions)
- [ ] Report body: UI and other layers for extra feature
- [ ] Report body: object creation for extra feature
- [ ] Report body: all design patterns documented (name, diagram, role table, reasoning)
- [ ] Report body: individual reflection (per student)
- [ ] Working Eclipse project with Swing/AWT UI
- [ ] Test presentation XML file
- [ ] Participate in 1-hour discussion with teachers
