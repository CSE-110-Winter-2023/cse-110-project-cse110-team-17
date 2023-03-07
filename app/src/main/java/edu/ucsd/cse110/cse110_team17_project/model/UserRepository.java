package edu.ucsd.cse110.cse110_team17_project.model;

import android.util.Pair;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import edu.ucsd.cse110.cse110_team17_project.model.UserInfoAPI;
import edu.ucsd.cse110.cse110_team17_project.model.UserInfo;

public class UserRepository {


    public UserRepository() {

    }

    public LiveData<List<UserInfo>> getRemoteUserInfo(List<String> privateCodes) {
        var resultList = new MutableLiveData<List<UserInfo>>();
        var executor = Executors.newSingleThreadScheduledExecutor();
        UserInfoAPI uApi = UserInfoAPI.provide();

        var Future = executor.scheduleAtFixedRate(() -> {
            var tempList = resultList.getValue();
            tempList.clear();
            for (String privateCode: privateCodes) {
                String json = uApi.getUser(privateCode);
                tempList.add(UserInfo.formJSON(json));
            }
            resultList.postValue(tempList);
        }, 0, 5000, TimeUnit.MILLISECONDS);
        return resultList;
    }

}
