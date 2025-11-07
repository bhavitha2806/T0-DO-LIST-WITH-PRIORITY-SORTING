package todolistproject;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ToDoApp extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private JTextField titleField, dateField;
    private JTextArea descArea;
    private JComboBox<String> priorityBox;

    Color bgColor = new Color(240, 248, 255);
    Color headerColor = new Color(70, 130, 180);
    Color buttonColor = new Color(65, 105, 225);
    Color panelColor = new Color(230, 240, 250);

    public ToDoApp() {
        setTitle("✨ To-Do List with Priority Sorting ✨");
        setSize(850, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(bgColor);

        JLabel titleLabel = new JLabel("📋 To-Do List Manager", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(Color.WHITE);

        JPanel header = new JPanel();
        header.setBackground(headerColor);
        header.add(titleLabel);
        add(header, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{"ID", "Title", "Description", "Due Date", "Priority"}, 0);
        table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(28);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        table.getTableHeader().setBackground(new Color(100, 149, 237));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(173, 216, 230));
        loadTasks();

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(headerColor, 2));
        add(scrollPane, BorderLayout.CENTER);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(5, 2, 12, 12));
        formPanel.setBackground(panelColor);
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel lblTitle = new JLabel("Task Title:");
        JLabel lblDesc = new JLabel("Description:");
        JLabel lblDate = new JLabel("Due Date (YYYY-MM-DD):");
        JLabel lblPriority = new JLabel("Priority:");

        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblDesc.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblDate.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblPriority.setFont(new Font("Segoe UI", Font.BOLD, 14));

        titleField = new JTextField();
        descArea = new JTextArea(3, 20);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        dateField = new JTextField();
        priorityBox = new JComboBox<>(new String[]{"High", "Medium", "Low"});
        priorityBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        formPanel.add(lblTitle); formPanel.add(titleField);
        formPanel.add(lblDesc); formPanel.add(new JScrollPane(descArea));
        formPanel.add(lblDate); formPanel.add(dateField);
        formPanel.add(lblPriority); formPanel.add(priorityBox);
        add(formPanel, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(bgColor);

        JButton addBtn = createStyledButton("➕ Add Task");
        JButton delBtn = createStyledButton("🗑 Delete Task");
        JButton refreshBtn = createStyledButton("🔄 Refresh List");

        addBtn.addActionListener(e -> addTask());
        delBtn.addActionListener(e -> deleteTask());
        refreshBtn.addActionListener(e -> loadTasks());

        buttonPanel.add(addBtn);
        buttonPanel.add(delBtn);
        buttonPanel.add(refreshBtn);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setBackground(buttonColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(30, 144, 255));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(buttonColor);
            }
        });
        return btn;
    }

    private void loadTasks() {
        model.setRowCount(0);
        List<Task> tasks = TaskDAO.getAllTasks();
        for (Task t : tasks) {
            model.addRow(new Object[]{t.getId(), t.getTitle(), t.getDescription(), t.getDueDate(), t.getPriority()});
        }
    }

    private void addTask() {
        String title = titleField.getText().trim();
        String desc = descArea.getText().trim();
        String date = dateField.getText().trim();
        String priority = (String) priorityBox.getSelectedItem();

        if (title.isEmpty() || date.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all required fields!", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        TaskDAO.addTask(title, desc, date, priority);
        loadTasks();
        clearFields();
        JOptionPane.showMessageDialog(this, "Task added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void clearFields() {
        titleField.setText("");
        descArea.setText("");
        dateField.setText("");
        priorityBox.setSelectedIndex(0);
    }

    private void deleteTask() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a task to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this task?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            int id = (int) model.getValueAt(selectedRow, 0);
            TaskDAO.deleteTask(id);
            loadTasks();
            JOptionPane.showMessageDialog(this, "Task deleted successfully!", "Deleted", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ToDoApp().setVisible(true));
    }
}
