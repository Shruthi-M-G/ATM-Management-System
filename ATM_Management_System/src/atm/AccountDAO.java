package atm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountDAO {

    public Account authenticate(String accountNumber, int pin) throws SQLException {
        String query = "SELECT account_number, holder_name, pin, balance FROM accounts WHERE account_number = ? AND pin = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, accountNumber);
            ps.setInt(2, pin);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Account(
                        rs.getString("account_number"),
                        rs.getString("holder_name"),
                        rs.getInt("pin"),
                        rs.getDouble("balance")
                    );
                }
            }
        }
        return null;
    }

    public boolean updateBalance(String accountNumber, double newBalance) throws SQLException {
        String query = "UPDATE accounts SET balance = ? WHERE account_number = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setDouble(1, newBalance);
            ps.setString(2, accountNumber);

            return ps.executeUpdate() > 0;
        }
    }

    public boolean updatePin(String accountNumber, int newPin) throws SQLException {
        String query = "UPDATE accounts SET pin = ? WHERE account_number = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, newPin);
            ps.setString(2, accountNumber);

            return ps.executeUpdate() > 0;
        }
    }
}