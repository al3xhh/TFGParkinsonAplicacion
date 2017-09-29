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

    public void setFecha(Date fecha) { this.fecha = fecha; }

    public void setHora(String hora) { this.hora = hora; }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
