import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.imageio.ImageIO;

public class HotelManagementLogin extends JFrame {

    public String UserRole;

    public HotelManagementLogin() {
        // Create the frame
        setTitle("Hotel Management System");
        setSize(500, 430);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        BackgroundPanel backgroundPanel = new BackgroundPanel();
        backgroundPanel.setLayout(null);

        // Title Label
        JLabel titleLabel = new JLabel("Hotel Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBounds(120, 10, 250, 30);
        backgroundPanel.add(titleLabel);

        // Subtitle Label
        JLabel loginLabel = new JLabel("Login");
        loginLabel.setFont(new Font("Arial", Font.BOLD, 14));
        loginLabel.setBounds(230, 50, 60, 30);
        backgroundPanel.add(loginLabel);

        // Username Label and TextField
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(60, 90, 100, 25);
        backgroundPanel.add(usernameLabel);

        JTextField usernameField = new JTextField();
        usernameField.setBounds(150, 90, 200, 25);
        backgroundPanel.add(usernameField);

        // Password Label and PasswordField
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(60, 130, 100, 25);
        backgroundPanel.add(passwordLabel);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(150, 130, 200, 25);
        backgroundPanel.add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(150, 170, 100, 30);
        backgroundPanel.add(loginButton);

        JLabel dontHaveAccountLabel = new JLabel("Don't have an account?");
        dontHaveAccountLabel.setBounds(60, 210, 200, 20);
        backgroundPanel.add(dontHaveAccountLabel);

        JButton signUpButton = new JButton("Sign up");
        signUpButton.setBounds(150, 240, 100, 30);
        backgroundPanel.add(signUpButton);

        // Add action listener to the Sign Up button
        signUpButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new SignUp();
            }
        });

        // Add action listener to the Login button
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter both username and password", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String sql = "SELECT * FROM hotel_managementdb.user Where Username = ? AND Password = ?";

                try (Connection connection = DatabaseUtil.getConnection();
                    PreparedStatement statement = connection.prepareStatement(sql)){
                        statement.setString(1,username);
                        statement.setString(2,password);
                        ResultSet resultSet = statement.executeQuery();

                        if (resultSet.next()){
                            UserRole = resultSet.getString("UserRole");
                             // Close the login window

                            System.out.println("User Role: "+UserRole);
                            if ("Admin".equals(UserRole)){
                                new Home(UserRole);
                            }else {
                                new Home_Customer(UserRole);
                            }
                            dispose();

                        }else {
                            JOptionPane.showMessageDialog(null, "Invalid username or password", "Login Error", JOptionPane.ERROR_MESSAGE);
                        }

                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        add(backgroundPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    static class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel() {
            try {
                backgroundImage = ImageIO.read(new File("C:\\Users\\HP\\IdeaProjects\\Hotel_Management\\src\\login_pic.jpg"));
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
        new HotelManagementLogin();
    }
}
