package video.paxra.com.videoconverter.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iuriegaitur on 11/26/16.
 */

public class StringUtils {
    public static List<String> splitStringIntoParts(String text, int times, int charsPerLine) {
        List<String> strings = new ArrayList<String>();

        int divider = charsPerLine;

        int index = 0;
        while (index < text.length()) {
            strings.add(text.substring(index, Math.min(index + divider,text.length())));
            index += divider;
        }
        return strings;
    }
}
