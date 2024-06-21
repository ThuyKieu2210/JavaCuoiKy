
package View;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import Model.HangHoa;
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
import java.util.Date;

import javax.swing.JTextField;
import javax.swing.JButton;

public class SuaSP extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;
    public static Socket clientSocket;
    public static int PORT = 3310;
    public ObjectOutputStream out;
    JPanel contentPane;
    public JTextField tfPORT, tfMaSach, tfTacGia, tfTheLoai, tfGiaBan, tfSLtonKho, tfTenSach;
    String clientName;
    JLabel lblTheLoai,lblGiaBan,lblSLtonKho,lblTacGia,lblMaSach,lblTenSach;
    JButton btnConnect;
    JButton btnOK;
    JButton btnCancel;
    private SanPhamPanelView sp;
    HangHoa hhc;
    DefaultTableModel dataModel;
    private JLabel lblNhpPort;



    public SuaSP(String s, HangHoa hangHoa, DefaultTableModel model, SanPhamPanelView sp) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 456, 348);
        setTitle("Cập nhật sản phẩm");
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        this.dataModel = model;
        contentPane.setLayout(null);

        lblMaSach = new JLabel("Mã Sách:");
        lblMaSach.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblMaSach.setBounds(10, 62, 109, 24);
        contentPane.add(lblMaSach);

        tfMaSach = new JTextField(hangHoa.getMaSach());
        tfMaSach.setColumns(10);
        tfMaSach.setBounds(162, 57, 250, 29);
        tfMaSach.setEditable(false); // Không cho phép chỉnh sửa
        contentPane.add(tfMaSach);

        lblTenSach = new JLabel("Tên Sách:");
        lblTenSach.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblTenSach.setBounds(10, 96, 109, 24);
        tfMaSach.setEditable(false);
        contentPane.add(lblTenSach);

        tfTenSach = new JTextField(hangHoa.getTenSach());
        tfTenSach.setBounds(162, 96, 250, 29);
        tfTenSach.setColumns(10);
        tfTenSach.setEditable(false);
        contentPane.add(tfTenSach);

        lblTacGia = new JLabel("Tác Giả:");
        lblTacGia.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblTacGia.setBounds(10, 130, 109, 24);
        contentPane.add(lblTacGia);

        tfTacGia = new JTextField(hangHoa.getTacGia());
        tfTacGia.setColumns(10);
        tfTacGia.setBounds(162, 130, 250, 29);
        tfTenSach.setEditable(false);
        contentPane.add(tfTacGia);

        lblTheLoai = new JLabel("Thể Loại:");
        lblTheLoai.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblTheLoai.setBounds(10, 164, 109, 24);
        contentPane.add(lblTheLoai);

        tfTheLoai = new JTextField(hangHoa.getTheLoai());
        tfTheLoai.setColumns(10);
        tfTheLoai.setBounds(162, 164, 250, 29);
        tfTheLoai.setEditable(false);
        contentPane.add(tfTheLoai);

        lblGiaBan = new JLabel("Giá Bán:");
        lblGiaBan.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblGiaBan.setBounds(10, 198, 109, 24);
        contentPane.add(lblGiaBan);

        tfGiaBan = new JTextField(String.valueOf(hangHoa.getGiaSach()));
        tfGiaBan.setColumns(10);
        tfGiaBan.setBounds(162, 198, 250, 29);
        contentPane.add(tfGiaBan);

        lblSLtonKho = new JLabel("Số lượng tồn kho:");
        lblSLtonKho.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblSLtonKho.setBounds(10, 232, 142, 24);
        contentPane.add(lblSLtonKho);

        tfSLtonKho = new JTextField(String.valueOf(hangHoa.getSoLuongTonKho()));
        tfSLtonKho.setColumns(10);
        tfSLtonKho.setBounds(162, 232, 250, 29);
        tfSLtonKho.setEditable(false);
        contentPane.add(tfSLtonKho);

        tfPORT = new JTextField();
        tfPORT.setBounds(162, 23, 152, 29);
        contentPane.add(tfPORT);
        tfPORT.setColumns(10);

        btnOK = new JButton("OK");
        btnOK.addActionListener(this);
        btnOK.setFont(new Font("Tahoma", Font.PLAIN, 16));
        btnOK.setBounds(149, 273, 59, 30);
        contentPane.add(btnOK);

        btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(this);
        btnCancel.setFont(new Font("Tahoma", Font.PLAIN, 16));
        btnCancel.setBounds(212, 273, 88, 30);
        contentPane.add(btnCancel);
        
        lblNhpPort = new JLabel("Nhập PORT");
        lblNhpPort.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblNhpPort.setBounds(10, 28, 109, 24);
        contentPane.add(lblNhpPort);

        this.sp = sp;

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("OK")) {
            if (tfPORT.getText().equals("3310")) {
                String maSach = tfMaSach.getText();
                String tenSach = tfTenSach.getText();
                String tacGia = tfTacGia.getText();
                String theLoai = tfTheLoai.getText();
                try {
                    double gia = Double.parseDouble(tfGiaBan.getText());
                    if (gia < 0) {
                        JOptionPane.showMessageDialog(rootPane, "Giá bán phải là số không âm!");
                        return;
                    }
                    try {
                        int soLuongTonKho = Integer.parseInt(tfSLtonKho.getText());
                        if (soLuongTonKho < 0) {
                            JOptionPane.showMessageDialog(rootPane, "Số lượng tồn kho phải là số không âm!");
                            return;
                        }
                        HangHoa hangHoa = new HangHoa(maSach, tenSach, tacGia, theLoai, gia, soLuongTonKho);
                        if (SuaHangHoaToDatabase(hangHoa)) {
                            sendProductToServer(hangHoa);
                            JOptionPane.showMessageDialog(rootPane, "Chỉnh sửa hàng hóa thành công!");
                            this.sp.display();
                        } else {
                            JOptionPane.showMessageDialog(rootPane, "Chỉnh sửa hàng hóa thất bại!");
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(rootPane, "Số lượng tồn kho phải là số!", "Lỗi định dạng số", JOptionPane.ERROR_MESSAGE);
                    }

                } catch (NumberFormatException e2) {
                    JOptionPane.showMessageDialog(rootPane, "Giá bán phải là số!", "Lỗi định dạng số", JOptionPane.ERROR_MESSAGE);
                }
            }else {
                JOptionPane.showMessageDialog(null, "PORT chưa đúng!");
            }
        } else if (e.getActionCommand().equals("Cancel")) {
            dispose();
        }
    }


    private boolean SuaHangHoaToDatabase(HangHoa hangHoa) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/quanlysach", "root", "221004");
            String updateQuery = "UPDATE them SET tenSach = ?, tacGia = ?, theLoai = ?, giaBan = ?, soLuongTonKho = ? WHERE maSach = ?";
            PreparedStatement statement = connection.prepareStatement(updateQuery);
            statement.setString(1, hangHoa.getTenSach());
            statement.setString(2, hangHoa.getTacGia());
            statement.setString(3, hangHoa.getTheLoai());
            statement.setDouble(4, hangHoa.getGiaSach());
            statement.setInt(5, hangHoa.getSoLuongTonKho());
            statement.setString(6, hangHoa.getMaSach());

            int rowsUpdated = statement.executeUpdate();
            connection.close();

            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void sendProductToServer(HangHoa hangHoa) {
        try {
            if (clientSocket == null || clientSocket.isClosed()) {
                clientSocket = new Socket("localhost", PORT);
                out = new ObjectOutputStream(clientSocket.getOutputStream());
            }
            out.writeObject(hangHoa);
            out.flush();
            addToLogs("Gia sach sau khi sua: " + hangHoa.getGiaSach());
        } catch (IOException ex) {
            addToLogs("Error sending product to server: " + ex.getMessage());
        }
    }

    public void start() {
		try {
			PORT = 3310;
			clientName = "Them San Pham";
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
