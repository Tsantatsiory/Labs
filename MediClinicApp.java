import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

// ===================== MODELS =====================

class Patient {
    private int id;
    private String name;
    private int age;
    private String phone;

    public Patient(int id, String name, int age, String phone) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.phone = phone;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    @Override
    public String toString() {
        return String.format("%-8s %-20s %-6s %-15s", 
            "ID: " + id, "Name: " + name, "Age: " + age, "Phone: " + phone);
    }
}

class Doctor {
    private int id;
    private String name;
    private String specialty;

    public Doctor(int id, String name, String specialty) {
        this.id = id;
        this.name = name;
        this.specialty = specialty;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSpecialty() { return specialty; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }

    @Override
    public String toString() {
        return String.format("%-8s %-25s %-20s", 
            "ID: " + id, "Name: " + name, "Specialty: " + specialty);
    }
}

class Consultation {
    private int patientId;
    private int doctorId;
    private String date;
    private String diagnosis;

    public Consultation(int patientId, int doctorId, String date, String diagnosis) {
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.date = date;
        this.diagnosis = diagnosis;
    }

    public int getPatientId() { return patientId; }
    public int getDoctorId() { return doctorId; }
    public String getDate() { return date; }
    public String getDiagnosis() { return diagnosis; }

    @Override
    public String toString() {
        return String.format("%-12s %-12s %-12s %-20s", 
            "Patient: " + patientId, "Doctor: " + doctorId, "Date: " + date, "Diagnosis: " + diagnosis);
    }
}

// ===================== LINKED LISTS =====================

class PatientLinkedList {
    private Node head;
    private int size;

    private class Node {
        Patient data;
        Node next;
        Node(Patient data) { this.data = data; }
    }

    public void add(Patient p) {
        Node newNode = new Node(p);
        if (head == null) {
            head = newNode;
        } else {
            Node temp = head;
            while (temp.next != null) temp = temp.next;
            temp.next = newNode;
        }
        size++;
    }

    public Patient get(int index) {
        if (index < 0 || index >= size) return null;
        Node temp = head;
        for (int i = 0; i < index; i++) temp = temp.next;
        return temp.data;
    }

    public boolean update(int id, Patient newData) {
        Node temp = head;
        while (temp != null) {
            if (temp.data.getId() == id) {
                if (newData.getName() != null && !newData.getName().trim().isEmpty()) {
                    temp.data.setName(newData.getName());
                }
                if (newData.getAge() > 0) {
                    temp.data.setAge(newData.getAge());
                }
                if (newData.getPhone() != null && !newData.getPhone().trim().isEmpty()) {
                    temp.data.setPhone(newData.getPhone());
                }
                return true;
            }
            temp = temp.next;
        }
        return false;
    }

    public boolean delete(int id) {
        if (head == null) return false;
        if (head.data.getId() == id) {
            head = head.next;
            size--;
            return true;
        }
        Node prev = head;
        Node curr = head.next;
        while (curr != null) {
            if (curr.data.getId() == id) {
                prev.next = curr.next;
                size--;
                return true;
            }
            prev = curr;
            curr = curr.next;
        }
        return false;
    }

    public String display() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-8s %-20s %-6s %-15s\n", "ID", "Name", "Age", "Phone"));
        sb.append("----------------------------------------------------------------\n");
        Node temp = head;
        while (temp != null) {
            sb.append(temp.data.toString()).append("\n");
            temp = temp.next;
        }
        return sb.toString();
    }

    public Patient searchById(int id) {
        Node temp = head;
        while (temp != null) {
            if (temp.data.getId() == id) return temp.data;
            temp = temp.next;
        }
        return null;
    }

    public Patient searchByName(String name) {
        Node temp = head;
        while (temp != null) {
            if (temp.data.getName().toLowerCase().contains(name.toLowerCase()))
                return temp.data;
            temp = temp.next;
        }
        return null;
    }

    public int getSize() { return size; }
}

class DoctorLinkedList {
    private Node head;
    private int size;

    private class Node {
        Doctor data;
        Node next;
        Node(Doctor data) { this.data = data; }
    }

    public void add(Doctor d) {
        Node newNode = new Node(d);
        if (head == null) head = newNode;
        else {
            Node temp = head;
            while (temp.next != null) temp = temp.next;
            temp.next = newNode;
        }
        size++;
    }

    public Doctor get(int index) {
        if (index < 0 || index >= size) return null;
        Node temp = head;
        for (int i = 0; i < index; i++) temp = temp.next;
        return temp.data;
    }

