import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.time.*;
import java.time.format.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.*;

public class AgendaApp extends JFrame {
    private JTable calendarTable;
    private DefaultTableModel tableModel;
    private JLabel monthLabel;
    private JTextArea eventDisplay;
    private LocalDate currentDate;
    private Map<LocalDate, List<Event>> events;
    private JComboBox<String> viewCombo;
    private JPanel mainPanel;
    private JPanel eventPanel;
    private JTextField searchField;
    private JButton searchBtn;

    // Palette Palette Dark Mode Premium (Style GitHub Dark / Dracula)
    private final Color COLOR_BG = new Color(30, 30, 30);         // Fond principal
    private final Color COLOR_PANEL = new Color(40, 44, 52);      // Panneaux secondaires
    private final Color COLOR_TEXT = new Color(220, 223, 228);    // Texte principal
    private final Color COLOR_MUTED = new Color(157, 165, 180);   // Texte secondaire
    private final Color COLOR_ACCENT = new Color(97, 175, 239);   // Bleu Accent
    private final Color COLOR_SELECT = new Color(44, 49, 58);     // Cellule sélectionnée
    private final Color COLOR_BORDER = new Color(24, 26, 31);     // Bordures sombres

    public AgendaApp() {
        setTitle("Mon Agenda Personnel");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1150, 750);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        currentDate = LocalDate.now();
        events = new HashMap<>();
        loadEvents();

        // Harmonisation globale pour les JOptionPane dialogs
        UIManager.put("OptionPane.background", COLOR_BG);
        UIManager.put("Panel.background", COLOR_BG);
        UIManager.put("OptionPane.messageForeground", COLOR_TEXT);
        UIManager.put("Button.background", COLOR_PANEL);
        UIManager.put("Button.foreground", COLOR_TEXT);

        createMenuBar();
        createToolBar();
        createMainPanel();
        createEventPanel();
        updateCalendar();

        getContentPane().setBackground(COLOR_BG);
        setVisible(true);
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(COLOR_PANEL);
        menuBar.setBorder(BorderFactory.createLineBorder(COLOR_BORDER));

        JMenu fileMenu = new JMenu("Fichier");
        fileMenu.setForeground(COLOR_TEXT);
        
        JMenuItem exportItem = createMenuItem("Exporter");
        exportItem.addActionListener(e -> exportEvents());
        
        JMenuItem importItem = createMenuItem("Importer");
        importItem.addActionListener(e -> importEvents());
        
        JMenuItem exitItem = createMenuItem("Quitter");
        exitItem.addActionListener(e -> System.exit(0));
        
        fileMenu.add(exportItem);
        fileMenu.add(importItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        JMenu viewMenu = new JMenu("Affichage");
        viewMenu.setForeground(COLOR_TEXT);
        
        JMenuItem todayItem = createMenuItem("Aujourd'hui");
        todayItem.addActionListener(e -> {
            currentDate = LocalDate.now();
            updateCalendar();
        });
        viewMenu.add(todayItem);

        menuBar.add(fileMenu);
        menuBar.add(viewMenu);
        setJMenuBar(menuBar);
    }

    private JMenuItem createMenuItem(String text) {
        JMenuItem item = new JMenuItem(text);
        item.setBackground(COLOR_PANEL);
        item.setForeground(COLOR_TEXT);
        item.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        return item;
    }

