package edu.ucsd.cse110.cse110_team17_project.viewmodel;

import android.app.Application;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import edu.ucsd.cse110.cse110_team17_project.model.UserInfo;
import edu.ucsd.cse110.cse110_team17_project.model.UserRepository;

public class CompassViewModel extends AndroidViewModel {

    private LiveData<Pair<Double, Double>> coordinate;

    private UserRepository userRepository;

    public CompassViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository();
    }

    public LiveData<Pair<Double, Double>> getCoordinate(String privateCode) {
        if (coordinate == null) {
            coordinate = userRepository.getRemoteUserInfo(privateCode);
        }

        return coordinate;
    }
}
