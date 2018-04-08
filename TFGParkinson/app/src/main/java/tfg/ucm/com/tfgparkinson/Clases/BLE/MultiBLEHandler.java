package tfg.ucm.com.tfgparkinson.Clases.BLE;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import tfg.ucm.com.tfgparkinson.Activities.MainActivityLibre;
import tfg.ucm.com.tfgparkinson.Clases.BBDD.GestorBD;
import tfg.ucm.com.tfgparkinson.Clases.utils.Constantes;
import tfg.ucm.com.tfgparkinson.Clases.utils.OpcionesVO;

import static tfg.ucm.com.tfgparkinson.Clases.utils.Constantes.TEXAS_INSTRUMENTS;

/**
 * Custom Handler implementation to process Message and Runnable
 * of the BLE Callbacks on multiple devices.
 * Created by silvia.valdez@hunabsys.com on 7/20/16.
 */
public class MultiBLEHandler extends Handler {

    private static final String TAG = MultiBLEHandler.class.getSimpleName();

    private IMultiBLEAccelDataReceiverDelegate mDelegate;
    private ProgressDialog mProgressDialog;
    private boolean mStopCapture;
    private int posicionesSensores;
    private byte accRange;

    // Public constructor of Multi BLE Handler.
    public MultiBLEHandler(ProgressDialog dialog, IMultiBLEAccelDataReceiverDelegate delegate) {
        // Delegate of the IMultiBLEAccelDataReceiverDelegate class to update the view.
        this.mDelegate = delegate;

        this.mProgressDialog = dialog;
        this.mStopCapture = false;
    }

    public MultiBLEHandler(ProgressDialog dialog, IMultiBLEAccelDataReceiverDelegate delegate, OpcionesVO opciones) {
        // Delegate of the IMultiBLEAccelDataReceiverDelegate class to update the view.
        this.mDelegate = delegate;
        this.posicionesSensores = opciones.getSensorPositions();
        this.accRange = opciones.getSensorsOptions().get(Constantes.ACCL_RANGE);
        this.mProgressDialog = dialog;
        this.mStopCapture = false;
    }

    @Override
    public void handleMessage(Message msg) {
        BluetoothGattDto bluetoothGattDto;
        BluetoothGatt gatt = null;
        BluetoothGattCharacteristic characteristic = null;
        int subject = msg.what;

        if (subject == IMultiBLEMessageType.STOP_CAPTURE && !mStopCapture) {
            mStopCapture = true;
        } else if (subject == IMultiBLEMessageType.RESUME_CAPTURE) {
            mStopCapture = false;
        }

        if (!mStopCapture) {
            if (msg.obj instanceof BluetoothGattDto) {
                // Custom DTO to save the data of the gatt and the characteristic read
                bluetoothGattDto = (BluetoothGattDto) msg.obj;
                gatt = bluetoothGattDto.getBluetoothGatt();
                characteristic = bluetoothGattDto.getBluetoothCharacteristic();
            }

            switch (msg.what) {
                case IMultiBLEMessageType.PROGRESS:
                    mProgressDialog.setMessage((String) msg.obj);
                    if (!mProgressDialog.isShowing()) {
                        mProgressDialog.show();
                    }
                    break;

                case IMultiBLEMessageType.ACCELEROMETER_MESSAGE:
                    if (characteristic == null || characteristic.getValue() == null) {
                        Log.w(TAG, "Error obtaining accelerometer value");
                        return;
                    }
                    updateAccelerometerValue16(gatt, characteristic);
                    break;

                case IMultiBLEMessageType.DISMISS:
                    mProgressDialog.hide();
                    break;
            }
        }
    }

    /**
     * Update accelerometer value base 16, if implemented.
     *
     * @param characteristic the characteristic read.
     */
    private void updateAccelerometerValue16(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic) {
        if (mDelegate != null) {
            float[] accelerometer = getAccelData16(characteristic);
            GestorBD bd = new GestorBD(MainActivityLibre.appContext);
            bd.insertDatosSensor(accelerometer, this.posicionesSensores, gatt.getDevice().toString(), TEXAS_INSTRUMENTS);
            mDelegate.updateAccelerometer(gatt, accelerometer[0], accelerometer[1],
                    accelerometer[2], accelerometer[3], accelerometer[4], accelerometer[5]);
        }
    }

    public float[] getAccelData16(BluetoothGattCharacteristic characteristic) {
        /*bytes   data
        * 0,1   >> GyroX
        * 2,3   >> GyroY
        * 4,5   >> GyroZ
        * 6,7   >> AcclX
        * 8,9   >> AcclY
        * 10,11 >> AcclZ
        * 12,13 >> MagnX
        * 14,15 >> MagnY
        * 16,17 >> MagnZ
        * */

        float[] result = new float[6];
        byte[] value = characteristic.getValue();

        // Three first values for the accelerometer data
        result[0] = sensorMpu9250AccConvert(getIntFromByteArray(value, 6));
        result[1] = sensorMpu9250AccConvert(getIntFromByteArray(value, 8));
        result[2] = sensorMpu9250AccConvert(getIntFromByteArray(value, 10));
        /*result[0] = sensorMpu9250AccConvert(characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_SINT16, 6));
        result[1] = sensorMpu9250AccConvert(characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_SINT16, 8));
        result[2] = sensorMpu9250AccConvert(characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_SINT16, 10));*/
        // Three last values for the gyroscope data
        result[3] = sensorMpu9250GyroConvert(getIntFromByteArray(value, 0));
        result[4] = sensorMpu9250GyroConvert(getIntFromByteArray(value, 2));
        result[5] = sensorMpu9250GyroConvert(getIntFromByteArray(value, 4));

        return result;
    }

    private int getIntFromByteArray(byte[] byteArray, int offset) {
        int result, high, low;

        high = byteArray[offset + 1];
        low = byteArray[offset];

        low = low & 0xFF;

        result = (high << 8) + low;

        return result;
    }

    private float sensorMpu9250GyroConvert(int data) {
        //-- calculate rotation, unit deg/s, range -250, +250

        return (data * 1.0f) / (65536 / 500);
    }

    private float sensorMpu9250AccConvert(int rawData) {
        float v;

        //32768 => max val positivo de un entero de 16 bits
        //(rawData * 1.0f) / (32768 / 2) como max(rawData) = 3268 al dividir limitamos el numero entre -2 y 2
        if (accRange == Constantes.ACCL_RANGE_2G)
            v = (rawData * 1.0f) / (32768 / 2);
        else if (accRange == Constantes.ACCL_RANGE_4G)
            v = (rawData * 1.0f) / (32768/4);
        else if (accRange == Constantes.ACCL_RANGE_8G)
            v = (rawData * 1.0f) / (32768/8);
        else
            v = (rawData * 1.0f) / (32768/16);

        return v;
    }
}
