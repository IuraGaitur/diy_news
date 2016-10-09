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
}
