package com.ksld.appemergencia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ComoFunciona_1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_como_funciona_1);
    }

    public void menu(View vista){
        Intent activity_inicio= new Intent(this, menu.class);
        startActivity(activity_inicio);
    }

    public void siguiente(View vista){
        Intent activity_inicio= new Intent(this, ComoFunciona_2.class);
        startActivity(activity_inicio);
    }
}