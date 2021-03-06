package video.paxra.com.videoconverter.activities.adapters;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import video.paxra.com.videoconverter.R;
import video.paxra.com.videoconverter.activities.EditFontActivity;
import video.paxra.com.videoconverter.utils.FontCache;

public class FontsAdapter extends BaseAdapter {

  private LayoutInflater flater;
  String[] data;
  Map<String, String> fonts;

  public FontsAdapter(Activity context, String[] fonts, Map<String, String> fontsFile){
    this.data = fonts;
    this.fonts = fontsFile;
    flater = context.getLayoutInflater();
  }

  @Override public int getCount() {
    return this.data.length;
  }

  @Override public String getItem(int position) {
    return this.data[position];
  }

  @Override public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    String rowItem = getItem(position);
    View rowview = flater.inflate(R.layout.item_font,null,true);
    ((TextView) rowview.findViewById(R.id.initial)).setText("As");
    ((TextView) rowview.findViewById(R.id.title)).setText(rowItem);

    Typeface customFont = FontCache.getTypeface(fonts.get(data[position]), flater.getContext());
    ((TextView) rowview.findViewById(R.id.initial)).setTypeface(customFont);
    return rowview;
  }
}
