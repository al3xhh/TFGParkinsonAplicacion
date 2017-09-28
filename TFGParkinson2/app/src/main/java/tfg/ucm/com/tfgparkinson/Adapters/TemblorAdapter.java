package tfg.ucm.com.tfgparkinson.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.ArrayList;

import tfg.ucm.com.tfgparkinson.Classes.Temblor;
import tfg.ucm.com.tfgparkinson.R;

public class TemblorAdapter extends ArrayAdapter<Temblor> {

    private ArrayList<Temblor> temblores;
    private Context context;

    public TemblorAdapter(Context context, int layoutResourceId, ArrayList<Temblor> temblores) {
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
        holder.hora_temblor = (TextView) item.findViewById(R.id.horaTemblor);
        holder.duracion_temblor = (TextView) item.findViewById(R.id.duracionTemblor);
        holder.observaciones_temblor = (TextView) item.findViewById(R.id.observacionesTemblor);
        holder.opciones_temblor = (ImageView) item.findViewById(R.id.opcionesTemblor);
        item.setTag(holder);

        final Temblor temblor = temblores.get(position);
        holder.hora_temblor.setText(temblor.getHora());
        //holder.duracion_temblor.setText(temblor.getDuracion());
        holder.observaciones_temblor.setText(temblor.getObservaciones());
        holder.opciones_temblor.setVisibility(View.VISIBLE);
        holder.opciones_temblor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(context, view);
                popup.getMenuInflater().inflate(R.menu.opciones_temblor, popup.getMenu());
                popup.show();
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.borra_temblor:
                                break;
                            case R.id.editar_temblor:
                                break;
                        }
                        return true;
                    }
                });
            }
        });

        return item;
    }

    private static class TemblorHolder {
        TextView hora_temblor;
        TextView duracion_temblor;
        TextView observaciones_temblor;
        ImageView opciones_temblor;
    }
}
