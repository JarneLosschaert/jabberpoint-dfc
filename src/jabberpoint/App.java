package jabberpoint;

import javax.swing.SwingUtilities;

public final class App {
	private App() {
		// Utility class
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			StartupFrame frame = new StartupFrame();
			frame.setVisible(true);		
		});
	}
}