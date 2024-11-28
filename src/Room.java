import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;


public class Room extends JFrame {

    private JTextField roomNoField;
    private JComboBox<String> roomTypeCombo;
    private JComboBox<String> bedTypeCombo;
    private JTextField amountField;
    private JTable roomTable;
    private DefaultTableModel tableModel;


    public Room(String UserRole) {
        // Frame properties
        setTitle("Manage Room");
        setSize(800, 500); // Increased the height to accommodate the header
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        // Header Label
        JLabel headerLabel = new JLabel("Manage Room");
        headerLabel.setBounds(50, 10, 700, 40);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24)); // Font size and style
        add(headerLabel);

        // Room No Label and TextField
        JLabel roomNoLabel = new JLabel("Room No:");
        roomNoLabel.setBounds(50, 70, 100, 30);
        add(roomNoLabel);

        roomNoField = new JTextField();
        roomNoField.setBounds(150, 70, 150, 30);
        add(roomNoField);

        // Room Type Label and ComboBox
        JLabel roomTypeLabel = new JLabel("Room Type:");
        roomTypeLabel.setBounds(50, 120, 100, 30);
        add(roomTypeLabel);

        roomTypeCombo = new JComboBox<>(new String[]{"AC Room", "Non AC Room"});
        roomTypeCombo.setBounds(150, 120, 150, 30);
        add(roomTypeCombo);

        // Bed Type Label and ComboBox
        JLabel bedTypeLabel = new JLabel("Bed Type:");
        bedTypeLabel.setBounds(50, 170, 100, 30);
        add(bedTypeLabel);

        bedTypeCombo = new JComboBox<>(new String[]{"Single Bed", "Double Bed"});
        bedTypeCombo.setBounds(150, 170, 150, 30);
        add(bedTypeCombo);

        // Amount Label and TextField
        JLabel amountLabel = new JLabel("Amount:");
        amountLabel.setBounds(50, 220, 100, 30);
        add(amountLabel);

        amountField = new JTextField();
        amountField.setBounds(150, 220, 150, 30);
        add(amountField);

        // Save Button
        JButton saveButton = new JButton("Save");
        saveButton.setBounds(50, 300, 80, 30);
        add(saveButton);

        // Update Button
        JButton editButton = new JButton("Update");
        editButton.setBounds(140, 300, 80, 30);
        add(editButton);

        // Delete Button
        JButton deleteButton = new JButton("Delete");
        deleteButton.setBounds(230, 300, 80, 30);
        add(deleteButton);

        // Clear Button
        JButton clearButton = new JButton("Clear");
        clearButton.setBounds(320, 300, 80, 30);
        add(clearButton);

        // Back to Home Page Button
        JButton homeButton = new JButton("Back to Home");
        homeButton.setBounds(140, 340, 170, 30);  // Adjusted Y-coordinate
        add(homeButton);

        // Table for displaying room details
        tableModel = new DefaultTableModel(new String[]{"ID","Room No", "Room Type", "Bed Type", "Amount"}, 0);
        roomTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(roomTable);
        tableScrollPane.setBounds(350, 70, 400, 200); // Adjusted Y-coordinate
        add(tableScrollPane);
        loadRooms();

        roomTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int selectedRow = roomTable.getSelectedRow();

                if (selectedRow >= 0) {
                    // Set the fields based on the selected row's data
                    roomNoField.setText(tableModel.getValueAt(selectedRow, 1).toString());
                    roomTypeCombo.setSelectedItem(tableModel.getValueAt(selectedRow, 2).toString());
                    bedTypeCombo.setSelectedItem(tableModel.getValueAt(selectedRow, 3).toString());
                    amountField.setText(tableModel.getValueAt(selectedRow, 4).toString());
                }
            }
        });

        // Action listeners
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String roomNo = roomNoField.getText();
                String roomType = roomTypeCombo.getSelectedItem().toString();
                String bedType = bedTypeCombo.getSelectedItem().toString();
                String amount = amountField.getText();

                if (!roomNo.isEmpty() && !amount.isEmpty()) {
                    try (Connection connection = DatabaseUtil.getConnection()) {
                        String sql = "INSERT INTO hotel_managementdb.room (room_no, room_type, bed_type, amount) VALUES (?, ?, ?, ?)";
                        try (PreparedStatement statement = connection.prepareStatement(sql)) {
                            statement.setString(1, roomNo);
                            statement.setString(2, roomType);
                            statement.setString(3, bedType);
                            statement.setDouble(4, Double.parseDouble(amount));

                            int rowsInserted = statement.executeUpdate();
                            if (rowsInserted > 0) {
                                JOptionPane.showMessageDialog(null, "Room saved successfully!");
                                loadRooms(); // Reload the table to show the newly added room
                                clearFields();
                            }
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "An error occurred while saving the room.");
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Please enter a valid amount.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please fill all fields.");
                }
            }
        });
        //Update section
        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = roomTable.getSelectedRow();
                if (selectedRow != -1) {
                    try {
                        // Retrieve values from fields
                        String roomNo = roomNoField.getText().trim();
                        String roomType = (String) roomTypeCombo.getSelectedItem();
                        String bedType = (String) bedTypeCombo.getSelectedItem();
                        String amountStr = amountField.getText().trim();

                        // Validate fields
                        if (roomNo.isEmpty() || amountStr.isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Please fill in all fields.");
                        } else {
                            double amount = Double.parseDouble(amountStr);

                            // Get the ID from the selected row
                            int roomId = (int) tableModel.getValueAt(selectedRow, 0);

                            // SQL update query
                            String sql = "UPDATE hotel_managementdb.room SET room_no=?, room_type=?, bed_type=?, amount=? WHERE ID=?";

                            try (Connection connection = DatabaseUtil.getConnection();
                                 PreparedStatement statement = connection.prepareStatement(sql)) {

                                statement.setString(1, roomNo);
                                statement.setString(2, roomType);
                                statement.setString(3, bedType);
                                statement.setDouble(4, amount);
                                statement.setInt(5, roomId);

                                // Execute the update
                                statement.executeUpdate();

                                // Update the table
                                loadRooms();

                                JOptionPane.showMessageDialog(null, "Room updated successfully.");
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                                JOptionPane.showMessageDialog(null, "Failed to update room: " + ex.getMessage());
                            }
                        }
                    } catch (NumberFormatException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Invalid amount format. Please enter a valid number.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please select a room to edit.");
                }
            }
        });


        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = roomTable.getSelectedRow();
                if (selectedRow >= 0) {
                    // Get the room ID from the selected row
                    int roomId = (int) tableModel.getValueAt(selectedRow, 0);

                    int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this room?", "Delete Confirmation", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        try (Connection connection = DatabaseUtil.getConnection()) {
                            String sql = "DELETE FROM hotel_managementdb.room WHERE id = ?";
                            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                                statement.setInt(1, roomId);

                                int rowsDeleted = statement.executeUpdate();
                                if (rowsDeleted > 0) {
                                    JOptionPane.showMessageDialog(null, "Room deleted successfully!");
                                    tableModel.removeRow(selectedRow); // Remove the row from the table
                                }
                            }
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(null, "An error occurred while deleting the room.");
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please select a row to delete.");
                }
            }
        });


        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clearFields();
            }
        });

        homeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Home("Admin");
                dispose(); // Close the current window
            }
        });

        setVisible(true);
    }

    private void clearFields() {
        roomNoField.setText("");
        roomTypeCombo.setSelectedIndex(0);
        bedTypeCombo.setSelectedIndex(0);
        amountField.setText("");
    }

    private void loadRooms() {
        tableModel.setRowCount(0);

        String sql = "SELECT * FROM hotel_managementdb.room";

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String roomNo = resultSet.getString("room_no");
                String roomType = resultSet.getString("room_type");
                String bedType = resultSet.getString("bed_type");
                double amount = resultSet.getDouble("amount");

                tableModel.addRow(new Object[]{id, roomNo, roomType, bedType, amount});
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "An error occurred while loading rooms.");
        }
    }

    public static void main(String[] args) {

        //new Room();
    }
}
