package jabberpoint.domain.model;

/**
 * The state of a slide show in the presentation lifecycle.
 * <p>
 * Defined in the Ubiquitous Language as the three stages a slide show progresses through:
 * <ul>
 *   <li>NOT_STARTED – The slide show has been created but presentation has not begun</li>
 *   <li>SHOWING_SLIDE – One of the slides is currently visible to the audience</li>
 *   <li>ENDED – All slides have been shown and the presentation is finished</li>
 * </ul>
 */
public enum SlideShowState {
	/**
	 * The slide show is created but not yet presented.
	 * Transition to: SHOWING_SLIDE (via start())
	 */
	NOT_STARTED("Not Started"),

	/**
	 * One of the slides is currently visible to the audience.
	 * Transition to: SHOWING_SLIDE (via showNextSlide/showPreviousSlide)
	 * Transition to: ENDED (via end())
	 */
	SHOWING_SLIDE("Showing Slide"),

	/**
	 * All slides have been shown and the presentation is finished.
	 * Terminal state - no further transitions.
	 */
	ENDED("Ended");

	private final String displayName;

	SlideShowState(String displayName) {
		this.displayName = displayName;
	}

	public String displayName() {
		return displayName;
	}
}
