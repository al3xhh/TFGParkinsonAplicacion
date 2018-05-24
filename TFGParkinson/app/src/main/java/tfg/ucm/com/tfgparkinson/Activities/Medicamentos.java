package tfg.ucm.com.tfgparkinson.Activities;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import tfg.ucm.com.tfgparkinson.Adaptadores.AdaptadorMedicamentos;
import tfg.ucm.com.tfgparkinson.Clases.BBDD.GestorBD;
import tfg.ucm.com.tfgparkinson.Clases.Medicamento;
import tfg.ucm.com.tfgparkinson.R;

/**
 * Created by al3x_hh on 08/04/2018.
 */

public class Medicamentos extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicamentos);
        setTitle("Medicamentos");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        reiniciarActivity();

        FloatingActionButton ayuda = (FloatingActionButton) findViewById(R.id.ayuda);
        ayuda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarAyuda();
            }
        });

        FloatingActionButton addMedicine = (FloatingActionButton) findViewById(R.id.addMedicineB);
        addMedicine.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                anyadirMedicacion();
            }
        });
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
        intervaloMedicacion.setRawInputType(Configuration.KEYBOARD_12KEY);
        dialogBuilder.setTitle("Añadir medicamento");
        dialogBuilder.setPositiveButton(getString(R.string.guardar), new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ArrayList<String> dias = new ArrayList<>();

                if(lunes.isChecked())
                    dias.add("L");
                if(martes.isChecked())
                    dias.add("M");
                if(miercoles.isChecked())
                    dias.add("X");
                if(jueves.isChecked())
                    dias.add("J");
                if(viernes.isChecked())
                    dias.add("V");
                if(sabado.isChecked())
                    dias.add("S");
                if(domingo.isChecked())
                    dias.add("D");

                try {
                    final Medicamento medicamento = new Medicamento(nombreMedicacion.getText().toString(),
                            Integer.parseInt(intervaloMedicacion.getText().toString()), horaMedicacion.getHour() + ":" +
                            horaMedicacion.getMinute(), dias);

                    GestorBD bd = new GestorBD(getApplicationContext());
                    bd.insertMedicamento(medicamento);
                    Toast.makeText(getApplicationContext(), R.string.exito_registro_medicamento, Toast.LENGTH_SHORT).show();
                    reiniciarActivity();
                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(medicamento.getHora().split(":")[0]));
                    cal.set(Calendar.MINUTE, Integer.parseInt(medicamento.getHora().split(":")[1]));
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

                                if (medicamento.getDias().contains(letter))
                                    notificacion(medicamento.getNombre());
                            }
                        }, delay, TimeUnit.MILLISECONDS); // run in "delay" millis
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(getApplicationContext(), R.string.intervalo_no_valido, Toast.LENGTH_SHORT).show();
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

    private void reiniciarActivity() {
        GestorBD bd = new GestorBD(this);
        ArrayList<Medicamento> medicamentos = bd.getMedicamentos();
        ListView medicamentosLV = (ListView) findViewById(R.id.medicamentosLV);
        TextView noMedicamentosTV = (TextView) findViewById(R.id.noMedicines);

        if(medicamentos.size() == 0) {
            noMedicamentosTV.setVisibility(View.VISIBLE);
            medicamentosLV.setVisibility(View.GONE);
        } else {
            noMedicamentosTV.setVisibility(View.GONE);
            medicamentosLV.setVisibility(View.VISIBLE);
            medicamentosLV.setAdapter(new AdaptadorMedicamentos(this, medicamentos));
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
        dialogBuilder.setTitle("Ayuda medicamentos");
        dialogBuilder.setMessage(Html.fromHtml("<p align=\"justify\">" +
                "- Para añadir un medicamento pulse en <strong>el botón de abajo a la derecha</strong> y rellene los campos. <br><br>" +
                "- Para editar un medicamento pulse <strong>dentro de los tres puntitos del medicamento en editar</strong>. <br><br>" +
                "- Para borrar un medicamento pulse <strong>dentro de los tres puntitos del medicamento en borrar</strong>.</p>"));

        dialogBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }
}
