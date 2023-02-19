package edu.ucsd.cse110.cse110_team17_project;


import static android.content.Context.MODE_PRIVATE;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

@RunWith(RobolectricTestRunner.class)
public class US3_2UnitTest{

    final String TEST_LABEL1 = "Test Label1";
    final String TEST_COORDINATE1 = "Test Coordinate1";
    final String TEST_LABEL2 = "Test Label1";
    final String TEST_COORDINATE2 = "Test Coordinate1";
    final String TEST_LABEL3 = "Test Label1";
    final String TEST_COORDINATE3 = "Test Coordinate1";
    @Before
    public void createPref() {
        SharedPreferences sharedPreferences = RuntimeEnvironment.application.getSharedPreferences("Main", Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("label1", TEST_LABEL1).commit();
        sharedPreferences.edit().putString("coordinate1", TEST_COORDINATE1).commit();
        sharedPreferences.edit().putString("label2", TEST_LABEL1).commit();
        sharedPreferences.edit().putString("coordinate2", TEST_COORDINATE2).commit();
        sharedPreferences.edit().putString("label3", TEST_LABEL1).commit();
        sharedPreferences.edit().putString("coordinate3", TEST_COORDINATE3).commit();

    }

    // It tests if Main get SharePreference Data
    @Test
    public void TestSharePre(){
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        scenario.onActivity(activity -> {
            TextView label1 = (TextView) activity.findViewById(R.id.label_1);
            assertEquals(TEST_LABEL1,label1.getText().toString());
            assertEquals(TEST_LABEL2,label1.getText().toString());
            assertEquals(TEST_LABEL3,label1.getText().toString());
        });
    }
}
