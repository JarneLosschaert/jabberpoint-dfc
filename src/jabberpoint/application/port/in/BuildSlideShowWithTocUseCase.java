package jabberpoint.application.port.in;

import java.util.Optional;

import jabberpoint.domain.model.SlideShow;

public interface BuildSlideShowWithTocUseCase {
    Optional<SlideShow> build(String slideShowId);
}
