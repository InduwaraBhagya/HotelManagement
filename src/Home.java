import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;


public class Home extends JFrame {

    public Home(String userRole) {
        // Set frame properties
        setTitle("Hotel Management System");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        BackgroundPanel backgroundPanel = new BackgroundPanel();
        backgroundPanel.setLayout(null); // Use null layout for custom placement

        // Manage Room Button
        JButton manageRoomButton = new JButton("Manage Room");
        manageRoomButton.setBounds(50, 50, 200, 50); // Position as per the image
        backgroundPanel.add(manageRoomButton);

        // Customer Reservation Button
        JButton customerReservationButton = new JButton("Customer Reservation");
        customerReservationButton.setBounds(50, 130, 200, 50); // Position as per the image
        backgroundPanel.add(customerReservationButton);

        // Customer Bill Button
        JButton customerBillButton = new JButton("Customer Report");
        customerBillButton.setBounds(50, 210, 200, 50); // Position as per the image
        backgroundPanel.add(customerBillButton);

        // Exit Button
        JButton exitButton = new JButton("Exit");
        exitButton.setBounds(450, 300, 100, 30); // Position as per the image
        backgroundPanel.add(exitButton);

        // Action listeners for buttons
        manageRoomButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Open the Room Management window
                new Room("Admin");
                dispose();
            }
        });

        customerReservationButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Open the Reservation window
                new Reservation("Admin");
                dispose();
            }
        });

        customerBillButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Open the CustomerBill window
                CustomerBill customerBill = new CustomerBill("Admin");
                customerBill.setVisible(true);
                dispose();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int response = JOptionPane.showConfirmDialog(null, "Do you really want to exit?", "Exit",
                        JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    System.exit(0); // Exit the program
                }
            }
        });

        add(backgroundPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel() {
            try {
                backgroundImage = ImageIO.read(new File("C:\\Users\\HP\\IdeaProjects\\Hotel_Management\\src\\home_pic.jpg"));
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
        //new Home("Admin");
    }
}
