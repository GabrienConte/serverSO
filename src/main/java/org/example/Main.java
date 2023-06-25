package org.example;

import org.example.global.Global;
import org.example.log.Log;
import org.example.model.Lugar;
import org.example.webserver.SimpleSocketServer;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        InicializaLugar();
        Log log = new Log();
        SimpleSocketServer server = new SimpleSocketServer(80);
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

    public static void InicializaLugar() {
        if(Global.lugares.isEmpty()) {
            for(int i = 1; i <= Global.CONST_QTD_LUGARES; i++)
            {
                Lugar lugar = new Lugar(i);
                Global.lugares.add(lugar);
            }
        }
    }
}