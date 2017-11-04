package tfg.ucm.com.tfgparkinson.Clases.BLE;

import java.util.UUID;

/**
 * Interface for Message Types.
 * Created by silvia.valdez@hunabsys.com on 20/07/16.
 */
public interface IMultiBLEMessageType {

    // Accel Service ID's
    int ACCELEROMETER_SERVICE = 3;
    int ACCELEROMETER_MESSAGE = 10002;

    UUID ACCEL_SERVICE = UUID.fromString("f000aa80-0451-4000-b000-000000000000");//s
    UUID ACCEL_DATA_CHAR = UUID.fromString("f000aa81-0451-4000-b000-000000000000");
    UUID ACCEL_CONFIG_CHAR = UUID.fromString("f000aa82-0451-4000-b000-000000000000");
    UUID ACCEL_PERIOD = UUID.fromString("f000aa83-0451-4000-b000-000000000000");

    UUID CONFIG_DESCRIPTOR = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");

    // Other Messages
    int PROGRESS = 201;
    int DISMISS = 202;
    int CLEAR = 301;
    int STOP_CAPTURE = 1;
    int RESUME_CAPTURE = 2;

}
