package video.paxra.com.videoconverter.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


import java.util.ArrayList;

import video.paxra.com.videoconverter.R;
import video.paxra.com.videoconverter.utils.AndroidUtilities;

@TargetApi(10)
public class VideoTimelineView extends View {

    private long videoLength;
    private float progressLeft;
    private float progressRight = 1;
    private Paint paint;
    private Paint paint2;
    private boolean pressedLeft;
    private boolean pressedRight;
    private float pressDx;
    private MediaMetadataRetriever mediaMetadataRetriever;
    private VideoTimelineViewDelegate delegate;
    private ArrayList<Bitmap> frames = new ArrayList<>();
    private AsyncTask<Integer, Integer, Bitmap> currentTask;
    private static final Object sync = new Object();
    private long frameTimeOffset;
    private int frameWidth;
    private int frameHeight;
    private int framesToLoad;
    private Bitmap mTrimImage;
    private int mVideoWidth;
    private int mVideoHeight;
    private int mVideoRotation;

    public interface VideoTimelineViewDelegate {
        void onLeftProgressChanged(float progress);
        void onRightProgressChanged(float progress);
    }

    public VideoTimelineView(Context context) {
        super(context);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(0xffffffff);
        paint2 = new Paint();
        paint2.setColor(0x7f000000);
    }

