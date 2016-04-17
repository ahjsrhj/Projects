package cn.imrhj.newlogin.presenter;

import android.content.Context;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import cn.imrhj.newlogin.model.LocalInfoBusinessImp;
import cn.imrhj.newlogin.model.LocalUserInfoInterface;
import cn.imrhj.newlogin.model.LoginBusinessImp;
import cn.imrhj.newlogin.model.LoginModelInterface;
import cn.imrhj.newlogin.View.LoginViewInterface;
import cn.imrhj.newlogin.View.MainActivity;
import cn.imrhj.newlogin.model.NetWorkBusinessImp;
import cn.imrhj.newlogin.model.NetWorkInfoInterface;
import cn.imrhj.newlogin.model.UserInfo;

/**
 * Created by rhj on 16/4/17.
 */
public class LoginPresenter {
    private static final String TAG = "LoginPresenter";
    private final LoginModelInterface loginBusiness;
    private final LoginViewInterface loginViewInterface;
    private final LocalUserInfoInterface localInfoBusiness;
    private final NetWorkInfoInterface netWorkBusiness;
    private String ssid;

    public LoginPresenter(Context context, LoginViewInterface loginViewInterface) {
        this.localInfoBusiness = new LocalInfoBusinessImp(context);
        this.netWorkBusiness = new NetWorkBusinessImp(context);
        this.loginBusiness = new LoginBusinessImp(context);
        this.loginViewInterface = loginViewInterface;

        init();

    }

    /**
     * 执行初始化操作,读取本地的用户名密码,显示到主界面,检测网络类型,显示到主界面
     */
    private void init() {
        UserInfo userInfo = localInfoBusiness.getUserInfo();
        if (!userInfo.isEmpty()) {
            loginViewInterface.setUserInfo(userInfo);
        }
        //初始化网络状态
        NetWorkInfoInterface.NetWorkStatus status = netWorkBusiness.getNetWorkType();
        String netWorkInfoString = null;
        switch (status) {
            case NOT_CONNECT:
                netWorkInfoString = "未连接网络";
                break;
            case WIFI:
                ssid = netWorkBusiness.getSSID();
                netWorkInfoString = "WIFI(" + ssid + ")";
                break;
            case OTHER:
                netWorkInfoString = netWorkBusiness.getName();
                break;
        }
        loginViewInterface.showNetWorkType(netWorkInfoString);
    }

    public void login() {
        UserInfo info = loginViewInterface.getUserInfo();
        localInfoBusiness.saveUserInfo(info);
        info.setSSID(ssid);

        //进行登录操作
        loginBusiness.login(
                info,
                ssid,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: " + response);
                        loginViewInterface.showMessage(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "onErrorResponse: " + error.toString());
                        loginViewInterface.showMessage(error.toString());
                    }
                });
    }


    public void login(String ssid) {
        this.ssid = ssid;
        login();
    }
}
