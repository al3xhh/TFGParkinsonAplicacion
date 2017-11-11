package tfg.ucm.com.tfgparkinson.Clases.utils;

import java.util.HashMap;

/**
 * Created by chris on 11/11/2017.
 */

public class OpcionesVO {
    private HashMap<Integer, Byte> sensorsOptions;
    private int sensorPositions;

    public HashMap<Integer, Byte> getSensorsOptions() {
        return sensorsOptions;
    }

    public void setSensorsOptions(HashMap<Integer, Byte> sensorsOptions) {
        this.sensorsOptions = sensorsOptions;
    }

    public int getSensorPositions() {
        return sensorPositions;
    }

    public void setSensorPositions(int sensorPositions) {
        this.sensorPositions = sensorPositions;
    }
}
