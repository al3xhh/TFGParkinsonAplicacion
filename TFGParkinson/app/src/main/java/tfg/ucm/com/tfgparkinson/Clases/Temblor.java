package tfg.ucm.com.tfgparkinson.Clases;

import android.content.ContentValues;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

public class Temblor {

    private int duracion;
    private String observaciones;
    private int id;
    private Timestamp timestamp;

    public Temblor(int id, Timestamp ts, int duracion, String observaciones) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(ts.getTime());

        this.duracion = duracion;
        this.observaciones = observaciones;
        this.id = id;
        this.timestamp = ts;
    }

    public Temblor(String fecha, String hora, int duracion, String observaciones) {
        Calendar calendar = Calendar.getInstance();
        ArrayList<Integer> datosTs = parseDiaYHora(fecha, hora);

        calendar.set(datosTs.get(0), datosTs.get(1), datosTs.get(2), datosTs.get(3), datosTs.get(4));
        Timestamp ts = new Timestamp(calendar.getTimeInMillis());

        this.timestamp = ts;
        this.duracion = duracion;
        this.observaciones = observaciones;
    }

    public Temblor(int duracion, String observaciones){
        this.duracion = duracion;
        this.observaciones = observaciones;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getFecha() {

        String[] tokens = timestamp.toString().split(" ");
        String[] fecha = tokens[0].split("-");
        String formatedFecha = fecha[2] + "/" + fecha[1] + "/" + fecha[0];

        return formatedFecha;
    }

    public String getHora() {
        String[] tokens = timestamp.toString().split(" ");
        String[] hora = tokens[1].split(":");

        return hora[0] + ":" + hora[1];
    }

    public int getDuracion() { return this.duracion; }

    public String getObservaciones() {
        return observaciones;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    private ArrayList<Integer> parseDiaYHora(String fecha, String hora) {
        ArrayList<Integer> datos = new ArrayList<Integer>();
        String[] tokensFecha = fecha.split("/");
        String[] tokensHora = hora.split(":");

        try{
            for (int i = tokensFecha.length - 1; i >= 0; i--)
                datos.add(Integer.parseInt(tokensFecha[i]));

            for (int i = 0; i < tokensHora.length; i++)
                datos.add(Integer.parseInt(tokensHora[i]));
        }
        catch (NumberFormatException e) {
            datos = null;
        }

        return datos;
    }
}
