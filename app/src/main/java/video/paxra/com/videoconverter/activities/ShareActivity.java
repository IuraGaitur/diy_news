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
import android.widget.EditText;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.Optional;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerSimple;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import video.paxra.com.videoconverter.R;
import video.paxra.com.videoconverter.views.VideoPlayer;

public class ShareActivity extends AppCompatActivity {

    @Optional @InjectView(R.id.shareVideoView) JCVideoPlayerStandard mVideoView;
    String fileOutPath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        fileOutPath = getIntent().getStringExtra(MenuActivity.TAG_FILE_URI);
        ButterKnife.inject(this);
        setVideoUrl(fileOutPath);
    }


    private void setVideoUrl(String videoUrl) {
        Log.d("Video url", videoUrl);
        if(videoUrl != null && mVideoView != null) {
            mVideoView.setUp(videoUrl, JCVideoPlayerStandard.SCREEN_LAYOUT_LIST, "");
        }
    }

    @OnClick(R.id.btn_exit)
    public void exit(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("Exit me", true);
        startActivity(intent);
        finish();
    }


    @OnClick(R.id.btn_share)
    public void share(View view) {
        shareVideo(this, fileOutPath);
    }

    @OnClick(R.id.btn_save)
    public void saveVideo(View view) {
        showSaveDialog();
    }


    private void insertFileInMediaStore(String fileOutPath, String videoName) {
        Log.d("Title", fileOutPath);
        Log.d("Data", fileOutPath);
        ContentResolver cr = this.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(MediaStore.Video.Media.TITLE, videoName);
        values.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");
        values.put(MediaStore.Video.Media.DATA, fileOutPath);
        cr.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
        Toast.makeText(this, "Salvare completa", Toast.LENGTH_SHORT);

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

    }

    @Override
    public void onPause() {
        super.onPause();
        mVideoView.release();
        VideoPlayer.releaseAllVideos();
    }
}
