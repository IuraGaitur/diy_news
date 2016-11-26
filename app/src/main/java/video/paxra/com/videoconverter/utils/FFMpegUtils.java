package video.paxra.com.videoconverter.utils;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import video.paxra.com.videoconverter.activities.ConvertActivity;
import video.paxra.com.videoconverter.activities.Convertable;
import video.paxra.com.videoconverter.activities.MainActivity;

/**
 * Created by iura on 9/30/16.
 */
public class FFMpegUtils {

    public static void convertVideo(Context context, final Convertable resolver, String[] text) {

        /*String[] texts = inputs;
        String inputName = filePath;
        String outputName = fileOutPath = PathUtil.getPathFromFile(filePath) + TimeUtil.getTime()+".mp4";
        String imageName = PathUtil.getPathFromFile(filePath) + "ic_app.png";
        String[] cmds = CommandsFFMpegUtil.buildCommand(inputName, outputName, texts, font);*/

        Log.d("Convert", "Convert video");
        Log.d("Commands", text.length + "");
        FFmpeg ffmpeg = FFmpeg.getInstance(context);
        try {
            // to execute "ffmpeg -version" command you just need to pass "-version"
            ffmpeg.execute(text, new ExecuteBinaryResponseHandler() {
                @Override
                public void onStart() {
                    resolver.onStartConvert();
                }

                @Override
                public void onProgress(String message) {
                    Log.d("Progress", message);
                    resolver.onProgressConvert(message);
                }

                @Override
                public void onFailure(String message) {
                    resolver.onFailureConvert(message);
                }

                @Override
                public void onSuccess(String message) {
                    resolver.onSuccessConvert(message);
                }

                @Override
                public void onFinish() {
                    resolver.onFinishConvert();
                }
            });
        } catch (FFmpegCommandAlreadyRunningException e) {

        }

    }


    public static void getInfo(Context context, final Convertable resolver, String fileName) {
        String[] commands = ("-i " + fileName).split(" ");
        Log.d("FileName", fileName);
        FFmpeg ffmpeg = FFmpeg.getInstance(context);

        try {
            // to execute "ffmpeg -version" command you just need to pass "-version"
            ffmpeg.execute(commands, new ExecuteBinaryResponseHandler() {
                @Override
                public void onStart() {
                    resolver.onStartConvert();
                }

                @Override
                public void onProgress(String message) {
                    resolver.onProgressConvert(message);
                }

                @Override
                public void onFailure(String message) {
                    String time = "";
                    String fps = "";
                    Calendar calendar = Calendar.getInstance();
                    Log.d("Message", message);
                    Pattern pattern = Pattern.compile("Duration: (.{1,15})\\,|\\, (.{1,7}) fps\\,");
                    Matcher matcher = pattern.matcher(message);
                    int counter = 1;
                    while (matcher.find())
                    {
                        if(counter == 1) time = matcher.group(counter);
                        if(counter == 2) fps = matcher.group(counter);
                        counter++;
                    }
                    Log.d("Time", time);
                    SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss.SS");
                    try {
                        calendar.setTime(format.parse(time));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    double seconds = (calendar.get(Calendar.HOUR_OF_DAY) * 60 * 60 +
                            calendar.get(Calendar.MINUTE) * 60 +
                            calendar.get(Calendar.SECOND) +
                            calendar.get(Calendar.MILLISECOND) / 1000);
                    double totalFPS = seconds * Double.parseDouble(fps);
                    Log.d("Total FPS", totalFPS + "");
                    resolver.onFailureConvert("Total FPS:" + totalFPS);
                }

                @Override
                public void onSuccess(String message) {
                    resolver.onSuccessConvert(message);
                }

                @Override
                public void onFinish() {
                    resolver.onFinishConvert();
                }
            });
        } catch (FFmpegCommandAlreadyRunningException e) {

        }
    }

    public static String formatTimeForFFmpeg(int time) {
        if(time < 10) {
            return "00:00:0"+ time;
        }else if(time < 60) {
            return "00:00:" + time;
        }else if(time < 600) {
            return "00:" + time /60 + ":" + time % 60;
        }else if(time < 6000) {
            return time / 3600 + ":" + time % 60 + ":" + time % 3600;
        }
        return "00:00:00";
    }
}

