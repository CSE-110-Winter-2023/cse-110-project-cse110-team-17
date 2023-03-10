package edu.ucsd.cse110.cse110_team17_project.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.cse110_team17_project.R;

import edu.ucsd.cse110.cse110_team17_project.Utilities;
import edu.ucsd.cse110.cse110_team17_project.model.UserInfo;
import edu.ucsd.cse110.cse110_team17_project.services.LocationService;
import edu.ucsd.cse110.cse110_team17_project.services.OrientationService;
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

    private String label;
    private String uid;

    MutableLiveData<Float> zoomSubject;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);
        startUIDActicity();

        // here is for Zoom part, will be refact later
        zoomSubject = new MutableLiveData<>(1F);
        ImageView innerCircle = findViewById(R.id.inner_circle1);
        ImageView outerCircle = findViewById(R.id.circle_rim);
        View Constra = findViewById(R.id.rotateConstra);
        Button zoomInBtn = findViewById(R.id.zoom_in);
        Button zoomOutBtn = findViewById(R.id.zoom_out);
        zoomInBtn.setOnClickListener(this::clickedOnZoomIn);
        zoomOutBtn.setOnClickListener(this::clickedOnZoomOut);
        zoomSubject.observe(this, (num)->{
            innerCircle.setScaleX(num);
            innerCircle.setScaleY(num);
            outerCircle.setScaleX(num);
            outerCircle.setScaleY(num);
        });





        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        }
        locationService = LocationService.singleton(this);

        SharedPreferences preferences = getSharedPreferences("MAIN", MODE_PRIVATE);
        label = preferences.getString("username", "DefaultUser");
        uid = preferences.getString("myUID", "aaaaa");



        curUserInfo = new UserInfo("17testUser1", label, "17testUser1");

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
    }

    private void startUIDActicity() {
        Intent intent = new Intent(this, UIDActivity.class);
        startActivity(intent);
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
        orientationService = OrientationService.singleton(this);
        startOrientationSensor(orientationService);
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




    // This method starts the orientation sensor
    private void startOrientationSensor(OrientationService orientationService) {
        orientationService.registerSensorListeners();

        orientationService.getOrientation().observe(this, orientation -> {
            float actualAngle = -orientation / (float) Math.PI * 180;
            setAllLabelRotations(actualAngle);
        });



    }

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
    }

    private void setViewLocation(TextView label, UserInfo userInfo) {
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) label.getLayoutParams();
        Context context = this;
        if (userInfo == null) return;

        float angle = (float) Utilities.updateAngle(currentLocation.first.floatValue(), currentLocation.second.floatValue(),
                (float)userInfo.latitude, (float)userInfo.longitude);

        double distance = Utilities.distance(currentLocation.first.doubleValue(), currentLocation.second.doubleValue(),
                userInfo.latitude, userInfo.longitude);
        int radius = (int) (Utilities.distanceToViewRadius(distance) * zoomSubject.getValue());




        if (radius > 450) {
            label.setText(userInfo.label);
            label.setTextSize(15.0F);
            Log.i("ALERT", "No dot is called");
            radius = 450;
        }
        else {
            label.setText("·");
            label.setTextSize(100.0F);
            Log.i("ALERT", String.valueOf(radius));
        }
        layoutParams.circleConstraint = R.id.status_dot;
        layoutParams.circleRadius = radius;
        layoutParams.circleAngle = angle;

        label.setLayoutParams(layoutParams);
    }

    private void clickedOnZoomOut(View view) {
        zoomSubject.postValue(zoomSubject.getValue() * 1.1F);
    }

    private void clickedOnZoomIn(View view) {
        zoomSubject.postValue(zoomSubject.getValue() / 1.1F);
    }

}