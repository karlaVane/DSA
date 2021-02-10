package com.ksld.appemergencia;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

public class ComoFunciona_3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_como_funciona_2);
    }

    public void menu(View vista){
        Intent activity_inicio= new Intent(this, menu.class);
        startActivity(activity_inicio);
    }

    public void siguiente(View vista){
        Intent activity_inicio= new Intent(this, ComoFunciona_4.class);
        startActivity(activity_inicio);
    }
}