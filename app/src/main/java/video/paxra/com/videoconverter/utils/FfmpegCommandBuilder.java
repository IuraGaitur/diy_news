package video.paxra.com.videoconverter.utils;

import java.util.ArrayList;
import java.util.List;

import video.paxra.com.videoconverter.models.Answer;

/**
 * Created by iura on 7/2/16.
 */
public class FfmpegCommandBuilder {

    private static FfmpegCommandBuilder mInstance;

    private String mCommand;
    private List<String> mCommands = new ArrayList<>();

    private String mInputFile;
    private String mOutputFile;
    private List<Answer> mTexts;
    private String mFontFile;
    private String mImagePath;
    private int mOutputWidth;
    private int mOutputHeight;
    private int mCropFrom;
    private int mCropTo;

    int TEXT_WHEN_POSITION = 0;
    int TEXT_WHERE_POSITION = 1;

    private FfmpegCommandBuilder() {
    }

    public static FfmpegCommandBuilder getInstance() {
        if (mInstance == null) {
            mInstance = new FfmpegCommandBuilder();
        }

        return mInstance;
    }

    public FfmpegCommandBuilder setInput(String input) {
        this.mInputFile = input.replace("file://", "");
        return mInstance;
    }

    public FfmpegCommandBuilder setOutput(String output) {
        this.mOutputFile = output;
        return mInstance;
    }

    public FfmpegCommandBuilder setTexts(List<Answer> texts) {
        this.mTexts = texts;
        return mInstance;
    }

    public FfmpegCommandBuilder setFont(String font) {
        this.mFontFile = font;
        return mInstance;
    }

    public FfmpegCommandBuilder setImage(String image) {
        this.mImagePath = image;
        return mInstance;
    }

    public FfmpegCommandBuilder setWidth(int width) {
        this.mOutputWidth = width;
        return mInstance;
    }

    public FfmpegCommandBuilder setHeight(int height) {
        this.mOutputHeight = height;
        return mInstance;
    }

    public FfmpegCommandBuilder setCropFrom(int from) {
        this.mCropFrom = from;
        return mInstance;
    }

    public FfmpegCommandBuilder setCropTo(int to) {
        this.mCropTo = to;
        return mInstance;
    }

    public String[] build() {
        mCommand += buildInputAndCrop();
        mCommand += buildVideoScale();
        mCommand += buildTextsAndImage();
        mCommand += buildOutputAndSettings();

        for (String val : mCommand.split(" ")) {
            mCommands.add(val);
        }
        return (String[]) mCommands.toArray();
    }

    private String buildInputAndCrop() {
        String start = FFMpegUtils.formatTime(mCropFrom);
        String end = FFMpegUtils.formatTime(mCropTo);
        String startPart = String.format("-ss %s -t %s -i %s -i %s -filter_complex",
                start, end, mInputFile, mImagePath);

        return startPart;
    }

    private String buildVideoScale() {
        int width = mOutputWidth > mOutputHeight ? Constants.VIDEO_WIDTH : Constants.VIDEO_HEIGHT;
        int height = mOutputWidth > mOutputHeight ? Constants.VIDEO_HEIGHT : Constants.VIDEO_WIDTH;

        return " [0:]scale=" + width + ":" + height + ",overlay=10:10,";
    }

    private String buildTextsAndImage() {

        String text;
        StringBuilder textCommand = new StringBuilder();


        text = addHeadlineText(mTexts.get(TEXT_WHEN_POSITION), mTexts.get(TEXT_WHERE_POSITION));
        textCommand.append(text);

        for (int pos = 2; pos < mTexts.size(); pos++) {
            boolean lastPosition = pos >= mTexts.size() - 1;
            text = addFooterText(mTexts.get(pos), lastPosition);
            textCommand.append(text);
        }

        return textCommand.toString();
    }

    private String addHeadlineText(Answer whenAnswer, Answer whereAnswer) {
        String headline = "";
        int videoDuration = mCropFrom - mCropTo;

        String whereFormatted = whereAnswer.getAnswer().replace(":", "\\:").replace("'", "'\\\\\\''");
        String whenFormatted = whenAnswer.getAnswer().replace(":", "\\:").replace("'", "'\\\\\\''");

        String whenText = String.format("drawtext=enable='between(t,%d,%d)':fontfile=%s:text='%s'" +
                        ": fontcolor=white:shadowcolor=black:shadowx=1:shadowy=1: fontsize=%d: x=(w-text_w)/1.07: y=30,",
                0, videoDuration, mFontFile, whenFormatted, Constants.HEADER_FONT_SIZE);
        headline += whenText;
        String whereText = String.format("drawtext=enable='between(t,%d,%d)':fontfile=%s:text='%s'" +
                        ": fontcolor=white:shadowcolor=black:shadowx=1:shadowy=1: fontsize=%d: x=(w-text_w)/1.07: y=35 + th,",
                0, videoDuration, mFontFile, whereFormatted, Constants.HEADER_FONT_SIZE);
        headline += whereText;

        return headline;
    }

