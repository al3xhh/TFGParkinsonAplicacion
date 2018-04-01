package tfg.ucm.com.tfgparkinson.Activities;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.os.Handler;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import tfg.ucm.com.tfgparkinson.Clases.services.BluetoothLeService;
import tfg.ucm.com.tfgparkinson.Clases.services.NotificationService;
import tfg.ucm.com.tfgparkinson.Clases.utils.ApplicationSession;
import tfg.ucm.com.tfgparkinson.R;

public class DeviceScanActivityHexiwear extends AppCompatActivity {

    //private static final String TAG = "DeviceScanActivity";
    private BluetoothAdapter mBluetoothAdapter;
    private Handler mHandler;
    public static final String UUID_CHAR_ALERTIN = "00002031-0000-1000-8000-00805f9b34fb";
    private BluetoothGattCharacteristic alertInCharacteristic;
    //private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    //private static final int REQUEST_ENABLE_BT = 1;
    //public static final String KWARP_ADDRESS = "00:29:40:08:00:1F";
    private TextView mScanTitle;
    private String mDeviceAddress;
    private BluetoothDevice device;
    private final String LIST_UUID = "UUID";
    private static BluetoothLeService mBluetoothLeService;
    private static ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
    public static ArrayList<ArrayList<BluetoothGattCharacteristic>> getGattCharacteristics() {return mGattCharacteristics;}
    public static BluetoothLeService getBluetoothLeService() {
        return mBluetoothLeService;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_scan_hexiwear);

        mHandler = new Handler();
        mScanTitle = (TextView) findViewById(R.id.scanTitle);

        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        checkNotificationEnabled();
        LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, new IntentFilter("Msg"));
        ComponentName name = startService(new Intent(DeviceScanActivityHexiwear.this, NotificationService.class));
        //registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        scanLeDevice(true);
        Intent gattServiceIntent = new Intent(DeviceScanActivityHexiwear.this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
    }

    private void displayGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null) return;
        String uuid = null;
        String unknownServiceString = getResources().getString(R.string.unknown_service);
        String unknownCharaString = getResources().getString(R.string.unknown_characteristic);
        ArrayList<HashMap<String, String>> gattServiceData = new ArrayList<HashMap<String, String>>();
        ArrayList<ArrayList<HashMap<String, String>>> gattCharacteristicData = new ArrayList<ArrayList<HashMap<String, String>>>();
        mGattCharacteristics.clear();
        mGattCharacteristics = new ArrayList<>();

        // Loops through available GATT Services.
        for (BluetoothGattService gattService : gattServices) {
            HashMap<String, String> currentServiceData = new HashMap<String, String>();
            uuid = gattService.getUuid().toString();
            currentServiceData.put(LIST_UUID, uuid);
            gattServiceData.add(currentServiceData);

            ArrayList<HashMap<String, String>> gattCharacteristicGroupData = new ArrayList<>();
            List<BluetoothGattCharacteristic> gattCharacteristics = gattService.getCharacteristics();
            ArrayList<BluetoothGattCharacteristic> charas = new ArrayList<>();

            // Loops through available Characteristics.
            for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                charas.add(gattCharacteristic);
                HashMap<String, String> currentCharaData = new HashMap<>();
                uuid = gattCharacteristic.getUuid().toString();
                if(uuid.equals(UUID_CHAR_ALERTIN)) {
                    alertInCharacteristic = gattCharacteristic;
                    byte[] value = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
                    alertInCharacteristic.setValue(value);
                }
                currentCharaData.put(LIST_UUID, uuid);
                gattCharacteristicGroupData.add(currentCharaData);
            }
            mGattCharacteristics.add(charas);
            gattCharacteristicData.add(gattCharacteristicGroupData);
        }
    }



    public boolean checkNotificationEnabled() {
        try {
            if (Settings.Secure.getString(this.getContentResolver(), "enabled_notification_listeners").contains(getApplicationContext().getPackageName())) {
                return true;
            } else {
                //service is not enabled try to enabled by calling...
                Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        intentFilter.addAction(BluetoothLeService.ACTION_WRITE_RESPONSE_OK);
        intentFilter.addAction(BluetoothLeService.ACTION_WRITE_RESPONSE_ERROR);
        return intentFilter;
    }

    private BroadcastReceiver onNotice = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String text = intent.getStringExtra("text");
            if ((alertInCharacteristic != null) && (mBluetoothLeService != null)) {
                int charaProp = alertInCharacteristic.getProperties();
                if ((charaProp & BluetoothGattCharacteristic.PROPERTY_WRITE) > 0) {
                    byte[] bytes = Arrays.copyOf(text.getBytes(), 20);
                    alertInCharacteristic.setValue(bytes);
                    while(!mBluetoothLeService.writeNoResponseCharacteristic(alertInCharacteristic)) {
                        try {
                            Thread.sleep(50);
                        }
                        catch (InterruptedException e) {
                            //Log.e(TAG, "InterruptedException");
                        }
                    }
                }
            }
        }
    };

    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                finish();
            }
            // Automatically connects to the device upon successful start-up initialization.
            mBluetoothLeService.connect(mDeviceAddress);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                mScanTitle.setText(R.string.device_connected);
                invalidateOptionsMenu();
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                invalidateOptionsMenu();
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                displayGattServices(mBluetoothLeService.getSupportedGattServices());
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {

            } else if ((BluetoothLeService.ACTION_WRITE_RESPONSE_OK.equals(action)) || (BluetoothLeService.ACTION_WRITE_RESPONSE_ERROR.equals(action))) {

            }

        }
    };

    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
            try {
                //TODO
                if (device.getAddress().equals(KWARP_ADDRESS)) {
                    mBluetoothLeService.connect(mDeviceAddress);
                    DeviceScanActivityHexiwear.this.device = device;
                    scanLeDevice(false);
                }
                //Log.e("BLE", KWARP_ADDRESS + " " + device.getAddress() + " " + device.getName());
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
        invalidateOptionsMenu();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mGattUpdateReceiver);
        mBluetoothLeService = null;
        stopService(new Intent(DeviceScanActivity.this, NotificationService.class));
        mGattCharacteristics.clear();
        unbindService(mServiceConnection);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
