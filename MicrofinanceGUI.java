import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class MicrofinanceGUI extends JFrame {

    // Declare the savings balance variable
    private double savingsBalance = 0.0;
    int userId = 1;

    // Components for Member Registration
    private JTextField nameField, contactField;
    private JButton registerMemberButton;

    // Components for Loan Calculation
    private JTextField loanAmountField, loanDurationField, intrestRateField;
    private JButton calculateLoanButton;
    private JLabel loanResultLabel;

    // Components for Loan Repayment
    private JTextField repaymentAmountField, transactionIdField;
    private JButton repayLoanButton;

    // Components for Savings and Withdrawals
    private JTextField savingsAmountField, withdrawAmountField,id_number;
    private JButton depositButton, withdrawButton;
    private JLabel balanceLabel,idnumber;

    // MySQL Database connection
    private Connection connection;

    public MicrofinanceGUI() {
        // Initialize MySQL connection
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/java_project", "scoffield",
                    "Ronjohnsonowuor@8382");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error connecting to database: " + ex.getMessage());
            return;
        }

        setTitle("Microfinance Management System");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Member Registration Panel
        JPanel memberPanel = new JPanel();
        memberPanel.setLayout(new GridLayout(4,2));
        memberPanel.setBorder(BorderFactory.createTitledBorder("Member Registration"));

        nameField = new JTextField();
        contactField = new JTextField();
        registerMemberButton = new JButton("Register Member");

        memberPanel.add(new JLabel("Name:"));
        memberPanel.add(nameField);
        memberPanel.add(new JLabel("Contact:"));
        memberPanel.add(contactField);
        memberPanel.add(new JLabel(""));
        memberPanel.add(registerMemberButton);
        idnumber = new JLabel("you id number will appear here after registration");
        memberPanel.add(idnumber);
        id_number = new JTextField();
        memberPanel.add(id_number);

        // Loan Calculation Panel
        JPanel loanPanel = new JPanel();
        loanPanel.setLayout(new GridLayout(4, 3));
        loanPanel.setBorder(BorderFactory.createTitledBorder("Loan Calculation"));

        loanAmountField = new JTextField();
        loanDurationField = new JTextField();
        intrestRateField = new JTextField();
        calculateLoanButton = new JButton("Calculate Loan");
        loanResultLabel = new JLabel("Loan details will appear here");

        loanPanel.add(new JLabel("Loan Amount:"));
        loanPanel.add(loanAmountField);
        loanPanel.add(new JLabel("Loan Duration (years):"));
        loanPanel.add(loanDurationField);
        loanPanel.add(new JLabel("InterestRate:"));
        loanPanel.add(intrestRateField);
        loanPanel.add(calculateLoanButton);
        loanPanel.add(loanResultLabel);

        // Loan Repayment Panel
        JPanel repaymentPanel = new JPanel();
        repaymentPanel.setLayout(new GridLayout(3, 2));
        repaymentPanel.setBorder(BorderFactory.createTitledBorder("Loan Repayment"));

        transactionIdField = new JTextField();
        repaymentAmountField = new JTextField();
        repayLoanButton = new JButton("Repay Loan");

        repaymentPanel.add(new JLabel("Transaction ID:"));
        repaymentPanel.add(transactionIdField);
        repaymentPanel.add(new JLabel("Repayment Amount:"));
        repaymentPanel.add(repaymentAmountField);
        repaymentPanel.add(new JLabel(""));
        repaymentPanel.add(repayLoanButton);

        // Savings and Withdrawals Panel
        JPanel savingsPanel = new JPanel();
        savingsPanel.setLayout(new GridLayout(4, 2));
        savingsPanel.setBorder(BorderFactory.createTitledBorder("Savings and Withdrawals"));

        savingsAmountField = new JTextField();
        withdrawAmountField = new JTextField();
        depositButton = new JButton("Deposit");
        withdrawButton = new JButton("Withdraw");
        balanceLabel = new JLabel("Savings Balance: 0.0");

        savingsPanel.add(new JLabel("Deposit Amount:"));
        savingsPanel.add(savingsAmountField);
        savingsPanel.add(new JLabel("Withdraw Amount:"));
        savingsPanel.add(withdrawAmountField);
        savingsPanel.add(depositButton);
        savingsPanel.add(withdrawButton);
        savingsPanel.add(balanceLabel);

        // Add Panels to Main Frame
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(5, 1));
        mainPanel.add(memberPanel);
        mainPanel.add(loanPanel);
        mainPanel.add(repaymentPanel);
        mainPanel.add(savingsPanel);

        // Add View Balance Button
        JButton viewBalanceButton = new JButton("View Balance");
        // mainPanel.add(viewBalanceButton);

        add(mainPanel, BorderLayout.CENTER);

        // Action Listeners
        registerMemberButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String name = nameField.getText();
                    String contact = contactField.getText();
                    String query = "INSERT INTO members (name, contact) VALUES (?, ?)";
                    try (PreparedStatement stmt = connection.prepareStatement(query,Statement.RETURN_GENERATED_KEYS)) {
                        stmt.setString(1, name);
                        stmt.setString(2, contact);
                        stmt.executeUpdate();

                        try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                            if (generatedKeys.next()) {
                                int loanId = generatedKeys.getInt(1);
                                userId= loanId;
                                id_number.setText("your id number is: "+ loanId);
                            }
                        }

                    }

                    if (name.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "please enter name");
                    } else if (contact.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "please enter contact");
                    } else {
                        JOptionPane.showMessageDialog(null, "Member Registered Successfully!");
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error registering member: " + ex.getMessage());
                }
            }
        });

        calculateLoanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    double loanAmount = Double.parseDouble(loanAmountField.getText());
                    int loanDuration = Integer.parseInt(loanDurationField.getText());
                    double InterestRate = Double.parseDouble(intrestRateField.getText());
                    double totalAmount = loanAmount * (1 + InterestRate / 100 * loanDuration);

                    // Save loan data in database
                    String query = "INSERT INTO loans (member_id, loan_amount, loan_duration, total_amount) VALUES (?, ?, ?, ?)";
                    try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                        stmt.setInt(1, 1);
                        stmt.setDouble(2, loanAmount);
                        stmt.setInt(3, loanDuration);
                        stmt.setDouble(4, totalAmount);
                        stmt.executeUpdate();

                        // Get the generated loan_id
                        try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                            if (generatedKeys.next()) {
                                int loanId = generatedKeys.getInt(1);
                                loanResultLabel.setText("Loan ID: " + loanId + ", Total Loan: " + totalAmount);
                            }
                        }
                    }
                } catch (NumberFormatException | SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid loan details: " + ex.getMessage());
                }
            }
        });

        repayLoanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    double repaymentAmount = Double.parseDouble(repaymentAmountField.getText());
                    String transactionId = transactionIdField.getText();

                    // Save repayment to database
                    String query = "INSERT INTO transactions (loan_id, transaction_type, amount, transaction_date) VALUES (?, ?, ?, CURDATE())";
                    try (PreparedStatement stmt = connection.prepareStatement(query)) {
                        stmt.setInt(1, 1); // Assuming loan_id = 1 for simplicity
                        stmt.setString(2, "Repayment");
                        stmt.setDouble(3, repaymentAmount);
                        stmt.executeUpdate();
                    }

                    JOptionPane.showMessageDialog(null, "Repayment of " + repaymentAmount + " for Transaction ID "
                            + transactionId + " successful.");
                } catch (NumberFormatException | SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid repayment amount: " + ex.getMessage());
                }
            }
        });

        depositButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    double depositAmount = Double.parseDouble(savingsAmountField.getText());
                    
                    try {
                        int loanDuration = 1;
                        double InterestRate = 7;
                        double initialLoan = 0.6 * depositAmount;
                        double totalAmount = initialLoan * (1 + InterestRate / 100 * loanDuration);
    
                        // Save loan data in database
                        String query = "INSERT INTO loans (member_id, loan_amount, loan_duration, total_amount) VALUES (?, ?, ?, ?)";
                        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                            stmt.setInt(1,userId);
                            stmt.setDouble(2, initialLoan);
                            stmt.setInt(3, 1);
                            stmt.setDouble(4, totalAmount);
                            stmt.executeUpdate();
    
                            // Get the generated loan_id
                            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                                if (generatedKeys.next()) {
                                    loanResultLabel.setText("your initial loan after deposit is:  "+ initialLoan +"  your interest: "+ InterestRate+ "  duration:"+loanDuration+" year"+"  total loan: " + totalAmount);
                                }
                            }
                        }
                    } catch (NumberFormatException | SQLException ex) {
                        JOptionPane.showMessageDialog(null, "Invalid loan details: " + ex.getMessage());
                    }
                    savingsBalance += depositAmount;
                    balanceLabel.setText("Savings Balance: " + savingsBalance);

                    // Save deposit to database
                    String query = "INSERT INTO transactions (loan_id, transaction_type, amount, transaction_date) VALUES (?, ?, ?, CURDATE())";
                    try (PreparedStatement stmt = connection.prepareStatement(query)) {
                        stmt.setInt(1, 1); // Assuming loan_id = 1 for simplicity
                        stmt.setString(2, "Deposit");
                        stmt.setDouble(3, depositAmount);
                        stmt.executeUpdate();
                    }
                    JOptionPane.showMessageDialog(null, "Deposited " + depositAmount + " to savings.");
                } catch (NumberFormatException | SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid deposit amount: " + ex.getMessage());
                }
            }
        });

        withdrawButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    double withdrawAmount = Double.parseDouble(withdrawAmountField.getText());
                    if (withdrawAmount <= savingsBalance) {
                        savingsBalance -= withdrawAmount;
                        balanceLabel.setText("Savings Balance: " + savingsBalance);

                        // Save withdrawal to database
                        String query = "INSERT INTO transactions (loan_id, transaction_type, amount, transaction_date) VALUES (?, ?, ?, CURDATE())";
                        try (PreparedStatement stmt = connection.prepareStatement(query)) {
                            stmt.setInt(1, 1); // Assuming loan_id = 1 for simplicity
                            stmt.setString(2, "Withdraw");
                            stmt.setDouble(3, withdrawAmount);
                            stmt.executeUpdate();
                        }
                        JOptionPane.showMessageDialog(null, "Withdrew " + withdrawAmount + " from savings.");
                    } else {
                        JOptionPane.showMessageDialog(null, "Insufficient savings balance");
                    }
                } catch (NumberFormatException | SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid withdrawal amount: " + ex.getMessage());
                }
            }
        });

        // View Balance Button ActionListener
        viewBalanceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Query the database for the savings balance and loan balance
                    String query = "SELECT savings_balance, loan_balance FROM accounts WHERE member_id = ?";
                    try (PreparedStatement stmt = connection.prepareStatement(query)) {
                        stmt.setInt(1, 1); // Assuming member_id = 1 for simplicity
                        ResultSet rs = stmt.executeQuery();

                        if (rs.next()) {
                            double savingsBalance = rs.getDouble("savings_balance");
                            double loanBalance = rs.getDouble("loan_balance");
                            JOptionPane.showMessageDialog(null,
                                    "Savings Balance: " + savingsBalance + "\nLoan Balance: " + loanBalance);
                        } else {
                            JOptionPane.showMessageDialog(null, "No account found for member.");
                        }
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error retrieving balance: " + ex.getMessage());
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MicrofinanceGUI gui = new MicrofinanceGUI();
            gui.setVisible(true);
        });
    }
}