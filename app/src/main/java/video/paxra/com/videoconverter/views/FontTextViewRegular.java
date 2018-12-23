package video.paxra.com.videoconverter.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import video.paxra.com.videoconverter.utils.FontCache;


public class FontTextViewRegular extends android.support.v7.widget.AppCompatTextView {

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
