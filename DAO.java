package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DAO {

    private static final String URL = "jdbc:mysql://localhost:3306/quanlysach";
    private static final String USER = "root";
    private static final String PASSWORD = "221004";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println("Kết nối thất bại!");
            e.printStackTrace();
            return null;
        }
    }

    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Đã đóng kết nối!");
            } catch (SQLException e) {
                System.out.println("Đóng kết nối thất bại!");
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        try (Connection connection = getConnection()) {
            if (connection != null) {
                System.out.println("Kết nối thành công!");
            } else {
                System.out.println("Kết nối không thành công!");
            }
        } catch (SQLException e) {
            System.out.println("Xảy ra lỗi khi thực hiện kết nối!");
            e.printStackTrace();
        }
    }
}
