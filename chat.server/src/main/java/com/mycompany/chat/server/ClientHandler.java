/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chat.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
/**
 *
 * @author THIETKE
 */
class ClientHandler implements Runnable {

    private Socket clientSocket;
    private ChatServer ChatServer;
    private BufferedReader inputReader;
    private PrintWriter outputWriter;

    public ClientHandler(Socket clientSocket, ChatServer ChatServer) throws IOException {
        this.clientSocket = clientSocket;
        this.ChatServer = ChatServer;
        inputReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        outputWriter = new PrintWriter(clientSocket.getOutputStream(), true);
    }

    @Override
    public void run() {
        try {
            String inputLine;
            while ((inputLine = inputReader.readLine()) != null) {
                ChatServer.broadcastMessage(inputLine);
            }
        } catch (IOException e) {
            System.out.println("Error handling client: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
                ChatServer.removeClient(this);
            } catch (IOException e) {
                System.out.println("Error closing client socket: " + e.getMessage());
            }
        }
    }

    public void sendMessage(String message) {
        outputWriter.println(message);
    }
}
