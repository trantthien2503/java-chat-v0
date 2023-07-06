/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.java.chat;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.*;

/**
 *
 * @author THIETKE
 */
public class JavaChat extends JFrame {

    private static final long serialVersionUID = 1L;
    private DefaultListModel<String> usersModel;
    private JList<String> usersList;
    private JTextArea chatArea;
    private JTextField messageField;
    private JButton sendButton;
    private JButton fileButton;

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;

    private ArrayList<ChatClientThread> clientThreads;

    public static void main(String[] args) {
        System.out.println("Hello World!");
    }
}

class ChatClientThread extends Thread {

    private Socket clientSocket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;

    public ChatClientThread(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        inputStream = new DataInputStream(clientSocket.getInputStream());
        outputStream = new DataOutputStream(clientSocket.getOutputStream());
    }

//    public void run() {
//        String username;
//
//        try {
//            username = inputStream.readUTF();
//            usersModel.addElement(username);
//
//            String message;
//
//            while (true) {
//                message = inputStream.readUTF();
//                if (message.startsWith("#FILE")) {
//                    // Receive file from client
//                    receiveFile(message);
//                } else {
//                    // Regular chat message
//                    chatArea.append(username + ": " + message + "\n");
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                clientSocket.close();
//                inputStream.close();
//                outputStream.close();
//                usersModel.removeElement(username);
//                clientThreads.remove(this);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    public void sendMessage(String message) throws IOException {
        outputStream.writeUTF(message);
        outputStream.flush();
    }

    public void sendFile(String fileName, byte[] fileBytes) throws IOException {
        outputStream.writeUTF("#FILE " + fileName);
        outputStream.writeInt(fileBytes.length);
        outputStream.write(fileBytes);
        outputStream.flush();
    }
//
//    private void receiveFile(String message) throws IOException {
//        String[] parts = message.split(" ");
//        String fileName = parts[1];
//        int fileSize = inputStream.readInt();
//
//        byte[] fileBytes = new byte[fileSize];
//        FileOutputStream fileOutputStream = new FileOutputStream(fileName);
//
//        int bytesRead;
//        int totalBytesRead = 0;
//        while ((bytesRead = inputStream.read(fileBytes, totalBytesRead, fileSize - totalBytesRead)) > 0) {
//            totalBytesRead += bytesRead;
//        }
//
//        fileOutputStream.write(fileBytes);
//        fileOutputStream.close();
//
//        chatArea.append("Received a file: " + fileName + "\n");
//    }
}
