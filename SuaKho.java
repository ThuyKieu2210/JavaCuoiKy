package View;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import Model.HangHoa;
import Model.NhapKho;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Date;

import javax.swing.JTextField;
import javax.swing.JButton;

public class SuaKho extends JFrame implements ActionListener {

    public static final long serialVersionUID = 1L;
    public static Socket clientSocket;
    public static int PORT = 3310;
    public ObjectOutputStream out;
    JPanel contentPane;
    JLabel lblsLnhap;
    public JTextField tfPORT, tfMaNK, tfSLnhap, tftenSachNhap, tfNgayNhap;
    String clientName;
    JLabel lbltenSachNhap;
    JButton btnConnect;
    JButton btnOK;
    JButton btnCancel;
    private JLabel lblMaNK, lblNgayNhap;
    private XuatNhapKhoPanel kho;
    HangHoa hhc;
    private SanPhamPanelView sanPhamPanel;
    public int soLuongNhapCu;

    DefaultTableModel dataModel;
    ArrayList<NhapKho> nhapkho = new ArrayList<NhapKho>();
    HangHoa hhk = new HangHoa();
//    public SuaKho(SanPhamPanelView sanPhamPanel) {
//        this.sanPhamPanel = sanPhamPanel;
//        
//    }

    public SuaKho(String s, NhapKho nhapkho, DefaultTableModel model, XuatNhapKhoPanel kho, SanPhamPanelView sanPhamPanel, int soLuongNhapCu) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 443, 313);
        setTitle("Sửa sản phẩm");
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        
        setContentPane(contentPane);
        this.dataModel = model;
        this.kho = kho;
        this.sanPhamPanel = sanPhamPanel; // Initialize reference to SanPhamPanel
        this.soLuongNhapCu = soLuongNhapCu;
        contentPane.setLayout(null);

        lblMaNK = new JLabel("Mã Sách:");
        lblMaNK.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblMaNK.setBounds(10, 30, 142, 24);
        contentPane.add(lblMaNK);

        tfMaNK = new JTextField(String.valueOf(nhapkho.getMaNhapKho()));
        tfMaNK.setColumns(10);
        tfMaNK.setBounds(162, 25, 250, 29);
        tfMaNK.setEditable(false);
        contentPane.add(tfMaNK);

        lblNgayNhap = new JLabel("Ngày Nhập:");
        lblNgayNhap.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblNgayNhap.setBounds(10, 64, 142, 24);
        contentPane.add(lblNgayNhap);

        tfNgayNhap = new JTextField(String.valueOf(nhapkho.getNgayNhap()));
        tfNgayNhap.setBounds(162, 64, 250, 29);
        tfNgayNhap.setEditable(false);
        contentPane.add(tfNgayNhap);
        tfNgayNhap.setColumns(10);

        lblsLnhap = new JLabel("Số Lượng Nhập:");
        lblsLnhap.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblsLnhap.setBounds(10, 98, 142, 24);
        contentPane.add(lblsLnhap);

        lbltenSachNhap = new JLabel("Tên sách nhập:");
        lbltenSachNhap.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lbltenSachNhap.setBounds(10, 132, 142, 24);
        contentPane.add(lbltenSachNhap);

        tfSLnhap = new JTextField(String.valueOf(nhapkho.getSoLuongNhap()));
        tfSLnhap.setColumns(10);
        tfSLnhap.setBounds(162, 98, 250, 29);
        contentPane.add(tfSLnhap);

        tftenSachNhap = new JTextField(String.valueOf(nhapkho.getTenSachNhap()));
        tftenSachNhap.setColumns(10);
        tftenSachNhap.setBounds(162, 132, 250, 29);
        contentPane.add(tftenSachNhap);

        tfPORT = new JTextField();
        tfPORT.setBounds(162, 197, 152, 29);
        contentPane.add(tfPORT);
        tfPORT.setColumns(10);

        btnOK = new JButton("OK");
        btnOK.addActionListener(this);
        btnOK.setFont(new Font("Tahoma", Font.PLAIN, 16));
        btnOK.setBounds(145, 236, 59, 30);
        contentPane.add(btnOK);

        btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(this);
        btnCancel.setFont(new Font("Tahoma", Font.PLAIN, 16));
        btnCancel.setBounds(214, 236, 88, 30);
        contentPane.add(btnCancel);

        JLabel lblNhpPort = new JLabel("Nhập PORT:");
        lblNhpPort.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblNhpPort.setBounds(10, 197, 109, 24);
        contentPane.add(lblNhpPort);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("OK")) {
            if (tfPORT.getText().equals("3310")) {
                String maSachNhapKho = tfMaNK.getText();
                String ngayNhap = tfNgayNhap.getText();
                String tenSachNhap = tftenSachNhap.getText();
                try {
                    int soLuongNhap = Integer.parseInt(tfSLnhap.getText());
                    if (soLuongNhap < 0) {
                        JOptionPane.showMessageDialog(rootPane, "Số lượng nhập phải là số không âm!");
                        return;
                    }
                    
                    NhapKho nhapkho = new NhapKho(maSachNhapKho, ngayNhap, soLuongNhap, tenSachNhap, soLuongNhapCu);
                    if (suaKhoToDatabase(nhapkho)) {
                        suaKhoToServer(nhapkho);
                        JOptionPane.showMessageDialog(rootPane, "Chỉnh sửa kho thành công!");
                        this.kho.display();
                        
                        
                        this.sanPhamPanel.updateProductQuantity(nhapkho.getMaNhapKho(), nhapkho.getUpdateValue()); // Update SanPhamPanel
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(rootPane, "Chỉnh sửa kho thất bại!");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(rootPane, "Số lượng nhập phải là số!", "Lỗi định dạng số", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "PORT chưa đúng!");
            }
        } else if (e.getActionCommand().equals("Cancel")) {
            dispose();
        }
    }

    private boolean suaKhoToDatabase(NhapKho nhapkho) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/quanlysach", "root", "221004");
            String updateQuery = "UPDATE nhap_kho SET ngayNhap = ?, soLuongNhap = ?, tenSachNhap = ? WHERE maSachNhapKho = ?";
            PreparedStatement statement = connection.prepareStatement(updateQuery);

            statement.setString(1, nhapkho.getNgayNhap());
            statement.setInt(2, nhapkho.getSoLuongNhap());
            statement.setString(3, nhapkho.getTenSachNhap());
            statement.setString(4, nhapkho.getMaNhapKho());

            int rowsUpdated = statement.executeUpdate();
            connection.close();

            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void suaKhoToServer(NhapKho nhapkho) {
        try {
            if (clientSocket == null || clientSocket.isClosed()) {
                clientSocket = new Socket("localhost", PORT);
                out = new ObjectOutputStream(clientSocket.getOutputStream());
            }
            out.writeObject(nhapkho);
            out.flush();
            addToLogs("Da Sua Kho: " + nhapkho.getTenSachNhap());
        } catch (IOException ex) {
            addToLogs("Error sending product to server: " + ex.getMessage());
        }
    }

    public void start() {
        try {
            PORT = 3310;
            clientName = "Sua Kho";
            clientSocket = new Socket("localhost", PORT);

            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
            out.writeObject(hhc);
            new Thread(new Listener()).start();
        } catch (Exception err) {
            addToLogs("[ERROR] " + err.getLocalizedMessage());
        }
    }

    public void stop() {
        if (!clientSocket.isClosed()) {
            try {
                clientSocket.close();
            } catch (IOException e1) {
            }
        }
    }

    public static void addToLogs(String message) {
        System.out.printf("%s %s\n", ServerUI.formatter.format(new Date()), message);
    }

    private static class Listener implements Runnable {
        private BufferedReader in;

        @Override
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String read;
                for (;;) {
                    read = in.readLine();
                    if (read != null && !(read.isEmpty())) addToLogs(read);
                }
            } catch (IOException e) {
                return;
            }
        }
    }

    public void GuiData() {
        try {
            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
            out.writeObject(hhc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
