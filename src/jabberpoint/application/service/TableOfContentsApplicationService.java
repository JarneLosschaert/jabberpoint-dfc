package jabberpoint.application.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import jabberpoint.application.port.in.BuildTableOfContentsUseCase;
import jabberpoint.application.port.in.BuildSlideShowWithTableOfContentsUseCase;
import jabberpoint.application.port.out.SlideShowRepository;
import jabberpoint.domain.model.Slide;
import jabberpoint.domain.model.SlideItem;
import jabberpoint.domain.model.SlideShow;
import jabberpoint.domain.model.Subject;
import jabberpoint.domain.model.TextItem;
import jabberpoint.domain.toc.TableOfContentsEntry;
import jabberpoint.domain.toc.TableOfContentsGenerator;

/*
 * Application service for building a table of contents for a slide show. This
 * service orchestrates the use case by retrieving the slide show from the
 * repository and delegating the TOC generation to a domain service. The
 * application service does not contain any business logic itself, but coordinates
 * the interaction between the domain and infrastructure layers.
*/

public final class TableOfContentsApplicationService implements BuildTableOfContentsUseCase, BuildSlideShowWithTableOfContentsUseCase {
	private final SlideShowRepository slideShowRepository;
	private final TableOfContentsGenerator tableOfContentsGenerator;

	public TableOfContentsApplicationService(SlideShowRepository slideShowRepository,
			TableOfContentsGenerator tableOfContentsGenerator) {
		this.slideShowRepository = Objects.requireNonNull(slideShowRepository, "slideShowRepository");
		this.tableOfContentsGenerator = Objects.requireNonNull(tableOfContentsGenerator, "tableOfContentsGenerator");
	}

	@Override
	public List<TableOfContentsEntry> buildFor(String slideShowId) {
		// Application orchestration only: fetch aggregate and delegate TOC policy to domain service.
		SlideShow slideShow = slideShowRepository.findById(slideShowId)
				.orElseThrow(() -> new IllegalArgumentException("Unknown slide show id: " + slideShowId));
		return tableOfContentsGenerator.generate(slideShow.slides());
	}

	@Override
	public Optional<SlideShow> buildWithTableOfContents(String slideShowId) {
		Optional<SlideShow> slideShowOpt = slideShowRepository.findById(slideShowId);
		if (slideShowOpt.isEmpty()) {
			return Optional.empty();
		}
		SlideShow original = slideShowOpt.get();
		List<Slide> originalSlides = original.slides();
		List<Integer> tocPositions = new ArrayList<>();
		for (int i = 0; i < originalSlides.size(); i++) {
			if (originalSlides.get(i).isTableOfContentsPlaceholder()) {
				tocPositions.add(i);
			}
		}
		List<TableOfContentsEntry> rawEntries = tableOfContentsGenerator.generate(originalSlides);
		List<TableOfContentsEntry> adjustedEntries = new ArrayList<>();
		for (TableOfContentsEntry entry : rawEntries) {
			int originalPos = entry.slideNumber() - 1; // 1-based to 0-based
			int adjustment = 0;
			for (int tocPos : tocPositions) {
				if (tocPos < originalPos) {
					adjustment++;
				}
			}
			int newPos = originalPos + adjustment + 1; // back to 1-based
			adjustedEntries.add(new TableOfContentsEntry(newPos, entry.subject()));
		}
		List<Slide> newSlides = new ArrayList<>();
		for (int i = 0; i < originalSlides.size(); i++) {
			Slide slide = originalSlides.get(i);
			if (slide.isTableOfContentsPlaceholder()) {
				newSlides.add(createTableOfContentsSlide(adjustedEntries));
			} else {
				newSlides.add(slide);
			}
		}
		SlideShow newSlideShow = new SlideShow(original.id(), original.title(), newSlides);
		return Optional.of(newSlideShow);
	}

	private Slide createTableOfContentsSlide(List<TableOfContentsEntry> entries) {
		List<SlideItem> items = new ArrayList<>();
		for (TableOfContentsEntry entry : entries) {
			items.add(new TextItem(0, entry.slideNumber() + ". " + entry.subject().value()));
		}
		return new Slide("Table of Contents", Subject.of("Table of Contents"), false, items);
	}
}
