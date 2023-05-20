package br.ufsm.poli.csi.so.webserver;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Server {

    private static int numAcessos;

    public static void main(String[] args) {
        try (ServerSocket ss = new ServerSocket(80);
             FileOutputStream logs = new FileOutputStream("log.txt")) {

            System.out.println("Iniciando server socket...");

            while (true) {
                Socket socket = ss.accept();
                System.out.println("Conexão recebida");

                try {
                    processRequest(socket, logs);
                } catch (IOException e) {
                    System.out.println("Erro ao processar a requisição: " + e.getMessage());
                } finally {
                    socket.close();
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao iniciar o servidor: " + e.getMessage());
        }
    }

    private static void processRequest(Socket socket, FileOutputStream logs) throws IOException {
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
                numAcessos++;
                resource = "/index.html";
            } else if (resource.contains("/compraingresso")) {
                String[] arr = resource.split("/\\?");
                String[] form = arr[1].split("&");
                // Processar os parâmetros do formulário
            }

            logs.write("algum log\n".getBytes(StandardCharsets.UTF_8));
            logs.flush();

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
    }

    private static String processVariables(ByteArrayOutputStream bout) {
        String content = new String(bout.toByteArray(), StandardCharsets.UTF_8);
        content = content.replace("${numAcessos}", Integer.toString(numAcessos));
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
}