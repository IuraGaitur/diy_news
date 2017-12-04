package video.paxra.com.videoconverter.activities;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.crashlytics.android.Crashlytics;
import com.facebook.appevents.AppEventsLogger;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.FFmpegLoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;
import io.fabric.sdk.android.Fabric;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import video.paxra.com.videoconverter.R;
import video.paxra.com.videoconverter.fragments.QuestionsFragment;
import video.paxra.com.videoconverter.interfaces.Convertable;
import video.paxra.com.videoconverter.models.Answer;
import video.paxra.com.videoconverter.utils.AssetUtil;
import video.paxra.com.videoconverter.utils.Constants;
import video.paxra.com.videoconverter.utils.FFMpegUtils;
import video.paxra.com.videoconverter.utils.FfmpegCommandBuilder;
import video.paxra.com.videoconverter.utils.StringUtils;
import video.paxra.com.videoconverter.views.RingProgressBar;

public class ConvertActivity extends AppCompatActivity implements Convertable {


  private static final int MY_PERMISSIONS_REQUEST_WRITE_FILES = 800;

  @InjectView(R.id.progress_bar_2) RingProgressBar mPgbView;
  @InjectView(R.id.btn_cancel) Button mCancelBtnView;

  String fileName;
  String outputFileName;
  String[] commands = {};
  private int mStartVideoPos = 0;
  private int mEndVideoPos = 0;
  private int mVideoWidth = 0;
  private int mVideoHeight = 0;
  private double mTotalFPS = 0;
  List<Answer> answers = new ArrayList<>();
  AppEventsLogger logger;

