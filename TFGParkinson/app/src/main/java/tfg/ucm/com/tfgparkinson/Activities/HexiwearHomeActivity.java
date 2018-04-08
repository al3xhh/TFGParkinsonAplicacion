package tfg.ucm.com.tfgparkinson.Activities;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import tfg.ucm.com.tfgparkinson.Clases.BBDD.GestorBD;
import tfg.ucm.com.tfgparkinson.R;
import tfg.ucm.com.tfgparkinson.Clases.BLE.Characteristic;
import tfg.ucm.com.tfgparkinson.Clases.services.BluetoothLeService;
import tfg.ucm.com.tfgparkinson.Clases.services.HexiwearService;

import java.text.DecimalFormat;
import java.util.ArrayList;

import static tfg.ucm.com.tfgparkinson.Clases.utils.Constantes.TEXAS_INSTRUMENTS;

public class HexiwearHomeActivity extends AppCompatActivity {

    private static final float CONSTANT_OFFSET = 0f;
    private static final float SCALE = 1000f;
    private static final float WEIGHT_RESOLUTION = 0.0000407059f;
//    private static final float WEIGHT_RESOLUTION = 0.0000386837f;
    private HexiwearService hexiwearService;
    private final ArrayList<String> uuidArray = new ArrayList<>();
    TextView data_tv;
    String uuid;
    byte[] data;
    float weight;
    String weightString;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_activty);

        uuidArray.add(HexiwearService.UUID_CHAR_ACCEL);

        //txtView_temperature = (TextView) findViewById(R.id.temperature_label);
        data_tv = (TextView) findViewById(R.id.data_tv);
    }

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("PATATA", "ENTROOOOOOO");
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                invalidateOptionsMenu();
                Intent intentAct = new Intent(HexiwearHomeActivity.this, DeviceScanActivityHexiwear.class);
                startActivity(intentAct);
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                Log.d("PATATA", "ENTRO ELSE IF");
                Log.d("PATATA", intent.toString());
                Log.d("PATATA", BluetoothLeService.EXTRA_DATA);
                data_tv.setText(intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
                //Log.d("PATATA", String.valueOf(weight));
                //uuid = intent.getStringExtra(BluetoothLeService.EXTRA_CHAR);
                //displayWeight(uuid, weight);
            }
        }
    };

    @Override

    protected void onResume() {
        super.onResume();
        hexiwearService = new HexiwearService(uuidArray);
        hexiwearService.readCharStart(10);
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
    }

    @Override
    protected void onPause() {
        super.onPause();
        hexiwearService.readCharStop();
        unregisterReceiver(mGattUpdateReceiver);
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }
}
