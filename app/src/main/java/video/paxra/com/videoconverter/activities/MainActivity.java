package video.paxra.com.videoconverter.activities;

import android.os.Bundle;


import android.support.v7.app.AppCompatActivity;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;
import video.paxra.com.videoconverter.R;
import video.paxra.com.videoconverter.fragments.VideoFragment;


public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_VIDEO_CAPTURE = 700;
    private static final int REQUEST_FILE_PICKER = 700;
    private String filePath = "";
    private String fileOutPath = "";
    private VideoFragment mVideoFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String uri = getIntent().getExtras().getString(MenuActivity.TAG_FILE_URI, "");
        mVideoFragment = VideoFragment.newInstance(uri);
        getFragmentManager().beginTransaction().add(R.id.videoFragment, mVideoFragment).commit();
        ButterKnife.inject(this);
        /*ObjectGraph.create(new DaggerDependencyModule(this)).inject(this);*/
        /*initActionBar();
        initConverter(this);*/
    }

    @Override
    public void onBackPressed() {
        mVideoFragment.onBackPressed();
        super.onBackPressed();

    }

    @OnClick(R.id.back)
    public void backBtnClick(View view) {
        onBackPressed();
    }

    /* public void setImportantQuestion(TextView view) {
        String text = view.getText().toString() + " *";
        Spannable wordtoSpan = new SpannableString(text);
        int start = text.indexOf('*');
        wordtoSpan.setSpan(new ForegroundColorSpan(Color.BLACK), 0, start, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        wordtoSpan.setSpan(new ForegroundColorSpan(Color.RED), start, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        view.setText(wordtoSpan);

    }*/
}
