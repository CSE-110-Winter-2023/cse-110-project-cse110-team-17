package edu.ucsd.cse110.cse110_team17_project;

import static android.content.Context.MODE_PRIVATE;

import static org.junit.Assert.assertEquals;

import android.Manifest;
import android.content.SharedPreferences;
import android.util.Pair;
import android.widget.TextView;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.MutableLiveData;
import androidx.test.core.app.ActivityScenario;
import androidx.test.rule.GrantPermissionRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import edu.ucsd.cse110.cse110_team17_project.activity.MainActivity;
import edu.ucsd.cse110.cse110_team17_project.model.LocationService;
import edu.ucsd.cse110.cse110_team17_project.model.Utilities;

@RunWith(RobolectricTestRunner.class)
public class US3_1UnitTest {
    final String TEST_LABEL1 = "Test Label1";
    final String TEST_COORDINATE1 = "0,0";
    Pair<Double, Double> coordinate1 = new Pair<>(0.0, 0.0);
    final String TEST_LABEL2 = "Test Label2";
    final String TEST_COORDINATE2 = "80,100";
    Pair<Double, Double> coordinate2 = new Pair<>(80.0, 100.0);
    final String TEST_LABEL3 = "Test Label3";
    final String TEST_COORDINATE3 = "50,-50";
    Pair<Double, Double> coordinate3 = new Pair<>(50.0, -50.0);
    Pair<Double, Double> curCoordinate = new Pair<>(32.715736, -117.16108);


    @Before
    public void createPref() {
        SharedPreferences sharedPreferences = RuntimeEnvironment.getApplication().getSharedPreferences("Main", MODE_PRIVATE);
        sharedPreferences.edit().putString("label1", TEST_LABEL1).apply();
        sharedPreferences.edit().putString("coordinate1", TEST_COORDINATE1).apply();
        sharedPreferences.edit().putString("label2", TEST_LABEL2).apply();
        sharedPreferences.edit().putString("coordinate2", TEST_COORDINATE2).apply();
        sharedPreferences.edit().putString("label3", TEST_LABEL3).apply();
        sharedPreferences.edit().putString("coordinate3", TEST_COORDINATE3).apply();

    }


    @Rule
    public GrantPermissionRule mRuntimePermissionRule = GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);

    @Test
    public void testCorrectAngleShown() {
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.RESUMED);

        MutableLiveData<Pair<Double, Double>> mockDataSource = new MutableLiveData<>();

        scenario.onActivity(activity -> {


            mockDataSource.setValue(curCoordinate);

            LocationService locationService = LocationService.singleton(activity);
            locationService.setMockOrientationSource(mockDataSource);

            TextView name_label1 = (TextView) activity.findViewById(R.id.label_1);
            TextView name_label2 = (TextView) activity.findViewById(R.id.label_2);
            TextView name_label3 = (TextView) activity.findViewById(R.id.label_3);


            float coordinate1Angle = (float) Utilities.updateAngle(curCoordinate.first.floatValue(),
                    curCoordinate.second.floatValue(), coordinate1.first.floatValue(), coordinate1.second.floatValue());

            float coordinate2Angle = (float) Utilities.updateAngle(curCoordinate.first.floatValue(),
                    curCoordinate.second.floatValue(), coordinate2.first.floatValue(), coordinate2.second.floatValue());

            float coordinate3Angle = (float) Utilities.updateAngle(curCoordinate.first.floatValue(),
                    curCoordinate.second.floatValue(), coordinate3.first.floatValue(), coordinate3.second.floatValue());


            float shownAngle1 = name_label1.getRotation();
            float shownAngle2 = name_label2.getRotation();
            float shownAngle3 = name_label3.getRotation();

            assertEquals(coordinate1Angle, shownAngle1, 0.01);
            assertEquals(coordinate2Angle, shownAngle2, 0.01);
            assertEquals(coordinate3Angle, shownAngle3, 0.01);
        });
    }


}
