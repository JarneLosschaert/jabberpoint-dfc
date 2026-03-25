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
		String normalizedId = Objects.requireNonNull(id, "id").trim();
		String normalizedTitle = Objects.requireNonNull(title, "title").trim();
		if (normalizedId.isEmpty()) {
			throw new IllegalArgumentException("Slide show id cannot be empty");
		}
		if (normalizedTitle.isEmpty()) {
			throw new IllegalArgumentException("Slide show title cannot be empty");
		}
		this.id = normalizedId;
		this.title = normalizedTitle;
		this.slides = Collections.unmodifiableList(new ArrayList<>(Objects.requireNonNull(slides, "slides")));
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
}
