package com.ivim.ivimadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Login extends AppCompatActivity {

    private ExecutorService executorService;

    private EditText correo,contrasena;
    private TextView ingresar,mensaje;
    private String valCorreo,valContra,correo_final;
    private static String SERVIDOR_CONTROLADOR;
    private int check=0;
    private SharedPreferences datosUsuario;
    private SharedPreferences.Editor editor;
    private boolean correo_exitoso,contrasena_exitoso;
    private JSONArray json_datos_usuario;
    private  String strInicio,strUsuario;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_login);

        executorService= Executors.newSingleThreadExecutor();
        SERVIDOR_CONTROLADOR = new  Servidor().servidor;
        datosUsuario = getSharedPreferences("Usuario",this.MODE_PRIVATE);
        editor=datosUsuario.edit();
        correo=findViewById(R.id.correo);
        contrasena =findViewById(R.id.contrasena);
        ingresar= findViewById(R.id.ingresar);
        mensaje =findViewById(R.id.mensaje);

        checkSesion();

        ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                valCorreo=correo.getText().toString();
                valContra=contrasena.getText().toString();
                Log.e("datocorreo",valCorreo );
                Log.e("datocontra",valContra );
                if(!valCorreo.trim().equals("")){
                    if(!valContra.trim().equals("")){
                        if(correo_exitoso==true){

                            ingresar.setVisibility(View.GONE);
                            mensaje.setText("Iniciando sesión ...");
                            mensaje.setVisibility(View.VISIBLE);
                            executorService.execute(new Runnable() {
                                @Override
                                public void run() {
                                    hacerPeticion();

                                }
                            });


                        }
                        else{
                            correo_exitoso = false;
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "La contrasena es necesario.", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "El correo es necesario.", Toast.LENGTH_LONG).show();
                }

            }

        });

        correo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean tieneFoco) {
                correo_final=correo.getText().toString().trim().toLowerCase();

                if(!tieneFoco)
                {
                    if (!correo_final.equals("")&&correo_final!=null)
                    {
                        // String regex = "^(.+)@(.+)$";

                        String regexUsuario = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";
                        Pattern pattern = Pattern.compile(regexUsuario);
                        Matcher matcher = pattern.matcher(correo_final);
                        if(matcher.matches()==true){
                            correo_exitoso=true;
                        }
                    }
                }
                else{
                    if(!correo_final.equals("")){
                        Toast.makeText(getApplicationContext(),"Ingrese correo valido.",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }


    public void hacerPeticion()
     {
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST,  SERVIDOR_CONTROLADOR+"inicio_sesion_appAdmin.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("respuesta:",response);
                        if (response.equals("no_existe")) {
                            ingresar.setVisibility(View.VISIBLE);
                            mensaje.setText("El teléfono o correo es incorrecto.");
                        } else if (response.contains("success")) {
                            try {
                                JSONObject jsonDatosUsuario = new JSONObject(response);

                                if (jsonDatosUsuario.getString("estatus").equals("success")) {
                                    Log.e("lala", "" + jsonDatosUsuario);

                                    String strId = jsonDatosUsuario.getString("id");
                                    String strId_sesion=jsonDatosUsuario.getString("id_sesion");
                                    String strTelefono=jsonDatosUsuario.getString("telefono");
                                    String strCorreo=jsonDatosUsuario.getString("correo");
                                    Log.e("strId", "" + strId);
                                    Log.e("strId_sesion", "" + strId_sesion);
                                    Log.e("strTelefono", "" + strTelefono);
                                    Log.e("strCorreo", "" + strCorreo);

                                    editor.putString("id",strId);
                                    editor.putString("telefono",strTelefono);
                                    editor.putString("correo",strCorreo);
                                    editor.putString("id_sesion",strId_sesion);
                                    editor.apply();
                                    Intent intent = new Intent(Login.this, Principal.class);
                                    startActivity(intent);

                                } else if (jsonDatosUsuario.getString("estatus").equals("no_existe")) {
                                    ingresar.setVisibility(View.VISIBLE);
                                    mensaje.setText("El teléfono o correo es incorrecto.");
                                }




                            }
                            catch (JSONException e) {
                                Log.e("errorRespuesta", String.valueOf(e));
                            }
                        }
                        {

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
                map.put("correo",valCorreo);
                map.put("contra",valContra);
                return map;
            }
        };
        requestQueue.add(request);
    }



    private void checkSesion() {
        strInicio = datosUsuario.getString("id_sesion", "no");

        Log.e("inicio",""+strInicio);
        if (!strInicio.equals("no"))
        {

            Log.e("idsesion_main",strInicio);
            Intent agenda= new Intent(Login.this, Principal.class);
            startActivity(agenda);
        }
    }
}