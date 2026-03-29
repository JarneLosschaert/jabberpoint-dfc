package jabberpoint.application.port.out;

import java.util.Optional;

import jabberpoint.domain.model.SlideShow;

/**
 * Interface for retrieving slide shows. The implementation of this interface is
 * an infrastructure concern, as it may involve reading from files, databases,
 * or other external sources. The application layer depends on this interface
 * to obtain slide shows without being coupled to the underlying infrastructure.
 * Default: InMemorySlideShowRepository, which provides a hardcoded demo slide
 * show for testing and development purposes (for now).
 */
public interface SlideShowRepository {
	Optional<SlideShow> findById(String slideShowId);
}
