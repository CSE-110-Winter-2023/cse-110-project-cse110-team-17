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
import edu.ucsd.cse110.cse110_team17_project.view.UserDisplayView;
import edu.ucsd.cse110.cse110_team17_project.viewmodel.CompassViewModel;
import edu.ucsd.cse110.cse110_team17_project.viewmodel.Presenter;

public class MainActivity extends AppCompatActivity {

    private static final Integer INITIAL_ZOOM = 1;
    public OrientationService orientationService;
    private LocationService locationService;

    private LiveData<List<UserInfo>> userInfos; // Default 3 elements for now
    // defaults to San Diego (fix that later)
    private UserInfo curUserInfo;
    MutableLiveData<Integer> zoomSubject;
    Presenter pr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);
        startUIDActicity();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        SharedPreferences preferences = getSharedPreferences("MAIN", MODE_PRIVATE);
        String label = preferences.getString("username", "DefaultUser");
        String uid = preferences.getString("myUID", "DefaultUID");
        zoomSubject = new MutableLiveData<>(preferences.getInt("zoomPosition", 1));
        // TODO: Change this after UID works
        curUserInfo = new UserInfo("17testUser1", label, "17testUser1");
        var viewModel = new ViewModelProvider(this).get(CompassViewModel.class);
        viewModel.postUserInfo(curUserInfo);


        setUpPresenter();
        setOrientationSensor();
        setLocationService();
        setZoomObservations();
        setUpUser(viewModel);
        // TODO: Wierd Delay, Ask for assistance maybe?
    }

    private void setUpUser(CompassViewModel viewModel) {
        List<String> keys = new ArrayList<>();
        keys.add("group17test1");
        keys.add("group17test2");
        keys.add("group17test3");
        userInfos = viewModel.getUserInfos(keys);
        userInfos.observe(this, infos -> {pr.ImageViewUpdate(infos);});
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


    private void setLocationService() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        }
        locationService = LocationService.singleton(this);

    }
    private void setUpPresenter(){
        List<ImageView> circles = new ArrayList<>();
        circles.add(findViewById(R.id.inner_circle1));
        circles.add(findViewById(R.id.inner_circle2));
        circles.add(findViewById(R.id.inner_circle3));
        pr = new Presenter(getApplicationContext(), zoomSubject.getValue(), circles);
        new UserDisplayView(pr, findViewById(R.id.label_1));
        new UserDisplayView(pr, findViewById(R.id.label_2));
        new UserDisplayView(pr, findViewById(R.id.label_3));
        new UserDisplayView(pr, findViewById(R.id.label_4));
        new UserDisplayView(pr, findViewById(R.id.label_5));
    }
    
    private void setZoomObservations() {
        Button zoomInBtn = findViewById(R.id.zoom_in);
        Button zoomOutBtn = findViewById(R.id.zoom_out);
        zoomInBtn.setOnClickListener(this::clickedOnZoomIn);
        zoomOutBtn.setOnClickListener(this::clickedOnZoomOut);
        zoomSubject.observe(this, (num)->{
            pr.zoomUpdate(num);
        });
    }

    // This method starts the orientation sensor
    private void setOrientationSensor() {
        orientationService = OrientationService.singleton(this);
        orientationService.registerSensorListeners();
        orientationService.getOrientation().observe(this, orientation -> {
            pr.updateRotation(-orientation / (float) Math.PI * 180);
        });
    }

    public void onBackClicked(View view) {
        startUIDActicity();
    }
    private void clickedOnZoomOut(View view) {
        if (zoomSubject.getValue() > 0){
            zoomSubject.postValue(zoomSubject.getValue() - 1);
        }
        locationService.getLocation().observe(this, loc->{
            System.out.println(Double.toString(loc.first) + ", " + Double.toString(loc.second));
            curUserInfo.latitude = loc.first.doubleValue();
            curUserInfo.longitude = loc.second.doubleValue();
            pr.updateLocation(loc);
        });
        putDefaultZoomPosition();
    }

    private void clickedOnZoomIn(View view) {
        if (zoomSubject.getValue() < 2){
            zoomSubject.postValue(zoomSubject.getValue() + 1);
        }

        putDefaultZoomPosition();
    }

    private void putDefaultZoomPosition() {
        SharedPreferences preferences = getSharedPreferences("MAIN", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("zoomPosition", zoomSubject.getValue());
        editor.apply();
    }
}