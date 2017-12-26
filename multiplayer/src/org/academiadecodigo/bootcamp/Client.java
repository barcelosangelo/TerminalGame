package org.academiadecodigo.bootcamp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

    public static void main(String[] args) {

        Client client = new Client();
        client.start();

    }

    private int port;
    private String hostName;
    private String userName;
    private ClientHelper clientHelper;
    PrintWriter out;
    BufferedReader in;
    Socket clientSocket = null;
    BufferedReader inReader;

    public void start() {

        inReader = new BufferedReader(new InputStreamReader(System.in));

        try {

            System.out.print("hostname: ");
            hostName = inReader.readLine();
            System.out.print("port: ");
            port = Integer.parseInt(inReader.readLine());
            System.out.print("username: ");
            userName = inReader.readLine();


            clientSocket = new Socket(hostName, port);

            System.out.println("Connection established with " + hostName + " on port " + port + "\n");
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        } catch (IOException e) {
            e.printStackTrace();
        }

        while (clientSocket.isBound()) {
            try {
                clientHelper = new ClientHelper(clientSocket);

                Thread thread = new Thread(clientHelper);
                thread.start();
                String message = null;
                message = in.readLine();
                System.out.println(message);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }


    private class ClientHelper implements Runnable {

        PrintWriter out2;
        BufferedReader in2;


        public ClientHelper(Socket clientSocket) {

            try {

                out = new PrintWriter(clientSocket.getOutputStream(), true);

            } catch (IOException e) {
                e.printStackTrace();
            }


        }

        @Override
        public void run() {
            while (clientSocket.isBound()) {
                try {
                    System.out.print("Your message: ");
                    String message = null;

                    message = inReader.readLine();

                    out.println(userName + ":" + message);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

        public String getUserName() {
            return userName;
        }
    }
}