import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;


 // GUI & Main Entry Point

public class MainApp extends JFrame {

    // Modern Color Palette
    private static final Color BG_MAIN = new Color(248, 250, 252);
    private static final Color CARD_BG = Color.WHITE;
    private static final Color BORDER_LIGHT = new Color(226, 232, 240);
    private static final Color PRIMARY_BLUE = new Color(37, 99, 235);
    private static final Color PRIMARY_HOVER = new Color(29, 78, 216);
    private static final Color DARK_SLATE = new Color(30, 41, 59);
    private static final Color TEXT_MUTED = new Color(100, 116, 139);
    private static final Color HIT_BG = new Color(220, 252, 231);
    private static final Color HIT_TEXT = new Color(22, 101, 52);
    private static final Color FAULT_BG = new Color(254, 226, 226);
    private static final Color FAULT_TEXT = new Color(153, 27, 27);

    private JComboBox<String> algoBox;
    private JComboBox<String> presetBox;
    private JTextField pagesInput;
    private JTextField framesInput;
    private JTabbedPane tabs;
    private JPanel statusBanner;
    private JLabel statusText;

    public MainApp() {
        setTitle("PT 101 | Page Replacement Simulator (FIFO, LRU, Optimal)");
        setSize(1080, 720);
        setMinimumSize(new Dimension(920, 620));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BG_MAIN);

