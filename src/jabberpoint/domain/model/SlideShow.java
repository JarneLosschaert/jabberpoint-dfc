package jabberpoint.domain.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Immutable State Machine pattern: an immutable aggregate representing an ordered sequence of
 * slides with a lifecycle state. Every navigation method validates the current state and returns
 * a new instance; no field is mutated after construction.
 */
public final class SlideShow {
	private final String id;
	private final String title;
	private final List<Slide> slides;
	private final SlideShowState state;
	private final int currentSlideIndex;

	public SlideShow(String id, String title, List<Slide> slides) {
		this(id, title, slides, SlideShowState.NOT_STARTED, -1);
	}

	private SlideShow(String id, String title, List<Slide> slides, SlideShowState state, int currentSlideIndex) {
		String normalizedId = Objects.requireNonNull(id, "id must not be null").trim();
		String normalizedTitle = Objects.requireNonNull(title, "title must not be null").trim();
		if (normalizedId.isEmpty()) {
			throw new IllegalArgumentException("Slide show id cannot be empty");
		}
		if (normalizedTitle.isEmpty()) {
			throw new IllegalArgumentException("Slide show title cannot be empty");
		}
		this.id = normalizedId;
		this.title = normalizedTitle;
		this.slides = Collections.unmodifiableList(new ArrayList<>(Objects.requireNonNull(slides, "slides must not be null")));
		this.state = Objects.requireNonNull(state, "state must not be null");
		this.currentSlideIndex = currentSlideIndex;
	}

	public static Builder builder(String id, String title) {
		return new Builder(id, title);
	}

	/** Builder pattern: constructs a {@link SlideShow} with a required id and title; slide list is optional. */
	public static final class Builder {
		private final String id;
		private final String title;
		private List<Slide> slides = List.of();

		public Builder(String id, String title) {
			this.id = Objects.requireNonNull(id, "id must not be null").trim();
			this.title = Objects.requireNonNull(title, "title must not be null").trim();
		}

		public Builder slides(List<Slide> slides) {
			this.slides = Objects.requireNonNull(slides, "slides must not be null");
			return this;
		}

		public SlideShow build() {
			return new SlideShow(id, title, slides);
		}
	}

	public String id() {
		return id;
	}

	public String title() {
		return title;
	}

	public List<Slide> slides() {
		return slides;
	}

	public SlideShowState state() {
		return state;
	}

	public int currentSlideIndex() {
		return currentSlideIndex;
	}

	/**
	 * Returns true if a slide is currently being shown (state is SHOWING_SLIDE).
	 */
	public boolean isShowingSlide() {
		return state == SlideShowState.SHOWING_SLIDE;
	}

	/**
	 * Returns the currently displayed slide, or empty if no slide is being shown.
	 */
	public java.util.Optional<Slide> currentSlide() {
		if (currentSlideIndex < 0 || currentSlideIndex >= slides.size()) {
			return java.util.Optional.empty();
		}
		return java.util.Optional.of(slides.get(currentSlideIndex));
	}

	/**
	 * Presenter Action: Start Slide Show. Transitions from NOT_STARTED to SHOWING_SLIDE
	 * and displays the first slide (index 0).
	 * 
	 * @throws IllegalStateException if the slide show is not in NOT_STARTED state
	 * @throws IllegalStateException if the slide show has no slides
	 */
	public SlideShow start() {
		if (state != SlideShowState.NOT_STARTED) {
			throw new IllegalStateException("Cannot start a slide show that is not in NOT_STARTED state");
		}
		if (slides.isEmpty()) {
			throw new IllegalStateException("Cannot start a slide show with no slides");
		}
		return new SlideShow(id, title, slides, SlideShowState.SHOWING_SLIDE, 0);
	}

	/**
	 * Presenter Action: Show Next Slide. Displays the next slide in the sequence.
	 * If already on the last slide, transitions to ENDED state.
	 * 
	 * @throws IllegalStateException if the slide show is not in SHOWING_SLIDE state
	 */
	public SlideShow showNextSlide() {
		if (state != SlideShowState.SHOWING_SLIDE) {
			throw new IllegalStateException("Cannot show next slide when state is " + state);
		}
		if (currentSlideIndex >= slides.size() - 1) {
			// Last slide reached, transition to ENDED
			return new SlideShow(id, title, slides, SlideShowState.ENDED, currentSlideIndex);
		}
		return new SlideShow(id, title, slides, state, currentSlideIndex + 1);
	}

	/**
	 * Presenter Action: Show Previous Slide. Returns to the previous slide in the sequence.
	 * Cannot go before the first slide.
	 * 
	 * @throws IllegalStateException if the slide show is not in SHOWING_SLIDE state
	 * @throws IllegalStateException if already on the first slide
	 */
	public SlideShow showPreviousSlide() {
		if (state != SlideShowState.SHOWING_SLIDE) {
			throw new IllegalStateException("Cannot show previous slide when state is " + state);
		}
		if (currentSlideIndex <= 0) {
			throw new IllegalStateException("Cannot go to previous slide when on the first slide");
		}
		return new SlideShow(id, title, slides, state, currentSlideIndex - 1);
	}

	/**
	 * Presenter Action: End Slide Show. Transitions from SHOWING_SLIDE to ENDED state.
	 * 
	 * @throws IllegalStateException if the slide show is not in SHOWING_SLIDE state
	 */
	public SlideShow end() {
		if (state != SlideShowState.SHOWING_SLIDE) {
			throw new IllegalStateException("Cannot end a slide show that is not in SHOWING_SLIDE state");
		}
		return new SlideShow(id, title, slides, SlideShowState.ENDED, currentSlideIndex);
	}

	/**
	 * Presenter Action: Go To Slide (optional). Jump directly to a specific slide.
	 * 
	 * @param slideNumber The slide number (1-based, as shown to the user)
	 * @return A new SlideShow at the specified slide
	 * @throws IllegalStateException if the slide show is not in SHOWING_SLIDE state
	 * @throws IllegalArgumentException if the slide number is invalid
	 */
	public SlideShow goToSlide(int slideNumber) {
		if (state != SlideShowState.SHOWING_SLIDE) {
			throw new IllegalStateException("Cannot go to slide when state is " + state);
		}
		int zeroBasedIndex = slideNumber - 1; // Convert 1-based to 0-based
		if (zeroBasedIndex < 0 || zeroBasedIndex >= slides.size()) {
			throw new IllegalArgumentException("Slide number " + slideNumber + " is out of range (1-" + slides.size() + ")");
		}
		return new SlideShow(id, title, slides, state, zeroBasedIndex);
	}

	/** Returns a copy of this slide show with a different slide list, reset to NOT_STARTED. */
	public SlideShow withSlides(List<Slide> newSlides) {
		return new SlideShow(id, title, newSlides, SlideShowState.NOT_STARTED, -1);
	}
}
