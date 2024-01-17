package com.ivim.ivimadmin;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ivim.ivimadmin.databinding.ActivityMapaInspectorBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;

public class MapaInspector extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String id_deudor_str,nombr_completo_str,id_admin,id_sesion,strId_verificador,id_inspector;

    private ExecutorService executorService;
    private static String SERVIDOR_CONTROLADOR;
    private SharedPreferences datosInspector,datosUsuario;
    private SharedPreferences.Editor editorInspector,editor;

    private double latitud, longitud;

    private Marker marker,lastMarker,marcadorDeudor;
    private Fragment map;
    private MapaInspector activity;
    private JSONArray json_gps_inspectores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_inspector);
        datosUsuario = getSharedPreferences("Usuario",this.MODE_PRIVATE);
        editor=datosUsuario.edit();
        datosInspector = getSharedPreferences("Inspector",this.MODE_PRIVATE);
        id_inspector=datosInspector.getString("id","no hay");
        editorInspector=datosInspector.edit();
        id_admin=datosUsuario.getString("id","no");
        id_sesion=datosUsuario.getString("id_sesion","no hay");
        SERVIDOR_CONTROLADOR = new  Servidor().servidor;
        datosInspector = getSharedPreferences("Inspector",this.MODE_PRIVATE);
        editorInspector=datosInspector.edit();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        String lat_tmp=datosInspector.getString("latitud","no hay");
        String long_tmp=datosInspector.getString("longitud","no hay");
        String nombres_tmp=datosInspector.getString("nombres","no hay");
        String apellidos_1_tmp=datosInspector.getString("apellido_1","no hay");
        String apellidos_2_tmp=datosInspector.getString("apellido_2","no hay");
        nombr_completo_str=nombres_tmp+" "+apellidos_1_tmp+" "+apellidos_2_tmp;
        id_deudor_str=datosInspector.getString("id","no hay");

        if(!lat_tmp.equals("0")){
            latitud= Double.parseDouble(lat_tmp);
            longitud= Double.parseDouble(long_tmp);
            Log.e("pralat", String.valueOf(latitud));
            Log.e("pralong", String.valueOf(longitud));
            LatLng inspector=new LatLng(latitud,longitud);

            // Infla tu layout personalizado
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View customMarkerView = inflater.inflate(R.layout.custom_marker_layout, null);
            // Opcional: Modifica el layout, como cambiar el texto del TextView
            TextView markerTextView = customMarkerView.findViewById(R.id.marker_text);
            markerTextView.setText(id_deudor_str);
            LatLng coordenadas = new LatLng(latitud,longitud);
            Bitmap customMarkerBitmap = getBitmapFromView(customMarkerView);
            CameraUpdate miUbicacion = CameraUpdateFactory.newLatLngZoom(inspector, 18);
            mMap.moveCamera(miUbicacion);
            marcadorDeudor = mMap.addMarker(new MarkerOptions()
                    .position(coordenadas)
                    .title(nombr_completo_str)
                    .icon(BitmapDescriptorFactory.fromBitmap(customMarkerBitmap)));
        }
        else{
            Toast.makeText(getApplicationContext(), "No existe nignuna ubicacion con esta direccion", Toast.LENGTH_LONG).show();
        }
        pedirUltimasUbicaciones();
    }
    private Bitmap getBitmapFromView(View view) {
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.draw(canvas);
        return bitmap;
    }
    public void pedirUltimasUbicaciones()
    {
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST,  SERVIDOR_CONTROLADOR+"pedir_ultimas_ubicaciones.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("respuestaUbicaicones:",response);
                        try {
                            json_gps_inspectores=new JSONArray(response);
                            for(int i=0;i<json_gps_inspectores.length();i++){
                                JSONObject jsonObject = json_gps_inspectores.getJSONObject(i);
                                String strId = jsonObject.getString("id");
                                strId_verificador = jsonObject.getString("id_verificador");
                                String  strFecha = jsonObject.getString("fecha");
                                String  strDireccion = jsonObject.getString("direccion");
                                String  strLatitud= jsonObject.getString("latitud");
                                String  strLongitud= jsonObject.getString("longitud");
                                LatLng ubicacion = new LatLng(Double.parseDouble(strLatitud), Double.parseDouble(strLongitud));
                                MarkerOptions markerOptions = new MarkerOptions().position(ubicacion).title("Fecha: " + strFecha).snippet("Dirección: " + strDireccion);

                                // Verificar si es la última ubicación
                                if (i == json_gps_inspectores.length() - 1) {
                                    // Usar un icono diferente para la última ubicación
                                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                                } else {
                                    // Usar el icono predeterminado para las ubicaciones anteriores
                                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                                }

                                mMap.addMarker(markerOptions);
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacion, 18));
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
                map.put("id_verificador",id_inspector);

                return map;
            }
        };
        requestQueue.add(request);
    }
}