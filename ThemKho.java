package View;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import com.toedter.calendar.JDateChooser;

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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JTextField;
import javax.swing.JButton;


public class ThemKho extends JFrame implements ActionListener {

    public static final long serialVersionUID = 1L;
    public static Socket clientSocket;
    public static int PORT = 3310;
    public ObjectOutputStream out;
    JPanel contentPane;
    JLabel lblsLnhap;
    public JTextField tfPORT, tfMaNK, tfSLnhap, tftenSachNhap, tfNgayNhap;
    JDateChooser dateChooser;

    String clientName;
    JLabel lbltenSachNhap;
    JButton btnConnect;
    JButton btnOK;
    JButton btnCancel;
    private XuatNhapKhoPanel kho;
    HangHoa hhc;

    DefaultTableModel dataModel;
    ArrayList<NhapKho> nhapkho = new ArrayList<NhapKho>();
    HangHoa hhk = new HangHoa();
    private JLabel lblMaNK, lblNgayNhap;

    public ThemKho(String s, ArrayList<NhapKho> arrayList, DefaultTableModel model, XuatNhapKhoPanel kho) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 443, 313);
        setTitle("Thêm kho");
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        this.dataModel = model;
        contentPane.setLayout(null);

        lblMaNK = new JLabel("Mã Sách:");
        lblMaNK.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblMaNK.setBounds(10, 75, 142, 24);
        contentPane.add(lblMaNK);

        tfMaNK = new JTextField();
        tfMaNK.setColumns(10);
        tfMaNK.setBounds(162, 70, 250, 29);
        contentPane.add(tfMaNK);

        lblNgayNhap = new JLabel("Ngày Nhâp:");
        lblNgayNhap.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblNgayNhap.setBounds(10, 109, 142, 24);
        contentPane.add(lblNgayNhap);

        dateChooser = new JDateChooser();
        dateChooser.setBounds(162, 109, 250, 29);
        contentPane.add(dateChooser);

        lblsLnhap = new JLabel("Số Lượng Nhập:");
        lblsLnhap.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblsLnhap.setBounds(10, 143, 142, 24);
        contentPane.add(lblsLnhap);

        lbltenSachNhap = new JLabel("Tên Sách Nhập:");
        lbltenSachNhap.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lbltenSachNhap.setBounds(10, 177, 142, 24);
        contentPane.add(lbltenSachNhap);

        tfSLnhap = new JTextField();
        tfSLnhap.setColumns(10);
        tfSLnhap.setBounds(162, 143, 250, 29);
        contentPane.add(tfSLnhap);

        tftenSachNhap = new JTextField();
        tftenSachNhap.setColumns(10);
        tftenSachNhap.setBounds(162, 177, 250, 29);
        contentPane.add(tftenSachNhap);

        tfPORT = new JTextField();
        tfPORT.setBounds(162, 31, 152, 29);
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
        lblNhpPort.setBounds(10, 31, 109, 24);
        contentPane.add(lblNhpPort);

        this.kho = kho;

        setVisible(true);
    }
    private boolean isNgayNhapValid(Date ngayNhapDate) {
        Date currentDate = new Date();
        return !ngayNhapDate.after(currentDate);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("OK")) {
            if (tfPORT.getText().equals("3310")) {
                String maNK = tfMaNK.getText();
                Date ngayNhapDate = dateChooser.getDate();
                if (ngayNhapDate == null) {
                    JOptionPane.showMessageDialog(rootPane, "Vui lòng chọn ngày nhập!");
                    return;
                }
                if (!isNgayNhapValid(ngayNhapDate)) {
                    JOptionPane.showMessageDialog(rootPane, "Ngày nhập không hợp lệ!");
                    return;
                }
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String ngayNhap = dateFormat.format(ngayNhapDate);

                String tenSachNhap = tftenSachNhap.getText();

                try {
                    int soLuongNhap = Integer.parseInt(tfSLnhap.getText());
                    if (soLuongNhap < 0) {
                        JOptionPane.showMessageDialog(rootPane, "Số lượng nhập phải là số không âm!");
                        return;
                    }

                    NhapKho nhapkho = new NhapKho(maNK, ngayNhap, soLuongNhap, tenSachNhap);
                    if (addNhapKhoToDatabase(nhapkho)) {
                        sendProductToServer(nhapkho);
                        JOptionPane.showMessageDialog(rootPane, "Nhập kho thành công!");
                        this.kho.display();
                    } else {
                        JOptionPane.showMessageDialog(rootPane, "Nhập kho thất bại!");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(rootPane, "Số lượng nhập không đúng định dạng số nguyên!");
                }
            } else {
                JOptionPane.showMessageDialog(null, "PORT chưa đúng!");
            }
        } else if (e.getActionCommand().equals("Cancel")) {
            dispose();
        }
    }

    private boolean addNhapKhoToDatabase(NhapKho nhapkho) {
        String url = "jdbc:mysql://localhost:3306/quanlysach";
        String user = "root";
        String password = "221004";

        String insertSQL = "INSERT INTO nhap_kho (maSachNhapKho, ngayNhap, soLuongNhap, tenSachNhap) VALUES (?, ?, ?, ?)";
        if (checkIfMaSachExists(nhapkho.getMaNhapKho())) {
            JOptionPane.showMessageDialog(rootPane, "Mã sách đã tồn tại trong kho!");
            return false;
        }
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(insertSQL)) {
            statement.setString(1, nhapkho.getMaNhapKho());
            statement.setString(2, nhapkho.getNgayNhap());

            statement.setInt(3, nhapkho.getSoLuongNhap());
            statement.setString(4, nhapkho.getTenSachNhap());

            int rowsInserted = statement.executeUpdate();

            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    private boolean checkIfMaSachExists(String maSach) {
        String url = "jdbc:mysql://localhost:3306/quanlysach";
        String user = "root";
        String password = "221004";

        String query = "SELECT COUNT(*) FROM nhap_kho WHERE maSachNhapKho = ?";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, maSach);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void sendProductToServer(NhapKho nhapkho) {
        try {
            if (clientSocket == null || clientSocket.isClosed()) {
                clientSocket = new Socket("localhost", PORT);
                out = new ObjectOutputStream(clientSocket.getOutputStream());
            }
            out.writeObject(nhapkho);
            out.flush(); 
            addToLogs("Nhap Kho: " + nhapkho.getTenSachNhap() + "\nSo luong nhap: " + nhapkho.getSoLuongNhap());
        } catch (IOException ex) {
            addToLogs("Error sending product to server: " + ex.getMessage()); // Xử lý lỗi
        }
    }

    public void start() {
        try {
            PORT = 3310;
            clientName = "Nhap Kho";
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
