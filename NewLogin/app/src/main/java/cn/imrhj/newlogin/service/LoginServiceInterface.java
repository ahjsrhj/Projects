package cn.imrhj.newlogin.service;

import cn.imrhj.newlogin.model.UserInfo;

/**
 * Created by rhj on 16/4/18.
 *
 */
public interface LoginServiceInterface {
    String getNetworkType();

    UserInfo getUserInfo();

    void saveUserInfo(UserInfo userInfo);

    void forcedLogin();

}
