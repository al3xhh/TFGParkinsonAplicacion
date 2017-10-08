package tfg.ucm.com.tfgparkinson.Activities;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.GregorianCalendar;

import tfg.ucm.com.tfgparkinson.Clases.BBDD.GestorBD;
import tfg.ucm.com.tfgparkinson.Clases.Temblor;
import tfg.ucm.com.tfgparkinson.R;

public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button anyadirTemblor = (Button) findViewById(R.id.botonAnyadirTemblor);
        anyadirTemblor.setOnClickListener(v -> anyadirTemblor());

        final Button estoyTemblando = (Button) findViewById(R.id.botonEstoyTemblando);
        estoyTemblando.setOnClickListener(v -> estoyTemblando());

        Button verHistorial = (Button) findViewById(R.id.botonVerHistorial);
        verHistorial.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, HistorialActivity.class);
            startActivity(i);
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.reconocer_sensores) {
            Intent i = new Intent(MainActivity.this, ReconocerSensoresActivity.class);
            startActivity(i);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.reconocer_sensores, menu);

        return true;
    }

    /**
     * Función que añade un temblor concreto.
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void anyadirTemblor() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.anyadir_temblor, null);

        final DatePicker fechaTemblor = (DatePicker) dialogView.findViewById(R.id.anyadirFecha);
        fechaTemblor.setVisibility(View.GONE);

        final TimePicker horaTemblor = (TimePicker) dialogView.findViewById(R.id.anyadirHora);
        horaTemblor.setIs24HourView(true);
        horaTemblor.setVisibility(View.GONE);

        final Button cambiarFecha = (Button) dialogView.findViewById(R.id.botonCambiarFecha);
        final Button cambiarHora = (Button) dialogView.findViewById(R.id.botonCambiarHora);
        final EditText duracionTemblor = (EditText) dialogView.findViewById(R.id.anyadirDuracion);
        final EditText observacionesTemblor = (EditText) dialogView.findViewById(R.id.anyadirObservaciones);

        cambiarFecha.setOnClickListener(v -> {
            if(cambiarHora.isEnabled())
                cambiarHora.setEnabled(false);
            else
                cambiarHora.setEnabled(true);
            if(fechaTemblor.getVisibility() == View.GONE)
                fechaTemblor.setVisibility(View.VISIBLE);
            else
                fechaTemblor.setVisibility(View.GONE);
        });

        cambiarHora.setOnClickListener(v -> {
            if(cambiarFecha.isEnabled())
                cambiarFecha.setEnabled(false);
            else
                cambiarFecha.setEnabled(true);
            if(horaTemblor.getVisibility() == View.GONE)
                horaTemblor.setVisibility(View.VISIBLE);
            else
                horaTemblor.setVisibility(View.GONE);
        });

        dialogBuilder.setPositiveButton(R.string.guardar, (dialog, which) -> {
            Temblor temblor = new Temblor(fechaTemblor.getYear() + "/" + fechaTemblor.getMonth() + "/" + fechaTemblor.getDayOfMonth(),
                    horaTemblor.getHour() + ":" + horaTemblor.getMinute(), Integer.parseInt(duracionTemblor.getText().toString()),
                    observacionesTemblor.getText().toString());
            GestorBD bd = new GestorBD(this);
            bd.insertTemblor(temblor);
        });

        dialogBuilder.setNegativeButton(R.string.cancelar, (dialog, which) -> {
        });

        dialogBuilder.setView(dialogView);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    /**
     * Función que añade un temblor, pero con la fecha y hora actuales sin que las elija el usuario.
     */
    private void estoyTemblando(){
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.estoy_temblando, null);

        final EditText duracionTemblor = (EditText) dialogView.findViewById(R.id.estoyTemblandoDuracion);
        final EditText observacionesTemblor = (EditText) dialogView.findViewById(R.id.estoyTemblandoObservaciones);

        dialogBuilder.setPositiveButton(getString(R.string.guardar), (dialog, which) -> {
            Calendar fechaActual = GregorianCalendar.getInstance();

            Temblor temblor = new Temblor(Integer.parseInt(duracionTemblor.getText().toString()), observacionesTemblor.getText().toString());
            GestorBD bd = new GestorBD(this);
            bd.insertTemblor(temblor);
        });

        dialogBuilder.setNegativeButton(getString(R.string.cancelar), (dialog, which) -> {
        });

        dialogBuilder.setView(dialogView);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }
}
