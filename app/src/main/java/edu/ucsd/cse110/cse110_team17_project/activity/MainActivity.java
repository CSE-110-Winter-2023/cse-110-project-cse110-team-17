package edu.ucsd.cse110.cse110_team17_project.activity;

import static java.lang.String.valueOf;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.cse110_team17_project.R;

import edu.ucsd.cse110.cse110_team17_project.Utilities;
import edu.ucsd.cse110.cse110_team17_project.model.UserInfo;
import edu.ucsd.cse110.cse110_team17_project.model.UserRepository;
import edu.ucsd.cse110.cse110_team17_project.services.LocationService;
import edu.ucsd.cse110.cse110_team17_project.services.OrientationService;
import edu.ucsd.cse110.cse110_team17_project.view.UserDisplayView;
import edu.ucsd.cse110.cse110_team17_project.viewmodel.Presenter;

public class MainActivity extends AppCompatActivity {

    private static final Integer INITIAL_ZOOM = 1;
    public OrientationService orientationService;
    private LocationService locationService;

    private LiveData<List<UserInfo>> userInfos; // Default 3 elements for now
    // defaults to San Diego (fix that later)
    public UserInfo curUserInfo;
    MutableLiveData<Integer> zoomSubject;
    Presenter pr;
    public boolean isMockLocationTesting = false;
    public UserRepository userrepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        SharedPreferences preferences = getSharedPreferences("MAIN", MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.clear();
//        editor.apply();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);
        userrepo = new UserRepository();
        userInfos = new MutableLiveData<>();
    }

    private void startEnterNameActivity() {
        Intent intent = new Intent(this, EnterNameActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        SharedPreferences preferences = getSharedPreferences("MAIN", MODE_PRIVATE);
        String label = preferences.getString("username", "");
        String uid = preferences.getString("myUID", "");
        // Check if all of them is empty, if yes, we have no input yet and need to go to InputActivity
        if (label.isEmpty()) {
            startEnterNameActivity();
        }

        zoomSubject = new MutableLiveData<>(preferences.getInt("zoomPosition", 1));
        // TODO: Change this after UID works
        curUserInfo = new UserInfo(uid, label, uid);
        userrepo.postLocalUserInfo(curUserInfo);

        setUpUser(userrepo);
        setUpPresenter();
        setLocationService();
        setOrientationSensor();
        setZoomObservations();
    }

    private void setUpUser(UserRepository us) {
        SharedPreferences preferences = getSharedPreferences("MAIN", MODE_PRIVATE);
        String friendListString = preferences.getString("friendListString", "");
        List<String> friendList = Utilities.parseFriendListString(friendListString);
        System.out.println("Friend List: " + friendList);
        userInfos = us.getRemoteUserInfo(friendList, isMockLocationTesting);
        userInfos.observe(this, infos -> {pr.infosUpdate(infos);});
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop the motion sensor to not drain battery on the background
        orientationService.unregisterSensorListeners();
    }

    private void startUIDActivity() {
        Intent intent = new Intent(this, UIDActivity.class);
        startActivity(intent);
    }

    private void setLocationService() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        }
        LocationService.singleton(this).getLocation().observe(this, loc->{
            System.out.println(Double.toString(loc.first) + ", " + Double.toString(loc.second));
            curUserInfo.latitude = loc.first.doubleValue();
            curUserInfo.longitude = loc.second.doubleValue();
            pr.updateLocation(loc);
        });

    }
    private void setUpPresenter(){
        List<ImageView> circles = new ArrayList<>();
        circles.add(findViewById(R.id.inner_circle1));
        circles.add(findViewById(R.id.inner_circle2));
        circles.add(findViewById(R.id.inner_circle3));
        pr = new Presenter(zoomSubject.getValue(), circles);
        new UserDisplayView(pr, findViewById(R.id.label_1));
        new UserDisplayView(pr, findViewById(R.id.label_2));
        new UserDisplayView(pr, findViewById(R.id.label_3));
        new UserDisplayView(pr, findViewById(R.id.label_4));
        new UserDisplayView(pr, findViewById(R.id.label_5));
        new UserDisplayView(pr, findViewById(R.id.label_6));
        new UserDisplayView(pr, findViewById(R.id.label_7));
    }
    
    private void setZoomObservations() {
        Button zoomInBtn = findViewById(R.id.zoom_in);
        Button zoomOutBtn = findViewById(R.id.zoom_out);
        zoomInBtn.setOnClickListener(this::clickedOnZoomIn);
        zoomOutBtn.setOnClickListener(this::clickedOnZoomOut);
        zoomSubject.observe(this, (num) -> {
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
        Intent addFriendsIntent = new Intent(this, AddFriendsActivity.class);
        startActivity(addFriendsIntent);
    }

    private void clickedOnZoomOut(View view) {
        int zoomNum = zoomSubject.getValue();
        if (zoomNum > 0){
            zoomSubject.postValue(--zoomNum);
        }
        putDefaultZoomPosition(zoomNum);
    }

    private void clickedOnZoomIn(View view) {
        int zoomNum = zoomSubject.getValue();
        if (zoomNum < 2){
            zoomSubject.postValue(++zoomNum);
        }

        putDefaultZoomPosition(zoomNum);
    }

    private void putDefaultZoomPosition(int num) {
        SharedPreferences preferences = getSharedPreferences("MAIN", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("zoomPosition", num);
        editor.apply();
    }

    public MutableLiveData<Integer> getZoomSubject(){
        return zoomSubject;
    }

    public Integer getZoomSize(){
        return zoomSubject.getValue();
    }

}