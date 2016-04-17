package cn.imrhj.calculater;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity implements NumBtn.OnNumBtnClick, NumBtn.OnCtlBtnClick {


    private static final String TAG = "MainActivity";
    private TextView mHistory;
    private TextView mResult;

    private boolean useOldNum = true;

    private char operate = ' ';



    public static final int[] BTN_NUM_ID = {
            R.id.btn_0, R.id.btn_1, R.id.btn_2,
            R.id.btn_3, R.id.btn_4, R.id.btn_5,
            R.id.btn_6, R.id.btn_7, R.id.btn_8,
            R.id.btn_9 };
    public static final int[] BTN_CTL_ID = {
            R.id.btn_clean, R.id.btn_del, R.id.btn_divide,
            R.id.btn_mult, R.id.btn_sub, R.id.btn_add,
            R.id.btn_equal, R.id.btn_bracket, R.id.btn_dot
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        for (int id : BTN_NUM_ID) {
            ((NumBtn)findViewById(id)).setOnNumBtnClick(this);
        }
        for (int id : BTN_CTL_ID) {
            ((NumBtn)findViewById(id)).setOnCtlBtnClick(this);
        }

        mHistory = (TextView) findViewById(R.id.text_history);
        mResult = (TextView) findViewById(R.id.text_result);
    }

    @Override
    public void onNumBtnClick(String s) {
        if (!useOldNum) {
            useOldNum = true;
            mResult.setText("");
        }
        addResultText(s);
    }

    private void addResultText(String s) {
        String string = mResult.getText().toString() + s;
        mResult.setText(string);
    }

    private void addResultText(char c) {
        String s = "" + c;
        addResultText(s);
    }

    @Override
    public void onCtlBtnClick(View v) {
        if (!useOldNum) {
            useOldNum = true;
        }
        switch (v.getId()) {
            case R.id.btn_clean:
                mResult.setText("");
                break;
            case R.id.btn_del:
                String str = mResult.getText().toString();
                if (!str.isEmpty()) {
                    mResult.setText(str.substring(0, str.length() - 1));
                }
                break;
            case R.id.btn_divide:
                operate = '÷';
                addResultText(operate);
                break;
            case R.id.btn_mult:
                operate = '×';
                addResultText(operate);
                break;
            case R.id.btn_sub:
                operate = '−';
                addResultText(operate);
                break;
            case R.id.btn_add:
                operate = '+';
                addResultText(operate);
                break;
            case R.id.btn_equal:
                calculate();
                useOldNum = false;
                break;
            case R.id.btn_dot:
                addResultText('.');
                break;
            case R.id.btn_bracket:
                break;
        }
    }

    private void calculate() {
        boolean hasDot = false;
        String s = mResult.getText().toString();
        int pos = s.indexOf(operate);
        if (pos == -1) {
            return;
        }
        String a = s.substring(0,pos);
        String b = s.substring(pos+1, s.length());
        Log.d(TAG, "calculate: a = " + a + ", b = " + b);
        // 判断是否有小数点
        if (a.indexOf('.') != -1 || b.indexOf('.') != -1) {
            hasDot = true;
        }

        mHistory.setText(mResult.getText());
        switch (operate) {
            case '+':
                if (hasDot) {
                    mResult.setText(String.valueOf(Float.valueOf(a) + Float.valueOf(b)));
                } else {
                    mResult.setText(String.valueOf(Integer.valueOf(a) + Integer.valueOf(b)));
                }
                break;
            case '−':
                if (hasDot) {
                    mResult.setText(String.valueOf(Float.valueOf(a) - Float.valueOf(b)));
                } else {
                    mResult.setText(String.valueOf(Integer.valueOf(a) - Integer.valueOf(b)));
                }
                break;
            case '×':
                if (hasDot) {
                    mResult.setText(String.valueOf(Float.valueOf(a) * Float.valueOf(b)));
                } else {
                    mResult.setText(String.valueOf(Integer.valueOf(a) * Integer.valueOf(b)));
                }
                break;
            case '÷':
                mResult.setText(String.valueOf(Float.valueOf(a) / Float.valueOf(b)));
                break;
        }
        operate = ' ';
    }
}
