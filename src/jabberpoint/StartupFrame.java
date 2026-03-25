package jabberpoint;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.InputMap;
import javax.swing.ActionMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;



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
		
		// position of label to press key Down
		//add(new JLabel("JabberPoint startup is running.",SwingConstants.CENTER),BorderLayout.SOUTH);
		JLabel label = new JLabel("Startup Frame - Press DOWN", SwingConstants.CENTER);
        add(label, BorderLayout.SOUTH);
		
		setupKeyBindings();
	}

	private void setupKeyBindings() {
		InputMap inputMap = getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		ActionMap actionMap = getRootPane().getActionMap();
        
		// Bind the DOWN key to an action that opens the SlideOneFrame
		inputMap.put(KeyStroke.getKeyStroke("DOWN"), "goToNext");
		actionMap.put("goToNext", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				SlideOneFrame slideOneFrame = new SlideOneFrame();
				slideOneFrame.setVisible(true);
				dispose(); // Close the startup frame
			}
		});
	}
}