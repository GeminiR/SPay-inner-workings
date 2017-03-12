package android.widget;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;

public class ZoomButton extends ImageButton implements OnLongClickListener {
    private final Handler mHandler;
    private boolean mIsInLongpress;
    private final Runnable mRunnable;
    private long mZoomSpeed;

    public ZoomButton(Context context) {
        this(context, null);
    }

    public ZoomButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZoomButton(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public ZoomButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mRunnable = new Runnable() {
            public void run() {
                if (ZoomButton.this.hasOnClickListeners() && ZoomButton.this.mIsInLongpress && ZoomButton.this.isEnabled()) {
                    ZoomButton.this.callOnClick();
                    ZoomButton.this.mHandler.postDelayed(this, ZoomButton.this.mZoomSpeed);
                }
            }
        };
        this.mZoomSpeed = 1000;
        this.mHandler = new Handler();
        setOnLongClickListener(this);
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == 3 || event.getAction() == 1) {
            this.mIsInLongpress = false;
        }
        return super.onTouchEvent(event);
    }

    public void setZoomSpeed(long speed) {
        this.mZoomSpeed = speed;
    }

    public boolean onLongClick(View v) {
        this.mIsInLongpress = true;
        this.mHandler.post(this.mRunnable);
        return true;
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        this.mIsInLongpress = false;
        return super.onKeyUp(keyCode, event);
    }

    public void setEnabled(boolean enabled) {
        if (!enabled) {
            setPressed(false);
        }
        super.setEnabled(enabled);
    }

    public boolean dispatchUnhandledMove(View focused, int direction) {
        clearFocus();
        return super.dispatchUnhandledMove(focused, direction);
    }

    public CharSequence getAccessibilityClassName() {
        return ZoomButton.class.getName();
    }
}
