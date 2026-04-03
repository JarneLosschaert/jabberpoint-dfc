package jabberpoint.domain.model;

import java.util.List;

/**
 * Marks the position in a slide sequence where the application should insert
 * a generated TocSlide
 * Why this type is needed:the presenter decides where in the
 * slide sequence the table of contents appears. That position is expressed in
 * the source XML as a <toc/> child element inside a <slide>.
 * During loading the parser produces a TocMarkerSlide to preserve that
 * positional intent inside the domain model. Before the presentation starts,
 * the application service replaces every marker with a fully rendered
 * This class has no configurable state: its only role is structural
 * (marking position), so no constructor arguments or builder are needed.
 */
public final class TocMarkerSlide implements Slide {
	private static final String MARKER_TITLE = "TOC Marker";

	@Override
	public String title() {
		return MARKER_TITLE;
	}

	@Override
	public Subject subject() {
		return Subject.unknown();
	}

	/** A TOC marker holds no items; it is a positional placeholder only. */
	@Override
	public List<SlideItem> items() {
		return List.of();
	}
}
