package video.paxra.com.videoconverter.activities.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import video.paxra.com.videoconverter.R;

public class TutorialAdapter extends PagerAdapter{

  int[] images = new int[] {};
  int[] texts = new int[] {};

  public TutorialAdapter(int[] images, int[] texts) {
    this.images = images;
    this.texts = texts;
  }

  @Override public int getCount() {
    return images.length;
  }

  @Override public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
    return view == o;
  }

  @NonNull @Override public Object instantiateItem(@NonNull ViewGroup container, int position) {
    Context context = container.getContext();
    LayoutInflater inflater = LayoutInflater.from(context);
    View layout = inflater.inflate(R.layout.view_tutorial, null);
    ((ImageView)layout.findViewById(R.id.image)).setImageResource(images[position]);
    ((TextView)layout.findViewById(R.id.text)).setText(context.getResources().getText(texts[position]));
    container.addView(layout);
    return layout;
  }

  @Override
  public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
    ((ViewPager) container).removeView((View) object);
  }
}
