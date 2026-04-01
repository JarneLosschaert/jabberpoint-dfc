package jabberpoint.application.port.in;

import java.util.List;

import jabberpoint.domain.toc.TocEntry;

/**
 * Interface for building a table of contents for a slide show. The
 * implementation of this interface is an application concern, as it involves
 * application logic to analyze the slide show and determine the structure of
 * the table of contents. The presentation layer depends on this interface to
 * obtain the data needed to render the table of contents without being coupled
 * to the underlying application logic.
 * Default: TocApplicationService, which orchestrates the use case
 * by retrieving the slide show from the repository and delegating the TOC
 * generation to a domain service.
 */
public interface BuildTocUseCase {
	List<TocEntry> buildFor(String slideShowId);
}
