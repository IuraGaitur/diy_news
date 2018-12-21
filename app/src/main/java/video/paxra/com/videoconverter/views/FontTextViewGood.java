package video.paxra.com.videoconverter.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import video.paxra.com.videoconverter.utils.FontCache;

/**
 * Created by iuriegaitur on 12/4/17.
 */

public class FontTextViewGood extends android.support.v7.widget.AppCompatTextView {

  public FontTextViewGood(Context context) {
    super(context);

    applyCustomFont(context);
  }

  public FontTextViewGood(Context context, AttributeSet attrs) {
    super(context, attrs);

    applyCustomFont(context);
  }

  public FontTextViewGood(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);

    applyCustomFont(context);
  }

  private void applyCustomFont(Context context) {
    Typeface customFont = FontCache.getTypeface("resources/font_simple.ttf", context);
    setTypeface(customFont);
  }
}
