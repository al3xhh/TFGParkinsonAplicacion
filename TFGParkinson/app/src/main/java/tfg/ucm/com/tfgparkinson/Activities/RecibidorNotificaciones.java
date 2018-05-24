package tfg.ucm.com.tfgparkinson.Activities;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import java.util.Date;

import tfg.ucm.com.tfgparkinson.Clases.Actividad;
import tfg.ucm.com.tfgparkinson.Clases.BBDD.GestorBD;
import tfg.ucm.com.tfgparkinson.Clases.Medicamento;
import tfg.ucm.com.tfgparkinson.Clases.utils.RespuestaServidor;
import tfg.ucm.com.tfgparkinson.R;

/**
 * Created by al3x_hh on 01/01/2018.
 */
public class RecibidorNotificaciones extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        Medicamento medicamento = (Medicamento) intent.getSerializableExtra("MEDICAMENTO");
        GestorBD gestorBD = new GestorBD(context);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.cancelAll();

        if(action.equals("CONFIRM")) {
            gestorBD.insertarToma(medicamento, "S");
            Intent i = new Intent(context, Actividades.class);
            i.putExtra("MEDICAMENTO", medicamento);
            i.putExtra("GRABAR", "S");
            context.startActivity(i);
        } else {
            gestorBD.insertarToma(medicamento, "N");
        }
    }
}
