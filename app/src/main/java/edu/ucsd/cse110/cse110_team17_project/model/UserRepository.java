package edu.ucsd.cse110.cse110_team17_project.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import edu.ucsd.cse110.cse110_team17_project.model.LocationAPI;
import edu.ucsd.cse110.cse110_team17_project.model.UserInfo;

public class UserRepository {

    public LiveData<UserInfo> getRemoteUserInfo(String privateCode) {
        var user = new MutableLiveData<UserInfo>();
        var executor = Executors.newSingleThreadScheduledExecutor();
        LocationAPI locationApi = LocationAPI.provide();

        var Future = executor.scheduleAtFixedRate(() -> {
            String json = locationApi.getUser(privateCode);
            user.postValue(UserInfo.formJSON(json));
        }, 0, 5000, TimeUnit.MILLISECONDS);
        return user;
    }
}
