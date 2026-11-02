package edu.temple.cis.paystation;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;

// Part 5: the admin panel
public class AdminPanel extends JPanel {

    private PayStation payStation;
    private CustomerPanel customerPanel;
    private JLabel currentRateLabel;
    private Town currentTown;

    public AdminPanel(PayStation payStation, CustomerPanel customerPanel) {
        this.payStation = payStation;
        this.customerPanel = customerPanel;
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Admin (Maintenance)"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        currentRateLabel = new JLabel("", JLabel.CENTER);
        currentRateLabel.setFont(new Font("SansSerif", Font.BOLD, 14));

        add(currentRateLabel, BorderLayout.NORTH);
        add(makeButtons(), BorderLayout.CENTER);

        changeRate(Town.DEFAULT, false);
    }

    private JPanel makeButtons() {
        JButton emptyButton = new JButton("Empty");
        JButton changeRateButton = new JButton("Change Rate Strategy");

        emptyButton.addActionListener(e -> emptyPayStation());
        // Show the town submenu just below the button
        changeRateButton.addActionListener(e -> makeTownMenu().show(changeRateButton, 0, changeRateButton.getHeight()));

        JPanel buttonRow = new JPanel(new GridLayout(1, 2, 5, 5));
        buttonRow.add(emptyButton);
        buttonRow.add(changeRateButton);
        return buttonRow;
    }

    // Submenu listing every town and its rate, with the current town checked
    private JPopupMenu makeTownMenu() {
        JPopupMenu townMenu = new JPopupMenu("Choose a town");
        ButtonGroup group = new ButtonGroup();

        for (Town town : Town.values()) {
            JRadioButtonMenuItem townItem = new JRadioButtonMenuItem(town.toString());
            townItem.setSelected(town == currentTown);
            townItem.addActionListener(e -> changeRate(town, true));
            group.add(townItem);
            townMenu.add(townItem);
        }
        return townMenu;
    }

    private void emptyPayStation() {
        int collected = payStation.empty();

        customerPanel.printMessage("========= ADMIN: EMPTY =========");
        customerPanel.printMessage("  Collected: " + collected + " cents (" + toDollars(collected) + ")");
        customerPanel.printMessage("================================");
    }

    private void changeRate(Town town, boolean announce) {
        payStation.setRateStrategy(town.createRateStrategy());
        currentTown = town;
        currentRateLabel.setText("Current rate: " + town);

        if (announce) {
            customerPanel.printMessage("Admin: rate changed to " + town);
        }
        customerPanel.refreshDisplay();
    }

    // e.g. 135 -> "$1.35"
    private static String toDollars(int cents) {
        return String.format("$%d.%02d", cents / 100, cents % 100);
    }
}
