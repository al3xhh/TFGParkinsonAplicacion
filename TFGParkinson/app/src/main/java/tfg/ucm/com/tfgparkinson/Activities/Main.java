package tfg.ucm.com.tfgparkinson.Activities;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import tfg.ucm.com.tfgparkinson.Clases.BBDD.GestorBD;
import tfg.ucm.com.tfgparkinson.Clases.Medicamento;
import tfg.ucm.com.tfgparkinson.Clases.utils.Constantes;
import tfg.ucm.com.tfgparkinson.Clases.utils.RespuestaServidor;
import tfg.ucm.com.tfgparkinson.Clases.utils.Servidor;
import tfg.ucm.com.tfgparkinson.R;

import static com.android.volley.Request.Method.POST;

public class Main extends AppCompatActivity implements RespuestaServidor {

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
                Intent intent = new Intent(Main.this, Actividades.class);
                startActivity(intent);
            }
        });

        Button medicamentos = (Button) findViewById(R.id.medicamentos);
        medicamentos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main.this, Medicamentos.class);
                startActivity(intent);
            }
        });

        GestorBD bd = new GestorBD(this);
        ArrayList<Medicamento> listaMedicamentos = bd.getMedicamentos();

        for (final Medicamento m : listaMedicamentos) {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(m.getHora().split(":")[0]));
            cal.set(Calendar.MINUTE, Integer.parseInt(m.getHora().split(":")[1]));
            cal.set(Calendar.SECOND, 0);

            Date d = cal.getTime();
            Date now = new Date();
            long delay = d.getTime() - now.getTime();

            if (delay > 0) {
                ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
                ses.schedule(new Runnable() {
                    @Override
                    public void run() {
                        SimpleDateFormat formatLetterDay = new SimpleDateFormat("EEEEE", Locale.getDefault());
                        String letter = formatLetterDay.format(new Date());

                        if (m.getDias().contains(letter))
                            notificacion(m.getNombre());
                    }
                }, delay, TimeUnit.MILLISECONDS); // run in "delay" millis
            }
        }
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
                enviarDatosServidor("http://192.168.1.35:5050/posiciones", posiciones);
            if (sensores.length() > 0)
                enviarDatosServidor("http://192.168.1.35:5050/datos_sensor", sensores);
            if (medicamentos.length() > 0)
                enviarDatosServidor("http://192.168.1.35:5050/medicamentos", medicamentos);
            if (actividades.length() > 0)
                enviarDatosServidor("http://192.168.1.35:5050/actividades", actividades);
            if (temblores.length() > 0)
                enviarDatosServidor("http://192.168.1.35:5050/temblores", temblores);
        } else {
            i = new Intent(Main.this, ConfigurarSensores.class);
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
        Servidor servidor = new Servidor(Main.this, url);
        servidor.setDelegate(Main.this);
        servidor.sendData(params, POST);
    }

    @Override
    public void onStart() {
        super.onStart();
        invalidateOptionsMenu();
    }


    public void notificacion(String nombre) {
        Intent intent = new Intent(this, RecibidorNotificaciones.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

        Intent intentConfirm = new Intent(this, RecibidorNotificaciones.class);
        intentConfirm.setAction("CONFIRM");
        intentConfirm.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


        Intent intentCancel = new Intent(this, RecibidorNotificaciones.class);
        intentCancel.setAction("CANCEL");
        intentCancel.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntentConfirm = PendingIntent.getBroadcast(this, 0, intentConfirm, PendingIntent.FLAG_CANCEL_CURRENT);
        PendingIntent pendingIntentCancel = PendingIntent.getBroadcast(this, 1, intentCancel, PendingIntent.FLAG_CANCEL_CURRENT);

        // Build notification
        // Actions are just fake
        Notification noti = new Notification.Builder(this)
                .setContentTitle("¿Se ha tomado la pastilla " + nombre + "?")
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
