package video.paxra.com.videoconverter;

import android.app.Application;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import io.fabric.sdk.android.Fabric;

/**
 * Created by iuriegaitur on 11/26/16.
 */

public class VideoConverterApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        Log.d("Facebook", "Started");
    }
}
