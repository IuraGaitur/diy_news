package video.paxra.com.videoconverter.activities;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.analytics.FirebaseAnalytics;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import video.paxra.com.videoconverter.R;
import video.paxra.com.videoconverter.utils.AndroidUtilities;
import video.paxra.com.videoconverter.utils.FirebaseUtil;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        AndroidUtilities.checkDisplaySize(this, this.getResources().getConfiguration());

        FirebaseUtil.logSplash(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashScreenActivity.this, MenuActivity.class));
                finish();
            }
        }, 2000);

    }
}
