package jabberpoint.application.port.in;

import java.util.Optional;

import jabberpoint.domain.model.SlideShow;

/**
 * Interface for the use case of building a slide show with a table of contents (TOC).
 * The implementation of this interface is an application concern, as it involves
 * application logic to retrieve the slide show, generate the TOC, and insert it into
 * the slide list. The presentation layer depends on this interface to obtain the modified
 * slide show without being coupled to the underlying application logic.
 */
public interface BuildSlideShowWithTocUseCase {
    Optional<SlideShow> build(String slideShowId);
}
