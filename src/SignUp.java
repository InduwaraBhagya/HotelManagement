import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SignUp extends JFrame {
    private JComboBox<String> UserRoleField;

    public SignUp() {

        JFrame frame = new JFrame("Hotel Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 450);

        BackgroundPanel backgroundPanel = new BackgroundPanel();
        backgroundPanel.setLayout(null); // Use null layout for custom placement
        frame.add(backgroundPanel);

        // Title Label
        JLabel titleLabel = new JLabel("Hotel Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBounds(160, 10, 800, 30);
        backgroundPanel.add(titleLabel);

        // Subtitle Label
        JLabel signUpLabel = new JLabel("Sign Up");
        signUpLabel.setFont(new Font("Arial", Font.BOLD, 14));
        signUpLabel.setBounds(230, 50, 300, 30);
        backgroundPanel.add(signUpLabel);

        //UserName
        JLabel UserNameLabel = new JLabel("Username:");
        UserNameLabel.setBounds(50, 90, 80, 25);
        backgroundPanel.add(UserNameLabel);

        JTextField UserNameField = new JTextField();
        UserNameField.setBounds(160, 90, 200, 25);
        backgroundPanel.add(UserNameField);

        //Role
        JLabel UserRoleLabel = new JLabel("User Role:");
        UserRoleLabel.setBounds(50, 120, 80, 25);
        backgroundPanel.add(UserRoleLabel);

        String[] UserRole = {"Admin", "Customer"};
        UserRoleField = new JComboBox<>(UserRole);
        UserRoleField.setBounds(160, 120, 200, 25);
        backgroundPanel.add(UserRoleField);


        // Name Label and TextField
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(50, 150, 80, 25);
        backgroundPanel.add(nameLabel);

        JTextField nameField = new JTextField();
        nameField.setBounds(160, 150, 200, 25);
        backgroundPanel.add(nameField);

        // Email Label and TextField
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(50, 180, 80, 25);
        backgroundPanel.add(emailLabel);

        JTextField emailField = new JTextField();
        emailField.setBounds(160, 180, 200, 25);
        backgroundPanel.add(emailField);

        // Password Label and PasswordField
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(50, 210, 80, 25);
        backgroundPanel.add(passwordLabel);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(160, 210, 200, 25);
        backgroundPanel.add(passwordField);

        // Confirm Password Label and PasswordField
        JLabel confirmPassLabel = new JLabel("Confirm Password:");
        confirmPassLabel.setBounds(50, 240, 120, 25);
        backgroundPanel.add(confirmPassLabel);

        JPasswordField confirmPasswordField = new JPasswordField();
        confirmPasswordField.setBounds(160, 240, 200, 25);
        backgroundPanel.add(confirmPasswordField);

        // Address Label and TextField
        JLabel addressLabel = new JLabel("Address:");
        addressLabel.setBounds(50, 270, 80, 25);
        backgroundPanel.add(addressLabel);

        JTextField addressField = new JTextField();
        addressField.setBounds(160, 270, 200, 25);
        backgroundPanel.add(addressField);

        // Sign In Button
        JButton signInButton = new JButton("Sign In");
        signInButton.setBounds(150, 330, 100, 30);
        backgroundPanel.add(signInButton);

        // Add action listener to Sign In button
        signInButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String Username = UserNameField.getText().trim();
                String name = nameField.getText().trim();
                String email = emailField.getText().trim();
                String password = new String(passwordField.getPassword());
                String confirmPassword = new String(confirmPasswordField.getPassword());
                String address = addressField.getText().trim();
                String UserRole = (String) UserRoleField.getSelectedItem();

                if (Username.isEmpty() || name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || address.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please fill in all fields.");
                } else if (!password.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(frame, "Passwords do not match!");
                } else {

                    String sql = "INSERT INTO hotel_managementdb.user (Username,UserRole ,Password, Name, Email, Address) VALUES (?,?,?,?,?,?);";

                    try (Connection connection = DatabaseUtil.getConnection();
                         PreparedStatement statement = connection.prepareStatement(sql)) {

                        statement.setString(1, Username);
                        statement.setString(2, UserRole);
                        statement.setString(3, password);
                        statement.setString(4, name);
                        statement.setString(5, email);
                        statement.setString(6, address);

                        System.out.println(statement);

                        statement.executeUpdate();
                        JOptionPane.showMessageDialog(frame, "Sign-Up successful!");
                        frame.dispose(); // Close the sign-up window
                        if (UserRole == "Admin"){
                            new Home(UserRole);
                        }else {
                            new Home_Customer(UserRole);
                        }


                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, "Signup failed: " + ex.getMessage(), "Signup Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        frame.setVisible(true);
    }

    private class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel() {
            try {
                backgroundImage = ImageIO.read(new File("C:\\Users\\HP\\IdeaProjects\\Hotel_Management\\src\\signup_pic.jpg"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    public static void main(String[] args) {
        new SignUp();
    }
}
