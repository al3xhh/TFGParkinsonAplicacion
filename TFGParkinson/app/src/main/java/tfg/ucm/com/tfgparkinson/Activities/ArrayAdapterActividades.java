package tfg.ucm.com.tfgparkinson.Activities;


import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import tfg.ucm.com.tfgparkinson.Clases.Actividad;
import tfg.ucm.com.tfgparkinson.Clases.Medicamento;
import tfg.ucm.com.tfgparkinson.R;

/**
 * Created by al3x_hh on 08/04/2018.
 */

public class ArrayAdapterActividades extends ArrayAdapter<Actividad> {

    private final Context context;
    private final ArrayList<Actividad> values;

    public ArrayAdapterActividades(@NonNull Context context, ArrayList<Actividad> values) {
        super(context, R.layout.representacion_actividad, values);

        this.context = context;
        this.values = values;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        @SuppressLint("ViewHolder") View rowView = inflater.inflate(R.layout.representacion_actividad, parent, false);
        rowView.setClickable(false);

        TextView nombreActividad = (TextView) rowView.findViewById(R.id.nombreActividad);
        TextView duracionActividad = (TextView) rowView.findViewById(R.id.duracionActividad);
        TextView horaActividad = (TextView) rowView.findViewById(R.id.horaActividad);
        TextView observacionesActividad = (TextView) rowView.findViewById(R.id.observacionesActividad);

        nombreActividad.setText(Html.fromHtml("<b>Nombre: </b>" + values.get(position).getNombre()));
        duracionActividad.setText(Html.fromHtml("<b>Duración: </b>" + String.valueOf(values.get(position).getIntervalo()) + " mins"));
        horaActividad.setText(Html.fromHtml("<b>Hora inicio: </b>" + values.get(position).getHora().toString()));
        observacionesActividad.setText(Html.fromHtml("<b>Observaciones: </b>" + values.get(position).getObservaciones()));

        return rowView;
    }
}
