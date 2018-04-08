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
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        @SuppressLint("ViewHolder") View rowView = inflater.inflate(R.layout.representacion_medicamento, parent, false);
        rowView.setClickable(false);

        TextView nombreMedicamento = (TextView) rowView.findViewById(R.id.nombreMedicamento);
        TextView diasMedicamento = (TextView) rowView.findViewById(R.id.diasMedicamento);
        TextView horaMedicamento = (TextView) rowView.findViewById(R.id.horaMedicamento);
        TextView tiempoMedicamento = (TextView) rowView.findViewById(R.id.tiempoMedicamento);

        nombreMedicamento.setText(Html.fromHtml("<b>Nombre: </b>" + values.get(position).getNombre()));
        tiempoMedicamento.setText(Html.fromHtml("<b>Intervalo: </b>" + String.valueOf(values.get(position).getIntervalo()) + " mins"));
        horaMedicamento.setText(Html.fromHtml("<b>Hora toma: </b>" + values.get(position).getHora()));
        diasMedicamento.setText(Html.fromHtml("<b>Días: </b>" + values.get(position).getDiasFormateados()));

        return rowView;
    }
}
