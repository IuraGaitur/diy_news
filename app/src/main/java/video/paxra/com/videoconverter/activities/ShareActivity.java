package video.paxra.com.videoconverter.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import butterknife.OnClick;
import video.paxra.com.videoconverter.R;

public class ShareActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
    }

    @OnClick(R.id.exit_btn)
    public void exit(View view) {
        onBackPressed();
    }
    /*@OnClick(R.id.share_btn)
    public void share(View view) {
        shareVideo(this, fileOutPath);
    }

    @OnClick(R.id.edit_btn)
    public void editVideo(View view) {
        mLvToolTipCreateView.setVisibility(View.VISIBLE);
        mTxtLvFieldsView.setVisibility(View.VISIBLE);
        //mFlvVideoView.setVisibility(View.VISIBLE);
    }


    private void insertFileInMediaStore(String fileOutPath) {
        ContentResolver cr = this.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(MediaStore.Video.Media.TITLE, "File");
        values.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");
        values.put(MediaStore.Video.Media.DATA, fileOutPath);
        cr.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);

    }

    public void shareVideo(Context context, String fileName) {

        ContentValues content = new ContentValues(4);
        content.put(MediaStore.Video.VideoColumns.DATE_ADDED,
                System.currentTimeMillis() / 1000);
        content.put(MediaStore.Video.Media.TITLE, fileName.split("/")[fileName.split("/").length-1]);
        content.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");
        content.put(MediaStore.Video.Media.DATA, fileName);
        ContentResolver resolver = getBaseContext().getContentResolver();
        Uri uri = resolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, content);

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("video*//*");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "#DIY News \n");
        sharingIntent.putExtra(android.content.Intent.EXTRA_STREAM,uri);
        startActivity(Intent.createChooser(sharingIntent, "share:"));

    }




    */


}
