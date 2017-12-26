package org.academiadecodigo.bootcamp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    public static void main(String[] args) {

        Server server = new Server();
        server.start();

    }

    private int port = 8080;
    private List <ClientHandler> clientList = Collections.synchronizedList(new ArrayList<ClientHandler>());
    private ClientHandler clientHandler;
    ExecutorService fixedPool = Executors.newFixedThreadPool(3);

    public void start() {

        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true) {
            try {

                Socket clientSocket = serverSocket.accept();
                System.out.println("Server is now connected and waiting for requests at port number: " + port);
                clientHandler = new ClientHandler(clientSocket);
                fixedPool.submit(clientHandler);
                clientList.add(clientHandler);
            } catch (IOException e) {
                System.out.println("Missing port number");
            }

        }
    }

    public void sendAll(String message) {

        for (int i = 0; i < clientList.size(); i++) {
            clientList.get(i).send(message);
        }

    }


    private class ClientHandler implements Runnable {

        PrintWriter out;
        BufferedReader in;

        public ClientHandler(Socket clientSocket) {
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        public void run() {
            while (true) {
                try {
                    String message = in.readLine();

                    sendAll(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

        public void send(String message) {
            out.println(message);
        }

    }

}