    private String addFooterText(Answer currentAnswer, boolean isLastPosition) {

        String textCommand = "";
        int width = mOutputWidth > mOutputHeight ? Constants.VIDEO_WIDTH : Constants.VIDEO_HEIGHT;
        int height = mOutputWidth > mOutputHeight ? Constants.VIDEO_HEIGHT : Constants.VIDEO_WIDTH;
        int lineNumber = AndroidUtilities.getNumberOfLines(currentAnswer.answer, width, height, Constants.HEADER_FONT_SIZE);
        int charsPerLine = AndroidUtilities.getCharsPerLine(width, Constants.TEXT_FONT_SIZE);
        List<String> cutAnswer = StringUtils.splitStringIntoParts(currentAnswer.getAnswer(), charsPerLine);

        for (int pos = 0; pos < cutAnswer.size(); pos++) {
            textCommand += buildSplittedLine(currentAnswer, cutAnswer.get(pos), pos, cutAnswer.size(), height, lineNumber);
        }

        if (!isLastPosition) {
            textCommand += ",";
        }

        return textCommand;
    }

    private String buildSplittedLine(Answer currentAnswer, String currentTextLine, int pos, int totalSize, int height, int lineNumber) {

        String textLine;
        int yPos = AndroidUtilities.getYStartPosition(height, pos + 1, lineNumber, Constants.TEXT_FONT_SIZE, Constants.BOTTOM_PADDING);

        if (totalSize == 1) {
            textLine = buildSingleLineOutput(currentAnswer, currentTextLine, Constants.TEXT_FONT_SIZE, yPos);
        } else {
            int linePos = totalSize - (pos + 1);
            textLine = buildMultiLineOuput(currentAnswer, currentTextLine, Constants.TEXT_FONT_SIZE, yPos, linePos);
        }
        if (pos < totalSize - 1) {
            textLine += ",";
        }

        return textLine;
    }

    private String buildSingleLineOutput(Answer currentAnswer, String currentTextLine, int fontSize, int yPos) {

        int from = currentAnswer.getFrom();
        int to = currentAnswer.getTo();
        String text = currentTextLine.replace(":", "\\:").replace("'", "'\\\\\\''");


        return String.format("drawtext=enable='between(t,%d,%d)':fontfile=%s:text='%s'" +
                        ": fontcolor=yellow:shadowcolor=black:shadowx=1:shadowy=1: " +
                        "fontsize=%d: x=(w-tw)/2: y=%d",
                from, to, mFontFile, text, fontSize, yPos);
    }

    private String buildMultiLineOuput(Answer currentAnswer, String currentTextLine, int fontSize, int yPos, int pos) {

        int negativePadding = 0;
        int from = currentAnswer.getFrom();
        int to = currentAnswer.getTo();
        String text = currentTextLine.replace(":", "\\:").replace("'", "'\\\\\\''");

        if (currentTextLine.contains("Ș") || currentTextLine.contains("Ț") || currentTextLine.contains("Ă") || currentTextLine.contains("Î")) {
            negativePadding = 7;
        }
        return String.format("drawtext=enable='between(t,%d,%d)':fontfile=%s:text='%s'" +
                        ": fontcolor=yellow:shadowcolor=black:shadowx=1:shadowy=1: fontsize=%d:" +
                        " x=(w / 20): y=(h - (h / 10) - (40 * %d) - %d)",
                from, to, mFontFile, text, fontSize, pos, negativePadding);

    }

