package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.ActionMode;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.IOError;

public class MainActivity extends AppCompatActivity {
    SeekBar seekBar;
    boolean success =false ;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setMax(255);
        seekBar.setProgress(getBrightness());
        getPermission();



        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar,int progress,boolean fromUser) {
                if (fromUser && success){
                    setBrightness(progress);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (!success){
                    Toast.makeText(MainActivity.this,"Permission not granted!" ,Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    private void setBrightness(int brightness){
        if(brightness<0){
            brightness=0;
        }else if(brightness>255){
            brightness=255;
        }


        ContentResolver contentResolver = getApplicationContext().getContentResolver();
        Settings.System.putInt(contentResolver , Settings.System.SCREEN_BRIGHTNESS ,brightness);

    }
    private int getBrightness(){
        int brightness=100;
        try {
            ContentResolver contentResolver = getApplicationContext().getContentResolver();
            brightness = Settings.System.getInt(contentResolver , Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e){
            e.printStackTrace();

        }
        return brightness;
    }
    private void getPermission(){
        boolean value;
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
        value = Settings.System.canWrite(getApplicationContext());
        if (value) {
            success = true;
        }else{
            Intent intent =new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
            intent.setData(Uri.parse("package:" + getApplicationContext().getPackageName()));
            startActivityForResult(intent,1000);
        }
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode,int resultCode,@Nullable Intent data) {
        if (requestCode==1000){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                boolean value =Settings.System.canWrite(getApplicationContext());
                if (value){
                    success= true;
                }else{
                    Toast.makeText(this, "Permission not granted" , Toast.LENGTH_SHORT).show();
                }
            }


            }
    }
}