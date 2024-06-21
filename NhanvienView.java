package View;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class NhanvienView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
//	private JPanel panel;
	private JButton btnQLSP;
	private JButton btnQlKho;
	private JButton btnBanHang;
	private JPanel contentPanel;
	private JPanel currentPanel;
	
	private ThongKePanel thongKePanel;
	
	private SanPhamPanelView sanPhamPanel = new SanPhamPanelView();
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					NhanvienView frame = new NhanvienView();
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
	

    
    public NhanvienView() {
    	
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 951, 587);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        thongKePanel = new ThongKePanel(); 
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(10, 41, 230, 430);
		contentPane.add(panel_1);
		panel_1.setLayout(null);
		
		btnQLSP = new JButton("QUẢN LÝ SẢN PHẨM");
		btnQLSP.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						SwingUtilities.invokeLater(new Runnable() {
							@Override
							public void run() {
								switchPanel(new SanPhamPanelView().getPanel());
							}
						});
					}
				}).start();
			}
		});

		btnQLSP.setBounds(10, 74, 203, 42);
		panel_1.add(btnQLSP);		
		btnQLSP.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		btnBanHang = new JButton("BÁN HÀNG");
		btnBanHang.setBounds(10, 187, 203, 42);
		panel_1.add(btnBanHang);
		btnBanHang.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						SwingUtilities.invokeLater(new Runnable() {
							@Override
							public void run() {
								switchPanel(new BanSachPanel(thongKePanel).getPanel());
							}
						});
					}
				}).start();
			}
		});
		btnBanHang.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		btnQlKho = new JButton("QUẢN LÝ KHO");
		btnQlKho.setBounds(10, 315, 203, 42);
		panel_1.add(btnQlKho);
		btnQlKho.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						SwingUtilities.invokeLater(new Runnable() {
							@Override
							public void run() {
								switchPanel(new XuatNhapKhoPanel(sanPhamPanel).getPanel());
							}
						});
					}
				}).start();
			}
		});
		btnQlKho.setFont(new Font("Tahoma", Font.PLAIN, 16));
		contentPanel = new JPanel(null);
		contentPanel.setBounds(274, 56, 653, 415);
		getContentPane().add(contentPanel);
		
		SanPhamPanelView sanPhamPanelView = new SanPhamPanelView();
		sanPhamPanelView.setSize(646, 415);
		sanPhamPanelView.setLocation(0, 0);
		currentPanel = sanPhamPanelView.getPanel();
		currentPanel.setBounds(0, 0, contentPanel.getWidth(), contentPanel.getHeight());
		contentPanel.add(currentPanel);
		
        
        this.setVisible(true);
    }
 
	private void switchPanel(JPanel newPanel) {
		if (currentPanel != null) {
			contentPanel.remove(currentPanel);
		}
		currentPanel = newPanel;
		currentPanel.setBounds(0, 0, contentPanel.getWidth(), contentPanel.getHeight());
		contentPanel.add(currentPanel);
		contentPanel.revalidate();
		contentPanel.repaint();
	}
}
