package View;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Model.TextAreaOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import Model.HangHoa;
import Model.NhapKho;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ServerUI extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;
	static SimpleDateFormat formatter = new SimpleDateFormat("[hh:mm a]");
    private static HashMap<String, PrintWriter> connectedClients = new HashMap<>();
    private static final int MAX_CONNECTED = 50;
    private static int PORT;
    private static ServerSocket server;
    private static volatile boolean exit = false;

    private JPanel contentPane;
    private JTextArea txtAreaLogs;
    private JButton btnStart;
    private JLabel lblChatServer;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ServerUI frame = new ServerUI();
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
                    SwingUtilities.updateComponentTreeUI(frame);
                    System.setOut(new PrintStream(new TextAreaOutputStream(frame.txtAreaLogs)));
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public ServerUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 570, 400);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));

        lblChatServer = new JLabel("QUAN LY");
        lblChatServer.setHorizontalAlignment(SwingConstants.CENTER);
        lblChatServer.setFont(new Font("Tahoma", Font.PLAIN, 40));
        contentPane.add(lblChatServer, BorderLayout.NORTH);

        btnStart = new JButton("START");
        btnStart.addActionListener(this);
        btnStart.setFont(new Font("Tahoma", Font.PLAIN, 30));
        contentPane.add(btnStart, BorderLayout.SOUTH);

        JScrollPane scrollPane = new JScrollPane();
        contentPane.add(scrollPane, BorderLayout.CENTER);

        txtAreaLogs = new JTextArea();
        txtAreaLogs.setBackground(Color.BLACK);
        txtAreaLogs.setForeground(Color.WHITE);
        txtAreaLogs.setLineWrap(true);
        scrollPane.setViewportView(txtAreaLogs);
        setLocation(10,10);
        setSize(450,550);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == btnStart) {
            if(btnStart.getText().equals("START")) {
                exit = false;
                getRandomPort();
                start();
                new loginView();
                btnStart.setText("STOP");
            }else {
                addToLogs("Chat server stopped...");
                exit = true;
                btnStart.setText("START");
            }
        }
        refreshUIComponents();
    }

    public void refreshUIComponents() {
        lblChatServer.setText("SERVER" + (!exit ? ": "+PORT:""));
    }

    public static void start() {
        new Thread(new ServerHandler()).start();
    }

    public static void stop() {
        try {
            if (server != null && !server.isClosed()) {
                server.close();
            }
        } catch (IOException e) {
            addToLogs("Error closing the server socket: " + e.getMessage());
        }
    }

//    private static void broadcastMessage(String message) {
//        for (PrintWriter p: connectedClients.values()) {
//            p.println(message);
//        }
//    }

    public static void addToLogs(String message) {
        System.out.printf("%s %s\n", formatter.format(new Date()), message);
    }

    private static int getRandomPort() {
        PORT = 3310; // Default port
        return PORT;
    }

    private static class ServerHandler implements Runnable {
        @Override
        public void run() {
            try {
                server = new ServerSocket(PORT);
                addToLogs("Bat dau quan ly PORT: " + PORT);
                addToLogs("Dang cho ket noi...");
                while (!exit) {
                    if (connectedClients.size() <= MAX_CONNECTED) {
                        new Thread(new ClientHandler(server.accept())).start();
                    }
                }
            } catch (IOException e) {
                addToLogs("Error occurred: " + e.getMessage());
                addToLogs("Exiting...");
            }
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket socket;
        private ObjectInputStream in;
        private ObjectOutputStream out;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            addToLogs("1 Client da ket noi: " + socket.getInetAddress());
            try {
                in = new ObjectInputStream(socket.getInputStream());
                out = new ObjectOutputStream(socket.getOutputStream());

                Object obj;
                while ((obj = in.readObject()) != null && !exit) {
                    if (obj instanceof NhapKho) {
                        NhapKho nhapkho = (NhapKho) obj;
//                        addToLogs("Nhap Sach: " + nhapkho.getTenSachNhap() + "\nSo Luong Nhap: " + nhapkho.getSoLuongNhap());
                    }
                    else if (obj instanceof HangHoa) {
                    	HangHoa hangHoa = (HangHoa) obj;
                    }
                    else {
                        addToLogs("Received unknown object type.");
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                addToLogs(e.getMessage());
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    addToLogs(e.getMessage());
                }
            }
        }
    }

}
