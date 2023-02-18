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

        String smallNum = "0.000000000002,0.00000000000156";
        Pair<Double, Double>p5 = new Pair<>(0.0, 0.0);
        assertEquals(p5, Utilities.validCoordinate(smallNum));

        String negativeInput = "-30.000000000002,-78.00000890";
        Pair<Double, Double>p6 = new Pair<>(-30.0, -78.000009);
        assertEquals(p6, Utilities.validCoordinate(negativeInput));

        String negativeLatPosLong = "-10.5349849,20.80840909";
        Pair<Double, Double>p7 = new Pair<>(-10.534985, 20.808409);
        assertEquals(p7, Utilities.validCoordinate(negativeLatPosLong));

        String posLatNegativeLong = "79,-59.900932";
        Pair<Double, Double>p8 = new Pair<>(79.0, -59.900932);
        assertEquals(p8, Utilities.validCoordinate(posLatNegativeLong));

        String latOutRange = "100000,10";
        assertEquals(null, Utilities.validCoordinate(latOutRange));

        String longOutRange = "10,-100000";
        assertEquals(null, Utilities.validCoordinate(longOutRange));

        String bothOutRange = "12345,9808";
        assertEquals(null, Utilities.validCoordinate(bothOutRange));

        String notNumberLatitude = "abc,23";
        assertEquals(null, Utilities.validCoordinate(notNumberLatitude));

        String notNumberLongitude = "23,abc";
        assertEquals(null, Utilities.validCoordinate(notNumberLongitude));

        String notNumber = "aa, bb";
        assertEquals(null, Utilities.validCoordinate(notNumber));
    }

    @Test
    public void testUpdateCoordinates(){
        double currentLat_northHemisphere = 32;
        double currentLat_southHemisphere = -32;
        double currentLon = -117;
        //small distance results in small bearing angle, so a delta value around 5 degrees is given for testing
        //north, same hemisphere
        double angle = Utilities.updateAngle(currentLat_northHemisphere, currentLon, currentLat_northHemisphere + 0.1, currentLon);
        assertEquals(0.0, angle, 0);
        //south, same hemisphere
        angle = Utilities.updateAngle(currentLat_northHemisphere, currentLon, currentLat_northHemisphere - 0.1, currentLon);
        assertEquals(180.0, angle, 0);
        //east, same hemisphere
        angle = Utilities.updateAngle(currentLat_northHemisphere, currentLon, currentLat_northHemisphere, currentLon + 0.1);
        assertEquals(90.0, angle, 0.05);
        //west, same hemisphere
        angle = Utilities.updateAngle(currentLat_northHemisphere, currentLon, currentLat_northHemisphere, currentLon - 0.1);
        assertEquals(270.0, angle, 0.05);
        //north-east, same hemisphere
        angle = Utilities.updateAngle(currentLat_northHemisphere, currentLon, currentLat_northHemisphere + 0.1, currentLon + 0.1);
        assertEquals(45, angle, 5);
        //north-west, same hemisphere
        angle = Utilities.updateAngle(currentLat_northHemisphere, currentLon, currentLat_northHemisphere + 0.1, currentLon - 0.1);
        assertEquals(315.0, angle, 5);
        //south-east, same hemisphere
        angle = Utilities.updateAngle(currentLat_northHemisphere, currentLon, currentLat_northHemisphere - 0.1, currentLon + 0.1);
        assertEquals(135.0, angle, 5);
        //south-west, same hemisphere
        angle = Utilities.updateAngle(currentLat_northHemisphere, currentLon, currentLat_northHemisphere - 0.1, currentLon - 0.1);
        assertEquals(225.0, angle, 5);

        //large distance results in large bearing angle, so if the angle is only tested against for the correct quadrant
        //north
        angle = Utilities.updateAngle(currentLat_southHemisphere, currentLon, currentLat_northHemisphere, currentLon);
        assertEquals(0.0, angle, 0);
        //south
        angle = Utilities.updateAngle(currentLat_northHemisphere, currentLon, currentLat_southHemisphere, currentLon);
        assertEquals(180.0, angle, 0);
        //east
        angle = Utilities.updateAngle(currentLat_northHemisphere, currentLon, currentLat_northHemisphere, currentLon + 90);
        assertEquals(45, angle, 45);
        //west
        angle = Utilities.updateAngle(currentLat_northHemisphere, currentLon, currentLat_northHemisphere, currentLon - 90);
        assertEquals(315, angle, 45);
        //north-east
        angle = Utilities.updateAngle(currentLat_southHemisphere, currentLon, currentLat_northHemisphere, currentLon + 90);
        assertEquals(45, angle, 45);
        //north-west
        angle = Utilities.updateAngle(currentLat_southHemisphere, currentLon, currentLat_northHemisphere, currentLon - 90);
        assertEquals(315, angle, 45);
        //south-east
        angle = Utilities.updateAngle(currentLat_northHemisphere, currentLon, currentLat_southHemisphere, currentLon + 90);
        assertEquals(135, angle, 45);
        //south-west
        angle = Utilities.updateAngle(currentLat_northHemisphere, currentLon, currentLat_southHemisphere, currentLon - 90);
        assertEquals(225, angle, 45);
    }

}
