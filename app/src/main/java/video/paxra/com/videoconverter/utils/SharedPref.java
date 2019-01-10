package video.paxra.com.videoconverter.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {

  Context context;

  public SharedPref(Context context) {
    this.context = context;
  }

  public boolean hasUserSeenTutorial() {
    SharedPreferences sharedPref = context.getSharedPreferences("APP", Context.MODE_PRIVATE);
    return sharedPref.getBoolean("seen_tutorial", false);
  }

  public void setUserSeenTutorial() {
    SharedPreferences sharedPref = context.getSharedPreferences("APP", Context.MODE_PRIVATE);
    sharedPref.edit().putBoolean("seen_tutorial", true).apply();
    return;
  }

  public boolean hasBoughtFonts() {
    SharedPreferences sharedPref = context.getSharedPreferences("APP", Context.MODE_PRIVATE);
    return sharedPref.getBoolean("bought_fonts", false);
  }

  public void setHasBoughtFonts() {
    SharedPreferences sharedPref = context.getSharedPreferences("APP", Context.MODE_PRIVATE);
    sharedPref.edit().putBoolean("bought_fonts", true).apply();
    return;
  }

}
