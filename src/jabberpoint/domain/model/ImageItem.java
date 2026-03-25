package jabberpoint.domain.model;

import java.util.Objects;

public final class ImageItem implements SlideItem {
	private final String source;

	public ImageItem(String source) {
		String normalizedSource = Objects.requireNonNull(source, "source").trim();
		if (normalizedSource.isEmpty()) {
			throw new IllegalArgumentException("Image source cannot be empty");
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
