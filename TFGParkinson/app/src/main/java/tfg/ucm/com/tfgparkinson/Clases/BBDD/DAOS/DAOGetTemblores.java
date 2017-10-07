package tfg.ucm.com.tfgparkinson.Clases.BBDD.DAOS;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.sql.Timestamp;
import java.util.ArrayList;

import tfg.ucm.com.tfgparkinson.Clases.BBDD.GestorBD;
import tfg.ucm.com.tfgparkinson.Clases.Temblor;

import static android.os.Build.VERSION_CODES.N;

/**
 * Created by chris on 07/10/2017.
 */

public class DAOGetTemblores {

    public ArrayList<Temblor> ejecutar(){
        SQLiteDatabase conexion = GestorBD.getConexion();
        ArrayList<Temblor> temblores = new ArrayList<Temblor>();

        Cursor cursor = conexion.query("TB_TEMBLORES",null,null,null,null,null,null,null);

        try{
            while (cursor.moveToNext())
                temblores.add(new Temblor(cursor.getInt(1), Timestamp.valueOf(cursor.getString(4)), cursor.getInt(2), cursor.getString(3)));
        } finally {
            cursor.close();
        }

        return temblores;
    }
}
