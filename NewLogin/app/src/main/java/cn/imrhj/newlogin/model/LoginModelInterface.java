package cn.imrhj.newlogin.model;

import com.android.volley.Response;

import cn.imrhj.newlogin.Utils.UserInfoModify;
import cn.imrhj.newlogin.presenter.LoginPresenter;

/**
 * Created by rhj on 16/4/17.
 */
public interface LoginModelInterface {
    void login(UserInfoModify infoModify,
               Response.Listener<String> stringListener,
               Response.ErrorListener errorListener);

    void forcedLogin(Response.Listener<String> stringListener, Response.ErrorListener errorListener);
}
