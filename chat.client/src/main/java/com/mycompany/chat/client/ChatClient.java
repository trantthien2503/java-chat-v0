/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.chat.client;

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
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;

/**
 *
 * @author THIETKE
 */
public class ChatClient extends JFrame implements ActionListener {

    private JLabel lblUsername, lblPassword;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin, btnRegister;

    public ChatClient() {
        setTitle("Login");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2, 10, 10)); // Thêm khoảng cách giữa các thành phần

        lblUsername = new JLabel("Username:");
        txtUsername = new JTextField("", 20);
        lblPassword = new JLabel("Password:");
        txtPassword = new JPasswordField("", 20);
        btnLogin = new JButton("Login");
        btnRegister = new JButton("Register");

        panel.add(lblUsername);
        panel.add(txtUsername);
        panel.add(lblPassword);
        panel.add(txtPassword);
        panel.add(btnLogin);
        panel.add(btnRegister);

        btnLogin.addActionListener(this);
        btnRegister.addActionListener(this);

        // Thêm khoảng cách giữa panel và viền JFrame
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(panel);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ChatClient chatClient = new ChatClient();
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnLogin) {
            // Xử lý đăng nhập
            String username = txtUsername.getText();
            String password = new String(txtPassword.getPassword());

            // Kiểm tra thông tin đăng nhập
            if (username.equals("admin") && password.equals("admin")) {
                JOptionPane.showMessageDialog(this, "Đăng nhập thành công!");
                // Mở giao diện chính của ứng dụng chat
                // TODO: Thêm code để mở giao diện chính ở đây
            } else {
                JOptionPane.showMessageDialog(this, "Sai thông tin đăng nhập!");
            }
        } else if (e.getSource() == btnRegister) {
            // Xử lý đăng kí
            // TODO: Thêm code xử lý đăng kí tại đây
        }
    }
}
