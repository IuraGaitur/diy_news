package video.paxra.com.videoconverter;

import android.test.AndroidTestCase;

import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;
import video.paxra.com.videoconverter.utils.CommandsFFMpegUtil;

/**
 * Created by iura on 7/3/16.
 */
public class CommandsFFMpegUtilTest extends TestCase{

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void testBuildCommand() {
        String input = "";
        String output = "";
        String[] texts = new String[]{};
        String color = "";

        String[] expected = ("-i input.mp4 -vf \"[in]drawtext=enable='between(t,3,9)':" +
                "fontfile=/home/iura/Downloads/font.ttf:text='La biblioteca > centrala': " +
                "fontcolor=white: fontsize=24: box=1: boxcolor=black@0.5:boxborderw=5: x=(w-text_w)/2:" +
                " y=(h-text_h)/2, drawtext=enable='between(t,3,9)':fontfile=/home/iura/Downloads/font.ttf:text='L " +
                "final': fontcolor=white: fontsize=24: box=1: boxcolor=black@0.5:boxborderw=5: x=(w-text_w)/5: y=20\" " +
                "-codec:v libx264 -crf 21 -bf 2 -flags +cgop -pix_fmt yuv420p -codec:a aac -strict -2 -b:a 384k -r:a " +
                "48000 -movflags faststart output.mp4").split(" ");

        String[] actual = CommandsFFMpegUtil.buildCommand(input, output, texts, color);

        Assert.assertArrayEquals("Strings are equal", expected, actual);
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
