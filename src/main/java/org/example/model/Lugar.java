package org.example.model;

import java.util.Collection;
import java.util.Date;

public class Lugar {
    private int id;
    private String reserva;
    private String dataReserva;

    public Lugar(){

    }
    public Lugar(int id) {
        this.id = id;
    }

    public static Lugar lugarPorID(Collection<Lugar> lugarCollection, String idProcurado) {
        return lugarCollection.stream().filter(lug -> idProcurado.equals(Integer.toString(lug.getId()))).findFirst().orElse(null);
    }

    public void FazerReserva(String reservaNome, String dataReserva) {
        this.setReserva(reservaNome);
        this.setDataReserva(dataReserva);
    }

    public int getId() {
        return id;
    }

    public String getReserva() {
        return reserva;
    }

    public void setReserva(String reserva) {
        this.reserva = reserva;
    }

    public String getDataReserva() {
        return dataReserva;
    }

    public void setDataReserva(String dataReserva) {
        this.dataReserva = dataReserva;
    }
}
