package android.widget;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Insets;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.Region.Op;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.provider.Settings$System;
import android.text.Layout;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.AllCapsTransformationMethod;
import android.text.method.TransformationMethod2;
import android.util.AttributeSet;
import android.util.FloatProperty;
import android.util.Log;
import android.util.MathUtils;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewStructure;
import android.view.WindowManager.LayoutParams;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import com.android.internal.R;
import com.sec.android.app.CscFeature;

public class Switch extends CompoundButton {
    private static final int[] CHECKED_STATE_SET = new int[]{R.attr.state_checked};
    private static final int MONOSPACE = 3;
    private static final int SANS = 1;
    private static final int SERIF = 2;
    private static final String TAG = "Switch";
    private static final int THUMB_ANIMATION_DURATION = 250;
    private static final FloatProperty<Switch> THUMB_POS = new FloatProperty<Switch>("thumbPos") {
        public Float get(Switch object) {
            return Float.valueOf(object.mThumbPosition);
        }

        public void setValue(Switch object, float value) {
            object.setThumbPosition(value);
        }
    };
    private static final int TOUCH_MODE_DOWN = 1;
    private static final int TOUCH_MODE_DRAGGING = 2;
    private static final int TOUCH_MODE_IDLE = 0;
    private static final int TW_CHANGE_FLING_VELOCITY = 2000;
    private static final int TW_MIN_FLING_VELOCITY = 500;
    AnimatorListener mAnimatorListener;
    private boolean mHasThumbTint;
    private boolean mHasThumbTintMode;
    private boolean mHasTrackTint;
    private boolean mHasTrackTintMode;
    private boolean mIsDeviceDefaultLight;
    private boolean mIsSetUsDelta;
    private boolean mIsThemeChanged;
    private int mMinFlingVelocity;
    private Layout mOffLayout;
    private Layout mOnLayout;
    private ObjectAnimator mPositionAnimator;
    private boolean mShowText;
    private boolean mSplitTrack;
    private int mSwitchBottom;
    private int mSwitchHeight;
    private int mSwitchLeft;
    private int mSwitchMinWidth;
    private int mSwitchPadding;
    private int mSwitchRight;
    private int mSwitchTop;
    private TransformationMethod2 mSwitchTransformationMethod;
    private int mSwitchWidth;
    private final Rect mTempRect;
    private ColorStateList mTextColors;
    private CharSequence mTextOff;
    private CharSequence mTextOn;
    private TextPaint mTextPaint;
    private Drawable mThumbDrawable;
    private float mThumbPosition;
    private int mThumbTextPadding;
    private ColorStateList mThumbTintList;
    private Mode mThumbTintMode;
    private int mThumbWidth;
    private int mTouchMode;
    private int mTouchSlop;
    private float mTouchX;
    private float mTouchY;
    private Drawable mTrackDrawable;
    private ColorStateList mTrackTintList;
    private Mode mTrackTintMode;
    private VelocityTracker mVelocityTracker;

    public Switch(Context context) {
        this(context, null);
    }

