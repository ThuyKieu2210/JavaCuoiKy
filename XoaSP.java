package View;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
import Model.HangHoa;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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

public class XoaSP extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;
    public static Socket clientSocket;
    public static int PORT = 3310;
    public static ObjectOutputStream out;
    JPanel contentPane;
    JLabel lblTacGia;
    public JTextField tfPORT, tfMaSach, tfTacGia, tfTheLoai, tfGiaBan, tfSLtonKho, tfTenSach;
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
  
    public XoaSP() {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("OK")) {
            if (tfPORT.getText().equals("3310")) {
                String maSach = tfMaSach.getText();
                String tenSach = tfTenSach.getText();
                String tacGia = tfTacGia.getText();
                String theLoai = tfTheLoai.getText();
                double gia = Double.parseDouble(tfGiaBan.getText());
                int soLuongTonKho = Integer.parseInt(tfSLtonKho.getText());

                HangHoa hangHoa = new HangHoa(maSach, tenSach, tacGia, theLoai, gia, soLuongTonKho);
                if (XoaHangHoaToDatabase(maSach)) {
                    sendProductToServer(hangHoa);
                    JOptionPane.showMessageDialog(rootPane, "Xóa hàng hóa thành công!");
                    this.sp.display();
                } else {
                    JOptionPane.showMessageDialog(rootPane, "Xóa hàng hóa thất bại!");
                }
            }
        } else if (e.getActionCommand().equals("Cancel")) {
            dispose();
        }
    }

    public boolean XoaHangHoaToDatabase(String maSach) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/quanlysach", "root", "221004");
            String deleteQuery = "DELETE FROM them WHERE maSach = ?";
            PreparedStatement statement = connection.prepareStatement(deleteQuery);
            statement.setString(1, maSach);

            int rowsDeleted = statement.executeUpdate();
            connection.close();

            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void sendProductToServer(HangHoa hangHoa) {
        try {
            if (clientSocket == null || clientSocket.isClosed()) {
                clientSocket = new Socket("localhost", PORT);
                out = new ObjectOutputStream(clientSocket.getOutputStream());
            }
            out.writeObject(hangHoa);
            out.flush();
            addToLogs("Xoa san pham: " + hangHoa.getTenSach());
        } catch (IOException ex) {
            addToLogs("Error sending product to server: " + ex.getMessage());
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
                while ((read = in.readLine()) != null) {
                    if (!read.isEmpty()) {
                    	addToLogs("Hello");
                        addToLogs(read);
                    }
                }
            } catch (IOException e) {
                return;
            }
        }
    }
    public void start() {
		try {
			PORT = 3310;
			clientName = "Xoa San Pham";
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
