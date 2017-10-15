package tfg.ucm.com.tfgparkinson.Clases.BBDD;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

/**
 * Created by al3x_hh on 15/10/2017.
 */
public class GestorFirebase {
    private DatabaseReference myRef;

    public GestorFirebase(String collection) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference(collection);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword("prueba@ucm.es", "123456");
    }

    public void guardarDatos(HashMap datos) {
        myRef.setValue(datos);
    }
}
