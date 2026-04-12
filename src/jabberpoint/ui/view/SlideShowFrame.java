package jabberpoint.ui.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;

import jabberpoint.domain.model.FigureItem;
import jabberpoint.domain.model.ListItem;
import jabberpoint.domain.model.OrdinarySlide;
import jabberpoint.domain.model.PositionItem;
import jabberpoint.domain.model.Slide;
import jabberpoint.domain.model.SlideItem;
import jabberpoint.domain.model.SlideShow;
import jabberpoint.domain.model.SpecialSlide;
import jabberpoint.domain.model.Subject;
import jabberpoint.domain.model.TextItem;
import jabberpoint.domain.model.TitleSlide;
import jabberpoint.domain.model.TocMarkerSlide;
import jabberpoint.domain.model.TocSlide;
import jabberpoint.domain.toc.TocEntry;

/**
 * Immutable State Machine (consumer): Swing UI for presenting a slide show.
 * Replaces its {@code SlideShow} reference on each navigation call and uses an
 * exhaustive switch over the sealed {@code Slide} type for rendering.
 */
public class SlideShowFrame extends JFrame {
    private static final Color ACTIVE_SECTION_COLOR = new Color(0, 100, 200);
    private static final Color INACTIVE_SECTION_COLOR = Color.DARK_GRAY;

    private SlideShow slideShow;
    private final JLabel titleLabel;
    private final JLabel subjectLabel;
    private final JPanel itemsPanel;
    private final JLabel slideNumberLabel;
    private final JTextField goToSlideField;

    public SlideShowFrame(SlideShow slideShow) {
        super("JabberPoint - " + slideShow.title());
        this.slideShow = slideShow.start();
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
        itemsPanel = new JPanel(null);
        itemsPanel.setPreferredSize(new Dimension(760, 0));
        add(new JScrollPane(itemsPanel), BorderLayout.CENTER);

        // Bottom panel for navigation
        JPanel bottomPanel = new JPanel(new FlowLayout());
        JButton prevButton = new JButton("Previous");
        prevButton.addActionListener(e -> showPreviousSlide());
        JButton nextButton = new JButton("Next");
        nextButton.addActionListener(e -> showNextSlide());
        slideNumberLabel = new JLabel();
        goToSlideField = new JTextField(5);
        goToSlideField.setToolTipText("Enter slide number (1-" + slideShow.slides().size() + ")");
        JButton goToButton = new JButton("Enter");
        goToButton.addActionListener(e -> goToSlide());
        bottomPanel.add(prevButton);
        bottomPanel.add(slideNumberLabel);
        bottomPanel.add(nextButton);
        bottomPanel.add(new JLabel(" | Go to slide: "));
        bottomPanel.add(goToSlideField);
        bottomPanel.add(goToButton);
        add(bottomPanel, BorderLayout.SOUTH);

        // Setup key bindings after all UI components are initialized
        setupKeyBindings();

        displayCurrentSlide();
    }

    private void displayCurrentSlide() {
        slideShow.currentSlide().ifPresent(slide -> {
            int index = slideShow.currentSlideIndex();
            titleLabel.setText(slide.title());
            subjectLabel.setText(slide.subject().value());
            itemsPanel.removeAll();

            switch (slide) {
                case TocSlide toc -> renderTocItems(toc, findActiveSubject(index));
                case TitleSlide ts -> renderTitleSlide(ts);
                case OrdinarySlide os -> renderRegularItems(os);
                case SpecialSlide ss -> renderRegularItems(ss);
                case TocMarkerSlide ignored -> {
                    /* placeholder — should have been replaced before display */ }
            }

            slideNumberLabel.setText("Slide " + (index + 1) + " of " + slideShow.slides().size());
            itemsPanel.revalidate();
            itemsPanel.repaint();
        });
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
        int currentY = 0;
        Font tocFont = new Font(Font.SANS_SERIF, Font.PLAIN, 18);
        Font activeTocFont = tocFont.deriveFont(Font.BOLD);

        for (TocEntry entry : slide.entries()) {
            boolean isActive = entry.subject().equals(activeSubject);
            String text = entry.slideNumber() + ". " + entry.subject().value();
            JLabel label = new JLabel(text, SwingConstants.LEFT);
            label.setFont(isActive ? activeTocFont : tocFont);
            label.setForeground(isActive ? ACTIVE_SECTION_COLOR : INACTIVE_SECTION_COLOR);
            label.setBorder(BorderFactory.createEmptyBorder(4, 16, 4, 16));
            Dimension size = label.getPreferredSize();
            addComponent(label, 20, currentY, 720, size.height);
            currentY += size.height + 4;
        }

        itemsPanel.setPreferredSize(new Dimension(760, Math.max(currentY, 200)));
    }

