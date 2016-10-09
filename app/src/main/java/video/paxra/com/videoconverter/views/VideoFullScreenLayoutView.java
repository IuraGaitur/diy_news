/*
package video.paxra.com.videoconverter.views;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import video.paxra.com.videoconverter.R;


public class VideoFullScreenLayoutView extends VideoFullScreen implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, MediaPlayer.OnPreparedListener, View.OnTouchListener {

    */
/**
     * Log cat TAG name
     *//*

    private final static String TAG = "FullscreenVideoLayout";

    // Control views
    protected View videoControlsView;
    protected SeekBar seekBar;
    protected ImageButton imgplay;
    protected ImageButton imgfullscreen;
    protected TextView textTotal, textElapsed;

    protected View.OnTouchListener touchListener;

    protected IPlayCompletion completionListener;

    protected Animation animShow, animHide;


    // Counter
    protected static final Handler TIME_THREAD = new Handler();
    protected Runnable updateTimeRunnable = new Runnable() {
        public void run() {
            updateCounter();

            TIME_THREAD.postDelayed(this, 200);
        }
    };

    public VideoFullScreenLayoutView(Context context) {
        super(context);
    }

    public VideoFullScreenLayoutView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VideoFullScreenLayoutView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init() {
        Log.d(TAG, "init");

        super.init();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        this.videoControlsView = inflater.inflate(R.layout.view_videocontrols, null);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(ALIGN_PARENT_BOTTOM);
//        videoControlsView.setLayoutParams(params);
//        videoControlsView.setVisibility(View.VISIBLE);
        addView(videoControlsView, params);

        this.seekBar = (SeekBar) this.videoControlsView.findViewById(R.id.vcv_seekbar);
        this.imgfullscreen = (ImageButton) this.videoControlsView.findViewById(R.id.vcv_img_fullscreen);
        this.imgplay = (ImageButton) this.videoControlsView.findViewById(R.id.vcv_img_play);
        this.textTotal = (TextView) this.videoControlsView.findViewById(R.id.vcv_txt_total);
        this.textElapsed = (TextView) this.videoControlsView.findViewById(R.id.vcv_txt_elapsed);

        // We need to add it to show/hide the controls
        super.setOnTouchListener(this);

        this.imgplay.setOnClickListener(this);
        this.imgfullscreen.setOnClickListener(this);
        this.seekBar.setOnSeekBarChangeListener(this);

        // Start controls invisible. Make it visible when it is prepared
        this.videoControlsView.setVisibility(View.INVISIBLE);

        initAnimation();
    }

    protected void startCounter() {
        Log.d(TAG, "startCounter");

        TIME_THREAD.postDelayed(updateTimeRunnable, 200);
    }

    protected void stopCounter() {
        Log.d(TAG, "stopCounter");

        TIME_THREAD.removeCallbacks(updateTimeRunnable);
    }

    protected void updateCounter() {
        int elapsed = getCurrentPosition();
        // getCurrentPosition is a little bit buggy :(
        if (elapsed > 0 && elapsed < getDuration()) {
            seekBar.setProgress(elapsed);
            if(getDuration() - elapsed < 1000) {
                if(completionListener != null) {
                    completionListener.playNextVideo();
                }*/
/*else {
                    stop();
                }*//*

            }

            elapsed = Math.round(elapsed / 1000.f);
            long s = elapsed % 60;
            long m = (elapsed / 60) % 60;
            long h = (elapsed / (60 * 60)) % 24;

            if (h > 0)
                textElapsed.setText(String.format("%d:%02d:%02d", h, m, s));
            else
                textElapsed.setText(String.format("%02d:%02d", m, s));

        }
    }

    @Override
    public void setOnTouchListener(View.OnTouchListener l) {
        touchListener = l;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.d(TAG, "onCompletion");
        pause();
        pause();
        imgplay.setBackground(context.getResources().getDrawable(R.drawable.ic_media_play));
        //start();
        //stop();
        //reset();
        //stopCounter();
        //updateControls();
        //super.onCompletion(mp);
        //stopCounter();
        //updateControls();
        //updateCounter();
        */
