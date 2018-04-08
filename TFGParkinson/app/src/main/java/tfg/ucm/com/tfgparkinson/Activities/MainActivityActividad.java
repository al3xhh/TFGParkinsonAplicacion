package tfg.ucm.com.tfgparkinson.Activities;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
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
import org.json.JSONObject;

import java.util.ArrayList;

import tfg.ucm.com.tfgparkinson.Clases.Actividad;
import tfg.ucm.com.tfgparkinson.Clases.BBDD.GestorBD;
import tfg.ucm.com.tfgparkinson.Clases.Medicamento;
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
        setContentView(R.layout.activity_main);

        appContext = getApplicationContext();

        Button actividades = (Button) findViewById(R.id.actividades);
        actividades.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivityActividad.this, ActividadesActivity.class);
                startActivity(intent);
            }
        });

        Button medicamentos = (Button) findViewById(R.id.medicamentos);
        medicamentos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivityActividad.this, MedicamentosActivity.class);
                startActivity(intent);
            }
        });

        /*Button anyadirMedicamento = (Button) findViewById(R.id.botonAnyadirMedicamento);
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
                /*Intent i = new Intent(MainActivityActividad.this, GraficosActivity.class);
                startActivity(i);
                notificacion();
            }
        });

        Button verMedicamentos = (Button) findViewById(R.id.botonVerMedicamentos);
        verMedicamentos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivityActividad.this, ListadoMedicamentosActivity.class);
                startActivity(i);
            }
        });*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent i = null;

        if(item.getItemId() == R.id.enviar_datos) {
            GestorBD bd = new GestorBD(getApplicationContext());
            JSONArray sensores = bd.getTb_datos_sensor();
            JSONArray posiciones = bd.getTb_posiciones();
            JSONArray temblores = bd.getTb_temlobres();
            JSONArray medicamentos = bd.getTb_medicamentos();
            JSONArray actividades = bd.getTb_actividades();

            if (posiciones.length() > 0)
                enviarDatosServidor("http://10.153.44.52:5050/posiciones", posiciones);
            if (sensores.length() > 0)
                enviarDatosServidor("http://10.153.44.52:5050/datos_sensor", sensores);
            if (medicamentos.length() > 0)
                enviarDatosServidor("http://10.153.44.52:5050/medicamentos", medicamentos);
            if (actividades.length() > 0)
                enviarDatosServidor("http://10.153.44.52:5050/actividades", actividades);
            if (temblores.length() > 0)
                enviarDatosServidor("http://10.153.44.52:5050/temblores", temblores);
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
        int medicamentoDataCount = bd.getNumFilas("TB_MEDICAMENTOS");
        int actividadDataCount = bd.getNumFilas("TB_ACTIVIDADES");

        if(sensorDataCount == 0 && medicamentoDataCount == 0 && actividadDataCount == 0)
            menu.getItem(0).setVisible(false);

        return true;
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


    private void notificacion() {
        Intent intent = new Intent(this, NotificationReceiverActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

        Intent intentConfirm = new Intent(this, NotificationReceiverActivity.class);
        intentConfirm.setAction("CONFIRM");
        intentConfirm.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


        Intent intentCancel = new Intent(this, NotificationReceiverActivity.class);
        intentCancel.setAction("CANCEL");
        intentCancel.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntentConfirm = PendingIntent.getBroadcast(this, 0, intentConfirm, PendingIntent.FLAG_CANCEL_CURRENT);
        PendingIntent pendingIntentCancel = PendingIntent.getBroadcast(this, 1, intentCancel, PendingIntent.FLAG_CANCEL_CURRENT);

        // Build notification
        // Actions are just fake
        Notification noti = new Notification.Builder(this)
                .setContentTitle("¿Se ha tomado la pastilla?")
                .setSmallIcon(R.drawable.ic_access_time_black_24dp)
                .setContentIntent(pIntent)
                .addAction(R.drawable.ic_done_black_24dp, "Si", pendingIntentConfirm)
                .addAction(R.drawable.ic_clear_black_24dp, "No", pendingIntentCancel)
                .build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // hide the notification after its selected
        noti.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(0, noti);
    }
}
