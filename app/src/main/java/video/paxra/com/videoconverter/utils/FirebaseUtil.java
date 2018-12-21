package video.paxra.com.videoconverter.utils;

import android.app.Activity;
import android.os.Bundle;
import com.google.firebase.analytics.FirebaseAnalytics;
import video.paxra.com.videoconverter.activities.ShareActivity;

public class FirebaseUtil {

  public static void logSplash(Activity activity) {
    Bundle bundle = new Bundle();
    bundle.putString("EVENT", "START");
    FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(activity);
    mFirebaseAnalytics.logEvent("SPLASH", bundle);
  }

  public static void logMenu(Activity activity) {
    Bundle bundle = new Bundle();
    bundle.putString("EVENT", "MENU");
    FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(activity);
    mFirebaseAnalytics.logEvent("MENU", bundle);
  }

  public static void logSelect(Activity activity) {
    Bundle bundle = new Bundle();
    bundle.putString("EVENT", "SELECT");
    FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(activity);
    mFirebaseAnalytics.logEvent("MENU", bundle);
  }

  public static void logRecord(Activity activity) {
    Bundle bundle = new Bundle();
    bundle.putString("EVENT", "RECORD");
    FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(activity);
    mFirebaseAnalytics.logEvent("MENU", bundle);
  }

  public static void logCut(Activity activity, int from, int to, int duration) {
    Bundle bundle = new Bundle();
    bundle.putString("EVENT", "CROP");
    bundle.putString("FROM", from + "");
    bundle.putString("TO", to + "");
    bundle.putString("DURATION", duration + "");
    FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(activity);
    mFirebaseAnalytics.logEvent("CROP", bundle);
  }

  public static void logAnswer(Activity activity) {
    Bundle bundle = new Bundle();
    bundle.putString("EVENT", "ANSWER");
    FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(activity);
    mFirebaseAnalytics.logEvent("ANSWER", bundle);
  }

  public static void logConvert(Activity activity) {
    Bundle bundle = new Bundle();
    bundle.putString("EVENT", "CONVERT");
    FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(activity);
    mFirebaseAnalytics.logEvent("CONVERT", bundle);
  }

  public static void logSuccessConvert(Activity activity) {
    Bundle bundle = new Bundle();
    bundle.putString("EVENT", "CONVERT SUCCESS");
    FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(activity);
    mFirebaseAnalytics.logEvent("CONVERT", bundle);
  }

  public static void logFailConvert(Activity activity, String error) {
    Bundle bundle = new Bundle();
    bundle.putString("EVENT", "CONVERT FAIL");
    bundle.putString("ERROR", error);
    FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(activity);
    mFirebaseAnalytics.logEvent("CONVERT", bundle);
  }

  public static void logShare(Activity activity) {
    Bundle bundle = new Bundle();
    bundle.putString("EVENT", "SHARE");
    FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(activity);
    mFirebaseAnalytics.logEvent("SHARE", bundle);
  }

  public static void logSave(Activity activity) {
    Bundle bundle = new Bundle();
    bundle.putString("EVENT", "SAVE");
    FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(activity);
    mFirebaseAnalytics.logEvent("SHARE", bundle);
  }

  public static void logQuit(Activity activity) {
    Bundle bundle = new Bundle();
    bundle.putString("EVENT", "QUIT");
    FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(activity);
    mFirebaseAnalytics.logEvent("SHARE", bundle);
  }

  public static void logShareApp(Activity activity) {
    Bundle bundle = new Bundle();
    bundle.putString("EVENT", "SHARE EXTERN");
    FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(activity);
    mFirebaseAnalytics.logEvent("SHARE", bundle);
  }

  public static void logYoutube(Activity activity) {
    Bundle bundle = new Bundle();
    bundle.putString("EVENT", "YOUTUBE");
    FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(activity);
    mFirebaseAnalytics.logEvent("SHARE", bundle);
  }
}
