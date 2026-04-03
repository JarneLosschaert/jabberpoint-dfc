package jabberpoint.domain.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * A standard content slide containing a title and one or more items.
 * This is the primary slide type for most slides in a presentation.
 */
public final class OrdinarySlide implements Slide {
	private final String title;
	private final Subject subject;
	private final List<SlideItem> items;

	public OrdinarySlide(String title, Subject subject, List<SlideItem> items) {
		String normalizedTitle = Objects.requireNonNull(title, "title must not be null").trim();
		if (normalizedTitle.isEmpty()) {
			throw new IllegalArgumentException("Slide title cannot be empty");
		}
		this.title = normalizedTitle;
		this.subject = Objects.requireNonNull(subject, "subject must not be null");
		this.items = Collections.unmodifiableList(
				new ArrayList<>(Objects.requireNonNull(items, "items must not be null")));
	}

	public static Builder builder(String title) {
		return new Builder(title);
	}

	public static final class Builder {
		private final String title;
		private Subject subject = Subject.unknown();
		private List<SlideItem> items = List.of();

		public Builder(String title) {
			this.title = Objects.requireNonNull(title, "title must not be null");
		}

		public Builder subject(Subject subject) {
			this.subject = subject;
			return this;
		}

		public Builder items(List<SlideItem> items) {
			this.items = items;
			return this;
		}

		public OrdinarySlide build() {
			return new OrdinarySlide(title, subject, items);
		}
	}

	@Override
	public String title() {
		return title;
	}

	@Override
	public Subject subject() {
		return subject;
	}

	@Override
	public List<SlideItem> items() {
		return items;
	}
}
