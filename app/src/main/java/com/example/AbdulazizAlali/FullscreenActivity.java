package com.example.AbdulazizAlali;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;



import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * Created by ssaurel on 02/12/2016.
 */
public class FullscreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);
        askForPermission(Manifest.permission.ACCESS_FINE_LOCATION,0);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

                 if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        startApp();

                    }else {
                        askForPermission(Manifest.permission.ACCESS_FINE_LOCATION, 0);
                    }
            }



        public void startApp(){
            Thread timer=new Thread()
            {
                public void run() {
                    try {
                        sleep(4000);
                        Intent i=new Intent(FullscreenActivity.this,Main2Activity.class);
                        startActivity(i);
                        finish();
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            };
            timer.start();
        }

    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
            }
        }
        else startApp();
    }
}