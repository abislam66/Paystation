package edu.temple.cis.paystation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

// Part 4: the customer screen for the pay station.
// It lets a customer put in coins, see their parking time,
// buy a ticket, or cancel and get their coins back.
//
// How Part 5 uses it:
//     PayStation payStation = new PayStationImpl();
//     CustomerPanel customerPanel = new CustomerPanel(payStation);
//     frame.add(customerPanel);
public class CustomerPanel extends JPanel {

    private PayStation payStation;   // the pay station this screen talks to
    private JLabel displayLabel;     // the screen that shows the minutes bought
    private JTextArea printerArea;   // works like a printer for receipts and messages
    private int centsInserted = 0;   // how many cents the customer has put in so far

    // Build the customer screen
    public CustomerPanel(PayStation payStation) {
        this.payStation = payStation;                               // remember which pay station to use
        setLayout(new BorderLayout(10, 10));                        // top / middle / bottom layout
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // leave some space around the edges

        add(makeDisplay(), BorderLayout.NORTH);   // display at the top
        add(makeButtons(), BorderLayout.CENTER);  // buttons in the middle
        add(makePrinter(), BorderLayout.SOUTH);   // printer at the bottom

        refreshDisplay();                         // show "0 min" when the screen opens
    }

    // Make the black display screen with green numbers
    private JLabel makeDisplay() {
        displayLabel = new JLabel("", JLabel.CENTER);                            // text goes in the middle
        displayLabel.setOpaque(true);                                            // let the background color show
        displayLabel.setBackground(Color.BLACK);                                 // black screen
        displayLabel.setForeground(Color.GREEN);                                 // green text
        displayLabel.setFont(new Font("Monospaced", Font.BOLD, 28));             // big, bold numbers
        displayLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // space around the text
        return displayLabel;
    }

    // Put the coin row on top and the options row underneath
    private JPanel makeButtons() {
        JPanel buttonArea = new JPanel(new GridLayout(2, 1, 5, 5)); // 2 rows, 1 column
        buttonArea.add(makeCoinRow());
        buttonArea.add(makeOptionRow());
        return buttonArea;
    }

    // Make the 5, 10 and 25 cent buttons
    private JPanel makeCoinRow() {
        JButton fiveCentButton = new JButton("5 cents");
        JButton tenCentButton = new JButton("10 cents");
        JButton quarterButton = new JButton("25 cents");

        // When a coin button is clicked, put that coin in
        fiveCentButton.addActionListener(e -> depositCoin(5));
        tenCentButton.addActionListener(e -> depositCoin(10));
        quarterButton.addActionListener(e -> depositCoin(25));

        JPanel coinRow = new JPanel(new GridLayout(1, 3, 5, 5));             // 1 row, 3 buttons side by side
        coinRow.setBorder(BorderFactory.createTitledBorder("Deposit Coins")); // label above the row
        coinRow.add(fiveCentButton);
        coinRow.add(tenCentButton);
        coinRow.add(quarterButton);
        return coinRow;
    }

    // Make the Display, Buy Ticket and Cancel buttons
    private JPanel makeOptionRow() {
        JButton displayButton = new JButton("Display");
        JButton buyButton = new JButton("Buy Ticket");
        JButton cancelButton = new JButton("Cancel");

        // Tell each button which method to run when clicked
        displayButton.addActionListener(e -> showDisplay());
        buyButton.addActionListener(e -> buyTicket());
        cancelButton.addActionListener(e -> cancelTransaction());

        JPanel optionRow = new JPanel(new GridLayout(1, 3, 5, 5));                // 1 row, 3 buttons side by side
        optionRow.setBorder(BorderFactory.createTitledBorder("Customer Options")); // label above the row
        optionRow.add(displayButton);
        optionRow.add(buyButton);
        optionRow.add(cancelButton);
        return optionRow;
    }

