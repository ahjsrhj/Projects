package cn.imrhj.newlogin.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import cn.imrhj.newlogin.View.LoginViewInterface;
import cn.imrhj.newlogin.model.LoginBusinessImp;
import cn.imrhj.newlogin.model.UserInfo;
import cn.imrhj.newlogin.presenter.LoginPresenter;

/**
 * Created by rhj on 16/4/17.
 */
public class LoginService extends Service implements LoginViewInterface{
    private static final String TAG = "LoginService";
    private UserInfo mUserInfo;
    private LoginPresenter mPresenter;
    private long startTime = 0L;


    @Override
    public void onCreate() {
        super.onCreate();
        mPresenter = new LoginPresenter(this, this);
        Log.d(TAG, "onCreate: ");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (System.currentTimeMillis() - startTime > 1000) {
            mPresenter.login(intent.getStringExtra("ssid"));
            startTime = System.currentTimeMillis();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public UserInfo getUserInfo() {
        return mUserInfo;
    }

    @Override
    public void setUserInfo(UserInfo userInfo) {
        mUserInfo = userInfo;
    }

    @Override
    public void showNetWorkType(String string) {
    }

    @Override
    public void showMessage(String msg) {
        if (msg.contains("login_ok")) {
            Toast.makeText(LoginService.this, "登录成功!", Toast.LENGTH_SHORT).show();
        }
    }
}