  private Handler mConvertHandler;
  private Runnable mConvertRunnable = new Runnable() {
    @Override public void run() {
      FFMpegUtils.convertVideo(ConvertActivity.this, ConvertActivity.this, commands);
    }
  };

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_converter);
    ButterKnife.inject(this);
    initializeConvertion();
    Fabric.with(this, new Crashlytics());
    parseExtras();
    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    logScreen();
  }

  private void parseExtras() {
    answers = (List<Answer>) getIntent().getSerializableExtra(QuestionsFragment.TAG_ANSWERS);
    fileName = getIntent().getStringExtra(QuestionsFragment.TAG_FILE);
    mStartVideoPos = getIntent().getExtras().getInt(CropActivity.TAG_START_POS, 0);
    mEndVideoPos = getIntent().getExtras().getInt(CropActivity.TAG_END_POS, 0);
    mVideoWidth = getIntent().getExtras().getInt(CropActivity.TAG_WIDTH, 0);
    mVideoHeight = getIntent().getExtras().getInt(CropActivity.TAG_HEIGHT, 0);
  }

  private void logScreen() {
    logger = AppEventsLogger.newLogger(this);
    logger.logEvent("CONVERT_VIDEO_STARTED");
  }

  private void initializeConvertion() {
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(this,
          new String[] { Manifest.permission.READ_EXTERNAL_STORAGE },
          MY_PERMISSIONS_REQUEST_WRITE_FILES);
    } else {
      AssetUtil.copyAssets(this);
      initializeConvertingLibrary(this);
    }
  }

  @Override public void onRequestPermissionsResult(int requestCode, String permissions[],
      int[] grantResults) {
    switch (requestCode) {
      case MY_PERMISSIONS_REQUEST_WRITE_FILES: {
        // If request is cancelled, the result arrays are empty.
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
          AssetUtil.copyAssets(this);
          initializeConvertingLibrary(this);
        } else {
          Toast.makeText(this, "You need Write permission in order to start converting video",
              Toast.LENGTH_SHORT);
        }
        return;
      }
    }
  }

  public void gotoFinishLayer() {
    logger.logEvent("CONVERT_VIDEO_ENDED");
    new Handler().postDelayed(new Runnable() {
      @Override public void run() {
        Intent intent = new Intent(ConvertActivity.this, ShareActivity.class);
        intent.putExtra(MenuActivity.TAG_FILE_URI, outputFileName);
        intent.putExtra(CropActivity.TAG_START_POS,
            (long) ((mEndVideoPos - mStartVideoPos) * 1000));
        startActivity(intent);
        finish();
      }
    }, 1000);
  }

  @Override public void onStartConvert() {

  }

  @Override public void onProgressConvert(String message) {
    Pattern pattern = Pattern.compile("frame= (.{1,12}) fps");
    Matcher matcher = pattern.matcher(message);
    while (matcher.find()) {
      double frames = Double.parseDouble(matcher.group(1).trim());
      double percentage = (frames / mTotalFPS) * 100;
      mPgbView.setProgress((int) percentage);
    }
  }

  @Override public void onFailureConvert(String message) {
    Toast.makeText(this, message, Toast.LENGTH_LONG);
    logger.logEvent("Error:" + message);
    Log.d("Failure", message);
  }

  @Override public void onSuccessConvert(String message) {
    ContentResolver cr = getContentResolver();
    ContentValues values = new ContentValues();
    values.put(MediaStore.Video.Media.TITLE, "Title1");
    values.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");
    values.put(MediaStore.Video.Media.DATA, outputFileName);
    Uri uri = cr.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
    Log.d("Success", message);
  }

  @OnClick(R.id.btn_cancel) public void cancelConverting(View view) {
    super.onBackPressed();
    mConvertHandler.removeCallbacks(mConvertRunnable);
  }

  @Override public void onBackPressed() {
    super.onBackPressed();
    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
  }

  @Override public void onFinishConvert() {
    gotoFinishLayer();
  }

  public void initializeConvertingLibrary(Context context) {
    try {
      FFmpeg.getInstance(context).loadBinary(new FFmpegLoadBinaryResponseHandler() {
        @Override public void onFailure() {
          Log.d("Init FFMPEG", "Failed");
        }

        @Override public void onSuccess() {
          Log.d("Init FFMPEG", "Success");
        }

        @Override public void onStart() {
        }

        @Override public void onFinish() {
          initializeConvertingVideo(answers, fileName);
        }
      });
    } catch (FFmpegNotSupportedException e) {
      Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG);
      e.printStackTrace();
    }
  }

  public void initializeConvertingVideo(List<Answer> answers, String fileName) {
    String image = getExternalFilesDir(null) + Constants.ICON_NAME;
    String fontFile = this.getExternalFilesDir(null) + Constants.FONT_NAME;
    outputFileName = StringUtils.generateRandomFromName(fileName);

    //Build commands for converting video
    FfmpegCommandBuilder commandBuilder = FfmpegCommandBuilder.getInstance()
        .setInput(fileName)
        .setOutput(outputFileName)
        .setFont(fontFile)
        .setTexts(answers)
        .setImage(image)
        .setWidth(mVideoWidth)
        .setHeight(mVideoHeight)
        .setCropFrom(mStartVideoPos)
        .setCropTo(mEndVideoPos);

    commands = commandBuilder.build();

    getInfoAboutVideo(this, fileName);
    //commands = FfmpegCommandBuilder.buildCommand(fileName, outputFileName, answers, fontFile, image, mVideoWidth, mVideoHeight, mStartVideoPos, mEndVideoPos);
  }

  public void getInfoAboutVideo(Context context, final String fileName) {
    FFMpegUtils.getInfo(context, new Convertable() {
      @Override public void onStartConvert() {

      }

      @Override public void onProgressConvert(String message) {

      }

      @Override public void onFailureConvert(String message) {
        if (message.contains("Total FPS")) {
          mTotalFPS = Double.parseDouble(message.split(":")[1]);
        }
      }

      @Override public void onSuccessConvert(String message) {

      }

      @Override public void onFinishConvert() {
        mConvertHandler = new Handler();
        mConvertHandler.post(mConvertRunnable);
      }
    }, fileName);
  }
}
