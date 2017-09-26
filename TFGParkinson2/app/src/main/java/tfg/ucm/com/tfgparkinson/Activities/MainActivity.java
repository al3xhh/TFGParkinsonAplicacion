package tfg.ucm.com.tfgparkinson.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import tfg.ucm.com.tfgparkinson.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TimePicker hora = (TimePicker) findViewById(R.id.timePicker);
        hora.setIs24HourView(true);

        Button historial = (Button) findViewById(R.id.buttonHistorial);
        historial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, HistorialActiviy.class);
                startActivity(i);
            }
        });

        Button observacion = (Button) findViewById(R.id.buttonTemblorAhora);
        observacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anyadirObservacion();
            }
        });
    }

    private void anyadirObservacion() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Observacion");
        final EditText input = new EditText(this);
        input.setHint(R.string.observacion_hint);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        alert.setView(input);
        alert.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        alert.show();
    }
}
