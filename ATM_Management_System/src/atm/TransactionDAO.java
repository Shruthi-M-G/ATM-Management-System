package atm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TransactionDAO {

    public void recordTransaction(String accountNumber, String type, double amount, double balanceAfter) throws SQLException {
        String query = "INSERT INTO transactions (account_number, transaction_type, amount, balance_after) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, accountNumber);
            ps.setString(2, type);
            ps.setDouble(3, amount);
            ps.setDouble(4, balanceAfter);

            ps.executeUpdate();
        }
    }

    public void printMiniStatement(String accountNumber) throws SQLException {
        String query = "SELECT transaction_type, amount, balance_after, timestamp " +
                       "FROM transactions WHERE account_number = ? " +
                       "ORDER BY timestamp DESC LIMIT 5";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, accountNumber);
            try (ResultSet rs = ps.executeQuery()) {
                System.out.println("\n---------------- Mini Statement (Last 5) ----------------");
                System.out.printf("%-12s | %-12s | %-14s | %-19s%n", "Type", "Amount", "Balance", "Timestamp");
                System.out.println("---------------------------------------------------------");

                boolean recordsExist = false;
                while (rs.next()) {
                    recordsExist = true;
                    System.out.printf("%-12s | $%-11.2f | $%-13.2f | %s%n",
                        rs.getString("transaction_type"),
                        rs.getDouble("amount"),
                        rs.getDouble("balance_after"),
                        rs.getTimestamp("timestamp")
                    );
                }

                if (!recordsExist) {
                    System.out.println("No transactions found.");
                }
                System.out.println("---------------------------------------------------------");
            }
        }
    }
}