package org.example.webserver;

import com.sun.source.tree.WhileLoopTree;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(80);
        System.out.println("Iniciando server socket...");

        while (true) {
            //1 Recebe a conexão
            Socket socket = ss.accept();
            System.out.println("Conexão recebida");
            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();
            //2 Recebe a requisição
            byte[] buffer = new byte[1024];
            int nBytes = in.read(buffer);
            String str = new String(buffer, 0, nBytes);
            String[] linhas = str.split("\n");
            int i = 1;
            for (String linha : linhas) {
                System.out.println("[LINHA " + 1 + "] " + linha);
                i++;
            }
            String[] linha1 = linhas[0].split(" ");
            String recurso = linha1[1];
            System.out.println("[RECURSO]" + recurso);

            String header = "HTTP/1.1 200 OK\n" +
                    "Content-Type: text/html; charset=utf-8\n\n";
            if (recurso.equals("/")) {
                recurso = "/index.html";
            }
            recurso = recurso.replace('/', File.separatorChar);
            File f = new File("arquivos_html" + recurso);
            System.out.println(f);
            if(!f.exists()) {
                out.write("404 NOT FOUND \n\n".getBytes(StandardCharsets.UTF_8));
            } else {
                InputStream fileIn = new FileInputStream(f);
                out.write(header.getBytes(StandardCharsets.UTF_8));
                // escreve arquivo
                nBytes = fileIn.read(buffer);
                do {
                    if (nBytes > 0) {
                        out.write(buffer, 0, nBytes);
                        nBytes = fileIn.read(buffer);
                    }
                } while (nBytes == 1024);
            }
            out.flush();
            socket.close();
        }
    }
}
