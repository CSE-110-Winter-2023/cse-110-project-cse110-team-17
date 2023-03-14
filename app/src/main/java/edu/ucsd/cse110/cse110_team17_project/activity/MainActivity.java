package edu.ucsd.cse110.cse110_team17_project.activity;

import static java.lang.String.valueOf;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
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
    private LocationService locationService;

    private LiveData<List<UserInfo>> userInfos; // Default 3 elements for now
    // defaults to San Diego (fix that later)
    private Pair<Double, Double> currentLocation = new Pair<>(32.715736, -117.161087);
    private UserInfo curUserInfo;
    MutableLiveData<Float> zoomSubject;

    List<PositionObject> relativePositions;
    private int screenWidth;
    private int zoomPosition = 1;

    public void onBackClicked(View view) {
        startUIDActicity();
    }

    private class PositionObject {
        int label_id;
        int radius;
        float angle;

        PositionObject(int label_id, int radius, float angle) {
            this.label_id = label_id;
            this.radius = radius;
            this.angle = angle;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);
        startUIDActicity();
    }


    @Override
    protected void onResume() {
        super.onResume();
        setLocationService();
        startOrientationSensor();
        setZoomObservations();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        var viewModel = setupViewModel();
        SharedPreferences preferences = getSharedPreferences("MAIN", MODE_PRIVATE);
        String label = preferences.getString("username", "DefaultUser");
        String uid = preferences.getString("myUID", "DefaultUID");
        // TODO: Change this after UID works
        curUserInfo = new UserInfo("17testUser1", label, "17testUser1");

        // TODO: Wierd Delay, Ask for assistance maybe?
        locationService.getLocation().observe(this, loc->{
            System.out.println(Double.toString(loc.first) + ", " + Double.toString(loc.second));
            curUserInfo.latitude = loc.first.doubleValue();
            curUserInfo.longitude = loc.second.doubleValue();
            currentLocation = loc;
        });
        viewModel.postUserInfo(curUserInfo);

        List<String> keys = new ArrayList<>();
        keys.add("group17test1");
        keys.add("group17test2");
        keys.add("group17test3");

        userInfos = viewModel.getUserInfos(keys);

        userInfos.observe(this, this::onUserInfoChanged);

//        Button back_btn = findViewById(R.id.back);
//        back_btn.setOnClickListener(this::startUIDActicity});
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop the motion sensor to not drain battery on the background
        orientationService.unregisterSensorListeners();
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

        relativePositions = new ArrayList<>();

        //TODO: Refactor!!
        setViewLocation(name_label1, userInfo1, R.id.label_1);
        setViewLocation(name_label2, userInfo2, R.id.label_2);
        setViewLocation(name_label3, userInfo3, R.id.label_3);

        ConstraintLayout.LayoutParams layoutParams1 =
                (ConstraintLayout.LayoutParams) name_label1.getLayoutParams();
        ConstraintLayout.LayoutParams layoutParams2 =
                (ConstraintLayout.LayoutParams) name_label2.getLayoutParams();

        Log.i("A", valueOf(layoutParams1.circleRadius));
        Log.i("A", valueOf(layoutParams2.circleRadius));
        Log.i("A", valueOf(layoutParams1.circleAngle));
        Log.i("A", valueOf(layoutParams2.circleAngle));
    }

    private CompassViewModel setupViewModel() {
        return new ViewModelProvider(this).get(CompassViewModel.class);
    }

    private void setLocationService() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        }
        locationService = LocationService.singleton(this);
    }

    private void setZoomObservations() {
        // here is for Zoom part, will be refact later
        zoomSubject = new MutableLiveData<>(Utilities.correctZoomRatio(zoomPosition));
        ImageView innerCircle1 = findViewById(R.id.inner_circle1);
        ImageView innerCircle2 = findViewById(R.id.inner_circle2);
        ImageView innerCircle3 = findViewById(R.id.inner_circle3);
        ImageView outerCircle = findViewById(R.id.circle_rim);
        Button zoomInBtn = findViewById(R.id.zoom_in);
        Button zoomOutBtn = findViewById(R.id.zoom_out);
        zoomInBtn.setOnClickListener(this::clickedOnZoomIn);
        zoomOutBtn.setOnClickListener(this::clickedOnZoomOut);


        zoomSubject.observe(this, (num)->{
            innerCircle1.setScaleX(num);
            innerCircle1.setScaleY(num);
            innerCircle2.setScaleX(num);
            innerCircle2.setScaleY(num);
            innerCircle3.setScaleX(num);
            innerCircle3.setScaleY(num);
            outerCircle.setScaleX(num);
            outerCircle.setScaleY(num);

//            ImageView circle1 = findViewById(R.id.circle_rim);
//            if (zoomPosition < 3){
//                circle1.setVisibility(View.INVISIBLE);
//            }else{
//                circle1.setVisibility(View.VISIBLE);
//            }



            try {
                onUserInfoChanged(userInfos.getValue());
            }catch (Exception e)
            {
                var a = 1;
            }
        });
    }



    // This method starts the orientation sensor
    private void startOrientationSensor() {
        orientationService = OrientationService.singleton(this);
        orientationService.registerSensorListeners();

        orientationService.getOrientation().observe(this, orientation -> {
            float actualAngle = -orientation / (float) Math.PI * 180;
            setAllLabelRotations(actualAngle);
        });



    }

    // This method sets the rotation angle to the angle provided
    public void setAllLabelRotations(float angle) {
        View rotateConstraint = (View) findViewById(R.id.rotateConstraint);
        rotateConstraint.setRotation(angle);


        TextView name_label1 = (TextView) findViewById(R.id.label_1);
        TextView name_label2 = (TextView) findViewById(R.id.label_2);
        TextView name_label3 = (TextView) findViewById(R.id.label_3);

        name_label1.setRotation(-angle);
        name_label2.setRotation(-angle);
        name_label3.setRotation(-angle);
    }

    private void setViewLocation(TextView label, UserInfo userInfo, int curLabelID) {
        screenWidth = this.getResources().getDisplayMetrics().widthPixels;
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) label.getLayoutParams();
        Context context = this;
        if (userInfo == null) return;

        layoutParams.constrainedWidth = false;
        layoutParams.matchConstraintMaxWidth = 1000;

        float angle = (float) Utilities.updateAngle(currentLocation.first.floatValue(), currentLocation.second.floatValue(),
                (float)userInfo.latitude, (float)userInfo.longitude);

        double distance = Utilities.distance(currentLocation.first.doubleValue(), currentLocation.second.doubleValue(),
                userInfo.latitude, userInfo.longitude);
        int radius = (int) (Utilities.distanceToViewRadius(distance) * zoomSubject.getValue());

        for (PositionObject position: relativePositions) {
            TextView collisionLabel = findViewById(position.label_id);
            ConstraintLayout.LayoutParams layoutParamsCollsion = (ConstraintLayout.LayoutParams)
                    collisionLabel.getLayoutParams();
            if (Math.abs(position.angle - angle) < 10) {
                if (position.radius - radius >= 0 && position.radius - radius < 100) {
                    layoutParamsCollsion.constrainedWidth = true;
                    layoutParamsCollsion.matchConstraintMaxWidth = 100;
                    collisionLabel.setLayoutParams(layoutParamsCollsion);
                    radius += 100;
                    Log.i("A", "This is called");
                }
                else if (radius - position.radius >= 0 && radius - position.radius < 100) {
                    layoutParamsCollsion.circleRadius += 100;
                    collisionLabel.setLayoutParams(layoutParamsCollsion);
                    layoutParams.constrainedWidth = true;
                    layoutParams.matchConstraintMaxWidth = 100;
                    Log.i("A", "This is called");
                }
                else {
                    Log.i("A", "This is NOT called");
                }
            }
        }

        relativePositions.add(new PositionObject(curLabelID, radius, angle));

        int maxRadius = screenWidth / 2;

        if (radius < maxRadius) {
            label.setText(userInfo.label);
            label.setTextSize(15.0F);
        }
        else {
            radius = maxRadius;
            label.setText("·");
            label.setTextSize(100.0F);
        }
        layoutParams.circleConstraint = R.id.status_dot;
        layoutParams.circleRadius = radius;
        layoutParams.circleAngle = angle;

        label.setLayoutParams(layoutParams);
    }

    private void clickedOnZoomOut(View view) {
        if (zoomPosition > 0) zoomPosition--;
        ImageView circle1 = findViewById(R.id.inner_circle1);
        if (zoomPosition > 0){
            circle1.setVisibility(View.INVISIBLE);
        }else{
            circle1.setVisibility(View.VISIBLE);
        }
        zoomSubject.postValue(Utilities.correctZoomRatio(zoomPosition));
    }

    private void clickedOnZoomIn(View view) {
        if (zoomPosition < 2) zoomPosition++;
        ImageView circle1 = findViewById(R.id.inner_circle1);
        if (zoomPosition > 0){
            circle1.setVisibility(View.INVISIBLE);
        }else{
            circle1.setVisibility(View.VISIBLE);
        }
        zoomSubject.postValue(Utilities.correctZoomRatio(zoomPosition));
    }

}