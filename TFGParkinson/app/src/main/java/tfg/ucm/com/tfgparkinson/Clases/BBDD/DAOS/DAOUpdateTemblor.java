package tfg.ucm.com.tfgparkinson.Clases.BBDD.DAOS;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import tfg.ucm.com.tfgparkinson.Clases.BBDD.GestorBD;
import tfg.ucm.com.tfgparkinson.Clases.Temblor;

/**
 * Created by chris on 07/10/2017.
 */

public class DAOUpdateTemblor {

    private Temblor temblor;

    public DAOUpdateTemblor(Temblor temblor){
        this.temblor = temblor;
    }


    /**
     * Devuelve el id de la fila insertada o -1 si hay algun error
     * */
    public long exjecutar(){
        SQLiteDatabase conexion = GestorBD.getConexion();
        ContentValues cv = new ContentValues();
        String[] whereArgs = new String[]{""+temblor.getId()};

        if (temblor.getTimestamp() != null)
            cv.put("TIMESTAMP_INICIO", temblor.getTimestamp().toString());
        cv.put("DURACION", temblor.getDuracion());
        cv.put("OBSERVACIONES", temblor.getObservaciones());

        return conexion.update("TB_TEMBLORES", cv, "ID = ?", whereArgs);
    }
}
