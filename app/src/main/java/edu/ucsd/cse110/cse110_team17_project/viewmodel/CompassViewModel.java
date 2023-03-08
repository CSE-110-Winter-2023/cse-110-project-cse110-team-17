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

    private LiveData<List<UserInfo>> userInfos;

    private UserRepository userRepository;

    public CompassViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository();
    }

    public LiveData<List<UserInfo>> getUserInfos(List<String> codes) {
        if (userInfos == null) {
            userInfos = userRepository.getRemoteUserInfo(codes);
        }

        return userInfos;
    }

    public void postUserInfo(UserInfo uI) {
        userRepository.postLocalUserInfo(uI);
    }
}