    /**
     * Renders a list of slide items starting at the given Y offset; returns the new
     * Y offset.
     */
    private int renderItemList(List<SlideItem> items, int startY) {
        int currentY = startY;
        for (SlideItem item : items) {
            switch (item) {
                case TextItem textItem -> {
                    String indent = "  ".repeat(textItem.level());
                    JLabel label = new JLabel(indent + textItem.text());
                    Dimension size = label.getPreferredSize();
                    addComponent(label, 20, currentY, 720, size.height);
                    currentY += size.height + 4;
                }
                case ListItem listItem -> {
                    for (TextItem entry : listItem.entries()) {
                        String indent = "  ".repeat(entry.level());
                        JLabel label = new JLabel("\u2022 " + indent + entry.text());
                        Dimension size = label.getPreferredSize();
                        addComponent(label, 20, currentY, 720, size.height);
                        currentY += size.height + 4;
                    }
                }
                case FigureItem figureItem -> {
                    JLabel label;
                    try {
                        ImageIcon icon = new ImageIcon(figureItem.source());
                        label = new JLabel(icon);
                    } catch (Exception e) {
                        label = new JLabel("Image: " + figureItem.source());
                    }
                    Dimension size = label.getPreferredSize();
                    addComponent(label, 20, currentY, 720, size.height);
                    currentY += size.height + 4;
                }
                case PositionItem positionItem -> {
                    String indent = "  ".repeat(positionItem.level());
                    JLabel label = new JLabel(indent + positionItem.text());
                    Dimension size = label.getPreferredSize();
                    addComponent(label, positionItem.x(), positionItem.y(), 720, size.height);
                    currentY = Math.max(currentY, positionItem.y() + size.height + 4);
                }
            }
        }
        return currentY;
    }

    /** Renders a regular (non-TOC) slide: text with indentation levels, images. */
    private void renderRegularItems(Slide slide) {
        int currentY = renderItemList(slide.items(), 0);
        itemsPanel.setPreferredSize(new Dimension(760, Math.max(currentY, 200)));
    }

    /**
     * Renders a title slide: presenter name and date as meta info, then regular
     * items.
     */
    private void renderTitleSlide(TitleSlide slide) {
        int currentY = 0;
        if (slide.presenterName() != null && !slide.presenterName().isEmpty()) {
            JLabel label = new JLabel("Presenter: " + slide.presenterName());
            Dimension size = label.getPreferredSize();
            addComponent(label, 20, currentY, 720, size.height);
            currentY += size.height + 4;
        }
        if (slide.date() != null && !slide.date().isEmpty()) {
            JLabel label = new JLabel("Date: " + slide.date());
            Dimension size = label.getPreferredSize();
            addComponent(label, 20, currentY, 720, size.height);
            currentY += size.height + 4;
        }
        currentY = renderItemList(slide.items(), currentY);
        itemsPanel.setPreferredSize(new Dimension(760, Math.max(currentY, 200)));
    }

    private void addComponent(JComponent component, int x, int y, int width, int height) {
        component.setBounds(x, y, width, height);
        itemsPanel.add(component);
    }

    private void showPreviousSlide() {
        if (slideShow.isShowingSlide() && slideShow.currentSlideIndex() > 0) {
            slideShow = slideShow.showPreviousSlide();
            displayCurrentSlide();
        }
    }

    private void showNextSlide() {
        if (slideShow.isShowingSlide() && slideShow.currentSlideIndex() < slideShow.slides().size() - 1) {
            slideShow = slideShow.showNextSlide();
            displayCurrentSlide();
        }
    }

    private void goToSlide() {
        try {
            String input = goToSlideField.getText().trim();
            if (input.isEmpty()) {
                return;
            }
            int slideNumber = Integer.parseInt(input);
            if (slideNumber < 1 || slideNumber > slideShow.slides().size()) {
                goToSlideField.setText("");
                return;
            }
            slideShow = slideShow.goToSlide(slideNumber);
            displayCurrentSlide();
            goToSlideField.setText("");
        } catch (NumberFormatException | IllegalStateException e) {
            goToSlideField.setText("");
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

        // Enter key in goToSlideField triggers navigation
        goToSlideField.addActionListener(e -> goToSlide());
    }
}
