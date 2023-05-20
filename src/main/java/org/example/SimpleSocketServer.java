package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

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
                System.out.println("Listening for a connection");

                // Call accept() to receive the next connection
                Socket socket = serverSocket.accept();

                // Pass the socket to the RequestHandler thread for processing
                RequestHandler requestHandler = new RequestHandler(socket);
                requestHandler.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class RequestHandler extends Thread {
        private Socket socket;

        RequestHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                try (InputStream in = socket.getInputStream();
                     OutputStream out = socket.getOutputStream()) {

                    byte[] buffer = new byte[1024];
                    int nBytes = in.read(buffer);
                    String request = new String(buffer, 0, nBytes);
                    String[] lines = request.split("\n");

                    for (int i = 0; i < lines.length; i++) {
                        System.out.println("[LINHA " + (i + 1) + "] " + lines[i]);
                    }

                    String[] requestLine = lines[0].split(" ");
                    String resource = requestLine[1];
                    System.out.println("[RECURSO] " + resource);

                    if (resource.equals("/")) {
                        resource = "/index.html";
                    } else if (resource.contains("/reserva")) {
                        resource = "/reserva.html";
                        String[] arr = resource.split("/\\?");
//                        String[] form = arr[1].split("&");
                        // Processar os parâmetros do formulário
                    }
                    System.out.println(resource);

                    resource = resource.replace('/', File.separatorChar);
                    String header = "HTTP/1.1 200 OK\n" +
                            "Content-Type: " + getContentType(resource) + "; charset=utf-8\n\n";
                    File file = new File("arquivos_html" + resource);

                    try (ByteArrayOutputStream bout = new ByteArrayOutputStream()) {
                        if (!file.exists()) {
                            bout.write("404 NOT FOUND\n\n".getBytes(StandardCharsets.UTF_8));
                        } else {
                            try (InputStream fileIn = new FileInputStream(file)) {
                                bout.write(header.getBytes(StandardCharsets.UTF_8));

                                while ((nBytes = fileIn.read(buffer)) != -1) {
                                    bout.write(buffer, 0, nBytes);
                                }
                            }
                        }

                        String response = processVariables(bout);
                        out.write(response.getBytes(StandardCharsets.UTF_8));
                        out.flush();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private static String processVariables(ByteArrayOutputStream bout) {
        String content = new String(bout.toByteArray(), StandardCharsets.UTF_8);
        return content;
    }

    private static String getContentType(String resourceName) {
        if (resourceName.toLowerCase().endsWith(".css")) {
            return "text/css";
        } else if (resourceName.toLowerCase().endsWith(".jpg") || resourceName.toLowerCase().endsWith(".jpeg")) {
            return "image/jpeg";
        }else if (resourceName.toLowerCase().endsWith(".png")) {
            return "image/png";
        } else if (resourceName.toLowerCase().contains(".js")) {
            return "application/javascript";
        } else {
            return "text/html";
        }
    }

    public static void main(String[] args) {
        SimpleSocketServer server = new SimpleSocketServer(8080);
        server.startServer();

        // Automatically shutdown in 1 minute
        try {
            while(true){}
        } catch (Exception e) {
            e.printStackTrace();
        }

        server.stopServer();
    }


}

