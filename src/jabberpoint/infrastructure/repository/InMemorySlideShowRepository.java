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
		// Demo data keeps infrastructure independent from XML while phase 4 is not implemented yet.
		List<Slide> slides = List.of(
				new Slide("Welcome", Subject.of("Introduction"), false, List.of(new TextItem(0, "Welcome to JabberPoint"))),
				new Slide("Goals", Subject.of("Introduction"), false, List.of(new TextItem(1, "What we will build"))),
				new Slide("TOC marker", Subject.of("Introduction"), true, List.of()),
				new Slide("Architecture", Subject.of("Architecture"), false,
						List.of(new TextItem(1, "Onion architecture in layers"))),
				new Slide("Wrap-up", Subject.fromNullable(null), false, List.of(new TextItem(1, "Questions"))));

		return new SlideShow("demo", "Demo Slide Show", slides);
	}
}
