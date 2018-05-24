package tfg.ucm.com.tfgparkinson.Clases;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by al3x_hh on 11/12/2017.
 */

public class Medicamento {

    private String nombre;
    private int intervalo;
    private Timestamp timestamp;
    private ArrayList<String> dias;
    private String hora;

    public Medicamento(String nombre, int intervalo, String hora, ArrayList<String> dias) {
        Calendar calendar = Calendar.getInstance();

        if(hora.contains("-")) {
            calendar.set(Calendar.HOUR, Integer.parseInt(hora.split(" ")[1].split(":")[0]));
            calendar.set(Calendar.MINUTE, Integer.parseInt(hora.split(" ")[1].split(":")[1]));
        } else {
            calendar.set(Calendar.HOUR, Integer.parseInt(hora.split(":")[0]));
            calendar.set(Calendar.MINUTE, Integer.parseInt(hora.split(":")[1]));
        }

        Timestamp ts = new Timestamp(calendar.getTimeInMillis());

        this.nombre = nombre;
        this.intervalo = intervalo;
        this.timestamp = ts;
        this.dias = dias;
        this.hora = hora;
    }

    public String getNombre() {
        return nombre;
    }

    public int getIntervalo() {
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

    public void setIntervalo(int intervalo) {
        this.intervalo = intervalo;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public void setDias(ArrayList<String> dias) {
        this.dias = dias;
    }

    public String getHora() {
        return hora;
    }

    public String getDiasFormateados() {
        int size = dias.size();
        StringBuilder ret = new StringBuilder();

        for(int i = 0; i < size; i ++) {
            ret.append(dias.get(i));

            if(i != size - 1)
                ret.append("-");
        }

        return ret.toString();
    }
}
