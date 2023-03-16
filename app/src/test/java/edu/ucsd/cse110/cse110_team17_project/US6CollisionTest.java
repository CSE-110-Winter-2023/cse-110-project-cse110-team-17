package edu.ucsd.cse110.cse110_team17_project;

import static org.junit.Assert.assertEquals;

import android.Manifest;
import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.widget.TextView;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.rule.GrantPermissionRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.cse110_team17_project.activity.MainActivity;
import edu.ucsd.cse110.cse110_team17_project.model.UserInfo;

@RunWith(RobolectricTestRunner.class)
public class US6CollisionTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
    @Rule
    public GrantPermissionRule mRuntimePermissionRule = GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);

    @Test
    public void testCollision() {
        var scenario = ActivityScenario.launch(MainActivity.class);
        List<UserInfo> mockUserInfos = new ArrayList<>();
        mockUserInfos.add(new UserInfo("", "", "MockPoint1", 1.0, 1.0));
        mockUserInfos.add(new UserInfo("", "", "MockPoint2", 1.0, 1.0));
        mockUserInfos.add(new UserInfo("", "", "MockPoint3", 1.0, 1.0));
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.onActivity(activity -> {
            activity.isMockLocationTesting = true;
            activity.userrepo.setData(mockUserInfos);
        });

        scenario.moveToState(Lifecycle.State.RESUMED);
        scenario.onActivity(activity -> {

            TextView tv1 = activity.findViewById(R.id.label_1);
            assertEquals("MockPoint1", tv1.getText());
        });

    }
}
