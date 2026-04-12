package jabberpoint.domain.model;

/**
 * The state of a slide show in the presentation lifecycle.
 * NOT_STARTED – The slide show has been created but presentation has not begun
 * SHOWING_SLIDE – One of the slides is currently visible to the audience
 * ENDED – All slides have been shown and the presentation is finished
 */
public enum SlideShowState {
	NOT_STARTED("Not Started"),
	SHOWING_SLIDE("Showing Slide"),
	ENDED("Ended");

	private final String displayName;

	SlideShowState(String displayName) {
		this.displayName = displayName;
	}

	public String displayName() {
		return displayName;
	}
}
