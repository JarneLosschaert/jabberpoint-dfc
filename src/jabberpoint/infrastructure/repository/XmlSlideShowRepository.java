package jabberpoint.infrastructure.repository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

public final class XmlSlideShowRepository implements SlideShowRepository {

    private static final String XML_EXTENSION = ".xml";

    @Override
    public Optional<SlideShow> findById(String slideShowId) {
        // look for the file 
        String fileName = slideShowId + XML_EXTENSION;
        File xmlFile = new File(fileName);

        if (!xmlFile.exists()) {
            return Optional.empty();// If file does not exist. 
        }

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            // Prevent XXE: disable external entity loading while keeping DOCTYPE processing
            factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
            factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            return Optional.of(parseSlideShow(doc, slideShowId));
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new RuntimeException("Failed to parse XML file: " + fileName, e);
        }
    }

    private SlideShow parseSlideShow(Document doc, String slideShowId) {
        Element presentationElement = doc.getDocumentElement();

        String title = parseShowTitle(presentationElement);
        List<Slide> slides = parseSlides(presentationElement);

        return new SlideShow(slideShowId, title, slides);
    }

    private String parseShowTitle(Element presentationElement) {
        NodeList titleNodes = presentationElement.getElementsByTagName("showtitle");
        if (titleNodes.getLength() > 0) {
            return titleNodes.item(0).getTextContent().trim();
        }
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
        boolean isTocPlaceholder = parseTocPlaceholder(slideElement);
        List<SlideItem> items = parseItems(slideElement);

        return new Slide(title, subject, isTocPlaceholder, items);
    }

    private String parseTitle(Element slideElement) {
        NodeList titleNodes = slideElement.getElementsByTagName("title");
        if (titleNodes.getLength() > 0) {
            return titleNodes.item(0).getTextContent().trim();
        }
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
            // if kind is not known.
            return new TextItem(0, content);
        }
    }

    private int parseLevel(Element itemElement) {
        String levelStr = itemElement.getAttribute("level");
        if (levelStr != null && !levelStr.isEmpty()) {
            try {
                return Integer.parseInt(levelStr);
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }
}