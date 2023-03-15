package edu.ucsd.cse110.cse110_team17_project;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.ucsd.cse110.cse110_team17_project.activity.UIDActivity;


@RunWith(RobolectricTestRunner.class)
public class AddFriendsActivityTest {


    @Test
    public void testParseFriendsListString() {
        String beforeParse1 = "fbvrrvm65y6rhbwld9-rln4x2d70zzysth0ib-fsoessa9tlgw16yg5v-ke8omty5lepfe2uhcx-m7mpvbqq3jfdtcuads";
        String[] array = {"fbvrrvm65y6rhbwld9", "rln4x2d70zzysth0ib", "fsoessa9tlgw16yg5v", "ke8omty5lepfe2uhcx", "m7mpvbqq3jfdtcuads"};

        List<String> afterParse1 = Arrays.asList(array);

        String emptyCase = "";

        assertEquals(afterParse1, Utilities.parseFriendListString(beforeParse1));
        assertTrue(Utilities.parseFriendListString(emptyCase).isEmpty());
    }

}
