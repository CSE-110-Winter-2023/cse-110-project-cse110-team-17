package edu.ucsd.cse110.cse110_team17_project.services;

import static androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import edu.ucsd.cse110.cse110_team17_project.R;

public class LocationService implements LocationListener {
    private static LocationService instance;
    private Activity activity;

    private MutableLiveData<Pair<Double, Double>> locationValue;

    private final LocationManager locationManager;

    private int count;

    public static LocationService singleton(Activity activity){
        if(instance == null){
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
        if(ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            throw new IllegalStateException("App needs location permission to get latest location");
        }
        this.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        this.locationValue.postValue(new Pair<Double, Double>(location.getLatitude(), location.getLongitude()));
    }

    private Future<Void> future;
    private ExecutorService backGroundThreadExecutor = Executors.newSingleThreadExecutor();
    @Override
    public void onProviderDisabled(@NonNull String provider){
        //future.cancel(true);
        ImageView green = activity.findViewById(R.id.green_dot);
        ImageView red = activity.findViewById(R.id.red_dot);
        TextView timeout = activity.findViewById(R.id.timeout);
        this.future = backGroundThreadExecutor.submit(() ->{
            do{
                count++;
                Thread.sleep(1000);
                if (count < 3600 && count >= 60){
                    activity.runOnUiThread(() ->{
                        timeout.setText(count / 60 + "m");
                        green.setVisibility(View.INVISIBLE);
                        red.setVisibility(View.VISIBLE);
                    });
                } else if (count >= 3600) {
                    activity.runOnUiThread(() ->{
                        timeout.setText("1h+");
                    });
                }

            }while(count > 0);
            return null;
        });

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        future.cancel(true);
        ImageView green = activity.findViewById(R.id.green_dot);
        ImageView red = activity.findViewById(R.id.red_dot);
        TextView timeout = activity.findViewById(R.id.timeout);
        count = 0;
        this.future = backGroundThreadExecutor.submit(() ->{
            do{
                count++;
                Thread.sleep(1000);
            }while(count < 60);
            activity.runOnUiThread(() ->{
                timeout.setText("");
                green.setVisibility(View.VISIBLE);
                red.setVisibility(View.INVISIBLE);
            });
            return null;
        });
    }

    private void unregisterLocationListener() {locationManager.removeUpdates(this);}

    public LiveData<Pair<Double, Double>> getLocation() {return this.locationValue;}

    public void setMockOrientationSource(MutableLiveData<Pair<Double, Double>> mockDataSource){
        unregisterLocationListener();
        this.locationValue = mockDataSource;
    }

}
