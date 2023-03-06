package edu.ucsd.cse110.cse110_team17_project.model;

import android.util.Pair;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import edu.ucsd.cse110.cse110_team17_project.model.UserInfoAPI;
import edu.ucsd.cse110.cse110_team17_project.model.UserInfo;

public class UserRepository {


    public UserRepository() {

    }

    public LiveData<Pair<Double, Double>> getRemoteUserInfo(String privateCode) {
        var coordinate = new MutableLiveData<Pair<Double, Double>>();
        var executor = Executors.newSingleThreadScheduledExecutor();
        UserInfoAPI uApi = UserInfoAPI.provide();

        var Future = executor.scheduleAtFixedRate(() -> {
            String json = uApi.getUser(privateCode);
            var user = UserInfo.formJSON(json);
            Pair<Double, Double> pair = new Pair<>(user.latitude, user.longitude);
            coordinate.postValue(pair);
        }, 0, 5000, TimeUnit.MILLISECONDS);
        return coordinate;
    }

}
