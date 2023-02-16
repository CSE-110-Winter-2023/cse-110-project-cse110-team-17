package edu.ucsd.cse110.cse110_team17_project;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.content.Intent;
import android.util.Pair;
import android.widget.Button;

import androidx.test.core.app.ActivityScenario;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class US4_1UnitTest {

    @Test
    public void testBackButton(){
        String input = "0.0001,12.345";
        String input2 = "0.1234567,12.3450908";
        Pair<Double, Double>p1 = new Pair<>(0.0001, 12.345);
        Pair<Double, Double>p2 = new Pair<>(0.123457, 12.345091);
        assertEquals(p1, Utilities.validCoordinate(input));
        assertEquals(p2, Utilities.validCoordinate(input2));
    }


}
