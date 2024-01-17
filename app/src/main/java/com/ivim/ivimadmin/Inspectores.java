package com.ivim.ivimadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Inspectores extends AppCompatActivity {
    private ExecutorService executorService;
    private static String SERVIDOR_CONTROLADOR;
    private SharedPreferences datosUsuario,datosInspector;
    private SharedPreferences.Editor editor,editorInspector;
    private String id_admin,id_sesion;
    private JSONArray json_datos_inspectores;
    private ArrayList<ListaInspectores> listaInspectores;
    private AdapterListaInspectores adapterListaInspectores;
    private RecyclerView recycler_inspectores;
    private Context context;
    private Inspectores activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspectores);
        datosUsuario = getSharedPreferences("Usuario",this.MODE_PRIVATE);
        editor=datosUsuario.edit();
        datosInspector = getSharedPreferences("Inspector",this.MODE_PRIVATE);
        editorInspector=datosInspector.edit();
        id_admin=datosUsuario.getString("id","no");
        id_sesion=datosUsuario.getString("id_sesion","no hay");
        SERVIDOR_CONTROLADOR = new  Servidor().servidor;
        listaInspectores= new ArrayList<>();
        recycler_inspectores=findViewById(R.id.recycler_inspectores);
        context=this;
        recycler_inspectores.setLayoutManager(new LinearLayoutManager(context));
        executorService= Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                pedirInspectores();


            }
        });

    }
    public void verActividadInspector(String id,String nombres,String apellido_1,String apellido_2,String telefono,String correo ,int posiTion,String fecha_ingreso,String latitud,String longitud, String ultima_fecha_conexion){
        editorInspector.putString("id",id);
        editorInspector.putString("nombres",nombres);
        editorInspector.putString("apellido_1",apellido_1);
        editorInspector.putString("apellido_2",apellido_2);
        editorInspector.putString("telefono",telefono);
        editorInspector.putString("correo",correo);
        editorInspector.putString("fecha_ingreso",fecha_ingreso);
        editorInspector.putString("latitud",latitud);
        editorInspector.putString("longitud",longitud);
        editorInspector.putString("ultima_fecha_conexion",ultima_fecha_conexion);
        editorInspector.apply();

        Intent intent = new Intent(Inspectores.this, MapaInspector.class);
        startActivity(intent);

    }
    public void pedirInspectores()
    {
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST,  SERVIDOR_CONTROLADOR+"pedir_inspectores.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("respuesta:",response);
                        try {
                            json_datos_inspectores=new JSONArray(response);
                            for(int i=0;i<json_datos_inspectores.length();i++){
                                JSONObject jsonObject = json_datos_inspectores.getJSONObject(i);
                                String strId = jsonObject.getString("id");
                                String  strNombres = jsonObject.getString("nombres");
                                String  strApellido_1 = jsonObject.getString("apellido_1");
                                String  straApellido_2 = jsonObject.getString("apellido_2");
                                String  strTelefono = jsonObject.getString("telefono");
                                String  strCorreo = jsonObject.getString("correo");
                                String  strFecha_ingreso= jsonObject.getString("fecha_ingreso");
                                String  strLatitud= jsonObject.getString("latitud");
                                String  strLongitud= jsonObject.getString("longitud");
                                String  strUltima_fecha_conexion= jsonObject.getString("ultima_fecha_conexion");
                                listaInspectores.add(new ListaInspectores(strId,strNombres,strApellido_1,straApellido_2,strTelefono,strCorreo,strFecha_ingreso,strLatitud,strLongitud,strUltima_fecha_conexion));
                                adapterListaInspectores=new AdapterListaInspectores(activity,R.layout.item_lista_inspectores,listaInspectores,getResources());
                                recycler_inspectores.setAdapter(adapterListaInspectores);
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e( "error", "error: " +error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("id",id_admin);
                map.put("id_sesion",id_sesion);
                return map;
            }
        };
        requestQueue.add(request);
    }

}