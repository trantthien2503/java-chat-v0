/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.chat.server;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.SwingUtilities;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.text.DefaultCaret;

/**
 *
 * @author THIETKE
 */
public class ChatServer extends JFrame {

    private List<ClientHandler> clients;
    private JTextField portTextField;
    private JTextArea actionTextArea;
    private JButton startButton;
    private JList logTextArea;
    private JPanel mainJPanel;
    private JPanel topPanel;
    private JScrollPane logScrollPane;
    private JScrollPane actionScrollPane;
    public static ArrayList<String> logs = new ArrayList<>();
    public static DefaultListModel<String> listModel = new DefaultListModel<>();

    public ChatServer() {
        clients = new ArrayList<>();
        portTextField = new JTextField("8080", 20);
        portTextField.setPreferredSize(new Dimension(250, 30));

        actionTextArea = new JTextArea();
        actionTextArea.setEditable(false);
        startButton = new JButton("Start");
        startButton.setPreferredSize(new Dimension(100, 30));
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int port = Integer.parseInt(portTextField.getText());
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        start(port);
                    }
                });
            }
        });
        setupGUI();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void setupGUI() {
        mainJPanel = new JPanel(new BorderLayout(10, 10));

        topPanel = new JPanel();
        topPanel.add(portTextField);
        topPanel.add(startButton);
        mainJPanel.add(topPanel, BorderLayout.NORTH);
        logTextArea = new JList(listModel);
        listModel.addListDataListener(new ListDataListener() {
            @Override
            public void intervalAdded(ListDataEvent e) {
                SwingUtilities.invokeLater(() -> {
                    logScrollPane.revalidate();
                    logScrollPane.repaint();
                });
            }

            @Override
            public void intervalRemoved(ListDataEvent e) {
                SwingUtilities.invokeLater(() -> {
                    // Cập nhật logScrollPane khi có giá trị bị xóa khỏi listModel
                    logScrollPane.revalidate();
                    logScrollPane.repaint();
                });
            }

            @Override

            public void contentsChanged(ListDataEvent e) {
                SwingUtilities.invokeLater(() -> {
                    // Cập nhật logScrollPane khi nội dung của listModel thay đổi
                    logScrollPane.revalidate();
                    logScrollPane.repaint();
                });
            }
        });
        logScrollPane = new JScrollPane(logTextArea);
        logScrollPane.setPreferredSize(new Dimension(400, 200));
        mainJPanel.add(logScrollPane, BorderLayout.CENTER);

        actionScrollPane = new JScrollPane(actionTextArea);
        actionScrollPane.setPreferredSize(new Dimension(400, 100));
        mainJPanel.add(actionScrollPane, BorderLayout.SOUTH);

        getContentPane().add(mainJPanel);

    }

    public void start(int port) {
        System.out.println(".run()");

        try {
            ServerSocket serverSocket = new ServerSocket(port);
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    System.out.println(".run()");
                    listModel.addElement("Server started on port " + port + "...\n");
                }
            });

            while (true) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        listModel.addElement("Waiting for client connections...\n");
                    }
                });

                Socket clientSocket = serverSocket.accept();

                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        listModel.addElement("Client connected: " + clientSocket + "\n");
                    }
                });

                logTextArea = new JList(listModel);
                logScrollPane = new JScrollPane(logTextArea);
                ClientHandler clientHandler = new ClientHandler(clientSocket, this);
                clients.add(clientHandler);

                Thread clientThread = new Thread(clientHandler);
                clientThread.start();

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void broadcastMessage(String message, ClientHandler sender) {
        for (ClientHandler client : clients) {
            if (client != sender) {
                client.sendMessage(message);
            }
        }
    }

    public synchronized void removeClient(ClientHandler clientHandler) {
        clients.remove(clientHandler);
        System.out.println("Client disconnected: " + clientHandler.getClientSocket());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ChatServer chatServer = new ChatServer();
        });
    }
}
