import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * To-Do List Application using Java Swing
 * A simple GUI application to manage daily tasks
 */
public class TodoApp extends JFrame {
    // Components
    private DefaultListModel<String> todoListModel;
    private JList<String> todoList;
    private JTextField taskInput;
    private JButton addButton;
    private JButton deleteButton;
    private JButton clearButton;
    private JButton markCompleteButton;
    private JLabel statusLabel;
    
    // Colors for modern UI
    private final Color PRIMARY_COLOR = new Color(70, 130, 180);
    private final Color SECONDARY_COLOR = new Color(240, 248, 255);
    private final Color ACCENT_COLOR = new Color(255, 99, 71);
    
    public TodoApp() {
        initializeUI();
        setupComponents();
        setupEventListeners();
        setupLayout();
    }
    
    private void initializeUI() {
        setTitle("📝 Java To-Do List Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 600);
        setLocationRelativeTo(null); // Center the window
        setResizable(true);
        
        // Set application icon
        ImageIcon icon = new ImageIcon("todo_icon.png"); // You can add an icon file
        if (icon.getImageLoadStatus() == MediaTracker.COMPLETE) {
            setIconImage(icon.getImage());
        }
        
        // Set modern look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeel());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void setupComponents() {
        // Initialize list model and JList
        todoListModel = new DefaultListModel<>();
        todoList = new JList<>(todoListModel);
        todoList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        todoList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        todoList.setBackground(Color.WHITE);
        todoList.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
        // Task input field
        taskInput = new JTextField();
        taskInput.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        taskInput.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        taskInput.setToolTipText("Enter your task here...");
        
        // Add placeholder text
        taskInput.setText("Enter your task here...");
        taskInput.setForeground(Color.GRAY);
        taskInput.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (taskInput.getText().equals("Enter your task here...")) {
                    taskInput.setText("");
                    taskInput.setForeground(Color.BLACK);
                }
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                if (taskInput.getText().isEmpty()) {
                    taskInput.setText("Enter your task here...");
                    taskInput.setForeground(Color.GRAY);
                }
            }
        });
        
        // Buttons with modern styling
        addButton = createStyledButton("➕ Add Task", PRIMARY_COLOR);
        deleteButton = createStyledButton("🗑️ Delete Task", ACCENT_COLOR);
        clearButton = createStyledButton("🧹 Clear All", new Color(169, 169, 169));
        markCompleteButton = createStyledButton("✅ Mark Complete", new Color(34, 139, 34));
        
        // Status label
        statusLabel = new JLabel("Ready to add tasks! Total: 0");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setForeground(Color.DARK_GRAY);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    }
    
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(bgColor.darker(), 1),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.brighter());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    private void setupEventListeners() {
        // Add task button action
        addButton.addActionListener(e -> addTask());
        
        // Add task on Enter key press
        taskInput.addActionListener(e -> addTask());
        
        // Delete task button action
        deleteButton.addActionListener(e -> deleteTask());
        
        // Clear all button action
        clearButton.addActionListener(e -> clearAllTasks());
        
        // Mark complete button action
        markCompleteButton.addActionListener(e -> markTaskComplete());
        
        // Double-click to mark complete
        todoList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    markTaskComplete();
                }
            }
        });
        
        // Delete task with Delete key
        todoList.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                    deleteTask();
                }
            }
        });
    }
    
    private void setupLayout() {
        // Main container with modern background
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(SECONDARY_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout(10, 10));
        headerPanel.setBackground(SECONDARY_COLOR);
        
        JLabel titleLabel = new JLabel("📝 My To-Do List");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(taskInput, BorderLayout.CENTER);
        headerPanel.add(addButton, BorderLayout.EAST);
        
        // Task list panel with scroll pane
        JScrollPane scrollPane = new JScrollPane(todoList);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            "Your Tasks",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12),
            PRIMARY_COLOR
        ));
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(SECONDARY_COLOR);
        buttonPanel.add(markCompleteButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);
        
        // Assemble main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Status bar
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBackground(Color.WHITE);
        statusPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        statusPanel.add(statusLabel, BorderLayout.WEST);
        
        // Add everything to frame
        setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);
        add(statusPanel, BorderLayout.SOUTH);
    }
    
    private void addTask() {
        String task = taskInput.getText().trim();
        
        // Validate input
        if (task.isEmpty() || task.equals("Enter your task here...")) {
            showMessage("Please enter a task!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Add task to list
        todoListModel.addElement("• " + task);
        taskInput.setText("");
        taskInput.requestFocus();
        
        updateStatus();
        showMessage("Task added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void deleteTask() {
        int selectedIndex = todoList.getSelectedIndex();
        
        if (selectedIndex == -1) {
            showMessage("Please select a task to delete!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String task = todoListModel.get(selectedIndex);
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete this task?\n" + task,
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            todoListModel.remove(selectedIndex);
            updateStatus();
            showMessage("Task deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void markTaskComplete() {
        int selectedIndex = todoList.getSelectedIndex();
        
        if (selectedIndex == -1) {
            showMessage("Please select a task to mark as complete!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String task = todoListModel.get(selectedIndex);
        
        // Check if already completed
        if (task.contains("✅")) {
            showMessage("This task is already completed!", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // Mark as complete
        String completedTask = task.replace("•", "✅");
        todoListModel.set(selectedIndex, completedTask);
        
        updateStatus();
        showMessage("Task marked as complete! Great job! 🎉", "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void clearAllTasks() {
        if (todoListModel.isEmpty()) {
            showMessage("The task list is already empty!", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to clear ALL tasks?",
            "Confirm Clear All",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            todoListModel.clear();
            updateStatus();
            showMessage("All tasks cleared!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void updateStatus() {
        int totalTasks = todoListModel.getSize();
        int completedTasks = 0;
        
        for (int i = 0; i < totalTasks; i++) {
            if (todoListModel.get(i).contains("✅")) {
                completedTasks++;
            }
        }
        
        statusLabel.setText(String.format("Total: %d | Completed: %d | Pending: %d", 
            totalTasks, completedTasks, totalTasks - completedTasks));
    }
    
    private void showMessage(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }
    
    // Main method
    public static void main(String[] args) {
        // Set modern look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeel());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Create and show GUI on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            new TodoApp().setVisible(true);
        });
    }
}
