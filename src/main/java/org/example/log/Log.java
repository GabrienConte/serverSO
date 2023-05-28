package org.example.log;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class Log {
    public static void main(String[] args) {
        new Log();
    }

    public Log() {
        new Thread(new Produtor()).start();
        new Thread(new Consumidor()).start();
    }

    private final int CAP_BUFFER = 100;
    private List<Integer> buffer = new ArrayList<>(CAP_BUFFER);

    public static void createFile() {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm");
            Calendar cal = Calendar.getInstance();

            File myObj = new File("C:\\Users\\Gabriel Conte\\IdeaProjects\\serverSO\\arquivos_log\\log"+ dateFormat.format(cal.getTime()) +".txt");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public class Produtor implements Runnable {
        private Random rnd = new Random(System.currentTimeMillis());

        @Override
        public void run() {
            while (true) {
                Integer inteiroProduzido = produzDado();
                synchronized (buffer) {
                    if (buffer.size() == CAP_BUFFER) { //cheio
                        try { buffer.wait(); } catch (InterruptedException e) { }
                    }
                    buffer.add(inteiroProduzido);
                    buffer.notify();
                }
                System.out.println("Produtor: produziu " + inteiroProduzido);

            }
        }

        private Integer produzDado() {
            return rnd.nextInt();
        }
    }

    public class Consumidor implements Runnable {

        @Override
        public void run() {
            while (true) {
                synchronized (buffer) {
                    if (buffer.isEmpty()) { //vazio
                        try { buffer.wait(); } catch (InterruptedException e) { }
                    }
                    Integer iConsumido = buffer.remove(0);
                    System.out.println("Consumidor: consumiu " + iConsumido);
                    buffer.notify();
                }
            }
        }
    }
}