    private void createToolBar() {
        JPanel toolBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        toolBar.setBackground(COLOR_PANEL);
        toolBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_BORDER));

        JButton prevBtn = createStyledButton("<");
        prevBtn.addActionListener(e -> {
            currentDate = "Mois".equals(viewCombo.getSelectedItem()) ? currentDate.minusMonths(1) : currentDate.minusWeeks(1);
            updateCalendar();
        });

        JButton nextBtn = createStyledButton(">");
        nextBtn.addActionListener(e -> {
            currentDate = "Mois".equals(viewCombo.getSelectedItem()) ? currentDate.plusMonths(1) : currentDate.plusWeeks(1);
            updateCalendar();
        });

        monthLabel = new JLabel("", SwingConstants.CENTER);
        monthLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        monthLabel.setForeground(COLOR_ACCENT);
        monthLabel.setPreferredSize(new Dimension(180, 25));

        viewCombo = new JComboBox<>(new String[]{"Mois", "Semaine"});
        styleComponent(viewCombo);
        viewCombo.addActionListener(e -> updateCalendar());

        JButton addEventBtn = createStyledButton("+ Ajouter");
        addEventBtn.addActionListener(e -> showAddEventDialog());

        JButton clearDayBtn = createStyledButton("Effacer Jour");
        clearDayBtn.addActionListener(e -> clearDayEvents());

        searchField = new JTextField(12);
        styleComponent(searchField);
        searchBtn = createStyledButton("Chercher");
        searchBtn.addActionListener(e -> searchEvents());

        toolBar.add(prevBtn);
        toolBar.add(monthLabel);
        toolBar.add(nextBtn);
        
        JSeparator sep1 = new JSeparator(JSeparator.VERTICAL);
        sep1.setPreferredSize(new Dimension(2, 20));
        toolBar.add(sep1);
        
        toolBar.add(viewCombo);
        toolBar.add(addEventBtn);
        toolBar.add(clearDayBtn);
        
        JSeparator sep2 = new JSeparator(JSeparator.VERTICAL);
        sep2.setPreferredSize(new Dimension(2, 20));
        toolBar.add(sep2);
        
        toolBar.add(searchField);
        toolBar.add(searchBtn);

        add(toolBar, BorderLayout.NORTH);
    }

    private void createMainPanel() {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(COLOR_BG);

        String[] columns = {"Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi", "Dimanche"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        calendarTable = new JTable(tableModel);
        calendarTable.setRowHeight(85);
        calendarTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        calendarTable.setBackground(COLOR_BG);
        calendarTable.setForeground(COLOR_TEXT);
        calendarTable.setSelectionBackground(COLOR_SELECT);
        calendarTable.setSelectionForeground(Color.WHITE);
        calendarTable.setGridColor(COLOR_BORDER);
        calendarTable.setShowGrid(true);
        calendarTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Customisation propre de l'en-tête de table (Sans bordures blanches natives)
        JTableHeader header = calendarTable.getTableHeader();
        header.setBackground(COLOR_PANEL);
        header.setForeground(COLOR_ACCENT);
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setReorderingAllowed(false);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_BORDER));

        calendarTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = calendarTable.getSelectedRow();
                int col = calendarTable.getSelectedColumn();
                if (row >= 0 && col >= 0) {
                    Object value = tableModel.getValueAt(row, col);
                    if (value != null && !value.toString().isEmpty()) {
                        try {
                            int day = Integer.parseInt(value.toString().split(" ")[0]);
                            LocalDate selectedDate = getDateFromCell(row, col, day);
                            if (selectedDate != null) {
                                showDayEvents(selectedDate);
                            }
                        } catch (NumberFormatException ex) {
                            // Safe protection
                        }
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(calendarTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(COLOR_BG);
        
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);
    }

    private void createEventPanel() {
        eventPanel = new JPanel(new BorderLayout());
        eventPanel.setPreferredSize(new Dimension(320, 0));
        eventPanel.setBackground(COLOR_PANEL);
        
        TitledBorder border = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(COLOR_SELECT), "Evenements du jour");
        border.setTitleColor(COLOR_ACCENT);
        border.setTitleFont(new Font("Segoe UI", Font.BOLD, 13));
        eventPanel.setBorder(border);

        eventDisplay = new JTextArea();
        eventDisplay.setEditable(false);
        eventDisplay.setBackground(COLOR_PANEL);
        eventDisplay.setForeground(COLOR_TEXT);
        eventDisplay.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        eventDisplay.setMargin(new Insets(15, 15, 15, 15));
        eventDisplay.setLineWrap(true);
        eventDisplay.setWrapStyleWord(true);
        
        JScrollPane scrollPane = new JScrollPane(eventDisplay);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        eventPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 15));
        btnPanel.setBackground(COLOR_PANEL);
        
        JButton editBtn = createStyledButton("Modifier");
        editBtn.addActionListener(e -> editSelectedEvent());
        JButton deleteBtn = createStyledButton("Supprimer");
        deleteBtn.addActionListener(e -> deleteSelectedEvent());
        
        btnPanel.add(editBtn);
        btnPanel.add(deleteBtn);
        eventPanel.add(btnPanel, BorderLayout.SOUTH);

        add(eventPanel, BorderLayout.EAST);
    }

    private void updateCalendar() {
        if (calendarTable == null) return;
        String view = (String) viewCombo.getSelectedItem();
        if ("Mois".equals(view)) {
            updateMonthView();
        } else {
            updateWeekView();
        }
    }

    private void updateMonthView() {
        tableModel.setRowCount(0);
        String txtMonth = currentDate.getMonth().getDisplayName(TextStyle.FULL, Locale.FRENCH);
        monthLabel.setText(txtMonth.substring(0, 1).toUpperCase() + txtMonth.substring(1) + " " + currentDate.getYear());

        YearMonth yearMonth = YearMonth.from(currentDate);
        LocalDate firstOfMonth = yearMonth.atDay(1);
        int startDayOfWeek = firstOfMonth.getDayOfWeek().getValue() - 1;
        int daysInMonth = yearMonth.lengthOfMonth();

        Object[] row = new Object[7];
        Arrays.fill(row, "");

        int day = 1;
        for (int i = startDayOfWeek; i < 7 && day <= daysInMonth; i++) {
            row[i] = day;
            day++;
        }
        tableModel.addRow(row);

        while (day <= daysInMonth) {
            row = new Object[7];
            Arrays.fill(row, "");
            for (int i = 0; i < 7 && day <= daysInMonth; i++) {
                row[i] = day;
                day++;
            }
            tableModel.addRow(row);
        }

        // Remplacement des Emojis par un marqueur propre texte "(*)"
        for (int r = 0; r < tableModel.getRowCount(); r++) {
            for (int c = 0; c < 7; c++) {
                Object val = tableModel.getValueAt(r, c);
                if (val != null && !val.toString().isEmpty()) {
                    try {
                        int dayNum = Integer.parseInt(val.toString().split(" ")[0]);
                        LocalDate date = getDateFromCell(r, c, dayNum);
                        if (date != null && events.containsKey(date) && !events.get(date).isEmpty()) {
                            tableModel.setValueAt(dayNum + "  (•)", r, c); // Indicateur propre sans bug d'encodage
                        }
                    } catch (NumberFormatException e) {
                        // Safe skip
                    }
                }
            }
        }
        highlightToday();
    }

    private void updateWeekView() {
        tableModel.setRowCount(0);
        LocalDate startOfWeek = currentDate.with(DayOfWeek.MONDAY);
        monthLabel.setText("Semaine du " + startOfWeek.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        Object[] row = new Object[7];
        for (int i = 0; i < 7; i++) {
            LocalDate date = startOfWeek.plusDays(i);
            int dayNum = date.getDayOfMonth();
            if (events.containsKey(date) && !events.get(date).isEmpty()) {
                row[i] = dayNum + "  (•)";
            } else {
                row[i] = dayNum;
            }
        }
        tableModel.addRow(row);
        highlightToday();
    }

    private LocalDate getDateFromCell(int row, int col, int day) {
        if ("Mois".equals(viewCombo.getSelectedItem())) {
            try {
                return currentDate.withDayOfMonth(day);
            } catch (DateTimeException e) {
                return null;
            }
        } else {
            return currentDate.with(DayOfWeek.MONDAY).plusDays(col);
        }
    }

    private void highlightToday() {
        LocalDate today = LocalDate.now();
        for (int r = 0; r < tableModel.getRowCount(); r++) {
            for (int c = 0; c < 7; c++) {
                Object val = tableModel.getValueAt(r, c);
                if (val != null && !val.toString().isEmpty()) {
                    try {
                        int day = Integer.parseInt(val.toString().split(" ")[0]);
                        LocalDate date = getDateFromCell(r, c, day);
                        if (date != null && date.equals(today)) {
                            calendarTable.changeSelection(r, c, false, false);
                            showDayEvents(date);
                            return;
                        }
                    } catch (NumberFormatException e) {
                        // Safe skip
                    }
                }
            }
        }
        showDayEvents(currentDate);
    }

    private void showDayEvents(LocalDate date) {
        eventDisplay.setText("");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.FRENCH);
        eventDisplay.append("Date : " + date.format(formatter) + "\n");
        eventDisplay.append("----------------------------------\n\n");

        if (events.containsKey(date) && !events.get(date).isEmpty()) {
            List<Event> dayEvents = events.get(date);
            Collections.sort(dayEvents);
            for (int i = 0; i < dayEvents.size(); i++) {
                Event e = dayEvents.get(i);
                eventDisplay.append("[" + (i + 1) + "] ");
                if (e.time != null && !e.time.isEmpty()) {
                    eventDisplay.append(e.time + " | ");
                }
                eventDisplay.append(e.title + " (" + e.priority + ")\n");
                if (e.description != null && !e.description.isEmpty()) {
                    eventDisplay.append("    " + e.description + "\n");
                }
                eventDisplay.append("\n");
            }
        } else {
            eventDisplay.append("Aucun evenement prevu.\n\n");
            eventDisplay.append("Cliquez sur '+ Ajouter' pour planifier votre jour.");
        }
        eventDisplay.setCaretPosition(0);
    }

    private boolean hasTimeConflict(LocalDate date, String time, Event excludeEvent) {
        if (!events.containsKey(date)) return false;
        for (Event existing : events.get(date)) {
            if (excludeEvent != null && existing == excludeEvent) continue;
            if (existing.time != null && existing.time.equals(time) && !time.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private void showAddEventDialog() {
        JDialog dialog = new JDialog(this, "Planifier un evenement", true);
        dialog.getContentPane().setBackground(COLOR_BG);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(420, 380);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBackground(COLOR_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField dateField = new JTextField(currentDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        JTextField timeField = new JTextField("09:00");
        JTextField titleField = new JTextField();
        JTextArea descArea = new JTextArea(3, 20);
        styleComponent(dateField); styleComponent(timeField); styleComponent(titleField);
        descArea.setBackground(COLOR_PANEL); descArea.setForeground(COLOR_TEXT); descArea.setCaretColor(Color.WHITE);
        
        JScrollPane descScroll = new JScrollPane(descArea);
        descScroll.setBorder(BorderFactory.createLineBorder(COLOR_SELECT));
        
        JComboBox<String> priorityCombo = new JComboBox<>(new String[]{"Normale", "Haute", "Basse"});
        styleComponent(priorityCombo);

        panel.add(createStyledLabel("Date (jj/mm/aaaa) :")); panel.add(dateField);
        panel.add(createStyledLabel("Heure (hh:mm) :")); panel.add(timeField);
        panel.add(createStyledLabel("Titre :")); panel.add(titleField);
        panel.add(createStyledLabel("Description :")); panel.add(descScroll);
        panel.add(createStyledLabel("Priorite :")); panel.add(priorityCombo);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        btnPanel.setBackground(COLOR_BG);
        JButton saveBtn = createStyledButton("Enregistrer");
        JButton cancelBtn = createStyledButton("Annuler");

        saveBtn.addActionListener(e -> {
            try {
                LocalDate date = LocalDate.parse(dateField.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                String title = titleField.getText().trim();
                String time = timeField.getText().trim();

                if (title.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Le titre est obligatoire.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (hasTimeConflict(date, time, null)) {
                    JOptionPane.showMessageDialog(dialog, "Conflit d'horaire detecte.", "Attention", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                Event event = new Event();
                event.title = title;
                event.time = time;
                event.description = descArea.getText().trim();
                event.priority = (String) priorityCombo.getSelectedItem();

                events.computeIfAbsent(date, k -> new ArrayList<>()).add(event);
                saveEvents();
                updateCalendar();
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Format de date incorrect.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelBtn.addActionListener(e -> dialog.dispose());
        btnPanel.add(saveBtn); btnPanel.add(cancelBtn);

        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(btnPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void editSelectedEvent() {
        LocalDate selectedDate = getSelectedDate();
        if (selectedDate == null || !events.containsKey(selectedDate) || events.get(selectedDate).isEmpty()) {
            JOptionPane.showMessageDialog(this, "Aucun evenement a modifier.");
            return;
        }

        List<Event> dayEvents = events.get(selectedDate);
        String[] options = new String[dayEvents.size()];
        for (int i = 0; i < dayEvents.size(); i++) {
            options[i] = (i + 1) + ". " + dayEvents.get(i).title;
        }

        String choice = (String) JOptionPane.showInputDialog(this, "Selectionnez l'element :", "Modifier", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        if (choice != null) {
            int index = Integer.parseInt(choice.split("\\.")[0]) - 1;
            Event event = dayEvents.get(index);
            // Re-use logic standard ou dialogue customisé ici
            showAddEventDialog();
            dayEvents.remove(event);
            updateCalendar();
        }
    }

    private void deleteSelectedEvent() {
        LocalDate selectedDate = getSelectedDate();
        if (selectedDate == null || !events.containsKey(selectedDate) || events.get(selectedDate).isEmpty()) {
            JOptionPane.showMessageDialog(this, "Aucun evenement a supprimer.");
            return;
        }

        List<Event> dayEvents = events.get(selectedDate);
        String[] options = new String[dayEvents.size()];
        for (int i = 0; i < dayEvents.size(); i++) {
            options[i] = (i + 1) + ". " + dayEvents.get(i).title;
        }

        String choice = (String) JOptionPane.showInputDialog(this, "Selectionnez l'element a supprimer :", "Supprimer", JOptionPane.WARNING_MESSAGE, null, options, options[0]);
        if (choice != null) {
            int index = Integer.parseInt(choice.split("\\.")[0]) - 1;
            dayEvents.remove(index);
            if (dayEvents.isEmpty()) events.remove(selectedDate);
            saveEvents();
            updateCalendar();
        }
    }

    private void clearDayEvents() {
        LocalDate selectedDate = getSelectedDate();
        if (selectedDate == null || !events.containsKey(selectedDate)) {
            JOptionPane.showMessageDialog(this, "Rien a effacer.");
            return;
        }
        events.remove(selectedDate);
        saveEvents();
        updateCalendar();
    }

    private void searchEvents() {
        String query = searchField.getText().trim().toLowerCase();
        if (query.isEmpty()) return;

        StringBuilder results = new StringBuilder("Resultats pour : " + query + "\n\n");
        events.forEach((date, list) -> list.forEach(e -> {
            if (e.title.toLowerCase().contains(query) || e.description.toLowerCase().contains(query)) {
                results.append("- ").append(date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                        .append(" : ").append(e.title).append("\n");
            }
        }));
        
        JTextArea area = new JTextArea(results.toString(), 15, 40);
        area.setBackground(COLOR_PANEL); area.setForeground(COLOR_TEXT);
        JOptionPane.showMessageDialog(this, new JScrollPane(area), "Recherche", JOptionPane.INFORMATION_MESSAGE);
    }

    private LocalDate getSelectedDate() {
        int row = calendarTable.getSelectedRow();
        int col = calendarTable.getSelectedColumn();
        if (row >= 0 && col >= 0) {
            Object val = tableModel.getValueAt(row, col);
            if (val != null && !val.toString().isEmpty()) {
                int day = Integer.parseInt(val.toString().split(" ")[0]);
                return getDateFromCell(row, col, day);
            }
        }
        return currentDate;
    }

    private void saveEvents() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("agenda_data.ser"))) {
            oos.writeObject(events);
        } catch (IOException e) { e.printStackTrace(); }
    }

    @SuppressWarnings("unchecked")
    private void loadEvents() {
        File file = new File("agenda_data.ser");
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                events = (Map<LocalDate, List<Event>>) ois.readObject();
            } catch (Exception e) { events = new HashMap<>(); }
        }
    }

    private void exportEvents() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try (PrintWriter writer = new PrintWriter(chooser.getSelectedFile())) {
                events.forEach((date, list) -> {
                    writer.println("[" + date + "]");
                    list.forEach(e -> writer.println(e.time + ";" + e.title + ";" + e.description + ";" + e.priority));
                });
                JOptionPane.showMessageDialog(this, "Exportation réussie !");
            } catch (Exception e) { /* Safe catch */ }
        }
    }

    private void importEvents() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try (BufferedReader reader = new BufferedReader(new FileReader(chooser.getSelectedFile()))) {
                String line;
                LocalDate currentImportDate = null;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("[") && line.endsWith("]")) {
                        currentImportDate = LocalDate.parse(line.substring(1, line.length() - 1));
                    } else if (currentImportDate != null && !line.isEmpty()) {
                        String[] tokens = line.split(";");
                        if (tokens.length >= 2) {
                            Event e = new Event();
                            e.time = tokens[0];
                            e.title = tokens[1];
                            e.description = tokens.length > 2 ? tokens[2] : "";
                            e.priority = tokens.length > 3 ? tokens[3] : "Normale";
                            events.computeIfAbsent(currentImportDate, k -> new ArrayList<>()).add(e);
                        }
                    }
                }
                saveEvents();
                updateCalendar();
                JOptionPane.showMessageDialog(this, "Importation reussie !");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erreur fichier.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // --- HOVER EFFECT STYLING UTILS ---
    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(COLOR_PANEL);
        btn.setForeground(COLOR_TEXT);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_SELECT, 1),
                BorderFactory.createEmptyBorder(6, 14, 6, 14)));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(true);

        // Effet de survol natif moderne
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                btn.setBackground(COLOR_SELECT);
                btn.setForeground(Color.WHITE);
            }
            public void mouseExited(MouseEvent evt) {
                btn.setBackground(COLOR_PANEL);
                btn.setForeground(COLOR_TEXT);
            }
        });
        return btn;
    }

    private JLabel createStyledLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(COLOR_TEXT);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        return lbl;
    }

    private void styleComponent(JComponent comp) {
        comp.setBackground(COLOR_BG);
        comp.setForeground(COLOR_TEXT);
        comp.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comp.setBorder(BorderFactory.createLineBorder(COLOR_SELECT));
        if (comp instanceof JTextField) {
            ((JTextField) comp).setCaretColor(Color.WHITE);
        }
    }

    static class Event implements Serializable, Comparable<Event> {
        String title;
        String time;
        String description;
        String priority;

        @Override
        public int compareTo(Event o) {
            if (this.time == null || o.time == null) return 0;
            return this.time.compareTo(o.time);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AgendaApp::new);
    }
}