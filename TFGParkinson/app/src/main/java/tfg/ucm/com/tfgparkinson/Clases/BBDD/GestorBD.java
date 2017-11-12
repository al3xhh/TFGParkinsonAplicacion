package tfg.ucm.com.tfgparkinson.Clases.BBDD;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;

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
        db.execSQL("CREATE TABLE TB_POSICIONES ( " +
                            "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "POSICIONES VARCHAR NOT NULL UNIQUE);");

        db.execSQL("CREATE TABLE TB_DATOS_SENSOR ( " +
                            "POSICIONES INTEGER NOT NULL, " +
                            "DB_TIMESTAMP DATETIME NOT NULL DEFAULT (datetime(CURRENT_TIMESTAMP, 'localtime')), " +
                            "APP_TIMESTAMP DATETIME NOT NULL," +
                            "DATOS VARCHAR NOT NULL," +
                            "PRIMARY KEY (POSICIONES, TIMESTAMP)," +
                            "FOREIGN KEY (POSICIONES) REFERENCES TB_POSICIONES(ID));");

        db.execSQL("CREATE TABLE TB_TEMBLORES ( " +
                            "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "DURACION INTEGER NOT NULL, " +
                            "OBSERVACIONES VARCHAR, " +
                            "TIMESTAMP_INICIO DATETIME DEFAULT (datetime(CURRENT_TIMESTAMP, 'localtime')));");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP DATABASE");

        this.onCreate(db);
    }

    public ArrayList<Temblor> getTemblores() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Temblor> temblores = new ArrayList<Temblor>();

        Cursor cursor = db.rawQuery("SELECT * FROM TB_TEMBLORES ORDER BY TIMESTAMP_INICIO ASC", null);

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

    public boolean checkPosicion(String posiciones){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Temblor> temblores = new ArrayList<Temblor>();
        int count = -1;
        boolean exists = false;

        Cursor cursor = db.rawQuery("SELECT count(*) FROM TB_POSICIONES WHERE POSICIONES = ?", new String[]{posiciones});

        try{
            while (cursor.moveToNext())
                count = cursor.getInt(0);
        } finally {
            cursor.close();
            db.close();
        }

        if (count > 0) exists = true;

        return exists;
    }

    public void insertPosicion(String posiciones){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("POSICIONES", posiciones);

        db.insert("TB_POSICIONES", null, cv);

        db.close();
    }

    public int getPosicionID(String posiciones){
        int id = -1;
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Temblor> temblores = new ArrayList<Temblor>();

        Cursor cursor = db.rawQuery("SELECT ID FROM TB_POSICIONES WHERE POSICIONES = ?", new String[]{posiciones});

        try{
            while (cursor.moveToNext())
                id = cursor.getInt(0);
        } finally {
            cursor.close();
            db.close();
        }

        return id;

    }

    public void insertDatosSensor(int[] data, int posicionesID){
        Calendar calendar = Calendar.getInstance();
        Timestamp ts = new Timestamp(calendar.getTimeInMillis());
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("POSICIONES", posicionesID);
        cv.put("APP_TIMESTAMP", ts.toString());
        cv.put("DATOS", intArrayToString(data));

        db.insert("TB_DATOS_SENSOR", null, cv);

        db.close();

    }

    public void vaciarTabla(String tabla){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + tabla);
        db.close();
    }

    private String intArrayToString(int[] data){
        String intArray = data[0] + "";

        for (int i = 1; i < data.length; i++)
            intArray += "," + data[i];

        return intArray;
    }
}