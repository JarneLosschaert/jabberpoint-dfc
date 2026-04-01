package jabberpoint.domain.toc;

import jabberpoint.domain.model.Subject;

// One rendered TOC row: the first slide number where a subject block starts.
public record TocEntry(int slideNumber, Subject subject) {
	public TocEntry {
		if (slideNumber < 1) {
			throw new IllegalArgumentException("Slide number must be 1 or greater");
		}
	}
}
