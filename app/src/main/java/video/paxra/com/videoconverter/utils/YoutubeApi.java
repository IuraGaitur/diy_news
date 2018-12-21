package video.paxra.com.videoconverter.utils;


import android.util.Log;


import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.media.MediaHttpUploader;
import com.google.api.client.googleapis.media.MediaHttpUploaderProgressListener;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeScopes;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoSnippet;
import com.google.api.services.youtube.model.VideoStatus;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class YoutubeApi {

    private static final String VIDEO_FILE_FORMAT = "video/*";

    static String clientId = "785496267169-kiodda9liidpk38jutd82stmv4cel1bp.apps.googleusercontent.com";
    static String clientSecret = "dlC17keZtU5xJgJh0Av8Nyi0";
    static String refreshToken = "1/ot1B_uQ0_j0-im32CV7Mqxx5y3ygb8AyffWMZAwhSwfkPvfqKoVS-jwZt6c5jDyn";


    public static void uploadVideo(InputStream inputStream, String title, String description) {


        HttpTransport transport = new NetHttpTransport();
        JsonFactory jsonFactory = new JacksonFactory();

        GoogleCredential credential = new GoogleCredential.Builder()
                .setTransport(transport)
                .setJsonFactory(jsonFactory)
                .setClientSecrets(clientId, clientSecret)
                .build()
                .setRefreshToken(refreshToken);

        try {
            credential.refreshToken();
        } catch (IOException e) {
            e.printStackTrace();
        }


        YouTube youtube = new YouTube.Builder(transport, jsonFactory, credential).setApplicationName("DIY_NEWS").build();

        Calendar calendar = Calendar.getInstance();
        VideoSnippet snippet = new VideoSnippet();
        snippet.setTitle(title);
        snippet.setDescription(description);

        List<String> tags = new ArrayList<String>();
        tags.add("DIYNews");
        snippet.setTags(tags);

        Video video = new Video();
        VideoStatus videoStatus = new VideoStatus();
        videoStatus.setPrivacyStatus("private");
        video.setStatus(videoStatus);
        video.setSnippet(snippet);



        try {
            InputStreamContent mediaContent = new InputStreamContent(VIDEO_FILE_FORMAT, inputStream);


            YouTube.Videos.Insert insert = youtube.videos().insert("snippet,statistics,status", video, mediaContent);
            MediaHttpUploader uploader = insert.getMediaHttpUploader();
            uploader.setDirectUploadEnabled(false);

            MediaHttpUploaderProgressListener progressListener = new MediaHttpUploaderProgressListener() {
                @Override
                public void progressChanged(MediaHttpUploader uploader) throws IOException {
                    switch (uploader.getUploadState()) {
                        case INITIATION_STARTED:
                            Log.d("DIY_NEWS", "Initiation Started");
                            break;
                        case INITIATION_COMPLETE:
                            Log.d("DIY_NEWS","Initiation Completed");
                            break;
                        case MEDIA_IN_PROGRESS:
                            Log.d("DIY_NEWS","Upload in progress");
                            Log.d("DIY_NEWS","Upload percentage: " + uploader.getNumBytesUploaded());
                            break;
                        case MEDIA_COMPLETE:
                            Log.d("DIY_NEWS","Upload Completed!");
                            break;
                        case NOT_STARTED:
                            Log.d("DIY_NEWS","Upload Not Started!");
                            break;
                    }
                }
            };
            uploader.setProgressListener(progressListener);

            Video returnedVideo = insert.execute();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}

