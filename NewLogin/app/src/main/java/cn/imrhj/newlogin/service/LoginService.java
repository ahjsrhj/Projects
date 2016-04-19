package cn.imrhj.newlogin.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import cn.imrhj.newlogin.Utils.UserInfoModify;
import cn.imrhj.newlogin.model.LocalInfoBusinessImp;
import cn.imrhj.newlogin.model.LocalUserInfoInterface;
import cn.imrhj.newlogin.model.LoginBusinessImp;
import cn.imrhj.newlogin.model.LoginModelInterface;
import cn.imrhj.newlogin.model.NetWorkBusinessImp;
import cn.imrhj.newlogin.model.NetWorkInfoInterface;
import cn.imrhj.newlogin.model.UserInfo;

/**
 * Created by rhj on 16/4/17.
 * 后台服务,进行网络检测登录等相关操作.
 */
public class LoginService extends Service  implements LoginServiceInterface{
    private static final String TAG = "LoginService";
    private UserInfo mUserInfo;
    private LocalUserInfoInterface localUserInfoBusiness;
    private LoginModelInterface loginBusiness;
    private NetWorkInfoInterface netWorkInfoBusiness;
    private LoginServieBinder mBinder = new LoginServieBinder();
    private long sendTime;


    @Override
    public void onCreate() {
        super.onCreate();
        this.localUserInfoBusiness = new LocalInfoBusinessImp(getApplicationContext());
        this.loginBusiness = new LoginBusinessImp(getApplicationContext());
        this.netWorkInfoBusiness = new NetWorkBusinessImp(getApplicationContext());


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (netWorkInfoBusiness.isCanLoginSSID()) {
            String SSID = netWorkInfoBusiness.getSSID();
            Log.d(TAG, "onStartCommand: " + SSID);
            UserInfoModify userInfoModify = new UserInfoModify(mUserInfo, SSID);

            if (!userInfoModify.isUserInfoInvalid()) {
                Log.d(TAG, "onStartCommand:  + 用户检测通过" );
                //判断时间,防止多次发送
                if (System.currentTimeMillis() - sendTime > 1500) {
                    Log.e(TAG, "onStartCommand: 发送登录请求" );
                    loginBusiness.login(userInfoModify, stringListener, errorListener);
                    sendTime = System.currentTimeMillis();
                }
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


    @Override
    public String getNetworkType() {
        return netWorkInfoBusiness.getNetWorkType();
    }

    @Override
    public UserInfo getUserInfo() {
        mUserInfo = localUserInfoBusiness.getUserInfo();
        return mUserInfo;
    }

    @Override
    public void saveUserInfo(UserInfo userInfo) {
        mUserInfo = userInfo;
        localUserInfoBusiness.saveUserInfo(userInfo);
    }



    /**
     * 当ssid为WXXY_ChinaNet/WXXY_ChinaUnicom/WXXY_CMCC时, 当用户不为空,发送对应的登录请求,当用户为空,弹出警告.
     * 其它SSID直接发送默认的请求
     */
    @Override
    public void forcedLogin() {
        if (netWorkInfoBusiness.isThreeSSID()) {
            if (mUserInfo == null || mUserInfo.isEmpty()) {
                Toast.makeText(LoginService.this,
                        "用户信息为空,无法登录运营商网络!",
                        Toast.LENGTH_SHORT).show();
            } else {
                UserInfoModify modify = new UserInfoModify(mUserInfo, netWorkInfoBusiness.getSSID());
                loginBusiness.login(modify,stringListener, errorListener);
            }
        } else {
            loginBusiness.forcedLogin(stringListener, errorListener);
        }
    }

    Response.Listener<String> stringListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            Log.d(TAG, "onResponse: " + response);
            if (response.contains("login_ok")) {
                Toast.makeText(LoginService.this, "登录成功!", Toast.LENGTH_SHORT).show();
            }
        }
    };

    Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            if (error.getMessage() == null) {
                Toast.makeText(LoginService.this, "登录失败! 请检查网络", Toast.LENGTH_SHORT).show();
            }
            Log.d(TAG, "onErrorResponse: " + error.getLocalizedMessage());

        }
    };


    public class LoginServieBinder extends Binder {
        public LoginServiceInterface getLoginService() {
            return LoginService.this;
        }
    }
}
