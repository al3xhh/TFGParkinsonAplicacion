package tfg.ucm.com.tfgparkinson.Clases.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


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

    public void sendData(JSONArray params, int method) {
        final ProgressDialog progressDialog = new ProgressDialog(this.context);

        progressDialog.setMessage("Los datos están siendo procesados en el servidor");
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(context);

        // Request a string response from the provided URL.
        JsonArrayRequest request_json;

        request_json = new JsonArrayRequest(method, url, params,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("RESPUESTA", response.toString());
                        String [] splittedUrl = url.split("/");
                        delegate.processFinish(splittedUrl[splittedUrl.length - 1]);
                        progressDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    if (error.getMessage() != null){
                        StringBuilder builder = new StringBuilder(error.getMessage());
                        builder.replace(0, 30, "");
                        JSONObject message = new JSONObject(builder.toString());

                        if (message.get("_status").equals("OK")) {
                            String[] splittedUrl = url.split("/");
                            delegate.processFinish(splittedUrl[splittedUrl.length - 1]);
                        }
                }
                } catch (JSONException e) {
                    delegate.processFinish("ERROR");
                    e.printStackTrace();
                }
                VolleyLog.d("ERROR LISTENER", error.getMessage());
                progressDialog.dismiss();
            }
        });


        // add the request object to the queue to be executed
        queue.add(request_json);
    }
}
