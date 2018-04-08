package tfg.ucm.com.tfgparkinson.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;

import tfg.ucm.com.tfgparkinson.Clases.Actividad;
import tfg.ucm.com.tfgparkinson.Clases.BBDD.GestorBD;
import tfg.ucm.com.tfgparkinson.Clases.Medicamento;
import tfg.ucm.com.tfgparkinson.R;

/**
 * Created by al3x_hh on 08/04/2018.
 */

public class ActividadesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividades);

        reiniciarActivity();

        FloatingActionButton addActivity = (FloatingActionButton) findViewById(R.id.addActivityB);
        addActivity.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                anyadirActividad();
            }
        });
    }

    /**
     * Función que añade una actividad.
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void anyadirActividad() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.anyadir_actividad, null);

        final Spinner nombreActividad = (Spinner) dialogView.findViewById(R.id.seleccionarActividad);
        final EditText duracion = (EditText) dialogView.findViewById(R.id.duracionActividad);
        final TimePicker horaInicio = (TimePicker) dialogView.findViewById(R.id.horaInicioActividad);
        final EditText observaciones = (EditText) dialogView.findViewById(R.id.observacionesActividad);

        duracion.setRawInputType(Configuration.KEYBOARD_12KEY);
        horaInicio.setIs24HourView(true);

        dialogBuilder.setTitle("Añadir actividad");
        dialogBuilder.setPositiveButton(getString(R.string.guardar), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    Actividad actividad = new Actividad(nombreActividad.getSelectedItem().toString(),
                            Integer.parseInt(duracion.getText().toString()), horaInicio.getHour() + ":" + horaInicio.getMinute(),
                            observaciones.getText().toString());
                    GestorBD gestorBD = new GestorBD(getApplicationContext());
                    gestorBD.insertActividad(actividad);
                    Toast.makeText(getApplicationContext(), R.string.exito_registro_actividad, Toast.LENGTH_SHORT).show();
                    reiniciarActivity();
                } catch (NumberFormatException e) {
                    Toast.makeText(getApplicationContext(), R.string.duracion_no_valida, Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialogBuilder.setNegativeButton(getString(R.string.cancelar), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        dialogBuilder.setView(dialogView);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    private void reiniciarActivity() {
        GestorBD bd = new GestorBD(this);
        ArrayList<Actividad> actividades = bd.getActividades();

        ListView actividadesLV = (ListView) findViewById(R.id.actividadesLV);
        actividadesLV.setAdapter(new ArrayAdapterActividades(this, actividades));
    }
}
