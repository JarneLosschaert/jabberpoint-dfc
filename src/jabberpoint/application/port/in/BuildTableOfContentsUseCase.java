package jabberpoint.application.port.in;

import java.util.List;

import jabberpoint.domain.toc.TableOfContentsEntry;

/**
 * Responsibility: orchestrate TOC building for one slide show.
 * Reason for abstraction: UI can trigger use cases without knowing implementation details.
 * Dependency direction: UI depends on this inbound port; implementation depends inward on domain.
 */
public interface BuildTableOfContentsUseCase {
	List<TableOfContentsEntry> buildFor(String slideShowId);
}
