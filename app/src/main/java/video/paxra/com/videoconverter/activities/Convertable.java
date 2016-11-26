package video.paxra.com.videoconverter.activities;

/**
 * Created by iura on 9/30/16.
 */
public interface Convertable {
    void onStartConvert();
    void onProgressConvert(String message);
    void onFailureConvert(String message);
    void onSuccessConvert(String message);
    void onFinishConvert();
}
