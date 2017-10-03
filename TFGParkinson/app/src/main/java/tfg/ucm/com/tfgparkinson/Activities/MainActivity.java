package tfg.ucm.com.tfgparkinson.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import tfg.ucm.com.tfgparkinson.R;

public class MainActivity extends AppCompatActivity {

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

        Button estoyTemblando = (Button) findViewById(R.id.botonEstoyTemblando);
        estoyTemblando.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anyadirTemblor();
            }
        });

        Button verHIstorial = (Button) findViewById(R.id.botonVerHistorial);
        verHIstorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, HistorialActivity.class);
                startActivity(i);

            }
        });
    }

    private void anyadirTemblor() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.editar_anyadir_temblor, null);

        final DatePicker fechaTemblor = (DatePicker) dialogView.findViewById(R.id.editarAnyadirFecha);
        fechaTemblor.setVisibility(View.GONE);

        final TimePicker horaTemblor = (TimePicker) dialogView.findViewById(R.id.editarAnyadirHora);
        horaTemblor.setIs24HourView(true);
        horaTemblor.setVisibility(View.GONE);

        final Button cambiarFecha = (Button) dialogView.findViewById(R.id.botonCambiarFecha);
        final Button cambiarHora = (Button) dialogView.findViewById(R.id.botonCambiarHora);

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

        dialogBuilder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //TODO aquí habrá que hacer persistentes los cambios en el fichero o base de datos
            }
        });

        dialogBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        dialogBuilder.setView(dialogView);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }
}
