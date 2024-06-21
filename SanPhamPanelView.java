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
import java.text.DecimalFormat;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import Model.HangHoa;
import java.util.ArrayList;

public class SanPhamPanelView extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTable table;
    private JLabel lblQLsach;
    private JButton btnThem;
    private JButton btnSua;
    private JButton btnXoa;
    private JLabel lblNewLabel;
    private DefaultTableModel dataModel;
    private ThemSP them;
    private SuaSP sua;
    private XoaSP xoa;
    private JButton btnTimKiem;
    public SuaKho suaKho;
    private JLabel lblNewLabel_1;

    public SanPhamPanelView() {
        setBackground(new Color(255, 255, 187));
        setLayout(null);
        this.init();
    }
    public JPanel getPanel() {
		return this;
	}

    public void init() {
//        this.setBounds(275, 44, 648, 426);
        this.setBounds(274, 56, 653, 415);
        this.setForeground(new Color(0, 128, 255));

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(14, 115, 620, 195);
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
                "Ma Sach", "Ten Sach", "Tac Gia", "The Loai", "Gia Sach", "So Luong Ton Kho"
            }
        );
        table.setModel(dataModel);
        scrollPane.setViewportView(table);

        btnThem = new JButton("Thêm");
        btnThem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                themSanPham();
            }
        });
        btnThem.setFont(new Font("Tahoma", Font.PLAIN, 16));
        btnThem.setBounds(41, 330, 120, 43);
        add(btnThem);

        btnSua = new JButton("Cập Nhật");
        btnSua.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int[] selectedRows = table.getSelectedRows(); 

                if (selectedRows.length == 1) { 
                    int selectedRow = selectedRows[0];
                    String maSach = (String) dataModel.getValueAt(selectedRow, 0);
                    String tenSach = (String) dataModel.getValueAt(selectedRow, 1);
                    String tacGia = (String) dataModel.getValueAt(selectedRow, 2);
                    String theLoai = (String) dataModel.getValueAt(selectedRow, 3);
                    double giaBan = Double.parseDouble(dataModel.getValueAt(selectedRow, 4).toString());
                    int soLuongTonKho = Integer.parseInt(dataModel.getValueAt(selectedRow, 5).toString());

                    HangHoa hangHoa = new HangHoa(maSach, tenSach, tacGia, theLoai, giaBan, soLuongTonKho);
                    sua = new SuaSP("Cập nhật sản phẩm", hangHoa, dataModel, SanPhamPanelView.this);
                    sua.setVisible(true);
                } else if (selectedRows.length > 1) { 
                    JOptionPane.showMessageDialog(SanPhamPanelView.this, "Chỉ được chọn một sản phẩm để cập nhật.");
                } else { 
                    JOptionPane.showMessageDialog(SanPhamPanelView.this, "Vui lòng chọn một sản phẩm để cập nhật.");
                }
            }
        });

        btnSua.setFont(new Font("Tahoma", Font.PLAIN, 16));
        btnSua.setBounds(244, 330, 120, 43);
        add(btnSua);

        btnXoa = new JButton("Xóa");
        btnXoa.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                xoaSanPham();
            }
        });
        btnXoa.setFont(new Font("Tahoma", Font.PLAIN, 16));
        btnXoa.setBounds(445, 330, 120, 43);
        add(btnXoa);
        
        JTextField tfTimKiem = new JTextField();
        tfTimKiem.setBounds(147, 66, 271, 34);
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
                // Không dùng
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
        
        lblNewLabel = new JLabel("Tìm Kiếm: ");
        lblNewLabel.setForeground(new Color(255, 128, 64));
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblNewLabel.setBounds(44, 66, 93, 34);
        add(lblNewLabel);

        btnTimKiem = new JButton("Tìm Kiếm");
        btnTimKiem.setFont(new Font("Tahoma", Font.PLAIN, 16));
        btnTimKiem.setBounds(445, 62, 120, 43);
        add(btnTimKiem);
        
        lblNewLabel_1 = new JLabel("TRANG SẢN PHẨM");
        lblNewLabel_1.setForeground(new Color(255, 128, 64));
        lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 19));
        lblNewLabel_1.setBounds(244, 21, 184, 35);
        add(lblNewLabel_1);
        btnTimKiem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String keyword = tfTimKiem.getText().trim(); 
                if (!keyword.isEmpty()) { 
                    TimKiem(keyword); 
                } else {
                    JOptionPane.showMessageDialog(SanPhamPanelView.this, "Vui lòng nhập từ khóa tìm kiếm.");
                }
            }
        });
        
        xoa = new XoaSP();
        this.display();
        this.setVisible(true);
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

    private void xoaSanPham() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            String maSach = (String) dataModel.getValueAt(selectedRow, 0);
            String tenSach = (String) dataModel.getValueAt(selectedRow, 1);
            String tacGia = (String) dataModel.getValueAt(selectedRow, 2);
            String theLoai = (String) dataModel.getValueAt(selectedRow, 3);
            double giaBan = Double.parseDouble(dataModel.getValueAt(selectedRow, 4).toString());
            int soLuongTonKho = Integer.parseInt(dataModel.getValueAt(selectedRow, 5).toString());

            HangHoa hangHoa = new HangHoa(maSach, tenSach, tacGia, theLoai, giaBan, soLuongTonKho);

            int option = JOptionPane.showConfirmDialog(
                null,
                "Bạn có chắc chắn muốn xóa sản phẩm này?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION
            );

            if (option == JOptionPane.YES_OPTION) {
                if (xoa.XoaHangHoaToDatabase(maSach)) {
                    dataModel.removeRow(selectedRow);
                    xoa.sendProductToServer(hangHoa);
                    JOptionPane.showMessageDialog(SanPhamPanelView.this, "Xóa sản phẩm thành công.");
                } else {
                    JOptionPane.showMessageDialog(SanPhamPanelView.this, "Xóa sản phẩm thất bại.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(SanPhamPanelView.this, "Vui lòng chọn một sản phẩm để xóa.");
        }
    }

    private void TimKiem(String keyword) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/quanlysach", "root", "221004");
            String sql = "SELECT maSach, tenSach, tacGia, theLoai, giaBan, soLuongTonKho FROM them WHERE maSach LIKE ? OR tenSach LIKE ? OR tacGia LIKE ? OR theLoai LIKE ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            for (int i = 1; i <= 4; i++) {
                statement.setString(i, "%" + keyword + "%");
            }
            ResultSet rs = statement.executeQuery();

            DefaultTableModel qldtvsmodel_table = (DefaultTableModel) table.getModel();
            qldtvsmodel_table.setRowCount(0);

            int count = 0;
            while (rs.next()) {
                String maSach = rs.getString("maSach");
                String tenSach = rs.getString("tenSach");
                String tacGia = rs.getString("tacGia");
                String theLoai = rs.getString("theLoai");
                double gia = rs.getDouble("giaBan");
                int soLuongTonKho = rs.getInt("soLuongTonKho");

                qldtvsmodel_table.addRow(new Object[]{maSach, tenSach, tacGia, theLoai, gia, soLuongTonKho});
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

    public void display() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/quanlysach", "root", "221004");
            String sql = "SELECT maSach, tenSach, tacGia, theLoai, giaBan, soLuongTonKho FROM them";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();

            DefaultTableModel qldtvsmodel_table = (DefaultTableModel) table.getModel();
            qldtvsmodel_table.setRowCount(0);

            while (rs.next()) {
                String maSach = rs.getString("maSach");
                String tenSach = rs.getString("tenSach");
                String tacGia = rs.getString("tacGia");
                String theLoai = rs.getString("theLoai");
                double gia = rs.getDouble("giaBan");
                DecimalFormat format = new DecimalFormat("0.#");
                int soLuongTonKho = rs.getInt("soLuongTonKho");

                qldtvsmodel_table.addRow(new Object[]{maSach, tenSach, tacGia, theLoai, format.format(gia), soLuongTonKho});
            }

            rs.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateProductQuantity(String maSach, int soLuongNhap) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            if (model.getValueAt(i, 0).equals(maSach)) {
                int currentQuantity = Integer.parseInt(model.getValueAt(i, 5).toString());
                
                try {
                    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/quanlysach", "root", "221004");
                    String sql = "UPDATE them SET soLuongTonKho = ? WHERE maSach = ?;";
                    PreparedStatement statement = connection.prepareStatement(sql);
                    statement.setInt(1, soLuongNhap+currentQuantity);
                    statement.setString(2, maSach);
                    
                    statement.execute();
                    
                    statement.close();
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public void themSanPham() {
        them = new ThemSP("Thêm Sản Phẩm", new ArrayList<HangHoa>(), (DefaultTableModel) table.getModel(), this);
        them.setVisible(true);
    }
}
