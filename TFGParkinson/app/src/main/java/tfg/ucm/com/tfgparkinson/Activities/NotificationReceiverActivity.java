package tfg.ucm.com.tfgparkinson.Activities;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by al3x_hh on 01/01/2018.
 */
public class NotificationReceiverActivity extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        Toast.makeText(context, action, Toast.LENGTH_SHORT).show();

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.cancelAll();
    }
}
