package cn.imrhj.calculater;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.widget.TextView;

/**
 * Created by rhj on 16/4/14.
 */
public class AutoTextView extends TextView {

    private static final String TAG = "AutoTextView";
    private static float DEFAULT_MIN_TEXT_SIZE = 10;
    private static float DEFAULT_MAX_TEXT_SIZE = 16;

    // Attributes
    private Paint testPaint;
    private float minTextSize, maxTextSize;
    private Context mContext;

    public AutoTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initialise();
    }


    private void initialise() {
        testPaint = new Paint();
        testPaint.set(this.getPaint());


        maxTextSize = this.getTextSize();

        if (maxTextSize <= DEFAULT_MIN_TEXT_SIZE) {
            maxTextSize = DEFAULT_MAX_TEXT_SIZE;
        }

        minTextSize = DEFAULT_MIN_TEXT_SIZE;
    }

    private void refitText(String text, int textWidth) {
        if (textWidth == 0) return;
        int strLength = text.length();
        if (strLength == 0) return;
        float maxWidth = px2sp(textWidth / strLength) * 1.6f;
        float strWidth = px2sp(getTextSize());
        Log.d(TAG, "refitText: maxWidth = " + maxWidth + ", strWidth = " + strWidth + ", textWidth = " + textWidth);

        while (strWidth > maxWidth) {
            this.setTextSize(strWidth);
            strWidth = px2sp(getTextSize())-1;
        }

    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int before, int after) {
        super.onTextChanged(text, start, before, after);
        refitText(text.toString(), this.getWidth());

    }
    private int px2sp(float pxValue) {
        final float fontScale = mContext.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }




}
