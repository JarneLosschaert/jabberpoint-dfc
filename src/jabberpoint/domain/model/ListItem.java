package jabberpoint.domain.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * A slide item representing a sequence of text entries shown as a list.
 */
public final class ListItem implements SlideItem {
    private final List<TextItem> entries;

    public ListItem(List<TextItem> entries) {
        List<TextItem> normalizedEntries = new ArrayList<>(
                Objects.requireNonNull(entries, "entries must not be null"));
        if (normalizedEntries.isEmpty()) {
            throw new IllegalArgumentException("List item must contain at least one entry");
        }
        for (TextItem entry : normalizedEntries) {
            Objects.requireNonNull(entry, "list entries must not contain null");
        }
        this.entries = Collections.unmodifiableList(normalizedEntries);
    }

    public List<TextItem> entries() {
        return entries;
    }

    @Override
    public String renderText() {
        return entries.stream()
                .map(TextItem::text)
                .collect(Collectors.joining("\n"));
    }
}
