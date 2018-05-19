package tfg.ucm.com.tfgparkinson.Activities;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.res.Configuration;

import android.os.Build;
import android.os.Bundle;

import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.text.Html;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;

import tfg.ucm.com.tfgparkinson.Adaptadores.AdaptadorActividades;
import tfg.ucm.com.tfgparkinson.Clases.Actividad;
import tfg.ucm.com.tfgparkinson.Clases.BBDD.GestorBD;
import tfg.ucm.com.tfgparkinson.R;

/**
 * Actividad que muestra lo relacionado con las actividades:
 *  - Lista de actividades.
 *  - Añadir nueva actividad.
 */
public class Actividades extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividades);
        setTitle("Actividades");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        reiniciarActivity();

        FloatingActionButton ayuda = (FloatingActionButton) findViewById(R.id.ayuda);
        ayuda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarAyuda();
            }
        });

        FloatingActionButton anyadir = (FloatingActionButton) findViewById(R.id.addActivityB);
        anyadir.setOnClickListener(new View.OnClickListener() {
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
        @SuppressLint("InflateParams") View dialogView = inflater.inflate(R.layout.anyadir_actividad,null);

        final EditText nombreActividad = (EditText) dialogView.findViewById(R.id.seleccionarActividad);
        final EditText duracion = (EditText) dialogView.findViewById(R.id.duracionActividad);
        final TimePicker horaInicio = (TimePicker) dialogView.findViewById(R.id.horaInicioActividad);
        final DatePicker fechaAcitivdad = (DatePicker) dialogView.findViewById(R.id.fechaActividad);
        final EditText observaciones = (EditText) dialogView.findViewById(R.id.observacionesActividad);

        duracion.setRawInputType(Configuration.KEYBOARD_12KEY);
        horaInicio.setIs24HourView(true);

        dialogBuilder.setTitle("Añadir actividad");
        dialogBuilder.setPositiveButton(getString(R.string.guardar), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    Actividad actividad = new Actividad(nombreActividad.getText().toString(),
                            Integer.parseInt(duracion.getText().toString()),
                            horaInicio.getHour() + ":" + horaInicio.getMinute(),
                            fechaAcitivdad.getDayOfMonth() + "/" + fechaAcitivdad.getMonth() + "/" + fechaAcitivdad.getYear(),
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

    /**
     * Función que permite reiniciar la actividad para que los cambios
     * se hagan visibles en la lista.
     */
    private void reiniciarActivity() {
        GestorBD gestorBD = new GestorBD(this);
        ArrayList<Actividad> listaActividades = gestorBD.getActividades();
        ListView actividades = (ListView) findViewById(R.id.actividadesLV);
        TextView noActividades = (TextView) findViewById(R.id.noActivities);

        if(listaActividades.size() == 0) {
            noActividades.setVisibility(View.VISIBLE);
            actividades.setVisibility(View.GONE);
        } else {
            noActividades.setVisibility(View.GONE);
            actividades.setVisibility(View.VISIBLE);
            actividades.setAdapter(new AdaptadorActividades(this, listaActividades));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void mostrarAyuda() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.dialogo));
        dialogBuilder.setTitle("Ayuda actividades");
        dialogBuilder.setMessage(Html.fromHtml("<p align=\"justify\">" +
                "- Para añadir una actividad pulse en <strong>el botón de abajo a la derecha</strong> y rellene los campos. <br><br>" +
                "- Para editar una actividad pulse <strong>dentro de los tres puntitos de la actividad en editar</strong>. <br><br>" +
                "- Para borrar una actividad pulse <strong>dentro de los tres puntitos de la actividad en borrar</strong>.</p>"));

        dialogBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }
}
