package jabberpoint.domain.toc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import jabberpoint.domain.model.Slide;
import jabberpoint.domain.model.Subject;

public final class ConsecutiveSubjectTableOfContentsGenerator implements TableOfContentsGenerator {
	@Override
	public List<TableOfContentsEntry> generate(List<Slide> orderedSlides) {
		List<Slide> safeSlides = Objects.requireNonNull(orderedSlides, "orderedSlides");
		List<TableOfContentsEntry> entries = new ArrayList<>();
		Subject previousSubject = null;

		for (int index = 0; index < safeSlides.size(); index++) {
			Slide slide = safeSlides.get(index);
			// Placeholder slides are insertion markers and should never appear as TOC content.
			if (slide.isTableOfContentsPlaceholder()) {
				continue;
			}

			Subject currentSubject = slide.subject();
			// Consecutive deduplication rule
			if (!currentSubject.equals(previousSubject)) {
				entries.add(new TableOfContentsEntry(index + 1, currentSubject));
				previousSubject = currentSubject;
			}
		}

		return Collections.unmodifiableList(entries);
	}
}
