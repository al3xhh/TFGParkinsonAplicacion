package tfg.ucm.com.tfgparkinson.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
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
import tfg.ucm.com.tfgparkinson.Clases.Medicamento;
import tfg.ucm.com.tfgparkinson.Clases.Temblor;
import tfg.ucm.com.tfgparkinson.R;

/**
 * Created by al3x_hh on 11/12/2017.
 */

public class ListadoMedicamentosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);

        LinearLayout historial = (LinearLayout) findViewById(R.id.historial);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(getApplicationContext().LAYOUT_INFLATER_SERVICE);
        View vistaActual;
        TextView textoActual;

        ArrayList<Medicamento> medicamentos = new ArrayList<>();
        ArrayList<String> m1dias = new ArrayList<>();
        m1dias.add("L");
        m1dias.add("X");
        m1dias.add("V");
        Medicamento m1 = new Medicamento("Medicamento 1", "3", new Timestamp(System.currentTimeMillis()), m1dias);
        medicamentos.add(m1);
        ArrayList<String> m2dias = new ArrayList<>();
        m2dias.add("M");
        m2dias.add("J");
        m2dias.add("D");
        Medicamento m2 = new Medicamento("Medicamento 2", "4", new Timestamp(System.currentTimeMillis()), m2dias);
        medicamentos.add(m2);

        for(int i = 0; i < medicamentos.size(); i ++) {
            final Medicamento medicamento = medicamentos.get(i);

            vistaActual = layoutInflater.inflate(R.layout.representacion_medicamento, null, false);
            textoActual = (TextView) vistaActual.findViewById(R.id.nombreMedicamento);
            textoActual.setText("Nombre: " + medicamento.getNombre());
            textoActual = (TextView) vistaActual.findViewById(R.id.diasMedicamento);
            textoActual.setText("Dias: " + medicamento.getDiasFormateados());
            textoActual = (TextView) vistaActual.findViewById(R.id.horaMedicamento);
            textoActual.setText("Hora: " + medicamento.getTimestamp());
            textoActual = (TextView) vistaActual.findViewById(R.id.tiempoMedicamento);
            textoActual.setText("Intervalo: " + medicamento.getIntervalo());
            ImageView opcionesTemblor = (ImageView) vistaActual.findViewById(R.id.opcionesMedicamento);
            opcionesTemblor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popup = new PopupMenu(getBaseContext(), v);
                    popup.getMenuInflater().inflate(R.menu.opciones_temblor, popup.getMenu());
                    popup.show();
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.M)
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.borrar_temblor:
                                    borrar(medicamento);
                                    break;
                                case R.id.editar_temblor:
                                    editar(medicamento);
                                    break;
                            }
                            return true;
                        }
                    });
                }
            });
            historial.addView(vistaActual);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void editar(final Medicamento medicamento) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.anyadir_medicacion, null);
        dialogBuilder.setView(dialogView);

        final EditText nombre = (EditText) dialogView.findViewById(R.id.nombreMedicacion);
        final EditText intervalo = (EditText) dialogView.findViewById(R.id.intervaloMedicacion);
        final TimePicker horaMedicacion = (TimePicker) dialogView.findViewById(R.id.horaMedicacion);
        final CheckBox lunes = (CheckBox) dialogView.findViewById(R.id.lunes);
        final CheckBox martes = (CheckBox) dialogView.findViewById(R.id.martes);
        final CheckBox miercoles = (CheckBox) dialogView.findViewById(R.id.miercoles);
        final CheckBox jueves = (CheckBox) dialogView.findViewById(R.id.jueves);
        final CheckBox viernes = (CheckBox) dialogView.findViewById(R.id.viernes);
        final CheckBox sabado = (CheckBox) dialogView.findViewById(R.id.sabado);
        final CheckBox domingo = (CheckBox) dialogView.findViewById(R.id.domingo);

        nombre.setText(medicamento.getNombre());
        intervalo.setText(medicamento.getIntervalo());

        int size = medicamento.getDias().size();
        String dia;
        ArrayList<String> dias = medicamento.getDias();

        for(int i = 0; i < size; i ++) {
            dia = dias.get(i);

            if(dia.equals("L"))
                lunes.setChecked(true);
            if(dia.equals("M"))
                martes.setChecked(true);
            if(dia.equals("X"))
                miercoles.setChecked(true);
            if(dia.equals("J"))
                jueves.setChecked(true);
            if(dia.equals("V"))
                viernes.setChecked(true);
            if(dia.equals("S"))
                sabado.setChecked(true);
            if(dia.equals("D"))
                domingo.setChecked(true);
        }

        dialogBuilder.setPositiveButton("Editar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

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

    private void borrar(final Medicamento medicamento) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Borrar mendicamento");
        dialogBuilder.setMessage("¿Realmente desea borrar el medicamento?");
        dialogBuilder.setPositiveButton("Borrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

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
