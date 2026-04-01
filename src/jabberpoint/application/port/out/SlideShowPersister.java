package jabberpoint.application.port.out;

import jabberpoint.domain.model.SlideShow;

/**
 * Responsibility: persist slide shows into external representation (XML).
 * Dependency direction: infrastructure adapters implement this outbound port.
 */

public interface SlideShowPersister {
    void save(SlideShow slideShow, String fileName);
}