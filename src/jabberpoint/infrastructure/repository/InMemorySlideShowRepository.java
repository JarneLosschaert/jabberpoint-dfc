package jabberpoint.infrastructure.repository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import jabberpoint.application.port.out.SlideShowRepository;
import jabberpoint.domain.model.Slide;
import jabberpoint.domain.model.SlideShow;
import jabberpoint.domain.model.Subject;
import jabberpoint.domain.model.TextItem;

public final class InMemorySlideShowRepository implements SlideShowRepository {
	private final Map<String, SlideShow> slideShowsById;

	public InMemorySlideShowRepository() {
		this.slideShowsById = new LinkedHashMap<>();
		slideShowsById.put("demo", createDemoSlideShow());
	}

	@Override
	public Optional<SlideShow> findById(String slideShowId) {
		return Optional.ofNullable(slideShowsById.get(slideShowId));
	}

	private SlideShow createDemoSlideShow() {
		// Demo data keeps infrastructure independent from XML
		List<Slide> slides = List.of(
				new Slide.Builder("Welcome")
						.subject(Subject.of("Introduction"))
						.items(List.of(new TextItem(0, "Welcome to JabberPoint")))
						.build(),
				new Slide.Builder("Goals")
						.subject(Subject.of("Introduction"))
						.items(List.of(new TextItem(1, "What we will build")))
						.build(),
				new Slide.Builder("TOC marker")
						.subject(Subject.of("Introduction"))
						.tocPlaceholder()
						.build(),
				new Slide.Builder("Architecture")
						.subject(Subject.of("Architecture"))
						.items(List.of(new TextItem(1, "Onion architecture in layers")))
						.build(),
				new Slide.Builder("Wrap-up")
						.subject(Subject.fromNullable(null))
						.items(List.of(new TextItem(1, "Questions")))
						.build());

		return SlideShow.builder("demo", "Demo Slide Show")
				.slides(slides)
				.build();
	}
}
