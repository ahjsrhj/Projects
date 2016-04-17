package cn.imrhj.newlogin.model;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import cn.imrhj.newlogin.presenter.LoginPresenter;

/**
 * Created by rhj on 16/4/17.
 */
public class LoginBusinessImp implements LoginModelInterface {
    private static final String TAG = "LoginBusinessImp";
    private RequestQueue mQueue;
    Map<String, String> mLinkMap = new HashMap<String, String>();

    public LoginBusinessImp(Context context) {
        mQueue = Volley.newRequestQueue(context);
        mLinkMap.put("WLZX", "http://211.70.160.3/include/auth_action.php");
        mLinkMap.put("WXXY", "http://211.70.160.3/include/auth_action.php");
        mLinkMap.put("rhj-miwifi_5G", "http://211.70.160.3/include/auth_action.php");
        mLinkMap.put("rhj-miwifi", "http://211.70.160.3/include/auth_action.php");
        mLinkMap.put("WXXY_ChinaNet", "http://211.70.160.207:801/include/auth_action.php");
        mLinkMap.put("WXXY_ChinaUnicom", "http://211.70.160.207:801/include/auth_action.php");
        mLinkMap.put("WXXY_CMCC", "http://211.70.160.207:801/include/auth_action.php");
        //// TODO: 16/4/17 写入配置文件中,并且可以手动添加
    }

    @Override
    public void login(final UserInfo userInfo, final String SSID,
                      Response.Listener<String> stringListener,
                      Response.ErrorListener errorListener) {
        StringRequest request = new StringRequest(
                Request.Method.POST, mLinkMap.get(SSID),stringListener, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("action", "login");
                map.put("username", userInfo.getUsername());
                map.put("password", userInfo.getPassword());
                map.put("ac_id", userInfo.getAC_ID());
                map.put("user_ip", "");
                map.put("nas_ip", "");
                map.put("user_mac", "");
                map.put("save_me", "0");
                map.put("ajax", "1");
                Log.d(TAG, "getParams: " + map.toString());
                return map;
            } };
        Log.d(TAG, "login: " + userInfo.getUsername() +" ssid =  " + SSID);
        mQueue.add(request);
    }
}
