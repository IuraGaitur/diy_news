package video.paxra.com.videoconverter.utils;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import video.paxra.com.videoconverter.models.Answer;

/**
 * Created by iura on 7/2/16.
 */
public class CommandsFFMpegUtil {
    public static String[] buildCommand(String inputFile, String outputFile, ArrayList<Answer> answers, String fontFile, String imagePath, int screenWidth, int screenHeight, int cropFrom, int cropTo) {
        int paddingBottom = 20;
        int fontSize = (960 + 540) / 50;
        int videoDuration = cropTo - cropFrom;
        List<String> data = new ArrayList();
        StringBuilder result = new StringBuilder();
        String fromTimeFormatted = FFMpegUtils.formatTimeForFFmpeg(cropFrom);
        String endTimeFormatted = FFMpegUtils.formatTimeForFFmpeg(cropTo);

        //add preconditions
        String startPart = String.format("-ss %s -t %s -i %s -i %s -filter_complex",
                fromTimeFormatted, endTimeFormatted, inputFile.replace("file://", ""), imagePath);

        //add preconditions to final command
        String[] split = startPart.split(" ");
        for (String val : split) {
            data.add(val);
        }

        //add middle condition
        int finalWidth = screenWidth > screenHeight ? 720 : 480;
        int finalHeight = screenHeight > screenWidth ? 720 : 480;
        result.append("[0:]scale=" + finalWidth + ":" + finalHeight + ",overlay=10:10,");

        for (int i = 1; i < answers.size(); i++) {

            int lineNumber = AndroidUtilities.getNumberOfLines(answers.get(i).answer, finalWidth, finalHeight, fontSize);
            int yPos = AndroidUtilities.getYStartPosition(finalHeight, 1, lineNumber, fontSize, paddingBottom);
            Log.d("Information", "Width:" + screenWidth + ";Height:" + screenHeight + ";lineNum:" + lineNumber + ";yPos:" + yPos);
            String item = "";

            if (i == 1) {
                item = String.format("drawtext=enable='between(t,%d,%d)':fontfile=%s:text='%s'" +
                                ": fontcolor=white:shadowcolor=black:shadowx=1:shadowy=1: fontsize=" + (960+540)/60 +": x=(w-text_w)/1.07: y=30,", 0, videoDuration, fontFile, answers.get(0).getAnswer().replace(":", "\\:").replace("'", "'\\\\\\''"));
                result.append(item);
                item = String.format("drawtext=enable='between(t,%d,%d)':fontfile=%s:text='%s'" +
                                ": fontcolor=white:shadowcolor=black:shadowx=1:shadowy=1: fontsize=" + (960+540)/60 + ": x=(w-text_w)/1.07: y=35 + th", 0, videoDuration, fontFile, answers.get(1).getAnswer().replace(":", "\\:").replace("'", "'\\\\\\''"));
                result.append(item);
            } else {
                int charsPerLine = AndroidUtilities.getCharsPerLine(finalWidth, fontSize);
                List<String> cutAnswer = StringUtils.splitStringIntoParts(answers.get(i).answer, charsPerLine);
                for (int j = 0; j < cutAnswer.size(); j++) {
                    yPos = AndroidUtilities.getYStartPosition(finalHeight, j, lineNumber, fontSize, paddingBottom);

                    if (cutAnswer.size() == 1) {
                        item = String.format("drawtext=enable='between(t,%d,%d)':fontfile=%s:text='%s'" +
                                        ": fontcolor=yellow:shadowcolor=black:shadowx=1:shadowy=1: fontsize=" + (960+540)/50 + ": x=(w-tw)/2: y=" + yPos,
                                answers.get(i).getFrom(), answers.get(i).getTo(), fontFile, cutAnswer.get(j).replace(":", "\\:").replace("'", "'\\\\\\''"));
                    } else {
                        item = String.format("drawtext=enable='between(t,%d,%d)':fontfile=%s:text='%s'" +
                                        ": fontcolor=yellow:shadowcolor=black:shadowx=1:shadowy=1: fontsize=" + (960+540)/50 + ": x=(w / 20): y=" + yPos,
                                answers.get(i).getFrom(), answers.get(i).getTo(), fontFile, cutAnswer.get(j).replace(":", "\\:").replace("'", "'\\\\\\''"));
                    }
                    if (j < cutAnswer.size() - 1) {
                        item += ",";
                    }

                    result.append(item);
                }

                //String drawGrid = String.format("drawbox=enable='between(n,%d, %d)' : x=%d : y=%d : w=iw-20: h=ih-20 : color=yellow", time, time + 2, xPos, yPos);
                //result.append(drawGrid);
            }

            //take all except the last one
            if (i < answers.size() - 1) {
                result.append(",");
            }
        }
        //append this as command
        data.add(result.toString());
        result.setLength(0);

        //append last condition
        String videoBuildCommand = "-codec:v libx264 -preset ultrafast " + outputFile;
        result.append(videoBuildCommand);


        //format into ffmpeg
        split = result.toString().split(" ");
        for (String val : split) {
            data.add(val);
        }
        String[] splittedCmds = data.toArray(new String[0]);

        //check commands
        for (String item : splittedCmds) {
            Log.d("Commands", item.toString());
        }

        return splittedCmds;
    }
}
