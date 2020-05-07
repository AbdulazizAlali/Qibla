package com.example.AbdulazizAlali;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.Manifest;
import android.widget.Toast;

public class Main2Activity extends Activity implements SensorEventListener, LocationListener {

    // define the display assembly compass picture
    private RelativeLayout image;
    private  int TEST_MAG;
    private RelativeLayout noGps;
    private ImageView combassbackground;
    // record the compass picture angle turned
    private float currentDegree = 0f;
    private float currentNorthDegree = 0f;
    private Location location = new Location("A");
    private Location target = new Location("B");
    // device sensor manager
    private SensorManager mSensorManager;
    protected LocationManager locationManager;
    TextView accuracy;
    TextView tvHeading;
    ImageView kaaba;

    private Sensor mySensorEventListener ;
    private Sensor SensorEventListener ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // our compass image
        image = findViewById(R.id.compass);
        kaaba = findViewById(R.id.kaaba);
        combassbackground = findViewById(R.id.widget37);
        noGps = findViewById(R.id.no_gps);
        accuracy = findViewById(R.id.accuracy);

        target.setLatitude(21.422487);
        target.setLongitude(39.826206);

        location.setAltitude(24.774265);
        location.setLongitude(46.738586);

        // TextView that will tell the user what degree is he heading
        tvHeading = (TextView) findViewById(R.id.textview1);

        // initialize your android device sensor capabilities
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mySensorEventListener = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // for the system's orientation sensor registered listeners
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // to stop the listener and save battery
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
          int magnetic_field_strength = (int)Math.sqrt((event.values[0]*event.values[0])+(event.values[1]*event.values[1])+(event.values[2]*event.values[2]));

            if(magnetic_field_strength<30){

                this.accuracy.setText(getString(R.string.accuracy )+(magnetic_field_strength/2)*2 + "μT\n"+getString(R.string.bad ));
                this.accuracy.setTextColor(getColor(R.color.nonaccurate));
            }
            else if(magnetic_field_strength<70){
                this.accuracy.setText(getString(R.string.accuracy )+(magnetic_field_strength/2)*2 + "μT\n"+getString(R.string.good));
                this.accuracy.setTextColor(getColor(R.color.colorPrimaryDark));
            }
            else{
                this.accuracy.setText(getString(R.string.accuracy )+(magnetic_field_strength/2)*2 + "μT\n"+getString(R.string.bad ));
                this.accuracy.setTextColor(getColor(R.color.nonaccurate));
            }

        } else {


            // get the angle around the z-axis rotated
            float degree = Math.round(event.values[0]);
            float degreenorth = Math.round(event.values[0]);


            // create a rotation animation (reverse turn degree degrees)


            if (degree > -6 && degree < 6) {
                kaaba.setImageResource(R.drawable.kaabacolors);
            } else {
                kaaba.setImageResource(R.drawable.kaabamono);
            }
            tvHeading.setText("Heading: " + Float.toString(degree) + " degrees");

            float bearing = location.bearingTo(target);

            degree = (bearing - degree) * -1;

            degree = normalizeDegree(degree);


            tvHeading.setText((int) degree + "");

            // create a rotation animation (reverse turn degree degrees)
            RotateAnimation findQibla = new RotateAnimation(
                    currentDegree,
                    -degree,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF,
                    0.5f);

            RotateAnimation findNorth = new RotateAnimation(
                    currentNorthDegree,
                    -(degreenorth),
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF,
                    0.5f);

            // how long the animation will take place
            findQibla.setDuration(210);

            // set the animation after the end of the reservation status
            findQibla.setFillAfter(true);
            findNorth.setDuration(210);
            // set the animation after the end of the reservation status
            findNorth.setFillAfter(true);

            // Start the animation
            image.startAnimation(findQibla);
            combassbackground.startAnimation(findNorth);
            if (degree > -6 && degree < 6) {
                kaaba.setImageResource(R.drawable.kaabacolors);
            } else {
                kaaba.setImageResource(R.drawable.kaabamono);
            }
            currentDegree = -degree;
            currentNorthDegree = -degreenorth;
        }
    }

    private float normalizeDegree(float value) {
        if (value >= -180.0 && value <= 180.0) {
            return value;
        } else if(value>180)
            return value-360;
        else return value+360;
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // not in use

    }

    @Override
    public void onLocationChanged(Location location) {
        noGps.setVisibility(View.INVISIBLE);
        this.location.setLatitude(location.getLatitude());
        this.location.setLongitude(location.getLongitude());

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


}