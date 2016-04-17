package cn.imrhj.newlogin.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import cn.imrhj.newlogin.service.LoginService;

/**
 * Created by rhj on 16/4/17.
 * todo 关闭或者开启wifi,会有多条广播,如何解决冲突
 */
public class WifiReceiver extends BroadcastReceiver{
    private int a = 1;

    private static final String TAG = "WifiReciever";


    @Override
    public void onReceive(Context context, Intent intent) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifiManager.getConnectionInfo();
        if (info != null) {
            String SSID = info.getSSID();
            if (    SSID.contains("WXXY")
                    || SSID.contains("WLZX")
                    || SSID.contains("rhj-miwifi_5G")
                    || SSID.contains("rhj-miwifi")
                    || SSID.contains("WXXY_ChinaNet")
                    || SSID.contains("WXXY_ChinaUnicom")
                    || SSID.contains("WXXY_CMCC")) {
                Intent service = new Intent(context, LoginService.class);
                service.putExtra("ssid", modifySSID(info.getSSID()));
                context.startService(service);

            }
        }

    }

    private String modifySSID(String ssid) {
        if (ssid.startsWith("\"")) {
            return ssid.substring(1,ssid.length()-1);
        }
        return ssid;
    }
}
