package cn.imrhj.calculater;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.TextView;

/**
 * Created by rhj on 16/4/11.
 * 自定义控件,用于计算器的按钮
 */
public class NumBtn extends TextView implements View.OnClickListener {
    public static final String namespace = "http://schemas.android.com/apk/res/android";
    private static final String TAG = "NumBtn";
    private OnNumBtnClick onNumBtnClick;
    private OnCtlBtnClick onCtlBtnClick;

    private String mText;
    private float mTextSize = 30.0f;
    private int mTextColor = Color.WHITE;
    private int mBackgroundColor = 0x2f353b;

    private Paint mPaint = new Paint();

    public NumBtn(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
    }

    private void initAttrs(AttributeSet attrs) {
        mText = attrs.getAttributeValue(namespace, "text");
        mBackgroundColor = attrs.getAttributeResourceValue(namespace, "background", 0);
        mPaint.setTextSize(mTextSize);
        mPaint.setColor(mTextColor);
        mPaint.setAntiAlias(true);

        this.setOnClickListener(this);
        this.setBackgroundColor(getResources().getColor(mBackgroundColor == 0 ? R.color.colorBackground : R.color.colorAccent));
        this.setTextSize(50);
        this.setTextColor(Color.WHITE);
        this.setGravity(Gravity.CENTER);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public void onClick(final View v) {
        ViewCompat.animate(v)
                .setDuration(150)
                .scaleX(0.9f)
                .scaleY(0.9f)
                .setInterpolator(new CycleInterPolator())
                .setListener(new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationStart(View view) {
                        v.setClickable(false);
                    }

                    @Override
                    public void onAnimationEnd(View view) {
                        v.setClickable(true);
                        if ("0123456789".contains(mText)) {
                            if (onNumBtnClick != null) {
                                onNumBtnClick.onNumBtnClick(mText);
                            }
                        } else{
                            if (onCtlBtnClick != null) {
                                onCtlBtnClick.onCtlBtnClick(view);
                            }
                        }

                    }

                    @Override
                    public void onAnimationCancel(View view) {
                        v.setClickable(true);
                    }
                })
                .withLayer()
                .start();

    }

    public void setOnNumBtnClick(OnNumBtnClick click) {
        onNumBtnClick = click;
    }

    public void setOnCtlBtnClick(OnCtlBtnClick click) {
        onCtlBtnClick = click;

    }


    private class CycleInterPolator implements Interpolator {
        private final  float mCycles = 0.5f;

        @Override
        public float getInterpolation(float input) {
            return (float)Math.sin(2.0f * mCycles * Math.PI * input);
        }
    }

    interface OnNumBtnClick {
        void onNumBtnClick(String s);
    }

    interface OnCtlBtnClick {
        void onCtlBtnClick(View v);
    }
}
