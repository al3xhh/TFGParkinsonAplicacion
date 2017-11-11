package tfg.ucm.com.tfgparkinson.Activities;

import android.content.Context;

import android.support.annotation.NonNull;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import tfg.ucm.com.tfgparkinson.R;

/**
 * Created by al3x_hh on 11/11/2017.
 */

public class AdaptadorSensores extends ArrayAdapter<String> {

    private ArrayList<String> macs;
    private Context context;

    public AdaptadorSensores(@NonNull Context context, int resource, ArrayList<String> macs) {
        super(context, resource, macs);

        this.context = context;
        this.macs = macs;
    }

    @NonNull
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {

        View item = convertView;
        ItemHolder holder;

        if (item == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            item = inflater.inflate(R.layout.representacion_item_lista, null);
        }

        holder = new ItemHolder();
        holder.mac = (TextView) item.findViewById(R.id.macSensor);
        holder.partesCuerpo = (Spinner) item.findViewById(R.id.partesCuerpo);
        item.setTag(holder);


        String mac = macs.get(position);
        holder.mac.setText(mac);

        return item;
    }

    private static class ItemHolder {
        TextView mac;
        Spinner partesCuerpo;
    }
}
