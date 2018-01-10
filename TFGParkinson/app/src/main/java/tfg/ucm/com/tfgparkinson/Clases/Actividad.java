package tfg.ucm.com.tfgparkinson.Clases;

import java.sql.Timestamp;
import java.util.Calendar;

/**
 * Created by al3x_hh on 10/01/2018.
 */

public class Actividad {
    private String nombre;
    private int intervalo;
    private Timestamp hora;

    public Actividad(String nombre, int intervalo, String hora) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR, Integer.parseInt(hora.split(":")[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(hora.split(":")[1]));
        Timestamp ts = new Timestamp(calendar.getTimeInMillis());

        this.nombre = nombre;
        this.intervalo = intervalo;
        this.hora = ts;
    }

    public String getNombre() {
        return nombre;
    }

    public int getIntervalo() {
        return intervalo;
    }

    public Timestamp getHora() {
        return hora;
    }
}
