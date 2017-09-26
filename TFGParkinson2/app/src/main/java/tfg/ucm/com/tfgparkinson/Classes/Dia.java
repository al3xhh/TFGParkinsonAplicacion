package tfg.ucm.com.tfgparkinson.Classes;

import java.util.ArrayList;
import java.util.Date;

public class Dia {
    private Date fecha;
    private ArrayList<Temblor> temblores;

    public Dia(Date fecha, ArrayList<Temblor> temblores) {
        this.fecha = fecha;
        this.temblores = temblores;
    }

    public Date getFecha() { return fecha; }

    public ArrayList<Temblor> getTemblores() { return temblores; }
}
