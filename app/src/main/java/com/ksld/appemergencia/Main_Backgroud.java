package com.ksld.appemergencia;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

public class Main_Backgroud extends AppCompatActivity {
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static final String[] permissions = {Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA};
    private boolean audioRecordingPermissionGranted = false;

    private String fileName;
    private Bitmap captureImage;
    private Button startRecordingButton, stopRecordingButton, playRecordingButton, stopPlayingButton;
    private Button btOpen, btnKill, btnEnviar;
    private MediaRecorder recorder;
    private MediaPlayer player;
    private String image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__backgroud);
        /*
        //Servicio FOREGROUD
        Intent i = new Intent(this, VolumenService.class);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            startForegroundService(i);
        }else{
            //Si no soporta ejecuto como servicio normal
            startService(i);
        }*/
        //ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
        /*if (ContextCompat.checkSelfPermission(Main_Backgroud.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions( Main_Backgroud.this, new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO
            },100);
        }*/
        startRecordingButton = (Button)findViewById(R.id.activity_main_record);
        stopRecordingButton = (Button)findViewById(R.id.activity_main_stop);
        playRecordingButton = (Button)findViewById(R.id.activity_main_play);
        stopPlayingButton = (Button)findViewById(R.id.activity_main_stop_playing);
        btOpen = (Button)findViewById(R.id.btn_open);
        btnKill = (Button)findViewById(R.id.btnKill);
        btnEnviar = (Button)findViewById(R.id.btn_enviar);
        playRecordingButton.setVisibility(View.GONE);
        stopPlayingButton.setVisibility(View.GONE);

        startRecordingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRecording();
                playRecordingButton.setVisibility(View.VISIBLE);
                stopPlayingButton.setVisibility(View.VISIBLE);
            }
        });


        stopRecordingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopRecording();
            }
        });


        playRecordingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playRecording();

            }
        });

        stopPlayingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopPlaying();
            }
        });

        // Request For Camera Permission
        /*
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions( MainActivity.this, new String[]{
                    Manifest.permission.CAMERA
            },100);
        }
         */

        btOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Open Camera
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,100);
            }
        });

        btnKill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iKill = new Intent(getApplicationContext(), VolumenService.class);
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                    stopService(iKill);
                }else{
                    //Si no soporta ejecuto como servicio normal
                    stopService(iKill);
                }
            }
        });

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fileName!= null && image != null){
                    Intent i = new Intent(Main_Backgroud.this, ConexionWhats.class);
                    i.putExtra("Image", image);
                    i.putExtra("Audio", fileName);
                    mensaje("Todo Correcto");
                    startActivity(i);
                }else{
                    mensaje("Primero Grabe un Audio y tome una foto");
                }
            }
        });
    }

    //***********************************//
    //***********************************//
            //Imagen
    //***********************************//
    //***********************************//

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            // Get Capture Image
            captureImage = (Bitmap) data.getExtras().get("data"); //Variable que guarda la imagen

            // BITARRAY
            image=getImageByte(captureImage);


            //ByteArrayOutputStream stream = new ByteArrayOutputStream();
            //captureImage.compress(Bitmap.CompressFormat.JPEG,100, stream);
            //byte[] byteArray = stream.toByteArray();
            //https://stackoverflow.com/questions/20329090/how-to-convert-a-bitmap-to-a-jpeg-file-in-android/20329141
        }
    }

    //For encoding toString
    public String getImageByte(Bitmap bmp){
       /* ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        //String encodedImage = android.util.Base64.encodeToString(imageBytes, Base64.DEFAULT);
        //return encodedImage;*/
        File outputDir = getApplicationContext().getCacheDir();
        File imageFile = new File(outputDir, "img.jpg");

        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e(getApplicationContext().getClass().getSimpleName(), "Error writing file", e);
        }

        return imageFile.getAbsolutePath();
        //return imageBytes;
    }





    //***********************************//
    //***********************************//
    //Imagen
    //***********************************//
    //***********************************//


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_RECORD_AUDIO_PERMISSION:
                audioRecordingPermissionGranted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }

        if (!audioRecordingPermissionGranted) {
            finish();
        }
    }

    private void startRecording() {
        String uuid = UUID.randomUUID().toString();
        fileName = getFilesDir().getPath() + "/" + uuid + ".3gp"; //Variable que guarda el audio
        Log.i(MainActivity.class.getSimpleName(), fileName);

        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e(MainActivity.class.getSimpleName() + ":startRecording()", "prepare() failed");
        }

        recorder.start();

        //startRecorderService();
    }

    private void stopRecording() {
        if (recorder != null) {
            recorder.release();
            recorder = null;
            //stopRecorderService();
        }
    }

    private void playRecording() {
        player = new MediaPlayer();
        try {
            player.setDataSource(fileName);
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    stopPlaying();
                }
            });
            player.prepare();
            player.start();
            //startPlayerService();
        } catch (IOException e) {
            Log.e(MainActivity.class.getSimpleName() + ":playRecording()", "prepare() failed");
        }
    }

    private void stopPlaying() {
        if (player != null) {
            player.release();
            player = null;
            //stopPlayerService();
        }
    }

    private void startRecorderService() {
        Intent serviceIntent = new Intent(this, RecorderService.class);
        serviceIntent.putExtra("inputExtra", "Recording in progress");
        ContextCompat.startForegroundService(this, serviceIntent);

    }

    private void stopRecorderService() {
        Intent serviceIntent = new Intent(this, RecorderService.class);
        stopService(serviceIntent);
    }

    private void startPlayerService() {
        Intent serviceIntent = new Intent(this, PlayerService.class);
        serviceIntent.putExtra("inputExtra", "Playing recording");
        ContextCompat.startForegroundService(this, serviceIntent);
    }

    private void stopPlayerService() {
        Intent serviceIntent = new Intent(this, PlayerService.class);
        stopService(serviceIntent);
    }

    public void mensaje(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }



}