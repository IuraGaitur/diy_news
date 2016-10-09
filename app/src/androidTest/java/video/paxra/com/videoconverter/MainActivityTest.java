package video.paxra.com.videoconverter;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;
import android.widget.EditText;

import video.paxra.com.videoconverter.activities.MainActivity;

/**
 * Created by iura on 8/27/16.
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {
    public MainActivityTest() {
        super(MainActivity.class);
    }


    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }
    @SmallTest
    public void testEditTextThrowNotFound() {
        EditText editText = (EditText)getActivity().findViewById(R.id.edit_text);
        assertNotNull(editText);
    }


    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }


}
