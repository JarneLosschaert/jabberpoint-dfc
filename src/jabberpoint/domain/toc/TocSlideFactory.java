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
public final class TocSlideFactory {

    private TocSlideFactory() {
        // utility class
    }

    public static Slide create(List<TocEntry> entries) {
        List<SlideItem> items = new ArrayList<>();
        for (TocEntry entry : entries) {
            items.add(new TextItem(0, entry.slideNumber() + ". " + entry.subject().value()));
        }
        return new Slide.Builder("Table of Contents")
                .subject(Subject.of("Table of Contents"))
                .items(items)
                .build();
    }
}
