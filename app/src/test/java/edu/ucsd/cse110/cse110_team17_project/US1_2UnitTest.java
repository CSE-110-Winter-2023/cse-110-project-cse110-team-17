package edu.ucsd.cse110.cse110_team17_project;

import static android.content.Context.MODE_PRIVATE;
import static org.junit.Assert.assertEquals;

import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.EditText;

import androidx.test.core.app.ActivityScenario;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class US1_2UnitTest {
    final String TEST_LABEL1 = "Test Label1";
    final String TEST_COORDINATE1 = "Test Coordinate1";
    final String TEST_LABEL2 = "Test Label2";
    final String TEST_COORDINATE2 = "Test Coordinate2";
    final String TEST_LABEL3 = "Test Label3";
    final String TEST_COORDINATE3 = "Test Coordinate3";
    final String EMPTY_STRING = "";

    @Test
    public void testInputOneSavedOnSubmitClicked(){
        try (ActivityScenario<InputActivity> scenario = ActivityScenario.launch(InputActivity.class)) {
            scenario.onActivity(activity -> {
                EditText label1 = activity.findViewById(R.id.label_name1);
                EditText coordinate1 = activity.findViewById(R.id.coordinate1);
                Button submitBtn = activity.findViewById(R.id.submitBtn);
                label1.setText(TEST_LABEL1);
                coordinate1.setText(TEST_COORDINATE1);
                submitBtn.performClick();
//                SharedPreferences preferences = activity.getPreferences(MODE_PRIVATE);
                SharedPreferences preferences = activity.getSharedPreferences("MAIN", MODE_PRIVATE);
                assertEquals(preferences.getString("label1", ""), TEST_LABEL1);
                assertEquals(preferences.getString("coordinate1", ""), TEST_COORDINATE1);
            });
        }
    }

    @Test
    public void testAllInputsSavedOnSubmitClicked(){
        try (ActivityScenario<InputActivity> scenario = ActivityScenario.launch(InputActivity.class)) {
            scenario.onActivity(activity -> {
                EditText label1 = activity.findViewById(R.id.label_name1);
                EditText coordinate1 = activity.findViewById(R.id.coordinate1);
                EditText label2 = activity.findViewById(R.id.label_name2);
                EditText coordinate2 = activity.findViewById(R.id.coordinate2);
                EditText label3 = activity.findViewById(R.id.label_name3);
                EditText coordinate3 = activity.findViewById(R.id.coordinate3);
                Button submitBtn = activity.findViewById(R.id.submitBtn);
                label1.setText(TEST_LABEL1);
                coordinate1.setText(TEST_COORDINATE1);
                label2.setText(TEST_LABEL2);
                coordinate2.setText(TEST_COORDINATE2);
                label3.setText(TEST_LABEL3);
                coordinate3.setText(TEST_COORDINATE3);
                submitBtn.performClick();
//                SharedPreferences preferences = activity.getPreferences(MODE_PRIVATE);
                SharedPreferences preferences = activity.getSharedPreferences("MAIN", MODE_PRIVATE);
                assertEquals(TEST_LABEL1, preferences.getString("label1", ""));
                assertEquals(TEST_COORDINATE1, preferences.getString("coordinate1", ""));
                assertEquals(TEST_LABEL2, preferences.getString("label2", ""));
                assertEquals(TEST_COORDINATE2, preferences.getString("coordinate2", ""));
                assertEquals(TEST_LABEL3, preferences.getString("label3", ""));
                assertEquals(TEST_COORDINATE3, preferences.getString("coordinate3", ""));
            });
        }
    }
    @Test
    public void testNoInputsOnSubmitClicked(){
        try (ActivityScenario<InputActivity> scenario = ActivityScenario.launch(InputActivity.class)) {
            scenario.onActivity(activity -> {
                Button submitBtn = activity.findViewById(R.id.submitBtn);
                submitBtn.performClick();
//                SharedPreferences preferences = activity.getPreferences(MODE_PRIVATE);
                SharedPreferences preferences = activity.getSharedPreferences("MAIN", MODE_PRIVATE);
                assertEquals(preferences.getString("label1", ""), EMPTY_STRING);
                assertEquals(preferences.getString("coordinate1", ""), EMPTY_STRING);
                assertEquals(preferences.getString("label2", ""), EMPTY_STRING);
                assertEquals(preferences.getString("coordinate2", ""), EMPTY_STRING);
                assertEquals(preferences.getString("label3", ""), EMPTY_STRING);
                assertEquals(preferences.getString("coordinate3", ""), EMPTY_STRING);
            });
        }
    }
    @Test
    public void testMissingCoordinateOnSubmitClicked(){
        try (ActivityScenario<InputActivity> scenario = ActivityScenario.launch(InputActivity.class)) {
            scenario.onActivity(activity -> {
                EditText label1 = activity.findViewById(R.id.label_name1);
                Button submitBtn = activity.findViewById(R.id.submitBtn);
                label1.setText(TEST_LABEL1);
                submitBtn.performClick();
//                SharedPreferences preferences = activity.getPreferences(MODE_PRIVATE);
                SharedPreferences preferences = activity.getSharedPreferences("MAIN", MODE_PRIVATE);
                assertEquals(preferences.getString("label1", ""), EMPTY_STRING);
                assertEquals(preferences.getString("coordinate1", ""), EMPTY_STRING);
            });
        }
    }
    @Test
    public void testMissingLabelOnSubmitClicked(){
        try (ActivityScenario<InputActivity> scenario = ActivityScenario.launch(InputActivity.class)) {
            scenario.onActivity(activity -> {
                EditText coordinate1 = activity.findViewById(R.id.coordinate1);
                Button submitBtn = activity.findViewById(R.id.submitBtn);
                coordinate1.setText(TEST_COORDINATE1);
                submitBtn.performClick();
//                SharedPreferences preferences = activity.getPreferences(MODE_PRIVATE);
                SharedPreferences preferences = activity.getSharedPreferences("MAIN", MODE_PRIVATE);
                assertEquals(preferences.getString("label1", ""), EMPTY_STRING);
                assertEquals(preferences.getString("coordinate1", ""), EMPTY_STRING);
            });
        }
    }
}
