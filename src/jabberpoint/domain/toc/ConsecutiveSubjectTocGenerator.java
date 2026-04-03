package jabberpoint.domain.toc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import jabberpoint.domain.model.Slide;
import jabberpoint.domain.model.Subject;
import jabberpoint.domain.model.TocMarkerSlide;
import jabberpoint.domain.model.TocSlide;

public final class ConsecutiveSubjectTocGenerator implements TocGenerator {
	@Override
	public List<TocEntry> generate(List<Slide> orderedSlides) {
		List<Slide> safeSlides = Objects.requireNonNull(orderedSlides, "orderedSlides must not be null");
		List<TocEntry> entries = new ArrayList<>();
		Subject previousSubject = null;

		for (int index = 0; index < safeSlides.size(); index++) {
			Slide slide = safeSlides.get(index);
			// TOC marker and generated TOC slides are structural, not content;
			// they must not appear as entries in the table of contents.
			if (slide instanceof TocMarkerSlide || slide instanceof TocSlide) {
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