    public VideoTimelineView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(0xffffffff);
        paint2 = new Paint();
        paint2.setColor(0x7f000000);
        mTrimImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.trimmer);
        int height = AndroidUtilities.dp(74);
        int width = (int)(height * 0.28);
        if(width <= 0) width = 1;
        if(height <= 0) height = 1;
        mTrimImage = Bitmap.createScaledBitmap(mTrimImage, width, height, true);
    }

    public VideoTimelineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(0xffffffff);
        paint2 = new Paint();
        paint2.setColor(0x7f000000);
    }


    public float getLeftProgress() {
        return progressLeft;
    }

    public float getRightProgress() {
        return progressRight;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event == null) {
            return false;
        }
        float x = event.getX();
        float y = event.getY();

        int width = getMeasuredWidth() - AndroidUtilities.dp(32);
        int startX = (int)(width * progressLeft) + AndroidUtilities.dp(16);
        int endX = (int)(width * progressRight) + AndroidUtilities.dp(16);

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int additionWidth = AndroidUtilities.dp(12);
            if (startX - additionWidth <= x && x <= startX + additionWidth && y >= 0 && y <= getMeasuredHeight()) {
                pressedLeft = true;
                pressDx = (int)(x - startX);
                getParent().requestDisallowInterceptTouchEvent(true);
                invalidate();
                return true;
            } else if (endX - additionWidth <= x && x <= endX + additionWidth && y >= 0 && y <= getMeasuredHeight()) {
                pressedRight = true;
                pressDx = (int)(x - endX);
                getParent().requestDisallowInterceptTouchEvent(true);
                invalidate();
                return true;
            }
        } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
            if (pressedLeft) {
                pressedLeft = false;
                return true;
            } else if (pressedRight) {
                pressedRight = false;
                return true;
            }
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if (pressedLeft) {
                startX = (int)(x - pressDx);
                if (startX < AndroidUtilities.dp(16)) {
                    startX = AndroidUtilities.dp(16);
                } else if (startX > endX) {
                    startX = endX;
                }
                progressLeft = (float)(startX - AndroidUtilities.dp(16)) / (float)width;
                if (delegate != null) {
                    delegate.onLeftProgressChanged(progressLeft);
                }
                invalidate();
                return true;
            } else if (pressedRight) {
                endX = (int)(x - pressDx);
                if (endX < startX) {
                    endX = startX;
                } else if (endX > width + AndroidUtilities.dp(16)) {
                    endX = width + AndroidUtilities.dp(16);
                }
                progressRight = (float)(endX - AndroidUtilities.dp(16)) / (float)width;
                if (delegate != null) {
                    delegate.onRightProgressChanged(progressRight);
                }
                invalidate();
                return true;
            }
        }
        return false;
    }

    public void setVideoPath(String path) {
        mediaMetadataRetriever = new MediaMetadataRetriever();
        try {
            mediaMetadataRetriever.setDataSource(path);
            String duration = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            String height = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);
            String width = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
            String rotation = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION);
            videoLength = Long.parseLong(duration);
            mVideoWidth = Integer.parseInt(width);
            mVideoHeight = Integer.parseInt(height);
            mVideoRotation = Integer.parseInt(rotation);

        } catch (Exception ex) {
            String err = (ex.getMessage()==null)?"SD Card failed":ex.getMessage();
            Log.e("sdcard-err2:",err);
            //Log.e("tmessages", e.getMessage());
        }
    }

    public void setDelegate(VideoTimelineViewDelegate delegate) {
        this.delegate = delegate;
    }

    private void reloadFrames(int frameNum) {
        if (mediaMetadataRetriever == null) {
            return;
        }
        if (frameNum == 0) {
            frameHeight = AndroidUtilities.dp(70);
            if(frameHeight > 0) {
                framesToLoad = (getMeasuredWidth() - AndroidUtilities.dp(16)) / frameHeight;
                frameWidth = (int) Math.ceil((float) (getMeasuredWidth() - AndroidUtilities.dp(16)) / (float) framesToLoad);
                frameTimeOffset = videoLength / framesToLoad;
            }
        }
        currentTask = new AsyncTask<Integer, Integer, Bitmap>() {
            private int frameNum = 0;

            @Override
            protected Bitmap doInBackground(Integer... objects) {
                frameNum = objects[0];
                Bitmap bitmap = null;
                if (isCancelled()) {
                    return null;
                }
                try {
                    bitmap = mediaMetadataRetriever.getFrameAtTime(frameTimeOffset * frameNum * 1000);
                    if (isCancelled()) {
                        return null;
                    }
                    if (bitmap != null) {
                        Bitmap result = Bitmap.createBitmap(frameWidth, frameHeight, bitmap.getConfig());
                        Canvas canvas = new Canvas(result);
                        float scaleX = (float) frameWidth / (float) bitmap.getWidth();
                        float scaleY = (float) frameHeight / (float) bitmap.getHeight();
                        float scale = scaleX > scaleY ? scaleX : scaleY;
                        int w = (int) (bitmap.getWidth() * scale);
                        int h = (int) (bitmap.getHeight() * scale);
                        Rect srcRect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
                        Rect destRect = new Rect((frameWidth - w) / 2, (frameHeight - h) / 2, w, h);
                        canvas.drawBitmap(bitmap, srcRect, destRect, null);
                        bitmap.recycle();
                        bitmap = result;
                    }
                } catch (Exception e) {
                    Log.e("tmessages", e.getMessage());
                }
                return bitmap;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                if (!isCancelled()) {
                    frames.add(bitmap);
                    invalidate();
                    if (frameNum < framesToLoad) {
                        reloadFrames(frameNum + 1);
                    }
                }
            }
        };
        currentTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, frameNum, null, null);
    }

    public void destroy() {
        synchronized (sync) {
            try {
                if (mediaMetadataRetriever != null) {
                    mediaMetadataRetriever.release();
                    mediaMetadataRetriever = null;
                }
            } catch (Exception e) {
                Log.e("tmessages", e.getMessage());
            }
        }
        for (Bitmap bitmap : frames) {
            if (bitmap != null) {
                bitmap.recycle();
            }
        }
        frames.clear();
        if (currentTask != null) {
            currentTask.cancel(true);
            currentTask = null;
        }
    }

    public void clearFrames() {
        for (Bitmap bitmap : frames) {
            if (bitmap != null) {
                bitmap.recycle();
            }
        }
        frames.clear();
        if (currentTask != null) {
            currentTask.cancel(true);
            currentTask = null;
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getMeasuredWidth() - AndroidUtilities.dp(36);
        int startX = (int)(width * progressLeft) + AndroidUtilities.dp(16);
        int endX = (int)(width * progressRight) + AndroidUtilities.dp(16);

        canvas.save();
        canvas.clipRect(AndroidUtilities.dp(16), 0, width + AndroidUtilities.dp(20), AndroidUtilities.dp(74));
        if (frames.isEmpty() && currentTask == null) {
            reloadFrames(0);
        } else {
            int offset = 0;
            for (int a = 0; a < frames.size(); a++) {
                Bitmap bitmap = frames.get(a);
                if (bitmap != null) {
                    canvas.drawBitmap(bitmap, AndroidUtilities.dp(16) + offset * frameWidth, AndroidUtilities.dp(2), null);
                }
                offset++;
            }
        }

        canvas.drawRect(AndroidUtilities.dp(16), AndroidUtilities.dp(2), startX, AndroidUtilities.dp(72), paint2);
        canvas.drawRect(endX + AndroidUtilities.dp(4), AndroidUtilities.dp(2), AndroidUtilities.dp(16) + width + AndroidUtilities.dp(4), AndroidUtilities.dp(72), paint2);

        canvas.drawRect(startX, 0, startX + AndroidUtilities.dp(2), AndroidUtilities.dp(74), paint);
        canvas.drawRect(endX + AndroidUtilities.dp(2), 0, endX + AndroidUtilities.dp(4), AndroidUtilities.dp(74), paint);
        //canvas.drawRect(startX + AndroidUtilities.dp(2), 0, endX + AndroidUtilities.dp(4), AndroidUtilities.dp(2), paint);
        //canvas.drawRect(startX + AndroidUtilities.dp(2), AndroidUtilities.dp(52), endX + AndroidUtilities.dp(4), AndroidUtilities.dp(44), paint);
        canvas.restore();

        canvas.drawBitmap(mTrimImage, startX, 0, paint);
        canvas.drawBitmap(mTrimImage, endX - AndroidUtilities.dp(10), 0, paint);
        //canvas.drawCircle(startX, getMeasuredHeight() / 2, AndroidUtilities.dp(7), paint);
        //canvas.drawCircle(endX + AndroidUtilities.dp(4), getMeasuredHeight() / 2, AndroidUtilities.dp(7), paint);
    }

    public long getVideoLength() {
        return videoLength;
    }

    public int getVideoWidth() {
        return mVideoWidth;
    }

    public int getVideoRotation() {
        return mVideoRotation;
    }

    public int getVideoHeight() {
        return mVideoHeight;
    }
}