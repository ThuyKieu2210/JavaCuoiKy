package Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HangHoaDao {
    private static final String URL = "jdbc:mysql://localhost:3306/quanlysach";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "221004";

    public List<HangHoa> getAllHangHoa() {
        List<HangHoa> list = new ArrayList<>();
        String query = "SELECT * FROM them";

        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String maSach = rs.getString("maSach");
                String tenSach = rs.getString("tenSach");
                String tacGia = rs.getString("tacGia");
                String theLoai = rs.getString("theLoai");
                double giaBan = rs.getDouble("giaBan");
                int soLuongTonKho = rs.getInt("soLuongTonKho");

                HangHoa hh = new HangHoa(maSach, tenSach, tacGia, theLoai, giaBan, soLuongTonKho);
                list.add(hh);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // Thêm phương thức để thực hiện các thao tác thêm, cập nhật và xóa dữ liệu
}
