package tfg.ucm.com.tfgparkinson.Clases;

import java.sql.Timestamp;
import java.util.Calendar;

/**
 * Created by al3x_hh on 10/01/2018.
 */

public class Actividad {
    private String nombre;
    private int intervalo;
    private String hora;
    private String observaciones;

    public Actividad(String nombre, int intervalo, String hora, String observaciones) {
        this.nombre = nombre;
        this.intervalo = intervalo;
        this.hora = hora;
        this.observaciones = (observaciones == null ? "" : observaciones);
    }

    public String getNombre() {
        return nombre;
    }

    public int getIntervalo() {
        return intervalo;
    }

    public String getHora() {
        return hora;
    }

    public String getObservaciones() { return observaciones; }
}
