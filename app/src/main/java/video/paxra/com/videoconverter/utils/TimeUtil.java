package video.paxra.com.videoconverter.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by iura on 7/3/16.
 */
public class TimeUtil {
    public static String getTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd:HH-mm");

        try {
            return formatter.format(new Date());
        }catch (Exception ex) {

        }
        return "";
    }

    public static String formatTime(float milisec) {
        int sec = (int)milisec / 1000;
        if((sec/60) > 10) {
            return (sec / 60) + ":" + (sec % 60);
        }
        if(sec > 59) {
            return "0" + (sec / 60) + ":" + (sec % 60);
        }
        if(sec == 0) {
            return "00:00";
        }
        return "00:"+ sec ;

    }
}