    public Switch(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.switchStyle);
    }

    public Switch(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public Switch(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mThumbTintList = null;
        this.mThumbTintMode = null;
        this.mHasThumbTint = false;
        this.mHasThumbTintMode = false;
        this.mTrackTintList = null;
        this.mTrackTintMode = null;
        this.mHasTrackTint = false;
        this.mHasTrackTintMode = false;
        this.mVelocityTracker = VelocityTracker.obtain();
        this.mTempRect = new Rect();
        this.mAnimatorListener = new AnimatorListener() {
            public void onAnimationStart(Animator animator) {
            }

            public void onAnimationEnd(Animator animator) {
            }

            public void onAnimationCancel(Animator animator) {
                Log.i(Switch.TAG, "onAnimationCancel");
            }

            public void onAnimationRepeat(Animator animator) {
            }
        };
        TypedValue outValue = new TypedValue();
        TypedValue colorValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.parentIsDeviceDefault, outValue, false);
        context.getTheme().resolveAttribute(R.attr.parentIsDeviceDefaultDark, colorValue, true);
        if (outValue.data != 0 && colorValue.data == 0) {
            this.mIsDeviceDefaultLight = true;
            this.mIsThemeChanged = Settings$System.getString(context.getContentResolver(), "current_sec_active_themepackage") != null;
            this.mIsSetUsDelta = "us".equals(CscFeature.getInstance().getString("CscFeature_Framework_ConfigOpStyleSwitchButtonWidget"));
        }
        this.mTextPaint = new TextPaint(1);
        Resources res = getResources();
        this.mTextPaint.density = res.getDisplayMetrics().density;
        this.mTextPaint.setCompatibilityScaling(res.getCompatibilityInfo().applicationScale);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Switch, defStyleAttr, defStyleRes);
        this.mThumbDrawable = a.getDrawable(2);
        if (this.mThumbDrawable != null) {
            this.mThumbDrawable.setCallback(this);
        }
        this.mTrackDrawable = a.getDrawable(4);
        if (this.mTrackDrawable != null) {
            this.mTrackDrawable.setCallback(this);
        }
        try {
            if (this.mIsDeviceDefaultLight && this.mIsSetUsDelta && !this.mIsThemeChanged) {
                this.mThumbDrawable = res.getDrawable(R.drawable.tw_switch_thumb_material_anim_us, context.getTheme());
            }
        } catch (NotFoundException e) {
            Log.e(TAG, "Switch Drawable not found", e);
            a.recycle();
        }
        this.mTextOn = a.getText(0);
        this.mTextOff = a.getText(1);
        this.mShowText = a.getBoolean(11, true);
        this.mThumbTextPadding = a.getDimensionPixelSize(7, 0);
        this.mSwitchMinWidth = a.getDimensionPixelSize(5, 0);
        this.mSwitchPadding = a.getDimensionPixelSize(6, 0);
        this.mSplitTrack = a.getBoolean(8, false);
        ColorStateList thumbTintList = a.getColorStateList(9);
        if (thumbTintList != null) {
            this.mThumbTintList = thumbTintList;
            this.mHasThumbTint = true;
        }
        Mode thumbTintMode = Drawable.parseTintMode(a.getInt(10, -1), null);
        if (this.mThumbTintMode != thumbTintMode) {
            this.mThumbTintMode = thumbTintMode;
            this.mHasThumbTintMode = true;
        }
        if (this.mHasThumbTint || this.mHasThumbTintMode) {
            applyThumbTint();
        }
        ColorStateList trackTintList = a.getColorStateList(12);
        if (trackTintList != null) {
            this.mTrackTintList = trackTintList;
            this.mHasTrackTint = true;
        }
        Mode trackTintMode = Drawable.parseTintMode(a.getInt(13, -1), null);
        if (this.mTrackTintMode != trackTintMode) {
            this.mTrackTintMode = trackTintMode;
            this.mHasTrackTintMode = true;
        }
        if (this.mHasTrackTint || this.mHasTrackTintMode) {
            applyTrackTint();
        }
        int appearance = a.getResourceId(3, 0);
        if (appearance != 0) {
            setSwitchTextAppearance(context, appearance);
        }
        a.recycle();
        ViewConfiguration config = ViewConfiguration.get(context);
        this.mTouchSlop = config.getScaledTouchSlop();
        this.mMinFlingVelocity = config.getScaledMinimumFlingVelocity();
        refreshDrawableState();
        setChecked(isChecked());
    }

    public void setSwitchTextAppearance(Context context, int resid) {
        TypedArray appearance = context.obtainStyledAttributes(resid, R.styleable.TextAppearance);
        ColorStateList colors = appearance.getColorStateList(3);
        if (colors != null) {
            this.mTextColors = colors;
        } else {
            this.mTextColors = getTextColors();
        }
        int ts = appearance.getDimensionPixelSize(0, 0);
        if (!(ts == 0 || ((float) ts) == this.mTextPaint.getTextSize())) {
            this.mTextPaint.setTextSize((float) ts);
            requestLayout();
        }
        setSwitchTypefaceByIndex(appearance.getInt(1, -1), appearance.getInt(2, -1));
        if (appearance.getBoolean(11, false)) {
            this.mSwitchTransformationMethod = new AllCapsTransformationMethod(getContext());
            this.mSwitchTransformationMethod.setLengthChangesAllowed(true);
        } else {
            this.mSwitchTransformationMethod = null;
        }
        appearance.recycle();
    }

    private void setSwitchTypefaceByIndex(int typefaceIndex, int styleIndex) {
        Typeface tf = null;
        switch (typefaceIndex) {
            case 1:
                tf = Typeface.SANS_SERIF;
                break;
            case 2:
                tf = Typeface.SERIF;
                break;
            case 3:
                tf = Typeface.MONOSPACE;
                break;
        }
        setSwitchTypeface(tf, styleIndex);
    }

    public void setSwitchTypeface(Typeface tf, int style) {
        boolean z = false;
        if (style > 0) {
            int typefaceStyle;
            float f;
            if (tf == null) {
                tf = Typeface.defaultFromStyle(style);
            } else {
                tf = Typeface.create(tf, style);
            }
            setSwitchTypeface(tf);
            if (tf != null) {
                typefaceStyle = tf.getStyle();
            } else {
                typefaceStyle = 0;
            }
            int need = style & (typefaceStyle ^ -1);
            TextPaint textPaint = this.mTextPaint;
            if ((need & 1) != 0) {
                z = true;
            }
            textPaint.setFakeBoldText(z);
            textPaint = this.mTextPaint;
            if ((need & 2) != 0) {
                f = -0.25f;
            } else {
                f = 0.0f;
            }
            textPaint.setTextSkewX(f);
            return;
        }
        this.mTextPaint.setFakeBoldText(false);
        this.mTextPaint.setTextSkewX(0.0f);
        setSwitchTypeface(tf);
    }

    public void setSwitchTypeface(Typeface tf) {
        if (this.mTextPaint.getTypeface() != tf) {
            this.mTextPaint.setTypeface(tf);
            requestLayout();
            invalidate();
        }
    }

    public void setSwitchPadding(int pixels) {
        this.mSwitchPadding = pixels;
        requestLayout();
    }

    public int getSwitchPadding() {
        return this.mSwitchPadding;
    }

    public void setSwitchMinWidth(int pixels) {
        this.mSwitchMinWidth = pixels;
        requestLayout();
    }

    public int getSwitchMinWidth() {
        return this.mSwitchMinWidth;
    }

    public void setThumbTextPadding(int pixels) {
        this.mThumbTextPadding = pixels;
        requestLayout();
    }

    public int getThumbTextPadding() {
        return this.mThumbTextPadding;
    }

    public void setTrackDrawable(Drawable track) {
        if (this.mTrackDrawable != null) {
            this.mTrackDrawable.setCallback(null);
        }
        this.mTrackDrawable = track;
        if (track != null) {
            track.setCallback(this);
        }
        requestLayout();
    }

    public void setTrackResource(int resId) {
        setTrackDrawable(getContext().getDrawable(resId));
    }

    public Drawable getTrackDrawable() {
        return this.mTrackDrawable;
    }

    public void setTrackTintList(ColorStateList tint) {
        this.mTrackTintList = tint;
        this.mHasTrackTint = true;
        applyTrackTint();
    }

    public ColorStateList getTrackTintList() {
        return this.mTrackTintList;
    }

    public void setTrackTintMode(Mode tintMode) {
        this.mTrackTintMode = tintMode;
        this.mHasTrackTintMode = true;
        applyTrackTint();
    }

    public Mode getTrackTintMode() {
        return this.mTrackTintMode;
    }

    private void applyTrackTint() {
        if (this.mTrackDrawable == null) {
            return;
        }
        if (this.mHasTrackTint || this.mHasTrackTintMode) {
            this.mTrackDrawable = this.mTrackDrawable.mutate();
            if (this.mHasTrackTint) {
                this.mTrackDrawable.setTintList(this.mTrackTintList);
            }
            if (this.mHasTrackTintMode) {
                this.mTrackDrawable.setTintMode(this.mTrackTintMode);
            }
            if (this.mTrackDrawable.isStateful()) {
                this.mTrackDrawable.setState(getDrawableState());
            }
        }
    }

    public void setThumbDrawable(Drawable thumb) {
        if (this.mThumbDrawable != null) {
            this.mThumbDrawable.setCallback(null);
        }
        this.mThumbDrawable = thumb;
        if (thumb != null) {
            thumb.setCallback(this);
        }
        requestLayout();
    }

    public void setThumbResource(int resId) {
        setThumbDrawable(getContext().getDrawable(resId));
    }

    public Drawable getThumbDrawable() {
        return this.mThumbDrawable;
    }

    public void setThumbTintList(ColorStateList tint) {
        this.mThumbTintList = tint;
        this.mHasThumbTint = true;
        applyThumbTint();
    }

    public ColorStateList getThumbTintList() {
        return this.mThumbTintList;
    }

    public void setThumbTintMode(Mode tintMode) {
        this.mThumbTintMode = tintMode;
        this.mHasThumbTintMode = true;
        applyThumbTint();
    }

    public Mode getThumbTintMode() {
        return this.mThumbTintMode;
    }

    private void applyThumbTint() {
        if (this.mThumbDrawable == null) {
            return;
        }
        if (this.mHasThumbTint || this.mHasThumbTintMode) {
            this.mThumbDrawable = this.mThumbDrawable.mutate();
            if (this.mHasThumbTint) {
                this.mThumbDrawable.setTintList(this.mThumbTintList);
            }
            if (this.mHasThumbTintMode) {
                this.mThumbDrawable.setTintMode(this.mThumbTintMode);
            }
            if (this.mThumbDrawable.isStateful()) {
                this.mThumbDrawable.setState(getDrawableState());
            }
        }
    }

    public void setSplitTrack(boolean splitTrack) {
        this.mSplitTrack = splitTrack;
        invalidate();
    }

    public boolean getSplitTrack() {
        return this.mSplitTrack;
    }

    public CharSequence getTextOn() {
        return this.mTextOn;
    }

    public void setTextOn(CharSequence textOn) {
        this.mTextOn = textOn;
        requestLayout();
    }

    public CharSequence getTextOff() {
        return this.mTextOff;
    }

    public void setTextOff(CharSequence textOff) {
        this.mTextOff = textOff;
        requestLayout();
    }

    public void setShowText(boolean showText) {
        if (this.mShowText != showText) {
            this.mShowText = showText;
            requestLayout();
        }
    }

    public boolean getShowText() {
        return this.mShowText;
    }

    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int thumbWidth;
        int thumbHeight;
        int maxTextWidth;
        int trackHeight;
        if (this.mShowText) {
            if (this.mOnLayout == null) {
                this.mOnLayout = makeLayout(this.mTextOn);
            }
            if (this.mOffLayout == null) {
                this.mOffLayout = makeLayout(this.mTextOff);
            }
        }
        Rect padding = this.mTempRect;
        if (this.mThumbDrawable != null) {
            this.mThumbDrawable.getPadding(padding);
            if (this.mIsDeviceDefaultLight) {
                thumbWidth = this.mThumbDrawable.getIntrinsicWidth();
            } else {
                thumbWidth = (this.mThumbDrawable.getIntrinsicWidth() - padding.left) - padding.right;
            }
            thumbHeight = this.mThumbDrawable.getIntrinsicHeight();
        } else {
            thumbWidth = 0;
            thumbHeight = 0;
        }
        if (this.mShowText) {
            maxTextWidth = Math.max(this.mOnLayout.getWidth(), this.mOffLayout.getWidth()) + (this.mThumbTextPadding * 2);
        } else {
            maxTextWidth = 0;
        }
        this.mThumbWidth = Math.max(maxTextWidth, thumbWidth);
        if (this.mTrackDrawable != null) {
            this.mTrackDrawable.getPadding(padding);
            trackHeight = this.mTrackDrawable.getIntrinsicHeight();
        } else {
            padding.setEmpty();
            trackHeight = 0;
        }
        int paddingLeft = padding.left;
        int paddingRight = padding.right;
        if (this.mThumbDrawable != null) {
            Insets inset = this.mThumbDrawable.getOpticalInsets();
            paddingLeft = Math.max(paddingLeft, inset.left);
            paddingRight = Math.max(paddingRight, inset.right);
        }
        int mTwSwitchWidth = getResources().getDimensionPixelSize(R.dimen.tw_switch_width);
        int i = this.mSwitchMinWidth;
        if (!this.mIsDeviceDefaultLight) {
            mTwSwitchWidth = ((this.mThumbWidth * 2) + paddingLeft) + paddingRight;
        }
        int switchWidth = Math.max(i, mTwSwitchWidth);
        int switchHeight = Math.max(trackHeight, thumbHeight);
        this.mSwitchWidth = switchWidth;
        this.mSwitchHeight = switchHeight;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getMeasuredHeight() < switchHeight) {
            setMeasuredDimension(getMeasuredWidthAndState(), switchHeight);
        }
    }

    public void onPopulateAccessibilityEventInternal(AccessibilityEvent event) {
        super.onPopulateAccessibilityEventInternal(event);
        CharSequence text = isChecked() ? this.mTextOn : this.mTextOff;
        if (text != null) {
            event.getText().add(text);
        }
    }

    private Layout makeLayout(CharSequence text) {
        CharSequence transformed;
        if (this.mSwitchTransformationMethod != null) {
            transformed = this.mSwitchTransformationMethod.getTransformation(text, this);
        } else {
            transformed = text;
        }
        return new StaticLayout(transformed, this.mTextPaint, (int) Math.ceil((double) Layout.getDesiredWidth(transformed, this.mTextPaint)), Alignment.ALIGN_NORMAL, 1.0f, 0.0f, true);
    }

    private boolean hitThumb(float x, float y) {
        if (this.mThumbDrawable == null) {
            return false;
        }
        int thumbOffset = getThumbOffset();
        this.mThumbDrawable.getPadding(this.mTempRect);
        int thumbTop = this.mSwitchTop - this.mTouchSlop;
        int thumbLeft = (this.mSwitchLeft + thumbOffset) - this.mTouchSlop;
        int thumbRight = (((this.mThumbWidth + thumbLeft) + this.mTempRect.left) + this.mTempRect.right) + this.mTouchSlop;
        int thumbBottom = this.mSwitchBottom + this.mTouchSlop;
        if (x <= ((float) thumbLeft) || x >= ((float) thumbRight) || y <= ((float) thumbTop) || y >= ((float) thumbBottom)) {
            return false;
        }
        return true;
    }

    public boolean onTouchEvent(MotionEvent ev) {
        this.mVelocityTracker.addMovement(ev);
        float x;
        float y;
        switch (ev.getActionMasked()) {
            case 0:
                x = ev.getX();
                y = ev.getY();
                if (isEnabled() && hitThumb(x, y)) {
                    this.mTouchMode = 1;
                    this.mTouchX = x;
                    this.mTouchY = y;
                    break;
                }
            case 1:
            case 3:
                if (this.mTouchMode != 2) {
                    this.mTouchMode = 0;
                    this.mVelocityTracker.clear();
                    break;
                }
                stopDrag(ev);
                super.onTouchEvent(ev);
                return true;
            case 2:
                switch (this.mTouchMode) {
                    case 0:
                        break;
                    case 1:
                        x = ev.getX();
                        y = ev.getY();
                        if (Math.abs(x - this.mTouchX) > ((float) this.mTouchSlop) || Math.abs(y - this.mTouchY) > ((float) this.mTouchSlop)) {
                            this.mTouchMode = 2;
                            getParent().requestDisallowInterceptTouchEvent(true);
                            this.mTouchX = x;
                            this.mTouchY = y;
                            return true;
                        }
                    case 2:
                        float dPos;
                        x = ev.getX();
                        int thumbScrollRange = getThumbScrollRange();
                        float thumbScrollOffset = x - this.mTouchX;
                        if (thumbScrollRange != 0) {
                            dPos = thumbScrollOffset / ((float) thumbScrollRange);
                        } else {
                            dPos = thumbScrollOffset > 0.0f ? 1.0f : LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
                        }
                        if (isLayoutRtl()) {
                            dPos = -dPos;
                        }
                        float newPos = MathUtils.constrain(this.mThumbPosition + dPos, 0.0f, 1.0f);
                        if (newPos != this.mThumbPosition) {
                            this.mTouchX = x;
                            setThumbPosition(newPos);
                        }
                        return true;
                    default:
                        break;
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    private void cancelSuperTouch(MotionEvent ev) {
        MotionEvent cancel = MotionEvent.obtain(ev);
        cancel.setAction(3);
        super.onTouchEvent(cancel);
        cancel.recycle();
    }

    private void stopDrag(MotionEvent ev) {
        boolean commitChange;
        boolean newState;
        this.mTouchMode = 0;
        if (ev.getAction() == 1 && isEnabled()) {
            commitChange = true;
        } else {
            commitChange = false;
        }
        boolean oldState = isChecked();
        if (commitChange) {
            this.mVelocityTracker.computeCurrentVelocity(1000);
            float xvel = this.mVelocityTracker.getXVelocity();
            if (this.mIsDeviceDefaultLight) {
                if (Math.abs(xvel) > 2000.0f) {
                    newState = isLayoutRtl() ? xvel < 0.0f : xvel > 0.0f;
                } else if (Math.abs(xvel) > 500.0f || !(this.mThumbPosition == 0.0f || this.mThumbPosition == 1.0f)) {
                    newState = isLayoutRtl() ? xvel < 0.0f : xvel > 0.0f;
                } else {
                    newState = getTargetCheckedState();
                }
            } else if (Math.abs(xvel) > ((float) this.mMinFlingVelocity)) {
                newState = isLayoutRtl() ? xvel < 0.0f : xvel > 0.0f;
            } else {
                newState = getTargetCheckedState();
            }
        } else {
            newState = oldState;
        }
        if (newState != oldState) {
            playSoundEffect(0);
            setChecked(newState);
        } else {
            setChecked(oldState);
        }
        cancelSuperTouch(ev);
    }

    private void animateThumbToCheckedState(boolean newCheckedState) {
        float targetPosition = newCheckedState ? 1.0f : 0.0f;
        this.mPositionAnimator = ObjectAnimator.ofFloat(this, THUMB_POS, new float[]{targetPosition});
        this.mPositionAnimator.setDuration(250);
        this.mPositionAnimator.addListener(this.mAnimatorListener);
        this.mPositionAnimator.setAutoCancel(true);
        this.mPositionAnimator.start();
    }

    private void cancelPositionAnimator() {
        if (this.mPositionAnimator != null) {
            this.mPositionAnimator.cancel();
        }
    }

    private boolean getTargetCheckedState() {
        return this.mThumbPosition > 0.5f;
    }

    private void setThumbPosition(float position) {
        this.mThumbPosition = position;
        invalidate();
    }

    public void toggle() {
        setChecked(!isChecked());
    }

    public void setChecked(boolean checked) {
        super.setChecked(checked);
        checked = isChecked();
        if (isAttachedToWindow() && isLaidOut()) {
            animateThumbToCheckedState(checked);
            return;
        }
        Log.i(TAG, "Immediately mode attached=" + isAttachedToWindow() + " laidOut=" + isLaidOut());
        cancelPositionAnimator();
        setThumbPosition(checked ? 1.0f : 0.0f);
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int switchLeft;
        int switchRight;
        int switchTop;
        int switchBottom;
        super.onLayout(changed, left, top, right, bottom);
        int opticalInsetLeft = 0;
        int opticalInsetRight = 0;
        if (this.mThumbDrawable != null) {
            Rect trackPadding = this.mTempRect;
            if (this.mTrackDrawable != null) {
                this.mTrackDrawable.getPadding(trackPadding);
            } else {
                trackPadding.setEmpty();
            }
            Insets insets = this.mThumbDrawable.getOpticalInsets();
            opticalInsetLeft = Math.max(0, insets.left - trackPadding.left);
            opticalInsetRight = Math.max(0, insets.right - trackPadding.right);
        }
        if (isLayoutRtl()) {
            switchLeft = getPaddingLeft() + opticalInsetLeft;
            switchRight = ((this.mSwitchWidth + switchLeft) - opticalInsetLeft) - opticalInsetRight;
        } else {
            switchRight = (getWidth() - getPaddingRight()) - opticalInsetRight;
            switchLeft = ((switchRight - this.mSwitchWidth) + opticalInsetLeft) + opticalInsetRight;
        }
        switch (getGravity() & 112) {
            case 16:
                switchTop = (((getPaddingTop() + getHeight()) - getPaddingBottom()) / 2) - (this.mSwitchHeight / 2);
                switchBottom = switchTop + this.mSwitchHeight;
                break;
            case 80:
                switchBottom = getHeight() - getPaddingBottom();
                switchTop = switchBottom - this.mSwitchHeight;
                break;
            default:
                switchTop = getPaddingTop();
                switchBottom = switchTop + this.mSwitchHeight;
                break;
        }
        this.mSwitchLeft = switchLeft;
        this.mSwitchTop = switchTop;
        this.mSwitchBottom = switchBottom;
        this.mSwitchRight = switchRight;
    }

    public void draw(Canvas c) {
        Insets thumbInsets;
        Rect padding = this.mTempRect;
        int switchLeft = this.mSwitchLeft;
        int switchTop = this.mSwitchTop;
        int switchRight = this.mSwitchRight;
        int switchBottom = this.mSwitchBottom;
        int thumbInitialLeft = switchLeft + getThumbOffset();
        if (this.mThumbDrawable != null) {
            thumbInsets = this.mThumbDrawable.getOpticalInsets();
        } else {
            thumbInsets = Insets.NONE;
        }
        if (this.mTrackDrawable != null) {
            this.mTrackDrawable.getPadding(padding);
            if (!this.mIsDeviceDefaultLight) {
                thumbInitialLeft += padding.left;
            }
            int trackLeft = switchLeft;
            int trackTop = switchTop;
            int trackRight = switchRight;
            int trackBottom = switchBottom;
            if (thumbInsets != Insets.NONE) {
                if (thumbInsets.left > padding.left) {
                    trackLeft += thumbInsets.left - padding.left;
                }
                if (thumbInsets.top > padding.top) {
                    trackTop += thumbInsets.top - padding.top;
                }
                if (thumbInsets.right > padding.right) {
                    trackRight -= thumbInsets.right - padding.right;
                }
                if (thumbInsets.bottom > padding.bottom) {
                    trackBottom -= thumbInsets.bottom - padding.bottom;
                }
            }
            if (this.mIsDeviceDefaultLight) {
                this.mTrackDrawable.setBounds(switchLeft, trackTop, switchRight, trackBottom);
            } else {
                this.mTrackDrawable.setBounds(trackLeft, trackTop, trackRight, trackBottom);
            }
        }
        if (this.mThumbDrawable != null) {
            this.mThumbDrawable.getPadding(padding);
            int thumbLeft = thumbInitialLeft - padding.left;
            int thumbRight = (this.mThumbWidth + thumbInitialLeft) + padding.right;
            if (this.mIsDeviceDefaultLight) {
                thumbLeft = switchLeft + getThumbOffset();
                thumbRight = thumbLeft + this.mThumbWidth;
            }
            this.mThumbDrawable.setBounds(thumbLeft, switchTop, thumbRight, switchBottom);
            Drawable background = getBackground();
            if (background != null) {
                background.setHotspotBounds(thumbLeft, switchTop, thumbRight, switchBottom);
            }
        }
        super.draw(c);
    }

    protected void onDraw(Canvas canvas) {
        int saveCount;
        Layout switchText;
        super.onDraw(canvas);
        Rect padding = this.mTempRect;
        Drawable trackDrawable = this.mTrackDrawable;
        if (trackDrawable != null) {
            trackDrawable.getPadding(padding);
        } else {
            padding.setEmpty();
        }
        int switchTop = this.mSwitchTop;
        int switchInnerTop = switchTop + padding.top;
        int switchInnerBottom = this.mSwitchBottom - padding.bottom;
        Drawable thumbDrawable = this.mThumbDrawable;
        if (trackDrawable != null) {
            if (!this.mSplitTrack || thumbDrawable == null) {
                trackDrawable.draw(canvas);
            } else {
                Insets insets = thumbDrawable.getOpticalInsets();
                thumbDrawable.copyBounds(padding);
                padding.left += insets.left;
                padding.right -= insets.right;
                saveCount = canvas.save();
                canvas.clipRect(padding, Op.DIFFERENCE);
                trackDrawable.draw(canvas);
                canvas.restoreToCount(saveCount);
            }
        }
        saveCount = canvas.save();
        if (thumbDrawable != null) {
            thumbDrawable.draw(canvas);
        }
        if (getTargetCheckedState()) {
            switchText = this.mOnLayout;
        } else {
            switchText = this.mOffLayout;
        }
        if (switchText != null) {
            int cX;
            int[] drawableState = getDrawableState();
            if (this.mTextColors != null) {
                this.mTextPaint.setColor(this.mTextColors.getColorForState(drawableState, 0));
            }
            this.mTextPaint.drawableState = drawableState;
            if (thumbDrawable != null) {
                Rect bounds = thumbDrawable.getBounds();
                cX = bounds.left + bounds.right;
            } else {
                cX = getWidth();
            }
            canvas.translate((float) ((cX / 2) - (switchText.getWidth() / 2)), (float) (((switchInnerTop + switchInnerBottom) / 2) - (switchText.getHeight() / 2)));
            switchText.draw(canvas);
        }
        canvas.restoreToCount(saveCount);
    }

    public int getCompoundPaddingLeft() {
        if (!isLayoutRtl()) {
            return super.getCompoundPaddingLeft();
        }
        int padding = super.getCompoundPaddingLeft() + this.mSwitchWidth;
        if (TextUtils.isEmpty(getText())) {
            return padding;
        }
        return padding + this.mSwitchPadding;
    }

    public int getCompoundPaddingRight() {
        if (isLayoutRtl()) {
            return super.getCompoundPaddingRight();
        }
        int padding = super.getCompoundPaddingRight() + this.mSwitchWidth;
        if (TextUtils.isEmpty(getText())) {
            return padding;
        }
        return padding + this.mSwitchPadding;
    }

    private int getThumbOffset() {
        float thumbPosition;
        if (isLayoutRtl()) {
            thumbPosition = 1.0f - this.mThumbPosition;
        } else {
            thumbPosition = this.mThumbPosition;
        }
        return (int) ((((float) getThumbScrollRange()) * thumbPosition) + 0.5f);
    }

    private int getThumbScrollRange() {
        if (this.mTrackDrawable == null) {
            return 0;
        }
        Insets insets;
        Rect padding = this.mTempRect;
        if (this.mIsDeviceDefaultLight) {
            this.mThumbDrawable.getPadding(padding);
        } else {
            this.mTrackDrawable.getPadding(padding);
        }
        if (this.mThumbDrawable != null) {
            insets = this.mThumbDrawable.getOpticalInsets();
        } else {
            insets = Insets.NONE;
        }
        if (this.mIsDeviceDefaultLight) {
            return ((this.mSwitchWidth - this.mThumbWidth) - padding.left) - padding.right;
        }
        return ((((this.mSwitchWidth - this.mThumbWidth) - padding.left) - padding.right) - insets.left) - insets.right;
    }

    protected int[] onCreateDrawableState(int extraSpace) {
        int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            View.mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
    }

    protected void drawableStateChanged() {
        super.drawableStateChanged();
        int[] myDrawableState = getDrawableState();
        if (this.mThumbDrawable != null) {
            this.mThumbDrawable.setState(myDrawableState);
        }
        if (this.mTrackDrawable != null) {
            this.mTrackDrawable.setState(myDrawableState);
        }
        invalidate();
    }

    public void drawableHotspotChanged(float x, float y) {
        super.drawableHotspotChanged(x, y);
        if (this.mThumbDrawable != null) {
            this.mThumbDrawable.setHotspot(x, y);
        }
        if (this.mTrackDrawable != null) {
            this.mTrackDrawable.setHotspot(x, y);
        }
    }

    protected boolean verifyDrawable(Drawable who) {
        return super.verifyDrawable(who) || who == this.mThumbDrawable || who == this.mTrackDrawable;
    }

    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        if (this.mThumbDrawable != null) {
            this.mThumbDrawable.jumpToCurrentState();
        }
        if (this.mTrackDrawable != null) {
            this.mTrackDrawable.jumpToCurrentState();
        }
        if (this.mPositionAnimator != null && this.mPositionAnimator.isRunning()) {
            this.mPositionAnimator.end();
            this.mPositionAnimator = null;
        }
    }

    public CharSequence getAccessibilityClassName() {
        return Switch.class.getName();
    }

    public void onProvideStructure(ViewStructure structure) {
        super.onProvideStructure(structure);
        CharSequence switchText = isChecked() ? this.mTextOn : this.mTextOff;
        if (!TextUtils.isEmpty(switchText)) {
            CharSequence oldText = structure.getText();
            if (TextUtils.isEmpty(oldText)) {
                structure.setText(switchText);
                return;
            }
            StringBuilder newText = new StringBuilder();
            newText.append(oldText).append(' ').append(switchText);
            structure.setText(newText);
        }
    }

    public void onInitializeAccessibilityNodeInfoInternal(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfoInternal(info);
        CharSequence switchText = isChecked() ? this.mTextOn : this.mTextOff;
        if (!TextUtils.isEmpty(switchText)) {
            CharSequence oldText = info.getText();
            if (TextUtils.isEmpty(oldText)) {
                info.setText(switchText);
                return;
            }
            StringBuilder newText = new StringBuilder();
            newText.append(oldText).append(' ').append(switchText);
            info.setText(newText);
        }
    }
}
