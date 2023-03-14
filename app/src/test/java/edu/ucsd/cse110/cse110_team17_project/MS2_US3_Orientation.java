package edu.ucsd.cse110.cse110_team17_project;


import android.Manifest;
import android.arch.core.executor.testing.InstantTaskExecutorRule;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.test.rule.GrantPermissionRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.cse110_team17_project.model.UserInfo;

@RunWith(RobolectricTestRunner.class)
public class MS2_US3_Orientation {

    List<Integer> MockInfoList= new ArrayList<>();
    UserInfo user1 = new UserInfo("privateCode1", "label1", "publicCode1");
    UserInfo user2 = new UserInfo("privateCode2", "label2", "publicCode2");
    UserInfo user3 = new UserInfo("privateCode3", "label3", "publicCode3");
//    MockInfoList.add(1)


    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
    @Rule
    public GrantPermissionRule mRuntimePermissionRule = GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);


}
