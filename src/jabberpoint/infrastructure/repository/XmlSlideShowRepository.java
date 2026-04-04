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
        // TOC markers are detected first — they need no title, subject, or items.
        if (parseTocMarker(slideElement)) {
            return new TocMarkerSlide();
        }

        String title = parseTitle(slideElement);
        Subject subject = parseSubject(slideElement);
        List<SlideItem> items = parseItems(slideElement);
        String kind = slideElement.getAttribute("kind");

        return switch (kind) {
            case "title"   -> TitleSlide.builder(title)
                    .subject(subject)
                    .items(items)
                    .presenterName(parsePresenterName(slideElement))
                    .date(parseDate(slideElement))
                    .build();
            case "special" -> new SpecialSlide(title, subject, items);
            default        -> new OrdinarySlide(title, subject, items); // "ordinary" or absent
        };
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

    private boolean parseTocMarker(Element slideElement) {
        return slideElement.getElementsByTagName("toc").getLength() > 0;
    }

    private String parsePresenterName(Element slideElement) {
        NodeList presenterNodes = slideElement.getElementsByTagName("presenter");
        if (presenterNodes.getLength() > 0) {
            return presenterNodes.item(0).getTextContent().trim();
        }
        return null;
    }

    private String parseDate(Element slideElement) {
        NodeList dateNodes = slideElement.getElementsByTagName("date");
        if (dateNodes.getLength() > 0) {
            return dateNodes.item(0).getTextContent().trim();
        }
        return null;
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
            return new FigureItem(content);
        } else if ("list".equals(kind)) {
            return parseListItem(content);
        } else if ("position".equals(kind)) {
            return parsePositionItem(itemElement, content);
        } else {
            LOG.warning("Unknown item kind '" + kind + "'; treating as plain text at level 0");
            return new TextItem(0, content);
        }
    }

    private ListItem parseListItem(String content) {
        List<TextItem> entries = new ArrayList<>();
        content.lines()
                .map(String::trim)
                .filter(line -> !line.isEmpty())
                .forEach(line -> entries.add(new TextItem(0, line)));
        if (entries.isEmpty()) {
            throw new IllegalArgumentException("List item must contain at least one entry");
        }
        return new ListItem(entries);
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

    private PositionItem parsePositionItem(Element itemElement, String content) {
        int x = parseCoordinate(itemElement, "x");
        int y = parseCoordinate(itemElement, "y");
        int level = parseLevel(itemElement);
        return new PositionItem(x, y, level, content);
    }

    private int parseCoordinate(Element itemElement, String attributeName) {
        String value = itemElement.getAttribute(attributeName);
        if (value == null || value.isEmpty()) {
            LOG.warning("Position item missing '" + attributeName + "' attribute; defaulting to 0");
            return 0;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            LOG.warning("Invalid position value '" + value + "' for attribute '" + attributeName + "'; defaulting to 0");
            return 0;
        }
    }
}