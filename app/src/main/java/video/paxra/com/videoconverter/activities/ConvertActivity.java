package video.paxra.com.videoconverter.activities;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;
import com.crashlytics.android.Crashlytics;
import com.facebook.appevents.AppEventsLogger;
import io.fabric.sdk.android.Fabric;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import nl.bravobit.ffmpeg.FFmpeg;
import video.paxra.com.videoconverter.R;
import video.paxra.com.videoconverter.fragments.QuestionsFragment;
import video.paxra.com.videoconverter.interfaces.Convertable;
import video.paxra.com.videoconverter.models.Answer;
import video.paxra.com.videoconverter.models.Line;
import video.paxra.com.videoconverter.utils.AssetUtil;
import video.paxra.com.videoconverter.utils.Constants;
import video.paxra.com.videoconverter.utils.FFMpegUtils;
import video.paxra.com.videoconverter.utils.FfmpegCommandBuilder2;
import video.paxra.com.videoconverter.utils.FirebaseUtil;
import video.paxra.com.videoconverter.utils.StringUtils;
import video.paxra.com.videoconverter.views.RingProgressBar;

public class ConvertActivity extends AppCompatActivity implements Convertable {


  private static final int MY_PERMISSIONS_REQUEST_WRITE_FILES = 800;

  @BindView(R.id.progress_bar_2) RingProgressBar mPgbView;
  @BindView(R.id.btn_cancel) Button mCancelBtnView;
  @BindView(R.id.text) TextView textView;

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
    ButterKnife.bind(this);
    Fabric.with(this, new Crashlytics());
    parseExtras();
    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    logScreen();

    initTextViewComponent();
    formatedAnswers(answers);
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
    boolean hasWritePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        == PackageManager.PERMISSION_GRANTED;
    boolean hasReadPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        == PackageManager.PERMISSION_GRANTED;
    if (!hasReadPermission || !hasWritePermission) {
      ActivityCompat.requestPermissions(this,
          new String[] { Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE },
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
    FirebaseUtil.logFailConvert(this, message);
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
    FirebaseUtil.logSuccessConvert(this);
  }

  @OnClick(R.id.btn_cancel) public void cancelConverting(View view) {
    super.onBackPressed();
    if(mConvertHandler != null) {
      mConvertHandler.removeCallbacks(mConvertRunnable);
    }
  }

  @Override public void onBackPressed() {
    super.onBackPressed();
    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
  }

  @Override public void onFinishConvert() {
    gotoFinishLayer();
  }

  public void initializeConvertingLibrary(Context context) {
    if (!FFmpeg.getInstance(context).isSupported()) {
      Toast.makeText(this, "FFMPEG is not supported", Toast.LENGTH_LONG).show();
    } else {
      initializeConvertingVideo(answers, fileName);
    }
  }

  public void initializeConvertingVideo(List<Answer> answers, String fileName) {
    String image = getExternalFilesDir(null) + Constants.ICON_NAME;
    String fontFile = this.getExternalFilesDir(null) + Constants.FONT_NAME;
    outputFileName = StringUtils.generateRandomFromName(fileName);

    //Build commands for converting video
    FfmpegCommandBuilder2 commandBuilder = FfmpegCommandBuilder2.getInstance()
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

  private void initTextViewComponent() {
    textView = (TextView) findViewById(R.id.text);
    int width = mVideoWidth > mVideoHeight ? Constants.VIDEO_WIDTH : Constants.VIDEO_HEIGHT;
    textView.setLayoutParams(
        new RelativeLayout.LayoutParams(width - Constants.MARGIN_PADDING,
            RelativeLayout.LayoutParams.WRAP_CONTENT));
  }

  private void formatedAnswers(final List<Answer> answers) {
    postItem(answers, 0);
  }

  private void postItem(final List<Answer> answers, final int counter) {
    if(answers.isEmpty()) return;

    textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, Constants.TEXT_FONT_SIZE);
    String text = answers.get(counter).answer.replaceAll("_%", " ");
    textView.setText(text);

    textView.post(new Runnable() {
      @Override public void run() {
        int height = textView.getMeasuredHeight();
        answers.get(counter).height = height;
        int lineCount = textView.getLineCount();

        List<Line> data = new ArrayList<Line>();
        for (int i = 0; i < lineCount; i++) {
          int startPos = textView.getLayout().getLineStart(i);
          int endPos = textView.getLayout().getLineEnd(i);
          Rect rect = new Rect();
          textView.getLayout().getLineBounds(i, rect);
          String text = answers.get(counter).getAnswer()
              .replaceAll("_%", " ").toUpperCase()
              .substring(startPos, endPos).replaceAll(" ", "_%");
          int calcHeight = rect.height();
          data.add(new Line(i, calcHeight, text));
        }
        answers.get(counter).splittedText = data;

        if (counter == answers.size() - 1) {
          initializeConvertion();
        } else {
          postItem(answers, counter + 1);
        }
      }
    });
  }
}
