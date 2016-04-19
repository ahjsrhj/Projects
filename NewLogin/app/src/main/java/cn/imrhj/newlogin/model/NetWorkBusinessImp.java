package cn.imrhj.newlogin.model;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by rhj on 16/4/17.
 * 判断网络连接,返回连接的网络类型
 */
public class NetWorkBusinessImp implements NetWorkInfoInterface {

    private static final String NO_NETWORK = "未连接网络";
    public static final String CHINA_NET = "WXXY_ChinaNet";
    public static final String CHINA_UNICOM = "WXXY_ChinaUnicom";
    public static final String CMCC = "WXXY_CMCC";

    private static final String TAG = "NetWorkBusinessImp";
    private NetworkInfo mNetworkInfo;
    private WifiInfo mWifiInfo;
    private final ConnectivityManager mConnectivityManager;
    private final WifiManager mWifiManager;

    public NetWorkBusinessImp(Context context) {
        mConnectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        mWifiInfo = mWifiManager.getConnectionInfo();
        if (mWifiInfo != null) {
            Log.e(TAG, "NetWorkBusinessImp: SSID " + mWifiInfo.getSSID());
        }
    }

    @Override
    public String getNetWorkType() {

        mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        mWifiInfo = mWifiManager.getConnectionInfo();
        if (mNetworkInfo == null) {
            return NO_NETWORK;
        }

        if (mNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return mNetworkInfo.getTypeName() + mWifiInfo.getSSID();
        }
        return mNetworkInfo.getTypeName();
    }

    @Override
    public String getSSID() {
        mWifiInfo = mWifiManager.getConnectionInfo();
        if (mWifiInfo != null) {
            return modifySSID(mWifiInfo.getSSID());
        } else {
            return null;
        }
    }

    @Override
    public String getName() {
        if (mNetworkInfo == null) return null;
        return mNetworkInfo.getTypeName();
    }

    /**
     * 是否为三家运营商的无线
     * @return
     */
    @Override
    public boolean isThreeSSID() {
        String SSID = modifySSID(mWifiInfo.getSSID());
        if (!TextUtils.isEmpty(SSID)) {
            if (SSID.contains(CHINA_NET) || SSID.contains(CHINA_UNICOM) || SSID.contains(CMCC)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 是否为可以登录的SSID
     */
    @Override
    public boolean isCanLoginSSID() {
        mWifiInfo = mWifiManager.getConnectionInfo();
        String SSID = modifySSID(mWifiInfo.getSSID());
        Log.d(TAG, "isCanLoginSSID: " + SSID);
        if (    SSID.contains("WXXY")
                || SSID.contains("WLZX")
                || SSID.contains("rhj-miwifi_5G")
                || SSID.contains("rhj-miwifi")
                || SSID.contains("WXXY_ChinaNet")
                || SSID.contains("WXXY_ChinaUnicom")
                || SSID.contains("WXXY_CMCC")) {
            return true;
        }
        return false;
    }


    /**
     * 将SSID中可能出现的"删除
     * @param ssid
     * @return
     */
    private String modifySSID(String ssid) {
        if (ssid.startsWith("\"")) {
            return ssid.substring(1,ssid.length()-1);
        }
        return ssid;
    }

}
