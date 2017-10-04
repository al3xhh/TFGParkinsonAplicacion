package tfg.ucm.com.tfgparkinson.Activities;

import android.content.DialogInterface;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import tfg.ucm.com.tfgparkinson.Clases.Temblor;
import tfg.ucm.com.tfgparkinson.R;

public class HistorialActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);

        Temblor temblor1 = new Temblor(new Date(), "18:00", 1, "Estaba tecleando");
        Temblor temblor2 = new Temblor(new Date(), "20:00", 2, "Estaba usando el móvil");
        Temblor temblor3 = new Temblor(new Date(), "21:00", 4, "Estaba fregando");
        Temblor temblor4 = new Temblor(new Date(), "21:00", 5, "Estaba fregando");
        Temblor temblor5 = new Temblor(new Date(), "14:00", 5, "Estaba andando");
        Temblor temblor6 = new Temblor(new Date(), "15:00", 8, "Estaba leyendo");
        Temblor temblor7 = new Temblor(new Date(), "18:00", 8, "Estaba tecleando");
        Temblor temblor8 = new Temblor(new Date(), "20:00", 13, "Estaba usando el móvil");
        Temblor temblor9 = new Temblor(new Date(), "20:00", 9, "Estaba fregando");
        Temblor temblor10 = new Temblor(new Date(), "20:00", 34, "Estaba andando");
        Temblor temblor11 = new Temblor(new Date(), "20:00", 3, "Estaba leyendo");
        Temblor temblor12 = new Temblor(new Date(), "20:00", 9, "Estaba usando el móvil");
        Temblor temblor13 = new Temblor(new Date(), "20:00", 2, "Estaba fregando");
        Temblor temblor14 = new Temblor(new Date(), "20:00", 78, "Estaba andando");
        Temblor temblor15 = new Temblor(new Date(), "20:00", 9, "Estaba leyendo");

        final ArrayList<Temblor> temblores = new ArrayList<>();
        temblores.add(temblor1);
        temblores.add(temblor2);
        temblores.add(temblor3);
        temblores.add(temblor4);
        temblores.add(temblor5);
        temblores.add(temblor6);
        temblores.add(temblor7);
        temblores.add(temblor8);
        temblores.add(temblor9);
        temblores.add(temblor10);
        temblores.add(temblor11);
        temblores.add(temblor12);
        temblores.add(temblor13);
        temblores.add(temblor14);
        temblores.add(temblor15);

        LinearLayout historial = (LinearLayout) findViewById(R.id.historial);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(getApplicationContext().LAYOUT_INFLATER_SERVICE);
        View vistaActual;
        TextView textoActual;
        int colorActual = getColor(R.color.colorIndigo);

        for(int i = 0; i < temblores.size(); i ++) {
            final Temblor temblor = temblores.get(i);
            if (i % 3 == 0) {
                vistaActual = layoutInflater.inflate(R.layout.representacion_dia, null, false);
                vistaActual.setBackgroundColor(colorActual);
                textoActual = (TextView) vistaActual.findViewById(R.id.fechaTemblor);
                textoActual.setText("12/07/17");
            } else {
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
                    public void onClick(View view) {
                        PopupMenu popup = new PopupMenu(getBaseContext(), view);
                        popup.getMenuInflater().inflate(R.menu.opciones_temblor, popup.getMenu());
                        popup.show();
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.M)
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                switch (menuItem.getItemId()) {
                                    case R.id.borrar_temblor:
                                        //TODO habrá que pasar el id del temblor que se quiere borrar
                                        borrar();
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
                //TODO CAMBIAR ESTO POR LOS TEMBLORES DE CADA DIA
                if((i - 2) % 3 == 0) {
                    if(colorActual == getColor(R.color.colorIndigo))
                        colorActual = getColor(R.color.colorBlue);
                    else
                        colorActual = getColor(R.color.colorIndigo);
                }
            }

            historial.addView(vistaActual);
        }
    }

    /**
     *
     * @param temblor
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void editar(final Temblor temblor) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.editar_temblor, null);
        dialogBuilder.setView(dialogView);

        String[] hora = temblor.getHora().split(":");
        Calendar cal = Calendar.getInstance();
        cal.setTime(temblor.getFecha());
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.editarFecha);
        TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.editarHora);
        EditText duracion = (EditText) dialogView.findViewById(R.id.editarDuracion);
        EditText observaciones = (EditText) dialogView.findViewById(R.id.editarObservciones);

        datePicker.updateDate(year, month, day);
        timePicker.setIs24HourView(true);
        timePicker.setHour(Integer.parseInt(hora[0]));
        timePicker.setMinute(Integer.parseInt(hora[0]));
        duracion.setText(String.valueOf(temblor.getDuracion()));
        observaciones.setText(temblor.getObservaciones());

        dialogBuilder.setPositiveButton("Editar", new DialogInterface.OnClickListener() {
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

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    /**
     *
     */
    private void borrar() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Borrar temblor");
        dialogBuilder.setMessage("¿Realmente desea borrar el temblor?");
        dialogBuilder.setPositiveButton("Borrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //TODO aquí habra que borrar el temblor del fichero o base de datos
            }
        });
        dialogBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }
}
