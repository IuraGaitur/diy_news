package video.paxra.com.videoconverter.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by iuriegaitur on 10/29/16.
 */
public class AndroidUtilities {

    static float density;
    private static boolean usingHardwareInput;
    private static DisplayMetrics displayMetrics;
    private static Point displaySize;

    public static int dp(float value) {
        if (value == 0) {
            return 0;
        }
        return (int) Math.ceil(density * value);
    }

    public static void checkDisplaySize(Context context, Configuration newConfiguration) {
        try {
            density = context.getResources().getDisplayMetrics().density;
            Configuration configuration = newConfiguration;
            if (configuration == null) {
                configuration = context.getResources().getConfiguration();
            }
            usingHardwareInput = configuration.keyboard != Configuration.KEYBOARD_NOKEYS && configuration.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO;
            WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            if (manager != null) {
                Display display = manager.getDefaultDisplay();
                if (display != null) {
                    display.getMetrics(displayMetrics);
                    display.getSize(displaySize);
                }
            }
            if (configuration.screenWidthDp != Configuration.SCREEN_WIDTH_DP_UNDEFINED) {
                int newSize = (int) Math.ceil(configuration.screenWidthDp * density);
                if (Math.abs(displaySize.x - newSize) > 3) {
                    displaySize.x = newSize;
                }
            }
            if (configuration.screenHeightDp != Configuration.SCREEN_HEIGHT_DP_UNDEFINED) {
                int newSize = (int) Math.ceil(configuration.screenHeightDp * density);
                if (Math.abs(displaySize.y - newSize) > 3) {
                    displaySize.y = newSize;
                }
            }
            Log.e("tmessages", "display size = " + displaySize.x + " " + displaySize.y + " " + displayMetrics.xdpi + "x" + displayMetrics.ydpi);
        } catch (Exception e) {
            Log.e("tmessages", e.getMessage());
        }
    }

    public static int getXStartPosition(String text, int videoWidth, int fontSize) {
        int textLength = text.length();

        //int widthWithoutPadding = videoWidth - (videoWidth * 10 / 100);// get 10% for padding
        int startPos = (videoWidth - (textLength * fontSize /2)) / 2;

        //if(startPosition  < 30) return 30;

        return startPos;

    }

    public static int getYStartPosition(int videoHeight, int lineNumber, int totalLines, int fontSize, int padding) {
        int heightWithoutPadding = videoHeight - (videoHeight * 5 / 100);
        heightWithoutPadding = heightWithoutPadding - fontSize - ((totalLines - lineNumber) * fontSize  + padding) - ((totalLines - lineNumber) * 6);
        return heightWithoutPadding;
    }

    public static int getNumberOfLines(String text, int videoWidth, int videoHeight, int fontSize) {
        int widthWithoutPadding = videoWidth;// get 10% for padding
        int textLength = text.length();
        return ((textLength * (fontSize / 2)) / widthWithoutPadding) + 1;

    }
}