    // Make the printer box where receipts and messages show up
    private JScrollPane makePrinter() {
        printerArea = new JTextArea(10, 32);                                // 10 lines tall, 32 letters wide
        printerArea.setEditable(false);                                     // the customer can't type in it
        printerArea.setFont(new Font("Monospaced", Font.PLAIN, 14));        // same-width letters so lines match up
        JScrollPane printerBox = new JScrollPane(printerArea);              // adds a scroll bar for long output
        printerBox.setBorder(BorderFactory.createTitledBorder("Printer"));  // label the box "Printer"
        return printerBox;
    }

    // Deposit Coins: put one coin into the pay station
    private void depositCoin(int coinValue) {
        try {
            payStation.addPayment(coinValue);                  // give the coin to the pay station
            centsInserted = centsInserted + coinValue;         // keep track of the money put in
            printMessage("Inserted " + coinValue + " cents");  // show which coin went in
            if (isFreeParking()) {                             // is parking free right now?
                printMessage("Parking is FREE right now");     // then tell the customer
            }
        } catch (IllegalCoinException error) {
            // Our buttons only use good coins, but Java makes us handle bad ones anyway
            printMessage("Coin rejected: " + error.getMessage());
        }
        refreshDisplay();                                      // update the screen
    }

    // Display: print how much parking time has been bought so far
    private void showDisplay() {
        refreshDisplay();
        if (isFreeParking()) {
            printMessage("Display: parking is FREE right now");
        } else {
            int minutes = payStation.readDisplay();            // ask the pay station for the minutes
            printMessage("Display: " + minutes + " minutes bought so far");
        }
    }

    // Buy Ticket: finish paying and print a receipt
    private void buyTicket() {
        boolean free = isFreeParking();       // check this first, because buy() clears everything
        Receipt receipt = payStation.buy();   // the pay station gives us a receipt
        centsInserted = 0;                    // start fresh for the next customer

        printMessage("======== PARKING RECEIPT ========");
        if (free) {
            printMessage("  Parking time: FREE today");
        } else {
            printMessage("  Parking time: " + receipt.value() + " minutes");
        }
        printMessage("=================================");
        refreshDisplay();                     // the screen goes back to 0 min
    }

    // Cancel: give the coins back and print what was returned
    private void cancelTransaction() {
        Map<Integer, Integer> returnedCoins = payStation.cancel(); // coin value -> how many of that coin
        centsInserted = 0;                                         // start fresh for the next customer

        if (returnedCoins.isEmpty()) {                             // no coins were put in
            printMessage("Cancel: no coins to return");
        } else {
            printMessage("------ TRANSACTION CANCELED ------");
            printReturnedCoins(returnedCoins);
            printMessage("----------------------------------");
        }
        refreshDisplay();                                          // the screen goes back to 0 min
    }

    // Print each coin that was returned, how many of it, and the total
    private void printReturnedCoins(Map<Integer, Integer> returnedCoins) {
        int[] coinTypes = {5, 10, 25};   // check in this order so the list always looks the same
        int totalReturned = 0;

        for (int coin : coinTypes) {
            if (returnedCoins.containsKey(coin)) {         // was this coin returned?
                int count = returnedCoins.get(coin);       // how many of this coin
                printMessage("  " + coin + " cents x " + count);
                totalReturned = totalReturned + coin * count;
            }
        }
        printMessage("  Total returned: " + totalReturned + " cents");
    }

    // Free parking means coins went in but the rate gave 0 minutes.
    // Every paid rate gives at least 1 minute per coin, so 0 only happens
    // when parking is free (like Omegatown on weekends).
    private boolean isFreeParking() {
        return centsInserted > 0 && payStation.readDisplay() == 0;
    }

    // Update the screen with the minutes bought (or FREE).
    // Public so Part 5 can call it after Empty or Change Rate Strategy.
    public void refreshDisplay() {
        if (isFreeParking()) {
            displayLabel.setText("FREE");
        } else {
            displayLabel.setText(payStation.readDisplay() + " min");
        }
    }

    // Add one line to the printer box.
    // Public so Part 5 can print admin messages here too.
    public void printMessage(String message) {
        printerArea.append(message + "\n");                                  // add the text on a new line
        printerArea.setCaretPosition(printerArea.getDocument().getLength()); // scroll down to the newest line
    }
}