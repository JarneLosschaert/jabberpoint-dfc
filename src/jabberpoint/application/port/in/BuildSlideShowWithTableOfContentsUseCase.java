package jabberpoint.application.port.in;

import java.util.Optional;

import jabberpoint.domain.model.SlideShow;

public interface BuildSlideShowWithTableOfContentsUseCase {
    Optional<SlideShow> buildWithTableOfContents(String slideShowId);
}