package tfg.ucm.com.tfgparkinson.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import java.util.HashMap;

import tfg.ucm.com.tfgparkinson.Clases.utils.Constantes;
import tfg.ucm.com.tfgparkinson.R;

/**
 * Created by al3x_hh on 05/11/2017.
 */

public class ConfigurarSensores extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configurar_sensores);

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

                            Intent intent = new Intent(ConfigurarSensores.this, EmparejarSensoresActivity.class);
                            intent.putExtra("options", options);
                            startActivity(intent);
                        }
                    } catch (Exception e) {
                        periodo.setError("Valor no válido");
                    }
            }
        });
    }
}
