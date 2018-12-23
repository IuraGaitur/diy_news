package video.paxra.com.videoconverter.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;


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

    public static String generateRandomFromName(String fileName) {
        String outputFileName = fileName.split("\\.")[fileName.split("\\.").length - 1];
        String pattern = "MM_dd_yyyy";
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        String date = format.format(new Date());
        Random r = new Random();
        int number = r.nextInt(1000);

        String replacer = "_" + date + number + "." + outputFileName;
        outputFileName = fileName.replace("." + outputFileName, replacer);

        return outputFileName;
    }
}
