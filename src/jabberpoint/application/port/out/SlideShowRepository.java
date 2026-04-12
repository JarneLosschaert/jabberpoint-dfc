package jabberpoint.application.port.out;

import java.util.Optional;

import jabberpoint.domain.model.SlideShow;

/**
 * Repository + Ports & Adapters (outbound port): abstraction over slide show
 * retrieval. Infrastructure adapters implement this interface.
 */
public interface SlideShowRepository {
    Optional<SlideShow> findById(String slideShowId);
}
