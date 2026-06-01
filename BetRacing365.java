import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

/**
 * BetRacing365 - DSA Assignment 1: Sorting Algorithm
 * Uses Merge Sort algorithm for sorting horses and jockeys.
 * Note: When sorting by horse, the corresponding jockey moves with it, and vice versa.
 */
public class BetRacing365 extends JFrame {

    // Data 
    private String[] horses  = new String[10];
    private String[] jockeys = new String[10];
    private int[]    numbers = new int[10]; // horse numbers / race positions

    // UI Components 
    private JTextField[] horseFields  = new JTextField[10];
    private JTextField[] jockeyFields = new JTextField[10];
    private JTextField[] numberFields = new JTextField[10];

    private DefaultTableModel tableModel;
    private JTable resultTable;

    private JLabel statusLabel;
    private JButton sortHorseAscBtn, sortHorseDescBtn;
    private JButton sortJockeyAscBtn, sortJockeyDescBtn;
    private JButton sortNumAscBtn, sortNumDescBtn;
    private JButton loadBtn, clearBtn, randomBtn;

    // Colors / Fonts 
    private static final Color BG_DARK    = new Color(37, 41, 45);
    private static final Color BG_PANEL   = new Color(37, 41, 45);
    private static final Color BG_INPUT   = new Color(37, 41, 45);
    private static final Color ACCENT     = new Color(37, 99, 235);
    private static final Color ACCENT2    = new Color(239, 68, 68);
    private static final Color TEXT_LIGHT = new Color(255,255,255);
    private static final Color TEXT_DIM   = new Color(100, 116, 139);
    private static final Color BORDER_COL = new Color(226, 232, 240);

    private static final Font TITLE_FONT   = new Font("Segoe UI", Font.BOLD, 24);
    private static final Font HEADER_FONT  = new Font("Segoe UI", Font.BOLD, 16);
    private static final Font LABEL_FONT   = new Font("Segoe UI", Font.PLAIN, 12);
    private static final Font INPUT_FONT   = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font BUTTON_FONT  = new Font("Segoe UI", Font.BOLD, 13);
    private static final Font STATUS_FONT  = new Font("Segoe UI", Font.ITALIC, 12);

