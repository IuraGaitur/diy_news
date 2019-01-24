package video.paxra.com.videoconverter.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import video.paxra.com.videoconverter.utils.FontCache;

/**
 * Created by crove on 09.10.2016.
 */

public class FontTextViewDemiBold extends TextView {

    public FontTextViewDemiBold(Context context) {
        super(context);

        applyCustomFont(context);
    }

    public FontTextViewDemiBold(Context context, AttributeSet attrs) {
        super(context, attrs);

        applyCustomFont(context);
    }

    public FontTextViewDemiBold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface("resources/AvenirNext-DemiBold.ttf", context);
        setTypeface(customFont);
    }
}
