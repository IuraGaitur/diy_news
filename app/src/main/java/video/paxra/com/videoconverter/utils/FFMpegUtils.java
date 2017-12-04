package video.paxra.com.videoconverter.utils;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import video.paxra.com.videoconverter.activities.ConvertActivity;
import video.paxra.com.videoconverter.activities.Convertable;
import video.paxra.com.videoconverter.activities.MainActivity;
import video.paxra.com.videoconverter.models.Answer;

/**
 * Created by iura on 9/30/16.
 */
public class FFMpegUtils {

    public static void convertVideo(Context context, final Convertable resolver, String[] text) {

        /*String[] texts = inputs;
        String inputName = filePath;
        String outputName = fileOutPath = PathUtil.getPathFromFile(filePath) + TimeUtil.getTime()+".mp4";
        String imageName = PathUtil.getPathFromFile(filePath) + "ic_app.png";
        String[] cmds = FfmpegCommandBuilder.buildCommand(inputName, outputName, texts, font);*/

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
                    double totalFPS = 0;
                    try {
                        calendar.setTime(format.parse(time));
                        double seconds = (calendar.get(Calendar.HOUR_OF_DAY) * 60 * 60 +
                                calendar.get(Calendar.MINUTE) * 60 +
                                calendar.get(Calendar.SECOND) +
                                calendar.get(Calendar.MILLISECOND) / 1000);
                        totalFPS = seconds * Double.parseDouble(fps);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

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

    /**
     * Calculate time for each text with each is distributed equally
     * between them
     * @param answers - list texts
     * @param videoLenght - total video size
     * @return
     */
    public static ArrayList<Answer> calculateTimeShowForText(ArrayList<Answer> answers, int videoLenght) {

        int emptyTime = 2;
        int numOfText = answers.size() - 2;
        //get 4 second for time as intro and ending
        int videoTimeWithPadding = videoLenght - emptyTime;
        //start from 2 because first two text ar as headers
        int continueTime = emptyTime;
        int pauseTime = (int)(videoTimeWithPadding / numOfText * 0.3);
        int textTime = (int)(videoTimeWithPadding / numOfText * 0.7);

        for (int i = 2; i < answers.size(); i++) {
            answers.get(i).setFrom(continueTime);
            answers.get(i).setTo(continueTime + textTime);
            continueTime = continueTime + textTime + pauseTime;
        }

        return answers;

    }

    /**
     * Calculate time for each text in dependency of each answers text
     * so that each text get a ratio of how long it should be shown to the user
     * @param answers - texts to show in the video
     * @param videoLenght - total video length
     * @return - items with from and to
     */
    public static ArrayList<Answer> calculateSmartTimeShowForText(ArrayList<Answer> answers, int videoLenght) {

        int emptyTime = 2;
        int numOfText = answers.size() - 2;
        //get 4 second for time as intro and ending
        int videoTimeWithPadding = videoLenght - emptyTime;
        //start from 2 because first two text ar as headers
        int continueTime = emptyTime;
        int pauseTime = (int)(videoTimeWithPadding / numOfText * 0.3);
        int totalPauseTime = numOfText * pauseTime;

        int totalTextTime = videoTimeWithPadding - totalPauseTime;

        int totalTextChars = 0;
        for (int i = 2; i < answers.size(); i++) {
            totalTextChars += answers.get(i).getAnswer().length();
        }


        for (int i = 2; i < answers.size(); i++) {
            answers.get(i).setFrom(continueTime);
            int textLength = answers.get(i).getAnswer().length();
            float textTime = totalTextTime * textLength / totalTextChars;
            answers.get(i).setTo(continueTime + (int)textTime);
            continueTime = continueTime + (int)textTime + pauseTime;
        }

        return answers;

    }


}