    public boolean update(int id, Doctor newData) {
        Node temp = head;
        while (temp != null) {
            if (temp.data.getId() == id) {
                if (newData.getName() != null && !newData.getName().trim().isEmpty()) {
                    temp.data.setName(newData.getName());
                }
                if (newData.getSpecialty() != null && !newData.getSpecialty().trim().isEmpty()) {
                    temp.data.setSpecialty(newData.getSpecialty());
                }
                return true;
            }
            temp = temp.next;
        }
        return false;
    }

    public boolean delete(int id) {
        if (head == null) return false;
        if (head.data.getId() == id) {
            head = head.next;
            size--;
            return true;
        }
        Node prev = head;
        Node curr = head.next;
        while (curr != null) {
            if (curr.data.getId() == id) {
                prev.next = curr.next;
                size--;
                return true;
            }
            prev = curr;
            curr = curr.next;
        }
        return false;
    }

    public String display() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-8s %-25s %-20s\n", "ID", "Name", "Specialty"));
        sb.append("----------------------------------------------------------------\n");
        Node temp = head;
        while (temp != null) {
            sb.append(temp.data.toString()).append("\n");
            temp = temp.next;
        }
        return sb.toString();
    }

    public Doctor searchById(int id) {
        Node temp = head;
        while (temp != null) {
            if (temp.data.getId() == id) return temp.data;
            temp = temp.next;
        }
        return null;
    }

    public Doctor searchByName(String name) {
        Node temp = head;
        while (temp != null) {
            if (temp.data.getName().toLowerCase().contains(name.toLowerCase()))
                return temp.data;
            temp = temp.next;
        }
        return null;
    }

    public int getSize() { return size; }
}

class ConsultationLinkedList {
    private Node head;
    private int size;

    private class Node {
        Consultation data;
        Node next;
        Node(Consultation data) { this.data = data; }
    }

    public void add(Consultation c) {
        Node newNode = new Node(c);
        if (head == null) head = newNode;
        else {
            Node temp = head;
            while (temp.next != null) temp = temp.next;
            temp.next = newNode;
        }
        size++;
    }

    public String display() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-12s %-12s %-12s %-20s\n", "Patient", "Doctor", "Date", "Diagnosis"));
        sb.append("----------------------------------------------------------------\n");
        Node temp = head;
        while (temp != null) {
            sb.append(temp.data.toString()).append("\n");
            temp = temp.next;
        }
        return sb.toString();
    }

    public int getSize() { return size; }
}

// ===================== DARK COLOR PALETTE =====================

class DarkPalette {
    // Backgrounds
    public static final Color BG_DARK = new Color(18, 18, 24);
    public static final Color BG_MEDIUM = new Color(28, 28, 38);
    public static final Color BG_LIGHT = new Color(38, 38, 52);
    public static final Color BG_CARD = new Color(45, 45, 60);
    public static final Color BG_INPUT = new Color(55, 55, 72);
    
    // Accent Colors - Pour les bordures et textes
    public static final Color ACCENT_BLUE = new Color(64, 156, 255);
    public static final Color ACCENT_GREEN = new Color(46, 204, 113);
    public static final Color ACCENT_ORANGE = new Color(255, 159, 67);
    public static final Color ACCENT_RED = new Color(231, 76, 60);
    public static final Color ACCENT_PURPLE = new Color(155, 89, 182);
    public static final Color ACCENT_CYAN = new Color(26, 188, 188);
    
    // Text Colors
    public static final Color TEXT_PRIMARY = new Color(236, 240, 241);
    public static final Color TEXT_SECONDARY = new Color(189, 195, 199);
    public static final Color TEXT_MUTED = new Color(149, 165, 166);
    public static final Color TEXT_WHITE = new Color(255, 255, 255);
    
    // Borders
    public static final Color BORDER = new Color(60, 60, 80);
    public static final Color BORDER_LIGHT = new Color(80, 80, 100);
    
    // Selection
    public static final Color SELECTION = new Color(64, 156, 255, 50);
}

// ===================== CUSTOM COMPONENTS =====================

class BorderButton extends JButton {
    private Color borderColor;
    
    public BorderButton(String text, Color color) {
        super(text);
        this.borderColor = color;
        
        setFont(new Font("Segoe UI", Font.BOLD, 13));
        setFocusPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setBackground(new Color(0, 0, 0, 0)); // Transparent
        setForeground(color);
        setOpaque(false);
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 2),
            BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));
        setContentAreaFilled(false);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Background transparent
        if (isEnabled()) {
            g2.setColor(new Color(0, 0, 0, 0));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
        }
        
        // Text
        g2.setColor(getForeground());
        FontMetrics fm = g2.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(getText())) / 2;
        int y = (getHeight() + fm.getAscent()) / 2 - 2;
        g2.drawString(getText(), x, y);
        
        g2.dispose();
    }
    
    @Override
    public void setBorder(Border border) {
        super.setBorder(border);
    }
}

