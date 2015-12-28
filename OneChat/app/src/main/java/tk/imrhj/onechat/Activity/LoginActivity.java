package tk.imrhj.onechat.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
        if (!clientId.equals("")) {
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
    }


    @OnClick(R.id.activity_login_btn_login)
    public void onLoginClick(View v) {
        final String clientId = nameView.getText().toString();
        final String passWd = null;
        if (TextUtils.isEmpty(clientId.trim())) {
            showToast(R.string.login_null_name_tip);
            return;
        }
        final Intent service = new Intent(this, WifiChangeService.class);
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
                                savePreferences(clientId, passWd);
                                service.putExtra("bool_login", true);
                                stopService(service);
                                startService(service);



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
     * 跳转到MainActivity
     */
    private void turnToMainActivity() {
        finish();
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void initChatManager(String userId) {
        final ChatManager chatManager = ChatManager.getInstance();
        chatManager.init(this);
        if (!TextUtils.isEmpty(userId)) {
            chatManager.setupManagerWithUserId(userId);
        }
        chatManager.setConversationEventHandler(ConversationEventHandler.getInstance());
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


}
