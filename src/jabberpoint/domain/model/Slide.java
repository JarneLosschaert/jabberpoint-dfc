package jabberpoint.domain.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class Slide {
	private final String title;
	private final Subject subject;
	// For now: a simple boolean flag to indicate whether this slide is a
	// placeholder for the table of contents. This can be changed later with
	// explicit slide subtypes for types of slides.
	private final boolean tableOfContentsPlaceholder;
	private final List<SlideItem> items;

	private Slide(String title, Subject subject, boolean tableOfContentsPlaceholder, List<SlideItem> items) {
		String normalizedTitle = Objects.requireNonNull(title, "title must not be null").trim();
		// For now: every slide has a title. This can be changed later with explicit
		// slide subtypes for types of slides.
		if (normalizedTitle.isEmpty()) {
			throw new IllegalArgumentException("Slide title cannot be empty");
		}
		this.title = normalizedTitle;
		this.subject = Objects.requireNonNull(subject, "subject must not be null");
		this.tableOfContentsPlaceholder = tableOfContentsPlaceholder;
		this.items = Collections.unmodifiableList(new ArrayList<>(Objects.requireNonNull(items, "items must not be null")));
	}

	public String title() {
		return title;
	}

	public Subject subject() {
		return subject;
	}

	public boolean isTableOfContentsPlaceholder() {
		return tableOfContentsPlaceholder;
	}

	public List<SlideItem> items() {
		return items;
	}

	/**
	 * Builder for {@link Slide}. Use this instead of calling the constructor
	 * directly. Only the title is mandatory; all other fields have sensible
	 * defaults so that adding a new field to {@code Slide} in the future only
	 * requires updating this builder, not every call site.
	 *
	 * <pre>{@code
	 * Slide slide = new Slide.Builder("Introduction")
	 *     .subject(Subject.of("Overview"))
	 *     .items(List.of(new TextItem(0, "Hello")))
	 *     .build();
	 * }</pre>
	 */
	public static final class Builder {
		private final String title;
		private Subject subject = Subject.unknown();
		private boolean tableOfContentsPlaceholder = false;
		private List<SlideItem> items = List.of();

		public Builder(String title) {
			this.title = Objects.requireNonNull(title, "title must not be null");
		}

		public Builder subject(Subject subject) {
			this.subject = Objects.requireNonNull(subject, "subject must not be null");
			return this;
		}

		public Builder items(List<SlideItem> items) {
			this.items = Objects.requireNonNull(items, "items must not be null");
			return this;
		}

		/**
		 * Marks this slide as a table-of-contents placeholder. The slot will be
		 * replaced by a generated TOC slide before the presentation starts.
		 */
		public Builder tocPlaceholder() {
			this.tableOfContentsPlaceholder = true;
			return this;
		}

		public Slide build() {
			return new Slide(title, subject, tableOfContentsPlaceholder, items);
		}
	}
}
