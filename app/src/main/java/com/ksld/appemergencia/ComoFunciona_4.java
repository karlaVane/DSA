package com.ksld.appemergencia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ComoFunciona_4 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_como_funciona_4);
    }

    public void menu(View vista){
        Intent activity_inicio= new Intent(this, menu.class);
        startActivity(activity_inicio);
    }

    public void siguiente(View vista){
        Intent activity_inicio= new Intent(this, menu.class);
        startActivity(activity_inicio);
    }
}