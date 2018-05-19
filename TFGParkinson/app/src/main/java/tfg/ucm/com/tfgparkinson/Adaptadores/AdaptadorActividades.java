package tfg.ucm.com.tfgparkinson.Adaptadores;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;

import tfg.ucm.com.tfgparkinson.Clases.Actividad;
import tfg.ucm.com.tfgparkinson.Clases.BBDD.GestorBD;
import tfg.ucm.com.tfgparkinson.R;

/**
 * Created by al3x_hh on 08/04/2018.
 */
public class AdaptadorActividades extends ArrayAdapter<Actividad> {

    private final Context context;
    private final ArrayList<Actividad> values;

    public AdaptadorActividades(@NonNull Context context, ArrayList<Actividad> values) {
        super(context, R.layout.representacion_actividad, values);

        this.context = context;
        this.values = values;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        @SuppressLint("ViewHolder") View rowView = inflater.inflate(R.layout.representacion_actividad, parent, false);
        rowView.setClickable(false);

        TextView nombreActividad = (TextView) rowView.findViewById(R.id.nombreActividad);
        TextView duracionActividad = (TextView) rowView.findViewById(R.id.duracionActividad);
        TextView horaActividad = (TextView) rowView.findViewById(R.id.horaActividad);
        TextView observacionesActividad = (TextView) rowView.findViewById(R.id.observacionesActividad);
        ImageView opcionesActividad = (ImageView) rowView.findViewById(R.id.opcionesActividad);

        nombreActividad.setText(Html.fromHtml("<b>Nombre: </b>" + values.get(position).getNombre()));
        duracionActividad.setText(Html.fromHtml("<b>Duración: </b>" + String.valueOf(values.get(position).getIntervalo()) + " mins"));
        horaActividad.setText(Html.fromHtml("<b>Hora inicio: </b>" + values.get(position).getHora().toString()));
        observacionesActividad.setText(Html.fromHtml("<b>Observaciones: </b>" + values.get(position).getObservaciones()));

        opcionesActividad.setOnClickListener(new View.OnClickListener() {
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
        View dialogView = inflater.inflate(R.layout.anyadir_actividad, null);

        final EditText nombreActividad = (EditText) dialogView.findViewById(R.id.seleccionarActividad);
        final EditText duracion = (EditText) dialogView.findViewById(R.id.duracionActividad);
        final TimePicker horaInicio = (TimePicker) dialogView.findViewById(R.id.horaInicioActividad);
        final DatePicker fechaAcitivdad = (DatePicker) dialogView.findViewById(R.id.fechaActividad);
        final EditText observaciones = (EditText) dialogView.findViewById(R.id.observacionesActividad);

        duracion.setRawInputType(Configuration.KEYBOARD_12KEY);
        horaInicio.setIs24HourView(true);

        Actividad actividad = values.get(position);
        nombreActividad.setText(actividad.getNombre());
        nombreActividad.setEnabled(false);
        duracion.setText(String.valueOf(actividad.getIntervalo()));
        horaInicio.setHour(Integer.parseInt((actividad.getHora().split(":")[0])));
        horaInicio.setMinute(Integer.parseInt((actividad.getHora().split(":")[1])));
        fechaAcitivdad.updateDate(Integer.parseInt(actividad.getFeha().split("/")[2]),
                Integer.parseInt(actividad.getFeha().split("/")[1]),
                Integer.parseInt(actividad.getFeha().split("/")[0]));
        fechaAcitivdad.setFirstDayOfWeek(Integer.parseInt(actividad.getFeha().split("/")[2]));
        observaciones.setText(actividad.getObservaciones());

        dialogBuilder.setTitle("Editar actividad");
        dialogBuilder.setPositiveButton(context.getString(R.string.guardar), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    Actividad actividad = new Actividad(nombreActividad.getText().toString(),
                            Integer.parseInt(duracion.getText().toString()),
                            horaInicio.getHour() + ":" + horaInicio.getMinute(),
                            fechaAcitivdad.getDayOfMonth() + "/" + fechaAcitivdad.getMonth() + "/" + fechaAcitivdad.getYear(),
                            observaciones.getText().toString());
                    GestorBD gestorBD = new GestorBD(context);
                    gestorBD.updateActividad(actividad);
                    Toast.makeText(context, R.string.exito_editar_actividad, Toast.LENGTH_SHORT).show();
                    values.remove(position);
                    values.add(actividad);
                    notifyDataSetChanged();
                } catch (NumberFormatException e) {
                    Toast.makeText(context, R.string.duracion_no_valida, Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialogBuilder.setNegativeButton(context.getString(R.string.cancelar), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        dialogBuilder.setView(dialogView);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    private void borrar(final int position) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setTitle("Borrar Actividad");
        dialogBuilder.setMessage("¿Realmente desea borrar la actividad?");
        dialogBuilder.setPositiveButton("Borrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                GestorBD bd = new GestorBD(context);
                bd.deleteActividad(values.get(position));
                values.remove(position);
                notifyDataSetChanged();
                Toast.makeText(context, R.string.exito_borrar_actividad, Toast.LENGTH_SHORT).show();
                //context.startActivity(((Activity)context).getIntent());
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
