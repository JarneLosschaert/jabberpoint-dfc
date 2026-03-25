package jabberpoint.application.port.out;

import java.util.Optional;

import jabberpoint.domain.model.SlideShow;

/**
 * Responsibility: provide slide shows to the application layer.
 * Reason for abstraction: domain/application do not depend on XML, files, or databases.
 * Dependency direction: infrastructure adapters implement this outbound port.
 */
public interface SlideShowRepository {
	Optional<SlideShow> findById(String slideShowId);
}
