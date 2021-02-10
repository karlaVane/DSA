package com.ksld.appemergencia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

public class menu extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        if (ContextCompat.checkSelfPermission(menu.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions( menu.this, new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO
            },100);
        }
    }
    public void ac_funcionalidad(View vista){
        Intent activity_inicio= new Intent(this, ComoFunciona_1.class);
        startActivity(activity_inicio);
    }
    public void ac_configuracion(View vista){
        Intent activity_inicio= new Intent(this,configuracion.class);
        startActivity(activity_inicio);
    }

    public void salir(View vista){
        moveTaskToBack(true);
    }

}