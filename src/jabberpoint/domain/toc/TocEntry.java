package jabberpoint.domain.toc;

import jabberpoint.domain.model.Subject;

/**
 * Value Object pattern: one TOC row holding the slide number where a subject
 * block starts and the subject itself.
 */
public record TocEntry(int slideNumber, Subject subject) {
	public TocEntry {
		if (slideNumber < 1) {
			throw new IllegalArgumentException("Slide number must be 1 or greater");
		}
	}
}
