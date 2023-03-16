package edu.ucsd.cse110.cse110_team17_project;

import static org.junit.Assert.assertEquals;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.cse110_team17_project.model.UserInfo;
import edu.ucsd.cse110.cse110_team17_project.model.UserInfoAPI;
import edu.ucsd.cse110.cse110_team17_project.model.UserRepository;

@RunWith(RobolectricTestRunner.class)
public class US3RemoteAPITest {

    UserRepository uS = new UserRepository();
    static final String TEST_PUBLIC_CODE1 = "group17test1";
    static final String TEST_PUBLIC_CODE2 = "group17test2";
    static final String TEST_PUBLIC_CODE3 = "group17test3";
    static final String TEST_LABEL1 = "Point1";
    static final String TEST_LABEL2 = "Point2";
    static final String TEST_LABEL3 = "Point3";
    static final double TEST_LATITUDE1 = 41.5;
    static final double TEST_LONGTITUDE1 = -120.81;
    static final double TEST_LATITUDE2 = 4.5;
    static final double TEST_LONGTITUDE2 = -10.81;
    static final double TEST_LATITUDE3 = 0;
    static final double TEST_LONGTITUDE3 = 0;



    static final String TEST_PUT_PUBLIC = "Group17UnitTestPut";
    static final String TEST_PUT_PRIVATE = "Group17UnitTestPut";


    @Test
    public void testRemoteGet() {
        UserInfoAPI uI = UserInfoAPI.provide();

        String result = uI.getUser(TEST_PUBLIC_CODE1);
        UserInfo user = UserInfo.formJSON(result);


        assertEquals(TEST_PUBLIC_CODE1, user.public_code);
        assertEquals(TEST_LATITUDE1, user.latitude, 0.01);
        assertEquals(TEST_LONGTITUDE1, user.longitude, 0.01);
        assertEquals(TEST_LABEL1, user.label);

    }

    @Test
    public void testRemotePut() {
        UserInfoAPI uI = UserInfoAPI.provide();
        UserInfo user = new UserInfo(TEST_PUT_PRIVATE, TEST_PUT_PUBLIC, TEST_LABEL1, TEST_LATITUDE1, TEST_LONGTITUDE1);

        uI.postInfo(user);
        String result = uI.getUser(TEST_PUT_PUBLIC);
        UserInfo get = UserInfo.formJSON(result);

        assertEquals(user.public_code, get.public_code);
        assertEquals(user.latitude, get.latitude, 0.01);
        assertEquals(user.longitude, get.longitude, 0.01);
        assertEquals(user.label, get.label);
    }

    @Test
    public void testRepoGet() {
        UserInfoAPI uI = UserInfoAPI.provide();

        UserInfo user1 = new UserInfo(TEST_PUBLIC_CODE1, TEST_PUBLIC_CODE1, TEST_LABEL1, TEST_LATITUDE1, TEST_LONGTITUDE1);
        UserInfo user2 = new UserInfo(TEST_PUBLIC_CODE2, TEST_PUBLIC_CODE2, TEST_LABEL2, TEST_LATITUDE2, TEST_LONGTITUDE2);
        UserInfo user3 = new UserInfo(TEST_PUBLIC_CODE3, TEST_PUBLIC_CODE3, TEST_LABEL3, TEST_LATITUDE3, TEST_LONGTITUDE3);
        uI.postInfo(user1);
        uI.postInfo(user2);
        uI.postInfo(user3);

        List<String> publicCodes = new ArrayList<>();
        publicCodes.add(TEST_PUBLIC_CODE1);
        publicCodes.add(TEST_PUBLIC_CODE2);
        publicCodes.add(TEST_PUBLIC_CODE3);

        LiveData<List<UserInfo>> liveInfos = uS.getRemoteUserInfo(publicCodes, false);


        liveInfos.observeForever(new Observer<List<UserInfo>>() {
            @Override
            public void onChanged(List<UserInfo> userInfos) {
                var info = liveInfos.getValue();

                UserInfo getUser1 =  info.get(0);
                UserInfo getUser2 =  info.get(1);
                UserInfo getUser3 =  info.get(2);

                assertEquals(user1.public_code, getUser1.public_code);
                assertEquals(user1.latitude, getUser1.latitude, 0.01);
                assertEquals(user1.longitude, getUser1.longitude, 0.01);
                assertEquals(user1.label, getUser1.label);

                assertEquals(user2.public_code, getUser2.public_code);
                assertEquals(user2.latitude, getUser2.latitude, 0.01);
                assertEquals(user2.longitude, getUser2.longitude, 0.01);
                assertEquals(user2.label, getUser2.label);

                assertEquals(user3.public_code, getUser3.public_code);
                assertEquals(user3.latitude, getUser3.latitude, 0.01);
                assertEquals(user3.longitude, getUser3.longitude, 0.01);
                assertEquals(user3.label, getUser3.label);
            }
        });
    }


}
