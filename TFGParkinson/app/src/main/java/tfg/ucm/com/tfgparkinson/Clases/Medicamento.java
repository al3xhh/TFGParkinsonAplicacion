package tfg.ucm.com.tfgparkinson.Clases;

import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Created by al3x_hh on 11/12/2017.
 */

public class Medicamento {

    private String nombre;
    private String intervalo;
    private Timestamp timestamp;
    private ArrayList<String> dias;

    public Medicamento(String nombre, String intervalo, Timestamp timestamp, ArrayList<String> dias) {
        this.nombre = nombre;
        this.intervalo = intervalo;
        this.timestamp = timestamp;
        this.dias = dias;
    }

    public String getNombre() {
        return nombre;
    }

    public String getIntervalo() {
        return intervalo;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public ArrayList<String> getDias() {
        return dias;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setIntervalo(String intervalo) {
        this.intervalo = intervalo;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public void setDias(ArrayList<String> dias) {
        this.dias = dias;
    }

    public String getDiasFormateados() {
        int size = dias.size();
        StringBuilder ret = new StringBuilder();

        for(int i = 0; i < size; i ++) {
            ret.append(dias.get(i));

            if(i != size - 1)
                ret.append(" - ");
        }

        return ret.toString();
    }
}
