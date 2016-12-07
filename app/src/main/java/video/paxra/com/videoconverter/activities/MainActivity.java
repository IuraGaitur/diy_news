package video.paxra.com.videoconverter.activities;

import android.content.Intent;
import android.os.Bundle;


import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.Optional;
import video.paxra.com.videoconverter.R;
import video.paxra.com.videoconverter.fragments.QuestionsFragment;
import video.paxra.com.videoconverter.fragments.VideoFragment;


public class MainActivity extends AppCompatActivity {

    @InjectView(R.id.back_btn) ImageView mBackImageView;
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

    public static final int REQUEST_TRIM = 400;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        extractBundleData();
        mVideoFragment = VideoFragment.newInstance(fileUri, mVideoWidth, mVideoHeight, mStartVideoPos, mEndVideoPos);
        getFragmentManager().beginTransaction().add(R.id.videoFragment, mVideoFragment).commit();
        mQuestionFragment = QuestionsFragment.newInstance(fileUri, mVideoWidth, mVideoHeight, mStartVideoPos, mEndVideoPos);
        getFragmentManager().beginTransaction().add(R.id.questionFragment, mQuestionFragment).commit();
        /*ObjectGraph.create(new DaggerDependencyModule(this)).inject(this);*/

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
        mVideoFragment.onBackPressed();
        super.onBackPressed();

    }

    @Optional @OnClick(R.id.back_btn)
    public void backBtnClick(View view) {
        onBackPressed();
    }

    public String getFilePath() {
        return filePath;
    }

    /* public void setImportantQuestion(TextView view) {
        String text = view.getText().toString() + " *";
        Spannable wordtoSpan = new SpannableString(text);
        int start = text.indexOf('*');
        wordtoSpan.setSpan(new ForegroundColorSpan(Color.BLACK), 0, start, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        wordtoSpan.setSpan(new ForegroundColorSpan(Color.RED), start, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        view.setText(wordtoSpan);

    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_TRIM) {
            //mQuestionFragment;
            int[] result = data.getIntArrayExtra("data");
            Log.d("Result", result.toString());
        }
    }

}
