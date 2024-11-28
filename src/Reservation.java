import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.awt.Font;

public class Reservation extends JFrame {

    private JFrame frame;
    private JTextField NameField, MobileNumberField, NationalityField, EmailField, NICNumberField, AddressField;
    private JTextField dateInField, dateOutField;
    private JComboBox<String> GenderField, RoomTypeField, BedTypeField;
    private JButton saveButton, editButton, deleteButton, clearButton, homeButton;
    private JTable reservationTable;
    private DefaultTableModel tableModel;

    // Date format for input and display
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public Reservation(String UserRole) {
        frame = new JFrame("Customer Reservation");
        frame.setSize(1250, 650); // Adjusted size to accommodate new fields
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        // Header Label
        JLabel headerLabel = new JLabel("Customer Reservation");
        headerLabel.setBounds(50, 10, 400, 40);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24)); // Font size and style
        frame.add(headerLabel);

        // Name
        JLabel NameLabel = new JLabel("Name:");
        NameLabel.setBounds(50, 100, 100, 25);
        frame.add(NameLabel);

        NameField = new JTextField();
        NameField.setBounds(150, 100, 200, 25);
        frame.add(NameField);

        // Mobile Number
        JLabel MobileNumberLabel = new JLabel("Mobile Number:");
        MobileNumberLabel.setBounds(50, 140, 100, 25);
        frame.add(MobileNumberLabel);

        MobileNumberField = new JTextField();
        MobileNumberField.setBounds(150, 140, 200, 25);
        frame.add(MobileNumberField);

        // Nationality
        JLabel NationalityLabel = new JLabel("Nationality:");
        NationalityLabel.setBounds(50, 180, 100, 25);
        frame.add(NationalityLabel);

        NationalityField = new JTextField();
        NationalityField.setBounds(150, 180, 200, 25);
        frame.add(NationalityField);

        // Gender
        JLabel GenderLabel = new JLabel("Gender:");
        GenderLabel.setBounds(50, 220, 100, 25);
        frame.add(GenderLabel);

        String[] genders = {"Male", "Female", "Other"};
        GenderField = new JComboBox<>(genders);
        GenderField.setBounds(150, 220, 200, 25);
        frame.add(GenderField);

        // Room Type
        JLabel RoomTypeLabel = new JLabel("Room Type:");
        RoomTypeLabel.setBounds(50, 260, 100, 25);
        frame.add(RoomTypeLabel);

        String[] roomTypes = {"AC Room", "Non AC Room"};
        RoomTypeField = new JComboBox<>(roomTypes);
        RoomTypeField.setBounds(150, 260, 200, 25);
        frame.add(RoomTypeField);

        // Bed Type
        JLabel BedTypeLabel = new JLabel("Bed Type:");
        BedTypeLabel.setBounds(50, 300, 100, 25);
        frame.add(BedTypeLabel);

        String[] bedTypes = {"Single", "Double"};
        BedTypeField = new JComboBox<>(bedTypes);
        BedTypeField.setBounds(150, 300, 200, 25);
        frame.add(BedTypeField);

        // Email
        JLabel EmailLabel = new JLabel("Email:");
        EmailLabel.setBounds(50, 340, 100, 25);
        frame.add(EmailLabel);

        EmailField = new JTextField();
        EmailField.setBounds(150, 340, 200, 25);
        frame.add(EmailField);

        // NIC Number
        JLabel NICNumberLabel = new JLabel("NIC Number:");
        NICNumberLabel.setBounds(50, 380, 100, 25);
        frame.add(NICNumberLabel);

        NICNumberField = new JTextField();
        NICNumberField.setBounds(150, 380, 200, 25);
        frame.add(NICNumberField);

        // Address
        JLabel AddressLabel = new JLabel("Address:");
        AddressLabel.setBounds(50, 420, 100, 25);
        frame.add(AddressLabel);

        AddressField = new JTextField();
        AddressField.setBounds(150, 420, 200, 25);
        frame.add(AddressField);

        // Date In
        JLabel dateInLabel = new JLabel("Date IN:");
        dateInLabel.setBounds(50, 460, 100, 25);
        frame.add(dateInLabel);

        dateInField = new JTextField();
        dateInField.setBounds(150, 460, 200, 25);
        frame.add(dateInField);

        // Date Out
        JLabel dateOutLabel = new JLabel("Date OUT:");
        dateOutLabel.setBounds(50, 500, 100, 25);
        frame.add(dateOutLabel);

        dateOutField = new JTextField();
        dateOutField.setBounds(150, 500, 200, 25);
        frame.add(dateOutField);

        // Save Button
        saveButton = new JButton("Save");
        saveButton.setBounds(30, 540, 80, 25);
        frame.add(saveButton);

        // Edit Button
        editButton = new JButton("Edit");
        editButton.setBounds(120, 540, 80, 25);
        frame.add(editButton);

        // Delete Button
        deleteButton = new JButton("Delete");
        deleteButton.setBounds(210, 540, 80, 25);
        frame.add(deleteButton);

        // Clear Button
        clearButton = new JButton("Clear");
        clearButton.setBounds(300, 540, 80, 25);
        frame.add(clearButton);

        // Back to Home Button
        homeButton = new JButton("Back to Home");
        homeButton.setBounds(150, 580, 180, 25);
        frame.add(homeButton);

        // Table for displaying reservation details
        tableModel = new DefaultTableModel(new String[]{"ID","Name", "Mob Num", "Nationality", "Gender", "Email", "NIC Num", "Address", "Room Type", "Bed Type", "Date In", "Date Out"}, 0);
        reservationTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(reservationTable);
        tableScrollPane.setBounds(390, 60, 750, 500); // Adjusted width and height
        frame.add(tableScrollPane);

        //load reservations from the database
        loadReservations();

        // Action Listener
        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ("Admin".equals(UserRole))
                new Home("Admin");
                else
                    new Home_Customer("Customer");// Assuming Home is another JFrame class for the home page
                frame.dispose(); // Close the current window
            }
        });

        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String Name = NameField.getText().trim();
                    String MobileNumber = MobileNumberField.getText().trim();
                    String Nationality = NationalityField.getText().trim();
                    String Gender = (String) GenderField.getSelectedItem();
                    String Email = EmailField.getText().trim();
                    String NICNumber = NICNumberField.getText().trim();
                    String Address = AddressField.getText().trim();
                    String RoomType = (String) RoomTypeField.getSelectedItem();
                    String BedType = (String) BedTypeField.getSelectedItem();

                    // Parsing dates
                    Date dateIn = sdf.parse(dateInField.getText().trim());
                    Date dateOut = sdf.parse(dateOutField.getText().trim());

                    // Validation to ensure all fields are filled
                    if (Name.isEmpty() || MobileNumber.isEmpty() || Nationality.isEmpty() || Gender.isEmpty() ||
                            Email.isEmpty() || NICNumber.isEmpty() || Address.isEmpty() || RoomType.isEmpty() ||
                            BedType.isEmpty() || dateIn == null || dateOut == null) {

                        JOptionPane.showMessageDialog(frame, "Please fill in all fields.");
                    } else {
                        String sql = "INSERT INTO hotel_managementdb.reservation (Name, MobileNumber, Nationality, Gender, Email, NICNumber, Address, RoomType, BedType, DateIn, DateOut) VALUES (?,?,?,?,?,?,?,?,?,?,?)";

                        try (Connection connection = DatabaseUtil.getConnection();
                             PreparedStatement statement = connection.prepareStatement(sql)) {



                            statement.setString(1, Name);
                            statement.setString(2, MobileNumber);
                            statement.setString(3, Nationality);
                            statement.setString(4, Gender);
                            statement.setString(5, Email);
                            statement.setString(6, NICNumber);
                            statement.setString(7, Address);
                            statement.setString(8, RoomType);
                            statement.setString(9, BedType);
                            statement.setString(10, sdf.format(dateIn));
                            statement.setString(11, sdf.format(dateOut));

                            statement.executeUpdate();

                            // Update the table
                            tableModel.addRow(new Object[]{Name, MobileNumber, Nationality, Gender, Email, NICNumber, Address, RoomType, BedType, sdf.format(dateIn), sdf.format(dateOut)});
                            JOptionPane.showMessageDialog(frame, "Reservation saved successfully.");

                            loadReservations();
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(frame, "Failed to save reservation: " + ex.getMessage());
                        }
                    }
                } catch (ParseException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Invalid date format. Please enter dates in the format yyyy-MM-dd.");
                }
            }
        });



        // Edit button action
        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = reservationTable.getSelectedRow();
                if (selectedRow != -1) {
                    try {

                        String Name = NameField.getText().trim();
                        String MobileNumber = MobileNumberField.getText().trim();
                        String Nationality = NationalityField.getText().trim();
                        String Gender = (String) GenderField.getSelectedItem();
                        String Email = EmailField.getText().trim();
                        String NICNumber = NICNumberField.getText().trim();
                        String Address = AddressField.getText().trim();
                        String RoomType = (String) RoomTypeField.getSelectedItem();
                        String BedType = (String) BedTypeField.getSelectedItem();

                        // Parsing dates
                        Date dateIn = sdf.parse(dateInField.getText().trim());
                        Date dateOut = sdf.parse(dateOutField.getText().trim());

                        String dateInStr = (dateIn != null) ? sdf.format(dateIn): "";
                        String dateOutStr = (dateOut != null) ? sdf.format(dateOut): "";

                        // Validation to ensure all fields are filled
                        if (Name.isEmpty() || MobileNumber.isEmpty() || Nationality.isEmpty() || Gender.isEmpty() ||
                                Email.isEmpty() || NICNumber.isEmpty() || Address.isEmpty() || RoomType.isEmpty() ||
                                BedType.isEmpty() || dateIn == null || dateOut == null) {

                            JOptionPane.showMessageDialog(frame, "Please fill in all fields.");
                        } else {
                            String sql = "UPDATE hotel_managementdb.reservation SET Name=?, MobileNumber=?, Nationality=?, Gender=?, Email=?, NICNumber=?, Address=?, RoomType=?, BedType=?, DateIn=?, DateOut=? WHERE ID=?"; // Make sure you have an ID column to identify rows

                            try (Connection connection = DatabaseUtil.getConnection();
                                 PreparedStatement statement = connection.prepareStatement(sql)) {

                                System.out.println("Datein passed: "+dateInStr);
                                System.out.println("Dateout passed: "+dateOutStr);


                                statement.setString(1, Name);
                                statement.setString(2, MobileNumber);
                                statement.setString(3, Nationality);
                                statement.setString(4, Gender);
                                statement.setString(5, Email);
                                statement.setString(6, NICNumber);
                                statement.setString(7, Address);
                                statement.setString(8, RoomType);
                                statement.setString(9, BedType);
                                statement.setString(10,dateInStr);
                                statement.setString(11, dateOutStr);

                                statement.setInt(12, Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString())); // Assuming the first column is the ID

                                statement.executeUpdate();

                                // Update the table
                                loadReservations();

                                JOptionPane.showMessageDialog(frame, "Reservation updated successfully.");
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                                JOptionPane.showMessageDialog(frame, "Failed to update reservation: " + ex.getMessage());
                            }
                        }
                    } catch (ParseException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(frame, "Invalid date format. Please enter dates in the format yyyy-MM-dd.");
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Please select a reservation to edit.");
                }
            }
        });

        reservationTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int selectedRow = reservationTable.getSelectedRow();

                if (selectedRow >= 0) {
                    NameField.setText(tableModel.getValueAt(selectedRow, 1).toString());
                    MobileNumberField.setText(tableModel.getValueAt(selectedRow, 2).toString());
                    NationalityField.setText(tableModel.getValueAt(selectedRow, 3).toString());
                    GenderField.setSelectedItem(tableModel.getValueAt(selectedRow, 4).toString());
                    EmailField.setText(tableModel.getValueAt(selectedRow, 5).toString());
                    NICNumberField.setText(tableModel.getValueAt(selectedRow, 6).toString());
                    AddressField.setText(tableModel.getValueAt(selectedRow, 7).toString());
                    RoomTypeField.setSelectedItem(tableModel.getValueAt(selectedRow, 8).toString());
                    BedTypeField.setSelectedItem(tableModel.getValueAt(selectedRow, 9).toString());
                    dateInField.setText(tableModel.getValueAt(selectedRow, 10).toString());
                    dateOutField.setText(tableModel.getValueAt(selectedRow, 11).toString());
                }
            }
        });

        // Delete button
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = reservationTable.getSelectedRow();
                if (selectedRow != -1) {
                    int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete this reservation?");
                    if (confirm == JOptionPane.YES_OPTION) {
                        String sql = "DELETE FROM hotel_managementdb.reservation WHERE ID=?";

                        try (Connection connection = DatabaseUtil.getConnection();
                            PreparedStatement statement = connection.prepareStatement(sql)) {
                            // Assuming the first column is the ID
                            statement.setInt(1, Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString()));

                            statement.executeUpdate();

                            // Remove the row from the table
                            tableModel.removeRow(selectedRow);

                            loadReservations();

                            JOptionPane.showMessageDialog(frame, "Reservation deleted successfully.");
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(frame, "Failed to delete reservation: " + ex.getMessage());
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Please select a reservation to delete.");
                }
            }
        });

        // Clear button action
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Clear all input fields
                NameField.setText("");
                MobileNumberField.setText("");
                NationalityField.setText("");
                GenderField.setSelectedIndex(0);
                EmailField.setText("");
                NICNumberField.setText("");
                AddressField.setText("");
                RoomTypeField.setSelectedIndex(0);
                BedTypeField.setSelectedIndex(0);
                dateInField.setText("");
                dateOutField.setText("");
            }
        });

        frame.setVisible(true);
    }

    private  void loadReservations(){
        tableModel.setRowCount(0);

        String sql = "Select * from hotel_managementdb.reservation";

        try(Connection connection = DatabaseUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery()){

            while (resultSet.next()){
                int id = resultSet.getInt("ID");
                String name = resultSet.getString("Name");
                String mobileNumber = resultSet.getString("MobileNumber");
                String nationality = resultSet.getString("Nationality");
                String gender = resultSet.getString("Gender");
                String email = resultSet.getString("Email");
                String nicNumber = resultSet.getString("NICNumber");
                String address = resultSet.getString("Address");
                String roomType = resultSet.getString("RoomType");
                String bedType = resultSet.getString("bedType");
                Date dateIn = resultSet.getDate("DateIn");
                Date dateOut = resultSet.getDate("DateOut");

                String dateInStr = (dateIn != null) ? sdf.format(dateIn): "";
                String dateOutStr = (dateOut != null) ? sdf.format(dateOut): "";


                tableModel.addRow(new Object[]{ id,name,mobileNumber,nationality,gender,email,nicNumber,address, roomType,bedType,dateInStr,dateOutStr});
            }

        }catch (SQLException e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame,"An error occured while loading reservation");
        }
    }

    public static void main(String[] args) {
        //new Reservation();
    }
}
