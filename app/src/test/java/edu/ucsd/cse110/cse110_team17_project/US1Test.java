package edu.ucsd.cse110.cse110_team17_project;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.*;

import android.view.View;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;


@RunWith(RobolectricTestRunner.class)
public class US1Test {
    @Test
    public void testOnSubmitClicked() {
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(activity -> {
                TextView newTextView = activity.findViewById(R.id.label_name1);
                assertEquals(newTextView.getVisibility(), View.VISIBLE);
                assertEquals("", newTextView.getText().toString());
            });
        }
    }
}