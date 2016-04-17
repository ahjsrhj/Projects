package cn.imrhj.newlogin.model;

import com.android.volley.Response;

import cn.imrhj.newlogin.presenter.LoginPresenter;

/**
 * Created by rhj on 16/4/17.
 */
public interface LoginModelInterface {
    void login(UserInfo userInfo, String SSID,
               Response.Listener<String> stringListener,
               Response.ErrorListener errorListener);
}
