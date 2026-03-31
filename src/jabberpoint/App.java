package jabberpoint;

import javax.swing.SwingUtilities;

import jabberpoint.application.port.in.BuildSlideShowWithTableOfContentsUseCase;
//import jabberpoint.application.port.in.BuildTableOfContentsUseCase;
import jabberpoint.application.port.out.SlideShowRepository;
import jabberpoint.application.port.out.SlideShowPersister;// demo
import jabberpoint.application.service.TableOfContentsApplicationService;
import jabberpoint.domain.toc.ConsecutiveSubjectTableOfContentsGenerator;
import jabberpoint.domain.toc.TableOfContentsGenerator;
//import jabberpoint.infrastructure.repository.InMemorySlideShowRepository;
import jabberpoint.infrastructure.repository.XmlSlideShowRepository;
import jabberpoint.infrastructure.repository.XmlSlideShowPersister;//demo
//import jabberpoint.ui.controller.StartupController;

public final class App {
	private App() {
		// Utility class
	}

	public static void main(String[] args) {
		SlideShowRepository slideShowRepository = new XmlSlideShowRepository();
		TableOfContentsGenerator tableOfContentsGenerator = new ConsecutiveSubjectTableOfContentsGenerator();
		TableOfContentsApplicationService tableOfContentsApplicationService = new TableOfContentsApplicationService(
				slideShowRepository, tableOfContentsGenerator);
		//BuildTableOfContentsUseCase buildTableOfContentsUseCase = tableOfContentsApplicationService;
		BuildSlideShowWithTableOfContentsUseCase buildSlideShowWithTableOfContentsUseCase = tableOfContentsApplicationService;
		//StartupController startupController = new StartupController(buildTableOfContentsUseCase);

		// Demonstrate XML-serialisatie van ingelezen presentatie
		SlideShowPersister slideShowPersister = new XmlSlideShowPersister();
		slideShowRepository.findById("demo")
				.ifPresent(slideShow -> slideShowPersister.save(slideShow, "demo-export.xml"));

		// Demonstrate building a slide show met TOC and lancering van UI
		buildSlideShowWithTableOfContentsUseCase.buildWithTableOfContents("demo")
				.ifPresent(slideShow -> SwingUtilities.invokeLater(() -> {
					SlideShowFrame frame = new SlideShowFrame(slideShow);
					frame.setVisible(true);
				}));
	}
}