        setupUI();
    }

    private void setupUI() {
        // 1. Top Navigation Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(DARK_SLATE);
        header.setBorder(new EmptyBorder(16, 24, 16, 24));

        JLabel title = new JLabel("Page Replacement Simulator");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JLabel subtitle = new JLabel("Topic 3: FIFO  •  LRU  •  Optimal   |   PT 101 Platform Technologies");
        subtitle.setForeground(new Color(148, 163, 184));
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JPanel titleGroup = new JPanel(new GridLayout(2, 1, 0, 2));
        titleGroup.setOpaque(false);
        titleGroup.add(title);
        titleGroup.add(subtitle);
        header.add(titleGroup, BorderLayout.WEST);

        // Preset Selector on Top Right
        JPanel presetPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        presetPanel.setOpaque(false);
        JLabel presetLabel = new JLabel("Quick Sample:");
        presetLabel.setForeground(new Color(203, 213, 225));
        presetLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));

        presetBox = new JComboBox<>(new String[]{
                "Sample 1: Standard (13 refs, 3 frames)",
                "Sample 2: Locality Test (12 refs, 3 frames)",
                "Sample 3: Extended Test (20 refs, 4 frames)"
        });
        presetBox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        presetBox.setPreferredSize(new Dimension(245, 30));
        presetBox.addActionListener(e -> applyPreset());

        presetPanel.add(presetLabel);
        presetPanel.add(presetBox);
        header.add(presetPanel, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        // 2. Center Workspace
        JPanel workspace = new JPanel(new BorderLayout(0, 16));
        workspace.setBackground(BG_MAIN);
        workspace.setBorder(new EmptyBorder(18, 24, 14, 24));

        // Input Card
        JPanel inputCard = new JPanel(new GridBagLayout());
        inputCard.setBackground(CARD_BG);
        inputCard.setBorder(new CompoundBorder(
                new LineBorder(BORDER_LIGHT, 1, true),
                new EmptyBorder(14, 18, 14, 18)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 8, 3, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Labels Row
        gbc.gridy = 0;
        gbc.gridx = 0;
        inputCard.add(makeFieldLabel("Algorithm Mode"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        inputCard.add(makeFieldLabel("Page-Reference String (spaces or commas)"), gbc);

        gbc.gridx = 2;
        gbc.weightx = 0.0;
        inputCard.add(makeFieldLabel("Page Frames (1–15)"), gbc);

        // Controls Row
        gbc.gridy = 1;
        gbc.gridx = 0;
        algoBox = new JComboBox<>(new String[]{"All Algorithms (Compare)", "FIFO", "LRU", "Optimal"});
        algoBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        algoBox.setPreferredSize(new Dimension(210, 36));
        inputCard.add(algoBox, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        pagesInput = new JTextField("7 0 1 2 0 3 0 4 2 3 0 3 2");
        pagesInput.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        pagesInput.setBorder(new CompoundBorder(
                new LineBorder(new Color(203, 213, 225), 1),
                new EmptyBorder(4, 10, 4, 10)
        ));
        pagesInput.setPreferredSize(new Dimension(300, 36));
        inputCard.add(pagesInput, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0.0;
        framesInput = new JTextField("3");
        framesInput.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        framesInput.setHorizontalAlignment(SwingConstants.CENTER);
        framesInput.setBorder(new CompoundBorder(
                new LineBorder(new Color(203, 213, 225), 1),
                new EmptyBorder(4, 10, 4, 10)
        ));
        framesInput.setPreferredSize(new Dimension(110, 36));
        inputCard.add(framesInput, gbc);

        gbc.gridx = 3;
        JButton runBtn = createStyledButton("Run Simulation", PRIMARY_BLUE, PRIMARY_HOVER, Color.WHITE);
        runBtn.setPreferredSize(new Dimension(160, 36));
        runBtn.addActionListener(e -> startSimulation());
        inputCard.add(runBtn, gbc);

        gbc.gridx = 4;
        JButton clearBtn = createStyledButton("Clear", new Color(241, 245, 249), new Color(226, 232, 240), DARK_SLATE);
        clearBtn.setPreferredSize(new Dimension(85, 36));
        clearBtn.addActionListener(e -> clearAll());
        inputCard.add(clearBtn, gbc);

        workspace.add(inputCard, BorderLayout.NORTH);

        // Output Tabs Area
        tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabs.setBackground(CARD_BG);
        workspace.add(tabs, BorderLayout.CENTER);

        add(workspace, BorderLayout.CENTER);

        // 3. Bottom Status Banner
        statusBanner = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        statusBanner.setBackground(new Color(241, 245, 249));
        statusBanner.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_LIGHT));

        statusText = new JLabel("Ready. Enter a page-reference string and click 'Run Simulation'.");
        statusText.setFont(new Font("Segoe UI", Font.BOLD, 13));
        statusText.setForeground(DARK_SLATE);
        statusBanner.add(statusText);

        add(statusBanner, BorderLayout.SOUTH);

        // Auto-run initial sample on startup
        startSimulation();
    }

    private void applyPreset() {
        int idx = presetBox.getSelectedIndex();
        if (idx == 0) {
            pagesInput.setText("7 0 1 2 0 3 0 4 2 3 0 3 2");
            framesInput.setText("3");
        } else if (idx == 1) {
            pagesInput.setText("1 2 3 4 1 2 5 1 2 3 4 5");
            framesInput.setText("3");
        } else if (idx == 2) {
            pagesInput.setText("7 0 1 2 0 3 0 4 2 3 0 3 2 1 2 0 1 7 0 1");
            framesInput.setText("4");
        }
        startSimulation();
    }

    private JLabel makeFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(TEXT_MUTED);
        return label;
    }

    private JButton createStyledButton(String text, Color bg, Color hoverBg, Color fg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(hoverBg);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(bg);
            }
        });
        return btn;
    }

    private void clearAll() {
        pagesInput.setText("");
        framesInput.setText("");
        tabs.removeAll();
        statusBanner.setBackground(new Color(241, 245, 249));
        statusText.setForeground(DARK_SLATE);
        statusText.setText("Inputs cleared. Enter new values to simulate.");
    }

    private void startSimulation() {
        try {
            int[] pages = Models.getPages(pagesInput.getText());
            int frameCount = Models.getFrameCount(framesInput.getText());

            tabs.removeAll();
            String choice = (String) algoBox.getSelectedItem();
            List<Models.Result> results = new ArrayList<>();

            if ("FIFO".equals(choice) || "All Algorithms (Compare)".equals(choice)) {
                results.add(FifoLru.runFIFO(pages, frameCount));
            }            if ("LRU".equals(choice) || "All Algorithms (Compare)".equals(choice)) {
                results.add(FifoLru.runLRU(pages, frameCount));
            }
            if ("Optimal".equals(choice) || "All Algorithms (Compare)".equals(choice)) {
                results.add(Optimal.runOptimal(pages, frameCount));
            }

            for (Models.Result res : results) {
                tabs.addTab("  " + res.name + " Trace  ", makeTraceTab(res, frameCount));
            }

            if (results.size() > 1) {
                tabs.addTab("  Summary Comparison  ", makeCompareTab(results));
            }

            statusBanner.setBackground(HIT_BG);
            statusText.setForeground(HIT_TEXT);
            statusText.setText("Simulation Completed!   (" + pages.length + " Page References  |  " + frameCount + " Memory Frames)");

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Invalid Input", JOptionPane.WARNING_MESSAGE);
        }
    }

    private JPanel makeTraceTab(Models.Result res, int frameCount) {
        JPanel panel = new JPanel(new BorderLayout(0, 14));
        panel.setBackground(CARD_BG);
        panel.setBorder(new EmptyBorder(16, 18, 16, 18));

        // 4 KPI Metric Cards Row
        JPanel cardsRow = new JPanel(new GridLayout(1, 4, 12, 0));
        cardsRow.setOpaque(false);

        cardsRow.add(makeMetricCard("ALGORITHM & FRAMES", res.name + " (" + frameCount + " Frames)", DARK_SLATE, new Color(241, 245, 249)));
        cardsRow.add(makeMetricCard("FINAL FRAME CONTENTS", res.finalFrames.toString(), PRIMARY_BLUE, new Color(239, 246, 255)));
        cardsRow.add(makeMetricCard("TOTAL HITS (HIT RATIO)", String.format("%d  (%.2f%%)", res.hits, res.getHitRate()), HIT_TEXT, HIT_BG));
        cardsRow.add(makeMetricCard("TOTAL FAULTS (FAULT RATIO)", String.format("%d  (%.2f%%)", res.faults, res.getFaultRate()), FAULT_TEXT, FAULT_BG));

        panel.add(cardsRow, BorderLayout.NORTH);

        // Build Frame-by-Frame Trace Table
        int total = res.totalPages;
        String[] headers = new String[total + 1];
        headers[0] = "Reference String";
        for (int i = 0; i < total; i++) {
            headers[i + 1] = String.valueOf(res.steps.get(i).page);
        }

        Object[][] data = new Object[frameCount + 1][total + 1];
        for (int f = 0; f < frameCount; f++) {
            data[f][0] = "Frame " + (f + 1);
            for (int col = 0; col < total; col++) {
                data[f][col + 1] = res.steps.get(col).frameValues[f];
            }
        }

        data[frameCount][0] = "Status";
        for (int col = 0; col < total; col++) {
            data[frameCount][col + 1] = res.steps.get(col).status;
        }

        DefaultTableModel model = new DefaultTableModel(data, headers) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.setRowHeight(36);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setShowGrid(true);
        table.setGridColor(BORDER_LIGHT);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(DARK_SLATE);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setPreferredSize(new Dimension(0, 36));
        table.getTableHeader().setReorderingAllowed(false);
                table.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object val, boolean isSelected,
                                                           boolean hasFocus, int row, int col) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(tbl, val, isSelected, hasFocus, row, col);
                lbl.setBackground(DARK_SLATE);
                lbl.setForeground(Color.WHITE);
                lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
                lbl.setHorizontalAlignment(SwingConstants.CENTER);
                lbl.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, new Color(51, 65, 85)));
                return lbl;
            }
        });

        table.getColumnModel().getColumn(0).setPreferredWidth(145);
        for (int col = 1; col <= total; col++) {
            table.getColumnModel().getColumn(col).setPreferredWidth(68);
        }

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object val, boolean isSelected,
                                                           boolean hasFocus, int row, int col) {
                Component cell = super.getTableCellRendererComponent(tbl, val, isSelected, hasFocus, row, col);
                setHorizontalAlignment(SwingConstants.CENTER);

                if (col == 0) {
                    cell.setBackground(new Color(241, 245, 249));
                    cell.setForeground(DARK_SLATE);
                    setFont(new Font("Segoe UI", Font.BOLD, 12));
                    setToolTipText(null);
                } else {
                    Models.Step step = res.steps.get(col - 1);
                    boolean isHit = "HIT".equals(step.status);

                    if (isHit) {
                        cell.setBackground(HIT_BG);
                        cell.setForeground(row == frameCount ? HIT_TEXT : DARK_SLATE);
                    } else {
                        cell.setBackground(FAULT_BG);
                        cell.setForeground(row == frameCount ? FAULT_TEXT : DARK_SLATE);
                    }
                    setFont(new Font("Segoe UI", row == frameCount ? Font.BOLD : Font.PLAIN, 13));
                    setToolTipText("Step " + col + ": Page " + step.page + " -> " + step.status);
                }
                return cell;
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new LineBorder(BORDER_LIGHT, 1));
        panel.add(scrollPane, BorderLayout.CENTER);

        // Legend Footer
        JLabel legend = new JLabel("Legend:   Green = Page HIT (Already in memory)   |   Red = Page FAULT (Loaded/Replaced in memory)   |   Hover any cell for step details");
        legend.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        legend.setForeground(TEXT_MUTED);
        panel.add(legend, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel makeMetricCard(String labelText, String valueText, Color accentColor, Color bgTint) {
        JPanel card = new JPanel(new GridLayout(2, 1, 0, 4));
        card.setBackground(bgTint);
        card.setBorder(new CompoundBorder(
                new LineBorder(BORDER_LIGHT, 1, true),
                new EmptyBorder(10, 14, 10, 14)
        ));

        JLabel top = new JLabel(labelText);
        top.setFont(new Font("Segoe UI", Font.BOLD, 11));
        top.setForeground(TEXT_MUTED);

        JLabel bottom = new JLabel(valueText);
        bottom.setFont(new Font("Segoe UI", Font.BOLD, 16));
        bottom.setForeground(accentColor);

        card.add(top);
        card.add(bottom);
        return card;
    }

    private JPanel makeCompareTab(List<Models.Result> results) {
        JPanel panel = new JPanel(new BorderLayout(0, 14));
        panel.setBackground(CARD_BG);
        panel.setBorder(new EmptyBorder(20, 22, 20, 22));

        // Find lowest fault count to highlight best algorithm
        int minFaults = Integer.MAX_VALUE;
        String bestAlgo = "";
        for (Models.Result r : results) {
            if (r.faults < minFaults) {
                minFaults = r.faults;
                bestAlgo = r.name;
            }
        }

        JPanel topInfo = new JPanel(new BorderLayout());
        topInfo.setOpaque(false);

        JLabel title = new JLabel("Algorithm Performance Comparison");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(DARK_SLATE);

        JLabel badge = new JLabel("Most Efficient for This Input: " + bestAlgo + " (" + minFaults + " Faults)");
        badge.setFont(new Font("Segoe UI", Font.BOLD, 13));
        badge.setForeground(HIT_TEXT);

        topInfo.add(title, BorderLayout.WEST);
        topInfo.add(badge, BorderLayout.EAST);
        panel.add(topInfo, BorderLayout.NORTH);

        String[] cols = {"Algorithm", "Total References", "Total Hits", "Total Faults", "Hit Percentage", "Fault Percentage", "Final Frames"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        for (Models.Result res : results) {
            model.addRow(new Object[]{
                    res.name,
                    res.totalPages,
                    res.hits,
                    res.faults,
                    String.format("%.2f%%", res.getHitRate()),
                    String.format("%.2f%%", res.getFaultRate()),
                    res.finalFrames.toString()
            });
        }

        JTable table = new JTable(model);
        table.setRowHeight(42);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setGridColor(BORDER_LIGHT);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(DARK_SLATE);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setPreferredSize(new Dimension(0, 38));

        DefaultTableCellRenderer center = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object val, boolean isSelected,
                                                           boolean hasFocus, int row, int col) {
                Component c = super.getTableCellRendererComponent(tbl, val, isSelected, hasFocus, row, col);
                setHorizontalAlignment(SwingConstants.CENTER);
                if (col == 0) {
                    setFont(new Font("Segoe UI", Font.BOLD, 14));
                }
                return c;
            }
        };
        for (int i = 0; i < cols.length; i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(center);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new LineBorder(BORDER_LIGHT, 1));
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            }
            new MainApp().setVisible(true);
        });
    }
}
