package jabberpoint.domain.model;

/**
 * Sealed Interface / Exhaustive Switch pattern: a content element shown on a
 * slide. Every switch over item types is exhaustively checked by the compiler.
 */
public sealed interface SlideItem permits TextItem, FigureItem, ListItem, PositionItem {
	String renderText();
}
