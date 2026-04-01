package jabberpoint;

import javax.swing.SwingUtilities;

import jabberpoint.application.port.in.BuildSlideShowWithTocUseCase;
//import jabberpoint.application.port.in.BuildTocUseCase;
import jabberpoint.application.port.out.SlideShowRepository;
import jabberpoint.application.port.out.SlideShowPersister;// demo
import jabberpoint.application.service.TocApplicationService;
import jabberpoint.domain.toc.ConsecutiveSubjectTocGenerator;
import jabberpoint.domain.toc.TocGenerator;
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
		TocGenerator tocGenerator = new ConsecutiveSubjectTocGenerator();
		TocApplicationService tocApplicationService = new TocApplicationService(slideShowRepository, tocGenerator);
		//BuildTocUseCase buildTocUseCase = tocApplicationService;
		BuildSlideShowWithTocUseCase buildSlideShowWithTocUseCase = tocApplicationService;
		//StartupController startupController = new StartupController(buildTocUseCase);

		// Demonstrate XML-serialisatie van ingelezen presentatie
		SlideShowPersister slideShowPersister = new XmlSlideShowPersister();
		slideShowRepository.findById("demo")
				.ifPresent(slideShow -> slideShowPersister.save(slideShow, "demo-export.xml"));

		// Demonstrate building a slide show met TOC and lancering van UI
		buildSlideShowWithTocUseCase.buildWithTableOfContents("demo")
				.ifPresent(slideShow -> SwingUtilities.invokeLater(() -> {
					SlideShowFrame frame = new SlideShowFrame(slideShow);
					frame.setVisible(true);
				}));
	}
}