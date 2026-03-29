package jabberpoint.domain.toc;

import java.util.List;

import jabberpoint.domain.model.Slide;

/*
 * Interface for generating a table of contents for a slide show. The
 * implementation of this interface is a domain concern, as it involves
 * business logic to analyze the structure of the slides and determine the
 * entries for the table of contents. The application layer depends on this
 * interface to obtain the data needed to build the table of contents without
 * being coupled to the underlying domain logic. Default: ConsecutiveSubjectTableOfContentsGenerator, 
 * which generates entries based on consecutive subjects in the slide list.
*/
public interface TableOfContentsGenerator {
	List<TableOfContentsEntry> generate(List<Slide> orderedSlides);
}
