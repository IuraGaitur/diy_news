package video.paxra.com.videoconverter.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.Optional;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import video.paxra.com.videoconverter.R;
import video.paxra.com.videoconverter.utils.TimeUtil;
import video.paxra.com.videoconverter.views.VideoTimelineView;

public class CropActivity extends AppCompatActivity {

    @InjectView(R.id.timelineview) VideoTimelineView mVideoTimelineView;
    @InjectView(R.id.videoview) JCVideoPlayerStandard mVideoView;
    @InjectView(R.id.img_next) ImageView mNextImageView;
    @InjectView(R.id.text_next) TextView mNextTextView;
    @InjectView(R.id.text_from_sec) TextView mFromSecTextView;
    @InjectView(R.id.text_to_sec) TextView mToSecTextView;
    @InjectView(R.id.back_btn) ImageView mBackImageView;
    String path;

    public static final String TAG_START_POS = "start";
    public static final String TAG_END_POS = "end";
    public static final String TAG_WIDTH = "width";
    public static final String TAG_HEIGHT = "height";

    private int mStartPos;
    private int mEndPos;
    private int mVideoWidth;
    private int mVideoHeight;

    private long mVideoDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);
        path = getIntent().getStringExtra(MenuActivity.TAG_FILE_URI);
        ButterKnife.inject(this);
        loadVideo(path);
        loadTimelineView(path);
    }

    private void loadVideo(String videoUrl) {
        mVideoView.setUp(videoUrl, JCVideoPlayerStandard.SCREEN_LAYOUT_LIST, "");
    }


    private void loadTimelineView(String videoPath) {
        mVideoTimelineView.setVideoPath(videoPath);

        mVideoDuration = mVideoTimelineView.getVideoLength();
        mToSecTextView.setText(TimeUtil.formatTime(mVideoDuration));
        mVideoWidth = mVideoTimelineView.getVideoWidth();
        mVideoHeight = mVideoTimelineView.getVideoHeight();
        int mVideoRotation = mVideoTimelineView.getVideoRotation();

        if(mVideoRotation == 90 || mVideoRotation == -90) {
            mVideoWidth = mVideoTimelineView.getVideoHeight();
            mVideoHeight = mVideoTimelineView.getVideoWidth();
        }

        Log.d("Information", "Width:" + mVideoWidth +";Height:" + mVideoHeight + ";Rotation:" + mVideoRotation);

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
        intent.putExtra(TAG_WIDTH, mVideoWidth);
        intent.putExtra(TAG_HEIGHT, mVideoHeight);
        intent.putExtra(MenuActivity.TAG_FILE_URI, path);
        Log.d("Start and end", "" + mStartPos + ":" + mEndPos);
        startActivity(intent);

    }
}
