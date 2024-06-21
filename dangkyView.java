package View;

import java.awt.EventQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;
import DAO.UserDAO;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class dangkyView extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField tfTenTaiKhoan;
    private JPasswordField tPMatKhau;
    private JPasswordField tPNhapLaiMK;
    private JButton btnTao;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    dangkyView frame = new dangkyView();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public JTextField getTfTenTaiKhoan() {
        return tfTenTaiKhoan;
    }

    public void setTfTenTaiKhoan(JTextField tfTenTaiKhoan) {
        this.tfTenTaiKhoan = tfTenTaiKhoan;
    }

    public JPasswordField gettPMatKhau() {
        return tPMatKhau;
    }

    public void settPMatKhau(JPasswordField tPMatKhau) {
        this.tPMatKhau = tPMatKhau;
    }

    public JPasswordField gettPNhapLaiMK() {
        return tPNhapLaiMK;
    }

    public void settPNhapLaiMK(JPasswordField tPNhapLaiMK) {
        this.tPNhapLaiMK = tPNhapLaiMK;
    }

    /**
     * Create the frame.
     */
    public dangkyView() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 535, 360);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("Thêm tài khoản");
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblNewLabel.setBounds(201, 3, 155, 55);
        contentPane.add(lblNewLabel);

        JLabel lblTenTaiKhoan = new JLabel("Số điện thoại:");
        lblTenTaiKhoan.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblTenTaiKhoan.setBounds(46, 68, 118, 39);
        contentPane.add(lblTenTaiKhoan);

        JLabel lblMatKhau = new JLabel("Mật khẩu:");
        lblMatKhau.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblMatKhau.setBounds(46, 129, 118, 39);
        contentPane.add(lblMatKhau);

        JLabel lblNhapLaiMK = new JLabel("Nhập lại mật khẩu:");
        lblNhapLaiMK.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblNhapLaiMK.setBounds(46, 191, 155, 39);
        contentPane.add(lblNhapLaiMK);

        tfTenTaiKhoan = new JTextField();
        tfTenTaiKhoan.setBounds(236, 68, 205, 39);
        contentPane.add(tfTenTaiKhoan);
        tfTenTaiKhoan.setColumns(10);

        tPMatKhau = new JPasswordField();
        tPMatKhau.setColumns(10);
        tPMatKhau.setBounds(236, 129, 205, 39);
        contentPane.add(tPMatKhau);

        tPNhapLaiMK = new JPasswordField();
        tPNhapLaiMK.setColumns(10);
        tPNhapLaiMK.setBounds(236, 191, 205, 39);
        contentPane.add(tPNhapLaiMK);

        btnTao = new JButton("Tạo tài khoản");
        btnTao.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = getTfTenTaiKhoan().getText();
                String password = new String(gettPMatKhau().getPassword());
                String nhapLaiPass = new String(gettPNhapLaiMK().getPassword());
                handleRegister(username, password, nhapLaiPass);
            }
        });
        btnTao.setFont(new Font("Tahoma", Font.BOLD, 16));
        btnTao.setBounds(201, 259, 148, 39);
        contentPane.add(btnTao);

        this.setVisible(true);
    }

    public void handleRegister(String username, String password, String nhapLaiPass) {
        if (!password.equals(nhapLaiPass)) {
            JOptionPane.showMessageDialog(this, "Mật khẩu nhập lại không khớp!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!isValidPhoneNumber(username)) {
            JOptionPane.showMessageDialog(this, "Số điện thoại không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (password.length() < 5) {
            JOptionPane.showMessageDialog(this, "Mật khẩu phải có ít nhất 5 ký tự!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (UserDAO.isUsernameTaken(username)) {
            JOptionPane.showMessageDialog(this, "Số điện thoại này đã được sử dụng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(password);

        if (UserDAO.registerUser(username, hashedPassword)) {
            JOptionPane.showMessageDialog(this, "Đăng ký tài khoản thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            this.dispose();
            new loginView();
        } else {
            JOptionPane.showMessageDialog(this, "Đăng ký tài khoản thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        String regex = "^\\d{10}$"; // Ví dụ, số điện thoại hợp lệ là chuỗi số có độ dài 10
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }
}
