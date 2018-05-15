package tfg.ucm.com.tfgparkinson.Clases.BBDD;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import tfg.ucm.com.tfgparkinson.Clases.Actividad;
import tfg.ucm.com.tfgparkinson.Clases.Medicamento;
import tfg.ucm.com.tfgparkinson.Clases.Temblor;
import tfg.ucm.com.tfgparkinson.Clases.utils.Constantes;

import static android.content.Context.MODE_PRIVATE;

import android.provider.Settings.Secure;
import android.widget.Toast;

/**
 * Created by chris on 07/10/2017.
 */

public class GestorBD extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 25;

    private static final String DATABASE_NAME = Constantes.NOMBRE_BD;

    private Context context;

    public GestorBD(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE TB_POSICIONES ( " +
                            "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "POSICIONES VARCHAR NOT NULL UNIQUE," +
                            "ENVIADO VARCHAR(1) DEFAULT ('N'));");

        db.execSQL("CREATE TABLE TB_DATOS_SENSOR ( " +
                            "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "SENSOR VARCHAR NOT NULL,"+
                            "POSICIONES INTEGER NOT NULL, " +
                            "DB_TIMESTAMP DATETIME NOT NULL DEFAULT (datetime(CURRENT_TIMESTAMP, 'localtime')), " +
                            "APP_TIMESTAMP DATETIME NOT NULL," +
                            "DATOS VARCHAR NOT NULL," +
                            "TIPO_SENSOR VARCHAR NOT NULL CHECK (TIPO_SENSOR = '1' OR TIPO_SENSOR='2'), " +
                            "FOREIGN KEY (POSICIONES) REFERENCES TB_POSICIONES(ID));");

        db.execSQL("CREATE TABLE TB_TEMBLORES ( " +
                            "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "DURACION INTEGER NOT NULL, " +
                            "OBSERVACIONES VARCHAR, " +
                            "TIMESTAMP_INICIO DATETIME DEFAULT (datetime(CURRENT_TIMESTAMP, 'localtime'))," +
                            "ENVIADO VARCHAR(1) DEFAULT ('N'));");

        db.execSQL("CREATE TABLE TB_MEDICAMENTOS ( " +
                            "NOMBRE VARCHAR PRIMARY KEY, " +
                            "DIAS VARCHAR NOT NULL, " +
                            "HORA DATETIME NOT NULL, " +
                            "MINUTOS_DESCARTAR NUMBER NOT NULL, " +
                            "ENVIADO VARCHAR(1) DEFAULT ('N'));");

        db.execSQL("CREATE TABLE TB_ACTIVIDADES ( " +
                            "NOMBRE VARCHAR, " +
                            "INTERVALO NUMBER NOT NULL, " +
                            "HORA VARCHAR NOT NULL," +
                            "OBSERVACIONES VARCHAR," +
                            "ENVIADO VARCHAR(1) DEFAULT ('N')," +
                            "PRIMARY KEY (NOMBRE,HORA));");

    }

    public void insertActividad(Actividad actividad) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("NOMBRE", actividad.getNombre());
        cv.put("INTERVALO", actividad.getIntervalo());
        cv.put("HORA", actividad.getHora().toString());
        cv.put("OBSERVACIONES", actividad.getObservaciones());
        db.insert("TB_ACTIVIDADES", null, cv);

        db.close();
    }

    public void deleteActividad(Actividad actividad) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] whereArgs = new String[]{actividad.getNombre(), actividad.getHora().toString()};

        db.delete("TB_ACTIVIDADES", "NOMBRE = ? AND HORA = ?", whereArgs);

        db.close();
    }

    public void updateActividad(Actividad actividad) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        String[] whereArgs = new String[]{actividad.getNombre(), actividad.getHora().toString()};

        cv.put("OBSERVACIONES", actividad.getObservaciones());
        cv.put("INTERVALO", actividad.getIntervalo());

        db.update("TB_ACTIVIDADES", cv, "NOMBRE = ? AND HORA = ?", whereArgs);

        db.close();
    }

    public void deleteMedicamento(Medicamento medicamento) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] whereArgs = new String[]{medicamento.getNombre()};

        db.delete("TB_MEDICAMENTOS", "NOMBRE = ?", whereArgs);

        db.close();
    }

    public void insertMedicamento(Medicamento medicamento) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("NOMBRE", medicamento.getNombre());
        cv.put("DIAS", medicamento.getDiasFormateados());
        cv.put("HORA", medicamento.getTimestamp().toString());
        cv.put("MINUTOS_DESCARTAR", medicamento.getIntervalo());

        db.insert("TB_MEDICAMENTOS", null, cv);

        db.close();
    }

    public void updateMedicamento(Medicamento medicamento) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        String[] whereArgs = new String[]{medicamento.getNombre()};

        cv.put("DIAS", medicamento.getDiasFormateados());
        cv.put("HORA", medicamento.getTimestamp().toString());
        cv.put("MINUTOS_DESCARTAR", medicamento.getIntervalo());

        db.update("TB_MEDICAMENTOS", cv, "NOMBRE = ?", whereArgs);

        db.close();
    }

    public ArrayList<Medicamento> getMedicamentos(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Medicamento> medicamentos = new ArrayList<Medicamento>();

        Cursor cursor = db.rawQuery("SELECT * FROM TB_MEDICAMENTOS ORDER BY NOMBRE ASC", null);

        try{
            while (cursor.moveToNext()) {
                String[] splitDias = cursor.getString(1).split("-");
                ArrayList<String> dias = new ArrayList<>();

                for(int i = 0; i < splitDias.length; i ++) {
                    dias.add(splitDias[i]);
                }

                medicamentos.add(new Medicamento(cursor.getString(0), Integer.parseInt(cursor.getString(3)),
                        cursor.getString(2), dias));
            }
        } finally {
            cursor.close();
            db.close();
        }

        return medicamentos;
    }

    public ArrayList<Actividad> getActividades(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Actividad> actividades = new ArrayList<Actividad>();

        Cursor cursor = db.rawQuery("SELECT * FROM TB_ACTIVIDADES ORDER BY NOMBRE ASC", null);

        try{
            while (cursor.moveToNext()) {
                actividades.add(new Actividad(cursor.getString(0), Integer.parseInt(cursor.getString(1)),
                        cursor.getString(2), cursor.getString(3)));
            }
        } finally {
            cursor.close();
            db.close();
        }

        return actividades;
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        /*
        db.execSQL("DROP TABLE TB_TEMBLORES");
        db.execSQL("DROP TABLE TB_POSICIONES");
        db.execSQL("DROP TABLE TB_MEDICAMENTOS");
        db.execSQL("DROP TABLE TB_ACTIVIDADES");*/

        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name like 'TB_%'", null);

        while (c.moveToNext())
            db.execSQL("DROP TABLE " + c.getString(0));

        this.onCreate(db);
    }

    public ArrayList<Temblor> getTemblores() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Temblor> temblores = new ArrayList<Temblor>();

        Cursor cursor = db.rawQuery("SELECT * FROM TB_TEMBLORES ORDER BY TIMESTAMP_INICIO ASC", null);

        try{
            while (cursor.moveToNext()) {
                temblores.add(new Temblor(cursor.getInt(0), Timestamp.valueOf(cursor.getString(3)), cursor.getInt(1), cursor.getString(2))); }
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

    public void insertDatosSensor(float[] data, int posicionesID, String sensor, String tipo){
        Calendar calendar = Calendar.getInstance();
        Timestamp ts = new Timestamp(calendar.getTimeInMillis());
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("POSICIONES", posicionesID);
        cv.put("APP_TIMESTAMP", ts.toString());
        cv.put("DATOS", floatArrayToString(data));
        cv.put("SENSOR", sensor);
        cv.put("TIPO_SENSOR", tipo);

        db.insert("TB_DATOS_SENSOR", null, cv);

        db.close();

    }

    public void vaciarTabla(String tabla){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + tabla);
        db.close();
    }

    public JSONArray getTb_posiciones(){
        SQLiteDatabase db = this.getReadableDatabase();
        JSONArray tabla = new JSONArray();
        Cursor cursor = db.rawQuery("SELECT * FROM TB_POSICIONES WHERE ENVIADO = 'N'", null);

        try{
            while (cursor.moveToNext()){
                JSONObject tupla = new JSONObject();

                tupla.put("id", cursor.getInt(0));
                tupla.put("posiciones", cursor.getString(1));
                tupla.put("device_id", Secure.getString(context.getContentResolver(), Secure.ANDROID_ID));
                tabla.put(tupla);
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        finally {
            cursor.close();
            db.close();
        }

        return tabla;
    }

    public JSONArray getTb_datos_sensor(){
        SQLiteDatabase db = this.getReadableDatabase();
        JSONArray tabla = new JSONArray();
        Cursor cursor = db.rawQuery("SELECT * FROM TB_DATOS_SENSOR", null);

        try{
            while (cursor.moveToNext()){
                JSONObject tupla = new JSONObject();

                tupla.put("id", cursor.getInt(0));
                tupla.put("sensor", cursor.getString(1));
                tupla.put("posiciones", cursor.getInt(2));
                tupla.put("db_timestamp", Timestamp.valueOf(cursor.getString(3)).getTime());
                tupla.put("app_timestamp", Timestamp.valueOf(cursor.getString(4)).getTime());
                tupla.put("datos", cursor.getString(5));
                tupla.put("device_id", Secure.getString(context.getContentResolver(), Secure.ANDROID_ID));
                tupla.put("tipo_sensor", cursor.getString(6));
                tabla.put(tupla);
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        finally {
            cursor.close();
            db.close();
        }

        return tabla;
    }

    public JSONArray getTb_temlobres(){
        SQLiteDatabase db = this.getReadableDatabase();
        JSONArray tabla = new JSONArray();
        Cursor cursor = db.rawQuery("SELECT * FROM TB_TEMBLORES WHERE ENVIADO = 'N'", null);

        try{
            while (cursor.moveToNext()){
                JSONObject tupla = new JSONObject();

                tupla.put("id", cursor.getInt(0));
                tupla.put("duracion", cursor.getInt(1));
                tupla.put("observaciones", cursor.getString(2));
                tupla.put("timestamp_inicio", Timestamp.valueOf(cursor.getString(3)).getTime());
                tupla.put("device_id", Secure.getString(context.getContentResolver(), Secure.ANDROID_ID));
                tabla.put(tupla);
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        finally {
            cursor.close();
            db.close();
        }

        return tabla;
    }

    public JSONArray getTb_medicamentos(){
        SQLiteDatabase db = this.getReadableDatabase();
        JSONArray tabla = new JSONArray();
        Cursor cursor = db.rawQuery("SELECT * FROM TB_MEDICAMENTOS WHERE ENVIADO = 'N'", null);

        try{
            while (cursor.moveToNext()){
                JSONObject tupla = new JSONObject();

                tupla.put("nombre", cursor.getString(0));
                tupla.put("dias", cursor.getString(1));
                tupla.put("hora", cursor.getString(2));
                tupla.put("minutos_descartar", cursor.getInt(3));
                tupla.put("device_id", Secure.getString(context.getContentResolver(), Secure.ANDROID_ID));
                tabla.put(tupla);
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        finally {
            cursor.close();
            db.close();
        }

        return tabla;
    }

    public JSONArray getTb_actividades(){
        SQLiteDatabase db = this.getReadableDatabase();
        JSONArray tabla = new JSONArray();
        Cursor cursor = db.rawQuery("SELECT * FROM TB_ACTIVIDADES WHERE ENVIADO = 'N'", null);

        try{
            while (cursor.moveToNext()){
                JSONObject tupla = new JSONObject();

                tupla.put("nombre", cursor.getString(0));
                tupla.put("intervalo", cursor.getInt(1));
                tupla.put("hora", cursor.getString(2));
                tupla.put("observaciones", cursor.getString(3));
                tupla.put("device_id", Secure.getString(context.getContentResolver(), Secure.ANDROID_ID));
                tabla.put(tupla);
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        finally {
            cursor.close();
            db.close();
        }

        return tabla;
    }


    public int getNumFilas(String tabla){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT count(*) FROM " + tabla, null);
        int count = -1;
        try{
            while (cursor.moveToNext())
                count = cursor.getInt(0);
        } finally {
            cursor.close();
            db.close();
        }

        return count;
    }

    public void updateEnviado(String estado, String tabla) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        String[] whereArgs = new String[]{estado};

        cv.put("ENVIADO", estado);

        db.update(tabla, cv, "ENVIADO <> ?", whereArgs);

        db.close();
    }

    private String floatArrayToString(float[] data){
        String floatArray = data[0] + "";

        for (int i = 1; i < data.length; i++)
            floatArray += "," + data[i];

        return floatArray;
    }
}
