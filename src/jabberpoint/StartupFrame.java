package jabberpoint;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class StartupFrame extends JFrame {
	private static final long serialVersionUID = 1L;

	public StartupFrame() {
		super("JabberPoint - Startup");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(800, 600));
		setLocationRelativeTo(null);

		JPanel root = new JPanel(new BorderLayout());
		root.add(new JLabel("JabberPoint startup is running.", SwingConstants.CENTER), BorderLayout.CENTER);
		setContentPane(root);
	}
}