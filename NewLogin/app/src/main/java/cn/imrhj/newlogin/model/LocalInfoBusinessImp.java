package cn.imrhj.newlogin.model;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by rhj on 16/4/17.
 * 本地信息读取业务代码
 */
public class LocalInfoBusinessImp implements LocalUserInfoInterface {
    private final SharedPreferences sharedPreferences;

    public LocalInfoBusinessImp(Context context) {
        this.sharedPreferences = context.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
    }

    /**
     * 获取本地存储的用户信息
     *
     * @return
     */
    @Override
    public UserInfo getUserInfo() {
        String username = sharedPreferences.getString("username", "");
        String password = sharedPreferences.getString("password", "");
        UserInfo userInfo = new UserInfo(username, password);
        if (userInfo.isEmpty()) {
            return null;
        } else {
            return userInfo;
        }
    }

    /**
     * 保存用户信息到本地
     *
     * @param userInfo
     */
    @Override
    public void saveUserInfo(UserInfo userInfo) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", userInfo.getUsername());
        editor.putString("password", userInfo.getPassword());
        editor.apply();
    }
}
