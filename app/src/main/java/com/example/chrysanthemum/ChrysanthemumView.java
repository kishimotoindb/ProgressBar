package com.example.chrysanthemum;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by BigFaceBear on 2017.12.14
 */

public class ChrysanthemumView extends View {
    private static final int MSG_ROTATION = 101;

    private Paint mChryPaint;

    private float mStartAngle = 0;
    private float mEndAngle = 10;
    private float mDAngle = 10;

    private RectF mRectF;

    private int mChryColor = 0xffffffff;
    private int mChryWidth = 5; //px
    private int mChryRotationDuration = 100; //ms

    private Handler mRotationHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            recomputeAngle();
            invalidate();
        }
    };

    private void recomputeAngle() {
        mEndAngle += mDAngle;
        mStartAngle += mDAngle;

        if (mStartAngle > 360) {
            mStartAngle %= 360;
            mEndAngle %= 360;
        }
    }


    public ChrysanthemumView(Context context) {
        super(context);
        initView(context);
    }

    public ChrysanthemumView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public ChrysanthemumView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    private void initView(Context context) {
        initView(context, null);

    }

    private void initView(Context context, AttributeSet attrs) {
        initView(context, attrs, -1);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        mChryPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mChryPaint.setStyle(Paint.Style.STROKE);

        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ChrysanthemumView);
            mChryWidth = (int) ta.getDimension(R.styleable.ChrysanthemumView_chry_width, mChryWidth);
            mChryColor = ta.getColor(R.styleable.ChrysanthemumView_chry_color, mChryColor);
            mChryRotationDuration = ta.getInt(R.styleable.ChrysanthemumView_chry_rotation_duration, mChryRotationDuration);

            mChryPaint.setStrokeWidth(mChryWidth);
            mChryPaint.setColor(mChryColor);

            ta.recycle();
        } else {
            mChryPaint.setStrokeWidth(mChryWidth);
            mChryPaint.setColor(mChryColor);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if (mRectF == null) {
            mRectF = new RectF();
        }

        mRectF.left = 0;
        mRectF.top = 0;
        mRectF.right = getWidth();
        mRectF.bottom = getHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawArc(mRectF, mStartAngle, mEndAngle, false, mChryPaint);
        nextRotation();
    }

    private void nextRotation() {
        mRotationHandler.sendEmptyMessageDelayed(MSG_ROTATION, mChryRotationDuration);
    }

    private void cancelRotation() {
        mRotationHandler.removeMessages(MSG_ROTATION);
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);

        if (visibility == VISIBLE) {
            nextRotation();
        } else {
            cancelRotation();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        cancelRotation();
    }
}
