package tfg.ucm.com.tfgparkinson.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;

import tfg.ucm.com.tfgparkinson.Adapters.HistorialAdapter;
import tfg.ucm.com.tfgparkinson.Classes.Temblor;
import tfg.ucm.com.tfgparkinson.R;

public class HistorialActiviy extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);

        Temblor temblor = new Temblor(new Date(), "18:00", "Estaba tecleando");
        Temblor temblor1 = new Temblor(new Date(), "20:00", "Estaba usando el móvil");
        ArrayList<Temblor> temblores = new ArrayList<>();
        temblores.add(temblor);
        temblores.add(temblor1);

        HistorialAdapter adapter = new HistorialAdapter(getApplicationContext(), R.layout.temblor_grafico, temblores);
        ListView historial = (ListView) findViewById(R.id.historial);
        historial.setAdapter(adapter);
    }
}
