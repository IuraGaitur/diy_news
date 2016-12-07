package video.paxra.com.videoconverter;

import android.test.AndroidTestCase;
import android.util.Log;

import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

import video.paxra.com.videoconverter.models.Answer;
import video.paxra.com.videoconverter.utils.CommandsFFMpegUtil;
import video.paxra.com.videoconverter.utils.AndroidUtilities;
import video.paxra.com.videoconverter.utils.FFMpegUtils;
import video.paxra.com.videoconverter.utils.StringUtils;

/**
 * Created by iura on 7/3/16.
 */
public class CommandsFFMpegUtilTest extends TestCase{

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void testCalculateTimeForText() {
        List<Answer> expectedResult = null;
        String text = "Au declarat ca nu sunt buni de nimic si de aceea o sa lese postul si o sa plece acasa";
        ArrayList<Answer> answers = new ArrayList<>();
        answers.add(new Answer(1, "2012-05-14", "header"));
        answers.add(new Answer(2, "Chisinau, Moldova", "header"));
        answers.add(new Answer(3, "O grupa de activisti din Republica Moldova", "text"));
        answers.add(new Answer(4, "Vreau unire cu Romania", "text"));
        answers.add(new Answer(5, "Au declarat ca nu sunt buni de nimic si de aceea o sa lese postul si o sa plece acasa", "text"));
        //answers.add(new Answer(6, "Pot fi posibile incaierari dintre baieti", "text"));

        List<Answer> actualResult = FFMpegUtils.calculateTimeShowForText(answers, 35);
        for (Answer answer: actualResult) {
            Log.d("Answer", answer.toString());
        }

        Assert.assertEquals("Text time are equals", expectedResult, actualResult);
    }



    @Test
    public void testNumberLinesReturnEqual() {
        int expectedResult = 1;
        String text = "O grupa de activisti din Republica Moldova";
        int actualResult = AndroidUtilities.getNumberOfLines(text, 480, 720, 20);


        Assert.assertEquals("Number lines are equals", expectedResult, actualResult);
    }

    @Test
    public void testXStartPositionReturnEqual() {
        int expectedResult = 100;
        String text = "Au declara ca nu sunt buni de nimic si de";
        int actualResult = AndroidUtilities.getXStartPosition(text, 480, 20);


        Assert.assertEquals("X Positions are equals", expectedResult, actualResult);
    }

    @Test
    public void testYStartPositionReturnEqual() {
        int expectedResult = 100;
        String text = "La monumentul de la gara de nord s-a intimplat o catastrofa de tip mare de aceea socot ca nu e nevoie de facut";
        int actualResult = AndroidUtilities.getYStartPosition(720, 2, 3, 20, 10);


        Assert.assertEquals("Y Positions are equals", expectedResult, actualResult);
    }

    @Test
    public void testCutStringReturnEqual() {
        int expectedResult = 100;
        String text = "Au declarat ca nu sunt buni de nimic si de aceea o sa lese postul si o sa plece acasa";
        List<String> actualResult = StringUtils.splitStringIntoParts(text, 2);


        Assert.assertEquals("Y Positions are equals", expectedResult, actualResult);
    }

    @Test
    public void testConvertCelsiusToFahrenheit() {
        float actual = 100;
        // expected value is 100
        float expected = 100;
        // use this method because float is not precise
        assertEquals("Conversion from celsius to fahrenheit failed", expected,
                actual, 0.001);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }



}
