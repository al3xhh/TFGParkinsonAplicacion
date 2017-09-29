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

import tfg.ucm.com.tfgparkinson.Classes.Temblor;
import tfg.ucm.com.tfgparkinson.R;

public class HistorialActiviy extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);

        Temblor temblor1 = new Temblor(new Date(), "18:00", 1, "Estaba tecleando");
        Temblor temblor2 = new Temblor(new Date(), "20:00", 1, "Estaba usando el móvil");
        Temblor temblor3 = new Temblor(new Date(), "21:00", 1, "Estaba fregando");
        Temblor temblor4 = new Temblor(new Date(), "21:00", 1, "Estaba fregando");
        Temblor temblor5 = new Temblor(new Date(), "14:00", 1, "Estaba andando");
        Temblor temblor6 = new Temblor(new Date(), "15:00", 1, "Estaba leyendo");
        Temblor temblor7 = new Temblor(new Date(), "18:00", 1, "Estaba tecleando");
        Temblor temblor8 = new Temblor(new Date(), "20:00", 1, "Estaba usando el móvil");
        Temblor temblor9 = new Temblor(new Date(), "20:00", 1, "Estaba fregando");
        Temblor temblor10 = new Temblor(new Date(), "20:00", 1, "Estaba andando");
        Temblor temblor11 = new Temblor(new Date(), "20:00", 1, "Estaba leyendo");
        Temblor temblor12 = new Temblor(new Date(), "20:00", 1, "Estaba usando el móvil");
        Temblor temblor13 = new Temblor(new Date(), "20:00", 1, "Estaba fregando");
        Temblor temblor14 = new Temblor(new Date(), "20:00", 1, "Estaba andando");
        Temblor temblor15 = new Temblor(new Date(), "20:00", 1, "Estaba leyendo");

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

        for(int i = 0; i < temblores.size(); i ++) {
            final Temblor temblor = temblores.get(i);
            if(i % 3 == 0) {
                vistaActual = layoutInflater.inflate(R.layout.dia_grafico, null, false);
                textoActual = (TextView) vistaActual.findViewById(R.id.fecha);
                textoActual.setText("12/07/17");
            } else {
                vistaActual = layoutInflater.inflate(R.layout.temblor_grafico, null, false);
                textoActual = (TextView) vistaActual.findViewById(R.id.observacionesTemblor);
                textoActual.setText("Observaciones: " + temblores.get(i).getObservaciones());
                textoActual = (TextView) vistaActual.findViewById(R.id.horaTemblor);
                textoActual.setText("Hora: " + temblores.get(i).getHora());
                ImageView opcionesTemblor = (ImageView) vistaActual.findViewById(R.id.opcionesTemblor);
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
                                    case R.id.borra_temblor:
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
                //textoActual = (TextView) vistaActual.findViewById(R.id.duracionTemblor);
                //textoActual.setText(temblores.get(i).getDuracion());
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

        final DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.editarFecha);
        final TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.editarHora);
        //EditText duracion = (EditText) dialogView.findViewById(R.id.editarDuracion);
        final EditText observaciones = (EditText) dialogView.findViewById(R.id.editarObservaciones);


        datePicker.updateDate(year, month, day);
        timePicker.setIs24HourView(true);
        timePicker.setHour(Integer.parseInt(hora[0]));
        timePicker.setMinute(Integer.parseInt(hora[0]));
        //duracion.setText(temblor.getDuracion());
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
