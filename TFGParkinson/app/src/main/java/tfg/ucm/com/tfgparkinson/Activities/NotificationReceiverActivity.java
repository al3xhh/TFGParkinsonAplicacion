package tfg.ucm.com.tfgparkinson.Activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import tfg.ucm.com.tfgparkinson.R;

/**
 * Created by al3x_hh on 01/01/2018.
 */

public class NotificationReceiverActivity extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        Toast.makeText(context, action, Toast.LENGTH_SHORT).show();
    }
}
