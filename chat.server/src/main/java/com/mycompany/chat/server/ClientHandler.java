/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chat.server;

import java.io.*;
import java.net.*;

/**
 *
 * @author THIETKE
 */
class ClientHandler implements Runnable {

    private Socket clientSocket;
    private ChatServer chatServer;
    private BufferedReader inputReader;
    private PrintWriter outputWriter;

    public ClientHandler(Socket clientSocket, ChatServer chatServer) throws IOException {
        this.clientSocket = clientSocket;
        this.chatServer = chatServer;
        inputReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        outputWriter = new PrintWriter(clientSocket.getOutputStream(), true);
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    @Override
    public void run() {
        try {
            String inputLine;
            while ((inputLine = inputReader.readLine()) != null) {
                chatServer.broadcastMessage(inputLine, this);
            }
        } catch (IOException e) {
            System.out.println("Error handling client: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
                chatServer.removeClient(this);
            } catch (IOException e) {
                System.out.println("Error closing client socket: " + e.getMessage());
            }
        }
    }

    public void sendMessage(String message) {
        outputWriter.println(message);
    }
}
