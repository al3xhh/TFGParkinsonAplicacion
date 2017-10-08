package tfg.ucm.com.tfgparkinson.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.TimePicker;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;

import tfg.ucm.com.tfgparkinson.Clases.BBDD.GestorBD;
import tfg.ucm.com.tfgparkinson.Clases.Temblor;
import tfg.ucm.com.tfgparkinson.R;

/**
 *
 */
public class HistorialActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);

        GestorBD bd = new GestorBD(this);
        ArrayList<Temblor> temblores = bd.getTemblores();

        LinearLayout historial = (LinearLayout) findViewById(R.id.historial);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(getApplicationContext().LAYOUT_INFLATER_SERVICE);
        View vistaActual;
        TextView textoActual;
        int colorActual = getColor(R.color.colorIndigo);
        String fechaActual, fechaAnterior = "";

        for(int i = 0; i < temblores.size(); i ++) {
            final Temblor temblor = temblores.get(i);
            fechaActual = temblor.getFecha();

            if (!fechaActual.equals(fechaAnterior)) {
                vistaActual = layoutInflater.inflate(R.layout.representacion_dia, null, false);
                vistaActual.setBackgroundColor(colorActual);
                textoActual = (TextView) vistaActual.findViewById(R.id.fechaTemblor);
                textoActual.setText(temblor.getFecha());
                historial.addView(vistaActual);
            }
            vistaActual = layoutInflater.inflate(R.layout.representacion_temblor, null, false);
            vistaActual.setBackgroundColor(colorActual);
            textoActual = (TextView) vistaActual.findViewById(R.id.horaTemblor);
            textoActual.setText("Hora: " + temblores.get(i).getHora());
            textoActual = (TextView) vistaActual.findViewById(R.id.duracionTemblor);
            textoActual.setText("Duración: " + temblores.get(i).getDuracion());
            textoActual = (TextView) vistaActual.findViewById(R.id.observacionesTemblor);
            textoActual.setText("Observaciones: " + temblores.get(i).getObservaciones());
            ImageView opcionesTemblor = (ImageView) vistaActual.findViewById(R.id.opcionesTemblor);
            opcionesTemblor.setBackgroundColor(colorActual);
            opcionesTemblor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popup = new PopupMenu(getBaseContext(), v);
                    popup.getMenuInflater().inflate(R.menu.opciones_temblor, popup.getMenu());
                    popup.show();
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.borrar_temblor:
                                    borrar(temblor);
                                    break;
                                case R.id.editar_temblor:
                                    editar(temblor);
                                    break;
                            }
                            return true;
                        }
                    });
                }
            });
            historial.addView(vistaActual);

            if(i < temblores.size() - 1)
                if(!fechaActual.equals(temblores.get(i + 1).getFecha()))
                    colorActual = (colorActual == getColor(R.color.colorIndigo)) ? getColor(R.color.colorBlue) : getColor(R.color.colorIndigo);

            fechaAnterior = temblor.getFecha();
        }
    }

    /**
     * Función que edita un temblor concreto.
     *
     * @param temblor temblor que se quiere editar.
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void editar(final Temblor temblor) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.editar_temblor, null);
        dialogBuilder.setView(dialogView);

        String[] hora = temblor.getHora().split(":");
        String[] fecha = temblor.getFecha().split("/");
        int year = Integer.parseInt(fecha[2]);
        int month = Integer.parseInt(fecha[1]);
        int day = Integer.parseInt(fecha[0]);

        final DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.editarFecha);
        final TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.editarHora);
        final EditText duracion = (EditText) dialogView.findViewById(R.id.editarDuracion);
        final EditText observaciones = (EditText) dialogView.findViewById(R.id.editarObservciones);

        datePicker.updateDate(year, month, day);
        timePicker.setIs24HourView(true);
        timePicker.setHour(Integer.parseInt(hora[0]));
        timePicker.setMinute(Integer.parseInt(hora[0]));
        duracion.setText(String.valueOf(temblor.getDuracion()));
        observaciones.setText(temblor.getObservaciones());

        dialogBuilder.setPositiveButton("Editar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), timePicker.getHour(), timePicker.getMinute());
                Timestamp ts = new Timestamp(calendar.getTimeInMillis());
                temblor.setTimestamp(ts);
                temblor.setDuracion(Integer.parseInt(duracion.getText().toString()));
                temblor.setObservaciones(observaciones.getText().toString());
                GestorBD bd = new GestorBD(getApplicationContext());
                bd.updateTemblor(temblor);
                reiniciarActivity();
            }
        });

        dialogBuilder.setNegativeButton(getString(R.string.cancelar), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    /**
     * Función que borra un temblor concreto.
     *
     * @param temblor temblor que se quiere borrar.
     */
    private void borrar(final Temblor temblor) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Borrar temblor");
        dialogBuilder.setMessage("¿Realmente desea borrar el temblor?");
        dialogBuilder.setPositiveButton("Borrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                GestorBD bd = new GestorBD(getApplicationContext());
                bd.deleteTemblor(temblor);
                reiniciarActivity();
            }
        });

        dialogBuilder.setNegativeButton(getString(R.string.cancelar), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    private void reiniciarActivity() {
        Intent i = getIntent();
        finish();
        startActivity(i);
    }
}
