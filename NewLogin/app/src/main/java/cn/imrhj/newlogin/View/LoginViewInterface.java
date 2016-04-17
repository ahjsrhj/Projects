package cn.imrhj.newlogin.View;

import cn.imrhj.newlogin.model.UserInfo;

/**
 * Created by rhj on 16/4/17.
 */
public interface LoginViewInterface {

    /**
     * 获取用户信息
     * @return
     */
    UserInfo getUserInfo();

    /**
     * 设置用户信息
     * @param userInfo
     */
    void setUserInfo(UserInfo userInfo);

    /**
     * 显示网络类型
     * @param string
     */
    void showNetWorkType(String string);

    /**
     * 显示提示信息
     * @param msg
     */
    void showMessage(String msg);


}
