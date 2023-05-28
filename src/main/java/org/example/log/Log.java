package org.example.log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Log {
    private static ArrayList<String> buffer = new ArrayList<>();
    private File logFile;
    public Log() {
        this.logFile = createFile();
        new Thread(new Leitor("Iniciou")).start();
        new Thread(new Escritor()).start();
    }

    public static File createFile() {
        File myObj = null;
        try {
            myObj = new File("arquivos_log\\log"+ dataAtual("yyyy-MM-dd_HH-mm") +".txt");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return myObj;
    }

    public static String dataAtual(String formato) {
        DateFormat dateFormat = new SimpleDateFormat(formato);
        Calendar cal = Calendar.getInstance();
        return dateFormat.format(cal.getTime());
    }

    public static void logTexto(String texto) {
        new Thread(new Leitor(texto)).start();
    }

    public static class Leitor implements Runnable {

        private String texto;

        public Leitor(String texto) {
            this.texto = texto;
        }

        @Override
        public void run() {
            String textoRecebido = this.texto;
            synchronized (buffer) {
               buffer.add(textoRecebido);
            }
        }
    }

    public class Escritor implements Runnable {
        @Override
        public void run() {
            while (true) {
                synchronized (buffer) {
                    try {
                        if(!buffer.isEmpty())  {
                            escreve(buffer.get(0));
                            buffer.remove(0);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        private void escreve(String mensagem) {
            try {
                FileWriter fr = new FileWriter(logFile, true);
                BufferedWriter bw = new BufferedWriter(fr);

                String logTexto = "[" + Log.dataAtual("yyyy-MM-dd HH:mm:ss") + "] " + mensagem + "\n";
                bw.write(logTexto);

                bw.close();
            }  catch (Exception error) {
                error.printStackTrace();
            }
        }
    }
}
