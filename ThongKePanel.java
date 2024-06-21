package View;
import java.awt.Color;
import java.awt.Font;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ThongKePanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTable tableThongKe;
    private JLabel lblSUM;

    public ThongKePanel() {
        setBackground(new Color(185, 255, 203));
        setLayout(null);
        init();
        Tong();
    }
    public JPanel getPanel() {
		return this;
	}
    private void init() {
        setBounds(275, 44, 648, 426);
        setForeground(new Color(0, 128, 255));

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(36, 149, 580, 169);
        add(scrollPane);

        tableThongKe = new JTable();
        tableThongKe.setFont(new Font("Tahoma", Font.PLAIN, 14));
        tableThongKe.setModel(new DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "Mã Sách", "Tên Sách", "Số lượng bán", "Thành Tiền", "Ngày bán"
                }
        ));
        scrollPane.setViewportView(tableThongKe);

        lblSUM = new JLabel("Tổng doanh thu:");
        lblSUM.setForeground(new Color(0, 128, 0));
        lblSUM.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblSUM.setBounds(34, 345, 349, 30);
        add(lblSUM);

        JLabel lblSearch = new JLabel("Tìm kiếm:");
        lblSearch.setForeground(new Color(0, 128, 0));
        lblSearch.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblSearch.setBounds(36, 83, 132, 30);
        add(lblSearch);

        JTextField tfTimKiem = new JTextField();
        tfTimKiem.setBounds(168, 86, 186, 30);
        add(tfTimKiem);
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
            }
            private void filter() {
                String keyword = tfTimKiem.getText().trim();
                if (!keyword.isEmpty()) {
                    highlightRow(tableThongKe, keyword);
                } else {
                    taiThongTinTuCSDL();
                }
            }

        });
     
        JButton btnSearch = new JButton("Tìm kiếm");
        btnSearch.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		String keyword = tfTimKiem.getText().trim(); 
                if (!keyword.isEmpty()) { 
                    TimKiem(keyword); 
                } else {
                    JOptionPane.showMessageDialog(ThongKePanel.this, "Vui lòng nhập từ khóa tìm kiếm.");
                }
        	}
        });
        btnSearch.setFont(new Font("Tahoma", Font.PLAIN, 16));
        btnSearch.setBounds(401, 86, 115, 30);
        add(btnSearch);
        
        JLabel lblNewLabel = new JLabel("THỐNG KÊ");
        lblNewLabel.setForeground(new Color(0, 128, 0));
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 19));
        lblNewLabel.setBounds(275, 35, 115, 24);
        add(lblNewLabel);
        
        taiThongTinTuCSDL();
        setVisible(true);
    }
    private void highlightRow(JTable tableThongKe, String keyword) {
        DefaultTableModel model = (DefaultTableModel) tableThongKe.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        tableThongKe.setRowSorter(sorter);
        
        if (keyword.isEmpty()) {
        	tableThongKe.setRowSorter(null); 
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + keyword));
        }
    }
    private void TimKiem(String keyword) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/quanlysach", "root", "221004");
            String sql = "SELECT ma_san_pham, ten_san_pham, so_luong_ban, gia_ban, ngay_ban FROM sach_da_ban WHERE ma_san_pham LIKE ? OR ten_san_pham LIKE ? OR ngay_ban LIKE ? ";
            PreparedStatement statement = connection.prepareStatement(sql);
            for (int i = 1; i <= 3; i++) {
                statement.setString(i, "%" + keyword + "%");
            }
            ResultSet rs = statement.executeQuery();

            DefaultTableModel qldtvsmodel_table = (DefaultTableModel) tableThongKe.getModel();
            qldtvsmodel_table.setRowCount(0);

            int count = 0;
            while (rs.next()) {
            	 String maSach = rs.getString("ma_san_pham");
                 String tenSach = rs.getString("ten_san_pham");
                 int soLuongBan = rs.getInt("so_luong_ban");
                 int giaBan = rs.getInt("gia_ban");
                 Date ngayBan = rs.getDate("ngay_ban");

                qldtvsmodel_table.addRow(new Object[]{maSach, tenSach, soLuongBan, giaBan, ngayBan});
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


    private void Tong() {
        DecimalFormat x = new DecimalFormat("###,###,###");
        float tong = 0;
        DefaultTableModel table = (DefaultTableModel) tableThongKe.getModel();

        for (int i_row = 0; i_row < table.getRowCount(); i_row++) {
            float thanhTien = Float.parseFloat(table.getValueAt(i_row, 3).toString());
            tong += thanhTien;
        }

        lblSUM.setText("Tổng doanh thu: " + x.format(tong) + " VND");
    }

    private void capNhatTongDoanhThu() {
        Tong();
    }
    private boolean daTonTai(String maSach, String ngayBan) {
        DefaultTableModel model = (DefaultTableModel) tableThongKe.getModel();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < model.getRowCount(); i++) {
            String maSachDaBan = (String) model.getValueAt(i, 0);
            String ngayBanDaBan = sdf.format((java.util.Date) model.getValueAt(i, 4)); 
            if (maSach.equals(maSachDaBan) && ngayBan.equals(ngayBanDaBan)) {
                return true;
            }
        }
        return false;
    }


    public void capNhatBangThongKe(String maSach, String tenSach, int soLuongBan, int giaBan, String ngayBan) {
        DefaultTableModel model = (DefaultTableModel) tableThongKe.getModel();
        if (daTonTai(maSach, ngayBan)) {
            for (int i = 0; i < model.getRowCount(); i++) {
                String maSachDaBan = (String) model.getValueAt(i, 0);
                String ngayBanDaBan = ((java.sql.Date) model.getValueAt(i, 4)).toString();
                if (maSach.equals(maSachDaBan) && ngayBan.equals(ngayBanDaBan)) {
                    int soLuongCu = (int) model.getValueAt(i, 2);
                    int giaCu = (int) model.getValueAt(i, 3);
                    model.setValueAt(soLuongCu + soLuongBan, i, 2);
                    model.setValueAt(giaCu + giaBan, i, 3); 
                    break;
                }
            }
        } else {
            model.addRow(new Object[]{maSach, tenSach, soLuongBan, giaBan, ngayBan}); 
        }
        capNhatTongDoanhThu();
        luuSachDaBan(maSach, tenSach, soLuongBan, giaBan, ngayBan);
    }

    private void luuSachDaBan(String maSach, String tenSach, int soLuongBan, int giaBan, String ngayBan) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/quanlysach", "root", "221004")) {
            String insertQuery = "INSERT INTO sach_da_ban (ma_san_pham, ten_san_pham, so_luong_ban, gia_ban, ngay_ban) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(insertQuery)) {
                statement.setString(1, maSach);
                statement.setString(2, tenSach);
                statement.setInt(3, soLuongBan);
                statement.setInt(4, giaBan);
                statement.setDate(5, java.sql.Date.valueOf(ngayBan));
                statement.executeUpdate();
            }
        } catch (SQLException e) {
        	
        }
    }

    private void taiThongTinTuCSDL() {
        DefaultTableModel model = (DefaultTableModel) tableThongKe.getModel();
        model.setRowCount(0); 

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/quanlysach", "root", "221004")) {
            String selectQuery = "SELECT * FROM sach_da_ban";
            try (PreparedStatement statement = connection.prepareStatement(selectQuery)) {
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    String maSach = resultSet.getString("ma_san_pham");
                    String tenSach = resultSet.getString("ten_san_pham");
                    int soLuongBan = resultSet.getInt("so_luong_ban");
                    int giaBan = resultSet.getInt("gia_ban");
                    Date ngayBan = resultSet.getDate("ngay_ban");

                    model.addRow(new Object[]{maSach, tenSach, soLuongBan, giaBan, ngayBan});
                }
            }
        } catch (SQLException e) {

        }
    }
}