class DarkTextField extends JTextField {
    public DarkTextField(int columns) {
        super(columns);
        setFont(new Font("Segoe UI", Font.PLAIN, 13));
        setBackground(DarkPalette.BG_INPUT);
        setForeground(DarkPalette.TEXT_PRIMARY);
        setCaretColor(DarkPalette.ACCENT_BLUE);
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(DarkPalette.BORDER, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        setSelectionColor(DarkPalette.SELECTION);
    }
}

class DarkTextArea extends JTextArea {
    public DarkTextArea(int rows, int columns) {
        super(rows, columns);
        setFont(new Font("Consolas", Font.PLAIN, 12));
        setBackground(DarkPalette.BG_CARD);
        setForeground(DarkPalette.TEXT_PRIMARY);
        setCaretColor(DarkPalette.ACCENT_BLUE);
        setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        setSelectionColor(DarkPalette.SELECTION);
        setLineWrap(true);
        setWrapStyleWord(true);
    }
}

class DarkScrollPane extends JScrollPane {
    public DarkScrollPane(Component view) {
        super(view);
        setBorder(BorderFactory.createLineBorder(DarkPalette.BORDER, 1));
        getViewport().setBackground(DarkPalette.BG_CARD);
        setBackground(DarkPalette.BG_CARD);
        
        JScrollBar verticalBar = getVerticalScrollBar();
        verticalBar.setBackground(DarkPalette.BG_MEDIUM);
        verticalBar.setForeground(DarkPalette.ACCENT_BLUE);
        verticalBar.setPreferredSize(new Dimension(10, 0));
        
        JScrollBar horizontalBar = getHorizontalScrollBar();
        horizontalBar.setBackground(DarkPalette.BG_MEDIUM);
        horizontalBar.setForeground(DarkPalette.ACCENT_BLUE);
        horizontalBar.setPreferredSize(new Dimension(0, 10));
    }
}

// ===================== GUI PANELS =====================

class PatientPanel extends JPanel {
    private PatientLinkedList patientList;
    private JTextArea displayArea;
    private JTextField idField, nameField, ageField, phoneField, searchField;

