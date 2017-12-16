package tfg.ucm.com.tfgparkinson.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONArray;

import tfg.ucm.com.tfgparkinson.Clases.BBDD.GestorBD;
import tfg.ucm.com.tfgparkinson.Clases.Temblor;
import tfg.ucm.com.tfgparkinson.Clases.utils.Constantes;
import tfg.ucm.com.tfgparkinson.Clases.utils.RespuestaServidor;
import tfg.ucm.com.tfgparkinson.Clases.utils.Servidor;
import tfg.ucm.com.tfgparkinson.R;

import static com.android.volley.Request.Method.POST;

public class MainActivityActividad extends AppCompatActivity implements RespuestaServidor {

    public static Context appContext;

    @Override
    public Context getApplicationContext() {
        return super.getApplicationContext();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_actividad);

        appContext = getApplicationContext();

        Button anyadirActividad = (Button) findViewById(R.id.botonAnyadirActividad);
        anyadirActividad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anyadirActividad();
            }
        });

        Button anyadirMedicamento = (Button) findViewById(R.id.botonAnyadirMedicamento);
        anyadirMedicamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anyadirMedicacion();
            }
        });

        Button verGraficos = (Button) findViewById(R.id.botonVerGraficos);
        verGraficos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivityActividad.this, GraficosActivity.class);
                startActivity(i);
            }
        });

        Button verMedicamentos = (Button) findViewById(R.id.botonVerMedicamentos);
        verMedicamentos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivityActividad.this, ListadoMedicamentosActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent i = null;

        if(item.getItemId() == R.id.enviar_datos) {
            GestorBD bd = new GestorBD(getApplicationContext());
            JSONArray sensores = bd.getTb_datos_sensor();
            JSONArray posiciones = bd.getTb_posiciones();
            JSONArray temblores = bd.getTb_temlobres();
            Log.w("MainActivityLibre", "Datos enviados\n" + sensores.toString());
            enviarDatosServidor("http://192.168.1.108:5050/posiciones", posiciones);
            enviarDatosServidor("http://192.168.1.108:5050/datos_sensor", sensores);
            if (temblores.length() > 0)
                enviarDatosServidor("http://192.168.1.108:5050/temblores", temblores);
        } else {
            i = new Intent(MainActivityActividad.this, ConfigurarSensores.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sensores, menu);

        GestorBD bd = new GestorBD(getApplicationContext());
        int sensorDataCount = bd.getNumFilas("TB_DATOS_SENSOR");

        if(sensorDataCount == 0)
            menu.getItem(0).setVisible(false);

        return true;
    }

    /**
     * Función que añade un temblor concreto.
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void anyadirActividad() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.anyadir_actividad, null);

        final Spinner actividad = (Spinner) dialogView.findViewById(R.id.seleccionarActividad);
        final EditText duracion = (EditText) dialogView.findViewById(R.id.duracionActividad);
        final TimePicker horaInicio = (TimePicker) dialogView.findViewById(R.id.horaInicioActividad);

        horaInicio.setIs24HourView(true);
        dialogBuilder.setTitle("Añadir actividad");
        dialogBuilder.setPositiveButton(getString(R.string.guardar), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

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
     * Función que añade una medicación para el usuario.
     */
    private void anyadirMedicacion(){
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.anyadir_medicacion, null);

        final EditText nombreMedicacion = (EditText) dialogView.findViewById(R.id.nombreMedicacion);
        final EditText intervaloMedicacion = (EditText) dialogView.findViewById(R.id.intervaloMedicacion);
        final TimePicker horaMedicacion = (TimePicker) dialogView.findViewById(R.id.horaMedicacion);
        final CheckBox lunes = (CheckBox) dialogView.findViewById(R.id.lunes);
        final CheckBox martes = (CheckBox) dialogView.findViewById(R.id.martes);
        final CheckBox miercoles = (CheckBox) dialogView.findViewById(R.id.miercoles);
        final CheckBox jueves = (CheckBox) dialogView.findViewById(R.id.jueves);
        final CheckBox viernes = (CheckBox) dialogView.findViewById(R.id.viernes);
        final CheckBox sabado = (CheckBox) dialogView.findViewById(R.id.sabado);
        final CheckBox domingo = (CheckBox) dialogView.findViewById(R.id.domingo);

        horaMedicacion.setIs24HourView(true);
        dialogBuilder.setTitle("Añadir medicación");
        dialogBuilder.setPositiveButton(getString(R.string.guardar), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
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

    @Override
    public void processFinish(String response) {
        if(response.equals("datos_sensor")) {
            GestorBD bd = new GestorBD(getApplicationContext());
            bd.vaciarTabla("TB_DATOS_SENSOR");
            bd.updateEnviado("S", Constantes.TB_POSICINES);
            bd.updateEnviado("S", Constantes.TB_TEMBLORES);
        }

        Toast.makeText(getApplicationContext(), "Datos enviados correctamente", Toast.LENGTH_SHORT).show();
    }

    private void enviarDatosServidor(String url, JSONArray params) {
        Log.d("DATOSSSSS", params.toString());
        Servidor servidor = new Servidor(MainActivityActividad.this, url);
        servidor.setDelegate(MainActivityActividad.this);
        servidor.sendData(params, POST);
    }

    @Override
    public void onStart() {
        super.onStart();
        invalidateOptionsMenu();
    }
}
