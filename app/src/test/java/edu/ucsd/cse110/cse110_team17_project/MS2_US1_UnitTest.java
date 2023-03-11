package edu.ucsd.cse110.cse110_team17_project;

import static android.content.Context.MODE_PRIVATE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

<<<<<<< HEAD
import android.content.SharedPreferences;
import android.content.res.Configuration;
=======
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.util.Pair;
>>>>>>> 5b2bd4a5a7d53716ed3105ddc61761f21ce71574
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

<<<<<<< HEAD
import edu.ucsd.cse110.cse110_team17_project.activity.EnterNameActivity;

=======
>>>>>>> 5b2bd4a5a7d53716ed3105ddc61761f21ce71574
@RunWith(RobolectricTestRunner.class)
public class MS2_US1_UnitTest {

    @Test
    public void testOnSubmitClicked() {
        try (ActivityScenario<EnterNameActivity> scenario = ActivityScenario.launch(EnterNameActivity.class)) {
            scenario.onActivity(activity -> {
                TextView username = activity.findViewById(R.id.username);

                assertEquals(username.getVisibility(), View.VISIBLE);
                assertEquals("", username.getText().toString());

                int orientation = activity.getRequestedOrientation();
                assertEquals(Configuration.ORIENTATION_PORTRAIT, orientation);
                activity.setRequestedOrientation(Configuration.ORIENTATION_LANDSCAPE);
                assertEquals(Configuration.ORIENTATION_PORTRAIT, orientation);
            });
        }
    }

    @Test
    public void testUsernameSavedOnSubmitClicked(){
        try (ActivityScenario<EnterNameActivity> scenario = ActivityScenario.launch(EnterNameActivity.class)) {
            scenario.onActivity(activity -> {
                EditText username = activity.findViewById(R.id.username);
                Button submitBtn = activity.findViewById(R.id.submitName);
                username.setText("username");
                submitBtn.performClick();
                SharedPreferences preferences = activity.getSharedPreferences("MAIN", MODE_PRIVATE);
                assertEquals(preferences.getString("username", ""), "username");
            });
        }
    }

    @Test
    public void testNoInputsOnSubmitClicked(){
        try (ActivityScenario<EnterNameActivity> scenario = ActivityScenario.launch(EnterNameActivity.class)) {
            scenario.onActivity(activity -> {
                Button submitBtn = activity.findViewById(R.id.submitName);
                submitBtn.performClick();
                SharedPreferences preferences = activity.getSharedPreferences("MAIN", MODE_PRIVATE);
                assertEquals(preferences.getString("username", ""), "");
            });
        }
    }

    @Test
    public void testUpdateDistance(){
        double lat1 = 32;
        double lat2 = 60;
        double lat3 = -89;
        double long1 = 70;
        double long2 = -117;
        double long3 = 173;
        //north, same hemisphere
        double distance = Utilities.distance(lat1, long1, lat2, long2);
        assertEquals(9765/1.609, distance, 0.01);

        distance = Utilities.distance(lat1, long1, lat1, long1);
        assertEquals(0, distance, 0);

        distance = Utilities.distance(lat2, long2, lat3, long3);
        assertEquals(16639.741/1.609, distance, 0.01);

        distance = Utilities.distance(lat1, long1, lat3, long3);
        assertEquals(13590.216/1.609, distance, 0.01);
    }
}