    private String buildOutputAndSettings() {
        return " -codec:v libx264 -preset ultrafast " + mOutputFile;
    }

//    public static String[] buildCommand(String inputFile, String outputFile, ArrayList<Answer> answers, String fontFile, String imagePath, int screenWidth, int screenHeight, int cropFrom, int cropTo) {
//
//        int videoDuration = cropTo - cropFrom;
//        List<String> data = new ArrayList();
//        StringBuilder result = new StringBuilder();
//        String fromTimeFormatted = FFMpegUtils.formatTime(cropFrom);
//        String endTimeFormatted = FFMpegUtils.formatTime(cropTo);
//
//        //add preconditions
//        String startPart = String.format("-ss %s -t %s -i %s -i %s -filter_complex",
//                fromTimeFormatted, endTimeFormatted, inputFile.replace("file://", ""), imagePath);
//
//        //add preconditions to final command
//        String[] split = startPart.split(" ");
//        for (String val : split) {
//            data.add(val);
//        }
//
//        //add middle condition
//        int finalWidth = screenWidth > screenHeight ? 720 : 480;
//        int finalHeight = screenHeight > screenWidth ? 720 : 480;
//        result.append("[0:]scale=" + finalWidth + ":" + finalHeight + ",overlay=10:10,");
//
//        for (int i = 1; i < answers.size(); i++) {
//
//            int lineNumber = AndroidUtilities.getNumberOfLines(answers.get(i).answer, finalWidth, finalHeight, Constants.HEADER_FONT_SIZE);
//            int yPos = AndroidUtilities.getYStartPosition(finalHeight, 1, lineNumber, Constants.HEADER_FONT_SIZE, Constants.BOTTOM_PADDING);
//            Log.d("Information", "Width:" + screenWidth + ";Height:" + screenHeight + ";lineNum:" + lineNumber + ";yPos:" + yPos);
//            String item = "";
//
//            if (i == 1) {
//                item = String.format("drawtext=enable='between(t,%d,%d)':fontfile=%s:text='%s'" +
//                        ": fontcolor=white:shadowcolor=black:shadowx=1:shadowy=1: fontsize=" + (960 + 540) / 60 + ": x=(w-text_w)/1.07: y=30,", 0, videoDuration, fontFile, answers.get(0).getAnswer().replace(":", "\\:").replace("'", "'\\\\\\''"));
//                result.append(item);
//                item = String.format("drawtext=enable='between(t,%d,%d)':fontfile=%s:text='%s'" +
//                        ": fontcolor=white:shadowcolor=black:shadowx=1:shadowy=1: fontsize=" + (960 + 540) / 60 + ": x=(w-text_w)/1.07: y=35 + th", 0, videoDuration, fontFile, answers.get(1).getAnswer().replace(":", "\\:").replace("'", "'\\\\\\''"));
//                result.append(item);
//            } else {
//                int charsPerLine = AndroidUtilities.getCharsPerLine(finalWidth, Constants.TEXT_FONT_SIZE);
//                List<String> cutAnswer = StringUtils.splitStringIntoParts(answers.get(i).answer, charsPerLine);
//                for (int j = 0; j < cutAnswer.size(); j++) {
//                    yPos = AndroidUtilities.getYStartPosition(finalHeight, j + 1, lineNumber, Constants.TEXT_FONT_SIZE, Constants.BOTTOM_PADDING);
//
//                    Log.d("Y Pos", "Y pos" + yPos);
//
//                    if (cutAnswer.size() == 1) {
//                        item = String.format("drawtext=enable='between(t,%d,%d)':fontfile=%s:text='%s'" +
//                                        ": fontcolor=yellow:shadowcolor=black:shadowx=1:shadowy=1: fontsize=" + (960 + 540) / 50 + ": x=(w-tw)/2: y=" + yPos,
//                                answers.get(i).getFrom(), answers.get(i).getTo(), fontFile, cutAnswer.get(j).replace(":", "\\:").replace("'", "'\\\\\\''"));
//                    } else {
//                        int negativePadding = 0;
//                        if (cutAnswer.get(j).contains("Ș") || cutAnswer.get(j).contains("Ț") || cutAnswer.get(j).contains("Ă") || cutAnswer.get(j).contains("Î")) {
//                            negativePadding = 7;
//                        }
//                        item = String.format("drawtext=enable='between(t,%d,%d)':fontfile=%s:text='%s'" +
//                                        ": fontcolor=yellow:shadowcolor=black:shadowx=1:shadowy=1: fontsize=" + 32 + ": x=(w / 20): y=(h - (h / 10) - (40*" + (cutAnswer.size() - (j + 1)) + ") - " + negativePadding + ")",
//                                answers.get(i).getFrom(), answers.get(i).getTo(), fontFile, cutAnswer.get(j).replace(":", "\\:").replace("'", "'\\\\\\''"));
//                    }
//                    if (j < cutAnswer.size() - 1) {
//                        item += ",";
//                    }
//
//                    result.append(item);
//                }
//
//                //String drawGrid = String.format("drawbox=enable='between(n,%d, %d)' : x=%d : y=%d : w=iw-20: h=ih-20 : color=yellow", time, time + 2, xPos, yPos);
//                //result.append(drawGrid);
//            }
//
//            //take all except the last one
//            if (i < answers.size() - 1) {
//                result.append(",");
//            }
//        }
//        //append this as command
//        data.add(result.toString());
//        result.setLength(0);
//
//        //append last condition
//        String videoBuildCommand = "-codec:v libx264 -preset ultrafast " + outputFile;
//        result.append(videoBuildCommand);
//
//
//        //format into ffmpeg
//        split = result.toString().split(" ");
//        for (String val : split) {
//            data.add(val);
//        }
//        String[] splittedCmds = data.toArray(new String[0]);
//
//        //check commands
//        for (String item : splittedCmds) {
//            Log.d("Commands", item.toString());
//        }
//
//        return splittedCmds;
//    }
}
