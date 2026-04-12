package jabberpoint.domain.model;

import java.util.List;

/**
 * Sealed Interface / Exhaustive Switch pattern: a visual sheet in a slide show.
 * Sealed so that every switch over slide types is exhaustively checked by the compiler.
 * <p>
 * Known subtypes:
 * <ul>
 *   <li>{@link TitleSlide} – first slide showing the slide show title and meta information</li>
 *   <li>{@link OrdinarySlide} – a standard content slide with a title and items</li>
 *   <li>{@link TocSlide} – generated slide listing subject blocks</li>
 *   <li>{@link TocMarkerSlide} – placeholder marking where a TOC slide is to be inserted</li>
 *   <li>{@link SpecialSlide} – a slide that differs from ordinary slides (e.g. diagrams)</li>
 * </ul>
 */
public sealed interface Slide permits TitleSlide, OrdinarySlide, TocSlide, TocMarkerSlide, SpecialSlide {
	String title();
	Subject subject();
	List<SlideItem> items();
}
