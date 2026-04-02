package jabberpoint.infrastructure.repository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import jabberpoint.application.port.out.SlideShowRepository;
import jabberpoint.domain.model.ImageItem;
import jabberpoint.domain.model.Slide;
import jabberpoint.domain.model.SlideItem;
import jabberpoint.domain.model.SlideShow;
import jabberpoint.domain.model.Subject;
import jabberpoint.domain.model.TextItem;

/**
 * Infrastructure concern: loading a SlideShow from XML. Lives in the
 * infrastructure layer because it depends on XML libraries and file I/O, and is
 * not needed by any domain or application class.
 * Usage: called by application service when user opens a slideshow. It looks
 * for an XML file with the given ID, parses it according to the defined
 * contract, and transforms it into a SlideShow object. The XML format is
 * designed to be simple and stable, so that it can be easily parsed by this
 * repository, and to minimize the impact of future changes to the SlideShow
 * structure on the persistence format.
 */

public final class XmlSlideShowRepository implements SlideShowRepository {

    private static final Logger LOG = Logger.getLogger(XmlSlideShowRepository.class.getName());
    private static final String XML_EXTENSION = ".xml";

    @Override
    public Optional<SlideShow> findById(String slideShowId) {
        String fileName = slideShowId + XML_EXTENSION;
        File xmlFile = new File(fileName);

        if (!xmlFile.exists()) {
            LOG.info("No XML file found for slide show id '" + slideShowId + "' (looked for: " + fileName + ")");
            return Optional.empty();
        }

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            SlideShow slideShow = parseSlideShow(doc, slideShowId);
            LOG.info("Loaded slide show '" + slideShowId + "' with " + slideShow.slides().size() + " slides from " + fileName);
            return Optional.of(slideShow);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new RuntimeException("Failed to parse XML file: " + fileName, e);
        }
    }

    private SlideShow parseSlideShow(Document doc, String slideShowId) {
        Element presentationElement = doc.getDocumentElement();

        String title = parseShowTitle(presentationElement);
        List<Slide> slides = parseSlides(presentationElement);

        return SlideShow.builder(slideShowId, title)
                .slides(slides)
                .build();
    }

    private String parseShowTitle(Element presentationElement) {
        NodeList titleNodes = presentationElement.getElementsByTagName("showtitle");
        if (titleNodes.getLength() > 0) {
            return titleNodes.item(0).getTextContent().trim();
        }
        LOG.warning("No <showtitle> element found; defaulting to 'Untitled Presentation'");
        return "Untitled Presentation";
    }

    private List<Slide> parseSlides(Element presentationElement) {
        List<Slide> slides = new ArrayList<>();
        NodeList slideNodes = presentationElement.getElementsByTagName("slide");

        for (int i = 0; i < slideNodes.getLength(); i++) {
            Element slideElement = (Element) slideNodes.item(i);
            slides.add(parseSlide(slideElement));
        }

        return slides;
    }

    private Slide parseSlide(Element slideElement) {
        String title = parseTitle(slideElement);
        Subject subject = parseSubject(slideElement);
        List<SlideItem> items = parseItems(slideElement);

        Slide.Builder builder = new Slide.Builder(title).subject(subject).items(items);
        if (parseTocPlaceholder(slideElement)) {
            builder.tocPlaceholder();
        }
        return builder.build();
    }

    private String parseTitle(Element slideElement) {
        NodeList titleNodes = slideElement.getElementsByTagName("title");
        if (titleNodes.getLength() > 0) {
            return titleNodes.item(0).getTextContent().trim();
        }
        LOG.warning("Slide has no <title> element; defaulting to 'Untitled Slide'");
        return "Untitled Slide";
    }

    private Subject parseSubject(Element slideElement) {
        NodeList subjectNodes = slideElement.getElementsByTagName("subject");
        if (subjectNodes.getLength() > 0) {
            String subjectText = subjectNodes.item(0).getTextContent().trim();
            return Subject.fromNullable(subjectText);
        }
        return Subject.unknown();
    }

    private boolean parseTocPlaceholder(Element slideElement) {
        NodeList tocNodes = slideElement.getElementsByTagName("toc");
        return tocNodes.getLength() > 0
                && "true".equalsIgnoreCase(tocNodes.item(0).getTextContent().trim());
    }

    private List<SlideItem> parseItems(Element slideElement) {
        List<SlideItem> items = new ArrayList<>();
        NodeList itemNodes = slideElement.getElementsByTagName("item");

        for (int i = 0; i < itemNodes.getLength(); i++) {
            Element itemElement = (Element) itemNodes.item(i);
            items.add(parseItem(itemElement));
        }

        return items;
    }

    private SlideItem parseItem(Element itemElement) {
        String kind = itemElement.getAttribute("kind");
        String content = itemElement.getTextContent().trim();

        if ("text".equals(kind)) {
            int level = parseLevel(itemElement);
            return new TextItem(level, content);
        } else if ("image".equals(kind)) {
            return new ImageItem(content);
        } else {
            LOG.warning("Unknown item kind '" + kind + "'; treating as plain text at level 0");
            return new TextItem(0, content);
        }
    }

    private int parseLevel(Element itemElement) {
        String levelStr = itemElement.getAttribute("level");
        if (levelStr != null && !levelStr.isEmpty()) {
            try {
                return Integer.parseInt(levelStr);
            } catch (NumberFormatException e) {
                LOG.warning("Invalid level value '" + levelStr + "'; defaulting to 0");
                return 0;
            }
        }
        return 0;
    }
}