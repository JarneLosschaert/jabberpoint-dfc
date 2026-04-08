package jabberpoint.infrastructure.persistence;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import jabberpoint.application.port.out.SlideShowPersister;
import jabberpoint.domain.model.FigureItem;
import jabberpoint.domain.model.ListItem;
import jabberpoint.domain.model.OrdinarySlide;
import jabberpoint.domain.model.PositionItem;
import jabberpoint.domain.model.Slide;
import jabberpoint.domain.model.SlideItem;
import jabberpoint.domain.model.SlideShow;
import jabberpoint.domain.model.SpecialSlide;
import jabberpoint.domain.model.Subject;
import jabberpoint.domain.model.TextItem;
import jabberpoint.domain.model.TitleSlide;
import jabberpoint.domain.model.TocMarkerSlide;
import jabberpoint.domain.model.TocSlide;

/**
 * Infrastructure concern: persisting a SlideShow to XML. Lives in the
 * infrastructure layer because it depends on XML libraries and file I/O, and is
 * not needed by any domain or application class.
 * Usage: called by application service when user saves a slideshow. It
 * transforms
 * the SlideShow into an XML document according to the defined contract and
 * writes
 * it to a file. The XML format is designed to be simple and stable, so that it
 * can be
 * easily parsed by the XmlSlideShowRepository when loading a slideshow, and to
 * minimize
 * the impact of future changes to the SlideShow structure on the persistence
 * format.
 */

public final class XmlSlideShowPersister implements SlideShowPersister {

    private static final Logger LOG = Logger.getLogger(XmlSlideShowPersister.class.getName());

    @Override
    public void save(SlideShow slideShow, String fileName) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();

            Element presentation = document.createElement("presentation");
            document.appendChild(presentation);

            Element showTitle = document.createElement("showtitle");
            showTitle.setTextContent(slideShow.title());
            presentation.appendChild(showTitle);

            for (Slide slide : slideShow.slides()) {
                Element slideElement = document.createElement("slide");

                Element title = document.createElement("title");
                title.setTextContent(slide.title());
                slideElement.appendChild(title);

                Subject subject = slide.subject();
                if (!subject.equals(Subject.unknown())) {
                    Element subjectElement = document.createElement("subject");
                    subjectElement.setTextContent(subject.value());
                    slideElement.appendChild(subjectElement);
                }

                /*
                 * Use an exhaustive switch so that adding a new Slide subtype produces a
                 * compile error here, forcing the author to decide how it should be persisted.
                 */
                switch (slide) {
                    case TitleSlide ts -> {
                        slideElement.setAttribute("kind", "title");
                        appendItems(slideElement, ts.items(), document);
                        if (ts.presenterName() != null && !ts.presenterName().isEmpty()) {
                            Element presenter = document.createElement("presenter");
                            presenter.setTextContent(ts.presenterName());
                            slideElement.appendChild(presenter);
                        }
                        if (ts.date() != null && !ts.date().isEmpty()) {
                            Element date = document.createElement("date");
                            date.setTextContent(ts.date());
                            slideElement.appendChild(date);
                        }
                    }
                    case OrdinarySlide os -> {
                        // "ordinary" is the default; omit the attribute for backward compatibility
                        appendItems(slideElement, os.items(), document);
                    }
                    case SpecialSlide ss -> {
                        slideElement.setAttribute("kind", "special");
                        appendItems(slideElement, ss.items(), document);
                    }
                    case TocMarkerSlide ignored -> {
                        Element toc = document.createElement("toc");
                        slideElement.appendChild(toc);
                    }
                    case TocSlide ignored -> {
                        // Generated; not persisted — the TOC is derived from the source slides
                    }
                }

                presentation.appendChild(slideElement);
            }

            writeDocument(document, fileName);
            LOG.info("Saved slide show '" + slideShow.id() + "' (" + slideShow.slides().size() + " slides) to "
                    + fileName);
        } catch (ParserConfigurationException | TransformerException | IOException e) {
            throw new RuntimeException("Failed to persist slideshow to XML: " + fileName, e);
        }
    }

    private void writeDocument(Document document, String fileName)
            throws TransformerException, IOException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

        try (FileOutputStream outputStream = new FileOutputStream(new File(fileName))) {
            transformer.transform(new DOMSource(document), new StreamResult(outputStream));
        }
    }

    private void appendItems(Element slideElement, java.util.List<SlideItem> items, Document document) {
        for (SlideItem item : items) {
            Element itemElement = document.createElement("item");
            /*
             * Exhaustive switch: adding a new SlideItem subtype produces a compile error,
             * forcing the author to decide how it should be serialised.
             */
            switch (item) {
                case TextItem t -> {
                    itemElement.setAttribute("kind", "text");
                    itemElement.setAttribute("level", Integer.toString(t.level()));
                }
                case FigureItem ignored -> itemElement.setAttribute("kind", "image");
                case ListItem ignored -> itemElement.setAttribute("kind", "list");
                case PositionItem p -> {
                    itemElement.setAttribute("kind", "position");
                    itemElement.setAttribute("x", Integer.toString(p.x()));
                    itemElement.setAttribute("y", Integer.toString(p.y()));
                    if (p.level() > 0) {
                        itemElement.setAttribute("level", Integer.toString(p.level()));
                    }
                }
            }
            itemElement.setTextContent(item.renderText());
            slideElement.appendChild(itemElement);
        }
    }
}