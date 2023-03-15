package edu.ucsd.cse110.cse110_team17_project.viewmodel;

import android.util.Pair;

public interface locationObserver {
    void updateLocation(Pair<Double, Double> currentLocation);
}
