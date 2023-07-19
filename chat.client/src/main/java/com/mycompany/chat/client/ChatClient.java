/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.chat.client;

import Interface.User;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author THIETKE
 */
public class ChatClient extends JFrame implements ActionListener {

    private Socket socket;

    private BufferedReader reader;
    private PrintWriter writer;

    public static ArrayList<User> Users = new ArrayList<>();
    private JLabel lblUsername, lblPassword;
    private JTextField txtUsername, txtMessage, txtPort;
    private JPasswordField txtPassword;
    private JButton btnLogin, btnRegister, btnSend, btnConnect, btnAttach;
    private JPanel loginPanel, chatPanel;
    private JTextArea txtChat;

    public ChatClient() {
    }

    public void LoginRegisterGUI() {
        setTitle("Login");
        setSize(400, 180);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        loginPanel = new JPanel();
        loginPanel.setLayout(new GridLayout(3, 2, 10, 10)); // Thêm khoảng cách giữa các thành phần

        lblUsername = new JLabel("Username:");
        txtUsername = new JTextField("", 20);
        lblPassword = new JLabel("Password:");
        txtPassword = new JPasswordField("", 20);
        btnLogin = new JButton("Login");
        btnRegister = new JButton("Register");

        loginPanel.add(lblUsername);
        loginPanel.add(txtUsername);
        loginPanel.add(lblPassword);
        loginPanel.add(txtPassword);
        loginPanel.add(btnLogin);
        loginPanel.add(btnRegister);

        btnLogin.addActionListener(this);
        btnRegister.addActionListener(this);

        // Thêm khoảng cách giữa panel và viền JFrame
        loginPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(loginPanel);
        setVisible(true);
    }

    public void ChatClientGUI() {
        setTitle("Chat client");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JPanel connectionPanel = new JPanel();
        connectionPanel.setLayout(new FlowLayout());

        JLabel lblPort = new JLabel("Port:");
        txtPort = new JTextField("", 10);
        btnConnect = new JButton("Connect");

        connectionPanel.add(lblPort);
        connectionPanel.add(txtPort);
        connectionPanel.add(btnConnect);

        mainPanel.add(connectionPanel, BorderLayout.NORTH);

        chatPanel = new JPanel();
        chatPanel.setLayout(new BorderLayout());

        txtChat = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(txtChat);
        txtChat.setEditable(false);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());

        txtMessage = new JTextField();
        btnSend = new JButton("Send");
        btnAttach = new JButton("Attach");

        inputPanel.add(txtMessage, BorderLayout.CENTER);
        inputPanel.add(btnSend, BorderLayout.EAST);
        inputPanel.add(btnAttach, BorderLayout.WEST);

        chatPanel.add(scrollPane, BorderLayout.CENTER);
        chatPanel.add(inputPanel, BorderLayout.SOUTH);

        mainPanel.add(chatPanel, BorderLayout.CENTER);

        btnConnect.addActionListener(this);
        btnSend.addActionListener(this);
        btnAttach.addActionListener(this);

        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(mainPanel);
        setVisible(true);
    }

    public void ImportDataFromFileTxt() {
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/java/Assets/users.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] lines = line.split("`");
                User user = new User(lines[0], lines[1]);
                Users.add(user);
            }
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
    }

    public void connectToServer(String serverAddress, int serverPort) {
        try {
            socket = new Socket(serverAddress, serverPort);
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();

            reader = new BufferedReader(new InputStreamReader(inputStream));
            writer = new PrintWriter(outputStream, true);

            // Start a separate thread to listen for messages from the server
            Thread messageListener = new Thread(new MessageListener());
            messageListener.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage() {
        String message = txtMessage.getText();
        writer.println(message);
        appendMessage("Me: " + message);
        txtMessage.setText("");
    }

    private void appendMessage(String message) {
        txtChat.append(message + "\n");
    }

    private void sendFile() {
        // Logic để gửi file
        JOptionPane.showMessageDialog(this, "File sent!");
    }

    private class MessageListener implements Runnable {

        @Override
        public void run() {
            try {
                String message;
                while ((message = reader.readLine()) != null) {
                    appendMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ChatClient chatClient = new ChatClient();
            chatClient.ImportDataFromFileTxt();
            chatClient.ChatClientGUI();
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ChatClient chatClient = new ChatClient();

        if (e.getSource() == btnLogin) {
            // Xử lý đăng nhập
            String username = txtUsername.getText();
            String password = new String(txtPassword.getPassword());
            User user = new User(username, password);
            Boolean result = false;
            if (Users.size() > 0) {
                for (User itemUser : Users) {
                    if ((itemUser.getUser() == null ? user.getUser() == null : itemUser.getUser().equals(user.getUser())) && (itemUser.getPassword() == null ? user.getPassword() == null : itemUser.getPassword().equals(user.getPassword()))) {
                        result = true;
                        break;
                    }
                }
            }
            // Đăng nhập thành công, chuyển sang giao diện chat
            if (result == true) {
                chatClient.ChatClientGUI();
                revalidate();
                repaint();
            } else {
                JOptionPane.showMessageDialog(null, "Đăng nhập thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }

        } else if (e.getSource() == btnRegister) {
            // Xử lý đăng kí
            String username = txtUsername.getText();
            String password = new String(txtPassword.getPassword());
            if (username.length() == 0) {
                return;
            }
            if (password.length() == 0) {
                return;
            }

            String lineWriteToTxT = username + "`" + password;
            try {
                FileWriter fileWriter = new FileWriter("src/main/java/Assets/users.txt", true);
                try (BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
                    bufferedWriter.newLine();
                    bufferedWriter.write(lineWriteToTxT);
                    txtUsername.setText("");
                    txtPassword.setText("");
                    JOptionPane.showMessageDialog(null, "Đăng kí thành công!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Lưu dữ liệu thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
            // TODO: Thêm code để xử lý đăng kí tại đây
        } else if (e.getSource() == btnConnect) {
            int port = Integer.parseInt(txtUsername.getText());
            chatClient.connectToServer("localhost", port);
            // TODO: Kết nối tới server với serverIP và port đã nhập
        } else if (e.getSource() == btnSend) {
            String message = txtMessage.getText();

            // TODO: Gửi tin nhắn tới server
        } else if (e.getSource() == btnAttach) {
            // TODO: Kết nối tới server với serverIP và port đã nhập
        }
    }
}
