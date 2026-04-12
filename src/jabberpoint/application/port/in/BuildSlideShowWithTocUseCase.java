package jabberpoint.application.port.in;

import java.util.Optional;

import jabberpoint.domain.model.SlideShow;

/**
 * Ports & Adapters (inbound port): builds a slide show with TOC slides inserted
 * at every marker position.
 */
public interface BuildSlideShowWithTocUseCase {
    Optional<SlideShow> build(String slideShowId);
}
