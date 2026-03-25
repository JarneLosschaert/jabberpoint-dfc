# Ubiquitous Language – Slide Show Domain

# 1. Introduction

INTRODUCTION

Stefano Igbinosun & Jarne Losschaert

---

# 2. Glossary

| **Concept** | _Sub-concept_ | Meaning |
|-------------|---------------|---------|
| **Presentation** |  | The event during which a presenter presents a slide show to an audience. |
|  | _Presenter_ | The person presenting the slide show. |
|  | _Audience_ | The group of people attending the presentation. |
| **Slide Show** |  | An ordered sequence of slides presented to an audience. |
|  | _Sequence_ | The fixed order in which slides are presented. |
|  | _Subject Block_ | A contiguous sequence of slides sharing the same subject. |
| **Slide (Sheet)** |  | A visual sheet projected and visible to the audience during a presentation. |
|  | _Title Slide_ | The first slide of a slide show, showing the title and meta information of the slide show. |
|  | _Ordinary Slide_ | A slide containing a title and one or more items. |
|  | _Table of Contents Slide_ | A slide that lists the subject blocks in a structured manner. |
|  | _Special Slide_ | A slide with that differs from ordinary slides (e.g. diagrams, graphs, templates). |
| **Meta Information** |  | Information about the slide show displayed on slides. |
|  | _Slide Number_ | The position of a slide within the slide show sequence. |
|  | _Total Slides_ | The total number of slides in the slide show. |
|  | _Slide Show Title_ | The title of the slide show as shown on the title slide. |
|  | _Subject_ | The main topic or theme of a slide. |
|  | _Presenter Name_ | The name of the person presenting the slide show. |
|  | _Date_ | The date associated with the slide show. |
| **Item** |  | A content element shown on a slide. |
|  | _Text_ | A textual element displayed on a slide. |
|  | _Figure_ | A visual element shown on a slide, referenced by a source. |
|  | _List_ | A collection of text items shown in sequence on a slide. |
|  | _Level_ | The hierarchical depth of a text item in a list, determining visual formatting. |
| **Position** |  | An element with a fixed position on a slide. |

---

# 3. Relationships

> **Note:** The rules that you use to evaluate class diagrams do not apply here. We postpone that to the design phase.

- A Presentation includes exactly one Slide Show.
- A Presentation has one Presenter (not sure).
- A Presentation has an Audience.
- A Slide Show consists of one or more Slides.
- A Slide Show contains exactly one Title Slide.
- A Slide has a Slide Number.
- A Slide contains zero or more Items.
- An Ordinary Slide contains a Title and one or more Items.
- A Text Item has a Level.
- Meta Information may appear on slides.
- Every slide shows the subject of the slide.
- A Table of Contents Slide lists the subject blocks of the following slides in a structured manner.

---

# 4. Domain Model

![Domain_model_v3.jpg](uploads/753bd02527ce75f110150243e87f9f7e/Domain_model_v3.jpg){width=576 height=600}

---

# 5. Slide Show States

| State | Description |
|-------|-------------|
| Not Started | The slide show is created but not yet presented. |
| Showing Slide | One of the slides is currently visible to the audience. |
| Ended | All slides have been shown and the presentation is finished. |

---

# 6. Presenter Actions

| Action | Description |
|--------|-------------|
| Start Slide Show | Begin presenting the first slide. |
| Show Next Slide | Move to the next slide in the sequence. |
| Show Previous Slide | Return to the previous slide. |
| Go To Slide (optional) | Go to a specific slide by number. |
| End Slide Show | Stop presenting the slide show. |

---

# 7. Domain Rules

| Rule | Description |
|------|-------------|
| Title Slide Rule | The title slide is always the first slide in the sequence. |
| Slide Number Rule | Each slide has a unique number within the slide show. |
| Sequence Rule | Slides are presented in a fixed order. |
| Subject Rule | Every slide must have a subject that is displayed on the slide. |
| Table of Contents Rule | A table of contents slide lists the subject blocks in a structured manner. |
| Consecutive Subject Rule | Consecutive slides with the same subject are grouped into a single subject block for the table of contents. |
| Repeated Subject Rule | If a subject reappears after an interruption by a different subject, it is listed again in the table of contents. |
| Missing Subject Rule | If a slide does not have a subject, it is categorized under a default subject (e.g., "Onbekend onderwerp") in the table of contents. |

---

# 8. Notes

- Not sure about lists and levels.

> **Assignment:** Some items are in a list (the list may be bulleted, but not necessarily). These items usually have a level, which may dictate the font that will be used for the item, and the place on the slide (from left to right), where they start.

- Annotation? Depending on the slide projector either analog or digital.
- Is it possible to jump to a specific slide by number?
- Include container or projector?

> **Assignment:** They could be made visible on a screen using a slide projector. A container holding the separate slides created the sequence in which they were shown.