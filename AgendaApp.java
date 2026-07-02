import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.time.*;
import java.time.format.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
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
    private Color[] eventColors = {Color.BLUE, Color.RED, Color.GREEN, Color.ORANGE, Color.MAGENTA};
    private int colorIndex = 0;

    public AgendaApp() {
        setTitle("📅 Mon Agenda Personnel");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        currentDate = LocalDate.now();
        events = new HashMap<>();
        loadEvents();

        createMenuBar();
        createToolBar();
        createMainPanel();
        createEventPanel();
        updateCalendar();

        setVisible(true);
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("Fichier");
        JMenuItem exportItem = new JMenuItem("Exporter les événements");
        exportItem.addActionListener(e -> exportEvents());
        JMenuItem importItem = new JMenuItem("Importer les événements");
        importItem.addActionListener(e -> importEvents());
        JMenuItem exitItem = new JMenuItem("Quitter");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exportItem);
        fileMenu.add(importItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        JMenu viewMenu = new JMenu("Affichage");
        JMenuItem todayItem = new JMenuItem("Aujourd'hui");
        todayItem.addActionListener(e -> {
            currentDate = LocalDate.now();
            updateCalendar();
        });
        viewMenu.add(todayItem);

        menuBar.add(fileMenu);
        menuBar.add(viewMenu);
        setJMenuBar(menuBar);
    }

    private void createToolBar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);

        JButton prevBtn = new JButton("◄");
        prevBtn.addActionListener(e -> {
            currentDate = currentDate.minusMonths(1);
            updateCalendar();
        });

        JButton nextBtn = new JButton("►");
        nextBtn.addActionListener(e -> {
            currentDate = currentDate.plusMonths(1);
            updateCalendar();
        });

        monthLabel = new JLabel();
        monthLabel.setFont(new Font("Arial", Font.BOLD, 16));
        monthLabel.setPreferredSize(new Dimension(200, 30));

        viewCombo = new JComboBox<>(new String[]{"Mois", "Semaine"});
        viewCombo.addActionListener(e -> updateCalendar());

        JButton addEventBtn = new JButton("➕ Ajouter Événement");
        addEventBtn.addActionListener(e -> showAddEventDialog());

        JButton clearDayBtn = new JButton("🗑️ Effacer Jour");
        clearDayBtn.addActionListener(e -> clearDayEvents());

        searchField = new JTextField(15);
        searchBtn = new JButton("🔍 Rechercher");
        searchBtn.addActionListener(e -> searchEvents());

        toolBar.add(prevBtn);
        toolBar.add(monthLabel);
        toolBar.add(nextBtn);
        toolBar.addSeparator();
        toolBar.add(new JLabel("Vue:"));
        toolBar.add(viewCombo);
        toolBar.addSeparator();
        toolBar.add(addEventBtn);
        toolBar.add(clearDayBtn);
        toolBar.addSeparator();
        toolBar.add(new JLabel("Recherche:"));
        toolBar.add(searchField);
        toolBar.add(searchBtn);

        add(toolBar, BorderLayout.NORTH);
    }

    private void createMainPanel() {
        mainPanel = new JPanel(new BorderLayout());

        String[] columns = {"Lun", "Mar", "Mer", "Jeu", "Ven", "Sam", "Dim"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        calendarTable = new JTable(tableModel);
        calendarTable.setRowHeight(80);
        calendarTable.setFont(new Font("Arial", Font.PLAIN, 12));
        calendarTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
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
                            // Ignorer
                        }
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(calendarTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);
    }

    private void createEventPanel() {
        eventPanel = new JPanel(new BorderLayout());
        eventPanel.setPreferredSize(new Dimension(300, 0));
        eventPanel.setBorder(BorderFactory.createTitledBorder("Événements du jour"));

        eventDisplay = new JTextArea();
        eventDisplay.setEditable(false);
        eventDisplay.setFont(new Font("Monospaced", Font.PLAIN, 13));
        JScrollPane scrollPane = new JScrollPane(eventDisplay);
        eventPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout());
        JButton editBtn = new JButton("✏️ Modifier");
        editBtn.addActionListener(e -> editSelectedEvent());
        JButton deleteBtn = new JButton("❌ Supprimer");
        deleteBtn.addActionListener(e -> deleteSelectedEvent());
        btnPanel.add(editBtn);
        btnPanel.add(deleteBtn);
        eventPanel.add(btnPanel, BorderLayout.SOUTH);

        add(eventPanel, BorderLayout.EAST);
    }

    private void updateCalendar() {
        String view = (String) viewCombo.getSelectedItem();
        if ("Mois".equals(view)) {
            updateMonthView();
        } else {
            updateWeekView();
        }
    }

    private void updateMonthView() {
        tableModel.setRowCount(0);
        monthLabel.setText(currentDate.getMonth().toString() + " " + currentDate.getYear());

        YearMonth yearMonth = YearMonth.from(currentDate);
        LocalDate firstOfMonth = yearMonth.atDay(1);
        int startDayOfWeek = firstOfMonth.getDayOfWeek().getValue() - 1;
        int daysInMonth = yearMonth.lengthOfMonth();

        Object[] row = new Object[7];
        for (int i = 0; i < 7; i++) {
            row[i] = "";
        }

        int day = 1;
        for (int i = startDayOfWeek; i < 7 && day <= daysInMonth; i++) {
            row[i] = day;
            day++;
        }
        tableModel.addRow(row);

        while (day <= daysInMonth) {
            row = new Object[7];
            for (int i = 0; i < 7 && day <= daysInMonth; i++) {
                row[i] = day;
                day++;
            }
            tableModel.addRow(row);
        }

        // Colorer les cellules avec événements
        for (int r = 0; r < tableModel.getRowCount(); r++) {
            for (int c = 0; c < 7; c++) {
                Object val = tableModel.getValueAt(r, c);
                if (val != null && !val.toString().isEmpty()) {
                    try {
                        int dayNum = Integer.parseInt(val.toString().split(" ")[0]);
                        LocalDate date = getDateFromCell(r, c, dayNum);
                        if (date != null && events.containsKey(date) && !events.get(date).isEmpty()) {
                            tableModel.setValueAt(dayNum + " 📌", r, c);
                        }
                    } catch (NumberFormatException e) {
                        // Ignorer
                    }
                }
            }
        }

        // Afficher les événements du jour sélectionné ou du jour actuel
        int selectedRow = calendarTable.getSelectedRow();
        int selectedCol = calendarTable.getSelectedColumn();
        if (selectedRow >= 0 && selectedCol >= 0) {
            Object val = tableModel.getValueAt(selectedRow, selectedCol);
            if (val != null && !val.toString().isEmpty()) {
                try {
                    int day1 = Integer.parseInt(val.toString().split(" ")[0]);
                    LocalDate date = getDateFromCell(selectedRow, selectedCol, day1);
                    if (date != null) {
                        showDayEvents(date);
                    }
                } catch (NumberFormatException e) {
                    showDayEvents(currentDate);
                }
            }
        } else {
            showDayEvents(currentDate);
        }

        // Mettre en surbrillance aujourd'hui
        highlightToday();
    }

    private void updateWeekView() {
        tableModel.setRowCount(1);
        monthLabel.setText("Semaine du " + currentDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        LocalDate startOfWeek = currentDate.with(DayOfWeek.MONDAY);
        Object[] row = new Object[7];
        for (int i = 0; i < 7; i++) {
            LocalDate date = startOfWeek.plusDays(i);
            int dayNum = date.getDayOfMonth();
            if (events.containsKey(date) && !events.get(date).isEmpty()) {
                row[i] = dayNum + " 📌";
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
                YearMonth yearMonth = YearMonth.from(currentDate);
                return yearMonth.atDay(day);
            } catch (DateTimeException e) {
                return null;
            }
        } else {
            LocalDate startOfWeek = currentDate.with(DayOfWeek.MONDAY);
            return startOfWeek.plusDays(col);
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
                        }
                    } catch (NumberFormatException e) {
                        // Ignorer
                    }
                }
            }
        }
    }

    private void showDayEvents(LocalDate date) {
        eventDisplay.setText("");
        if (events.containsKey(date) && !events.get(date).isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            eventDisplay.append("📅 " + date.format(formatter) + "\n");
            eventDisplay.append("═".repeat(30) + "\n\n");
            List<Event> dayEvents = events.get(date);
            Collections.sort(dayEvents);
            for (int i = 0; i < dayEvents.size(); i++) {
                Event e = dayEvents.get(i);
                eventDisplay.append("[" + (i+1) + "] ");
                if (e.time != null && !e.time.isEmpty()) {
                    eventDisplay.append("🕐 " + e.time + " - ");
                }
                eventDisplay.append(e.title + "\n");
                if (e.description != null && !e.description.isEmpty()) {
                    eventDisplay.append("   " + e.description + "\n");
                }
                eventDisplay.append("\n");
            }
        } else {
            eventDisplay.append("📭 Aucun événement pour ce jour\n");
            eventDisplay.append("Cliquez sur un jour pour voir ses événements");
        }
        eventDisplay.setCaretPosition(0);
    }

    private void showAddEventDialog() {
        JDialog dialog = new JDialog(this, "Ajouter un événement", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField dateField = new JTextField(currentDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        JTextField timeField = new JTextField("09:00");
        JTextField titleField = new JTextField();
        JTextArea descArea = new JTextArea(3, 20);
        JScrollPane descScroll = new JScrollPane(descArea);
        JComboBox<String> priorityCombo = new JComboBox<>(new String[]{"Normale", "Haute", "Basse"});

        panel.add(new JLabel("Date (jj/mm/aaaa):"));
        panel.add(dateField);
        panel.add(new JLabel("Heure (hh:mm):"));
        panel.add(timeField);
        panel.add(new JLabel("Titre:"));
        panel.add(titleField);
        panel.add(new JLabel("Description:"));
        panel.add(descScroll);
        panel.add(new JLabel("Priorité:"));
        panel.add(priorityCombo);

        JPanel btnPanel = new JPanel(new FlowLayout());
        JButton saveBtn = new JButton("💾 Enregistrer");
        JButton cancelBtn = new JButton("Annuler");

        saveBtn.addActionListener(e -> {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate date = LocalDate.parse(dateField.getText(), formatter);
                String title = titleField.getText().trim();
                if (title.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Le titre est obligatoire!", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Event event = new Event();
                event.title = title;
                event.time = timeField.getText().trim();
                event.description = descArea.getText().trim();
                event.priority = (String) priorityCombo.getSelectedItem();
                event.color = eventColors[colorIndex % eventColors.length];
                colorIndex++;

                events.computeIfAbsent(date, k -> new ArrayList<>()).add(event);
                saveEvents();
                updateCalendar();
                dialog.dispose();
                JOptionPane.showMessageDialog(this, "✅ Événement ajouté avec succès!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Format de date invalide! Utilisez jj/mm/aaaa", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        btnPanel.add(saveBtn);
        btnPanel.add(cancelBtn);

        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(btnPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void editSelectedEvent() {
        LocalDate selectedDate = getSelectedDate();
        if (selectedDate == null || !events.containsKey(selectedDate) || events.get(selectedDate).isEmpty()) {
            JOptionPane.showMessageDialog(this, "Aucun événement à modifier pour ce jour");
            return;
        }

        List<Event> dayEvents = events.get(selectedDate);
        String[] options = new String[dayEvents.size()];
        for (int i = 0; i < dayEvents.size(); i++) {
            options[i] = (i+1) + ". " + dayEvents.get(i).title;
        }

        String choice = (String) JOptionPane.showInputDialog(this, "Choisissez l'événement à modifier:", 
                "Modifier événement", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if (choice != null) {
            int index = Integer.parseInt(choice.split("\\.")[0]) - 1;
            Event event = dayEvents.get(index);

            JDialog dialog = new JDialog(this, "Modifier l'événement", true);
            dialog.setLayout(new BorderLayout());
            dialog.setSize(400, 350);
            dialog.setLocationRelativeTo(this);

            JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
            panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            JTextField timeField = new JTextField(event.time);
            JTextField titleField = new JTextField(event.title);
            JTextArea descArea = new JTextArea(event.description, 3, 20);
            JScrollPane descScroll = new JScrollPane(descArea);
            JComboBox<String> priorityCombo = new JComboBox<>(new String[]{"Normale", "Haute", "Basse"});
            priorityCombo.setSelectedItem(event.priority);

            panel.add(new JLabel("Heure (hh:mm):"));
            panel.add(timeField);
            panel.add(new JLabel("Titre:"));
            panel.add(titleField);
            panel.add(new JLabel("Description:"));
            panel.add(descScroll);
            panel.add(new JLabel("Priorité:"));
            panel.add(priorityCombo);

            JPanel btnPanel = new JPanel(new FlowLayout());
            JButton saveBtn = new JButton("💾 Enregistrer");
            JButton cancelBtn = new JButton("Annuler");

            saveBtn.addActionListener(e -> {
                String title = titleField.getText().trim();
                if (title.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Le titre est obligatoire!", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                event.title = title;
                event.time = timeField.getText().trim();
                event.description = descArea.getText().trim();
                event.priority = (String) priorityCombo.getSelectedItem();
                saveEvents();
                updateCalendar();
                dialog.dispose();
                JOptionPane.showMessageDialog(this, "✅ Événement modifié!");
            });

            cancelBtn.addActionListener(e -> dialog.dispose());
            btnPanel.add(saveBtn);
            btnPanel.add(cancelBtn);

            dialog.add(panel, BorderLayout.CENTER);
            dialog.add(btnPanel, BorderLayout.SOUTH);
            dialog.setVisible(true);
        }
    }

    private void deleteSelectedEvent() {
        LocalDate selectedDate = getSelectedDate();
        if (selectedDate == null || !events.containsKey(selectedDate) || events.get(selectedDate).isEmpty()) {
            JOptionPane.showMessageDialog(this, "Aucun événement à supprimer pour ce jour");
            return;
        }

        List<Event> dayEvents = events.get(selectedDate);
        String[] options = new String[dayEvents.size()];
        for (int i = 0; i < dayEvents.size(); i++) {
            options[i] = (i+1) + ". " + dayEvents.get(i).title;
        }

        String choice = (String) JOptionPane.showInputDialog(this, "Choisissez l'événement à supprimer:", 
                "Supprimer événement", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if (choice != null) {
            int index = Integer.parseInt(choice.split("\\.")[0]) - 1;
            int confirm = JOptionPane.showConfirmDialog(this, "Supprimer cet événement ?", 
                    "Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dayEvents.remove(index);
                if (dayEvents.isEmpty()) {
                    events.remove(selectedDate);
                }
                saveEvents();
                updateCalendar();
                JOptionPane.showMessageDialog(this, "🗑️ Événement supprimé!");
            }
        }
    }

    private void clearDayEvents() {
        LocalDate selectedDate = getSelectedDate();
        if (selectedDate == null || !events.containsKey(selectedDate)) {
            JOptionPane.showMessageDialog(this, "Aucun événement à effacer pour ce jour");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Effacer tous les événements du " + 
                selectedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " ?", 
                "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            events.remove(selectedDate);
            saveEvents();
            updateCalendar();
            JOptionPane.showMessageDialog(this, "🗑️ Jour effacé!");
        }
    }

    private void searchEvents() {
        String query = searchField.getText().trim().toLowerCase();
        if (query.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez entrer un terme de recherche");
            return;
        }

        StringBuilder results = new StringBuilder();
        results.append("🔍 Résultats pour \"" + query + "\":\n\n");
        int count = 0;

        for (Map.Entry<LocalDate, List<Event>> entry : events.entrySet()) {
            LocalDate date = entry.getKey();
            for (Event e : entry.getValue()) {
                if (e.title.toLowerCase().contains(query) || 
                    e.description.toLowerCase().contains(query)) {
                    results.append("📅 " + date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                    if (e.time != null && !e.time.isEmpty()) {
                        results.append(" 🕐 " + e.time);
                    }
                    results.append(" - " + e.title + "\n");
                    count++;
                }
            }
        }

        if (count == 0) {
            results.append("❌ Aucun résultat trouvé");
        } else {
            results.append("\n" + count + " résultat(s) trouvé(s)");
        }

        JTextArea textArea = new JTextArea(results.toString(), 20, 50);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        JOptionPane.showMessageDialog(this, scrollPane, "Résultats de recherche", JOptionPane.INFORMATION_MESSAGE);
    }

    private LocalDate getSelectedDate() {
        int row = calendarTable.getSelectedRow();
        int col = calendarTable.getSelectedColumn();
        if (row >= 0 && col >= 0) {
            Object val = tableModel.getValueAt(row, col);
            if (val != null && !val.toString().isEmpty()) {
                try {
                    int day = Integer.parseInt(val.toString().split(" ")[0]);
                    return getDateFromCell(row, col, day);
                } catch (NumberFormatException e) {
                    return null;
                }
            }
        }
        return currentDate;
    }

    private void saveEvents() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("agenda_data.ser"))) {
            oos.writeObject(events);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private void loadEvents() {
        File file = new File("agenda_data.ser");
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                events = (Map<LocalDate, List<Event>>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                events = new HashMap<>();
            }
        }
    }

    private void exportEvents() {
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File("agenda_export.txt"));
        int result = chooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            try (PrintWriter writer = new PrintWriter(chooser.getSelectedFile())) {
                writer.println("=== EXPORTATION DE L'AGENDA ===\n");
                for (Map.Entry<LocalDate, List<Event>> entry : events.entrySet()) {
                    writer.println("📅 " + entry.getKey().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                    for (Event e : entry.getValue()) {
                        writer.println("   - " + (e.time != null ? e.time + " " : "") + e.title);
                        if (e.description != null && !e.description.isEmpty()) {
                            writer.println("     " + e.description);
                        }
                    }
                    writer.println();
                }
                JOptionPane.showMessageDialog(this, "✅ Exportation réussie!");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "❌ Erreur lors de l'exportation");
            }
        }
    }

    private void importEvents() {
        JFileChooser chooser = new JFileChooser();
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            // Implémentation simple - on pourrait parser un fichier texte
            JOptionPane.showMessageDialog(this, "Fonction d'importation (à implémenter)");
        }
    }

    static class Event implements Serializable, Comparable<Event> {
        String title;
        String time;
        String description;
        String priority;
        Color color;

        @Override
        public int compareTo(Event o) {
            if (this.time == null && o.time == null) return 0;
            if (this.time == null) return 1;
            if (o.time == null) return -1;
            return this.time.compareTo(o.time);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new AgendaApp();
        });
    }
}