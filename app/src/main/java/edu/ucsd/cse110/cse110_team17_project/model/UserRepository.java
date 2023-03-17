package edu.ucsd.cse110.cse110_team17_project.model;

import android.util.Log;
import android.util.Pair;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import edu.ucsd.cse110.cse110_team17_project.model.UserInfoAPI;
import edu.ucsd.cse110.cse110_team17_project.model.UserInfo;

public class UserRepository {

    UserInfoAPI uApi = UserInfoAPI.provide();
    public MutableLiveData<List<UserInfo>> userInfoList;

    public UserRepository() {
        userInfoList = new MutableLiveData<>();
    }

    public void setData(List<UserInfo> userInfos) {
        userInfoList.setValue(userInfos);
    }

    public LiveData<List<UserInfo>> getRemoteUserInfo(List<String> publicCodes, boolean isTesting) {
            var executor = Executors.newSingleThreadScheduledExecutor();
            var resultList = new MutableLiveData<List<UserInfo>>();
            if (!isTesting) {
                var Future = executor.scheduleAtFixedRate(() -> {
                        var tempList = new ArrayList<UserInfo>();
                        for (String publicCode : publicCodes) {
                            String json = uApi.getUser(publicCode);
                            tempList.add(UserInfo.formJSON(json));
                        }
                        resultList.postValue(tempList);
                }, 0, 5000, TimeUnit.MILLISECONDS);
                return resultList;
            }
            else {
                return userInfoList;
            }
    }




    public void postLocalUserInfo(UserInfo uI) {
        var executor = Executors.newSingleThreadScheduledExecutor();
        var Future = executor.scheduleAtFixedRate(() -> {
            String response = uApi.postInfo(uI);
            Log.i("POST", response);
        }, 0, 5000, TimeUnit.MILLISECONDS);
    }


}
