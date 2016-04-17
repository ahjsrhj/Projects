package cn.imrhj.newlogin.model;

/**
 * Created by rhj on 16/4/17.
 */
public interface LocalUserInfoInterface {
    /**
     * 获取本地存储的用户信息
     * @return
     */
    UserInfo getUserInfo();

    /**
     * 保存用户信息到本地
     * @param userInfo
     */
    void saveUserInfo(UserInfo userInfo);
}
