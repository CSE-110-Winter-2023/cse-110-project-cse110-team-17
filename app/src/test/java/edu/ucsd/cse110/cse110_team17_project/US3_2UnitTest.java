package edu.ucsd.cse110.cse110_team17_project;


import static android.app.UiAutomation.ROTATION_FREEZE_0;
import static android.content.Context.MODE_PRIVATE;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import org.junit.Rule;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import android.app.UiAutomation;
import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.MutableLiveData;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.ibm.icu.impl.Assert;

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
    final String ZERO = Character.toString('0');
    final String ONE = Character.toString('1');
    final String TWO = Character.toString('2');
    final String FIVE = Character.toString('5');
    final String TEN = "10";
    final String A_Round_360 = "360";
    @Before
    public void createPref() {

        SharedPreferences sharedPreferences = RuntimeEnvironment.getApplication().getSharedPreferences("Main", Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("label1", TEST_LABEL1).commit();
        sharedPreferences.edit().putString("coordinate1", TEST_COORDINATE1).commit();
        sharedPreferences.edit().putString("label2", TEST_LABEL2).commit();
        sharedPreferences.edit().putString("coordinate2", TEST_COORDINATE2).commit();
        sharedPreferences.edit().putString("label3", TEST_LABEL3).commit();
        sharedPreferences.edit().putString("coordinate3", TEST_COORDINATE3).commit();

    }

    // It is same as professor provided in github, but comment this Rule wouldn't affect result
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    // It tests if Main get SharePreference Data
    @Test
    public void TestSharePre(){
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        scenario.onActivity(activity -> {
            TextView label1 = (TextView) activity.findViewById(R.id.label_1);
            TextView label2 = (TextView) activity.findViewById(R.id.label_2);
            TextView label3 = (TextView) activity.findViewById(R.id.label_3);
            assertEquals(TEST_LABEL1,label1.getText().toString());
            assertEquals(TEST_LABEL2,label2.getText().toString());
            assertEquals(TEST_LABEL3,label3.getText().toString());
        });
    }

    // It test if angle can rotate compassFace
    @Test
    public void TestAngleInput(){
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.onActivity(activity -> {
            TextView angleInput = (TextView) activity.findViewById(R.id.input_angle);
            Button comfirmBt = (Button) activity.findViewById(R.id.confirmBtn);
            ImageView compass = activity.findViewById(R.id.compass_face);
            angleInput.setText(ZERO);
            comfirmBt.performClick();
            int angleStart = (int) compass.getRotation();
            angleInput.setText(ONE);
            comfirmBt.performClick();
            int angle1 = (int) compass.getRotation() - angleStart;
            angleInput.setText(TWO);
            comfirmBt.performClick();
            int angle2 = (int) compass.getRotation()- angleStart;
            angleInput.setText(FIVE);
            comfirmBt.performClick();
            int angle3 = (int) compass.getRotation() - angleStart;
            angleInput.setText(TEN);
            comfirmBt.performClick();
            int angle4 = (int) compass.getRotation() - angleStart;
            angleInput.setText(A_Round_360);
            comfirmBt.performClick();
            int angle5 = (int) compass.getRotation() - angleStart;
            assertEquals( Integer.parseInt(ONE), angle1);
            assertEquals( Integer.parseInt(TWO), angle2);
            assertEquals( Integer.parseInt(FIVE), angle3);
            assertEquals( Integer.parseInt(TEN), angle4);
            assertEquals(  Integer.parseInt(A_Round_360), angle5);
        });
    }

    // To test Resume button, for now, we have to adjust angle before using sensor
    @Test
    public void TestResume(){
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.STARTED);

        MutableLiveData<Float> mockDataSource = new MutableLiveData<>();

        scenario.onActivity(activity -> {
            OrientationService orientationService = OrientationService.singleton(activity);
            orientationService.setMockOrientationSource(mockDataSource);

            ImageView compass = activity.findViewById(R.id.compass_face);
            Button resumeBt = activity.findViewById(R.id.resume_btn);
            Button comfirmBt = activity.findViewById(R.id.confirmBtn);
            TextView angleInput = activity.findViewById(R.id.input_angle);
            angleInput.setText(TWO);
            comfirmBt.performClick();
            resumeBt.performClick();
            float startAngle = compass.getRotation();
            mockDataSource.setValue(0.1F);
            float angle1 = compass.getRotation();
            mockDataSource.setValue(0.2F);
            float angle2 = compass.getRotation();
            mockDataSource.setValue(0.3F);
            float angle3 = compass.getRotation();
            mockDataSource.setValue(0.1F);

            assertNotEquals(angle1, angle2);
            assertNotEquals(angle3, angle2);
            assertNotEquals(angle1, angle3);

            mockDataSource.setValue(0.1F);
            assertTrue(angle1 == compass.getRotation());
        });
    }
    // Repeat Button to see if still functioning
    @Test
    public void TestRepeateBt() {
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.STARTED);

        MutableLiveData<Float> mockDataSource = new MutableLiveData<>();

        scenario.onActivity(activity -> {
            OrientationService orientationService = OrientationService.singleton(activity);
            orientationService.setMockOrientationSource(mockDataSource);

            ImageView compass = activity.findViewById(R.id.compass_face);
            Button resumeBt = activity.findViewById(R.id.resume_btn);
            Button comfirmBt = activity.findViewById(R.id.confirmBtn);
            TextView angleInput = activity.findViewById(R.id.input_angle);
            mockDataSource.setValue(0F);
            angleInput.setText(TWO);
            comfirmBt.performClick();
            int angle_TWO_1 = (int) compass.getRotation();
            resumeBt.performClick();
            int angle_ZERO_1 = (int) compass.getRotation();
            assertNotEquals(angle_TWO_1, angle_ZERO_1);

            comfirmBt.performClick();
            int angle_TWO_2 = (int) compass.getRotation();
            resumeBt.performClick();
            int angle_ZERO_2 = (int) compass.getRotation();
            assertNotEquals(angle_TWO_2, angle_ZERO_2);

            angleInput.setText(FIVE);
            comfirmBt.performClick();
            int angle_FIVE_1 = (int) compass.getRotation();
            resumeBt.performClick();
            int angle_ZERO_3 = (int) compass.getRotation();
            assertNotEquals(angle_FIVE_1, angle_ZERO_3);

            mockDataSource.setValue(1F);

            int angle_1F_1 = (int) compass.getRotation();
            assertNotEquals(angle_FIVE_1, angle_1F_1);

            angleInput.setText(FIVE);
            comfirmBt.performClick();
            int angle_FIVE_2 = (int) compass.getRotation();
            mockDataSource.setValue(1F);
            int angle_1F_2 = (int) compass.getRotation();
            assertNotEquals(angle_FIVE_2, angle_1F_2);

            angleInput.setText(TEN);
            comfirmBt.performClick();
            int angle_TEN_1 = (int) compass.getRotation();
            mockDataSource.setValue(2F);
            int angle_2F_1 = (int) compass.getRotation();
            assertNotEquals(angle_TEN_1, angle_2F_1);

            angleInput.setText(TEN);
            comfirmBt.performClick();
            int angle_TEN_2 = (int) compass.getRotation();
            mockDataSource.setValue(1F);
            int angle_1F_3 = (int) compass.getRotation();
            assertNotEquals(angle_TEN_2, angle_1F_3);


            assertEquals(angle_ZERO_1, angle_ZERO_2);
            assertEquals(angle_ZERO_2, angle_ZERO_3);
            assertEquals(angle_TWO_1, angle_TWO_2);
            assertEquals(angle_TEN_1, angle_TEN_2);
            assertEquals(angle_1F_1, angle_1F_2);
            assertEquals(angle_1F_2, angle_1F_3);

        });
    }
}
