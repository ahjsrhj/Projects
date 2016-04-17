package cn.imrhj.newlogin.model;

import android.text.TextUtils;
import android.util.Base64;

/**
 * Created by rhj on 16/4/17.
 */
public class UserInfo {
    private String username;
    private String password;
    private String SSID = "";

    public UserInfo(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void setSSID(String SSID) {
        this.SSID = SSID;
    }

    public String getUsername() {
        switch (SSID) {
            case "WLZX":
            case "rhj-miwifi_5G":
            case "rhj-miwifi":
                return "net";
            case "WXXY":
                return username;
            case "WXXY_ChinaNet":
                return username + "@chinanet";
            case "WXXY_ChinaUnicom":
                return username + "@chinaunicom";
            case "WXXY_CMCC":
            default:
                return username;

        }
    }

    public String getKeepUsername() {
        return username;
    }

    public String getKeepPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        switch (SSID) {
            case "WLZX":
            case "rhj-miwifi_5G":
            case "rhj-miwifi":
                return "net";
            case "WXXY":
                return password;
            case "WXXY_ChinaNet":
            case "WXXY_ChinaUnicom":
            case "WXXY_CMCC":
                return getBase64Pass();
            default:
                return password;
        }
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String getBase64Pass() {
        return "{B}" + Base64.encodeToString(password.getBytes(), Base64.DEFAULT);
    }

    public boolean isEmpty() {
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            return true;
        } else {
            return false;
        }
    }

    public String getAC_ID() {
        switch (SSID) {
            case "WLZX":
            case "rhj-miwifi_5G":
            case "rhj-miwifi":
            case "WXXY":
                return "4";
            case "WXXY_ChinaNet":
            case "WXXY_ChinaUnicom":
            case "WXXY_CMCC":
                return "2";
            default:
                return "4";
        }
    }
}
