package tfg.ucm.com.tfgparkinson.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

import tfg.ucm.com.tfgparkinson.Adapters.DiaAdapter;
import tfg.ucm.com.tfgparkinson.Adapters.TemblorAdapter;
import tfg.ucm.com.tfgparkinson.Classes.Dia;
import tfg.ucm.com.tfgparkinson.Classes.Temblor;
import tfg.ucm.com.tfgparkinson.R;

public class HistorialActiviy extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);

        Temblor temblor1 = new Temblor(new Date(), "18:00", 1, "Estaba tecleando");
        Temblor temblor2 = new Temblor(new Date(), "20:00", 1, "Estaba usando el móvil");
        Temblor temblor3 = new Temblor(new Date(), "21:00", 1, "Estaba fregando");
        Temblor temblor4 = new Temblor(new Date(), "21:00", 1, "Estaba fregando");
        Temblor temblor5 = new Temblor(new Date(), "14:00", 1, "Estaba andando");
        Temblor temblor6 = new Temblor(new Date(), "15:00", 1, "Estaba leyendo");
        Temblor temblor7 = new Temblor(new Date(), "18:00", 1, "Estaba tecleando");
        Temblor temblor8 = new Temblor(new Date(), "20:00", 1, "Estaba usando el móvil");
        Temblor temblor9 = new Temblor(new Date(), "20:00", 1, "Estaba fregando");
        Temblor temblor10 = new Temblor(new Date(), "20:00", 1, "Estaba andando");
        Temblor temblor11 = new Temblor(new Date(), "20:00", 1, "Estaba leyendo");
        Temblor temblor12 = new Temblor(new Date(), "20:00", 1, "Estaba usando el móvil");
        Temblor temblor13 = new Temblor(new Date(), "20:00", 1, "Estaba fregando");
        Temblor temblor14 = new Temblor(new Date(), "20:00", 1, "Estaba andando");
        Temblor temblor15 = new Temblor(new Date(), "20:00", 1, "Estaba leyendo");

        ArrayList<Temblor> temblores = new ArrayList<>();
        temblores.add(temblor1);
        temblores.add(temblor2);
        temblores.add(temblor3);
        temblores.add(temblor4);
        temblores.add(temblor5);
        temblores.add(temblor6);
        temblores.add(temblor7);
        temblores.add(temblor8);
        temblores.add(temblor9);
        temblores.add(temblor10);
        temblores.add(temblor11);
        temblores.add(temblor12);
        temblores.add(temblor13);
        temblores.add(temblor14);
        temblores.add(temblor15);

        LinearLayout historial = (LinearLayout) findViewById(R.id.historial);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(getApplicationContext().LAYOUT_INFLATER_SERVICE);
        View vistaActual;
        TextView textoActual;

        for(int i = 0; i < temblores.size(); i ++) {
            if(i % 3 == 0) {
                vistaActual = layoutInflater.inflate(R.layout.dia_grafico, null, false);
                textoActual = (TextView) vistaActual.findViewById(R.id.fecha);
                textoActual.setText("12/07/17");
            } else {
                vistaActual = layoutInflater.inflate(R.layout.temblor_grafico, null, false);
                textoActual = (TextView) vistaActual.findViewById(R.id.observacionesTemblor);
                textoActual.setText("Observaciones: " + temblores.get(i).getObservaciones());
                textoActual = (TextView) vistaActual.findViewById(R.id.horaTemblor);
                textoActual.setText("Hora: " + temblores.get(i).getHora());
                //textoActual = (TextView) vistaActual.findViewById(R.id.duracionTemblor);
                //textoActual.setText(temblores.get(i).getDuracion());
            }
            historial.addView(vistaActual);
        }
    }
}
