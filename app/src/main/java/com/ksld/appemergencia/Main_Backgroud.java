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
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
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

        setContentView(R.layout.activity_main);

        startRecordingButton = findViewById(R.id.activity_main_record);
        stopRecordingButton = findViewById(R.id.activity_main_stop);
        playRecordingButton = findViewById(R.id.activity_main_play);
        stopPlayingButton = findViewById(R.id.activity_main_stop_playing);
        btOpen = findViewById(R.id.btn_open);
        btnKill = findViewById(R.id.btnKill);
        btnEnviar = findViewById(R.id.btn_enviar);

        playRecordingButton.setVisibility(View.INVISIBLE);
        stopPlayingButton.setVisibility(View.INVISIBLE);

        startRecordingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRecording();
                playRecordingButton.setVisibility(View.VISIBLE);
            }
        });


        stopRecordingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopRecording();
                stopPlayingButton.setVisibility(View.VISIBLE);
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
                if(fileName!="" && captureImage != null){
                    Intent i = new Intent();
                    i.putExtra("BitmapImg", captureImage);
                    i.putExtra("Audio", fileName);

                }else{
                    mensaje("Primero Grabe un Audio y tome una foto");
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            // Get Capture Image
            captureImage = (Bitmap) data.getExtras().get("data"); //Variable que guarda la imagen
        }
    }



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

        startRecorderService();
    }

    private void stopRecording() {
        if (recorder != null) {
            recorder.release();
            recorder = null;
            stopRecorderService();
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
            startPlayerService();
        } catch (IOException e) {
            Log.e(MainActivity.class.getSimpleName() + ":playRecording()", "prepare() failed");
        }
    }

    private void stopPlaying() {
        if (player != null) {
            player.release();
            player = null;
            stopPlayerService();
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