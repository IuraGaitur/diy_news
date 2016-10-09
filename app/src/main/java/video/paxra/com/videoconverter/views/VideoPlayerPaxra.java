/*
package video.paxra.com.videoconverter.views;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import fm.jiecao.jcvideoplayer_lib.JCMediaManager;
import fm.jiecao.jcvideoplayer_lib.JCUtils;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerManager;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

*/
/**
 * Created by crove on 09.10.2016.
 *//*


public class VideoPlayerPaxra extends JCVideoPlayerStandard {
    public VideoPlayerPaxra(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VideoPlayerPaxra(Context context) {
        super(context);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == fm.jiecao.jcvideoplayer_lib.R.id.thumb) {
            if (TextUtils.isEmpty(url)) {
                Toast.makeText(getContext(), getResources().getString(fm.jiecao.jcvideoplayer_lib.R.string.no_url), Toast.LENGTH_SHORT).show();
                return;
            }
            if (currentState == CURRENT_STATE_NORMAL) {
                if (!url.startsWith("file") && !JCUtils.isWifiConnected(getContext()) && !WIFI_TIP_DIALOG_SHOWED) {
                    showWifiDialog();
                    return;
                }
                startPlayLogic();
            } else if (currentState == CURRENT_STATE_AUTO_COMPLETE) {
                onClickUiToggle();
            }
        } else if (i == fm.jiecao.jcvideoplayer_lib.R.id.surface_container) {
            startDismissControlViewTimer();
        } else if (i == fm.jiecao.jcvideoplayer_lib.R.id.back) {
            backPress();
        } else if (i == fm.jiecao.jcvideoplayer_lib.R.id.back_tiny) {
            if (JCVideoPlayerManager.CURRENT_SCROLL_LISTENER_LIST.get(0).get() != null) {
                if (JCVideoPlayerManager.CURRENT_SCROLL_LISTENER_LIST.get(0).get().getUrl() != JCMediaManager.instance().mediaPlayer.getDataSource()) {
//                    if (!((JCVideoPlayer) JCVideoPlayerManager.CURRENT_SCROLL_LISTENER_LIST.get(0).get()).isShown()) {
                    releaseAllVideos();
                    return;
//                    }
                }
            }
            backPress();
        }
    }
}
*/
