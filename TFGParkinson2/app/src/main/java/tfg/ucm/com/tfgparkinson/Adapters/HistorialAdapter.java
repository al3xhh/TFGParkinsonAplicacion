package tfg.ucm.com.tfgparkinson.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import tfg.ucm.com.tfgparkinson.Classes.Temblor;
import tfg.ucm.com.tfgparkinson.R;

public class HistorialAdapter extends ArrayAdapter<Temblor> {

    private ArrayList<Temblor> temblores;
    private Context context;

    public HistorialAdapter(Context context, int layoutResourceId, ArrayList<Temblor> temblores) {
        super(context, layoutResourceId, temblores);
        this.context = context;
        this.temblores = temblores;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        View item = convertView;

        if (item == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            item = inflater.inflate(R.layout.temblor_grafico, null);
        }

        TemblorHolder holder = new TemblorHolder();
        holder.fecha_temblor = (TextView) item.findViewById(R.id.fechaTemblor);
        holder.hora_temblor = (TextView) item.findViewById(R.id.horaTemblor);
        holder.observaciones_temblor = (TextView) item.findViewById(R.id.observacionesTemblor);
        holder.opciones_temblor = (ImageView) item.findViewById(R.id.opcionesTemblor);
        item.setTag(holder);

        final Temblor temblor = temblores.get(position);
        holder.fecha_temblor.setText(temblor.getFecha().toString());
        holder.hora_temblor.setText(temblor.getHora());
        holder.observaciones_temblor.setText(temblor.getObservaciones());
        holder.opciones_temblor.setVisibility(View.VISIBLE);

        return item;
    }

    private static class TemblorHolder {
        TextView fecha_temblor;
        TextView hora_temblor;
        TextView observaciones_temblor;
        ImageView opciones_temblor;
    }
}
