package jabberpoint.domain.model;

import java.util.Objects;

public final class TextItem implements SlideItem {
	private final int level;
	private final String text;

	public TextItem(int level, String text) {
		if (level < 0) {
			throw new IllegalArgumentException("Level must be zero or greater");
		}
		this.level = level;
		this.text = Objects.requireNonNull(text, "text must not be null").trim();
		if (this.text.isEmpty()) {
			throw new IllegalArgumentException("Text item text cannot be empty");
		}
	}

	public int level() {
		return level;
	}

	public String text() {
		return text;
	}

	@Override
	public String renderText() {
		return text;
	}
}
