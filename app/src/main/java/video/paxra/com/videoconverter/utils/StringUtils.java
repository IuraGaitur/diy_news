package video.paxra.com.videoconverter.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iuriegaitur on 11/26/16.
 */

public class StringUtils {
    public static List<String> splitStringIntoParts(String text, int charsPerLine) {
        int rowChars = 0;
        String[] parts = text.split(" ");
        List<String> strings = new ArrayList<String>();
        StringBuilder partContainer = new StringBuilder();

        for (int wordNum = 0; wordNum < parts.length; wordNum++) {
            rowChars+=parts[wordNum].length() + 1;

            if(rowChars <= charsPerLine) {
                //add word that can get in
                partContainer.append(parts[wordNum] + " ");
            }else {
                //append word after
                wordNum--;
                rowChars = 0;
                strings.add(partContainer.toString());
                partContainer.setLength(0);
            }
        }
        //check if needs to add string
        if(partContainer.length() > 0) {
            strings.add(partContainer.toString());
        }

        return strings;
    }
}
