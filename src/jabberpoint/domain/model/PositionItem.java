package jabberpoint.domain.model;

import java.util.Objects;

/**
 * A slide item that is placed at a fixed position on the slide.
 */
public final class PositionItem implements SlideItem {
    private final int x;
    private final int y;
    private final int level;
    private final String text;

    public PositionItem(int x, int y, int level, String text) {
        if (x < 0 || y < 0) {
            throw new IllegalArgumentException("Position coordinates must be zero or greater");
        }
        if (level < 0) {
            throw new IllegalArgumentException("Level must be zero or greater");
        }
        this.x = x;
        this.y = y;
        this.level = level;
        this.text = Objects.requireNonNull(text, "text must not be null").trim();
        if (this.text.isEmpty()) {
            throw new IllegalArgumentException("Position item text cannot be empty");
        }
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
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
