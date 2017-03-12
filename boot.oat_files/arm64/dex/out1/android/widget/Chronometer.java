package android.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.RemotableViewMethod;
import android.widget.RemoteViews.RemoteView;
import com.android.internal.R;
import java.util.Formatter;
import java.util.IllegalFormatException;
import java.util.Locale;

@RemoteView
public class Chronometer extends TextView {
    private static final int HOUR_IN_SEC = 3600;
    private static final int MIN_IN_SEC = 60;
    private static final String TAG = "Chronometer";
    private static final int TICK_WHAT = 2;
    private long mBase;
    private String mFormat;
    private StringBuilder mFormatBuilder;
    private Formatter mFormatter;
    private Object[] mFormatterArgs;
    private Locale mFormatterLocale;
    private Handler mHandler;
    private boolean mLogged;
    private long mNow;
    private OnChronometerTickListener mOnChronometerTickListener;
    private StringBuilder mRecycle;
    private boolean mRunning;
    private boolean mStarted;
    private boolean mVisible;

    public interface OnChronometerTickListener {
        void onChronometerTick(Chronometer chronometer);
    }

    public Chronometer(Context context) {
        this(context, null, 0);
    }

    public Chronometer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Chronometer(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public Chronometer(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mFormatterArgs = new Object[1];
        this.mRecycle = new StringBuilder(8);
        this.mHandler = new Handler() {
            public void handleMessage(Message m) {
                if (Chronometer.this.mRunning) {
                    Chronometer.this.updateText(SystemClock.elapsedRealtime());
                    Chronometer.this.dispatchChronometerTick();
                    sendMessageDelayed(Message.obtain(this, 2), 1000);
                }
            }
        };
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Chronometer, defStyleAttr, defStyleRes);
        setFormat(a.getString(0));
        a.recycle();
        init();
    }

    private void init() {
        this.mBase = SystemClock.elapsedRealtime();
        updateText(this.mBase);
    }

    @RemotableViewMethod
    public void setBase(long base) {
        this.mBase = base;
        dispatchChronometerTick();
        updateText(SystemClock.elapsedRealtime());
    }

    public long getBase() {
        return this.mBase;
    }

    @RemotableViewMethod
    public void setFormat(String format) {
        this.mFormat = format;
        if (format != null && this.mFormatBuilder == null) {
            this.mFormatBuilder = new StringBuilder(format.length() * 2);
        }
    }

    public String getFormat() {
        return this.mFormat;
    }

    public void setOnChronometerTickListener(OnChronometerTickListener listener) {
        this.mOnChronometerTickListener = listener;
    }

    public OnChronometerTickListener getOnChronometerTickListener() {
        return this.mOnChronometerTickListener;
    }

    public void start() {
        this.mStarted = true;
        updateRunning();
    }

    public void stop() {
        this.mStarted = false;
        updateRunning();
    }

    @RemotableViewMethod
    public void setStarted(boolean started) {
        this.mStarted = started;
        updateRunning();
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mVisible = false;
        updateRunning();
    }

    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        this.mVisible = visibility == 0;
        updateRunning();
    }

    private synchronized void updateText(long now) {
        this.mNow = now;
        String text = DateUtils.formatElapsedTime(this.mRecycle, (now - this.mBase) / 1000);
        if (this.mFormat != null) {
            Locale loc = Locale.getDefault();
            if (this.mFormatter == null || !loc.equals(this.mFormatterLocale)) {
                this.mFormatterLocale = loc;
                this.mFormatter = new Formatter(this.mFormatBuilder, loc);
            }
            this.mFormatBuilder.setLength(0);
            this.mFormatterArgs[0] = text;
            try {
                this.mFormatter.format(this.mFormat, this.mFormatterArgs);
                text = this.mFormatBuilder.toString();
            } catch (IllegalFormatException e) {
                if (!this.mLogged) {
                    Log.w(TAG, "Illegal format string: " + this.mFormat);
                    this.mLogged = true;
                }
            }
        }
        setText((CharSequence) text);
    }

    private void updateRunning() {
        boolean running = this.mVisible && this.mStarted;
        if (running != this.mRunning) {
            if (running) {
                updateText(SystemClock.elapsedRealtime());
                dispatchChronometerTick();
                this.mHandler.sendMessageDelayed(Message.obtain(this.mHandler, 2), 1000);
            } else {
                this.mHandler.removeMessages(2);
            }
            this.mRunning = running;
        }
    }

    void dispatchChronometerTick() {
        if (this.mOnChronometerTickListener != null) {
            this.mOnChronometerTickListener.onChronometerTick(this);
        }
    }

    private static String formatDuration(long ms) {
        Resources res = Resources.getSystem();
        StringBuilder text = new StringBuilder();
        int duration = (int) (ms / 1000);
        if (duration < 0) {
            duration = -duration;
        }
        int h = 0;
        int m = 0;
        if (duration >= HOUR_IN_SEC) {
            h = duration / HOUR_IN_SEC;
            duration -= h * HOUR_IN_SEC;
        }
        if (duration >= 60) {
            m = duration / 60;
            duration -= m * 60;
        }
        int s = duration;
        if (h > 0) {
            try {
                text.append(res.getQuantityString(R.plurals.duration_hours, h, new Object[]{Integer.valueOf(h)}));
            } catch (NotFoundException e) {
                return null;
            }
        }
        if (m > 0) {
            if (text.length() > 0) {
                text.append(' ');
            }
            text.append(res.getQuantityString(R.plurals.duration_minutes, m, new Object[]{Integer.valueOf(m)}));
        }
        if (text.length() > 0) {
            text.append(' ');
        }
        text.append(res.getQuantityString(R.plurals.duration_seconds, s, new Object[]{Integer.valueOf(s)}));
        return text.toString();
    }

    public CharSequence getContentDescription() {
        return formatDuration(this.mNow - this.mBase);
    }

    public CharSequence getAccessibilityClassName() {
        return Chronometer.class.getName();
    }
}
