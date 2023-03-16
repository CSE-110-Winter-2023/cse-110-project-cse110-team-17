package edu.ucsd.cse110.cse110_team17_project;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.SystemClock;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.rule.GrantPermissionRule;

import org.checkerframework.checker.units.qual.A;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import edu.ucsd.cse110.cse110_team17_project.activity.MainActivity;
import edu.ucsd.cse110.cse110_team17_project.activity.UIDActivity;
import edu.ucsd.cse110.cse110_team17_project.services.LocationService;



@RunWith(RobolectricTestRunner.class)
public class US5_UnitTest {
    @Rule public GrantPermissionRule permissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    @Test
    public void testGPSBeforeStart() {
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(activity -> {
                ImageView green = activity.findViewById(R.id.green_dot);
                ImageView red = activity.findViewById(R.id.red_dot);
                TextView timeout = activity.findViewById(R.id.timeout);

                assertEquals(green.getVisibility(), View.VISIBLE);
                assertEquals(red.getVisibility(), View.INVISIBLE);
                assertEquals(timeout.getText().toString(), "");

                scenario.moveToState(Lifecycle.State.STARTED);

                assertEquals(green.getVisibility(), View.VISIBLE);
                assertEquals(red.getVisibility(), View.INVISIBLE);
                assertEquals(timeout.getText().toString(), "");
            });
        }
    }

//    @Test
//    public void testGPSStart() {
//        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
//            scenario.onActivity(activity -> {
//                LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
//                LocationService locationService = new LocationService(activity);
//                ImageView green = activity.findViewById(R.id.green_dot);
//                ImageView red = activity.findViewById(R.id.red_dot);
//                TextView timeout = activity.findViewById(R.id.timeout);
//
//                assertEquals(green.getVisibility(), View.VISIBLE);
//                assertEquals(red.getVisibility(), View.INVISIBLE);
//                assertEquals(timeout.getText().toString(), "");
//
//                locationService.ProviderDisabled(LocationManager.GPS_PROVIDER, activity);
//
//                assertEquals(green.getVisibility(), View.INVISIBLE);
//                assertEquals(red.getVisibility(), View.VISIBLE);
//                assertEquals(timeout.getText().toString(), "1m");
//
//            });
//        }
//    }

}
