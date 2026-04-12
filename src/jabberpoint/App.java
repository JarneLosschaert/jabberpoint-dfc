package jabberpoint;

import java.util.logging.Logger;

import javax.swing.SwingUtilities;

import jabberpoint.application.port.in.BuildSlideShowWithTocUseCase;
import jabberpoint.application.port.out.SlideShowPersister;
import jabberpoint.application.port.out.SlideShowRepository;
import jabberpoint.application.service.TocApplicationService;
import jabberpoint.domain.toc.ConsecutiveSubjectTocGenerator;
import jabberpoint.domain.toc.TocGenerator;
import jabberpoint.infrastructure.persistence.XmlSlideShowPersister;
import jabberpoint.infrastructure.repository.XmlSlideShowRepository;
import jabberpoint.ui.view.SlideShowFrame;

/**
 * Ports & Adapters (composition root). Wires all ports and adapters together
 * and launches the UI.
 */
public final class App {

	private static final Logger LOG = Logger.getLogger(App.class.getName());

	private App() {
		// Utility class
	}

	public static void main(String[] args) {
		LOG.info("JabberPoint starting up");
		SlideShowRepository slideShowRepository = new XmlSlideShowRepository();
		TocGenerator tocGenerator = new ConsecutiveSubjectTocGenerator();
		TocApplicationService tocApplicationService = new TocApplicationService(slideShowRepository, tocGenerator);
		BuildSlideShowWithTocUseCase buildSlideShowWithTocUseCase = tocApplicationService;

		// Persist the raw (pre-TOC) slideshow to XML for demonstration
		SlideShowPersister slideShowPersister = new XmlSlideShowPersister();
		slideShowRepository.findById("demo")
				.ifPresent(slideShow -> slideShowPersister.save(slideShow, "demo-export.xml"));

		// Build the slideshow with inserted TOC slides and open the UI
		buildSlideShowWithTocUseCase.build("demo")
				.ifPresent(slideShow -> SwingUtilities.invokeLater(() -> {
					SlideShowFrame frame = new SlideShowFrame(slideShow);
					frame.setVisible(true);
				}));
	}
}