package video.paxra.com.videoconverter.activities;

/**
 * Created by iura on 9/30/16.
 */
public interface Convertable {
    void onStartConvert();
    void onProgressConvert();
    void onFailureConvert();
    void onSuccessConvert();
    void onFinishConvert();
}
