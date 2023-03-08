package edu.ucsd.cse110.cse110_team17_project.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.cse110_team17_project.model.LocationService;
import edu.ucsd.cse110.cse110_team17_project.model.OrientationService;
import edu.ucsd.cse110.cse110_team17_project.R;
import edu.ucsd.cse110.cse110_team17_project.model.UserInfo;
import edu.ucsd.cse110.cse110_team17_project.model.Utilities;
import edu.ucsd.cse110.cse110_team17_project.viewmodel.CompassViewModel;

public class MainActivity extends AppCompatActivity {

    public OrientationService orientationService;
//    private boolean isSensorActivated = false;

    private float compassNorthAngle = 0;

    private LocationService locationService;

    private LiveData<List<UserInfo>> userInfos; // Default 3 elements for now

    // defaults to San Diego (fix that later)
    private Pair<Double, Double> currentLocation = new Pair<>(32.715736, -117.161087);
    private UserInfo curUserInfo;

    private String label = "usertest";



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

        curUserInfo = new UserInfo(label, label, label);

        // TODO: Wierd Delay, Ask for assistance maybe?
        locationService.getLocation().observe(this, loc->{
            System.out.println(Double.toString(loc.first) + ", " + Double.toString(loc.second));
            curUserInfo.latitude = loc.first.doubleValue();
            curUserInfo.longitude = loc.second.doubleValue();
            currentLocation = loc;
        });


        var viewModel = setupViewModel();
        List<String> keys = new ArrayList<>();
        keys.add("group17test1");
        keys.add("group17test2");
        keys.add("group17test3");

        userInfos = viewModel.getUserInfos(keys);
        viewModel.postUserInfo(curUserInfo);

        userInfos.observe(this, this::onUserInfoChanged);
//
//        SharedPreferences preferences = getSharedPreferences("MAIN", MODE_PRIVATE);
//        String label1 = preferences.getString("label1", "");
//        String label2 = preferences.getString("label2", "");
//        String label3 = preferences.getString("label3", "");
//        coordinate1 = Utilities.validCoordinate(preferences.getString("coordinate1", ""));
//        coordinate2 = Utilities.validCoordinate(preferences.getString("coordinate2", ""));
//        coordinate3 = Utilities.validCoordinate(preferences.getString("coordinate3", ""));



        // Check if all of them is empty, if yes, we have no input yet and need to go to InputActivity
//        if (label1.isEmpty() && label2.isEmpty() && label3.isEmpty()) {
//            Intent inputIntent = new Intent(this, InputActivity.class);
//            startActivity(inputIntent);
//        }

        //Initialize angles
//        setAllLabelRotations((float) 0.0);

    }

    private void onUserInfoChanged(List<UserInfo> userInfos) {
        var userInfo1 = userInfos.get(0);
        TextView name_label1 = (TextView) findViewById(R.id.label_1);
        var userInfo2 = userInfos.get(1);
        TextView name_label2 = (TextView) findViewById(R.id.label_2);
        var userInfo3 = userInfos.get(2);
        TextView name_label3 = (TextView) findViewById(R.id.label_3);

        setViewLocation(name_label1, userInfo1);
        setViewLocation(name_label2, userInfo2);
        setViewLocation(name_label3, userInfo3);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Get all the label names and coordinates from the SharedPreference object
//        SharedPreferences preferences = getSharedPreferences("MAIN", MODE_PRIVATE);

//        coordinate2 = Utilities.validCoordinate(preferences.getString("coordinate2", ""));
//        coordinate3 = Utilities.validCoordinate(preferences.getString("coordinate3", ""));


        // Check if all of them is empty, if yes, we have no input yet and need to go to InputActivity
//        if (label1.isEmpty() && label2.isEmpty() && label3.isEmpty()) {
//            Intent inputIntent = new Intent(this, InputActivity.class);
//            startActivity(inputIntent);
//        }


        orientationService = OrientationService.singleton(this);
        startOrientationSensor(orientationService);

        // Set label texts to their saved names
//        TextView name_label1 = (TextView) findViewById(R.id.label_1);
//        TextView name_label2 = (TextView) findViewById(R.id.label_2);
//        TextView name_label3 = (TextView) findViewById(R.id.label_3);
//        name_label1.setText(label1);
//        name_label2.setText(label2);
//        name_label3.setText(label3);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop the motion sensor to not drain battery on the background
        orientationService.unregisterSensorListeners();
    }

    private CompassViewModel setupViewModel() {
        return new ViewModelProvider(this).get(CompassViewModel.class);
    }

    // This method is used when we want to get back to edit the coordinates and stuff
    public void onBackClicked(View view) {
        Intent inputIntent = new Intent(this, InputActivity.class);
        startActivity(inputIntent);
    }

