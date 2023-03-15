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

import org.w3c.dom.Text;

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
    private int zoomPosition;
    private final float[] zoomScaleValues = {1F, 1.5F, 3F};
    private int[] labelIDs;

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
        labelIDs = new int[]{R.id.label_1, R.id.label_2, R.id.label_3, R.id.label_4, R.id.label_5};
        startUIDActicity();
    }


    @Override
    protected void onResume() {
        super.onResume();
        setLocationService();
        startOrientationSensor();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        var viewModel = setupViewModel();
        SharedPreferences preferences = getSharedPreferences("MAIN", MODE_PRIVATE);
        String label = preferences.getString("username", "DefaultUser");
        String uid = preferences.getString("myUID", "DefaultUID");
        zoomPosition = preferences.getInt("zoomPosition", 1);
        // TODO: Change this after UID works
        curUserInfo = new UserInfo("17testUser1", label, "17testUser1");
        setZoomObservations();

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
        relativePositions = new ArrayList<>();

        for (int index = 0; index < labelIDs.length; index++) {
            if (index < userInfos.size()) {
                var userInfo = userInfos.get(index);
                setViewLocation(userInfo, labelIDs[index]);
            }
            else {
                setViewInvisible(labelIDs[index]);
            }

        }

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
        zoomSubject = new MutableLiveData<>(zoomScaleValues[zoomPosition]);
        ImageView innerCircle1 = findViewById(R.id.inner_circle1);
        ImageView innerCircle2 = findViewById(R.id.inner_circle2);
        ImageView innerCircle3 = findViewById(R.id.inner_circle3);
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

    private void setViewLocation(UserInfo userInfo, int curLabelID) {
        TextView label = this.findViewById(curLabelID);
        screenWidth = this.getResources().getDisplayMetrics().widthPixels;
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) label.getLayoutParams();
        if (userInfo == null) return;
        layoutParams.constrainedWidth = false;
        layoutParams.matchConstraintMaxWidth = 1000;

        float angle = (float) Utilities.updateAngle(currentLocation.first.floatValue(), currentLocation.second.floatValue(),
                (float)userInfo.latitude, (float)userInfo.longitude);

        double distance = Utilities.distance(currentLocation.first.doubleValue(), currentLocation.second.doubleValue(),
                userInfo.latitude, userInfo.longitude);
        int radius = (int) (Utilities.distanceToViewRadius(distance) * zoomSubject.getValue());

        radius = caculateCollisions(curLabelID, layoutParams, angle, radius);

        int maxRadius = screenWidth / 2 - 25;

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

    private void setViewInvisible(int labelID) {
        View view = findViewById(labelID);
        view.setVisibility(View.INVISIBLE);
    }

    private int caculateCollisions(int curLabelID, ConstraintLayout.LayoutParams layoutParams, float angle, int radius) {
        for (PositionObject position: relativePositions) {
            TextView collisionLabel = findViewById(position.label_id);
            ConstraintLayout.LayoutParams layoutParamsCollsion = (ConstraintLayout.LayoutParams)
                    collisionLabel.getLayoutParams();
            if (Math.abs(position.angle - angle) < 10) {
                if (Math.abs(position.radius - radius)  < 50) {
                    layoutParamsCollsion.constrainedWidth = true;
                    layoutParamsCollsion.matchConstraintMaxWidth = 80;
                    collisionLabel.setLayoutParams(layoutParamsCollsion);
                    radius += 80;
                    layoutParams.constrainedWidth = true;
                    layoutParams.matchConstraintMaxWidth = 80;
                }

            }
        }

        relativePositions.add(new PositionObject(curLabelID, radius, angle));
        return radius;
    }

    private void clickedOnZoomOut(View view) {
        if (zoomPosition > 0) zoomPosition--;
        zoomSubject.postValue(zoomScaleValues[zoomPosition]);

        putDefaultZoomPosition();
    }

    private void clickedOnZoomIn(View view) {
        if (zoomPosition < 2) zoomPosition++;
        zoomSubject.postValue(zoomScaleValues[zoomPosition]);

        putDefaultZoomPosition();
    }

    private void putDefaultZoomPosition() {
        SharedPreferences preferences = getSharedPreferences("MAIN", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("zoomPosition", zoomPosition);
        editor.apply();
    }
}