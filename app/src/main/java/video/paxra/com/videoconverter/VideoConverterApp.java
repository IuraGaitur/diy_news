package video.paxra.com.videoconverter;

import android.app.Application;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import io.fabric.sdk.android.Fabric;

/**
 * Created by iuriegaitur on 11/26/16.
 */
//@SecureConfigurations(
//        useAesRandomly = true,
//        certificateSignature = "1501784074"
//)
public class VideoConverterApp extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        Log.d("Facebook", "Started");
        //SecureEnvironment.initialize(this);

        //@SecureKey(key = "token", value = "???");
    }
}
