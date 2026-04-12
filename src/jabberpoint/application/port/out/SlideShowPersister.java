package jabberpoint.application.port.out;

import jabberpoint.domain.model.SlideShow;

/**
 * Ports & Adapters (outbound port): contract for persisting a slide show to an
 * external representation. Infrastructure adapters implement this interface.
 */
public interface SlideShowPersister {
    void save(SlideShow slideShow, String fileName);
}