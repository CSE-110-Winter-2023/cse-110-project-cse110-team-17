package edu.ucsd.cse110.cse110_team17_project;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class LocationService implements LocationListener {
    private static LocationService instance;
    private Activity activity;

    private MutableLiveData<Pair<Double, Double>> locationValue;

    private final LocationManager locationManager;

    public static LocationService singleton(Activity activity) {
        if(instance ==null){
            instance = new LocationService(activity);
        }
        return instance;
    }
    protected LocationService(Activity activity){
        this.locationValue = new MutableLiveData<>();
        this.activity = activity;
        this.locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        this.registerLocationListener();
    }

    private void registerLocationListener(){
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            throw new IllegalStateException("App needs location permission to get latest location");
        }
        this.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }


    public void onLocationChanged(@NonNull Location location){
        this.locationValue.postValue(new Pair<Double, Double>(location.getLatitude(), location.getLongitude()));
    }

    private void unregisterLocationListener(){
        locationManager.removeUpdates(this);
    }
    public LiveData<Pair<Double, Double>> getLocation(){return this.locationValue;}

    public void setMockLocationSource(MutableLiveData<Pair<Double, Double>> mockDataSource) {
        System.out.println(this.locationValue.getValue());
        unregisterLocationListener();
        this.locationValue = mockDataSource;
        System.out.println(this.locationValue.getValue());
    }
}

