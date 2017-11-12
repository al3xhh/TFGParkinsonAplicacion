package tfg.ucm.com.tfgparkinson.Activities;

import android.content.DialogInterface;
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
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;

import tfg.ucm.com.tfgparkinson.Clases.BBDD.GestorBD;
import tfg.ucm.com.tfgparkinson.Clases.Temblor;
import tfg.ucm.com.tfgparkinson.Clases.utils.RespuestaServidor;
import tfg.ucm.com.tfgparkinson.Clases.utils.Servidor;
import tfg.ucm.com.tfgparkinson.R;

public class MainActivity extends AppCompatActivity implements RespuestaServidor {

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button anyadirTemblor = (Button) findViewById(R.id.botonAnyadirTemblor);
        anyadirTemblor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anyadirTemblor();
            }
        });

        final Button estoyTemblando = (Button) findViewById(R.id.botonEstoyTemblando);
        estoyTemblando.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                estoyTemblando();
            }
        });

        Button verHistorial = (Button) findViewById(R.id.botonVerHistorial);
        verHistorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, HistorialActivity.class);
                startActivity(i);
            }
        });

        Servidor servidor = new Servidor(getApplicationContext(), "http://192.168.1.35:5000/hello");
        servidor.setDelegate(this);

        HashMap<String, String> params = new HashMap<>();
        params.put("HOLA", "ADIÓS");

        servidor.sendData(params);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent i = null;
        if (item.getItemId() == R.id.emparejar_sensores) {
            i = new Intent(MainActivity.this, ConfigurarSensores.class);
        }

        startActivity(i);

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sensores, menu);

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

        cambiarFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cambiarHora.isEnabled())
                    cambiarHora.setEnabled(false);
                else
                    cambiarHora.setEnabled(true);
                if(fechaTemblor.getVisibility() == View.GONE)
                    fechaTemblor.setVisibility(View.VISIBLE);
                else
                    fechaTemblor.setVisibility(View.GONE);
            }
        });

        cambiarHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cambiarFecha.isEnabled())
                    cambiarFecha.setEnabled(false);
                else
                    cambiarFecha.setEnabled(true);
                if(horaTemblor.getVisibility() == View.GONE)
                    horaTemblor.setVisibility(View.VISIBLE);
                else
                    horaTemblor.setVisibility(View.GONE);
            }
        });

        dialogBuilder.setPositiveButton(getString(R.string.guardar), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(duracionTemblor.getText().toString().trim().length() == 0 ||
                        observacionesTemblor.getText().toString().trim().length() == 0) {
                    Toast.makeText(getApplicationContext(), R.string.campo_vacio, Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        Temblor temblor = new Temblor(fechaTemblor.getDayOfMonth() + "/" + fechaTemblor.getMonth() + "/" + fechaTemblor.getYear(),
                                horaTemblor.getHour() + ":" + horaTemblor.getMinute(), Integer.parseInt(duracionTemblor.getText().toString()),
                                observacionesTemblor.getText().toString());
                        GestorBD bd = new GestorBD(getApplicationContext());
                        bd.insertTemblor(temblor);
                        Toast.makeText(getApplicationContext(), R.string.exito_registro_temblor, Toast.LENGTH_SHORT).show();
                    } catch (NumberFormatException e) {
                        Toast.makeText(getApplicationContext(), R.string.duracion_no_valida, Toast.LENGTH_SHORT).show();
                    }
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
     * Función que añade un temblor, pero con la fecha y hora actuales sin que las elija el usuario.
     */
    private void estoyTemblando(){
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.estoy_temblando, null);

        final EditText duracionTemblor = (EditText) dialogView.findViewById(R.id.estoyTemblandoDuracion);
        final EditText observacionesTemblor = (EditText) dialogView.findViewById(R.id.estoyTemblandoObservaciones);

        dialogBuilder.setPositiveButton(getString(R.string.guardar), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(duracionTemblor.getText().toString().trim().length() == 0 ||
                        observacionesTemblor.getText().toString().trim().length() == 0) {
                    Toast.makeText(getApplicationContext(), getString(R.string.campo_vacio), Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        Temblor temblor = new Temblor(Integer.parseInt(duracionTemblor.getText().toString()), observacionesTemblor.getText().toString());
                        GestorBD bd = new GestorBD(getApplicationContext());
                        bd.insertTemblor(temblor);
                        Toast.makeText(getApplicationContext(), R.string.exito_registro_temblor, Toast.LENGTH_SHORT).show();
                    } catch (NumberFormatException e) {
                        Toast.makeText(getApplicationContext(), getString(R.string.duracion_no_valida), Toast.LENGTH_SHORT).show();
                    }
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

    @Override
    public void processFinish(JSONObject response) {
        Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
    }
}
