package com.litao.android.lib.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;

import com.litao.android.lib.R;

/**
 * Created by 李涛 on 16/4/25.
 */
public class MDCheckBox extends View implements Checkable {

    private final static float BOUNCE_VALUE = 0.2f;

    private Drawable checkDrawable;

    private Paint bitmapPaint;
    private Paint bitmapEraser;
    private Paint checkEraser;
    private Paint borderPaint;
    private Paint uncheckPaint;

    private Bitmap drawBitmap;
    private Bitmap checkBitmap;
    private Canvas bitmapCanvas;
    private Canvas checkCanvas;

    private float progress;
    private ObjectAnimator checkAnim;

    private boolean attachedToWindow;
    private boolean isChecked;

    private int size = 26;
    private int bitmapColor = 0xFF3F51B5;
    private int borderColor = 0xFFFFFFFF;
    private int uncheckColor = 0x22000000;

    public MDCheckBox(Context context) {
        this(context, null);
    }

    public MDCheckBox(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MDCheckBox(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        bitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bitmapEraser = new Paint(Paint.ANTI_ALIAS_FLAG);
        uncheckPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bitmapEraser.setColor(0);
        bitmapEraser.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        checkEraser = new Paint(Paint.ANTI_ALIAS_FLAG);
        checkEraser.setColor(0);
        checkEraser.setStyle(Paint.Style.STROKE);
        checkEraser.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(dp(2));
        checkDrawable = context.getResources().getDrawable(R.mipmap.check);
        setVisibility(VISIBLE);
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (visibility == VISIBLE && drawBitmap == null) {
            drawBitmap = Bitmap.createBitmap(dp(size), dp(size), Bitmap.Config.ARGB_8888);
            bitmapCanvas = new Canvas(drawBitmap);
            checkBitmap = Bitmap.createBitmap(dp(size), dp(size), Bitmap.Config.ARGB_8888);
            checkCanvas = new Canvas(checkBitmap);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int newSpec = MeasureSpec.makeMeasureSpec(dp(size), MeasureSpec.getMode(Math.min(widthMeasureSpec, heightMeasureSpec)));
        super.onMeasure(newSpec, newSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getVisibility() != VISIBLE) {
            return;
        }
        checkEraser.setStrokeWidth(dp(size));

        drawBitmap.eraseColor(0);
        float rad = getMeasuredWidth() / 2;

        float bitmapProgress = progress >= 0.5f ? 1.0f : progress / 0.5f;
        float checkProgress = progress < 0.5f ? 0.0f : (progress - 0.5f) / 0.5f;

        float p = isChecked ? progress : (1.0f - progress);

        if (p < BOUNCE_VALUE) {
            rad -= dp(2) * p;
        } else if (p < BOUNCE_VALUE * 2) {
            rad -= dp(2) - dp(2) * p;
        }

        borderPaint.setColor(borderColor);
        uncheckPaint.setColor(uncheckColor);
        canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, rad - dp(1), borderPaint);
        canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, rad - dp(2), uncheckPaint);

        bitmapPaint.setColor(bitmapColor);

        bitmapCanvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, rad, bitmapPaint);
        bitmapCanvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, rad * (1 - bitmapProgress), bitmapEraser);
        canvas.drawBitmap(drawBitmap, 0, 0, null);

        checkBitmap.eraseColor(0);
        int w = checkDrawable.getIntrinsicWidth();
        int h = checkDrawable.getIntrinsicHeight();
        int x = (getMeasuredWidth() - w) / 2;
        int y = (getMeasuredHeight() - h) / 2;

        checkDrawable.setBounds(x, y, x + w, y + h);
        checkDrawable.draw(checkCanvas);
        checkCanvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, rad * (1 - checkProgress), checkEraser);

        canvas.drawBitmap(checkBitmap, 0, 0, null);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        attachedToWindow = true;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        attachedToWindow = false;
    }


    public void setProgress(float value) {
        if (progress == value) {
            return;
        }
        progress = value;
        invalidate();
    }

    public void setCheckBoxColor(int bitmapColor) {
        this.bitmapColor = bitmapColor;
    }

    public void setCheckBoxSize(int size) {
        this.size = size;
    }

    public float getProgress() {
        return progress;
    }

    public void setCheckedColor(int value) {
        bitmapColor = value;
    }

    public void setBorderColor(int value) {
        borderColor = value;
        borderPaint.setColor(borderColor);
    }

    private void cancelAnim() {
        if (checkAnim != null) {
            checkAnim.cancel();
        }
    }

    private void addAnim(boolean isChecked) {
        checkAnim = ObjectAnimator.ofFloat(this, "progress", isChecked ? 1.0f : 0.0f);
        checkAnim.setDuration(300);
        checkAnim.start();
    }

    public void setChecked(boolean checked, boolean animated) {
        if (checked == isChecked) {
            return;
        }
        isChecked = checked;

        if (attachedToWindow && animated) {
            addAnim(checked);
        } else {
            cancelAnim();
            setProgress(checked ? 1.0f : 0.0f);
        }
    }

    public void toggle(boolean animated) {
        setChecked(!isChecked, animated);
    }

    @Override
    public void toggle() {
        setChecked(!isChecked);
    }

    @Override
    public void setChecked(boolean b) {
        setChecked(b, true);
    }

    @Override
    public boolean isChecked() {
        return isChecked;
    }

    public int dp(float value) {
        if (value == 0) {
            return 0;
        }
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) Math.ceil(density * value);
    }
}
