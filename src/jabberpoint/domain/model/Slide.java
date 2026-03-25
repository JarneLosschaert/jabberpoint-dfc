package jabberpoint.domain.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class Slide {
	private final String title;
	private final Subject subject;
	private final boolean tableOfContentsPlaceholder;
	private final List<SlideItem> items;

	public Slide(String title, Subject subject, boolean tableOfContentsPlaceholder, List<SlideItem> items) {
		String normalizedTitle = Objects.requireNonNull(title, "title").trim();
		// For now: every slide has a title. This can be changed later with explicit slide subtypes for types of slides.
		if (normalizedTitle.isEmpty()) {
			throw new IllegalArgumentException("Slide title cannot be empty");
		}
		this.title = normalizedTitle;
		this.subject = Objects.requireNonNull(subject, "subject");
		this.tableOfContentsPlaceholder = tableOfContentsPlaceholder;
		this.items = Collections.unmodifiableList(new ArrayList<>(Objects.requireNonNull(items, "items")));
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
}
