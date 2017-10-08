package tfg.ucm.com.tfgparkinson.Clases.BBDD.DAOS;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import java.sql.Timestamp;

import tfg.ucm.com.tfgparkinson.Clases.BBDD.GestorBD;
import tfg.ucm.com.tfgparkinson.Clases.Temblor;

import static android.os.Build.VERSION_CODES.N;

/**
 * Created by chris on 07/10/2017.
 */

public class DAOInsertTemblor {
    private Temblor temblor;

    public DAOInsertTemblor(Temblor temblor){
        this.temblor = temblor;
    }


    /**
     * Devuelve el id de la fila insertada o -1 si hay algun error
     * */
    public long ejecutar(){
        SQLiteDatabase conexion = GestorBD.getConexion();
        ContentValues cv = new ContentValues();

        if (temblor.getTimestamp() != null)
            cv.put("TIMESTAMP_INICIO", temblor.getTimestamp().toString());
        cv.put("DURACION", temblor.getDuracion());
        cv.put("OBSERVACIONES", temblor.getObservaciones());

        return conexion.insert("TB_TEMBLORES", null, cv);
    }

}
