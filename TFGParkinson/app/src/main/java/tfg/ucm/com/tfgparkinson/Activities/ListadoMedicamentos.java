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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;

import tfg.ucm.com.tfgparkinson.Clases.BBDD.GestorBD;
import tfg.ucm.com.tfgparkinson.Clases.Medicamento;
import tfg.ucm.com.tfgparkinson.R;

/**
 * Created by al3x_hh on 11/12/2017.
 */

public class ListadoMedicamentos extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);

        GestorBD bd = new GestorBD(this);
        ArrayList<Medicamento> medicamentos = bd.getMedicamentos();

        LinearLayout historial = (LinearLayout) findViewById(R.id.historial);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(getApplicationContext().LAYOUT_INFLATER_SERVICE);
        View vistaActual;
        TextView textoActual;

        if(medicamentos.size() == 0) {
            ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);
            scrollView.setVisibility(View.GONE);
        }

        for(int i = 0; i < medicamentos.size(); i ++) {
            final Medicamento medicamento = medicamentos.get(i);

            vistaActual = layoutInflater.inflate(R.layout.representacion_medicamento, null, false);
            textoActual = (TextView) vistaActual.findViewById(R.id.nombreMedicamento);
            textoActual.setText("Nombre: " + medicamento.getNombre());
            textoActual = (TextView) vistaActual.findViewById(R.id.diasMedicamento);
            textoActual.setText("Dias: " + medicamento.getDiasFormateados());
            textoActual = (TextView) vistaActual.findViewById(R.id.horaMedicamento);
            textoActual.setText("Hora: " + medicamento.getHora());
            textoActual = (TextView) vistaActual.findViewById(R.id.tiempoMedicamento);
            textoActual.setText("Intervalo: " + medicamento.getIntervalo() + " minutos");
            ImageView opcionesTemblor = (ImageView) vistaActual.findViewById(R.id.opcionesActividad);
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
        nombre.setEnabled(false);
        nombre.setEnabled(false);
        intervalo.setText(String.valueOf(medicamento.getIntervalo()));
        horaMedicacion.setIs24HourView(true);
        horaMedicacion.setHour(Integer.parseInt(medicamento.getHora().split(":")[0]));
        horaMedicacion.setMinute(Integer.parseInt(medicamento.getHora().split(":")[1]));

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
                    Medicamento medicamento = new Medicamento(nombre.getText().toString(),
                            Integer.parseInt(intervalo.getText().toString()), horaMedicacion.getHour() + ":" +
                            horaMedicacion.getMinute(), dias);

                    GestorBD bd = new GestorBD(getApplicationContext());
                    bd.updateMedicamento(medicamento);
                    reiniciarActivity();
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
                GestorBD bd = new GestorBD(getApplicationContext());
                bd.deleteMedicamento(medicamento);
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
