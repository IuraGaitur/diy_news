package video.paxra.com.videoconverter.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import video.paxra.com.videoconverter.utils.YoutubeApi;


public class YoutubeService extends IntentService {

    private String path;

    public static final String TAG_PATH = "path";

    private String title;

    public static final String TAG_TITLE = "title";

    private String description;

    public static final String TAG_DESC = "description";

    public YoutubeService() {
        super("Youtbe service");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public YoutubeService(String name) {
        super(name);
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        this.path = intent.getExtras().getString(TAG_PATH);
        this.title = intent.getExtras().getString(TAG_TITLE);
        this.description = intent.getExtras().getString(TAG_DESC);
        super.onStartCommand(intent, startId, startId);
        return START_NOT_STICKY;

    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        try {
            this.path = this.path.replace("file://", "");
            InputStream inputStream = new FileInputStream(new File(this.path));
            YoutubeApi.uploadVideo(inputStream, this.title, this.description);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
