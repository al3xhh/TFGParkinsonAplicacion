package tfg.ucm.com.tfgparkinson.Clases.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by al3x_hh on 12/11/2017.
 */

public class Servidor {

    private Context context;
    private String url;
    private RespuestaServidor delegate;

    public Servidor(Context context, String url) {
        this.context = context;
        this.url = url;
    }

    public void setDelegate(RespuestaServidor delegate) {
        this.delegate = delegate;
    }

    public void sendData(HashMap<String, String> params) {
        final ProgressDialog progressDialog = new ProgressDialog(this.context);

        progressDialog.setMessage("Los datos están siendo procesados en el servidor");
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(context);

        // Request a string response from the provided URL.
        JsonObjectRequest request_json = new JsonObjectRequest(url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("RESPUESTA", response.toString());
                        delegate.processFinish(response);
                        progressDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR", error.toString());
                progressDialog.dismiss();
            }
        });

        // add the request object to the queue to be executed
        queue.add(request_json);
    }
}
