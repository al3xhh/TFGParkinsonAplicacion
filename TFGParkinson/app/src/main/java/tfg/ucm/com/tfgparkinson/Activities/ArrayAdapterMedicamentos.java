package tfg.ucm.com.tfgparkinson.Activities;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;

import tfg.ucm.com.tfgparkinson.Clases.BBDD.GestorBD;
import tfg.ucm.com.tfgparkinson.Clases.Medicamento;
import tfg.ucm.com.tfgparkinson.Clases.Medicamento;
import tfg.ucm.com.tfgparkinson.R;

/**
 * Created by al3x_hh on 08/04/2018.
 */

public class ArrayAdapterMedicamentos extends ArrayAdapter<Medicamento> {

    private final Context context;
    private final ArrayList<Medicamento> values;

    public ArrayAdapterMedicamentos(@NonNull Context context, ArrayList<Medicamento> values) {
        super(context, R.layout.representacion_medicamento, values);

        this.context = context;
        this.values = values;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        @SuppressLint("ViewHolder") View rowView = inflater.inflate(R.layout.representacion_medicamento, parent, false);
        rowView.setClickable(false);

        TextView nombreMedicamento = (TextView) rowView.findViewById(R.id.nombreMedicamento);
        TextView diasMedicamento = (TextView) rowView.findViewById(R.id.diasMedicamento);
        TextView horaMedicamento = (TextView) rowView.findViewById(R.id.horaMedicamento);
        TextView tiempoMedicamento = (TextView) rowView.findViewById(R.id.tiempoMedicamento);
        ImageView opcionesMedicamento = (ImageView) rowView.findViewById(R.id.opcionesMedicamento);

        nombreMedicamento.setText(Html.fromHtml("<b>Nombre: </b>" + values.get(position).getNombre()));
        tiempoMedicamento.setText(Html.fromHtml("<b>Intervalo: </b>" + String.valueOf(values.get(position).getIntervalo()) + " mins"));
        horaMedicamento.setText(Html.fromHtml("<b>Hora toma: </b>" + values.get(position).getHora()));
        diasMedicamento.setText(Html.fromHtml("<b>Días: </b>" + values.get(position).getDiasFormateados()));

        opcionesMedicamento.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ViewHolder")
            @Override
            public void onClick(View v) {
                final PopupMenu popup = new PopupMenu(context, v);
                popup.getMenuInflater().inflate(R.menu.opciones_temblor, popup.getMenu());
                popup.show();
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.borrar_temblor:
                                borrar(position);
                                context.startActivity(((Activity)context).getIntent());
                                break;
                            case R.id.editar_temblor:
                                editar(position);
                                break;
                        }
                        return true;
                    }
                });
            }
        });

        return rowView;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void editar(final int position) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

        Medicamento medicamento = values.get(position);
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

                    GestorBD bd = new GestorBD(context);
                    bd.updateMedicamento(medicamento);
                    values.remove(position);
                    values.add(medicamento);
                    notifyDataSetChanged();
                    Toast.makeText(context, R.string.exito_editar_medicamento, Toast.LENGTH_SHORT).show();
                } catch (NumberFormatException e) {
                    Toast.makeText(context, R.string.intervalo_no_valido, Toast.LENGTH_SHORT).show();
                }

            }
        });

        dialogBuilder.setNegativeButton(context.getString(R.string.cancelar), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    private void borrar(final int position) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setTitle("Borrar mendicamento");
        dialogBuilder.setMessage("¿Realmente desea borrar el medicamento?");
        dialogBuilder.setPositiveButton("Borrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                GestorBD bd = new GestorBD(context);
                bd.deleteMedicamento(values.get(position));
                values.remove(position);
                notifyDataSetChanged();
                Toast.makeText(context, R.string.exito_borrar_medicamento, Toast.LENGTH_SHORT).show();
            }
        });

        dialogBuilder.setNegativeButton(context.getString(R.string.cancelar), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }
}
