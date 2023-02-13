package edu.ucsd.cse110.cse110_team17_project;

import static android.content.Context.MODE_PRIVATE;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.*;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.platform.device.DeviceController;


@RunWith(RobolectricTestRunner.class)
public class US1_1UnitTest {
    @Test
    public void testOnSubmitClicked() {
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(activity -> {
                TextView label_name1 = activity.findViewById(R.id.label_name1);
                TextView label_name2 = activity.findViewById(R.id.label_name2);
                TextView label_name3 = activity.findViewById(R.id.label_name3);
                TextView coordinate1 = activity.findViewById(R.id.coordinate1);
                TextView coordinate2 = activity.findViewById(R.id.coordinate2);
                TextView coordinate3 = activity.findViewById(R.id.coordinate3);

                assertEquals(label_name1.getVisibility(), View.VISIBLE);
                assertEquals("", label_name1.getText().toString());
                assertEquals(label_name2.getVisibility(), View.VISIBLE);
                assertEquals("", label_name2.getText().toString());
                assertEquals(label_name3.getVisibility(), View.VISIBLE);
                assertEquals("", label_name3.getText().toString());
                assertEquals(coordinate1.getVisibility(), View.VISIBLE);
                assertEquals("", coordinate1.getText().toString());
                assertEquals(coordinate2.getVisibility(), View.VISIBLE);
                assertEquals("", coordinate2.getText().toString());
                assertEquals(coordinate3.getVisibility(), View.VISIBLE);
                assertEquals("", coordinate3.getText().toString());

                int orientation = activity.getRequestedOrientation();
                assertEquals(Configuration.ORIENTATION_PORTRAIT, orientation);
                activity.setRequestedOrientation(Configuration.ORIENTATION_LANDSCAPE);
                assertEquals(Configuration.ORIENTATION_PORTRAIT, orientation);
            });
        }
    }
}
