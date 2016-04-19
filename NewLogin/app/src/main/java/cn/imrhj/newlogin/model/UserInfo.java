package cn.imrhj.newlogin.model;

import android.text.TextUtils;
import android.util.Base64;

/**
 * Created by rhj on 16/4/17.
 * 用户信息实体类
 */
public class UserInfo {
    private String username;
    private String password;

    public UserInfo(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEmpty() {
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            return true;
        } else {
            return false;
        }
    }

}
