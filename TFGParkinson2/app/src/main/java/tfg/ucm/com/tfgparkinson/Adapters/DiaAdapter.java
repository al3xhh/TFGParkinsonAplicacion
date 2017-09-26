package tfg.ucm.com.tfgparkinson.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

import tfg.ucm.com.tfgparkinson.Classes.Dia;
import tfg.ucm.com.tfgparkinson.Classes.Temblor;
import tfg.ucm.com.tfgparkinson.R;

public class DiaAdapter extends ArrayAdapter<Dia> {

    private ArrayList<Dia> dias;
    private Context context;

    public DiaAdapter(Context context, int layoutResourceId, ArrayList<Dia> dias) {
        super(context, layoutResourceId, dias);
        this.context = context;
        this.dias = dias;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        View item = convertView;

        if (item == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            item = inflater.inflate(R.layout.dia_grafico, null);
        }

        DiaHolder holder = new DiaHolder();
        holder.fecha = (TextView) item.findViewById(R.id.fecha);
        //holder.temblores = (ListView) item.findViewById(R.id.temblores);
        item.setTag(holder);

        final Dia dia = dias.get(position);
        holder.fecha.setText(dia.getFecha().toString());
        holder.temblores.setAdapter(new TemblorAdapter(context, R.layout.temblor_grafico, dia.getTemblores()));

        return item;
    }

    private static class DiaHolder {
        TextView fecha;
        ListView temblores;
    }
}
