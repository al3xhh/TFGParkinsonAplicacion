package tfg.ucm.com.tfgparkinson.Activities;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;

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

        reiniciarActivity();

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
                    Medicamento medicamento = new Medicamento(nombreMedicacion.getText().toString(),
                            Integer.parseInt(intervaloMedicacion.getText().toString()), horaMedicacion.getHour() + ":" +
                            horaMedicacion.getMinute(), dias);

                    GestorBD bd = new GestorBD(getApplicationContext());
                    bd.insertMedicamento(medicamento);
                    Toast.makeText(getApplicationContext(), R.string.exito_registro_medicamento, Toast.LENGTH_SHORT).show();
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


        dialogBuilder.setView(dialogView);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
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
}
