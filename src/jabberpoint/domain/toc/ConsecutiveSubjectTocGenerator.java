package jabberpoint.domain.toc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import jabberpoint.domain.model.Slide;
import jabberpoint.domain.model.Subject;

public final class ConsecutiveSubjectTocGenerator implements TocGenerator {
	@Override
	public List<TocEntry> generate(List<Slide> orderedSlides) {
		List<Slide> safeSlides = Objects.requireNonNull(orderedSlides, "orderedSlides must not be null");
		List<TocEntry> entries = new ArrayList<>();
		Subject previousSubject = null;

		for (int index = 0; index < safeSlides.size(); index++) {
			Slide slide = safeSlides.get(index);
			// Placeholder toc slides are insertion markers and should never appear as TOC
			// content. Will be removed later.
			if (slide.isTableOfContentsPlaceholder()) {
				continue;
			}

			Subject currentSubject = slide.subject();
			// Consecutive deduplication rule
			if (!currentSubject.equals(previousSubject)) {
				entries.add(new TocEntry(index + 1, currentSubject));
				previousSubject = currentSubject;
			}
		}

		return Collections.unmodifiableList(entries);
	}
}
