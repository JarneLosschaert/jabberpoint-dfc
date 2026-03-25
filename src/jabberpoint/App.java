package jabberpoint;

import javax.swing.SwingUtilities;

import jabberpoint.application.port.in.BuildTableOfContentsUseCase;
import jabberpoint.application.port.out.SlideShowRepository;
import jabberpoint.application.service.TableOfContentsApplicationService;
import jabberpoint.domain.toc.ConsecutiveSubjectTableOfContentsGenerator;
import jabberpoint.domain.toc.TableOfContentsGenerator;
import jabberpoint.infrastructure.repository.InMemorySlideShowRepository;
import jabberpoint.ui.controller.StartupController;

public final class App {
	private App() {
		// Utility class
	}

	public static void main(String[] args) {
		SlideShowRepository slideShowRepository = new InMemorySlideShowRepository();
		TableOfContentsGenerator tableOfContentsGenerator = new ConsecutiveSubjectTableOfContentsGenerator();
		BuildTableOfContentsUseCase buildTableOfContentsUseCase = new TableOfContentsApplicationService(
				slideShowRepository, tableOfContentsGenerator);
		StartupController startupController = new StartupController(buildTableOfContentsUseCase);

		SwingUtilities.invokeLater(() -> {
			StartupFrame frame = new StartupFrame(startupController);
			frame.setVisible(true);
		});
	}
}