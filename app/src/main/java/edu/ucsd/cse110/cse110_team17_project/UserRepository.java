package edu.ucsd.cse110.cse110_team17_project;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

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
