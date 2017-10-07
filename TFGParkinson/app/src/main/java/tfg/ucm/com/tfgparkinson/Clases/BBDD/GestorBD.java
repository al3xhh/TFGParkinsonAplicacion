package tfg.ucm.com.tfgparkinson.Clases.BBDD;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import tfg.ucm.com.tfgparkinson.Clases.utils.Constantes;

/**
 * Created by chris on 07/10/2017.
 */

public class GestorBD {

    private static SQLiteDatabase conexion;

    private static SQLiteDatabase GestorBD(String nombreBD){
        try {
            conexion = SQLiteDatabase.openDatabase(Constantes.NOMBRE_BD, null, SQLiteDatabase.OPEN_READWRITE);
        }
        catch(SQLiteException e){
            conexion = SQLiteDatabase.openOrCreateDatabase(Constantes.NOMBRE_BD, null);
            crearBD();
        }

        return conexion;
    }

    private static void crearBD(){
        conexion.execSQL("CREATE TABLE TB_SENSORES ( " +
                            "NOMBRE VARCHAR NOT NULL, " +
                            "PRIMARY KEY (NOMBRE));");

        conexion.execSQL("CREATE TABLE TB_DATOS_SENSOR ( " +
                            "ID INTEGER AUTOINCREMENT NOT NULL, " +
                            "SENSOR VARCHAR NOT NULL, " +
                            "TIMESTAMP DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
                            "DATOS VARCHAR NOT NULL," +
                            "PRIMARY KEY (ID, SENSOR, TIMESTAMP)," +
                            "FOREIGN KEY (SENSOR) REFERENCES TB_SENSORES (NOMBRE));");

        conexion.execSQL("CREATE TABLE TB_TEMBLORES ( " +
                            "ID INTEGER AUTOINCREMENT NOT NULL, " +
                            "DURACION INTEGER NOT NULL, " +
                            "OBSERVACIONES VARCHAR, " +
                            "TIMESTAMP_INICIO DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                            "PRIMARY KEY (ID));");

        /*conexion.execSQL("CREATE TABLE TB_INFO_TEMBLOR ( " +
                            "NOMBRE_SENSOR VARCHAR NOT NULL," +
                            "TIMESTAMP_SENSOR DATETIME NOT NULL, " +
                            "TEMBLOR NUMER NOT NULL, " +
                            "PRIMARY KEY (NOMBRE_SENSOR, TIMESTAMP_SENSOR, TEMBLOR), " +
                            "FOREIGN KEY (NOMBRE_SENSOR, TIMESTAMP_SENSOR) REFERENCES TB_DATOS_SENSOR (SENSOR, TIMESTAMP)," +
                            "FOREIGN KEY (TEMBLOR) REFERENCES TB_TEMBLORES (ID));");*/
    }

    public static SQLiteDatabase getConexion(){
        if (conexion == null)
            conexion = GestorBD(Constantes.NOMBRE_BD);

        return conexion;
    }

}
