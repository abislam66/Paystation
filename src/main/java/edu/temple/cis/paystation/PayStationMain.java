package edu.temple.cis.paystation;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

// Part 5: the main program
public class PayStationMain {

    public static void main(String[] args) {
        // Swing screens must be built on Swing's own thread
        SwingUtilities.invokeLater(PayStationMain::createAndShowWindow);
    }

    private static void createAndShowWindow() {
        PayStation payStation = new PayStationImpl();
        CustomerPanel customerPanel = new CustomerPanel(payStation);
        AdminPanel adminPanel = new AdminPanel(payStation, customerPanel);

        JFrame frame = new JFrame("PayStation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(customerPanel, BorderLayout.CENTER);
        frame.add(adminPanel, BorderLayout.SOUTH);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
