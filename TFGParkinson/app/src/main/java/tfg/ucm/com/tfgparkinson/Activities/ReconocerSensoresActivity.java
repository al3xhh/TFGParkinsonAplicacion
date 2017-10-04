package tfg.ucm.com.tfgparkinson.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import tfg.ucm.com.tfgparkinson.R;

/**
 * Created by al3x_hh on 04/10/2017.
 */

public class ReconocerSensoresActivity extends AppCompatActivity {

    private boolean sensorDerecho, sensorIzquierdo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reconocer_sensores);

        this.sensorIzquierdo = false;
        this.sensorDerecho = false;

        final TextView moverMunyeca = (TextView) findViewById(R.id.moverMunyeca);
        moverMunyeca.setText("Mueve la muñeca izquierda");

        final TextView munyecaDerecha = (TextView) findViewById(R.id.textoMunyecaDcha);
        munyecaDerecha.setVisibility(View.GONE);

        final Button botonMunyecaDcha = (Button) findViewById(R.id.botonMunyecaDcha);
        botonMunyecaDcha.setVisibility(View.GONE);

        final Button botonMunyecaIzq = (Button) findViewById(R.id.botonMunyecaIzq);

        final Button botonEntrar = (Button) findViewById(R.id.botonEntrar);
        botonEntrar.setVisibility(View.GONE);
        botonEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ReconocerSensoresActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

        new CountDownTimer(4000, 1000) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                sensorIzquierdo = true;
                botonMunyecaIzq.setBackground(getResources().getDrawable(R.drawable.ic_done_black_24dp));
                moverMunyeca.setText("Mueve la muñeca derecha");
                munyecaDerecha.setVisibility(View.VISIBLE);
                botonMunyecaDcha.setVisibility(View.VISIBLE);

                new CountDownTimer(4000, 1000) {

                    public void onTick(long millisUntilFinished) {
                    }

                    public void onFinish() {
                        sensorDerecho = true;
                        botonMunyecaDcha.setBackground(getResources().getDrawable(R.drawable.ic_done_black_24dp));
                        moverMunyeca.setText("Ya puedes entrar!");
                        botonEntrar.setVisibility(View.VISIBLE);
                    }
                }.start();
            }
        }.start();
    }
}
