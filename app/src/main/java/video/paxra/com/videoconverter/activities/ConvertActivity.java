package video.paxra.com.videoconverter.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.FFmpegLoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import video.paxra.com.videoconverter.R;
import video.paxra.com.videoconverter.fragments.QuestionsFragment;
import video.paxra.com.videoconverter.models.Answer;
import video.paxra.com.videoconverter.utils.AssetUtil;
import video.paxra.com.videoconverter.utils.CommandsFFMpegUtil;
import video.paxra.com.videoconverter.utils.FFMpegUtils;
import video.paxra.com.videoconverter.views.RingProgressBar;

public class ConvertActivity extends AppCompatActivity implements Convertable {

    @InjectView(R.id.progress_bar_2)
    RingProgressBar mPgbView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_converter);
        ButterKnife.inject(this);
        ArrayList<Answer> answers = (ArrayList<Answer>) getIntent().getSerializableExtra(QuestionsFragment.TAG_ANSWERS);
        String fileName = getIntent().getStringExtra(QuestionsFragment.TAG_FILE);
        initializeConvertingLibrary(this);
        startConvertingVideo(this, answers, fileName, "font.ttf");
    }


    public void gotoFinishLayer() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(ConvertActivity.this, ShareActivity.class);
                startActivity(intent);
            }
        }, 2000);
    }

    public void convertVideo() {

    }

    @Override
    public void onStartConvert() {

    }

    @Override
    public void onProgressConvert() {

    }

    @Override
    public void onFailureConvert() {

    }

    @Override
    public void onSuccessConvert() {

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
                }

                @Override
                public void onSuccess() {
                }

                @Override
                public void onStart() {
                }

                @Override
                public void onFinish() {
                }
            });
        } catch (FFmpegNotSupportedException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG);
            e.printStackTrace();
        }
    }

    public void startConvertingVideo(Context context, ArrayList<Answer> answers, String fileName, String fontFile) {
        CommandsFFMpegUtil.buildCommand(fileName, fileName, answers, fontFile);
        String[] commands = {};
        Uri image = null;
        FFMpegUtils.convertVideo(context, this, image, commands);
    }
}
