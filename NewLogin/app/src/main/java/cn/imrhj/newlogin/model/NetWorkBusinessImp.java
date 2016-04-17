package cn.imrhj.newlogin.model;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

/**
 * Created by rhj on 16/4/17.
 * 判断网络连接,返回连接的网络类型
 */
public class NetWorkBusinessImp implements NetWorkInfoInterface {


    private static final String TAG = "NetWorkBusinessImp";
    private final NetworkInfo mNetworkInfo;
    private final WifiInfo mWifiInfo;

    public NetWorkBusinessImp(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        mNetworkInfo = manager.getActiveNetworkInfo();
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        mWifiInfo = wifiManager.getConnectionInfo();
        if (mWifiInfo != null) {
            Log.e(TAG, "NetWorkBusinessImp: SSID " + mWifiInfo.getSSID());
            Log.e(TAG, "NetWorkBusinessImp: " + modifySSID(mWifiInfo.getSSID()));
        }
    }

    @Override
    public NetWorkStatus getNetWorkType() {
        if (mNetworkInfo == null) {
            return NetWorkStatus.NOT_CONNECT;
        }
        if (mNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return NetWorkStatus.WIFI;
        }

        return NetWorkStatus.OTHER;
    }

    @Override
    public String getSSID() {
        if (mWifiInfo == null) return null;
        return modifySSID(mWifiInfo.getSSID());
    }

    @Override
    public String getName() {
        if (mNetworkInfo == null) return null;
        return mNetworkInfo.getTypeName();
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
