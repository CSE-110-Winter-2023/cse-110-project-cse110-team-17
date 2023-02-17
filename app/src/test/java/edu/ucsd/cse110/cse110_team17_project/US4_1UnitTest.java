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
    public void testCoordinateEntered(){
        String lessThan6 = "0.0001,12.345";
        Pair<Double, Double>p1 = new Pair<>(0.0001, 12.345);
        assertEquals(p1, Utilities.validCoordinate(lessThan6));

        String moreThan6 = "0.1234567,12.3450908";
        Pair<Double, Double>p2 = new Pair<>(0.123457, 12.345091);
        assertEquals(p2, Utilities.validCoordinate(moreThan6));

        String manyZero = "0.0000000,0.00000000000";
        Pair<Double, Double>p3 = new Pair<>(0.0, 0.0);
        assertEquals(p3, Utilities.validCoordinate(manyZero));

        String largeNum = "12345,9808";
        Pair<Double, Double>p4 = null;
        assertEquals(p4, Utilities.validCoordinate(largeNum));

        String smallNum = "0.000000000002,0.00000000000156";
        Pair<Double, Double>p5 = new Pair<>(0.0, 0.0);
        assertEquals(p5, Utilities.validCoordinate(smallNum));

        String negativeInput = "-30.000000000002,-78.00000890";
        Pair<Double, Double>p6 = new Pair<>(-30.0, -78.000009);
        assertEquals(p6, Utilities.validCoordinate(negativeInput));

        String negativeLatiPosLong = "-10.5349849,20.80840909";
        Pair<Double, Double>p7 = new Pair<>(-10.534985, 20.808409);
        assertEquals(p7, Utilities.validCoordinate(negativeLatiPosLong));

        String posLatiNegativeLong = "79,-59.900932";
        Pair<Double, Double>p8 = new Pair<>(79.0, -59.900932);
        assertEquals(p8, Utilities.validCoordinate(posLatiNegativeLong));

        String LatiOutRange = "100000,10";
        Pair<Double, Double>p9 = null;
        assertEquals(p9, Utilities.validCoordinate(LatiOutRange));

        String LongOutRange = "10,-100000";
        Pair<Double, Double>p10 = null;
        assertEquals(p10, Utilities.validCoordinate(LongOutRange));

        String notNumberLatitude = "abc,23";
        Pair<Double, Double>p11 = null;
        assertEquals(p11, Utilities.validCoordinate(notNumberLatitude));

        String notNumberLongitude = "23,abc";
        Pair<Double, Double>p12 = null;
        assertEquals(p12, Utilities.validCoordinate(notNumberLongitude));

        String notNumber = "aa, bb";
        Pair<Double, Double>p13 = null;
        assertEquals(p13, Utilities.validCoordinate(notNumber));
    }

}
