# TODO

- `List` item type — UL defines List as a distinct Item sub-concept (a collection of text items shown in sequence), currently only `TextItem` and `FigureItem` exist -> ✅ COMPLETED
- `Position` — UL defines Position as an element with a fixed position on a slide, not represented in the domain model -> (implemented als positionItem  tried to cooperate within the rest) ✅ COMPLETED
- `PresenterName` and `Date` — UL lists these as Meta Information shown on slides, they are not domain fields (currently just item content) now implemented as domain fields within TitleSlide ✅ COMPLETED
- `Presenter` and `Audience` — UL names these as participants in a Presentation, not modelled -> ( I think is not needed) -> NOT clear ???
- `Presentation` — UL defines Presentation as the event wrapping a Slide Show with a Presenter and Audience, not modelled (only SlideShow exists) -> same with former point ???
- `SlideShow States` — UL defines Not Started, Showing Slide, and Ended states, no state machine exists in the domain -> ✅ COMPLETED
- `Go To Slide` — UL lists this as an optional Presenter Action, not implemented in the UI -> GoToSlide method implemented in slideshow.java and slideshowFrame.java implemented ✅ COMPLETED