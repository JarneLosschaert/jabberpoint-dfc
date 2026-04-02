package jabberpoint.domain.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class SlideShow {
	private final String id;
	private final String title;
	private final List<Slide> slides;

	public SlideShow(String id, String title, List<Slide> slides) {
		String normalizedId = Objects.requireNonNull(id, "id must not be null").trim();
		String normalizedTitle = Objects.requireNonNull(title, "title must not be null").trim();
		if (normalizedId.isEmpty()) {
			throw new IllegalArgumentException("Slide show id cannot be empty");
		}
		if (normalizedTitle.isEmpty()) {
			throw new IllegalArgumentException("Slide show title cannot be empty");
		}
		this.id = normalizedId;
		this.title = normalizedTitle;
		this.slides = Collections.unmodifiableList(new ArrayList<>(Objects.requireNonNull(slides, "slides must not be null")));
	}

	public static Builder builder(String id, String title) {
		return new Builder(id, title);
	}

	public static final class Builder {
		private final String id;
		private final String title;
		private List<Slide> slides = List.of();

		public Builder(String id, String title) {
			this.id = Objects.requireNonNull(id, "id must not be null").trim();
			this.title = Objects.requireNonNull(title, "title must not be null").trim();
		}

		public Builder slides(List<Slide> slides) {
			this.slides = Objects.requireNonNull(slides, "slides must not be null");
			return this;
		}

		public SlideShow build() {
			return new SlideShow(id, title, slides);
		}
	}

	public String id() {
		return id;
	}

	public String title() {
		return title;
	}

	public List<Slide> slides() {
		return slides;
	}

	/**
	 * Returns a new SlideShow with the same identity and title but a
	 * different slide list. Use this when deriving a modified version of the slide
	 * show (e.g. with TOC slides inserted), so that callers never need to repeat
	 * the other fields and remain correct if new fields are added later.
	 */
	public SlideShow withSlides(List<Slide> newSlides) {
		return new SlideShow(id, title, newSlides);
	}
}
