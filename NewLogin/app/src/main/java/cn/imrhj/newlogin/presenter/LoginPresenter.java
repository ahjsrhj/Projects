package cn.imrhj.newlogin.presenter;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.util.Log;

import cn.imrhj.newlogin.View.LoginViewInterface;
import cn.imrhj.newlogin.model.UserInfo;
import cn.imrhj.newlogin.service.LoginService;
import cn.imrhj.newlogin.service.LoginServiceInterface;

/**
 * Created by rhj on 16/4/17.
 * 与activity进行交互,执行登录\获取用户信息并反馈到Activity\
 * 注册广播,获取网络状态反馈到Activity\
 */
public class LoginPresenter {
    private static final String TAG = "LoginPresenter";
    private final LoginViewInterface loginViewInterface;
//    private final LocalUserInfoInterface localInfoBusiness;
    private LoginServiceInterface loginService;
    private WifiReceiver mWifiReceiver;
    private Context mContext;
    private Intent mServiceIntent;

    public LoginPresenter(Context context, LoginViewInterface loginViewInterface) {
//        this.localInfoBusiness = new LocalInfoBusinessImp(context);
        this.loginViewInterface = loginViewInterface;
        this.mContext = context;

        init();

    }

    /**
     * 执行初始化操作,读取本地的用户名密码,显示到主界面
     * 加载服务,检测网络类型,显示到主界面
     */
    private void init() {
        // 动态注册广播接收器
        mWifiReceiver = new WifiReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.wifi.WIFI_STATE_CHANGE");
        intentFilter.addAction("android.net.wifi.STATE_CHANGE");
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        mContext.registerReceiver(mWifiReceiver, intentFilter);

        // 绑定服务,同时启动一遍,防止service在activity被销毁时关闭
        mServiceIntent = new Intent(mContext, LoginService.class);
        mContext.bindService(mServiceIntent, conn, Context.BIND_AUTO_CREATE);
        mContext.startService(mServiceIntent);
    }

    public void login() {
        // 点击登录按钮,执行强制登录,使用默认测量发送请求
        UserInfo userInfo = loginViewInterface.getUserInfo();
        if (userInfo != null && !userInfo.isEmpty()) {
            loginService.saveUserInfo(userInfo);
        }

        loginService.forcedLogin();
    }

    /**
     * 断开服务的连接,当Activity被销毁时调用
     */
    public void onActivityDestroy() {
        mContext.unbindService(conn);
        mContext.unregisterReceiver(mWifiReceiver);
    }

    public class WifiReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e(TAG, "onReceive: 网络状态发生改变" );
            if (loginService != null) {
                loginViewInterface.showNetworkType(loginService.getNetworkType());
            }
        }
    }

    ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e(TAG, "onServiceConnected: ");
            LoginService.LoginServieBinder binder = (LoginService.LoginServieBinder) service;
            loginService = binder.getLoginService();
            loginViewInterface.setUserInfo(loginService.getUserInfo());
            loginViewInterface.showNetworkType(loginService.getNetworkType());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            loginService = null;
            mContext.bindService(mServiceIntent, conn, Context.BIND_AUTO_CREATE);
            Log.e(TAG, "onServiceDisconnected: ");
        }
    };


}
