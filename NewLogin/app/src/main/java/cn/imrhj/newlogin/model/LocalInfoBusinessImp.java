package cn.imrhj.newlogin.model;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by rhj on 16/4/17.
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
        return new UserInfo(username, password);
    }

    /**
     * 保存用户信息到本地
     *
     * @param userInfo
     */
    @Override
    public void saveUserInfo(UserInfo userInfo) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", userInfo.getKeepUsername());
        editor.putString("password", userInfo.getKeepPassword());
        editor.apply();
    }
}
