package com.ivim.ivimadmin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.ivim.ivimadmin.databinding.ActivityPrincipalBinding;

import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Principal extends AppCompatActivity {
    private LinearLayout pefil_administradorr,lista_inspectores,cerrar_sesion;
    private SharedPreferences datos_usuario;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_principal);

        pefil_administradorr=findViewById(R.id.pefil_administradorr);
        lista_inspectores=findViewById(R.id.lista_inspectores);
        cerrar_sesion=findViewById(R.id.cerrar_sesion);

        datos_usuario=getSharedPreferences("Usuario",this.MODE_PRIVATE);
        editor=datos_usuario.edit();

        pefil_administradorr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Principal.this,Perfil.class);
                startActivity(intent);
            }
        });
        lista_inspectores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Principal.this,Inspectores.class);
                startActivity(intent);
            }
        });
        cerrar_sesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.clear().apply();

                Intent ir_informes=new Intent(Principal.this, Login.class);
                startActivity(ir_informes);
            }
        });
    }
}