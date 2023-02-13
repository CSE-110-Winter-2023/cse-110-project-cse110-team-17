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
public class US2_3_Test {
    Intent intent = new Intent(getApplicationContext(), CompassActivity.class);


    @Test
    public void testBackButtom(){
        intent.putExtra("label_1","1");
        intent.putExtra("label_2","1");
        intent.putExtra("label_3","1");
        ActivityScenario<CompassActivity> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            Button BackButtom = (Button) activity.findViewById(R.id.back_btn);
            BackButtom.performClick();
            assertTrue(activity.isFinishing());
        });
    }
}