//    public void onClearClicked(View view) {
//        SharedPreferences preferences = getSharedPreferences("MAIN", MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.clear();
//        editor.apply();
//        Intent inputIntent = new Intent(this, InputActivity.class);
//        startActivity(inputIntent);
//    }


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
//    public void onResumeSensorsClicked(View view) {
//        orientationService = new OrientationService(this);
//        startOrientationSensor(orientationService);
//    }

    // This method sets the rotation angle to the angle provided
    public void setAllLabelRotations(float angle) {
        View rotateConstraint = (View) findViewById(R.id.rotateConstra);
        rotateConstraint.setRotation(angle);


        TextView name_label1 = (TextView) findViewById(R.id.label_1);
        TextView name_label2 = (TextView) findViewById(R.id.label_2);
        TextView name_label3 = (TextView) findViewById(R.id.label_3);

        name_label1.setRotation(-angle);
        name_label2.setRotation(-angle);
        name_label3.setRotation(-angle);
//        ConstraintLayout.LayoutParams layoutParams1 = (ConstraintLayout.LayoutParams) name_label1.getLayoutParams();
//        ConstraintLayout.LayoutParams layoutParams2 = (ConstraintLayout.LayoutParams) name_label2.getLayoutParams();
//        ConstraintLayout.LayoutParams layoutParams3 = (ConstraintLayout.LayoutParams) name_label3.getLayoutParams();
//
//
//
//        if (coordinate1 != null) {
//           float coordinate1Angle = (float) Utilities.updateAngle(currentLocation.first.floatValue(), currentLocation.second.floatValue(),
//                   coordinate1.getValue().first.floatValue(), coordinate1.getValue().second.floatValue());
//            layoutParams1.circleAngle = angle + coordinate1Angle;
//            name_label1.setLayoutParams(layoutParams1);
//        }
//        if (coordinate2 != null) {
//            float coordinate2Angle = (float) Utilities.updateAngle(currentLocation.first.floatValue(), currentLocation.second.floatValue(), coordinate2.first.floatValue(), coordinate2.second.floatValue());
//            layoutParams2.circleAngle = angle + coordinate2Angle;
//            name_label2.setLayoutParams(layoutParams2);
//        }
//        if (coordinate3 != null) {
//            float coordinate3Angle = (float) Utilities.updateAngle(currentLocation.first.floatValue(), currentLocation.second.floatValue(), coordinate3.first.floatValue(), coordinate3.second.floatValue());
//            layoutParams3.circleAngle = angle + coordinate3Angle;
//            name_label3.setLayoutParams(layoutParams3);
//        }
    }

    private void setViewLocation(TextView label, UserInfo userInfo) {
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) label.getLayoutParams();

        float angle = (float) Utilities.updateAngle(currentLocation.first.floatValue(), currentLocation.second.floatValue(),
                (float)userInfo.latitude, (float)userInfo.longitude);

        double distance = Utilities.distance(currentLocation.first.doubleValue(), currentLocation.second.doubleValue(),
                userInfo.latitude, userInfo.longitude);
        int radius = (int) Utilities.distanceToViewRadius(distance);

        layoutParams.circleRadius = radius;
        layoutParams.circleAngle = angle;

        label.setLayoutParams(layoutParams);

        if (radius < 450) {
            label.setText(userInfo.label);
            label.setTextSize(15.0F);
            Log.i("ALERT", "No dot is called");
        }
        else {
            label.setText(".");
            label.setTextSize(100.0F);
            Log.i("ALERT", String.valueOf(radius));
        }
    }

}