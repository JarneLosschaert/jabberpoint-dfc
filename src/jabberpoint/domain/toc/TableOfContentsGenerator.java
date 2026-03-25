package jabberpoint.domain.toc;

import java.util.List;

import jabberpoint.domain.model.Slide;

/**
 * Responsibility: convert an ordered slide sequence into table-of-contents entries.
 * Reason for abstraction: the generation policy can change independently from domain entities.
 * Dependency direction: callers in application/infrastructure depend on this interface; concrete
 * implementations depend only on the domain model.
 */
public interface TableOfContentsGenerator {
	List<TableOfContentsEntry> generate(List<Slide> orderedSlides);
}
