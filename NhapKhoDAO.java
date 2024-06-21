package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Model.NhapKho;

public class NhapKhoDAO {
	public static List<NhapKho> getAllNhapKho() {
        List<NhapKho> listNhapKho = new ArrayList<>();
        String sql = "SELECT * FROM nhap_kho";
        try (Connection conn = DAO.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                String maNhapKho = resultSet.getString("maNhapKho");
                String ngayNhap = resultSet.getString("ngayNhap");
                int soLuongNhap = resultSet.getInt("soLuongNhap");
                String tenSachNhap = resultSet.getString("tenSachNhap");
                listNhapKho.add(new NhapKho(maNhapKho, ngayNhap, soLuongNhap, tenSachNhap));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listNhapKho;
    }

    public static boolean addNhapKho(NhapKho nhapKho) {
        String sql = "INSERT INTO nhap_kho (maNhapKho, ngayNhap, soLuongNhap, nhaCungCap) VALUES (?, ?, ?, ?)";
        try (Connection conn = DAO.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, nhapKho.getMaNhapKho());
            statement.setString(2, nhapKho.getNgayNhap());
            statement.setInt(3, nhapKho.getSoLuongNhap());
            statement.setString(4, nhapKho.getTenSachNhap());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateNhapKho(NhapKho nhapKho) {
        String sql = "UPDATE nhap_kho SET ngayNhap = ?, soLuongNhap = ?, tenSachNhap = ? WHERE maNhapKho = ?";
        try (Connection conn = DAO.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1,nhapKho.getNgayNhap());
            statement.setInt(2, nhapKho.getSoLuongNhap());
            statement.setString(3, nhapKho.getTenSachNhap());
            statement.setString(4, nhapKho.getMaNhapKho());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteNhapKho(String maNhapKho) {
        String sql = "DELETE FROM nhap_kho WHERE maNhapKho = ?";
        try (Connection conn = DAO.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, maNhapKho);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
