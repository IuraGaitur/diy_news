package video.paxra.com.videoconverter;

import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import video.paxra.com.videoconverter.models.Answer;
import video.paxra.com.videoconverter.utils.AndroidUtilities;
import video.paxra.com.videoconverter.utils.FFMpegUtils;
import video.paxra.com.videoconverter.utils.StringUtils;

/**
 * Created by iura on 7/3/16.
 */
public class CommandsFFMpegUtilTest extends TestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void testCalculateTimeForText_ReturnEquals() {
        List<String> expectedResult = new ArrayList<>();
        expectedResult.add("02:05 ");
        expectedResult.add("07:11 ");
        expectedResult.add("13:16 ");
        ArrayList<Answer> answers = new ArrayList<>();
        answers.add(new Answer(1, "2012-05-14", "header"));
        answers.add(new Answer(2, "Chisinau, Moldova", "header"));
        answers.add(new Answer(3, "O grupa de activisti din Republica Moldova", "text"));
        //answers.add(new Answer(4, "Vreau unire cu Romania", "text"));
        //answers.add(new Answer(5, "Au declarat ca nu sunt buni de nimic si de aceea o sa lese postul si o sa plece acasa", "text"));
        //answers.add(new Answer(6, "Pot fi posibile incaierari dintre baieti", "text"));

        List<Answer> actual = FFMpegUtils.calculateTimeShowForText(answers, 35);
        List<String> actualResult = new ArrayList<>();
        for(Answer answer: actual) {
            actualResult.add(answer.from + ":" + answer.to + " ");
        }

        Assert.assertEquals("Text time are equals", expectedResult, actualResult);
    }

    @Test
    public void testCalculateSmartTimeForText_ReturnEquals() {
        List<String> expectedResult = new ArrayList<>();
        expectedResult.add("02:05 ");
        expectedResult.add("07:11 ");
        expectedResult.add("13:16 ");
        ArrayList<Answer> answers = new ArrayList<>();
        answers.add(new Answer(1, "2012-05-14", "header"));
        answers.add(new Answer(2, "Chisinau, Moldova", "header"));
        answers.add(new Answer(3, "O grupa de activisti din Republica Moldova", "text"));
        //answers.add(new Answer(4, "Vreau unire cu Romania", "text"));
        //answers.add(new Answer(5, "Au declarat ca nu sunt buni de nimic si de aceea o sa lese postul si o sa plece acasa", "text"));
        //answers.add(new Answer(6, "Pot fi posibile incaierari dintre baieti", "text"));

        List<Answer> actual = FFMpegUtils.calculateSmartTimeShowForText(answers, 35);
        List<String> actualResult = new ArrayList<>();
        for(Answer answer: actual) {
            actualResult.add(answer.from + ":" + answer.to + " ");
        }

        Assert.assertEquals("Text time are equals", expectedResult, actualResult);
    }


    @Test
    public void testNumberLinesReturnEqual() {
        int expectedResult = 2;
        String text = "O grupa de activisti din Republica Moldova";
        int actualResult = AndroidUtilities.getNumberOfLines(text, 480, 720, 20);

        Assert.assertEquals("Number lines are equals", expectedResult, actualResult);
    }

    @Test
    public void testXStartPositionReturnEqual() {
        int expectedResult = 35;
        String text = "Au declara ca nu sunt buni de nimic si de";
        int actualResult = AndroidUtilities.getXStartPosition(text, 480, 20);

        Assert.assertEquals("X Positions are equals", expectedResult, actualResult);
    }

    @Test
    public void testYStartPositionReturnEqual() {
        int[] expectedResult = {548, 584, 620, 656};
        String text = "Protestatarii scandează La Pușcarie hotii jos penalii. Kz dms ăsdadsaîățșdas dsașăîădsa îădsațș sadîădas îădsa".toUpperCase();
        int[] actualResult = new int[4];
        actualResult[0] = AndroidUtilities.getYStartPosition(720, 1, 4, 30, 20);
        actualResult[1] = AndroidUtilities.getYStartPosition(720, 2, 4, 30, 20);
        actualResult[2] = AndroidUtilities.getYStartPosition(720, 3, 4, 30, 20);
        actualResult[3] = AndroidUtilities.getYStartPosition(720, 4, 4, 30, 20);
        //actualResult[3] =AndroidUtilities.getYStartPosition(720, 4, 4, 20, 10);

        Assert.assertArrayEquals("Y Positions are equals", expectedResult, actualResult);
    }

    @Test
    public void testCutStringReturnEqual() {
        List<String> expectedResult = new ArrayList<>();
        expectedResult.add("Au declarat ca nu sunt buni de nimic si de aceea ");
        expectedResult.add("o sa lese postul si o sa plece acasa ");
        String text = "Au declarat ca nu sunt buni de nimic si de aceea o sa lese postul si o sa plece acasa ";
        List<String> actualResult = StringUtils.splitStringIntoParts(text, 50);

        Assert.assertEquals("Y Positions are equals", expectedResult, actualResult);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
