package tfg.ucm.com.tfgparkinson.Clases.BBDD;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Timestamp;
import java.util.ArrayList;

import tfg.ucm.com.tfgparkinson.Clases.Temblor;
import tfg.ucm.com.tfgparkinson.Clases.utils.Constantes;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by chris on 07/10/2017.
 */

public class GestorBD extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = Constantes.NOMBRE_BD;

    public GestorBD(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE TB_SENSORES ( " +
                            "NOMBRE VARCHAR NOT NULL, " +
                            "PRIMARY KEY (NOMBRE));");

        db.execSQL("CREATE TABLE TB_DATOS_SENSOR ( " +
                            "SENSOR VARCHAR NOT NULL, " +
                            "TIMESTAMP DATETIME NOT NULL DEFAULT (datetime(CURRENT_TIMESTAMP, 'localtime')), " +
                            "DATOS VARCHAR NOT NULL," +
                            "PRIMARY KEY (SENSOR, TIMESTAMP)," +
                            "FOREIGN KEY (SENSOR) REFERENCES TB_SENSORES (NOMBRE));");

        db.execSQL("CREATE TABLE TB_TEMBLORES ( " +
                            "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "DURACION INTEGER NOT NULL, " +
                            "OBSERVACIONES VARCHAR, " +
                            "TIMESTAMP_INICIO DATETIME DEFAULT (datetime(CURRENT_TIMESTAMP, 'localtime')));");

        /*db.execSQL("CREATE TABLE TB_INFO_TEMBLOR ( " +
                            "NOMBRE_SENSOR VARCHAR NOT NULL," +
                            "TIMESTAMP_SENSOR DATETIME NOT NULL, " +
                            "TEMBLOR NUMER NOT NULL, " +
                            "PRIMARY KEY (NOMBRE_SENSOR, TIMESTAMP_SENSOR, TEMBLOR), " +
                            "FOREIGN KEY (NOMBRE_SENSOR, TIMESTAMP_SENSOR) REFERENCES TB_DATOS_SENSOR (SENSOR, TIMESTAMP)," +
                            "FOREIGN KEY (TEMBLOR) REFERENCES TB_TEMBLORES (ID));");*/
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP DATABASE");

        this.onCreate(db);
    }

    public ArrayList<Temblor> getTemblores() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Temblor> temblores = new ArrayList<Temblor>();

        Cursor cursor = db.rawQuery("SELECT OID, * FROM TB_TEMBLORES ORDER BY TIMESTAMP_INICIO ASC", null);

        try{
            while (cursor.moveToNext())
                temblores.add(new Temblor(cursor.getInt(1), Timestamp.valueOf(cursor.getString(4)), cursor.getInt(2), cursor.getString(3)));
        } finally {
            cursor.close();
            db.close();
        }

        return temblores;

    }

    public void deleteTemblor(Temblor temblor) {
        SQLiteDatabase db = this.getWritableDatabase();
        int idTemblor = temblor.getId();
        String[] whereArgs = new String[]{idTemblor+""};

        db.delete("TB_TEMBLORES", "OID = ?", whereArgs);

        db.close();
    }

    public void insertTemblor(Temblor temblor) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        if (temblor.getTimestamp() != null)
            cv.put("TIMESTAMP_INICIO", temblor.getTimestamp().toString());
        cv.put("DURACION", temblor.getDuracion());
        cv.put("OBSERVACIONES", temblor.getObservaciones());

        db.insert("TB_TEMBLORES", null, cv);

        db.close();
    }

    public void updateTemblor(Temblor temblor){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        String[] whereArgs = new String[]{""+temblor.getId()};

        if (temblor.getTimestamp() != null)
            cv.put("TIMESTAMP_INICIO", temblor.getTimestamp().toString());
        cv.put("DURACION", temblor.getDuracion());
        cv.put("OBSERVACIONES", temblor.getObservaciones());

        db.update("TB_TEMBLORES", cv, "ID = ?", whereArgs);

        db.close();
    }

    public void vaciarTabla(String tabla){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + tabla);
        db.close();
    }
}
