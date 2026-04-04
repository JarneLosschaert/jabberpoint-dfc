package jabberpoint.domain.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * The first slide of a slide show, displaying the slide show title and
 * meta information such as presenter name and date as slide items.
 * Per the domain rule "Title Slide Rule": the title slide is always the
 * first slide in the sequence.
 */
public final class TitleSlide implements Slide {
	private final String title;
	private final Subject subject;
	private final List<SlideItem> items;
	private final String presenterName;
	private final String date;

	public TitleSlide(String title, Subject subject, List<SlideItem> items, String presenterName, String date) {
		String normalizedTitle = Objects.requireNonNull(title, "title must not be null").trim();
		if (normalizedTitle.isEmpty()) {
			throw new IllegalArgumentException("Slide title cannot be empty");
		}
		this.title = normalizedTitle;
		this.subject = Objects.requireNonNull(subject, "subject must not be null");
		this.items = Collections.unmodifiableList(
				new ArrayList<>(Objects.requireNonNull(items, "items must not be null")));
		this.presenterName = presenterName != null ? presenterName.trim() : null;
		this.date = date != null ? date.trim() : null;
	}

	public static Builder builder(String title) {
		return new Builder(title);
	}

	public static final class Builder {
		private final String title;
		private Subject subject = Subject.unknown();
		private List<SlideItem> items = List.of();
		private String presenterName;
		private String date;

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

		public Builder presenterName(String presenterName) {
			this.presenterName = presenterName;
			return this;
		}

		public Builder date(String date) {
			this.date = date;
			return this;
		}

		public TitleSlide build() {
			return new TitleSlide(title, subject, items, presenterName, date);
		}
	}

	public String presenterName() {
		return presenterName;
	}

	public String date() {
		return date;
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
