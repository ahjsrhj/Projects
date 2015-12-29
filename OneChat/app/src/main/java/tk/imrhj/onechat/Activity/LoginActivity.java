package tk.imrhj.onechat.Activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avoscloud.leanchatlib.activity.AVBaseActivity;
import com.avoscloud.leanchatlib.controller.ChatManager;
import com.avoscloud.leanchatlib.controller.ConversationEventHandler;

import butterknife.Bind;
import butterknife.OnClick;
import tk.imrhj.onechat.R;
import tk.imrhj.onechat.Service.WifiChangeService;
import tk.imrhj.onechat.Util.Utils;

public class LoginActivity extends AVBaseActivity {
    @Bind(R.id.activity_login_et_username)
    protected EditText nameView;

    @Bind(R.id.activity_login_et_password)
    protected EditText passView;

    @Bind(R.id.activity_login_btn_login)
    protected Button loginBtn;

    private WifiChangeService wifiChangeService;
    private MyHandler handler = new MyHandler();
    private String mClientID;
    private boolean mLoginSucceed = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initEditText();
    }

    /**
     * 初始化EditText
     */
    private void initEditText() {
        loadData();
        String clientId = nameView.getText().toString();
        if (mLoginSucceed && !clientId.equals("")) {
            initChatManager(clientId);
            turnToMainActivity();
        }
    }

    /**
     * 当打开应用时读取数据
     */
    private void loadData() {
        SharedPreferences preferences = getSharedPreferences(getString(R.string.string_file_name), MODE_MULTI_PROCESS);
        nameView.setText(preferences.getString(getString(R.string.string_user_name), ""));
        passView.setText(preferences.getString(getString(R.string.string_pass_word), ""));
        mLoginSucceed = preferences.getBoolean(getString(R.string.string_login_succeed), false);
    }


    @OnClick(R.id.activity_login_btn_login)
    public void onLoginClick(View v) {
        mClientID = nameView.getText().toString();
        final String passWd = null;
        if (TextUtils.isEmpty(mClientID.trim())) {
            showToast(R.string.login_null_name_tip);
            return;
        }
        final Intent service = new Intent(this, WifiChangeService.class);
        savePreferences(mClientID, passWd);
        service.putExtra("bool_login", true);
        stopService(service);
        startService(service);
        bindWifiChangeService(service);
    }

    /**
     * 初始化聊天服务，登录聊天系统
     * @param clientId
     */
    private void initChat(String clientId) {
        initChatManager(clientId);
        ChatManager.getInstance().openClient(new AVIMClientCallback() {
            @Override
            public void done(AVIMClient avimClient, AVIMException e) {
                if (null == e) {
                    AVIMConversation conversation = avimClient.getConversation(Utils.ALL_USER);
                    conversation.join(new AVIMConversationCallback() {
                        @Override
                        public void done(AVIMException e) {
                            if (null == e) {
                                turnToMainActivity();
                            }
                        }
                    });
                } else {
                    showToast(e.toString());
                }
            }
        });
    }

    /**
     * 初始化聊天管理器
     * @param userId
     */
    private void initChatManager(String userId) {
        final ChatManager chatManager = ChatManager.getInstance();
        chatManager.init(this);
        if (!TextUtils.isEmpty(userId)) {
            chatManager.setupManagerWithUserId(userId);
        }
        chatManager.setConversationEventHandler(ConversationEventHandler.getInstance());
    }


    /**
     * Bind服务，获取该服务的实例
     * @param intent
     */
    private void bindWifiChangeService(Intent intent) {
        ServiceConnection connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                wifiChangeService = ((WifiChangeService.MsgBinder) service).getService();
                new Thread(new MyRunnable()).start();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };

        bindService(intent, connection, 0);
    }

    /**
     * 跳转到MainActivity
     */
    private void turnToMainActivity() {
        finish();
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }


    /**
     * 保存帐号信息到本地
     * @param username
     * @param password
     */
    private void savePreferences(String username, String password) {
        SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.string_file_name), MODE_MULTI_PROCESS).edit();
        editor.putString(getString(R.string.string_user_name), username);
        editor.putString(getString(R.string.string_pass_word), password);
        editor.apply();
    }

    private class MyRunnable implements Runnable {

        @Override
        public void run() {
            int isLogin;
            while ((isLogin = wifiChangeService.isLogin()) == -1) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (isLogin == 0) {
                //wifi登录成功
                Message msg = new Message();
                msg.what = WifiChangeService.CONNECT_SUCCESS;
                handler.sendMessage(msg);
            } else {
                //wifi登录失败
            }

        }
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WifiChangeService.CONNECT_SUCCESS:
                    initChat(mClientID);
                    SharedPreferences.Editor editor = getSharedPreferences(
                                    getString(R.string.string_file_name),
                                    MODE_MULTI_PROCESS).edit();
                    editor.putBoolean(getString(R.string.string_login_succeed), true);
                    editor.apply();
                    break;
            }
        }
    }


}
