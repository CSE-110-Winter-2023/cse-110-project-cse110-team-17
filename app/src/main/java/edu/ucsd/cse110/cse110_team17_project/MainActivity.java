package edu.ucsd.cse110.cse110_team17_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    public OrientationService orientationService;
    private boolean isSensorActivated = false;

    // Right now these are arbitrary values, when we got 3-1 done we should have specific values for these
    private float cord1Angle = 45;
    private float cord2Angle = 90;
    private float cord3Angle = 180;
    private float compassNorthAngle = 45;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    protected void onResume(){
        super.onResume();
        // Get all the label names and coordinates from the SharedPreference object
        SharedPreferences preferences = getSharedPreferences("MAIN", MODE_PRIVATE);
        String label1 = preferences.getString("label1", "");
        String label2 = preferences.getString("label2", "");
        String label3 = preferences.getString("label3", "");
        String coordinate1 = preferences.getString("coordinate1", "");
        String coordinate2 = preferences.getString("coordinate2", "");
        String coordinate3 = preferences.getString("coordinate3", "");

        // Check if all of them is empty, if yes, we have no input yet and need to go to InputActivity
        if(label1.isEmpty() && label2.isEmpty() && label3.isEmpty()){
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
    protected void onPause(){
        super.onPause();
        // Stop the motion sensor to not drain battery on the background
        orientationService.unregisterSensorListeners();
    }

    // This method is used when we want to get back to edit the coordinates and stuff
    public void onBackClicked(View view) {
        Intent inputIntent = new Intent(this, InputActivity.class);
        startActivity(inputIntent);
    }

    // This method is for mock orientation testing
    public void onAngleConfirmClicked(View view) {
        // Stop the sensors first
        orientationService.unregisterSensorListeners();
        isSensorActivated = false;

        TextView AngleInput = (TextView) findViewById(R.id.input_angle);
        Integer angle = Integer.parseInt(AngleInput.getText().toString());

        setAllLabelRotations(angle);
    }

    // This method starts the orientation sensor
    private void startOrientationSensor(OrientationService orientationService) {

        // If sensor is already activated, no need to start again
        if (!isSensorActivated) {
            orientationService.registerSensorListeners();
            isSensorActivated = true;

            orientationService.getOrientation().observe(this, orientation -> {
                float actualAngle = -orientation / (float) Math.PI * 180;
                setAllLabelRotations(actualAngle);
            });


        }
    }

    // If we want to resume sensors, we clicked on the "Resume sensors" Button and start the orientation again
    public void onResumeSensorsClicked(View view) {
//        orientationService = new OrientationService(this);
        startOrientationSensor(orientationService);
    }

    // This method sets the rotation angle to the angle provided
    public void setAllLabelRotations(float angle) {
        TextView name_label1 = (TextView) findViewById(R.id.label_1);
        TextView name_label2 = (TextView) findViewById(R.id.label_2);
        TextView name_label3 = (TextView) findViewById(R.id.label_3);
        ImageView compass = (ImageView) findViewById(R.id.compass_face);
        ConstraintLayout.LayoutParams layoutParams1 = (ConstraintLayout.LayoutParams) name_label1.getLayoutParams();
        ConstraintLayout.LayoutParams layoutParams2 = (ConstraintLayout.LayoutParams) name_label2.getLayoutParams();
        ConstraintLayout.LayoutParams layoutParams3 = (ConstraintLayout.LayoutParams) name_label3.getLayoutParams();

        compass.setRotation(angle + compassNorthAngle);

        name_label1.setRotation(angle + cord1Angle);
        name_label2.setRotation(angle + cord2Angle);
        name_label3.setRotation(angle + cord3Angle);

        layoutParams1.circleAngle = angle + cord1Angle;
        layoutParams2.circleAngle = angle + cord2Angle;
        layoutParams3.circleAngle = angle + cord3Angle;
        name_label1.setLayoutParams(layoutParams1);
        name_label2.setLayoutParams(layoutParams2);
        name_label3.setLayoutParams(layoutParams3);


    }
}