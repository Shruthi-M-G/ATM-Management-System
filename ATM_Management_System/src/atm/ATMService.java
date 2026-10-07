package atm;

import java.sql.SQLException;

public class ATMService {
    private final AccountDAO accountDAO;
    private final TransactionDAO transactionDAO;
    private Account currentAccount;

    public ATMService() {
        this.accountDAO = new AccountDAO();
        this.transactionDAO = new TransactionDAO();
    }

    public boolean login(String accountNumber, int pin) {
        try {
            currentAccount = accountDAO.authenticate(accountNumber, pin);
            return currentAccount != null;
        } catch (SQLException e) {
            System.err.println("Authentication service error: " + e.getMessage());
            return false;
        }
    }

    public Account getCurrentAccount() {
        return currentAccount;
    }

    public double getBalance() {
        return currentAccount.getBalance();
    }

    public boolean deposit(double amount) {
        if (amount <= 0) {
            System.out.println("Transaction rejected: Deposit amount must be greater than zero.");
            return false;
        }

        double updatedBalance = currentAccount.getBalance() + amount;
        try {
            if (accountDAO.updateBalance(currentAccount.getAccountNumber(), updatedBalance)) {
                currentAccount.setBalance(updatedBalance);
                transactionDAO.recordTransaction(currentAccount.getAccountNumber(), "DEPOSIT", amount, updatedBalance);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Transaction processing error: " + e.getMessage());
        }
        return false;
    }

    public boolean withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("Transaction rejected: Withdrawal amount must be greater than zero.");
            return false;
        }

        if (amount > currentAccount.getBalance()) {
            System.out.println("Transaction rejected: Insufficient funds.");
            return false;
        }

        double updatedBalance = currentAccount.getBalance() - amount;
        try {
            if (accountDAO.updateBalance(currentAccount.getAccountNumber(), updatedBalance)) {
                currentAccount.setBalance(updatedBalance);
                transactionDAO.recordTransaction(currentAccount.getAccountNumber(), "WITHDRAWAL", amount, updatedBalance);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Transaction processing error: " + e.getMessage());
        }
        return false;
    }

    public boolean changePin(int newPin) {
        if (newPin < 1000 || newPin > 9999) {
            System.out.println("Invalid format: PIN must be a 4-digit number.");
            return false;
        }

        try {
            if (accountDAO.updatePin(currentAccount.getAccountNumber(), newPin)) {
                currentAccount.setPin(newPin);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("PIN update failed: " + e.getMessage());
        }
        return false;
    }

    public void displayMiniStatement() {
        try {
            transactionDAO.printMiniStatement(currentAccount.getAccountNumber());
        } catch (SQLException e) {
            System.err.println("Statement retrieval failed: " + e.getMessage());
        }
    }

    public void logout() {
        currentAccount = null;
    }
}