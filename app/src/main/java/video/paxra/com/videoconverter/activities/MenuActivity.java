package video.paxra.com.videoconverter.activities;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.crashlytics.android.Crashlytics;
import com.facebook.appevents.AppEventsLogger;
import com.tangxiaolv.telegramgallery.GalleryActivity;
import com.tangxiaolv.telegramgallery.GalleryConfig;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import droidninja.filepicker.FilePickerBuilder;
import io.fabric.sdk.android.Fabric;
import video.paxra.com.videoconverter.R;
import video.paxra.com.videoconverter.utils.PathUtil;
import video.paxra.com.videoconverter.views.PulsatorLayout;

public class MenuActivity extends AppCompatActivity {

    private static final int REQUEST_VIDEO_CAPTURE = 700;
    private static final int REQUEST_FILE_PICKER = 701;
    private String filePath = "";
    private String fileOutPath = "";
    AppEventsLogger logger;

    public final static String TAG_FILE_URI = "uri";

    @InjectView(R.id.action_import) ImageView mIvImport;
    @InjectView(R.id.action_record) ImageView mIvRecord;
    @InjectView(R.id.pulsator1) PulsatorLayout pulsatorLayoutFirstView;
    @InjectView(R.id.pulsator2) PulsatorLayout pulsatorLayoutSecondView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Fabric.with(this, new Crashlytics());

        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }
        ButterKnife.inject(this);
        pulsatorLayoutFirstView.start();
        pulsatorLayoutSecondView.start();
        logger = AppEventsLogger.newLogger(this);
    }



    @OnClick(R.id.action_import)
    public void actionImportClick(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        logger.logEvent("IMPORT_VIDEO_SELECTED");
        startActivityForResult(intent, REQUEST_FILE_PICKER);
    }

    @OnClick(R.id.action_record)
    public void actionRecordClick(View view) {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            logger.logEvent("RECORD_VIDEO_SELECTED");
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri videoUri = data.getData();
        filePath = PathUtil.getRealPathFromURI(this, videoUri);
        if (resultCode == RESULT_OK && requestCode == REQUEST_VIDEO_CAPTURE) {
            if(filePath == null) {
                filePath = videoUri.getPath().toString();
            }
            Log.d("Uri file", filePath.toString());

            Intent intent = new Intent(MenuActivity.this, CropActivity.class);
            intent.putExtra(TAG_FILE_URI, filePath);
            startActivity(intent);
        }else if(resultCode == RESULT_OK && requestCode == REQUEST_FILE_PICKER) {
            Intent intent = new Intent(MenuActivity.this, CropActivity.class);
            intent.putExtra(TAG_FILE_URI, filePath);
            startActivity(intent);
        }
    }
}
