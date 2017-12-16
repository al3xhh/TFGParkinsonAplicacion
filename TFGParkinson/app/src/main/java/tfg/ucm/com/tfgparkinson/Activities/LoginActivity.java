package tfg.ucm.com.tfgparkinson.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import tfg.ucm.com.tfgparkinson.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final Spinner tipoReconocimiento = (Spinner) findViewById(R.id.tipoDeReconocimiento);
        Button entrar = (Button) findViewById(R.id.botonEntrar);

        entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i;

                if(tipoReconocimiento.getSelectedItem().toString().equals("Libre")) {
                    i = new Intent(LoginActivity.this, MainActivityLibre.class);
                } else {
                    i = new Intent(LoginActivity.this, MainActivityActividad.class);
                }

                startActivity(i);
            }
        });
    }
}
