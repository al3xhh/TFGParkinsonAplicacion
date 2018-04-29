package tfg.ucm.com.tfgparkinson.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;

import android.view.View;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.HashMap;

import tfg.ucm.com.tfgparkinson.Adaptadores.AdaptadorSensores;
import tfg.ucm.com.tfgparkinson.Clases.BBDD.GestorBD;
import tfg.ucm.com.tfgparkinson.Clases.utils.Constantes;
import tfg.ucm.com.tfgparkinson.Clases.utils.OpcionesVO;
import tfg.ucm.com.tfgparkinson.R;

/**
 * Actividad que permite configurar los sensores.
 */
public class ConfigurarSensores extends AppCompatActivity implements Serializable {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configurar_sensores);

        ListView listaSensores = (ListView) findViewById(R.id.listaSensores);

        ArrayList<String> macs = new ArrayList<>();
        macs.add("00:00:00:00:00:00");
        macs.add("00:00:00:00:00:01");

        AdaptadorSensores adaptadorSensores = new AdaptadorSensores(getApplicationContext(), R.layout.representacion_item_lista, macs);
        listaSensores.setAdapter(adaptadorSensores);

        Button comenzar = (Button) findViewById(R.id.botonComenzar);
        comenzar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                @SuppressLint("UseSparseArrays") HashMap<Integer, Byte> opciones = new HashMap<>();

                Spinner rangoAcelerometro = (Spinner) findViewById(R.id.rangoAcelerometro);
                EditText periodo = (EditText) findViewById(R.id.periodoAcelerometro);
                CheckBox acelerometro = (CheckBox) findViewById(R.id.acelerometroCheckBox);
                CheckBox giroscopio = (CheckBox) findViewById(R.id.giroscopioCheckBox);
                CheckBox magnetometro = (CheckBox) findViewById(R.id.magnetometroCheckBox);
                Switch wakeOn = (Switch) findViewById(R.id.wakeOnSwitch);

                opciones.put(Constantes.ACCL_RANGE, obtenerRango(rangoAcelerometro.getSelectedItem().toString()));

                if(acelerometro.isChecked())
                    opciones.put(Constantes.ACCL_ON, Constantes.ACCL_ON_VALUE);
                else
                    opciones.put(Constantes.ACCL_ON, Constantes.OFF);
                if(giroscopio.isChecked())
                    opciones.put(Constantes.GYRO_ON, Constantes.GYRO_ON_VALUE);
                else
                    opciones.put(Constantes.GYRO_ON, Constantes.OFF);
                if(magnetometro.isChecked())
                    opciones.put(Constantes.MAGN_ON, Constantes.MAGN_ON_VALUE);
                else
                    opciones.put(Constantes.MAGN_ON, Constantes.OFF);
                if(!wakeOn.isChecked())
                    opciones.put(Constantes.WAKE_ON_MOTION, Constantes.OFF);
                if(periodo.getText().toString().equals(""))
                    opciones.put(Constantes.PERIOD, Constantes.DEFAULT_PERIOD);
                else
                    try {
                        if (Integer.parseInt(periodo.getText().toString()) < 100 || Integer.parseInt(periodo.getText().toString()) > 2550)
                            periodo.setError("Valor no válido, debe estar entre 100 y 2550");
                        else {
                            opciones.put(Constantes.PERIOD, Byte.valueOf((String.valueOf(Integer.parseInt(periodo.getText().toString()) / 10))));
                        }
                    } catch (Exception e) {
                        periodo.setError("Valor no válido");
                    }

                String posiciones = comprobarPosiciones((ListView) findViewById(R.id.listaSensores));

                if(!posiciones.equals("-1")) {
                    GestorBD gestorBD = new GestorBD(getApplicationContext());

                    if(!gestorBD.checkPosicion(posiciones))
                        gestorBD.insertPosicion(posiciones);

                    OpcionesVO opcionesVO = new OpcionesVO();
                    opcionesVO.setSensorsOptions(opciones);
                    opcionesVO.setSensorPositions(gestorBD.getPosicionID(posiciones));

                    Intent intent = new Intent(ConfigurarSensores.this, EmparejarSensores.class);
                    intent.putExtra("options", opcionesVO);
                    startActivity(intent);
                }
            }
        });
    }

    /**
     * Función que indica el rango del acelerómetro.
     * @param value valor que se desea conseguir.
     * @return rango del acelerómetro correspondiente al valor.
     */
    private static Byte obtenerRango(String value) {
        if(value.equals("2G"))
            return Constantes.ACCL_RANGE_2G;
        if(value.equals("4G"))
            return Constantes.ACCL_RANGE_4G;
        if(value.equals("8G"))
            return Constantes.ACCL_RANGE_8G;
        else
            return Constantes.ACCL_RANGE_16G;
    }

    /**
     * Función que comprueba que los sensores se pongan en partes del cuerpo diferentes.
     * @param list lista con las posiciones elegidas.
     * @return -1 en caso de que algo haya fallado, posiciones en caso contrario.
     */
    private String comprobarPosiciones(ListView list) {
        View v1, v2;
        StringBuilder ret = new StringBuilder();

        for(int i = 0; i < list.getCount(); i++) {
            v1 = list.getChildAt(i);
            Spinner parteCuerpoI = (Spinner) v1.findViewById(R.id.partesCuerpo);
            String seleccionadoI = parteCuerpoI.getSelectedItem().toString();
            TextView macI = (TextView) v1.findViewById(R.id.macSensor);
            String macSeleccionadaI = macI.getText().toString();
            ret.append(macSeleccionadaI).append(";").append(seleccionadoI).append(";");

            for(int j = 0; j < list.getCount(); j++) {
                v2 = list.getChildAt(j);
                Spinner parteCuerpoJ = (Spinner) v2.findViewById(R.id.partesCuerpo);
                String seleccionadoJ = parteCuerpoJ.getSelectedItem().toString();
                TextView macJ = (TextView) v2.findViewById(R.id.macSensor);
                String macSeleccionadaJ = macJ.getText().toString();

                if(!macSeleccionadaI.equals(macSeleccionadaJ) && seleccionadoI.equals(seleccionadoJ)) {
                    macJ.setError("Parte de cuerpo elegida ya");
                    return "-1";
                }
            }
        }

        return ret.toString();
    }
}
