package com.ksld.appemergencia;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class menu extends AppCompatActivity {
    public AdminSQLite admin;
    public SQLiteDatabase bd;

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


    public void cambiarColor(View view) {
        if (AppCompatDelegate.getDefaultNightMode()==1) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

}