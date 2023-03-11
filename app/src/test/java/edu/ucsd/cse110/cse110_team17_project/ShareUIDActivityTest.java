package edu.ucsd.cse110.cse110_team17_project;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import edu.ucsd.cse110.cse110_team17_project.activity.UIDActivity;


@RunWith(RobolectricTestRunner.class)
public class ShareUIDActivityTest {

    private static final String DEFAULT_UID_TEXT = "Generating UID";

    private static final String INVALID_UID_1 = "123456789-12345678";

    private static final String INVALID_UID_2 = "12345678912345";

    private static final String INVALID_UID_3 = "123456-*%9123";

    private static final String INVALID_UID_4 = "12ab56789 123cf678";

    private static final String VALID_UID = "123456789abcDeFghi";

    @Test
    public void testIsValidUID() {
        assertFalse(Utilities.isValidUID(INVALID_UID_1));
        assertFalse(Utilities.isValidUID(INVALID_UID_2));
        assertFalse(Utilities.isValidUID(INVALID_UID_3));
        assertFalse(Utilities.isValidUID(INVALID_UID_4));
        assertTrue(Utilities.isValidUID(VALID_UID));
    }

    @Test
    public void testValidUIDGeneration() {
        try (ActivityScenario<UIDActivity> scenario = ActivityScenario.launch(UIDActivity.class)) {
            scenario.onActivity(activity -> {
                TextView uid = activity.findViewById(R.id.yourID);
                String myUID = uid.getText().toString();

                assertNotEquals(DEFAULT_UID_TEXT, myUID);
                assertTrue(Utilities.isValidUID(myUID));
            });
        }
    }
}
