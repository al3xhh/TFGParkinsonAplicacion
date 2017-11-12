package tfg.ucm.com.tfgparkinson.Clases.utils;

import android.content.Context;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by chris on 11/11/2017.
 */

public class OpcionesVO implements Serializable {
    private HashMap<Integer, Byte> sensorsOptions;
    private int sensorPositions;
    private Context context;

    public HashMap<Integer, Byte> getSensorsOptions() {
        return sensorsOptions;
    }

    public void setSensorsOptions(HashMap<Integer, Byte> sensorsOptions) {
        this.sensorsOptions = sensorsOptions;
    }

    public Context getContext() {
        return context;
    }

    public int getSensorPositions() {
        return sensorPositions;
    }

    public void setSensorPositions(int sensorPositions) {
        this.sensorPositions = sensorPositions;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
