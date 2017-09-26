package tfg.ucm.com.tfgparkinson.Classes;

import java.util.Date;

public class Temblor {

    private Date fecha;
    private String hora;
    private String observaciones;

    public Temblor(Date fecha, String hora, String observaciones) {
        this.fecha = fecha;
        this.hora = hora;
        this.observaciones = observaciones;
    }

    public Date getFecha() {
        return fecha;
    }

    public String getHora() {
        return hora;
    }

    public String getObservaciones() {
        return observaciones;
    }
}
