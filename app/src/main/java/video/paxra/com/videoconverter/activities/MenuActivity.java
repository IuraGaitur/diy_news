package video.paxra.com.videoconverter.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;
import com.crashlytics.android.Crashlytics;
import com.facebook.appevents.AppEventsLogger;
import io.fabric.sdk.android.Fabric;
import video.paxra.com.videoconverter.R;
import video.paxra.com.videoconverter.utils.FirebaseUtil;
import video.paxra.com.videoconverter.utils.PathUtil;

public class MenuActivity extends AppCompatActivity {

  private static final int REQUEST_VIDEO_CAPTURE = 700;
  private static final int REQUEST_FILE_PICKER = 701;
  public final static String TAG_FILE_URI = "uri";
  private String filePath = "";
  private String fileOutPath = "";
  AppEventsLogger logger;

  @BindView(R.id.action_import) ImageView mIvImport;
  @BindView(R.id.action_record) ImageView mIvRecord;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_menu);
    Fabric.with(this, new Crashlytics());
    FirebaseUtil.logMenu(this);

    if (getIntent().getBooleanExtra("EXIT", false)) {
      finish();
    }
    ButterKnife.bind(this);
    logger = AppEventsLogger.newLogger(this);
  }

  @OnClick(R.id.action_import) public void actionImportClick(View view) {
    Intent intent = new Intent(Intent.ACTION_PICK,
        android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
    logger.logEvent("IMPORT_VIDEO_SELECTED");
    FirebaseUtil.logSelect(this);
    try {
      startActivityForResult(intent, REQUEST_FILE_PICKER);
    }catch (Exception ex) {
      ex.printStackTrace();
      Toast.makeText(this, "Access denied in accesing Gallery", Toast.LENGTH_LONG).show();
    }
  }

  @OnClick(R.id.action_record) public void actionRecordClick(View view) {
    Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
    if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
      logger.logEvent("RECORD_VIDEO_SELECTED");
      FirebaseUtil.logRecord(this);
      startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
    }
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    Uri videoUri = data.getData();
    filePath = PathUtil.getRealPathFromURI(this, videoUri);
    if (resultCode == RESULT_OK && requestCode == REQUEST_VIDEO_CAPTURE) {
      if (filePath == null) {
        filePath = videoUri.getPath().toString();
      }
      Log.d("Uri file", filePath.toString());

      Intent intent = new Intent(MenuActivity.this, CropActivity.class);
      intent.putExtra(TAG_FILE_URI, filePath);
      startActivity(intent);
    } else if (resultCode == RESULT_OK && requestCode == REQUEST_FILE_PICKER) {
      Intent intent = new Intent(MenuActivity.this, CropActivity.class);
      intent.putExtra(TAG_FILE_URI, filePath);
      startActivity(intent);
    }
  }
}
