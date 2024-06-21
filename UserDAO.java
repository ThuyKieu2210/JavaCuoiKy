package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserDAO {

    public static boolean registerUser(String username, String hashedPassword) {
        String sql = "INSERT INTO nguoidung (username, password, role) VALUES (?, ?, ?)";
        try (Connection conn = DAO.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, username);
            statement.setString(2, hashedPassword);
            statement.setString(3, "employee");
            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int userId = generatedKeys.getInt(1);
                        System.out.println("Inserted record's ID: " + userId);
                    }
                }
            }

            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isUsernameTaken(String username) {
        String sql = "SELECT COUNT(*) FROM nguoidung WHERE username = ?";
        try (Connection conn = DAO.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean validateLogin(String username, String password) {
        try (Connection conn = DAO.getConnection()) {
            String query = "SELECT password FROM nguoidung WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String hashedPassword = rs.getString("password");
                BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                return passwordEncoder.matches(password, hashedPassword);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
