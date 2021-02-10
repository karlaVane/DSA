package com.ksld.appemergencia;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.wafflecopter.multicontactpicker.ContactResult;
import com.wafflecopter.multicontactpicker.LimitColumn;
import com.wafflecopter.multicontactpicker.MultiContactPicker;

import java.util.ArrayList;
import java.util.List;

public class configuracion extends AppCompatActivity {
    public AdminSQLite admin;
    public SQLiteDatabase bd;
    private static final int CONTACT_PICKER_REQUEST=202;
    TextView listaC;
    List<ContactResult> results = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);
        listaC = findViewById(R.id.listaContactos);

        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.SEND_SMS,
                        Manifest.permission.READ_CONTACTS)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                    }
                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                    }
                }).check();

        conectar();
        Cursor fila = bd.rawQuery("select * from listaContactos",null);
        String hola="";
        if (fila.moveToFirst()){
            //fila.moveToFirst();
            do{
                hola += "\n"+fila.getString(1);
            }
            while(fila.moveToNext());
            listaC.setText(hola);
        }
    }

    public void Selec_contactos(View vista){
        new MultiContactPicker.Builder(configuracion.this) //Activity/fragment context
                .hideScrollbar(false) //Optional - default: false
                .showTrack(true) //Optional - default: true
                .searchIconColor(Color.WHITE) //Option - default: White
                .setChoiceMode(MultiContactPicker.CHOICE_MODE_MULTIPLE) //Optional - default: CHOICE_MODE_MULTIPLE
                .bubbleTextColor(Color.WHITE) //Optional - default: White
                .setTitleText("Selecionar Contactos") //Optional - default: Select Contacts
                .setLoadingType(MultiContactPicker.LOAD_ASYNC) //Optional - default LOAD_ASYNC (wait till all loaded vs stream results)
                .limitToColumn(LimitColumn.NONE) //Optional - default NONE (Include phone + email, limiting to one can improve loading time)
                .setActivityAnimations(android.R.anim.fade_in, android.R.anim.fade_out,
                        android.R.anim.fade_in,
                        android.R.anim.fade_out) //Optional - default: No animation overrides
                .showPickerForResult(CONTACT_PICKER_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CONTACT_PICKER_REQUEST){
            if(resultCode == RESULT_OK){
                results=MultiContactPicker.obtainResult(data);
                StringBuilder names= new StringBuilder(results.get(0).getDisplayName());
                String lista=names.toString()+" - "+results.get(0).getPhoneNumbers().get(0).getNumber();
                for (int j=0;j<results.size();j++){
                    if (j!=0){
                        names.append(", ").append(results.get(j).getDisplayName());
                        lista += "\n"+results.get(j).getDisplayName(); //results.get(j).getPhoneNumbers().get(0).getNumber();
                    }
                }
                //listaContactos.setText(names);
                listaC.setText(lista);
            }else if(resultCode==RESULT_CANCELED){
                System.out.println("se cerró");
            }
        }
    }

    public void guardar (View vista){
        conectar();
        bd.execSQL("DELETE FROM listaContactos");
        ContentValues reg = new ContentValues();
        for (int i=0;i<results.size();i++){
            reg.put("numero",results.get(i).getPhoneNumbers().get(0).getNumber());
            reg.put("nombre",results.get(i).getDisplayName());
            bd.insert("listaContactos",null,reg);
        }
        bd.close();
        Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show();
    }

    public void regresar_menu(View vista){
        finish();
    }
    public void salir(View vista){
        moveTaskToBack(true);
    }
    public void conectar(){
        admin=new AdminSQLite(this,"DSA",null,1);
        bd=admin.getWritableDatabase();
    }

}