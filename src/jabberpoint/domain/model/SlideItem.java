package jabberpoint.domain.model;

/**
 * Changed SlideItem to Sealed interface so when a new type of slide item is
 * added, it can be easily integrated and used in a switch.
 */

public sealed interface SlideItem permits TextItem, FigureItem {
	String renderText();
}
