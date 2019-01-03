package video.paxra.com.videoconverter.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import video.paxra.com.videoconverter.models.Answer;
import video.paxra.com.videoconverter.models.Line;

public class FfmpegCommandBuilder2 {

    private static FfmpegCommandBuilder2 mInstance;

    private String mCommand = "";
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

    private FfmpegCommandBuilder2() {
    }

    public static FfmpegCommandBuilder2 getInstance() {
        if (mInstance == null) {
            mInstance = new FfmpegCommandBuilder2();
        }

        return mInstance;
    }

    public FfmpegCommandBuilder2 setInput(String input) {
        this.mInputFile = input.replace("file://", "");
        return mInstance;
    }

    public FfmpegCommandBuilder2 setOutput(String output) {
        this.mOutputFile = output;
        return mInstance;
    }

    public FfmpegCommandBuilder2 setTexts(List<Answer> texts) {
        this.mTexts = texts;
        return mInstance;
    }

    public FfmpegCommandBuilder2 setFont(String font) {
        this.mFontFile = font;
        return mInstance;
    }

    public FfmpegCommandBuilder2 setImage(String image) {
        this.mImagePath = image;
        return mInstance;
    }

    public FfmpegCommandBuilder2 setWidth(int width) {
        this.mOutputWidth = width;
        return mInstance;
    }

    public FfmpegCommandBuilder2 setHeight(int height) {
        this.mOutputHeight = height;
        return mInstance;
    }

    public FfmpegCommandBuilder2 setCropFrom(int from) {
        this.mCropFrom = from;
        return mInstance;
    }

    public FfmpegCommandBuilder2 setCropTo(int to) {
        this.mCropTo = to;
        return mInstance;
    }

    public String[] build() {
        mCommand = "";
        mCommands.clear();

        mCommand += buildInputAndCrop();
        mCommand += buildVideoScale();
        mCommand += buildTextsAndImage();
        mCommand += buildOutputAndSettings();

        for (String val : mCommand.split(" ")) {
            val = val.replaceAll("^*", " ");
            mCommands.add(val);
        }
        return mCommands.toArray(new String[0]);
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
        int videoDuration = mCropTo - mCropFrom;

        String whereFormatted = whereAnswer.getAnswer().replace(":", "\\:").replace("'", "'\\\\\\''");
        String whenFormatted = whenAnswer.getAnswer().replace(":", "\\:").replace("'", "'\\\\\\''");

        String whenText = String.format("drawtext=enable='between(t,%d,%d)':fontfile=%s:text='%s'" +
                        ":fontcolor=white:shadowcolor=black:shadowx=1:shadowy=1:fontsize=%d:x=(w-text_w)/1.07:y=30,",
                0, videoDuration, mFontFile, whenFormatted, Constants.HEADER_FONT_SIZE);
        headline += whenText;
        String whereText = String.format("drawtext=enable='between(t,%d,%d)':fontfile=%s:text='%s'" +
                        ":fontcolor=white:shadowcolor=black:shadowx=1:shadowy=1:fontsize=%d:x=(w-text_w)/1.07:y=35+th,",
                0, videoDuration, mFontFile, whereFormatted, Constants.HEADER_FONT_SIZE);
        headline += whereText;

        return headline;
    }

    private String addFooterText(Answer currentAnswer, boolean isLastPosition) {

        String textCommand = "";
        int width = mOutputWidth > mOutputHeight ? Constants.VIDEO_WIDTH : Constants.VIDEO_HEIGHT;
        int height = mOutputWidth > mOutputHeight ? Constants.VIDEO_HEIGHT : Constants.VIDEO_WIDTH;

        int totalSize = currentAnswer.splittedText.size();
        Collections.reverse(currentAnswer.splittedText);
        for (int pos = 0; pos < totalSize; pos++) {
            textCommand += buildSplittedLine(currentAnswer, currentAnswer.splittedText, currentAnswer.splittedText.get(pos), pos, height);
            if (pos < totalSize - 1) {
                textCommand += ",";
            }
        }

        if (!isLastPosition) {
            textCommand += ",";
        }

        return textCommand;
    }

    private String buildSplittedLine(Answer currentAnswer, List<Line> lines, Line currentTextLine, int lineNumber, int height) {

        String textLine;
        int yPos = AndroidUtilities.getYPosition(height, lines, lineNumber);
        textLine = buildLineOutput(currentAnswer, currentTextLine.text, Constants.TEXT_FONT_SIZE, yPos);

        return textLine;
    }

    private String buildLineOutput(Answer currentAnswer, String currentTextLine, int fontSize, int yPos) {

        int from = currentAnswer.getFrom();
        int to = currentAnswer.getTo();
        String text = currentTextLine.replace(":", "\\:").replace("'", "'\\\\\\''");


        return String.format("drawtext=enable='between(t,%d,%d)':fontfile=%s:text='%s'" +
                        ":fontcolor=yellow:shadowcolor=black:shadowx=1:shadowy=1:" +
                        "fontsize=%d:x=(w-tw)/2:y=%d",
                from, to, mFontFile, text, fontSize, yPos);
    }

    private String buildOutputAndSettings() {
        return " -codec:v libx264 -preset ultrafast -r 24 " + mOutputFile;
    }

}
