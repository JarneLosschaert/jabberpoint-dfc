package jabberpoint;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Dimension;
import javax.swing.SwingConstants;
import javax.swing.JLabel;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.InputMap;
import javax.swing.ActionMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;



public class SlideOneFrame extends JFrame {
    private static final long serialVersionUID = 1L;

    public SlideOneFrame() {
        // Initialize the slide one frame
        super("JabberPoint - Slide One");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(800, 600));
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.add(new JLabel("This is Slide One.", SwingConstants.CENTER), BorderLayout.CENTER);
        setContentPane(root);

        //add(new JLabel("This is Slide One.", SwingConstants.BOTTOM), BorderLayout.SOUTH);
        JLabel label = new JLabel("Slide One - Press UP to go back", SwingConstants.CENTER);
        add(label, BorderLayout.SOUTH);

        setupKeyBindings();
    }

    private void setupKeyBindings() {
        InputMap inputMap = getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getRootPane().getActionMap();

        // Bind the UP key to an action that opens the StartupFrame
        inputMap.put(KeyStroke.getKeyStroke("UP"), "goToPrevious");
        actionMap.put("goToPrevious", new AbstractAction() {
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent e) {
                StartupFrame startupFrame = new StartupFrame();
                startupFrame.setVisible(true);
                dispose(); // Close the current slide frame
            }
        });

        // Bind the DOWN key to an action that opens the SlideTwoFrame
        inputMap.put(KeyStroke.getKeyStroke("DOWN"), "goToNext");
        actionMap.put("goToNext", new AbstractAction() {
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent e) {
                // Example for a next frame:
                //SlideTwoFrame slideTwoFrame = new SlideTwoFrame();
                //slideTwoFrame.setVisible(true);
                //dispose(); // Close the current slide frame
            }
        });
    }
    
}
