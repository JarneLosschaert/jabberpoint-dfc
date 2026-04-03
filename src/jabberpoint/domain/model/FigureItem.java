package jabberpoint.domain.model;

import java.util.Objects;

/**
 * A visual element shown on a slide, referenced by a source path or URL.
 * Called "Figure" in the ubiquitous language; the source identifies the
 * underlying image file.
 */
public final class FigureItem implements SlideItem {
	private final String source;

	public FigureItem(String source) {
		String normalizedSource = Objects.requireNonNull(source, "source must not be null").trim();
		if (normalizedSource.isEmpty()) {
			throw new IllegalArgumentException("Figure source cannot be empty");
		}
		this.source = normalizedSource;
	}

	public String source() {
		return source;
	}

	@Override
	public String renderText() {
		return source;
	}
}
