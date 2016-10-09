package video.paxra.com.videoconverter.utils;

import java.util.ArrayList;
import java.util.List;

import video.paxra.com.videoconverter.models.Answer;

/**
 * Created by iura on 7/2/16.
 */
public class CommandsFFMpegUtil {
    public static String[] buildCommand(String inputFile, String outputFile, ArrayList<Answer> answers, String fontFile) {
        List<String> data = new ArrayList();
        StringBuilder result = new StringBuilder();
        result.append("-i " + inputFile + " -vf");
        String[] split = result.toString().split(" ");
        for(String val: split) {
            data.add(val);
        }
        //clear
        result.setLength(0);

        result.append("[in]");

        int time = -2;
        for(int i=0;i< answers.size();i++) {

            String item = "";

            if(i==0) {
                item = String.format("drawtext=enable='between(t,%d,%d)':fontfile=%s:text='%s'" +
                                ": fontcolor=black: fontsize=18: box=1: boxcolor=yellow@0.5:boxborderw=5: x=(w-text_w)/1.15: y=30",
                        0, 12, fontFile, answers.get(i).answer);
            } else {
                item = String.format("drawtext=enable='between(t,%d,%d)':fontfile=%s:text='%s'" +
                                ": fontcolor=black: fontsize=23: box=1: boxcolor=yellow@0.5:boxborderw=5: x=(w-text_w)/2: y=(h-text_h)/1.2",
                        time, time + 2, fontFile, answers.get(i).answer);
            }

            result.append(item);
            time+=3;
            if(i < answers.size() - 1) {
                result.append(",");
            }
        }
        //result.append("\"");
        data.add(result.toString());
        result.setLength(0);

        String videoBuildCommand = buildGenerateCommand(outputFile);
        result.append(videoBuildCommand);

        split = result.toString().split(" ");
        for(String val: split) {
            data.add(val);
        }

        String[] splittedCmds = data.toArray(new String[0]);

        return splittedCmds;
    }

    public static String buildGenerateCommand(String outputFileName) {
        String videoBuildCommand = "-codec:v libx264 -preset ultrafast -crf 21 -bf 2 -flags +cgop -pix_fmt -bufsize 500k " +
                "yuv420p -codec:a aac -strict -2 -b:a 300k -r:a 48000 -movflags faststart " + outputFileName;
        //String videoBuildCommand = "-codec:v libx264 -profile:v baseline -preset slow -b:v 250k -maxrate 250k " +
        //        " -vf scale=-1:360 -threads 0 -codec:a -b:a 96k " + outputFileName;
        return videoBuildCommand;
    }


}
