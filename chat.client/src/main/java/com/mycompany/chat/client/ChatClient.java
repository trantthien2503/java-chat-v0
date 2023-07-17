/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.chat.client;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.net.Socket;

/**
 *
 * @author THIETKE
 */
public class ChatClient extends JFrame {

    private JTextField messageField;
    private JTextArea chatArea;
    private JButton sendButton;
    private JButton sendFileButton;
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;

    public void loginRegisterGUI() {
        setTitle("Login/Register");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(300, 200));

        // Tạo panel chứa các thành phần giao diện
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2, 10, 10));

        // Thêm các thành phần giao diện vào panel
        JLabel usernameLabel = new JLabel("Username:");
        panel.add(usernameLabel);

        usernameField = new JTextField();
        panel.add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        panel.add(passwordLabel);

        passwordField = new JPasswordField();
        panel.add(passwordField);

        loginButton = new JButton("Login");
        panel.add(loginButton);

        registerButton = new JButton("Register");
        panel.add(registerButton);

        // Đăng ký bộ xử lý sự kiện cho nút đăng nhập và nút đăng ký
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                char[] password = passwordField.getPassword();
                // Xử lý đăng nhập ở đây
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                char[] password = passwordField.getPassword();
                // Xử lý đăng ký ở đây
            }
        });

        // Thêm panel vào JFrame
        add(panel);

        pack();
        setLocationRelativeTo(null); // Đặt cửa sổ ở giữa màn hình
    }

    public void chatClient() {
        setTitle("Chat Client");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setFocusable(false); // Disabled chatArea
        JScrollPane chatScrollPane = new JScrollPane(chatArea);
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 3;
        mainPanel.add(chatScrollPane, constraints);

        messageField = new JTextField();
        sendButton = new JButton("Send");
        sendFileButton = new JButton("Send File");

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        sendFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendFile();
            }
        });

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.weighty = 0.0;
        constraints.gridwidth = 1;
        mainPanel.add(messageField, constraints);

        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.weightx = 0.0;
        mainPanel.add(sendButton, constraints);

        constraints.gridx = 2;
        constraints.gridy = 1;
        mainPanel.add(sendFileButton, constraints);

        add(mainPanel);
        setLocationRelativeTo(null); // Hiển thị cửa sổ ở giữa màn hình

        messageField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    sendMessage();
                    e.consume();
                }
            }
        });

        getRootPane().setDefaultButton(sendButton);
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
        String message = messageField.getText();
        writer.println(message);
        appendMessage("Me: " + message);
        messageField.setText("");
    }

    private void appendMessage(String message) {
        chatArea.append(message + "\n");
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
            chatClient.loginRegisterGUI();
//            chatClient.setVisible(true);

            // Connect to the server
//            chatClient.connectToServer("localhost", 12345);
        });
    }
}
