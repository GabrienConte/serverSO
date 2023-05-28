package org.example.webserver;

import org.example.log.Log;
import org.example.model.Lugar;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class SimpleSocketServer extends Thread {
    private ServerSocket serverSocket;
    private int port;
    private boolean running = false;

    public SimpleSocketServer(int port) {
        this.port = port;
    }

    public void startServer() {
        try {
            serverSocket = new ServerSocket(port);
            this.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopServer() {
        running = false;
        this.interrupt();
    }

    @Override
    public void run() {
        running = true;
        while (running) {
            try {
                Log.logTexto("Listening for a connection");
                System.out.println("Listening for a connection");

                Socket socket = serverSocket.accept();

                RequestHandler requestHandler = new RequestHandler(socket);
                requestHandler.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}