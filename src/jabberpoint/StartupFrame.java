package jabberpoint;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import jabberpoint.ui.controller.StartupController;

public class StartupFrame extends JFrame {
	private static final long serialVersionUID = 1L;

	public StartupFrame(StartupController startupController) {
		super("JabberPoint - Startup");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(800, 600));
		setLocationRelativeTo(null);

		JPanel root = new JPanel(new BorderLayout());

		JTextArea output = new JTextArea();
		output.setEditable(false);
		output.setLineWrap(true);
		output.setWrapStyleWord(true);
		output.setText(startupController.buildStatusText("demo"));

		root.add(new JScrollPane(output), BorderLayout.CENTER);
		setContentPane(root);
	}
}