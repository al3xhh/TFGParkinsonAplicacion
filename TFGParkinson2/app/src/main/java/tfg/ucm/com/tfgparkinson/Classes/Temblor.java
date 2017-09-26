package tfg.ucm.com.tfgparkinson.Classes;

import java.util.Date;

public class Temblor {

    private Date fecha;
    private String hora;
    private int duracion;
    private String observaciones;

    public Temblor(Date fecha, String hora, int duracion, String observaciones) {
        this.fecha = fecha;
        this.hora = hora;
        this.duracion = duracion;
        this.observaciones = observaciones;
    }

    public Date getFecha() { return fecha; }

    public String getHora() {
        return hora;
    }

    public int getDuracion() { return this.duracion; }

    public String getObservaciones() {
        return observaciones;
    }
}
