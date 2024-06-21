package View;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.toedter.calendar.JDateChooser;

import Model.HangHoa;

import java.util.Date;
import java.text.SimpleDateFormat;

public class BanSachPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    public static Socket clientSocket;
	public ObjectOutputStream out;
	public static int PORT = 3310;
	HangHoa hhc;
	String clientName;

    private JTextField tfMaSachBan;
    private JTextField tfTenSachBan;
    private JTextField tfSLban;
    private JLabel lblThanhTien;
    private JDateChooser dateChooser;
    private ThongKePanel thongKePanel;

    public BanSachPanel(ThongKePanel thongKePanel) {
    	setBackground(new Color(237, 209, 243));
        this.thongKePanel = thongKePanel;
        init();
        this.hhc = new HangHoa(); 

    }
    public JPanel getPanel() {
		return this;
	}

    private void init() {
        setLayout(null);
//        this.setBounds(275, 44, 648, 426);
        this.setBounds(274, 56, 653, 415);
        this.setForeground(new Color(0, 128, 255));
        this.setVisible(true);

        JLabel lblNewLabel = new JLabel("TRANG BÁN SÁCH");
        lblNewLabel.setForeground(new Color(215, 31, 224));
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 19));
        lblNewLabel.setBounds(234, 26, 188, 32);
        add(lblNewLabel);

        JLabel lblMaSachBan = new JLabel("Nhập Mã Sách:");
        lblMaSachBan.setForeground(new Color(215, 31, 224));
        lblMaSachBan.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblMaSachBan.setBounds(46, 76, 117, 32);
        add(lblMaSachBan);

        tfMaSachBan = new JTextField();
        tfMaSachBan.setBounds(217, 68, 205, 32);
        add(tfMaSachBan);
        tfMaSachBan.setColumns(10);

        JLabel lblTenSachBan = new JLabel("Tên Sách:");
        lblTenSachBan.setForeground(new Color(215, 31, 224));
        lblTenSachBan.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblTenSachBan.setBounds(46, 133, 117, 32);
        add(lblTenSachBan);

        tfTenSachBan = new JTextField();
        tfTenSachBan.setColumns(10);
        tfTenSachBan.setBounds(217, 133, 205, 32);
        tfTenSachBan.setEditable(false);
        add(tfTenSachBan);

        JLabel lblSLban = new JLabel("Số Lượng Bán:");
        lblSLban.setForeground(new Color(215, 31, 224));
        lblSLban.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblSLban.setBounds(46, 193, 117, 32);
        add(lblSLban);

        tfSLban = new JTextField();
        tfSLban.setColumns(10);
        tfSLban.setBounds(217, 193, 205, 32);
        add(tfSLban);

        JLabel lblNgayBan = new JLabel("Ngày Bán:");
        lblNgayBan.setForeground(new Color(215, 31, 224));
        lblNgayBan.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblNgayBan.setBounds(46, 249, 117, 32);
        add(lblNgayBan);

        dateChooser = new JDateChooser();
        dateChooser.setBounds(217, 249, 205, 32);
        add(dateChooser);

        JButton btnBanhang = new JButton("BÁN HÀNG");
        btnBanhang.setFont(new Font("Tahoma", Font.PLAIN, 16));
        btnBanhang.setBounds(421, 303, 157, 39);
        add(btnBanhang);

        JButton btnHoaDon = new JButton("Xuất Hóa Đơn");
        btnHoaDon.setFont(new Font("Tahoma", Font.PLAIN, 16));
        btnHoaDon.setBounds(421, 366, 157, 39);
        add(btnHoaDon);

        lblThanhTien = new JLabel("");
        lblThanhTien.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblThanhTien.setBounds(217, 303, 205, 37);
        add(lblThanhTien);

        tfMaSachBan.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String maSach = tfMaSachBan.getText().trim();
                if (XuatNhapKhoPanel.kiemTraMaSachTonTai(maSach)) {
                    String tenSach = XuatNhapKhoPanel.layTenSach(maSach);
                    tfTenSachBan.setText(tenSach);
                    tinhThanhTien();
                } else {
                    tfTenSachBan.setText("");
                    lblThanhTien.setText("");
                    tfSLban.setText("");
                    tfMaSachBan.requestFocus();
                    JOptionPane.showMessageDialog(null, "Mã sách không tồn tại.");
                }
            }
        });

        tfSLban.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tinhThanhTien();
            }
        });

        btnBanhang.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String maSach = tfMaSachBan.getText().trim();
                    String tenSach = tfTenSachBan.getText().trim();
                    int soLuongBan = Integer.parseInt(tfSLban.getText().trim());
                    int giaTien = XuatNhapKhoPanel.layGiaTien(maSach);
                    Date ngayBan = dateChooser.getDate();
                    if (ngayBan == null) {
                        JOptionPane.showMessageDialog(null, "Vui lòng chọn ngày bán!");
                        return;
                    }

                    if (!isNgayBanValid(ngayBan)) {
                        JOptionPane.showMessageDialog(null, "Ngày bán không hợp lệ!");
                        return;
                    }
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String ngayBanStr = sdf.format(ngayBan);

                    thongKePanel.capNhatBangThongKe(maSach, tenSach, soLuongBan, giaTien * soLuongBan, ngayBanStr);
                    capNhatSoLuongTonKho(maSach, soLuongBan); // Cập nhật số lượng tồn kho
                    sendProductToServer(maSach, tenSach, soLuongBan, ngayBanStr); // Gửi thông tin sách đến server
                    JOptionPane.showMessageDialog(null, "Bán hàng thành công!");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Không hợp lệ!");
                }
            }
        });



        btnHoaDon.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                xuatHoaDon();
            }
        });

        this.setVisible(true);
    }
    private boolean isNgayBanValid(Date ngayBanDate) {
        Date currentDate = new Date();
        return !ngayBanDate.after(currentDate);
    }

    private void tinhThanhTien() {
        try {
            String maSach = tfMaSachBan.getText().trim();
            int soLuongBan = Integer.parseInt(tfSLban.getText().trim());
            int giaTien = XuatNhapKhoPanel.layGiaTien(maSach);
            int thanhTien = soLuongBan * giaTien;
            lblThanhTien.setText("Thành tiền: " + thanhTien + "đ");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập số lượng hợp lệ.");
        }
    }

    public void capNhatSoLuongTonKho(String maSanPham, int soLuongBan) {
        String sql = "UPDATE them SET soLuongTonKho = soLuongTonKho - ? WHERE maSach = ?";
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/quanlysach", "root", "221004");
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, soLuongBan);
            statement.setString(2, maSanPham);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    


    private void xuatHoaDon() {
        try {
            String tenSach = tfTenSachBan.getText().trim();
            int soLuongBan = Integer.parseInt(tfSLban.getText().trim());
            int thanhTien = Integer.parseInt(lblThanhTien.getText().replaceAll("\\D+", ""));
            Date ngayBan = dateChooser.getDate();

            if (ngayBan == null) {
                JOptionPane.showMessageDialog(null, "Vui lòng chọn ngày bán!");
                return;
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String ngayBanStr = sdf.format(ngayBan);

            String hoaDon = "---- Hóa Đơn Bán Hàng ----\n\n";
            hoaDon += "Tên Sách: " + tenSach + "\n";
            hoaDon += "Số Lượng: " + soLuongBan + "\n";
            hoaDon += "Thành Tiền: " + thanhTien + "đ\n";
            hoaDon += "Ngày Bán: " + ngayBanStr + "\n";

            System.out.println("Xuất hóa đơn thành công:\n" + hoaDon);

            try (FileWriter writer = new FileWriter("HoaDonBanHang.txt")) {
                writer.write(hoaDon);
                System.out.println("Xuất hóa đơn ra file thành công!");
            } catch (IOException e) {
                System.err.println("Lỗi khi ghi hóa đơn ra file: " + e.getMessage());
                JOptionPane.showMessageDialog(null, "Lỗi khi ghi hóa đơn ra file!");
            }
        } catch (NumberFormatException e) {
            System.err.println("Lỗi định dạng số: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Số lượng hoặc thành tiền không đúng định dạng!");
        }
    }
    private void sendProductToServer(String maSach, String tenSach, int soLuongBan, String ngayBanStr) {
        try {
            if (clientSocket == null || clientSocket.isClosed()) {
                clientSocket = new Socket("localhost", PORT);
                out = new ObjectOutputStream(clientSocket.getOutputStream());
            }

            HangHoa hhc = new HangHoa();
            hhc.setMaSach(maSach);
            hhc.setTenSach(tenSach);
            hhc.setSoLuongBan(soLuongBan);

            out.writeObject(hhc);
            out.flush();
			addToLogs("Sach da ban: " + hhc.getTenSach() + ", So luong ban: " + soLuongBan);
        } catch (IOException ex) {
            addToLogs("Error sending product to server: " + ex.getMessage());
        }
    }




    public int getSoLuongBan() {
        String slBanStr = tfSLban.getText().trim();
        if (slBanStr.isEmpty()) {
            return 0; 
        }
        try {
            return Integer.parseInt(slBanStr);
        } catch (NumberFormatException e) {
            return 0; 
        }
    }

	public void start() {
		try {
			PORT = 3310;
			clientName = "Ban sach";
			clientSocket = new Socket("localhost", PORT);
			
			ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
			out.writeObject(hhc);
			
			new Thread(new Listener()).start();
		} catch (Exception err) {
			addToLogs("[ERROR] "+err.getLocalizedMessage());
		}
	}

	public void stop(){
		if(!clientSocket.isClosed()) {
			try {
				clientSocket.close();
			} catch (IOException e1) {}
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
				for(;;) {
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
