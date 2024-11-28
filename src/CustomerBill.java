import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerBill extends JFrame {

    private JTextField searchField;
    private JButton searchButton;
    private JButton backButton;
    private JTable customerTable;
    private DefaultTableModel tableModel;

    public CustomerBill(String UserRole) {
        // Frame setup
        setTitle("Customer Report");
        setSize(900, 500);  // Increased height to accommodate the title
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        // Title label for "Customer Details Bill"
        JLabel titleLabel = new JLabel("Customer Report", JLabel.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));  // Set font size and style
        titleLabel.setBounds(0, 10, 900, 30);  // Position it at the top center
        add(titleLabel);

        // Search label
        JLabel searchLabel = new JLabel("Search by NIC Number:");
        searchLabel.setBounds(30, 60, 300, 25);
        add(searchLabel);

        // Search text field
        searchField = new JTextField();
        searchField.setBounds(170, 60, 200, 25);
        add(searchField);

        // Search button
        searchButton = new JButton("Search");
        searchButton.setBounds(370, 60, 100, 25);
        add(searchButton);

        // Back to Home button
        backButton = new JButton("Back to Home");
        backButton.setBounds(700, 60, 150, 25);
        add(backButton);

        tableModel = new DefaultTableModel(new String[]{
                "Name", "Mob Number", "Gender", "Email", "Address", "Date In", "Date Out", "Room Type", "Bed Type"
        }, 0);
        customerTable = new JTable(tableModel);

        // Scroll pane for table
        JScrollPane scrollPane = new JScrollPane(customerTable);
        scrollPane.setBounds(50, 100, 800, 300);  // Adjusted size to fit new columns
        add(scrollPane);

        // Action Listener for search button
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchID = searchField.getText().trim();
                searchCustomerByID(searchID);
            }
        });

        // Action Listener for back button
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Close the current window and open the home screen
                dispose(); // Close the current window
                if ("Admin".equals(UserRole))
                    new Home("Admin").setVisible(true);
                else
                    new Home_Customer("Customer").setVisible(true);// Show the home screen
            }
        });
    }

    private void searchCustomerByID(String nicNumber) {
        // Clear the table before showing search results
        tableModel.setRowCount(0);

        // Use the correct column name here
        String query = "SELECT * FROM hotel_managementdb.reservation WHERE NICNumber = ?";

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, nicNumber);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String name = resultSet.getString("Name");
                String mobNumber = resultSet.getString("MobileNumber");
                String gender = resultSet.getString("Gender");
                String email = resultSet.getString("Email");
                String address = resultSet.getString("Address");
                String checkIn = resultSet.getString("DateIn");
                String checkOut = resultSet.getString("DateOut");
                String roomType = resultSet.getString("RoomType");
                String bedType = resultSet.getString("BedType");

                // Add the retrieved row to the table
                tableModel.addRow(new Object[]{name, mobNumber, gender, email, address, checkIn, checkOut, roomType, bedType});
            } else {
                JOptionPane.showMessageDialog(this, "No customer found with NIC number: " + nicNumber);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error retrieving data from the database", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
//            CustomerBill frame = new CustomerBill();
//            frame.setVisible(true);
        });
    }
}
