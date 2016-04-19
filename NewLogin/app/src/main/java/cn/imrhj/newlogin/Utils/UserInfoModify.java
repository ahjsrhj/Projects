package cn.imrhj.newlogin.Utils;

import android.text.TextUtils;
import android.util.Base64;


import cn.imrhj.newlogin.model.UserInfo;

/**
 * Created by rhj on 16/4/18.
 * 对用户信息进行加工修饰,以获取正确的用户名和密码
 * 对于不通的SSID,用户名和密码有不同的规则,主要如下
 * WXXY_ChinaNet: 用户名后面+"@chinanet"
 * WXXY_ChinaUnicom: 用户名后面+"@chinaunicom"
 * WXXY_CMCC: 用户名后面+"@cmcc"
 * 密码均为使用Base64加密
 * WXXY: 用户名与密码不做修改
 * WLZX:
 * rhj-miwifi:
 * rhj-miwifi_5G:
 * 使用默认规则: 用户名密码均为net,不做修改
 * */
public class UserInfoModify {
    private UserInfo mUserInfo;
    private String SSID;

    public UserInfoModify(UserInfo mUserInfo, String SSID) {
        this.mUserInfo = mUserInfo;
        this.SSID = SSID;
    }

    public UserInfo getmUserInfo() {
        return mUserInfo;
    }

    public void setmUserInfo(UserInfo mUserInfo) {
        this.mUserInfo = mUserInfo;
    }

    public String getSSID() {
        return SSID;
    }

    public void setSSID(String SSID) {
        this.SSID = SSID;
    }

    public boolean isUserInfoInvalid() {
        if (mUserInfo == null && SSID.contains("WXXY")) {
            return true;
        }
        return false;
    }

    public String getUserName() {
        switch (SSID) {
            case "WLZX":
            case "rhj-miwifi_5G":
            case "rhj-miwifi":
                return "net";
            case "WXXY":
                return mUserInfo.getUsername();
            case "WXXY_ChinaNet":
                return mUserInfo.getUsername() + "@chinanet";
            case "WXXY_ChinaUnicom":
                return mUserInfo.getUsername() + "@chinaunicom";
            case "WXXY_CMCC":
            default:
                if (TextUtils.isEmpty(mUserInfo.getUsername())) {
                    return "net";
                } else {
                    return mUserInfo.getUsername();
                }

        }
    }

    public String getPassword() {
        switch (SSID) {
            case "WLZX":
            case "rhj-miwifi_5G":
            case "rhj-miwifi":
                return "net";
            case "WXXY":
                return mUserInfo.getPassword();
            case "WXXY_ChinaNet":
            case "WXXY_ChinaUnicom":
            case "WXXY_CMCC":
                return getBase64Pass();
            default:
                if (TextUtils.isEmpty(mUserInfo.getPassword())) {
                    return "net";
                } else {
                    return mUserInfo.getPassword();
                }
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

    private String getBase64Pass() {
        return "{B}" + Base64.encodeToString(mUserInfo.getPassword().getBytes(), Base64.DEFAULT);
    }
}
