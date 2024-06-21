package View;

import java.awt.Component;

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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JTextField;
import javax.swing.JButton;

public class ThemSP extends JFrame implements ActionListener {

	public static final long serialVersionUID = 1L;
	public static Socket clientSocket;
	public static int PORT = 3310;
	public ObjectOutputStream out;
	JPanel contentPane;
	JLabel lblTacGia;
	public JTextField tfPORT,tfMaSach,tfTacGia,tfTheLoai,tfGiaBan,tfSLtonKho,tfTenSach;
	String clientName;
	JLabel lblTheLoai;
	JLabel lblGiaBan;
	JLabel lblSLtonKho;
	JButton btnConnect;
	JButton btnOK;
	JButton btnCancel;
	private SanPhamPanelView sp;
	HangHoa hhc;

	
	DefaultTableModel dataModel;
	ArrayList<HangHoa> hh = new ArrayList<HangHoa>();
	HangHoa hhk = new HangHoa();
	private JLabel lblMaSach;
	private Component lblTenSach;
	
	
	
	public HangHoa getHhk() {
		return hhk;
	}
	public void setHhk(HangHoa hhk) {
		this.hhk = hhk;
	}
    public ThemSP(String s, ArrayList<HangHoa> arrayList, DefaultTableModel model, SanPhamPanelView sp) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 456, 348);
		setTitle("Thêm sản phẩm");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		
		setContentPane(contentPane);
		this.dataModel = model;
		contentPane.setLayout(null);
		
		lblMaSach = new JLabel("Mã Sách:");
		lblMaSach.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblMaSach.setBounds(10, 55, 109, 24);
		contentPane.add(lblMaSach);
		
		tfMaSach = new JTextField();
		tfMaSach.setColumns(10);
		tfMaSach.setBounds(162, 50, 250, 29);
		contentPane.add(tfMaSach);
		
		lblTenSach = new JLabel("Tên Sách:");
		lblTenSach.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblTenSach.setBounds(10, 89, 109, 24);
		contentPane.add(lblTenSach);
		
		tfTenSach = new JTextField();
		tfTenSach.setBounds(162, 89, 250, 29);
		contentPane.add(tfTenSach);
		tfTenSach.setColumns(10);
		
		lblTacGia = new JLabel("Tác Giả:");
		lblTacGia.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblTacGia.setBounds(10, 123, 109, 24);
		contentPane.add(lblTacGia);
		
		lblTheLoai = new JLabel("Thể Loại:");
		lblTheLoai.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblTheLoai.setBounds(10, 157, 109, 24);
		contentPane.add(lblTheLoai);
		
		lblGiaBan = new JLabel("Giá Bán:");
		lblGiaBan.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblGiaBan.setBounds(10, 191, 109, 24);
		contentPane.add(lblGiaBan);
		
		lblSLtonKho = new JLabel("Số lượng tồn kho:");
		lblSLtonKho.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblSLtonKho.setBounds(10, 225, 142, 24);
		contentPane.add(lblSLtonKho);
		
		tfTacGia = new JTextField();
		tfTacGia.setColumns(10);
		tfTacGia.setBounds(162, 123, 250, 29);
		contentPane.add(tfTacGia);
		
		tfTheLoai = new JTextField();
		tfTheLoai.setColumns(10);
		tfTheLoai.setBounds(162, 157, 250, 29);
		contentPane.add(tfTheLoai);
		
		tfGiaBan = new JTextField();
		tfGiaBan.setColumns(10);
		tfGiaBan.setBounds(162, 191, 250, 29);
		contentPane.add(tfGiaBan);
		
		tfSLtonKho = new JTextField();
		tfSLtonKho.setColumns(10);
		tfSLtonKho.setBounds(162, 225, 250, 29);
		tfSLtonKho.setEditable(false);
		contentPane.add(tfSLtonKho);
		
		tfPORT = new JTextField();
		tfPORT.setBounds(162, 10, 152, 29);
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
		
		JLabel lblNhpPort = new JLabel("Nhập PORT");
		lblNhpPort.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNhpPort.setBounds(10, 18, 109, 24);
		contentPane.add(lblNhpPort);
		
		this.sp = sp;
		
		setVisible(true);
	}
   
	 public void actionPerformed(ActionEvent e) {
	        if (e.getActionCommand().equals("OK")) {
	            if(tfPORT.getText().equals("3310")) {
	            	String maSach = tfMaSach.getText();
	            	if (maSach.isEmpty()) {
	                    JOptionPane.showMessageDialog(rootPane, "Vui lòng nhập mã sách!");
	                    return;
	                }
	            	if(kiemTraMaSachTonTai(maSach)) {
	            		JOptionPane.showMessageDialog(rootPane, "Mã sản phẩm đã tồn tại");
	            		return;
	            	}
	            	
	            	HangHoa nhapKhoInfo = ProductInfoFromNhapKho(maSach);
	                if (nhapKhoInfo == null) {
	                    JOptionPane.showMessageDialog(rootPane, "Mã sản phẩm không tồn tại trong Nhập Kho!");
	                    return;
	                }
	                
	                tfTenSach.setText(nhapKhoInfo.getTenSach());
	                tfSLtonKho.setText(String.valueOf(nhapKhoInfo.getSoLuongTonKho()));
	                
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
	    		            if (addHangHoaToDatabase(hangHoa)) {
	    		            	sendProductToServer(hangHoa);
	    		                JOptionPane.showMessageDialog(rootPane, "Thêm hàng hóa thành công!");
	    		                this.sp.display();
	    		            } else {
	    		                JOptionPane.showMessageDialog(rootPane, "Thêm hàng hóa thất bại!");
	    		            }
			            }
			            catch(NumberFormatException e2) {
	                        JOptionPane.showMessageDialog(rootPane, "Số lượng tồn kho phải là số!", "Lỗi định dạng số", JOptionPane.ERROR_MESSAGE);

			            }
		            }
		            catch(NumberFormatException e2) {
	                    JOptionPane.showMessageDialog(rootPane, "Giá bán phải là số!", "Lỗi định dạng số", JOptionPane.ERROR_MESSAGE);
		            }
		            if (maSach.isEmpty()) {
		                JOptionPane.showMessageDialog(rootPane, "Vui lòng nhập mã sách!");
		                return;
		            }
	            }else {
	                JOptionPane.showMessageDialog(null, "PORT chưa đúng!");
	            }
	        }
	        else if (e.getActionCommand().equals("Cancel")) {
	            dispose();
	        }
	    }
	 public static HangHoa ProductInfoFromNhapKho(String maSach) {
	        try {
	            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/quanlysach", "root", "221004");
	            String sql = "SELECT tenSachNhap, soLuongNhap FROM nhap_kho WHERE maSachNhapKho = ?";
	            PreparedStatement statement = connection.prepareStatement(sql);
	            statement.setString(1, maSach);
	            ResultSet rs = statement.executeQuery();
	            if (rs.next()) {
	               String tenSach = rs.getString("tenSachNhap");
	               int soLuongNhap = rs.getInt("soLuongNhap");
	               return new HangHoa(maSach, tenSach, "", "", 0.0, soLuongNhap);

	            }
	            rs.close();
	            statement.close();
	            connection.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return null;
	    }
	 public static boolean kiemTraMaSachTonTai(String maSach) {
		    boolean tonTai = false;
		    try {
		        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/quanlysach", "root", "221004");
		        String sql = "SELECT COUNT(*) FROM them WHERE maSach = ?";
		        PreparedStatement statement = connection.prepareStatement(sql);
		        statement.setString(1, maSach);
		        ResultSet rs = statement.executeQuery();
		        if (rs.next()) {
		            tonTai = rs.getInt(1) > 0;
		        }
		        rs.close();
		        statement.close();
		        connection.close();
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		    return tonTai;
		}
	 private boolean addHangHoaToDatabase(HangHoa hangHoa) {
		 
		 try {
		        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/quanlysach", "root", "221004");
		        PreparedStatement statement = connection.prepareStatement("INSERT INTO them (maSach, tenSach, tacGia, theLoai, giaBan, soLuongTonKho) VALUES (?, ?, ?, ?, ?, ?)");
		        statement.setString(1, hangHoa.getMaSach());
		        statement.setString(2, hangHoa.getTenSach());
		        statement.setString(3, hangHoa.getTacGia());
		        statement.setString(4, hangHoa.getTheLoai());
		        statement.setDouble(5, hangHoa.getGiaSach());
		        statement.setInt(6, hangHoa.getSoLuongTonKho());
//		        statement.executeUpdate();
		        int rowsInserted = statement.executeUpdate();
		        connection.close();
		        
		        return rowsInserted > 0; 
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
		        addToLogs("Them san pham: " + hangHoa.getTenSach()); 
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
