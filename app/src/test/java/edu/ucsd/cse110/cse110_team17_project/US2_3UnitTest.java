package edu.ucsd.cse110.cse110_team17_project;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import org.junit.Rule;
import org.junit.Test;


import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.android.controller.ActivityController;

@RunWith(RobolectricTestRunner.class)
public class US2_3UnitTest {
    Intent intent = new Intent(getApplicationContext(), CompassActivity.class);

    final String TEST_LABEL1 = "Test Label1";
    final String TEST_COORDINATE1 = "Test Coordinate1";
    final String TEST_LABEL2 = "Test Label2";
    final String TEST_COORDINATE2 = "Test Coordinate2";
    final String TEST_LABEL3 = "Test Label3";
    final String TEST_COORDINATE3 = "Test Coordinate3";
    final String EMPTY_STRING = "";
    @Test
    public void testBackButton(){
        intent.putExtra("label_1","1");
        intent.putExtra("label_2","1");
        intent.putExtra("label_3","1");
        ActivityScenario<CompassActivity> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            Button BackButton = (Button) activity.findViewById(R.id.back_btn);
            BackButton.performClick();
            assertTrue(activity.isFinishing());
        });
    }

    @Test
    public void testAllLabelsPresent() {
        intent.putExtra("label_1",TEST_LABEL1);
        intent.putExtra("label_2",TEST_LABEL2);
        intent.putExtra("label_3",TEST_LABEL3);
        ActivityScenario<CompassActivity> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            TextView label_1 = (TextView) activity.findViewById(R.id.label_1);
            TextView label_2 = (TextView) activity.findViewById(R.id.label_2);
            TextView label_3 = (TextView) activity.findViewById(R.id.label_3);

            assertEquals(TEST_LABEL1, label_1.getText().toString());
            assertEquals(TEST_LABEL2, label_2.getText().toString());
            assertEquals(TEST_LABEL3, label_3.getText().toString());
        });

    }

    @Test
    public void testOnlyFirstLabel() {
        intent.putExtra("label_1",TEST_LABEL1);
        intent.putExtra("label_2",EMPTY_STRING);
        intent.putExtra("label_3",EMPTY_STRING);
        ActivityScenario<CompassActivity> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            TextView label_1 = (TextView) activity.findViewById(R.id.label_1);
            TextView label_2 = (TextView) activity.findViewById(R.id.label_2);
            TextView label_3 = (TextView) activity.findViewById(R.id.label_3);

            assertEquals(TEST_LABEL1, label_1.getText().toString());
            assertEquals(EMPTY_STRING, label_2.getText().toString());
            assertEquals(EMPTY_STRING, label_3.getText().toString());
        });

    }

    @Test
    public void testOnlyFirstLabelMissing() {
        intent.putExtra("label_1",EMPTY_STRING);
        intent.putExtra("label_2",TEST_LABEL2);
        intent.putExtra("label_3",TEST_LABEL3);
        ActivityScenario<CompassActivity> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            TextView label_1 = (TextView) activity.findViewById(R.id.label_1);
            TextView label_2 = (TextView) activity.findViewById(R.id.label_2);
            TextView label_3 = (TextView) activity.findViewById(R.id.label_3);

            assertEquals(EMPTY_STRING, label_1.getText().toString());
            assertEquals(TEST_LABEL2, label_2.getText().toString());
            assertEquals(TEST_LABEL3, label_3.getText().toString());
        });

    }
}
