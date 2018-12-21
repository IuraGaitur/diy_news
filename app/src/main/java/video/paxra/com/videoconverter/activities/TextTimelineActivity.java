package video.paxra.com.videoconverter.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import video.paxra.com.videoconverter.R;
import video.paxra.com.videoconverter.fragments.QuestionsFragment;
import video.paxra.com.videoconverter.utils.TimeUtil;
import video.paxra.com.videoconverter.views.VideoTimelineView;

public class TextTimelineActivity extends AppCompatActivity {

    @BindView(R.id.timelineview) VideoTimelineView mVideoTimelineView;
    @BindView(R.id.videoview) JCVideoPlayerStandard mVideoView;
    @BindView(R.id.img_next) ImageView mNextImageView;
    @BindView(R.id.text_next) TextView mNextTextView;
    @BindView(R.id.text_from_sec) TextView mFromSecTextView;
    @BindView(R.id.text_to_sec) TextView mToSecTextView;
    @BindView(R.id.back_btn) ImageView mBackImageView;
    String path;

    public static final String TAG_START_POS = "start";
    public static final String TAG_END_POS = "end";
    public static final String TAG_WIDTH = "width";
    public static final String TAG_HEIGHT = "height";

    private int mStartPos;
    private int mEndPos;

    private long mVideoDuration;
    private String mQuestionNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_timeline);
        path = getIntent().getStringExtra(MenuActivity.TAG_FILE_URI);
        mQuestionNumber = getIntent().getStringExtra(QuestionsFragment.TAG_QUESTION_NUMBER);
        mStartPos = getIntent().getIntExtra(CropActivity.TAG_WIDTH, 0);
        mEndPos = getIntent().getIntExtra(CropActivity.TAG_HEIGHT, 0);
        ButterKnife.bind(this);
        loadVideo(path);
        loadTimelineView(path);
    }

    private void loadVideo(String videoUrl) {
        Log.d("Path", videoUrl);
        mVideoView.setUp(videoUrl, JCVideoPlayerStandard.SCREEN_LAYOUT_LIST, "");
    }


    private void loadTimelineView(String videoPath) {
        mVideoTimelineView.setVideoPath(videoPath.replace("file:///", ""));

        mVideoDuration = mVideoTimelineView.getVideoLength();
        mToSecTextView.setText(TimeUtil.formatTime(mVideoDuration));


        mVideoTimelineView.setDelegate(new VideoTimelineView.VideoTimelineViewDelegate() {
            @Override
            public void onLeftProgressChanged(float progress) {
                mStartPos = (int) (progress * mVideoDuration);
                mFromSecTextView.setText(TimeUtil.formatTime(progress * mVideoDuration));
            }

            @Override
            public void onRightProgressChanged(float progress) {
                mEndPos = (int) (progress * mVideoDuration);
                mToSecTextView.setText(TimeUtil.formatTime(progress * mVideoDuration));
            }
        });


    }

    @Optional
    @OnClick(R.id.back_btn)
    public void backBtnClick(View view) {
        onBackPressed();
    }

    @OnClick({R.id.img_next, R.id.text_next})
    public void convert(View view) {
        mStartPos = (int) mStartPos / 1000;
        mEndPos = (int) mEndPos / 1000;

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(TAG_START_POS, mStartPos);
        intent.putExtra(TAG_END_POS, mEndPos);
        intent.putExtra(QuestionsFragment.TAG_QUESTION_NUMBER, mQuestionNumber);
        setResult(MainActivity.REQUEST_TRIM, intent);
        finish();
    }
}
