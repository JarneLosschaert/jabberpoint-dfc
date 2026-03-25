package jabberpoint.application.service;

import java.util.List;
import java.util.Objects;

import jabberpoint.application.port.in.BuildTableOfContentsUseCase;
import jabberpoint.application.port.out.SlideShowRepository;
import jabberpoint.domain.model.SlideShow;
import jabberpoint.domain.toc.TableOfContentsEntry;
import jabberpoint.domain.toc.TableOfContentsGenerator;

public final class TableOfContentsApplicationService implements BuildTableOfContentsUseCase {
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
}
