package edu.ucsd.cse110.cse110_team17_project.viewmodel;

import android.app.Application;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class CompassViewModel extends AndroidViewModel {

    LiveData<Pair<Double, Double>> coordinate;

    public CompassViewModel(@NonNull Application application) {
        super(application);

    }

    public LiveData<Pair<Double, Double>> getCoordinate() {
        return null;
    }
}
