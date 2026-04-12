package jabberpoint.domain.toc;

import java.util.List;

import jabberpoint.domain.model.Slide;

/**
 * Strategy pattern (strategy interface): defines the contract for generating TOC entries
 * from an ordered list of slides. The application layer depends on this interface,
 * keeping TOC policy decoupled from orchestration.
 */
public interface TocGenerator {
	List<TocEntry> generate(List<Slide> orderedSlides);
}
