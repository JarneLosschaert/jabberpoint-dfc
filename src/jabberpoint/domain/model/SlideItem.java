package jabberpoint.domain.model;

/* Changed SlideItem to TextItem, ImageItem so when a new type of slide item is added, it can be easily integrated */

public sealed interface SlideItem permits TextItem, ImageItem {
	String renderText();
}
