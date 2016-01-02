package tk.imrhj.onechat.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avoscloud.leanchatlib.controller.ChatManager;
import com.avoscloud.leanchatlib.controller.ConversationEventHandler;

import tk.imrhj.onechat.R;

/**
 * Created by rhj on 15/12/31.
 */
public class WelcomeActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        init();
    }

    /**
     * 初始化， 判断用户是否已经登录
     */
    private void init() {
        SharedPreferences preferences = getSharedPreferences(getString(R.string.string_file_name), MODE_MULTI_PROCESS);
        boolean loginSucceed = preferences.getBoolean(getString(R.string.string_login_succeed), false);
        if (loginSucceed) {
            String userId = preferences.getString(getString(R.string.string_user_name), "");
            initChat(userId);
            ChatManager.getInstance().openClient(new AVIMClientCallback() {
                @Override
                public void done(AVIMClient avimClient, AVIMException e) {
                    if (null == e) {
                        finish();
                        startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                    } else {
                        Toast.makeText(WelcomeActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            startActivity(new Intent(this, LoginActivity.class));
        }

    }

    private void initChat(String userId) {
        ChatManager chatManager = ChatManager.getInstance();
        chatManager.init(this);
        if (!TextUtils.isEmpty(userId)) {
            chatManager.setupManagerWithUserId(userId);
        }
        chatManager.setConversationEventHandler(ConversationEventHandler.getInstance());
    }

}
