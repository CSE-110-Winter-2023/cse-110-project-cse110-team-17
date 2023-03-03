package edu.ucsd.cse110.cse110_team17_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    public OrientationService orientationService;
//    private boolean isSensorActivated = false;

    // Right now these are arbitrary values, when we got 3-1 done we should have specific values for these
    private float cord1Angle;
    private float cord2Angle;
    private float cord3Angle;
    private float compassNorthAngle = 0;

    private LocationService locationService;

    private Pair<Double, Double> coordinate1;
    private Pair<Double, Double> coordinate2;
    private Pair<Double, Double> coordinate3;

    // defaults to San Diego (fix that later)
    private Pair<Double, Double> currentLocation = new Pair<>(32.715736, -117.161087);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        }
        locationService = LocationService.singleton(this);

        locationService.getLocation().observe(this, loc->{
            System.out.println(Double.toString(loc.first) + ", " + Double.toString(loc.second));
            currentLocation = loc;
        });

        SharedPreferences preferences = getSharedPreferences("MAIN", MODE_PRIVATE);
        String label1 = preferences.getString("label1", "");
        String label2 = preferences.getString("label2", "");
        String label3 = preferences.getString("label3", "");
        coordinate1 = Utilities.validCoordinate(preferences.getString("coordinate1", ""));
        coordinate2 = Utilities.validCoordinate(preferences.getString("coordinate2", ""));
        coordinate3 = Utilities.validCoordinate(preferences.getString("coordinate3", ""));

        // Check if all of them is empty, if yes, we have no input yet and need to go to InputActivity
        if (label1.isEmpty() && label2.isEmpty() && label3.isEmpty()) {
            Intent inputIntent = new Intent(this, InputActivity.class);
            startActivity(inputIntent);
        }


        cord1Angle = (float) Utilities.updateAngle(currentLocation.first.floatValue(),
                 currentLocation.second.floatValue(), coordinate1.first.floatValue(), coordinate1.second.floatValue());
        cord2Angle = (float) Utilities.updateAngle(currentLocation.first.floatValue(),
                currentLocation.second.floatValue(), coordinate2.first.floatValue(), coordinate2.second.floatValue());
        cord3Angle = (float) Utilities.updateAngle(currentLocation.first.floatValue(),
                currentLocation.second.floatValue(), coordinate3.first.floatValue(), coordinate3.second.floatValue());

        //Initialize angles
        setAllLabelRotations((float) 0.0);

    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("resumed");

        // Get all the label names and coordinates from the SharedPreference object
        SharedPreferences preferences = getSharedPreferences("MAIN", MODE_PRIVATE);
        String label1 = preferences.getString("label1", "");
        String label2 = preferences.getString("label2", "");
        String label3 = preferences.getString("label3", "");
        coordinate1 = Utilities.validCoordinate(preferences.getString("coordinate1", ""));
        coordinate2 = Utilities.validCoordinate(preferences.getString("coordinate2", ""));
        coordinate3 = Utilities.validCoordinate(preferences.getString("coordinate3", ""));


        // Check if all of them is empty, if yes, we have no input yet and need to go to InputActivity
        if (label1.isEmpty() && label2.isEmpty() && label3.isEmpty()) {
            Intent inputIntent = new Intent(this, InputActivity.class);
            startActivity(inputIntent);
        }


        orientationService = OrientationService.singleton(this);
        startOrientationSensor(orientationService);

        // Set label texts to their saved names
        TextView name_label1 = (TextView) findViewById(R.id.label_1);
        TextView name_label2 = (TextView) findViewById(R.id.label_2);
        TextView name_label3 = (TextView) findViewById(R.id.label_3);
        name_label1.setText(label1);
        name_label2.setText(label2);
        name_label3.setText(label3);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop the motion sensor to not drain battery on the background
        orientationService.unregisterSensorListeners();
    }


    // This method is used when we want to get back to edit the coordinates and stuff
    public void onBackClicked(View view) {
        Intent inputIntent = new Intent(this, InputActivity.class);
        startActivity(inputIntent);
    }

    public void onClearClicked(View view) {
        SharedPreferences preferences = getSharedPreferences("MAIN", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
        Intent inputIntent = new Intent(this, InputActivity.class);
        startActivity(inputIntent);
    }


    // This method starts the orientation sensor
    private void startOrientationSensor(OrientationService orientationService) {
        // If sensor is already activated, no need to start again
//        if (!isSensorActivated) {
        orientationService.registerSensorListeners();
//            isSensorActivated = true;

        orientationService.getOrientation().observe(this, orientation -> {
            float actualAngle = -orientation / (float) Math.PI * 180;
            setAllLabelRotations(actualAngle);
        });


//        }
    }

    // If we want to resume sensors, we clicked on the "Resume sensors" Button and start the orientation again
    public void onResumeSensorsClicked(View view) {
        orientationService = new OrientationService(this);
        startOrientationSensor(orientationService);
    }

    // This method sets the rotation angle to the angle provided
    public void setAllLabelRotations(float angle) {
        TextView name_label1 = (TextView) findViewById(R.id.label_1);
        TextView name_label2 = (TextView) findViewById(R.id.label_2);
        TextView name_label3 = (TextView) findViewById(R.id.label_3);
        ConstraintLayout.LayoutParams layoutParams1 = (ConstraintLayout.LayoutParams) name_label1.getLayoutParams();
        ConstraintLayout.LayoutParams layoutParams2 = (ConstraintLayout.LayoutParams) name_label2.getLayoutParams();
        ConstraintLayout.LayoutParams layoutParams3 = (ConstraintLayout.LayoutParams) name_label3.getLayoutParams();



        if (coordinate1 != null) {
           float coordinate1Angle = (float) Utilities.updateAngle(currentLocation.first.floatValue(), currentLocation.second.floatValue(), coordinate1.first.floatValue(), coordinate1.second.floatValue());
            layoutParams1.circleAngle = angle + coordinate1Angle;
            name_label1.setLayoutParams(layoutParams1);
        }
        if (coordinate2 != null) {
            float coordinate2Angle = (float) Utilities.updateAngle(currentLocation.first.floatValue(), currentLocation.second.floatValue(), coordinate2.first.floatValue(), coordinate2.second.floatValue());
            layoutParams2.circleAngle = angle + coordinate2Angle;
            name_label2.setLayoutParams(layoutParams2);
        }
        if (coordinate3 != null) {
            float coordinate3Angle = (float) Utilities.updateAngle(currentLocation.first.floatValue(), currentLocation.second.floatValue(), coordinate3.first.floatValue(), coordinate3.second.floatValue());
            layoutParams3.circleAngle = angle + coordinate3Angle;
            name_label3.setLayoutParams(layoutParams3);
        }
    }
}