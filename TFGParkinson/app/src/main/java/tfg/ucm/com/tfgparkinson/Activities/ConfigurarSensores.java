package tfg.ucm.com.tfgparkinson.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import tfg.ucm.com.tfgparkinson.Clases.BBDD.GestorBD;
import tfg.ucm.com.tfgparkinson.Clases.utils.Constantes;
import tfg.ucm.com.tfgparkinson.Clases.utils.OpcionesVO;
import tfg.ucm.com.tfgparkinson.R;

/**
 * Created by al3x_hh on 05/11/2017.
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
                HashMap<Integer, Byte> options = new HashMap<>();

                Spinner rangoAcelerometro = (Spinner) findViewById(R.id.rangoAcelerometro);
                EditText periodo = (EditText) findViewById(R.id.periodoAcelerometro);
                CheckBox acelerometro = (CheckBox) findViewById(R.id.acelerometroCheckBox);
                CheckBox giroscopio = (CheckBox) findViewById(R.id.giroscopioCheckBox);
                CheckBox magnetometro = (CheckBox) findViewById(R.id.magnetometroCheckBox);
                Switch wakeOn = (Switch) findViewById(R.id.wakeOnSwitch);

                options.put(Constantes.ACCL_RANGE, Constantes.getRange(rangoAcelerometro.getSelectedItem().toString()));

                if(acelerometro.isChecked())
                    options.put(Constantes.ACCL_ON, Constantes.ACCL_ON_VALUE);
                else
                    options.put(Constantes.ACCL_ON, Constantes.OFF);
                if(giroscopio.isChecked())
                    options.put(Constantes.GYRO_ON, Constantes.GYRO_ON_VALUE);
                else
                    options.put(Constantes.GYRO_ON, Constantes.OFF);
                if(magnetometro.isChecked())
                    options.put(Constantes.MAGN_ON, Constantes.MAGN_ON_VALUE);
                else
                    options.put(Constantes.MAGN_ON, Constantes.OFF);
                if(!wakeOn.isChecked())
                    options.put(Constantes.WAKE_ON_MOTION, Constantes.OFF);
                if(periodo.getText().toString().equals(""))
                    options.put(Constantes.PERIOD, Constantes.DEFAULT_PERIOD);
                else
                    try {
                        if (Integer.parseInt(periodo.getText().toString()) < 100 || Integer.parseInt(periodo.getText().toString()) > 2550)
                            periodo.setError("Valor no válido, debe estar entre 100 y 2550");
                        else {
                            options.put(Constantes.PERIOD, Byte.valueOf((String.valueOf(Integer.parseInt(periodo.getText().toString()) / 10))));
                            Log.d("Opciones", options.toString());
                        }
                    } catch (Exception e) {
                        periodo.setError("Valor no válido");
                    }

                String positions = checkPositions((ListView) findViewById(R.id.listaSensores));
                if(!positions.equals("-1")) {
                    GestorBD gestorBD = new GestorBD(getApplicationContext());
                    if(!gestorBD.checkPosicion(positions))
                        gestorBD.insertPosicion(positions);

                    OpcionesVO opcionesVO = new OpcionesVO();
                    opcionesVO.setSensorsOptions(options);
                    opcionesVO.setSensorPositions(gestorBD.getPosicionID(positions));
                    opcionesVO.setContext(ConfigurarSensores.this);

                    Intent intent = new Intent(ConfigurarSensores.this, EmparejarSensoresActivity.class);
                    intent.putExtra("options", opcionesVO);
                    startActivity(intent);
                }
            }
        });
    }

    private String checkPositions(ListView list) {
        View v1, v2;
        StringBuilder ret = new StringBuilder();

        for(int i = 0; i < list.getCount(); i++) {
            v1 = list.getChildAt(i);
            Spinner bodyPartsI = (Spinner) v1.findViewById(R.id.partesCuerpo);
            String selectedI = bodyPartsI.getSelectedItem().toString();
            TextView macI = (TextView) v1.findViewById(R.id.macSensor);
            String selectedMacI = macI.getText().toString();
            ret.append(selectedMacI).append(";").append(selectedI).append(";");

            for(int j = 0; j < list.getCount(); j++) {
                v2 = list.getChildAt(j);
                Spinner bodyPartsJ = (Spinner) v2.findViewById(R.id.partesCuerpo);
                String selectedJ = bodyPartsJ.getSelectedItem().toString();
                TextView macJ = (TextView) v2.findViewById(R.id.macSensor);
                String selectedMacJ = macJ.getText().toString();

                if(!selectedMacI.equals(selectedMacJ) && selectedI.equals(selectedJ)) {
                    macJ.setError("Parte de cuerpo elegida ya");
                    return "-1";
                }
            }
        }

        return ret.toString();
    }
}
