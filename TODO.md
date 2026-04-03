# TODO

- `List` item type — UL defines List as a distinct Item sub-concept (a collection of text items shown in sequence), currently only `TextItem` and `FigureItem` exist
- `Position` — UL defines Position as an element with a fixed position on a slide, not represented in the domain model
- `PresenterName` and `Date` — UL lists these as Meta Information shown on slides, they are not domain fields (currently just item content)
- `Presenter` and `Audience` — UL names these as participants in a Presentation, not modelled
- `Presentation` — UL defines Presentation as the event wrapping a Slide Show with a Presenter and Audience, not modelled (only SlideShow exists)
- `SlideShow States` — UL defines Not Started, Showing Slide, and Ended states, no state machine exists in the domain
- `Go To Slide` — UL lists this as an optional Presenter Action, not implemented in the UI