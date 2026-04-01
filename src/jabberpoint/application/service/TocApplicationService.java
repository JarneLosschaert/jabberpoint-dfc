package jabberpoint.application.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import jabberpoint.application.port.in.BuildTocUseCase;
import jabberpoint.application.port.in.BuildSlideShowWithTocUseCase;
import jabberpoint.application.port.out.SlideShowRepository;
import jabberpoint.domain.model.Slide;
import jabberpoint.domain.model.SlideShow;
import jabberpoint.domain.toc.TocEntry;
import jabberpoint.domain.toc.TocGenerator;
import jabberpoint.domain.toc.TocSlideFactory;

/*
 * Application service for building a table of contents for a slide show. This
 * service orchestrates the use case by retrieving the slide show from the
 * repository and delegating the TOC generation to a domain service. The
 * application service does not contain any business logic itself, but coordinates
 * the interaction between the domain and infrastructure layers.
*/

public final class TocApplicationService implements BuildTocUseCase, BuildSlideShowWithTocUseCase {
	private final SlideShowRepository slideShowRepository;
	private final TocGenerator tocGenerator;

	public TocApplicationService(SlideShowRepository slideShowRepository, TocGenerator tocGenerator) {
		this.slideShowRepository = Objects.requireNonNull(slideShowRepository, "slideShowRepository");
		this.tocGenerator = Objects.requireNonNull(tocGenerator, "tocGenerator");
	}

	@Override
	public List<TocEntry> buildFor(String slideShowId) {
		// Application orchestration only: fetch aggregate and delegate TOC policy to domain service.
		SlideShow slideShow = slideShowRepository.findById(slideShowId)
				.orElseThrow(() -> new IllegalArgumentException("Unknown slide show id: " + slideShowId));
		return tocGenerator.generate(slideShow.slides());
	}

	@Override
	public Optional<SlideShow> buildWithTableOfContents(String slideShowId) {
		Optional<SlideShow> slideShowOpt = slideShowRepository.findById(slideShowId);
		if (slideShowOpt.isEmpty()) {
			return Optional.empty();
		}
		SlideShow original = slideShowOpt.get();
		List<Slide> originalSlides = original.slides();
		List<TocEntry> entries = tocGenerator.generate(originalSlides);
		List<Slide> newSlides = new ArrayList<>();
		for (Slide slide : originalSlides) {
			if (slide.isTableOfContentsPlaceholder()) {
				newSlides.add(TocSlideFactory.create(entries));
			} else {
				newSlides.add(slide);
			}
		}
		return Optional.of(new SlideShow(original.id(), original.title(), newSlides));
	}
}