    // Constructor
    public BetRacing365() {
        super("BetRacing365 - Sorting Management System");
        initData();
        buildUI();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1050, 720));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Init default data 
    private void initData() {
        for (int i = 0; i < 10; i++) {
            horses[i]  = "";
            jockeys[i] = "";
            numbers[i] = i + 1;
        }
    }

    //  UI CONSTRUCTION
    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(BG_DARK);

        root.add(buildHeader(),      BorderLayout.NORTH);
        root.add(buildCenter(),      BorderLayout.CENTER);
        root.add(buildStatusBar(),   BorderLayout.SOUTH);

        setContentPane(root);
    }

    // Header
    private JPanel buildHeader() {
        JPanel hdr = new JPanel(new BorderLayout());
        hdr.setBackground(BG_PANEL);
        hdr.setBorder(new MatteBorder(0, 0, 2, 0, ACCENT));

        JLabel title = new JLabel("  BetRacing365", SwingConstants.LEFT);
        title.setFont(TITLE_FONT);
        title.setForeground(ACCENT);
        title.setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 0));

        JLabel sub = new JLabel("Horse & Jockey Sorting Management System  ", SwingConstants.RIGHT);
        sub.setFont(LABEL_FONT);
        sub.setForeground(TEXT_DIM);
        sub.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));

        hdr.add(title, BorderLayout.WEST);
        hdr.add(sub,   BorderLayout.EAST);
        return hdr;
    }

    // Center split: input left, results right
    private JPanel buildCenter() {
        JPanel center = new JPanel(new GridLayout(1, 2, 10, 0));
        center.setBackground(BG_DARK);
        center.setBorder(BorderFactory.createEmptyBorder(12, 12, 6, 12));

        center.add(buildInputPanel());
        center.add(buildOutputPanel());
        return center;
    }

    // Input Panel 
    private JPanel buildInputPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBackground(BG_PANEL);
        panel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER_COL, 1, true),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Title
        JLabel lbl = new JLabel("  Enter Horses & Jockeys");
        lbl.setFont(HEADER_FONT);
        lbl.setForeground(ACCENT);
        lbl.setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 0));

        // Column headers
        JPanel colHdr = new JPanel(new GridLayout(1, 3, 6, 0));
        colHdr.setBackground(BG_PANEL);
        colHdr.add(makeColLabel("#"));
        colHdr.add(makeColLabel("Horse Name"));
        colHdr.add(makeColLabel("Jockey Name"));

        // Rows
        JPanel rows = new JPanel(new GridLayout(10, 1, 0, 4));
        rows.setBackground(BG_PANEL);
        for (int i = 0; i < 10; i++) {
            rows.add(buildInputRow(i));
        }

        // Buttons
        JPanel btns = buildInputButtons();

        panel.add(lbl,   BorderLayout.NORTH);
        JPanel body = new JPanel(new BorderLayout(0, 4));
        body.setBackground(BG_PANEL);
        body.add(colHdr, BorderLayout.NORTH);
        body.add(rows,   BorderLayout.CENTER);
        panel.add(body,  BorderLayout.CENTER);
        panel.add(btns,  BorderLayout.SOUTH);
        return panel;
    }

    private JLabel makeColLabel(String text) {
        JLabel l = new JLabel(text, SwingConstants.CENTER);
        l.setFont(new Font("Segoe UI", Font.BOLD, 11));
        l.setForeground(TEXT_DIM);
        return l;
    }

    private JPanel buildInputRow(int i) {
        JPanel row = new JPanel(new GridLayout(1, 3, 6, 0));
        row.setBackground(BG_PANEL);

        numberFields[i] = new JTextField(String.valueOf(i + 1));
        numberFields[i].setFont(INPUT_FONT);
        numberFields[i].setHorizontalAlignment(SwingConstants.CENTER);
        styleField(numberFields[i]);

        horseFields[i]  = new JTextField();
        jockeyFields[i] = new JTextField();
        styleField(horseFields[i]);
        styleField(jockeyFields[i]);

        row.add(numberFields[i]);
        row.add(horseFields[i]);
        row.add(jockeyFields[i]);
        return row;
    }

    private JPanel buildInputButtons() {
        JPanel p = new JPanel(new GridLayout(2, 2, 6, 6));
        p.setBackground(BG_PANEL);
        p.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));

        loadBtn   = makeBtn("Load Data",   ACCENT,  BG_DARK);
        randomBtn = makeBtn("Generate Sample Data",      new Color(70,140,200), BG_DARK);
        clearBtn  = makeBtn("Clear Data",         ACCENT2, BG_DARK);

        loadBtn.addActionListener(e -> loadData());
        randomBtn.addActionListener(e -> fillRandom());
        clearBtn.addActionListener(e -> clearAll());

        p.add(loadBtn);
        p.add(randomBtn);
        p.add(clearBtn);
        p.add(new JLabel()); // spacer
        return p;
    }

    // Output Panel
    private JPanel buildOutputPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBackground(BG_PANEL);
        panel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER_COL, 1, true),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JLabel lbl = new JLabel("Sorted Results");
        lbl.setFont(HEADER_FONT);
        lbl.setForeground(ACCENT);
        lbl.setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 0));

        // Table
        String[] cols = {"#", "Horse Name", "Jockey Name"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        resultTable = new JTable(tableModel);
        styleTable();

        JScrollPane scroll = new JScrollPane(resultTable);
        scroll.setBorder(new LineBorder(BORDER_COL, 1));
        scroll.getViewport().setBackground(BG_INPUT);

        // Sort buttons
        JPanel sortBtns = buildSortButtons();

        panel.add(lbl,      BorderLayout.NORTH);
        panel.add(scroll,   BorderLayout.CENTER);
        panel.add(sortBtns, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buildSortButtons() {
        JPanel p = new JPanel(new GridLayout(3, 2, 6, 6));
        p.setBackground(BG_PANEL);
        p.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));

        sortHorseAscBtn  = makeBtn("Sort Horse A-Z",   new Color(60,150,100), BG_DARK);
        sortHorseDescBtn = makeBtn("Sort Horse Z-A",  new Color(60,150,100), BG_DARK);
        sortJockeyAscBtn = makeBtn("Sort Jockey A-Z",  new Color(80,120,190), BG_DARK);
        sortJockeyDescBtn= makeBtn("Sort Jockey Z-A", new Color(80,120,190), BG_DARK);
        sortNumAscBtn    = makeBtn("Sort Number Asc",   new Color(140,90,190), BG_DARK);
        sortNumDescBtn   = makeBtn("Sort Number Desc",  new Color(140,90,190), BG_DARK);

        sortHorseAscBtn.addActionListener(e  -> sortAndDisplay("HORSE",  true));
        sortHorseDescBtn.addActionListener(e -> sortAndDisplay("HORSE",  false));
        sortJockeyAscBtn.addActionListener(e  -> sortAndDisplay("JOCKEY", true));
        sortJockeyDescBtn.addActionListener(e -> sortAndDisplay("JOCKEY", false));
        sortNumAscBtn.addActionListener(e    -> sortAndDisplay("NUMBER", true));
        sortNumDescBtn.addActionListener(e   -> sortAndDisplay("NUMBER", false));

        p.add(sortHorseAscBtn);  p.add(sortHorseDescBtn);
        p.add(sortJockeyAscBtn); p.add(sortJockeyDescBtn);
        p.add(sortNumAscBtn);    p.add(sortNumDescBtn);
        return p;
    }

    // Status bar
    private JPanel buildStatusBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(new Color(10, 14, 25));
        bar.setBorder(new MatteBorder(1, 0, 0, 0, BORDER_COL));

        statusLabel = new JLabel("  Ready — enter horse & jockey names, then press Load & Display.");
        statusLabel.setFont(STATUS_FONT);
        statusLabel.setForeground(TEXT_DIM);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));

        JLabel algo = new JLabel("Algorithm: Merge Sort  ");
        algo.setFont(STATUS_FONT);
        algo.setForeground(ACCENT);
        algo.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));

        bar.add(statusLabel, BorderLayout.WEST);
        bar.add(algo,        BorderLayout.EAST);
        return bar;
    }

    //  STYLING HELPERS
    private void styleField(JTextField f) {
        f.setFont(INPUT_FONT);
        f.setBackground(BG_INPUT);
        f.setForeground(TEXT_LIGHT);
        f.setCaretColor(ACCENT);
        f.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER_COL, 1),
            BorderFactory.createEmptyBorder(3, 6, 3, 6)
        ));
    }

    private JButton makeBtn(String text, Color bg, Color fg) {
        JButton b = new JButton(text);
        b.setFont(BUTTON_FONT);
        b.setBackground(bg);
        b.setForeground(fg);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setOpaque(true);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addMouseListener(new MouseAdapter() {
            Color orig = bg;
            public void mouseEntered(MouseEvent e) {
                b.setBackground(orig.brighter());
            }
            public void mouseExited(MouseEvent e) {
                b.setBackground(orig);
            }
        });
        return b;
    }

    private void styleTable() {
        resultTable.setFont(INPUT_FONT);
        resultTable.setBackground(BG_INPUT);
        resultTable.setForeground(TEXT_LIGHT);
        resultTable.setSelectionBackground(new Color(80, 70, 40));
        resultTable.setSelectionForeground(ACCENT);
        resultTable.setGridColor(BORDER_COL);
        resultTable.setRowHeight(28);
        resultTable.setShowGrid(true);
        resultTable.getTableHeader().setFont(HEADER_FONT);
        resultTable.getTableHeader().setBackground(BG_PANEL);
        resultTable.getTableHeader().setForeground(ACCENT);
        resultTable.getTableHeader().setBorder(new LineBorder(BORDER_COL, 1));

        // Alternating row renderer
        resultTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int col) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                setFont(INPUT_FONT);
                setHorizontalAlignment(col == 0 ? SwingConstants.CENTER : SwingConstants.LEFT);
                if (isSelected) {
                    setBackground(new Color(80, 70, 40));
                    setForeground(ACCENT);
                } else if (row % 2 == 0) {
                    setBackground(BG_INPUT);
                    setForeground(TEXT_LIGHT);
                } else {
                    setBackground(new Color(40, 52, 80));
                    setForeground(TEXT_LIGHT);
                }
                setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
                return this;
            }
        });

        // Column widths
        resultTable.getColumnModel().getColumn(0).setMaxWidth(50);
    }

    //  ACTIONS

    /** Read fields → internal arrays → refresh table */
    private void loadData() {
        for (int i = 0; i < 10; i++) {
            horses[i]  = horseFields[i].getText().trim();
            jockeys[i] = jockeyFields[i].getText().trim();
            try {
                numbers[i] = Integer.parseInt(numberFields[i].getText().trim());
            } catch (NumberFormatException ex) {
                numbers[i] = i + 1;
                numberFields[i].setText(String.valueOf(i + 1));
            }
        }
        refreshTable();
        setStatus("✔  Data loaded — 10 horses & jockeys ready to sort.");
    }

    private void clearAll() {
        for (int i = 0; i < 10; i++) {
            horseFields[i].setText("");
            jockeyFields[i].setText("");
            numberFields[i].setText(String.valueOf(i + 1));
            horses[i]  = "";
            jockeys[i] = "";
            numbers[i] = i + 1;
        }
        tableModel.setRowCount(0);
        setStatus("  All fields cleared.");
    }

    private void fillRandom() {
        String[] sampleHorses  = {"Thunder Bolt","Silver Storm","Black Arrow","Golden Wind",
            "Iron Fist","Royal Flush","Midnight Sun","Desert Rose","Crimson Tide","Lucky Star"};
        String[] sampleJockeys = {"Marco Rossi","Liam Stone","Zara Ahmed","Kai Tanaka",
            "Sofia Moreau","Jake Riley","Ana Costa","Tom Harper","Mei Lin","Carlos Vega"};

        // Shuffle by picking random permutation
        ArrayList<Integer> idx = new ArrayList<>();
        for (int i = 0; i < 10; i++) idx.add(i);
        java.util.Collections.shuffle(idx);

        for (int i = 0; i < 10; i++) {
            horseFields[i].setText(sampleHorses[idx.get(i)]);
            jockeyFields[i].setText(sampleJockeys[idx.get(i)]);
            numberFields[i].setText(String.valueOf(i + 1));
        }
        loadData();
        setStatus("🎲  Random horses & jockeys filled and loaded.");
    }

    //  SORTING

    /**
     * Sort arrays using Merge Sort on the chosen key,
     * keeping horses, jockeys and numbers in sync.
     */
    private void sortAndDisplay(String key, boolean ascending) {
        // Work on copies
        String[] h = horses.clone();
        String[] j = jockeys.clone();
        int[]    n = numbers.clone();

        mergeSort(h, j, n, 0, h.length - 1, key, ascending);

        // Show result
        tableModel.setRowCount(0);
        for (int i = 0; i < 10; i++) {
            tableModel.addRow(new Object[]{n[i], h[i], j[i]});
        }

        String dir  = ascending ? "Ascending ▲" : "Descending ▼";
        String field = key.equals("HORSE") ? "Horse Name" : key.equals("JOCKEY") ? "Jockey Name" : "Race Number";
        setStatus("✔  Sorted by " + field + " — " + dir + "  (Merge Sort)");
    }

    // Merge Sort implementation that sorts based on the specified key while keeping all arrays in sync
    private void mergeSort(String[] h, String[] j, int[] n,
                            int left, int right, String key, boolean asc) {
        if (left >= right) return;
        int mid = (left + right) / 2;
        mergeSort(h, j, n, left,    mid,   key, asc);
        mergeSort(h, j, n, mid + 1, right, key, asc);
        merge(h, j, n, left, mid, right, key, asc);
    }

    private void merge(String[] h, String[] j, int[] n,
                       int left, int mid, int right, String key, boolean asc) {
        int len1 = mid - left + 1;
        int len2 = right - mid;

        String[] lh = new String[len1], rh = new String[len2];
        String[] lj = new String[len1], rj = new String[len2];
        int[]    ln = new int[len1],    rn = new int[len2];

        System.arraycopy(h, left,    lh, 0, len1);
        System.arraycopy(h, mid + 1, rh, 0, len2);
        System.arraycopy(j, left,    lj, 0, len1);
        System.arraycopy(j, mid + 1, rj, 0, len2);
        System.arraycopy(n, left,    ln, 0, len1);
        System.arraycopy(n, mid + 1, rn, 0, len2);

        int i = 0, k = 0, idx = left;
        while (i < len1 && k < len2) {
            int cmp = compare(lh[i], lj[i], ln[i], rh[k], rj[k], rn[k], key);
            if (asc ? cmp <= 0 : cmp >= 0) {
                h[idx] = lh[i]; j[idx] = lj[i]; n[idx] = ln[i]; i++;
            } else {
                h[idx] = rh[k]; j[idx] = rj[k]; n[idx] = rn[k]; k++;
            }
            idx++;
        }
        while (i < len1) { h[idx] = lh[i]; j[idx] = lj[i]; n[idx] = ln[i++]; idx++; }
        while (k < len2) { h[idx] = rh[k]; j[idx] = rj[k]; n[idx] = rn[k++]; idx++; }
    }

    /** Compare two entries by the chosen sort key */
    private int compare(String h1, String j1, int n1,
                        String h2, String j2, int n2, String key) {
        switch (key) {
            case "HORSE":  return h1.compareToIgnoreCase(h2);
            case "JOCKEY": return j1.compareToIgnoreCase(j2);
            case "NUMBER": return Integer.compare(n1, n2);
            default:       return 0;
        }
    }

    // Table refresh (unsorted) after loading new data
    private void refreshTable() {
        tableModel.setRowCount(0);
        for (int i = 0; i < 10; i++) {
            tableModel.addRow(new Object[]{numbers[i], horses[i], jockeys[i]});
        }
    }

    private void setStatus(String msg) {
        statusLabel.setText("  " + msg);
    }

    //  MAIN
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(BetRacing365::new);
    }
}
