package jabberpoint.infrastructure.repository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

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
import jabberpoint.domain.model.ImageItem;
import jabberpoint.domain.model.Slide;
import jabberpoint.domain.model.SlideItem;
import jabberpoint.domain.model.SlideShow;
import jabberpoint.domain.model.Subject;
import jabberpoint.domain.model.TextItem;

public final class XmlSlideShowPersister implements SlideShowPersister {
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

                if (slide.isTableOfContentsPlaceholder()) {
                    Element toc = document.createElement("toc");
                    toc.setTextContent("true");
                    slideElement.appendChild(toc);
                }

                for (SlideItem item : slide.items()) {
                    Element itemElement = document.createElement("item");
                    /* Changed the if/else to switch expression, so when a new type of slide item is added, it can be easily integrated */
                    switch (item) {
                        case TextItem t -> {
                            itemElement.setAttribute("kind", "text");
                            itemElement.setAttribute("level", Integer.toString(t.level()));
                        }
                        case ImageItem _ -> itemElement.setAttribute("kind", "image");
                    }
                    itemElement.setTextContent(item.renderText());
                    slideElement.appendChild(itemElement);
                }

                presentation.appendChild(slideElement);
            }

            writeDocument(document, fileName);
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
}