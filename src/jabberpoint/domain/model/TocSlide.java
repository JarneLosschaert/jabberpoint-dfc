package jabberpoint.domain.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import jabberpoint.domain.toc.TocEntry;

/**
 * Marker / Placeholder pattern (replacement): a generated slide that lists all subject blocks of a slide show.
 * Instances are never persisted to XML — the TOC is a derived view built
 * from the source slides. The application service replaces every
 * {@link TocMarkerSlide} placeholder with an instance of this class before
 * the presentation starts.
 */
public final class TocSlide implements Slide {
	private static final String TOC_TITLE = "Table of Contents";
	private static final Subject TOC_SUBJECT = Subject.of(TOC_TITLE);

	private final List<TocEntry> entries;
	private final List<SlideItem> items;

	public TocSlide(List<TocEntry> entries) {
		Objects.requireNonNull(entries, "entries must not be null");
		this.entries = Collections.unmodifiableList(new ArrayList<>(entries));
		List<SlideItem> rendered = new ArrayList<>();
		for (TocEntry entry : entries) {
			rendered.add(new TextItem(1, entry.slideNumber() + ". " + entry.subject().value()));
		}
		this.items = Collections.unmodifiableList(rendered);
	}

	/** Returns the typed TOC entries backing this slide. */
	public List<TocEntry> entries() {
		return entries;
	}

	@Override
	public String title() {
		return TOC_TITLE;
	}

	@Override
	public Subject subject() {
		return TOC_SUBJECT;
	}

	@Override
	public List<SlideItem> items() {
		return items;
	}
}
