package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;



public class AdminView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel panel;
	private JButton btnQLSP;
	private JButton btnQlKho;
	private JButton btnQLNV;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AdminView frame = new AdminView();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public AdminView() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 951, 587);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

//        
//        ActionListener action = new TrangChuAdmin(this);
//        
        btnQLSP = new JButton("QUẢN LÝ SẢN PHẨM");
//        btnQLSP.addActionListener(action);s
        
        btnQLSP.setFont(new Font("Tahoma", Font.PLAIN, 16));
        btnQLSP.setBounds(33, 59, 203, 42);
        contentPane.add(btnQLSP);
        
        JButton btnQLDH = new JButton("QUẢN LÝ ĐƠN HÀNG");
        btnQLDH.setFont(new Font("Tahoma", Font.PLAIN, 16));
        btnQLDH.setBounds(33, 148, 203, 42);
        contentPane.add(btnQLDH);
        
        btnQlKho = new JButton("QUẢN LÝ KHO");
        btnQlKho.setFont(new Font("Tahoma", Font.PLAIN, 16));
        btnQlKho.setBounds(33, 241, 203, 42);
        contentPane.add(btnQlKho);
        
        btnQLNV = new JButton("QUẢN LÝ NHÂN VIÊN");
        btnQLNV.setFont(new Font("Tahoma", Font.PLAIN, 16));
        btnQLNV.setBounds(33, 353, 203, 42);
        contentPane.add(btnQLNV);
        
        panel = new JPanel();
		panel.setForeground(new Color(128, 255, 0));
		panel.setBounds(275, 44, 648, 426);
		contentPane.add(panel);
        
        this.setVisible(true);
	}
	public void xemThongTin() {
    	panel.removeAll();
		panel.setLayout(new BorderLayout());
        panel.removeAll();
        panel.add(new SanPhamPanelView());
        panel.revalidate();
        panel.repaint();
    }

}
