package video.paxra.com.videoconverter.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import video.paxra.com.videoconverter.utils.FontCache;

/**
 * Created by crove on 09.10.2016.
 */

public class FontTextViewRegular extends TextView {

    public FontTextViewRegular(Context context) {
        super(context);

        applyCustomFont(context);
    }

    public FontTextViewRegular(Context context, AttributeSet attrs) {
        super(context, attrs);

        applyCustomFont(context);
    }

    public FontTextViewRegular(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface("AvenirNext-Regular.ttf", context);
        setTypeface(customFont);
    }
}
