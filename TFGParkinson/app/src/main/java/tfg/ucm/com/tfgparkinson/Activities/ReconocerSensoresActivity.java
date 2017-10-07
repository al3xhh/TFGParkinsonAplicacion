package tfg.ucm.com.tfgparkinson.Activities;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import tfg.ucm.com.tfgparkinson.R;

/**
 *
 */
public class ReconocerSensoresActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reconocer_sensores);

        final TextView moverMunyeca = (TextView) findViewById(R.id.moverMunyeca);
        moverMunyeca.setText("Mueve la muñeca izquierda");

        final TextView munyecaDerecha = (TextView) findViewById(R.id.textoMunyecaDcha);
        munyecaDerecha.setVisibility(View.GONE);

        final Button botonMunyecaDcha = (Button) findViewById(R.id.botonMunyecaDcha);
        botonMunyecaDcha.setVisibility(View.GONE);

        final Button botonMunyecaIzq = (Button) findViewById(R.id.botonMunyecaIzq);

        final Button botonOK = (Button) findViewById(R.id.botonOk);
        botonOK.setVisibility(View.GONE);
        botonOK.setOnClickListener(v -> {
            onBackPressed();
        });

        new CountDownTimer(2000, 1000) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                botonMunyecaIzq.setBackground(getResources().getDrawable(R.drawable.ic_done_black_24dp));
                moverMunyeca.setText("Mueve la muñeca derecha");
                munyecaDerecha.setVisibility(View.VISIBLE);
                botonMunyecaDcha.setVisibility(View.VISIBLE);

                new CountDownTimer(2000, 1000) {

                    public void onTick(long millisUntilFinished) {
                    }

                    public void onFinish() {
                        botonMunyecaDcha.setBackground(getResources().getDrawable(R.drawable.ic_done_black_24dp));
                        moverMunyeca.setText("Sensores configurados");
                        botonOK.setVisibility(View.VISIBLE);
                    }
                }.start();
            }
        }.start();
    }
}
