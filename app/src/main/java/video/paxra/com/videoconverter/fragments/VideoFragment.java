package video.paxra.com.videoconverter.fragments;

import android.app.Fragment;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import butterknife.ButterKnife;
import butterknife.InjectView;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import video.paxra.com.videoconverter.R;
import video.paxra.com.videoconverter.activities.MenuActivity;

/**
 * Created by root on 10/14/15.
 */
public class VideoFragment extends Fragment {

    @InjectView(R.id.videoview)
    JCVideoPlayerStandard mVideoView;
    private String videoUrl;


    public VideoFragment() {
    }


    public static VideoFragment newInstance(String videoUrl) {
        VideoFragment fragment = new VideoFragment();
        Bundle bundle = new Bundle();
        Log.d("File", videoUrl);
        bundle.putString(MenuActivity.TAG_FILE_URI, "file://" + videoUrl);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.videoUrl = getArguments().getString(MenuActivity.TAG_FILE_URI);
        Log.d("File", videoUrl);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_video, null);
        ButterKnife.inject(this, root);
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mVideoView.setUp(videoUrl, JCVideoPlayerStandard.SCREEN_LAYOUT_LIST, "");

    }

    @Override
    public void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

    public void setVideoUri(String uri) {
        this.videoUrl = uri;
    }


    public void setOnConfigurationChanged() {

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("Video destroy", "Destroing video");
    }
}
