package edu.ucsd.cse110.cse110_team17_project;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.rule.GrantPermissionRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import edu.ucsd.cse110.cse110_team17_project.activity.EnterNameActivity;
import edu.ucsd.cse110.cse110_team17_project.activity.MainActivity;
import edu.ucsd.cse110.cse110_team17_project.view.UserDisplayView;
import edu.ucsd.cse110.cse110_team17_project.viewmodel.Presenter;

@RunWith(RobolectricTestRunner.class)
public class MS2_US6 {

    @Rule
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant("android.permission.ACCESS_FINE_LOCATION");

    @Test
    public void testInitialZoom(){
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(activity -> {
                int zoomSize = activity.getZoomSize();
                assertEquals(1, zoomSize);
            });
        }
    }

    @Test
    public void testZoomIn(){
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(activity -> {
                Button zoomIn_btn = (Button) activity.findViewById(R.id.zoom_in);
                ImageView circle1 = (ImageView) activity.findViewById(R.id.inner_circle1);
                ImageView circle2 = (ImageView) activity.findViewById(R.id.inner_circle2);
                ImageView circle3 = (ImageView) activity.findViewById(R.id.inner_circle3);

                int widthBeforeZoomIn1 = circle1.getWidth();
                int widthBeforeZoomIn2 = circle2.getWidth();
                int widthBeforeZoomIn3 = circle3.getWidth();

                zoomIn_btn.performClick();
                assertTrue(circle1.getWidth() >= widthBeforeZoomIn1);
                assertTrue(circle2.getWidth() >= widthBeforeZoomIn2);
                assertTrue(circle3.getWidth() >= widthBeforeZoomIn3);
            });
        }
    }

    public void testZoomOut(){
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(activity -> {
                Button zoomOut_btn = (Button) activity.findViewById(R.id.zoom_in);
                ImageView circle1 = (ImageView) activity.findViewById(R.id.inner_circle1);
                ImageView circle2 = (ImageView) activity.findViewById(R.id.inner_circle2);
                ImageView circle3 = (ImageView) activity.findViewById(R.id.inner_circle3);

                int widthBeforeZoomIn1 = circle1.getWidth();
                int widthBeforeZoomIn2 = circle2.getWidth();
                int widthBeforeZoomIn3 = circle3.getWidth();

                zoomOut_btn.performClick();
                assertTrue(circle1.getWidth() <= widthBeforeZoomIn1);
                assertTrue(circle2.getWidth() <= widthBeforeZoomIn2);
                assertTrue(circle3.getWidth() <= widthBeforeZoomIn3);
            });
        }
    }
    @Test
    public void testCollison(){
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(activity -> {
                TextView tv1 = activity.findViewById(R.id.label_1);
                TextView tv2 = activity.findViewById(R.id.label_2);

                Presenter pr = new Presenter();
                UserDisplayView position1 = new UserDisplayView(pr, tv1);
                UserDisplayView position2 = new UserDisplayView(pr, tv2);

                position1.angle = 0F;
                position2.angle = 0F;
                position1.radius = 0;
                position2.radius = 0;

                position1.calculateCollisions();
                position2.calculateCollisions();
                assertNotEquals(position1.radius, position2.radius);
                assertEquals(position1.angle, 0F, 0.01);
                assertEquals(position2.angle, 0F, 0.01);

            });
        }
    }
}
