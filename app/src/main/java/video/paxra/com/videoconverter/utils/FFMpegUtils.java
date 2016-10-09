package video.paxra.com.videoconverter.utils;

import android.content.Context;
import android.net.Uri;

import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;

import video.paxra.com.videoconverter.activities.ConvertActivity;
import video.paxra.com.videoconverter.activities.Convertable;
import video.paxra.com.videoconverter.activities.MainActivity;

/**
 * Created by iura on 9/30/16.
 */
public class FFMpegUtils {

    public static void convertVideo(Context context, final Convertable resolver, Uri imagePath, String[] text) {

        /*String[] texts = inputs;
        String inputName = filePath;
        String outputName = fileOutPath = PathUtil.getPathFromFile(filePath) + TimeUtil.getTime()+".mp4";
        String imageName = PathUtil.getPathFromFile(filePath) + "ic_app.png";
        String font = this.getExternalFilesDir(null) + "/font.ttf";
        String[] cmds = CommandsFFMpegUtil.buildCommand(inputName, outputName, texts, font);*/

        FFmpeg ffmpeg = FFmpeg.getInstance(context);
        try {
            // to execute "ffmpeg -version" command you just need to pass "-version"
            ffmpeg.execute(null, new ExecuteBinaryResponseHandler() {
                @Override
                public void onStart() {
                    resolver.onStartConvert();
                }

                @Override
                public void onProgress(String message) {
                    resolver.onProgressConvert();
                }

                @Override
                public void onFailure(String message) {
                    resolver.onFailureConvert();
                }

                @Override
                public void onSuccess(String message) {
                    resolver.onSuccessConvert();
                }

                @Override
                public void onFinish() {
                    resolver.onFinishConvert();
                }
            });
        } catch (FFmpegCommandAlreadyRunningException e) {

        }

    }
}

