package org.example;

import org.example.webserver.SimpleSocketServer;

public class Main {
    public static void main(String[] args) {
        SimpleSocketServer server = new SimpleSocketServer(8080);
        server.startServer();

        // Automatically shutdown in 1 minute
        try {
            while (true) {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        server.stopServer();
    }
}