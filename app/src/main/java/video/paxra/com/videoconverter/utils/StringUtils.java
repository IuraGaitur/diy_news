package video.paxra.com.videoconverter.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iuriegaitur on 11/26/16.
 */

public class StringUtils {
    public static List<String> splitStringIntoParts(String text, int times) {
        List<String> strings = new ArrayList<String>();

        int divider = (text.length() / times) + 1;

        int index = 0;
        while (index < text.length()) {
            strings.add(text.substring(index, Math.min(index + divider,text.length())));
            index += divider;
        }
        return strings;
    }
}
