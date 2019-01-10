package video.paxra.com.videoconverter.activities;

import android.content.Intent;
import android.os.Bundle;


import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import android.widget.TextView;
import com.crashlytics.android.Crashlytics;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import io.fabric.sdk.android.Fabric;
import video.paxra.com.videoconverter.R;
import video.paxra.com.videoconverter.fragments.OnConvertCallback;
import video.paxra.com.videoconverter.fragments.QuestionsFragment;
import video.paxra.com.videoconverter.fragments.VideoFragment;

import static video.paxra.com.videoconverter.activities.EditFontActivity.EXTRA_COLOR;
import static video.paxra.com.videoconverter.activities.EditFontActivity.EXTRA_FONT;


public class MainActivity extends AppCompatActivity {

    @BindView(R.id.back_btn) ImageView mBackImageView;
    @BindView(R.id.text_edit) TextView editText;
    private VideoFragment mVideoFragment;
    private QuestionsFragment mQuestionFragment;

    private String fileUri;
    private static final int REQUEST_VIDEO_CAPTURE = 700;
    private static final int REQUEST_FILE_PICKER = 700;
    private String filePath = "";
    private int mStartVideoPos = 0;
    private int mEndVideoPos = 0;
    private int mVideoWidth = 0;
    private int mVideoHeight = 0;

    String font = "font_simple";
    String color = "yellow";

    public static final int REQUEST_TRIM = 400;
    public static final int REQUEST_CHANGE_FONT = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        extractBundleData();
        Fabric.with(this, new Crashlytics());
        mVideoFragment = VideoFragment.newInstance(fileUri, mVideoWidth, mVideoHeight, mStartVideoPos, mEndVideoPos);
        getFragmentManager().beginTransaction().add(R.id.videoFragment, mVideoFragment).commit();
        mQuestionFragment = QuestionsFragment.newInstance(fileUri, mVideoWidth, mVideoHeight, mStartVideoPos, mEndVideoPos);
        getFragmentManager().beginTransaction().add(R.id.questionFragment, mQuestionFragment).commit();

        mQuestionFragment.setConvertCallback(intent -> {
            intent.putExtra(EXTRA_FONT, font);
            intent.putExtra(EXTRA_COLOR, color);
            startActivity(intent);
        });
    }

    public void extractBundleData() {
        fileUri = getIntent().getExtras().getString(MenuActivity.TAG_FILE_URI, "");
        mStartVideoPos = getIntent().getExtras().getInt(CropActivity.TAG_START_POS, 0);
        mEndVideoPos = getIntent().getExtras().getInt(CropActivity.TAG_END_POS, 0);
        mVideoWidth = getIntent().getExtras().getInt(CropActivity.TAG_WIDTH, 0);
        mVideoHeight = getIntent().getExtras().getInt(CropActivity.TAG_HEIGHT, 0);
        Log.d("Start and end", "" + mStartVideoPos + ":" + mEndVideoPos);
    }

    @Override
    public void onBackPressed() {
        if(mVideoFragment.onBackPressed()) {
            return;
        }
        super.onBackPressed();
    }

    @Optional @OnClick(R.id.back_btn)
    public void backBtnClick(View view) {
        onBackPressed();
    }

    @Optional @OnClick(R.id.text_edit)
    public void editFonts() {
        startActivityForResult(new Intent(this, EditFontActivity.class), REQUEST_CHANGE_FONT);
    }

    public String getFilePath() {
        return filePath;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_TRIM) {
            int[] result = data.getIntArrayExtra("data");
            Log.d("Result", result.toString());
        } else if (requestCode == REQUEST_CHANGE_FONT) {
            font = data.getExtras().getString(EXTRA_FONT);
            color = data.getExtras().getString(EXTRA_COLOR);
        }
    }
}
