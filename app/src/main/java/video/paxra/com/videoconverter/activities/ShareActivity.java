package video.paxra.com.videoconverter.activities;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.facebook.appevents.AppEventsLogger;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.Optional;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerSimple;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import io.fabric.sdk.android.Fabric;
import video.paxra.com.videoconverter.R;
import video.paxra.com.videoconverter.models.VideoInfoPersistor;
import video.paxra.com.videoconverter.service.YoutubeService;
import video.paxra.com.videoconverter.utils.AssetUtil;
import video.paxra.com.videoconverter.utils.FirebaseUtil;
import video.paxra.com.videoconverter.views.VideoPlayer;

public class ShareActivity extends AppCompatActivity {

    @Optional @InjectView(R.id.shareVideoView) JCVideoPlayerStandard mVideoView;
    @Optional @InjectView(R.id.back_btn) ImageView mBackImageView;
    @Optional @InjectView(R.id.check_upload) CheckBox mCheckUploadView;
    AppEventsLogger logger;
    String fileOutPath = "";
    private boolean mVideoWasSaved;
    private long duration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        fileOutPath = getIntent().getStringExtra(MenuActivity.TAG_FILE_URI);
        duration = getIntent().getLongExtra(CropActivity.TAG_START_POS, 0);
        Fabric.with(this, new Crashlytics());
        ButterKnife.inject(this);
        setVideoUrl(fileOutPath);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        logger = AppEventsLogger.newLogger(this);
        FirebaseUtil.logShare(this);
    }


    private void setVideoUrl(String videoUrl) {
        Log.d("Video url", videoUrl);
        if(videoUrl != null && mVideoView != null) {
            mVideoView.setUp(videoUrl, JCVideoPlayerStandard.SCREEN_LAYOUT_LIST, "");
        }
    }

    @Optional @OnClick(R.id.btn_exit)
    public void exit(View view) {
        logger.logEvent("EXIT_APPLICATION");
        FirebaseUtil.logQuit(this);
        Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("EXIT", true);
        startActivity(intent);
    }


    @Optional @OnClick(R.id.btn_share)
    public void share(View view) {
        logger.logEvent("SHARE_VIDEO");
        shareVideo(this, fileOutPath);
        uploadOnYoutbe(this, fileOutPath);
        FirebaseUtil.logShareApp(this);
    }


    @Optional @OnClick(R.id.btn_save)
    public void saveVideo(View view) {
        logger.logEvent("SAVE_VIDEO");
        FirebaseUtil.logSave(this);
        showSaveDialog();
        uploadOnYoutbe(this, fileOutPath);
    }


    private void insertFileInMediaStore(String fileOutPath, String videoName) {
        Log.d("Title", fileOutPath);
        fileOutPath = fileOutPath.replace("file://", "");
        Log.d("Data", fileOutPath);
        ContentResolver cr = this.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(MediaStore.Video.Media.TITLE, videoName);
        values.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");
        values.put(MediaStore.Video.Media.DATA, fileOutPath);
        values.put(MediaStore.Video.Media.DURATION, duration);
        Uri uri = cr.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
        Toast.makeText(this, "Salvare completa", Toast.LENGTH_SHORT).show();

    }

    private void showSaveDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Nume");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        // Set up the buttons
        builder.setPositiveButton("Salvare", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = input.getText().toString();
                mVideoWasSaved = true;
                insertFileInMediaStore(fileOutPath, name);
            }
        });
        builder.setNegativeButton("Anulare", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void shareVideo(Context context, String fileName) {
        Log.d("FileName", fileName);
        Uri uri = Uri.parse(fileName);
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("video/mp4");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "#DIY News \n");
        sharingIntent.putExtra(android.content.Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(sharingIntent, "share:"));

    }b

    @Optional @OnClick(R.id.back_btn)
    public void backBtnClick(View view) {
        onBackPressed();
    }

    @Override
    public void onPause() {
        super.onPause();
        mVideoView.release();
        VideoPlayer.releaseAllVideos();
    }

    @Override
    public void onBackPressed()
    {
        if (mVideoView.backPress()) {
            return;
        }
        super.onBackPressed();  // optional depending on your needs
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(!mVideoWasSaved && !mCheckUploadView.isChecked()) {
            removeTemporaryVideo(fileOutPath);
        }
    }

    private void removeTemporaryVideo(String path) {
        try {
            AssetUtil.removeTemporaryFile(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void uploadOnYoutbe(Context context, String fileOutPath) {
        if(mCheckUploadView.isChecked()) {
            logger.logEvent("SAVE YOUTUBE");
            FirebaseUtil.logYoutube(this);
            Intent intent = new Intent(context, YoutubeService.class);
            intent.putExtra(YoutubeService.TAG_PATH, fileOutPath);
            intent.putExtra(YoutubeService.TAG_TITLE, VideoInfoPersistor.title);
            intent.putExtra(YoutubeService.TAG_DESC, VideoInfoPersistor.title);
            startService(intent);
        }


    }
}
