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
        List<String> data = new ArrayList();
        //append files
        StringBuilder result = new StringBuilder();
        String fromTimeFormatted = FFMpegUtils.formatTimeForFFmpeg(cropFrom);
        String endTimeFormatted = FFMpegUtils.formatTimeForFFmpeg(cropTo);
        int fontSize = 34;
        int paddingBottom = 20;
        int videoDuration = cropTo - cropFrom;
        //inputFile = inputFile.replace("file://", "");
        //outputFile = outputFile.replace("file://", "");


        //add preconditions
        String startPart = String.format("-ss %s -t %s -i %s -i %s -filter_complex",
                fromTimeFormatted, endTimeFormatted, inputFile.replace("file://", ""), imagePath);

        //result.append("-ss "+ fromTimeFormatted + " -t " + endTimeFormatted + " -i " + inputFile.replace("file://", "") + " -i " + imagePath + " -filter_complex");

        String[] split = startPart.split(" ");
        for (String val : split) {
            data.add(val);
        }

        //result.setLength(0);

        //add middle condition
        result.append("[0:]overlay=10:10,");

        for (int i = 1; i < answers.size(); i++) {

            int lineNumber = AndroidUtilities.getNumberOfLines(answers.get(i).answer, screenWidth, screenHeight, fontSize);
            int xPos = AndroidUtilities.getXStartPosition(answers.get(i).answer, screenWidth, fontSize);
            int yPos = AndroidUtilities.getYStartPosition(screenHeight, 1, lineNumber, fontSize, paddingBottom);
            Log.d("Information", "Width:" + screenWidth + ";Height:" + screenHeight + ";lineNum:" + lineNumber + ";xPos" + xPos + ";yPos:" + yPos);
            String item = "";

            if (i == 1) {
                item = String.format("drawtext=enable='between(t,%d,%d)':fontfile=%s:text='%s'" +
                                ": fontcolor=white: fontsize=" + (fontSize - 5) + ": x=(w-text_w)/1.15: y=30,",
                        0, videoDuration, fontFile, answers.get(0).answer);
                result.append(item);
                item = String.format("drawtext=enable='between(t,%d,%d)':fontfile=%s:text='%s'" +
                                ": fontcolor=white: fontsize=" + (fontSize - 5) + ": x=(w-text_w)/1.15: y=35 + th",
                        0, videoDuration, fontFile, answers.get(1).answer);
                result.append(item);
            } else {
                for (int j = 0; j < lineNumber; j++) {
                    List<String> cutAnswer = StringUtils.splitStringIntoParts(answers.get(i).answer, lineNumber);
                    xPos = AndroidUtilities.getXStartPosition(cutAnswer.get(j), screenWidth, fontSize);
                    yPos = AndroidUtilities.getYStartPosition(screenHeight, j, lineNumber, fontSize, paddingBottom);

                    item = String.format("drawtext=enable='between(t,%d,%d)':fontfile=%s:text='%s'" +
                                    ": fontcolor=white: fontsize=" + fontSize + ": x=(w-tw)/2: y=" + yPos ,
                            answers.get(i).getFrom(), answers.get(i).getTo(), fontFile, cutAnswer.get(j));
                    if(j < lineNumber - 1) {
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

    /*public static String buildGenerateCommand(String outputFileName) {
        String videoBuildCommand = "-codec:v libx264 -preset ultrafast -crf 21 -bf 2 -flags +cgop -pix_fmt -bufsize 500k " +
                "yuv420p -codec:a aac -strict -2 -b:a 300k -r:a 48000 -movflags faststart " + outputFileName;
        //String videoBuildCommand = "-codec:v libx264 -profile:v baseline -preset slow -b:v 250k -maxrate 250k " +
        //        " -vf scale=-1:360 -threads 0 -codec:a -b:a 96k " + outputFileName;
        return videoBuildCommand;
    }*/




    /*public static String[] buildCommand(String inputFile, String outputFile, ArrayList<Answer> answers, String fontFile, String imagePath, int screenWidth, int screenHeight, int cropFrom, int cropTo) {
        List<String> data = new ArrayList();
        //append files
        StringBuilder result = new StringBuilder();
        *//*String fromTimeFormatted = FFMpegUtils.formatTimeForFFmpeg(cropFrom);
        String endTimeFormatted = FFMpegUtils.formatTimeForFFmpeg(cropTo);
        inputFile = inputFile.replace("file://", "");*//*

        *//*String startPart = String.format("-ss %s -t %s -i %s -i  %s -filter_complex",
                fromTimeFormatted, endTimeFormatted, inputFile, imagePath);
        *//*


        result.append("-ss 00:00:02 -t 00:00:32 -i " + inputFile.replace("file://", "") + " -i " + imagePath + " -filter_complex");

        //add content in final array query
        String[] split = result.toString().split(" ");
        for (String val : split) {
            data.add(val);
        }
        result.setLength(0);


        result.append("[0:]overlay=10:10,");
        int time = 1;

        for (int i = 0; i < answers.size(); i++) {

            String item = "";
            if(i==0) {
                item = String.format("drawtext=enable='between(t,%d,%d)':fontfile=%s:text='%s'" +
                                ": fontcolor=black: fontsize=20: box=1: boxcolor=yellow@0.5:boxborderw=5: x=(w-text_w)/1.15: y=30",
                        time, time + 2, fontFile, answers.get(i).answer);
            }else {
                item = String.format("drawtext=enable='between(t,%d,%d)':fontfile=%s:text='%s'" +
                                ": fontcolor=black: fontsize=20: box=1: boxcolor=yellow@0.5:boxborderw=5: x=(w-text_w)/1.15: y=30",
                        time, time + 2, fontFile, answers.get(i).answer);
            }

            result.append(item);
            time += 3;
            //take all except the last one
            if (i < answers.size() - 1) {
                result.append(",");
            }else {
                //result.append("\"");
            }
        }
        //append this as command
        data.add(result.toString());
        result.setLength(0);


        String videoBuildCommand = "-codec:v libx264 -preset ultrafast " + outputFile.replace("file://", "");
        result.append(videoBuildCommand);

        split = result.toString().split(" ");
        for (String val : split) {
            data.add(val);
        }

        String[] splittedCmds = data.toArray(new String[0]);
        for (String item : splittedCmds) {
            Log.d("Commands", item.toString());
        }

        return splittedCmds;
    }


       public static String[] buildCommand(String inputFile, String outputFile, ArrayList<Answer> answers, String fontFile, String imagePath, int screenWidth, int screenHeight, int cropFrom, int cropTo) {
        List<String> data = new ArrayList();
        //append files
        StringBuilder result = new StringBuilder();
        String fromTimeFormatted = FFMpegUtils.formatTimeForFFmpeg(cropFrom);
        String endTimeFormatted = FFMpegUtils.formatTimeForFFmpeg(cropTo);
        int fontSize = 20;
        int paddingBottom = 20;
        //inputFile = inputFile.replace("file://", "");
        //outputFile = outputFile.replace("file://", "");


        //add preconditions
        String startPart = String.format("-ss %s -t %s -i %s -i %s -filter_complex",
                fromTimeFormatted, endTimeFormatted, inputFile.replace("file://", ""), imagePath);

        //result.append("-ss "+ fromTimeFormatted + " -t " + endTimeFormatted + " -i " + inputFile.replace("file://", "") + " -i " + imagePath + " -filter_complex");

        String[] split = startPart.split(" ");
        for (String val : split) {
            data.add(val);
        }

        //result.setLength(0);


        //add middle condition
        result.append("[0:]overlay=10:10,");

        int time = 1;
        for (int i = 0; i < answers.size(); i++) {

            int lineNumber = AndroidUtilities.getNumberOfLines(answers.get(i).answer, screenWidth, screenHeight, fontSize);
            int xPos = AndroidUtilities.getXStartPosition(answers.get(i).answer, screenWidth, fontSize);
            int yPos = AndroidUtilities.getYStartPosition(screenHeight, 1, lineNumber, fontSize, paddingBottom);
            Log.d("Information","Width:" + screenWidth + ";Height:" + screenHeight +
                    ";lineNum:" + lineNumber + ";xPos" + xPos +";yPos:"+ yPos);
            String item = "";

            if(i==0) {
                item = String.format("drawtext=enable='between(t,%d,%d)':fontfile=%s:text='%s'" +
                                ": fontcolor=black: fontsize=" + fontSize + ": box=1: boxcolor=yellow@0.5:boxborderw=5: x=(w-text_w)/1.15: y=30",
                        time, time + 2, fontFile, answers.get(i).answer);
                result.append(item);
            }else {

                for(int j = 0;j < lineNumber;j++) {
                    String cutAnswer = answers.get(i).answer
                    xPos = AndroidUtilities.getXStartPosition(answers.get(i).answer, screenWidth, fontSize);
                    yPos = AndroidUtilities.getYStartPosition(screenHeight, 1, lineNumber, fontSize, paddingBottom);

                    item = String.format("drawtext=enable='between(t,%d,%d)':fontfile=%s:text='%s'" +
                                    ": fontcolor=black: fontsize=" + fontSize + ": box=1: boxcolor=yellow@0.5:boxborderw=5: x=" + xPos + ": y=" + yPos,
                            time, time + 2, fontFile, answers.get(i).answer);

                    result.append(item);
                    if (j < lineNumber - 1) {
                        result.append(",");
                    }
                }
            }

            time += 3;
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

*/

}
