package video.paxra.com.videoconverter.activities;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.FFmpegLoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import video.paxra.com.videoconverter.R;
import video.paxra.com.videoconverter.fragments.QuestionsFragment;
import video.paxra.com.videoconverter.models.Answer;
import video.paxra.com.videoconverter.utils.CommandsFFMpegUtil;
import video.paxra.com.videoconverter.utils.FFMpegUtils;
import video.paxra.com.videoconverter.views.RingProgressBar;

public class ConvertActivity extends AppCompatActivity implements Convertable {

    @InjectView(R.id.progress_bar_2) RingProgressBar mPgbView;
    @InjectView(R.id.btn_cancel) Button mCancelBtnView;
    String outputFileName;
    private double mTotalFPS = 0;
    String fileName = "";
    ArrayList<Answer> answers = new ArrayList<>();
    String[] commands = {};
    private int mStartVideoPos = 0;
    private int mEndVideoPos = 0;
    private int mVideoWidth = 0;
    private int mVideoHeight = 0;

    private Handler mConvertHandler;
    private Runnable mConvertRunnable = new Runnable() {
        @Override
        public void run() {
            FFMpegUtils.convertVideo(ConvertActivity.this, ConvertActivity.this, commands);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_converter);
        ButterKnife.inject(this);
        answers = (ArrayList<Answer>) getIntent().getSerializableExtra(QuestionsFragment.TAG_ANSWERS);
        fileName = getIntent().getStringExtra(QuestionsFragment.TAG_FILE);
        mStartVideoPos = getIntent().getExtras().getInt(CropActivity.TAG_START_POS, 0);
        mEndVideoPos = getIntent().getExtras().getInt(CropActivity.TAG_END_POS, 0);
        mVideoWidth = getIntent().getExtras().getInt(CropActivity.TAG_WIDTH, 0);
        mVideoHeight = getIntent().getExtras().getInt(CropActivity.TAG_HEIGHT, 0);
        initializeConvertingLibrary(this);
    }


    public void gotoFinishLayer() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(ConvertActivity.this, ShareActivity.class);
                intent.putExtra(MenuActivity.TAG_FILE_URI, outputFileName);
                startActivity(intent);
            }
        }, 2000);
    }


    @Override
    public void onStartConvert() {

    }

    @Override
    public void onProgressConvert(String message) {
        Pattern pattern = Pattern.compile("frame= (.{1,12}) fps");
        Matcher matcher = pattern.matcher(message);
        while (matcher.find())
        {
            double frames = Double.parseDouble(matcher.group(1).trim());
            double percentage = (frames / mTotalFPS) * 100;
            mPgbView.setProgress((int)percentage);
            Log.d("Message", "Percents:" + percentage);

        }
        Log.d("Message", message);
    }

    @Override
    public void onFailureConvert(String message) {
        Log.d("Failure", message);
    }

    @Override
    public void onSuccessConvert(String message) {
        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();
        values.put(MediaStore.Video.Media.TITLE, "Title1");
        values.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");
        values.put(MediaStore.Video.Media.DATA, outputFileName);
        Uri uri = cr.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
        Log.d("Success", message);
    }

    @OnClick(R.id.btn_cancel)
    public void cancelConverting(View view) {
        super.onBackPressed();
        mConvertHandler.removeCallbacks(mConvertRunnable);
    }

    @Override
    public void onFinishConvert() {
        gotoFinishLayer();
    }

    public void initializeConvertingLibrary(Context context) {
        try {
            FFmpeg.getInstance(context).loadBinary(new FFmpegLoadBinaryResponseHandler() {
                @Override
                public void onFailure() {
                    Log.d("Init FFMPEG", "Failed");
                }

                @Override
                public void onSuccess() {
                    Log.d("Init FFMPEG", "Success");
                }

                @Override
                public void onStart() {
                }

                @Override
                public void onFinish() {
                    initializeConvertingVideo(ConvertActivity.this, answers, fileName);
                }
            });
        } catch (FFmpegNotSupportedException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG);
            e.printStackTrace();
        }
    }

    public void initializeConvertingVideo(Context context, ArrayList<Answer> answers, String fileName) {
        String image = getExternalFilesDir(null) + "/icon_last.png";;
        String fontFile = this.getExternalFilesDir(null) + "/font.ttf";
        // Generate random file name for video
        outputFileName = fileName.split("\\.")[fileName.split("\\.").length - 1 ];
        String pattern = "MM_dd_yyyy";
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        String date = format.format(new Date());
        Random r = new Random();
        int number = r.nextInt(1000) ;

        String replacer = "_" + date + number+ "." + outputFileName;
        outputFileName = fileName.replace("."+ outputFileName, replacer);

        commands = CommandsFFMpegUtil.buildCommand(fileName, outputFileName, answers, fontFile, image, mVideoWidth, mVideoHeight, mStartVideoPos, mEndVideoPos);
        getInfoAboutVideo(this, fileName);
    }

    public void getInfoAboutVideo(Context context, final String fileName) {
        FFMpegUtils.getInfo(context, new Convertable() {
            @Override
            public void onStartConvert() {

            }

            @Override
            public void onProgressConvert(String message) {

            }

            @Override
            public void onFailureConvert(String message) {
                if(message.contains("Total FPS")) {
                    mTotalFPS = Double.parseDouble(message.split(":")[1]);
                }

            }

            @Override
            public void onSuccessConvert(String message) {

            }

            @Override
            public void onFinishConvert() {
                mConvertHandler = new Handler();
                mConvertHandler.post(mConvertRunnable);
            }
        }, fileName);
    }


}