/*updateControls();
        ((AppCompatActivity)context).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //check if needs to play next video
        if(completionListener != null) {
            completionListener.playNextVideo();
        }*//*

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        boolean result = super.onError(mp, what, extra);
        stopCounter();
        updateControls();
        Log.d("VideoController","Error");
        return result;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (getCurrentState() == State.END) {
            Log.d(TAG, "onDetachedFromWindow END");
            stopCounter();
            ((AppCompatActivity)context).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

    @Override
    protected void tryToPrepare() {
        Log.d(TAG, "tryToPrepare");
        super.tryToPrepare();

        if (getCurrentState() == State.PREPARED || getCurrentState() == State.STARTED) {
            int total = getDuration();
            if (total > 0) {
                seekBar.setMax(total);
                seekBar.setProgress(0);

                total = total / 1000;
                long s = total % 60;
                long m = (total / 60) % 60;
                long h = (total / (60 * 60)) % 24;
                if (h > 0) {
                    textElapsed.setText("00:00:00");
                    textTotal.setText(String.format("%d:%02d:%02d", h, m, s));
                } else {
                    textElapsed.setText("00:00");
                    textTotal.setText(String.format("%02d:%02d", m, s));
                }
            }

            videoControlsView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void start() throws IllegalStateException {
        Log.d(TAG, "start");

        if (!isPlaying()) {
            super.start();
            startCounter();
            updateControls();
            ((AppCompatActivity)context).getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

    @Override
    public void pause() throws IllegalStateException {
        Log.d(TAG, "pause");

        if (isPlaying()) {
            stopCounter();
            super.pause();
            updateControls();
            ((AppCompatActivity)context).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

    @Override
    public void reset() {
        Log.d(TAG, "reset");
        super.reset();
        stopCounter();
        updateControls();
        ((AppCompatActivity)context).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void stop() throws IllegalStateException {
        Log.d(TAG, "stop");
        super.stop();
        stopCounter();
        updateControls();
        ((AppCompatActivity)context).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    protected void updateControls() {
        Drawable icon;
        if (getCurrentState() == State.STARTED) {
            icon = context.getResources().getDrawable(R.drawable.ic_media_pause);
        } else {
            icon = context.getResources().getDrawable(R.drawable.ic_media_play);
        }
        imgplay.setBackground(icon);
    }

    public void hideControls() {
        Log.d(TAG, "hideControls");
        if (videoControlsView != null) {
            videoControlsView.startAnimation(animHide);
            videoControlsView.setVisibility(View.INVISIBLE);
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        }
    }

    public void showControls() {
        Log.d(TAG, "showControls");
        if (videoControlsView != null) {
            videoControlsView.setVisibility(View.VISIBLE);
            videoControlsView.startAnimation(animShow);
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (videoControlsView != null) {
                if (videoControlsView.getVisibility() == View.VISIBLE) {
                    hideControls();
                } else {
                    showControls();
                }
            }
        }

        if (touchListener != null) {
            return touchListener.onTouch(VideoFullScreenLayoutView.this, event);
        }

        return false;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.vcv_img_play) {
            if (isPlaying()) {
                pause();
            } else {
                start();
            }
        } else if(v.getId() == R.id.vcv_img_fullscreen){
            //isFullScreenByRotate = false;
            rotateVideo();
        }
    }

    public void rotateVideo() {
        if (isPlaying()) {
            pause();
            swithFullScreen();
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    start();
                }
            });
        } else {
            swithFullScreen();
        }
    }

    public void swithFullScreen() {
        if(isFullScreen()) {
            ((ImageView) videoControlsView.findViewById(R.id.vcv_img_fullscreen)).setBackground(getResources().getDrawable(R.drawable.ic_fullscreen_alt));
        }else {
            ((ImageView) videoControlsView.findViewById(R.id.vcv_img_fullscreen)).setBackground(getResources().getDrawable(R.drawable.ic_fullscreen_exit_alt));
        }
        fullscreen();
    }


    */
/**
     * SeekBar Listener
     *//*


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        stopCounter();
        Log.d(TAG, "onStartTrackingTouch");

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        int progress = seekBar.getProgress();
        seekTo(progress);
        Log.d(TAG, "onStopTrackingTouch");

    }

    public boolean isFullScreen() {
        return isFullscreen;
    }

    private void initAnimation()
    {
        animShow = AnimationUtils.loadAnimation(context, R.anim.view_show);
        animHide = AnimationUtils.loadAnimation(context, R.anim.view_hide);
    }

    public void setOnCompletion(IPlayCompletion completionListener) {
        this.completionListener = completionListener;
    }

    public void setFullscreenButtonState(boolean enabled) {
        if(enabled)
            findViewById(R.id.vcv_img_fullscreen).setVisibility(VISIBLE);
        else
            findViewById(R.id.vcv_img_fullscreen).setVisibility(GONE);
    }


}
*/
