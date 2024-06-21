package View;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import Model.NhapKho;

public class XuatNhapKhoPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTable table;
    private JLabel lblQLsach;
    private JButton btnThem;
    private JButton btnSua;
    private JLabel lblNewLabel;
    private DefaultTableModel dataModel;
    private ThemKho them;
    private SuaKho sua;
    private XoaSP xoa;
    private JButton btnTimKiem;
    private static Map<String, Integer> danhSachMaSachNhapKho = new HashMap<>();
    private SanPhamPanelView sanPhamPanel;
    private JLabel lblNewLabel_1;

    public XuatNhapKhoPanel(SanPhamPanelView sanPhamPanel) {
        this.sanPhamPanel = sanPhamPanel; // Nhận tham chiếu SanPhamPanel
        setBackground(new Color(187, 221, 255));
        setLayout(null);
        this.init();
        this.setVisible(true);
    }
    public JPanel getPanel() {
		return this;
	}

    public static boolean kiemTraMaSachTonTai(String maSach) {
        boolean tonTai = false;
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/quanlysach", "root", "221004");
            String sql = "SELECT COUNT(*) FROM them WHERE maSach = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, maSach);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                tonTai = rs.getInt(1) > 0;
            }
            rs.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tonTai;
    }

    public static String layTenSach(String maSach) {
        String tenSach = "";
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/quanlysach", "root", "221004");
            String sql = "SELECT tenSach FROM them WHERE maSach = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, maSach);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                tenSach = rs.getString("tenSach");
            }
            rs.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tenSach;
    }

    public static int layGiaTien(String maSach) {
        int giaTien = 0;
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/quanlysach", "root", "221004");
            String sql = "SELECT giaBan FROM them WHERE maSach = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, maSach);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                giaTien = rs.getInt("giaBan");
            }
            rs.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return giaTien;
    }

    public void init() {
        this.setBounds(275, 44, 648, 426);
        this.setForeground(new Color(0, 128, 255));
        this.setVisible(true);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(18, 129, 620, 195);
        add(scrollPane);

        lblQLsach = new JLabel("QUẢN LÝ SÁCH");
        lblQLsach.setForeground(new Color(255, 128, 64));
        lblQLsach.setBackground(new Color(255, 128, 64));
        lblQLsach.setFont(new Font("Tahoma", Font.PLAIN, 16));
        add(lblQLsach);

        table = new JTable();
        table.setFont(new Font("Tahoma", Font.PLAIN, 14));
        dataModel = new DefaultTableModel(
            new Object[][] {},
            new String[] {
                "Mã Sách", "Ngày Nhập", "Số Lượng Nhập", "Tên Sách Nhập"
            }
        );
        table.setModel(dataModel);
        scrollPane.setViewportView(table);

        btnThem = new JButton("Thêm");
        btnThem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                themKho();
            }
        });
        btnThem.setFont(new Font("Tahoma", Font.PLAIN, 16));
        btnThem.setBounds(44, 347, 120, 43);
        add(btnThem);

        btnSua = new JButton("Cập Nhật");
        btnSua.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int[] selectedRows = table.getSelectedRows();

                if (selectedRows.length == 1) {
                    int selectedRow = selectedRows[0];
                    String maNK = (String) dataModel.getValueAt(selectedRow, 0);
                    String ngayNhapStr = (String) dataModel.getValueAt(selectedRow,1);
                    int soLuongNhap = (int) dataModel.getValueAt(selectedRow, 2); 
                    String tenSachNhap = (String) dataModel.getValueAt(selectedRow, 3);
                    
                    NhapKho nhapkho = new NhapKho(maNK, ngayNhapStr, soLuongNhap, tenSachNhap);
                    sua = new SuaKho("Sửa Kho", nhapkho, dataModel, XuatNhapKhoPanel.this, sanPhamPanel, soLuongNhap); // Truyền SanPhamPanel
                    sua.setVisible(true);
                } else if (selectedRows.length > 1) {
                    JOptionPane.showMessageDialog(XuatNhapKhoPanel.this, "Chỉ được chọn một sản phẩm để cập nhật.");
                } else {
                    JOptionPane.showMessageDialog(XuatNhapKhoPanel.this, "Vui lòng chọn một sản phẩm để cập nhật.");
                }
            }
        });

        btnSua.setFont(new Font("Tahoma", Font.PLAIN, 16));
        btnSua.setBounds(470, 347, 120, 43);
        add(btnSua);

        lblNewLabel = new JLabel("Tìm Kiếm: ");
        lblNewLabel.setForeground(new Color(0, 119, 238));
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblNewLabel.setBounds(44, 85, 93, 34);
        add(lblNewLabel);

        JTextField tfTimKiem = new JTextField();
        tfTimKiem.setBounds(145, 85, 271, 34);
        add(tfTimKiem);
        tfTimKiem.setColumns(10);
        tfTimKiem.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void removeUpdate(DocumentEvent e) {
                filter();
            }
            
            @Override
            public void insertUpdate(DocumentEvent e) {
                filter();
            }
            
            @Override
            public void changedUpdate(DocumentEvent e) {
                // Not used
            }
            
            private void filter() {
                String keyword = tfTimKiem.getText().trim();
                if (!keyword.isEmpty()) {
                    highlightRow(table, keyword);
                } else {
                    display();
                }
            }

        });
        
        btnTimKiem = new JButton("Tìm Kiếm");
        btnTimKiem.setFont(new Font("Tahoma", Font.PLAIN, 16));
        btnTimKiem.setBounds(448, 85, 113, 34);
        add(btnTimKiem);
        
        lblNewLabel_1 = new JLabel("QUẢN LÝ KHO");
        lblNewLabel_1.setForeground(new Color(0, 119, 238));
        lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 19));
        lblNewLabel_1.setBounds(267, 29, 163, 34);
        add(lblNewLabel_1);
        btnTimKiem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String keyword = tfTimKiem.getText().trim();
                if (!keyword.isEmpty()) {
                    highlightRow(table, keyword);
                } else {
                    display();
                    JOptionPane.showMessageDialog(XuatNhapKhoPanel.this, "Vui lòng nhập từ khóa tìm kiếm.");
                }
            }
        });

        xoa = new XoaSP();
        this.display();
        this.setVisible(true);
    }

    private void TimKiem(String keyword) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/quanlysach", "root", "221004");
            String sql = "SELECT maSachNhapKho, ngayNhap, soLuongNhap, tenSachNhap FROM nhap_kho";
            PreparedStatement statement = connection.prepareStatement(sql);
            for (int i = 1; i <= 4; i++) {
                statement.setString(i, "%" + keyword + "%");
            }
            ResultSet rs = statement.executeQuery();

            DefaultTableModel qldtvsmodel_table = (DefaultTableModel) table.getModel();
            qldtvsmodel_table.setRowCount(0);

            int count = 0;
            while (rs.next()) {
                String maNhapKho = rs.getString("maSachNhapKho");
                String ngayNhap = rs.getString("ngayNhap");
                int soLuongNhap = rs.getInt("soLuongNhap");
                String tenSachNhap = rs.getString("tenSachNhap");

                qldtvsmodel_table.addRow(new Object[]{maNhapKho, ngayNhap, soLuongNhap, tenSachNhap});
                count++;
            }

            if (count == 0) {
                JOptionPane.showMessageDialog(null, "Không tìm thấy sản phẩm phù hợp.");
            }

            rs.close();
            statement.close();
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void highlightRow(JTable table, String keyword) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);
        
        if (keyword.isEmpty()) {
            table.setRowSorter(null); // Reset the row sorter if the keyword is empty
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + keyword));
        }
    }

    public void display() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/quanlysach", "root", "221004");
            String sql = "SELECT maSachNhapKho, ngayNhap, soLuongNhap, tenSachNhap FROM nhap_kho";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();

            DefaultTableModel qldtvsmodel_table = (DefaultTableModel) table.getModel();
            qldtvsmodel_table.setRowCount(0);

            while (rs.next()) {
                String maSachNhapKho = rs.getString("maSachNhapKho");
                String ngayNhap = rs.getString("ngayNhap");
                int soLuongNhap = rs.getInt("soLuongNhap");
                String nhaCungCap = rs.getString("tenSachNhap");

                qldtvsmodel_table.addRow(new Object[]{maSachNhapKho, ngayNhap, soLuongNhap, nhaCungCap});
            }

            rs.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi tải dữ liệu từ cơ sở dữ liệu", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void capNhatSoLuongTonKho(String maSanPham, int soLuongBan) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/quanlysach", "root", "221004");
            String sql = "UPDATE nhap_kho SET soLuongNhap = soLuongNhap - ? WHERE maSachNhapKho = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, soLuongBan);
            statement.setString(2, maSanPham);
            statement.executeUpdate();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void themKho() {
        them = new ThemKho("Thêm Kho", new ArrayList<NhapKho>(), (DefaultTableModel) table.getModel(), this);
        them.setVisible(true);
    }
}