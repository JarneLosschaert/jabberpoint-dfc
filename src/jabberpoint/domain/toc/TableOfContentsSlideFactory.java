package jabberpoint.domain.toc;

import java.util.ArrayList;
import java.util.List;

import jabberpoint.domain.model.Slide;
import jabberpoint.domain.model.SlideItem;
import jabberpoint.domain.model.Subject;
import jabberpoint.domain.model.TextItem;

/**
 * Factory for creating a Table of Contents slide from a list of TOC entries.
 * Responsibility: pure domain concern — turning ordered TOC entries into a
 * rendered Slide. Lives in the domain layer so that no application or
 * infrastructure class needs to know the formatting rules.
 */
public final class TableOfContentsSlideFactory {

    private TableOfContentsSlideFactory() {
        // utility class
    }

    public static Slide create(List<TableOfContentsEntry> entries) {
        List<SlideItem> items = new ArrayList<>();
        for (TableOfContentsEntry entry : entries) {
            items.add(new TextItem(0, entry.slideNumber() + ". " + entry.subject().value()));
        }
        return new Slide("Table of Contents", Subject.of("Table of Contents"), false, items);
    }
}
