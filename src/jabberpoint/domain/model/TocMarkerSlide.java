package jabberpoint.domain.model;

import java.util.List;

/**
 * Marker / Placeholder pattern (marker): marks the position in the slide sequence where a TOC slide should be inserted.
 * The presenter declares this position in the source XML as a {@code <toc/>} element.
 * During loading, the parser produces a {@code TocMarkerSlide} to preserve that positional
 * intent inside the domain model. Before the presentation starts, {@code TocApplicationService}
 * replaces every marker with a fully rendered {@link TocSlide}.
 * This class carries no state: its only role is to mark a position in the slide sequence.
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