    public PatientPanel(PatientLinkedList list) {
        this.patientList = list;
        setLayout(new BorderLayout(10, 10));
        setBackground(DarkPalette.BG_DARK);

        JPanel mainContainer = new JPanel(new BorderLayout(10, 10));
        mainContainer.setBackground(DarkPalette.BG_DARK);
        mainContainer.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBackground(DarkPalette.BG_DARK);

        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(DarkPalette.BG_CARD);
        inputPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(DarkPalette.BORDER, 1),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("PATIENT MANAGEMENT");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(DarkPalette.ACCENT_BLUE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        inputPanel.add(titleLabel, gbc);

        JLabel subtitleLabel = new JLabel("Register and manage patient records");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitleLabel.setForeground(DarkPalette.TEXT_MUTED);
        gbc.gridy = 1;
        inputPanel.add(subtitleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 2;
        gbc.gridx = 0;
        JLabel idLabel = new JLabel("ID:");
        idLabel.setForeground(DarkPalette.TEXT_SECONDARY);
        inputPanel.add(idLabel, gbc);
        gbc.gridx = 1;
        idField = new DarkTextField(10);
        inputPanel.add(idField, gbc);

        gbc.gridx = 2;
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setForeground(DarkPalette.TEXT_SECONDARY);
        inputPanel.add(nameLabel, gbc);
        gbc.gridx = 3;
        nameField = new DarkTextField(10);
        inputPanel.add(nameField, gbc);

        gbc.gridy = 3;
        gbc.gridx = 0;
        JLabel ageLabel = new JLabel("Age:");
        ageLabel.setForeground(DarkPalette.TEXT_SECONDARY);
        inputPanel.add(ageLabel, gbc);
        gbc.gridx = 1;
        ageField = new DarkTextField(10);
        inputPanel.add(ageField, gbc);

        gbc.gridx = 2;
        JLabel phoneLabel = new JLabel("Phone:");
        phoneLabel.setForeground(DarkPalette.TEXT_SECONDARY);
        inputPanel.add(phoneLabel, gbc);
        gbc.gridx = 3;
        phoneField = new DarkTextField(10);
        inputPanel.add(phoneField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        buttonPanel.setBackground(DarkPalette.BG_CARD);

        BorderButton addBtn = new BorderButton("Add", DarkPalette.ACCENT_GREEN);
        BorderButton loadBtn = new BorderButton("Load", DarkPalette.ACCENT_BLUE);
        BorderButton updateBtn = new BorderButton("Update", DarkPalette.ACCENT_ORANGE);
        BorderButton deleteBtn = new BorderButton("Delete", DarkPalette.ACCENT_RED);
        BorderButton viewBtn = new BorderButton("View All", DarkPalette.ACCENT_PURPLE);

        buttonPanel.add(addBtn);
        buttonPanel.add(loadBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(viewBtn);

        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 4;
        inputPanel.add(buttonPanel, gbc);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(DarkPalette.BG_CARD);
        searchPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(DarkPalette.BORDER, 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        searchLabel.setForeground(DarkPalette.TEXT_SECONDARY);
        searchPanel.add(searchLabel);
        
        searchField = new DarkTextField(20);
        searchPanel.add(searchField);
        
        BorderButton searchBtn = new BorderButton("Find", DarkPalette.ACCENT_CYAN);
        searchPanel.add(searchBtn);

        topPanel.add(inputPanel, BorderLayout.NORTH);
        topPanel.add(searchPanel, BorderLayout.SOUTH);

        displayArea = new DarkTextArea(15, 50);
        displayArea.setEditable(false);
        
        DarkScrollPane scrollPane = new DarkScrollPane(displayArea);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(DarkPalette.BORDER, 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        JPanel displayPanel = new JPanel(new BorderLayout());
        displayPanel.setBackground(DarkPalette.BG_DARK);
        
        JLabel displayTitle = new JLabel("Patient Records");
        displayTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        displayTitle.setForeground(DarkPalette.TEXT_PRIMARY);
        displayPanel.add(displayTitle, BorderLayout.NORTH);
        displayPanel.add(scrollPane, BorderLayout.CENTER);

        mainContainer.add(topPanel, BorderLayout.NORTH);
        mainContainer.add(displayPanel, BorderLayout.CENTER);

        add(mainContainer);

        addBtn.addActionListener(e -> addPatient());
        loadBtn.addActionListener(e -> loadPatient());
        updateBtn.addActionListener(e -> updatePatient());
        deleteBtn.addActionListener(e -> deletePatient());
        viewBtn.addActionListener(e -> viewAll());
        searchBtn.addActionListener(e -> searchPatient());

        viewAll();
    }

    private void addPatient() {
        if (idField.getText().trim().isEmpty() || nameField.getText().trim().isEmpty() ||
            ageField.getText().trim().isEmpty() || phoneField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!");
            return;
        }
        
        try {
            int id = Integer.parseInt(idField.getText().trim());
            String name = nameField.getText().trim();
            int age = Integer.parseInt(ageField.getText().trim());
            String phone = phoneField.getText().trim();
            
            if (patientList.searchById(id) != null) {
                JOptionPane.showMessageDialog(this, "ID already exists!");
                return;
            }
            
            patientList.add(new Patient(id, name, age, phone));
            JOptionPane.showMessageDialog(this, "Patient added successfully!");
            clearFields();
            viewAll();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid number format!");
        }
    }

    private void loadPatient() {
        if (idField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter an ID!");
            return;
        }
        
        try {
            int id = Integer.parseInt(idField.getText().trim());
            Patient patient = patientList.searchById(id);
            
            if (patient != null) {
                nameField.setText(patient.getName());
                ageField.setText(String.valueOf(patient.getAge()));
                phoneField.setText(patient.getPhone());
                JOptionPane.showMessageDialog(this, "Patient loaded!");
            } else {
                JOptionPane.showMessageDialog(this, "ID not found!");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid ID!");
        }
    }

    private void updatePatient() {
        if (idField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter an ID!");
            return;
        }
        
        try {
            int id = Integer.parseInt(idField.getText().trim());
            
            Patient existing = patientList.searchById(id);
            if (existing == null) {
                JOptionPane.showMessageDialog(this, "ID not found!");
                return;
            }
            
            String name = nameField.getText().trim();
            int age = 0;
            String phone = phoneField.getText().trim();
            
            if (!ageField.getText().trim().isEmpty()) {
                age = Integer.parseInt(ageField.getText().trim());
            }
            
            Patient updatedData = new Patient(id, name, age, phone);
            
            if (patientList.update(id, updatedData)) {
                JOptionPane.showMessageDialog(this, "Patient updated successfully!");
                clearFields();
                viewAll();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid number format!");
        }
    }

    private void deletePatient() {
        if (idField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter an ID!");
            return;
        }
        
        try {
            int id = Integer.parseInt(idField.getText().trim());
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Delete patient with ID " + id + "?", 
                "Confirm Delete", 
                JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                if (patientList.delete(id)) {
                    JOptionPane.showMessageDialog(this, "Patient deleted!");
                    clearFields();
                    viewAll();
                } else {
                    JOptionPane.showMessageDialog(this, "ID not found!");
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid ID!");
        }
    }

    private void viewAll() {
        String data = patientList.display();
        displayArea.setText(data.isEmpty() ? "No patients registered yet." : data);
    }

    private void searchPatient() {
        String query = searchField.getText().trim();
        if (query.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter search term!");
            return;
        }
        Patient result;
        try {
            int id = Integer.parseInt(query);
            result = patientList.searchById(id);
        } catch (NumberFormatException e) {
            result = patientList.searchByName(query);
        }
        if (result != null) {
            displayArea.setText("Search Result:\n" + result.toString());
        } else {
            displayArea.setText("No patient found matching: " + query);
        }
    }

    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        ageField.setText("");
        phoneField.setText("");
    }
}

class DoctorPanel extends JPanel {
    private DoctorLinkedList doctorList;
    private JTextArea displayArea;
    private JTextField idField, nameField, specialtyField, searchField;

    public DoctorPanel(DoctorLinkedList list) {
        this.doctorList = list;
        setLayout(new BorderLayout(10, 10));
        setBackground(DarkPalette.BG_DARK);

        JPanel mainContainer = new JPanel(new BorderLayout(10, 10));
        mainContainer.setBackground(DarkPalette.BG_DARK);
        mainContainer.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBackground(DarkPalette.BG_DARK);

        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(DarkPalette.BG_CARD);
        inputPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(DarkPalette.BORDER, 1),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("DOCTOR MANAGEMENT");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(DarkPalette.ACCENT_PURPLE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        inputPanel.add(titleLabel, gbc);

        JLabel subtitleLabel = new JLabel("Register and manage doctor profiles");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitleLabel.setForeground(DarkPalette.TEXT_MUTED);
        gbc.gridy = 1;
        inputPanel.add(subtitleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 2;
        gbc.gridx = 0;
        JLabel idLabel = new JLabel("ID:");
        idLabel.setForeground(DarkPalette.TEXT_SECONDARY);
        inputPanel.add(idLabel, gbc);
        gbc.gridx = 1;
        idField = new DarkTextField(10);
        inputPanel.add(idField, gbc);

        gbc.gridx = 2;
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setForeground(DarkPalette.TEXT_SECONDARY);
        inputPanel.add(nameLabel, gbc);
        gbc.gridx = 3;
        nameField = new DarkTextField(10);
        inputPanel.add(nameField, gbc);

        gbc.gridy = 3;
        gbc.gridx = 0;
        JLabel specialtyLabel = new JLabel("Specialty:");
        specialtyLabel.setForeground(DarkPalette.TEXT_SECONDARY);
        inputPanel.add(specialtyLabel, gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        specialtyField = new DarkTextField(10);
        inputPanel.add(specialtyField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        buttonPanel.setBackground(DarkPalette.BG_CARD);

        BorderButton addBtn = new BorderButton("Add", DarkPalette.ACCENT_GREEN);
        BorderButton loadBtn = new BorderButton("Load", DarkPalette.ACCENT_BLUE);
        BorderButton updateBtn = new BorderButton("Update", DarkPalette.ACCENT_ORANGE);
        BorderButton deleteBtn = new BorderButton("Delete", DarkPalette.ACCENT_RED);
        BorderButton viewBtn = new BorderButton("View All", DarkPalette.ACCENT_PURPLE);

        buttonPanel.add(addBtn);
        buttonPanel.add(loadBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(viewBtn);

        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 4;
        inputPanel.add(buttonPanel, gbc);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(DarkPalette.BG_CARD);
        searchPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(DarkPalette.BORDER, 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        searchLabel.setForeground(DarkPalette.TEXT_SECONDARY);
        searchPanel.add(searchLabel);
        
        searchField = new DarkTextField(20);
        searchPanel.add(searchField);
        
        BorderButton searchBtn = new BorderButton("Find", DarkPalette.ACCENT_CYAN);
        searchPanel.add(searchBtn);

        topPanel.add(inputPanel, BorderLayout.NORTH);
        topPanel.add(searchPanel, BorderLayout.SOUTH);

        displayArea = new DarkTextArea(15, 50);
        displayArea.setEditable(false);
        
        DarkScrollPane scrollPane = new DarkScrollPane(displayArea);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(DarkPalette.BORDER, 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        JPanel displayPanel = new JPanel(new BorderLayout());
        displayPanel.setBackground(DarkPalette.BG_DARK);
        
        JLabel displayTitle = new JLabel("Doctor Records");
        displayTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        displayTitle.setForeground(DarkPalette.TEXT_PRIMARY);
        displayPanel.add(displayTitle, BorderLayout.NORTH);
        displayPanel.add(scrollPane, BorderLayout.CENTER);

        mainContainer.add(topPanel, BorderLayout.NORTH);
        mainContainer.add(displayPanel, BorderLayout.CENTER);

        add(mainContainer);

        addBtn.addActionListener(e -> addDoctor());
        loadBtn.addActionListener(e -> loadDoctor());
        updateBtn.addActionListener(e -> updateDoctor());
        deleteBtn.addActionListener(e -> deleteDoctor());
        viewBtn.addActionListener(e -> viewAll());
        searchBtn.addActionListener(e -> searchDoctor());

        viewAll();
    }

    private void addDoctor() {
        if (idField.getText().trim().isEmpty() || nameField.getText().trim().isEmpty() ||
            specialtyField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!");
            return;
        }
        
        try {
            int id = Integer.parseInt(idField.getText().trim());
            String name = nameField.getText().trim();
            String specialty = specialtyField.getText().trim();
            
            if (doctorList.searchById(id) != null) {
                JOptionPane.showMessageDialog(this, "ID already exists!");
                return;
            }
            
            doctorList.add(new Doctor(id, name, specialty));
            JOptionPane.showMessageDialog(this, "Doctor added successfully!");
            clearFields();
            viewAll();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid ID format!");
        }
    }

    private void loadDoctor() {
        if (idField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter an ID!");
            return;
        }
        
        try {
            int id = Integer.parseInt(idField.getText().trim());
            Doctor doctor = doctorList.searchById(id);
            
            if (doctor != null) {
                nameField.setText(doctor.getName());
                specialtyField.setText(doctor.getSpecialty());
                JOptionPane.showMessageDialog(this, "Doctor loaded!");
            } else {
                JOptionPane.showMessageDialog(this, "ID not found!");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid ID!");
        }
    }

    private void updateDoctor() {
        if (idField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter an ID!");
            return;
        }
        
        try {
            int id = Integer.parseInt(idField.getText().trim());
            
            Doctor existing = doctorList.searchById(id);
            if (existing == null) {
                JOptionPane.showMessageDialog(this, "ID not found!");
                return;
            }
            
            String name = nameField.getText().trim();
            String specialty = specialtyField.getText().trim();
            
            Doctor updatedData = new Doctor(id, name, specialty);
            
            if (doctorList.update(id, updatedData)) {
                JOptionPane.showMessageDialog(this, "Doctor updated successfully!");
                clearFields();
                viewAll();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid ID format!");
        }
    }

    private void deleteDoctor() {
        if (idField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter an ID!");
            return;
        }
        
        try {
            int id = Integer.parseInt(idField.getText().trim());
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Delete doctor with ID " + id + "?", 
                "Confirm Delete", 
                JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                if (doctorList.delete(id)) {
                    JOptionPane.showMessageDialog(this, "Doctor deleted!");
                    clearFields();
                    viewAll();
                } else {
                    JOptionPane.showMessageDialog(this, "ID not found!");
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid ID!");
        }
    }

    private void viewAll() {
        String data = doctorList.display();
        displayArea.setText(data.isEmpty() ? "No doctors registered yet." : data);
    }

    private void searchDoctor() {
        String query = searchField.getText().trim();
        if (query.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter search term!");
            return;
        }
        Doctor result;
        try {
            int id = Integer.parseInt(query);
            result = doctorList.searchById(id);
        } catch (NumberFormatException e) {
            result = doctorList.searchByName(query);
        }
        if (result != null) {
            displayArea.setText("Search Result:\n" + result.toString());
        } else {
            displayArea.setText("No doctor found matching: " + query);
        }
    }

    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        specialtyField.setText("");
    }
}

class ConsultationPanel extends JPanel {
    private ConsultationLinkedList consultationList;
    private JTextArea displayArea;
    private JTextField patientIdField, doctorIdField, dateField, diagnosisField;

    public ConsultationPanel(ConsultationLinkedList list) {
        this.consultationList = list;
        setLayout(new BorderLayout(10, 10));
        setBackground(DarkPalette.BG_DARK);

        JPanel mainContainer = new JPanel(new BorderLayout(10, 10));
        mainContainer.setBackground(DarkPalette.BG_DARK);
        mainContainer.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(DarkPalette.BG_CARD);
        inputPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(DarkPalette.BORDER, 1),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("CONSULTATION MANAGEMENT");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(DarkPalette.ACCENT_CYAN);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        inputPanel.add(titleLabel, gbc);

        JLabel subtitleLabel = new JLabel("Record patient consultations");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitleLabel.setForeground(DarkPalette.TEXT_MUTED);
        gbc.gridy = 1;
        inputPanel.add(subtitleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 2;
        gbc.gridx = 0;
        JLabel pidLabel = new JLabel("Patient ID:");
        pidLabel.setForeground(DarkPalette.TEXT_SECONDARY);
        inputPanel.add(pidLabel, gbc);
        gbc.gridx = 1;
        patientIdField = new DarkTextField(10);
        inputPanel.add(patientIdField, gbc);

        gbc.gridx = 2;
        JLabel didLabel = new JLabel("Doctor ID:");
        didLabel.setForeground(DarkPalette.TEXT_SECONDARY);
        inputPanel.add(didLabel, gbc);
        gbc.gridx = 3;
        doctorIdField = new DarkTextField(10);
        inputPanel.add(doctorIdField, gbc);

        gbc.gridy = 3;
        gbc.gridx = 0;
        JLabel dateLabel = new JLabel("Date (YYYY-MM-DD):");
        dateLabel.setForeground(DarkPalette.TEXT_SECONDARY);
        inputPanel.add(dateLabel, gbc);
        gbc.gridx = 1;
        dateField = new DarkTextField(10);
        inputPanel.add(dateField, gbc);

        gbc.gridx = 2;
        JLabel diagLabel = new JLabel("Diagnosis:");
        diagLabel.setForeground(DarkPalette.TEXT_SECONDARY);
        inputPanel.add(diagLabel, gbc);
        gbc.gridx = 3;
        diagnosisField = new DarkTextField(10);
        inputPanel.add(diagnosisField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        buttonPanel.setBackground(DarkPalette.BG_CARD);

        BorderButton addBtn = new BorderButton("Record", DarkPalette.ACCENT_GREEN);
        BorderButton viewBtn = new BorderButton("View All", DarkPalette.ACCENT_PURPLE);

        buttonPanel.add(addBtn);
        buttonPanel.add(viewBtn);

        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 4;
        inputPanel.add(buttonPanel, gbc);

        displayArea = new DarkTextArea(15, 50);
        displayArea.setEditable(false);
        
        DarkScrollPane scrollPane = new DarkScrollPane(displayArea);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(DarkPalette.BORDER, 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        JPanel displayPanel = new JPanel(new BorderLayout());
        displayPanel.setBackground(DarkPalette.BG_DARK);
        
        JLabel displayTitle = new JLabel("Consultation Records");
        displayTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        displayTitle.setForeground(DarkPalette.TEXT_PRIMARY);
        displayPanel.add(displayTitle, BorderLayout.NORTH);
        displayPanel.add(scrollPane, BorderLayout.CENTER);

        mainContainer.add(inputPanel, BorderLayout.NORTH);
        mainContainer.add(displayPanel, BorderLayout.CENTER);

        add(mainContainer);

        addBtn.addActionListener(e -> addConsultation());
        viewBtn.addActionListener(e -> viewAll());

        viewAll();
    }

    private void addConsultation() {
        if (patientIdField.getText().trim().isEmpty() || doctorIdField.getText().trim().isEmpty() ||
            dateField.getText().trim().isEmpty() || diagnosisField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!");
            return;
        }
        
        try {
            int pid = Integer.parseInt(patientIdField.getText().trim());
            int did = Integer.parseInt(doctorIdField.getText().trim());
            String date = dateField.getText().trim();
            String diagnosis = diagnosisField.getText().trim();
            
            consultationList.add(new Consultation(pid, did, date, diagnosis));
            JOptionPane.showMessageDialog(this, "Consultation recorded!");
            clearFields();
            viewAll();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid ID format!");
        }
    }

    private void viewAll() {
        String data = consultationList.display();
        displayArea.setText(data.isEmpty() ? "No consultations recorded yet." : data);
    }

    private void clearFields() {
        patientIdField.setText("");
        doctorIdField.setText("");
        dateField.setText("");
        diagnosisField.setText("");
    }
}

// ===================== MAIN FRAME =====================

public class MediClinicApp extends JFrame {
    private PatientLinkedList patientList = new PatientLinkedList();
    private DoctorLinkedList doctorList = new DoctorLinkedList();
    private ConsultationLinkedList consultationList = new ConsultationLinkedList();

    public MediClinicApp() {
        setTitle("MediClinic - Outpatient Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 750);
        setLocationRelativeTo(null);
        setBackground(DarkPalette.BG_DARK);

        loadSampleData();

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(DarkPalette.BG_DARK);

        JPanel headerPanel = createHeader();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabbedPane.setBackground(DarkPalette.BG_MEDIUM);
        tabbedPane.setForeground(DarkPalette.TEXT_PRIMARY);
        tabbedPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        tabbedPane.setUI(new BasicTabbedPaneUI() {
            @Override
            protected void installDefaults() {
                super.installDefaults();
                tabAreaInsets = new Insets(5, 5, 5, 5);
                contentBorderInsets = new Insets(0, 0, 0, 0);
            }
            
            @Override
            protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (isSelected) {
                    g2.setColor(DarkPalette.ACCENT_BLUE);
                    g2.fillRoundRect(x, y, w, h, 8, 8);
                } else {
                    g2.setColor(DarkPalette.BG_MEDIUM);
                    g2.fillRoundRect(x, y, w, h, 8, 8);
                }
                g2.dispose();
            }
            
            @Override
            protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
                // No border
            }
            
            @Override
            protected void paintFocusIndicator(Graphics g, int tabPlacement, Rectangle[] rects, int tabIndex, Rectangle iconRect, Rectangle textRect, boolean isSelected) {
                // No focus indicator
            }
        });

        tabbedPane.addTab("Patients", new PatientPanel(patientList));
        tabbedPane.addTab("Doctors", new DoctorPanel(doctorList));
        tabbedPane.addTab("Consultations", new ConsultationPanel(consultationList));

        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        JPanel footerPanel = createFooter();
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(28, 28, 38));
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, DarkPalette.ACCENT_BLUE),
            BorderFactory.createEmptyBorder(15, 25, 15, 25)
        ));

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("MediClinic");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(DarkPalette.TEXT_WHITE);
        leftPanel.add(titleLabel);

        JLabel subtitleLabel = new JLabel("Outpatient Management System");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(DarkPalette.TEXT_MUTED);
        leftPanel.add(subtitleLabel);

        header.add(leftPanel, BorderLayout.WEST);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setOpaque(false);

        JLabel versionLabel = new JLabel("Version 2.0");
        versionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        versionLabel.setForeground(DarkPalette.TEXT_MUTED);
        rightPanel.add(versionLabel);

        header.add(rightPanel, BorderLayout.EAST);

        return header;
    }

    private JPanel createFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(new Color(18, 18, 24));
        footer.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, DarkPalette.BORDER),
            BorderFactory.createEmptyBorder(10, 25, 10, 25)
        ));

        JLabel statusLabel = new JLabel(" 2026 MediClinic Inc. All rights reserved.");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        statusLabel.setForeground(DarkPalette.TEXT_MUTED);
        footer.add(statusLabel, BorderLayout.WEST);

        JLabel recordsLabel = new JLabel(
            "Patients: " + patientList.getSize() + 
            " | Doctors: " + doctorList.getSize() + 
            " | Consultations: " + consultationList.getSize()
        );
        recordsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        recordsLabel.setForeground(DarkPalette.TEXT_MUTED);
        footer.add(recordsLabel, BorderLayout.EAST);

        return footer;
    }

    private void loadSampleData() {
        patientList.add(new Patient(101, "John Smith", 45, "+1-234-567-8901"));
        patientList.add(new Patient(102, "Emma Johnson", 32, "+1-234-567-8902"));
        patientList.add(new Patient(103, "Michael Brown", 58, "+1-234-567-8903"));
        patientList.add(new Patient(104, "Sarah Wilson", 27, "+1-234-567-8904"));
        patientList.add(new Patient(105, "David Lee", 34, "+1-234-567-8905"));
        patientList.add(new Patient(106, "Lisa Taylor", 41, "+1-234-567-8906"));
        patientList.add(new Patient(107, "James Anderson", 29, "+1-234-567-8907"));
        patientList.add(new Patient(108, "Maria Garcia", 36, "+1-234-567-8908"));

        doctorList.add(new Doctor(201, "Dr. Robert Moreau", "Cardiology"));
        doctorList.add(new Doctor(202, "Dr. Anne Lefevre", "Dermatology"));
        doctorList.add(new Doctor(203, "Dr. Paul Rousseau", "Pediatrics"));
        doctorList.add(new Doctor(204, "Dr. Claire Dubois", "Orthopedics"));
        doctorList.add(new Doctor(205, "Dr. Marc Martin", "Neurology"));
        doctorList.add(new Doctor(206, "Dr. Sophie Lambert", "Gynecology"));
        doctorList.add(new Doctor(207, "Dr. Thomas Bernard", "Ophthalmology"));
        doctorList.add(new Doctor(208, "Dr. Julie Petit", "Psychiatry"));

        consultationList.add(new Consultation(101, 201, "2026-06-15", "Hypertension"));
        consultationList.add(new Consultation(102, 202, "2026-06-16", "Eczema"));
        consultationList.add(new Consultation(103, 203, "2026-06-17", "Common Cold"));
        consultationList.add(new Consultation(104, 204, "2026-06-18", "Sprained Ankle"));
        consultationList.add(new Consultation(105, 205, "2026-06-19", "Migraine"));
        consultationList.add(new Consultation(106, 206, "2026-06-20", "Pregnancy Check"));
        consultationList.add(new Consultation(107, 207, "2026-06-21", "Vision Test"));
        consultationList.add(new Consultation(108, 208, "2026-06-22", "Anxiety"));
        consultationList.add(new Consultation(101, 205, "2026-06-23", "Neurological Follow-up"));
        consultationList.add(new Consultation(103, 201, "2026-06-24", "Cardiac Check"));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new MediClinicApp().setVisible(true);
        });
    }
}