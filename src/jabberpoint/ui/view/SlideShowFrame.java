package jabberpoint.ui.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;

import jabberpoint.domain.model.FigureItem;
import jabberpoint.domain.model.OrdinarySlide;
import jabberpoint.domain.model.Slide;
import jabberpoint.domain.model.SlideItem;
import jabberpoint.domain.model.SlideShow;
import jabberpoint.domain.model.SpecialSlide;
import jabberpoint.domain.model.Subject;
import jabberpoint.domain.model.TextItem;
import jabberpoint.domain.model.TitleSlide;
import jabberpoint.domain.model.TocMarkerSlide;
import jabberpoint.domain.model.TocSlide;

public class SlideShowFrame extends JFrame {
    private static final Color ACTIVE_SECTION_COLOR = new Color(0, 100, 200);
    private static final Color INACTIVE_SECTION_COLOR = Color.DARK_GRAY;

    private final SlideShow slideShow;
    private int currentSlideIndex = 0;
    private final JLabel titleLabel;
    private final JLabel subjectLabel;
    private final JPanel itemsPanel;
    private final JLabel slideNumberLabel;

    public SlideShowFrame(SlideShow slideShow) {
        super("JabberPoint - " + slideShow.title());
        this.slideShow = slideShow;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Top panel for title and subject
        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        titleLabel = new JLabel("", SwingConstants.CENTER);
        titleLabel.setFont(titleLabel.getFont().deriveFont(24f));
        subjectLabel = new JLabel("", SwingConstants.CENTER);
        topPanel.add(titleLabel);
        topPanel.add(subjectLabel);
        add(topPanel, BorderLayout.NORTH);

        // Center panel for items
        itemsPanel = new JPanel();
        itemsPanel.setLayout(new BoxLayout(itemsPanel, BoxLayout.Y_AXIS));
        add(new JScrollPane(itemsPanel), BorderLayout.CENTER);

        // Bottom panel for navigation
        setupKeyBindings();
        JPanel bottomPanel = new JPanel(new FlowLayout());
        JButton prevButton = new JButton("Previous");
        prevButton.addActionListener(e -> showPreviousSlide());
        JButton nextButton = new JButton("Next");
        nextButton.addActionListener(e -> showNextSlide());
        slideNumberLabel = new JLabel();
        bottomPanel.add(prevButton);
        bottomPanel.add(slideNumberLabel);
        bottomPanel.add(nextButton);
        add(bottomPanel, BorderLayout.SOUTH);

        displaySlide(currentSlideIndex);
    }

    private void displaySlide(int index) {
        if (index < 0 || index >= slideShow.slides().size()) {
            return;
        }
        currentSlideIndex = index;
        Slide slide = slideShow.slides().get(index);
        titleLabel.setText(slide.title());
        subjectLabel.setText(slide.subject().value());
        itemsPanel.removeAll();

        switch (slide) {
            case TocSlide toc   -> renderTocItems(toc, findActiveSubject(index));
            case TitleSlide ts  -> renderRegularItems(ts);
            case OrdinarySlide os -> renderRegularItems(os);
            case SpecialSlide ss  -> renderRegularItems(ss);
            case TocMarkerSlide _ -> { /* placeholder — should have been replaced before display */ }
        }

        slideNumberLabel.setText("Slide " + (index + 1) + " of " + slideShow.slides().size());
        itemsPanel.revalidate();
        itemsPanel.repaint();
    }

    /**
     * Determines the active section when standing on a TOC slide.
     * Looks forward first (section we are about to enter), then backward
     * (section we just left) as fallback.
     */
    private Subject findActiveSubject(int tocIndex) {
        List<Slide> slides = slideShow.slides();
        for (int i = tocIndex + 1; i < slides.size(); i++) {
            Slide s = slides.get(i);
            if (!(s instanceof TocSlide) && !(s instanceof TocMarkerSlide)) {
                return s.subject();
            }
        }
        for (int i = tocIndex - 1; i >= 0; i--) {
            Slide s = slides.get(i);
            if (!(s instanceof TocSlide) && !(s instanceof TocMarkerSlide)) {
                return s.subject();
            }
        }
        return Subject.unknown();
    }

    private void renderTocItems(TocSlide slide, Subject activeSubject) {
        Font tocFont = new Font(Font.SANS_SERIF, Font.PLAIN, 18);
        Font activeTocFont = tocFont.deriveFont(Font.BOLD);
        for (SlideItem item : slide.items()) {
            switch (item) {
                case TextItem textItem -> {
                    boolean isActive = textItem.text().endsWith(activeSubject.value());
                    JLabel label = new JLabel(textItem.text(), SwingConstants.LEFT);
                    label.setFont(isActive ? activeTocFont : tocFont);
                    label.setForeground(isActive ? ACTIVE_SECTION_COLOR : INACTIVE_SECTION_COLOR);
                    label.setBorder(BorderFactory.createEmptyBorder(4, 16, 4, 16));
                    itemsPanel.add(label);
                }
                case FigureItem _ -> { /* TOC slides contain only text items */ }
            }
        }
    }

    /** Renders a regular (non-TOC) slide: text with indentation levels, images. */
    private void renderRegularItems(Slide slide) {
        for (SlideItem item : slide.items()) {
            switch (item) {
                case TextItem textItem -> {
                    String indent = "  ".repeat(textItem.level());
                    JLabel label = new JLabel(indent + textItem.text());
                    itemsPanel.add(label);
                }
                case FigureItem figureItem -> {
                    try {
                        ImageIcon icon = new ImageIcon(figureItem.source());
                        JLabel label = new JLabel(icon);
                        itemsPanel.add(label);
                    } catch (Exception e) {
                        JLabel label = new JLabel("Image: " + figureItem.source());
                        itemsPanel.add(label);
                    }
                }
            }
        }
    }

    private void showPreviousSlide() {
        if (currentSlideIndex > 0) {
            displaySlide(currentSlideIndex - 1);
        }
    }

    private void showNextSlide() {
        if (currentSlideIndex < slideShow.slides().size() - 1) {
            displaySlide(currentSlideIndex + 1);
        }
    }

    private void setupKeyBindings() {
        InputMap inputMap = getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getRootPane().getActionMap();

        inputMap.put(KeyStroke.getKeyStroke("UP"), "goToPrevious");
        actionMap.put("goToPrevious", new AbstractAction() {
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent e) {
                showPreviousSlide();
            }
        });

        inputMap.put(KeyStroke.getKeyStroke("DOWN"), "goToNext");
        actionMap.put("goToNext", new AbstractAction() {
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent e) {
                showNextSlide();
            }
        });
    }
}
