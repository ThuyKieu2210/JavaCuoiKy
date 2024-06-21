package View;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import DAO.UserDAO;

public class loginView extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField tfTentaikhoan;
    private JPasswordField tPMatkhau;
    private JButton btnLogin;
    private JButton btnDangKy;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    loginView frame = new loginView();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public JTextField getTfTentaikhoan() {
		return tfTentaikhoan;
	}

	public void setTfTentaikhoan(JTextField tfTentaikhoan) {
		this.tfTentaikhoan = tfTentaikhoan;
	}

	public JPasswordField gettPMatkhau() {
		return tPMatkhau;
	}

	public void settPMatkhau(JPasswordField tPMatkhau) {
		this.tPMatkhau = tPMatkhau;
	}

    public loginView() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 578, 283);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lbTitle = new JLabel("ĐĂNG NHẬP");
        lbTitle.setForeground(new Color(0, 0, 255));
        lbTitle.setVerticalAlignment(SwingConstants.TOP);
        lbTitle.setFont(new Font("Tahoma", Font.PLAIN, 20));
        lbTitle.setBounds(230, 10, 134, 33);
        contentPane.add(lbTitle);

        JLabel lbTentaikhoan = new JLabel("Tên tài khoản:");
        lbTentaikhoan.setForeground(new Color(0, 0, 255));
        lbTentaikhoan.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lbTentaikhoan.setBounds(10, 53, 125, 33);
        contentPane.add(lbTentaikhoan);

        JLabel lbMatkhau = new JLabel("Mật khẩu:");
        lbMatkhau.setForeground(new Color(0, 0, 255));
        lbMatkhau.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lbMatkhau.setBounds(10, 114, 125, 33);
        contentPane.add(lbMatkhau);

        tfTentaikhoan = new JTextField();
        tfTentaikhoan.setBounds(139, 53, 296, 33);
        contentPane.add(tfTentaikhoan);
        tfTentaikhoan.setColumns(10);

        tPMatkhau = new JPasswordField();
        tPMatkhau.setColumns(10);
        tPMatkhau.setBounds(139, 110, 296, 33);
        contentPane.add(tPMatkhau);

        btnLogin = new JButton("Đăng Nhập");
        btnLogin.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
                String username = tfTentaikhoan.getText();
                String password = new String(tPMatkhau.getPassword());
                if (UserDAO.validateLogin(username, password)) {
                    JOptionPane.showMessageDialog(null, "Đăng nhập thành công!");
                    dispose();
                    new TrangChuView();
                } else {
                    JOptionPane.showMessageDialog(null, "Đăng nhập thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        btnLogin.setForeground(new Color(0, 0, 255));
        btnLogin.setFont(new Font("Tahoma", Font.PLAIN, 16));
        btnLogin.setBounds(384, 166, 125, 44);
        contentPane.add(btnLogin);
        
        btnDangKy = new JButton("Đăng ký");
        btnDangKy.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		new dangkyView();
        	}
        	
        });
        btnDangKy.setForeground(Color.BLUE);
        btnDangKy.setFont(new Font("Tahoma", Font.PLAIN, 16));
        btnDangKy.setBounds(64, 166, 155, 44);
        contentPane.add(btnDangKy);

        this.setLocationRelativeTo(null);
        this.setVisible(true);
    
    }

    public void xoaFrame() {
        this.dispose();
    }

	
}
