package tfg.ucm.com.tfgparkinson.Clases.BBDD.DAOS;

import android.database.sqlite.SQLiteDatabase;

import tfg.ucm.com.tfgparkinson.Clases.BBDD.GestorBD;
import tfg.ucm.com.tfgparkinson.Clases.Temblor;

/**
 * Created by chris on 07/10/2017.
 */

public class DAODeleteTemblor {

    private Temblor temblor;

    public DAODeleteTemblor(Temblor temblor){
        this.temblor = temblor;
    }

    /**
     * Devuelve el numero de colummnas afectadas por la query
     * */
    public int ejecutar() {
        SQLiteDatabase conexion = GestorBD.getConexion();
        int idTemblor = temblor.getId();
        String[] whereArgs = new String[]{idTemblor+""};

        return conexion.delete("TB_TEMBLORES", "ID = ?", whereArgs);
    }
}
