package tfg.ucm.com.tfgparkinson.Clases.utils;

import java.util.HashMap;

/**
 * Created by chris on 07/10/2017.
 *
 * Clase que contendra todas las constantes que puedan resultar utiles.
 */

public class Constantes {

    public static final String NOMBRE_BD = "TFG_BD";

    //Claves para el HasMap<Integer, Byte> usado para pasar parametros entre la vista y las clases bluetooth
    public static final Integer GYRO_ON = 0;
    public static final Integer ACCL_ON = 1;
    public static final Integer MAGN_ON = 2;
    public static final Integer WAKE_ON_MOTION = 3;
    public static final Integer ACCL_RANGE = 4;
    public static final Integer PERIOD = 5;
    public static final Integer POSICIONES_ID = 6;
    //Valores para el HasMap<Integer, Byte> usado para pasar parametros entre la vista y las clases bluetooth
    public static final Byte DEFAULT_PERIOD = 0x64;
    public static final Byte ACCL_ON_VALUE = 0x38;
    public static final Byte GYRO_ON_VALUE = 0x07;
    public static final Byte MAGN_ON_VALUE = 0x40;
    public static final Byte ACCL_RANGE_2G = 0x00;
    public static final Byte ACCL_RANGE_4G = 0x01;
    public static final Byte ACCL_RANGE_8G = 0x02;
    public static final Byte ACCL_RANGE_16G = 0x03;

    //valor generio para indicar que no se quiere usar esa funcionalidad
    public static final Byte OFF = 0x00;
}
