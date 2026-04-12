package jabberpoint.application.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;

import jabberpoint.application.port.in.BuildSlideShowWithTocUseCase;
import jabberpoint.application.port.out.SlideShowRepository;
import jabberpoint.domain.model.Slide;
import jabberpoint.domain.model.SlideShow;
import jabberpoint.domain.model.TocMarkerSlide;
import jabberpoint.domain.model.TocSlide;
import jabberpoint.domain.toc.TocEntry;
import jabberpoint.domain.toc.TocGenerator;

/**
 * Strategy pattern (context) + Ports & Adapters (application service):
 * loads the slide show via the repository port, delegates entry generation to the
 * {@link jabberpoint.domain.toc.TocGenerator} strategy, and replaces every
 * {@link jabberpoint.domain.model.TocMarkerSlide} with a rendered {@link jabberpoint.domain.model.TocSlide}.
 */
public final class TocApplicationService implements BuildSlideShowWithTocUseCase {

	private static final Logger LOG = Logger.getLogger(TocApplicationService.class.getName());

	private final SlideShowRepository slideShowRepository;
	private final TocGenerator tocGenerator;

	public TocApplicationService(SlideShowRepository slideShowRepository, TocGenerator tocGenerator) {
		this.slideShowRepository = Objects.requireNonNull(slideShowRepository, "slideShowRepository");
		this.tocGenerator = Objects.requireNonNull(tocGenerator, "tocGenerator");
	}

	@Override
	public Optional<SlideShow> build(String slideShowId) {
		LOG.info("Building slide show with TOC for '" + slideShowId + "'");
		Optional<SlideShow> slideShowOpt = slideShowRepository.findById(slideShowId);
		if (slideShowOpt.isEmpty()) {
			LOG.warning("Slide show '" + slideShowId + "' not found; returning empty");
			return Optional.empty();
		}
		SlideShow original = slideShowOpt.get();
		List<Slide> originalSlides = original.slides();
		List<TocEntry> entries = tocGenerator.generate(originalSlides);
		List<Slide> newSlides = new ArrayList<>();
		for (Slide slide : originalSlides) {
			if (slide instanceof TocMarkerSlide) {
				newSlides.add(new TocSlide(entries));
			} else {
				newSlides.add(slide);
			}
		}
		long replacedCount = originalSlides.stream().filter(s -> s instanceof TocMarkerSlide).count();
		LOG.info("Built slide show '" + slideShowId + "' with " + replacedCount + " TOC placeholder(s) replaced");
		return Optional.of(original.withSlides(newSlides));
	}
}
