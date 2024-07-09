import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class UserForm extends JFrame {
    private JTextField nameField;
    private JTextField emailField;
    private JButton saveButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JTable userTable;
    private DefaultTableModel tableModel;
    private UserDAO userDAO;
    private int userId = -1;

    public UserForm() {
        userDAO = new UserDAO();
        initComponents();
        loadUsers();
    }

    private void initComponents() {
        // Set up frame
        setTitle("User Form");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setLayout(null);

        // Name label and field
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(50, 50, 100, 25);
        add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(150, 50, 200, 25);
        add(nameField);

        // Email label and field
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(50, 100, 100, 25);
        add(emailLabel);

        emailField = new JTextField();
        emailField.setBounds(150, 100, 200, 25);
        add(emailField);

        // Save button
        saveButton = new JButton("Save");
        saveButton.setBounds(50, 150, 100, 25);
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveUser();
            }
        });
        add(saveButton);

        // Update button
        updateButton = new JButton("Update");
        updateButton.setBounds(150, 150, 100, 25);
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateUser();
            }
        });
        add(updateButton);

        // Delete button
        deleteButton = new JButton("Delete");
        deleteButton.setBounds(250, 150, 100, 25);
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteUser();
            }
        });
        add(deleteButton);

        // User table
        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Email"}, 0);
        userTable = new JTable(tableModel);
        userTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = userTable.getSelectedRow();
                if (selectedRow != -1) {
                    userId = (int) tableModel.getValueAt(selectedRow, 0);
                    String name = (String) tableModel.getValueAt(selectedRow, 1);
                    String email = (String) tableModel.getValueAt(selectedRow, 2);
                    setUser(userId, name, email);
                }
            }
        });
        JScrollPane scrollPane = new JScrollPane(userTable);
        scrollPane.setBounds(50, 200, 500, 150);
        add(scrollPane);

        setVisible(true);
    }

    private void saveUser() {
        String name = nameField.getText();
        String email = emailField.getText();
        if (!name.isEmpty() && !email.isEmpty()) {
            userDAO.addUser(name, email);
            JOptionPane.showMessageDialog(this, "User saved successfully!");
            clearFields();
            loadUsers();
        } else {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
        }
    }

    private void updateUser() {
        String name = nameField.getText();
        String email = emailField.getText();
        if (userId != -1 && !name.isEmpty() && !email.isEmpty()) {
            userDAO.updateUser(userId, name, email);
            JOptionPane.showMessageDialog(this, "User updated successfully!");
            clearFields();
            loadUsers();
            userId = -1;
        } else {
            JOptionPane.showMessageDialog(this, "Please fill all fields and select a user to update.");
        }
    }

    private void deleteUser() {
        if (userId != -1) {
            userDAO.deleteUser(userId);
            JOptionPane.showMessageDialog(this, "User deleted successfully!");
            clearFields();
            loadUsers();
            userId = -1;
        } else {
            JOptionPane.showMessageDialog(this, "Please select a user to delete.");
        }
    }

    private void setUser(int id, String name, String email) {
        userId = id;
        nameField.setText(name);
        emailField.setText(email);
        saveButton.setEnabled(false);
        updateButton.setEnabled(true);
        deleteButton.setEnabled(true);
    }

    private void clearFields() {
        nameField.setText("");
        emailField.setText("");
        saveButton.setEnabled(true);
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);
    }

    private void loadUsers() {
        List<User> users = userDAO.getAllUsers();
        tableModel.setRowCount(0); // Clear existing data
        for (User user : users) {
            tableModel.addRow(new Object[]{user.getId(), user.getName(), user.getEmail()});
        }
    }

    public static void main(String[] args) {
        new UserForm();
    }
}