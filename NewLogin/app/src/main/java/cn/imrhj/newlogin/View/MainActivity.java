package cn.imrhj.newlogin.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.imrhj.newlogin.R;
import cn.imrhj.newlogin.model.UserInfo;
import cn.imrhj.newlogin.presenter.LoginPresenter;

public class MainActivity extends AppCompatActivity implements LoginViewInterface {
    private static final String TAG = "MainAcitivity";
    @Bind(R.id.edit_username) EditText mUserName;
    @Bind(R.id.edit_password) EditText mPassword;
    @Bind(R.id.tv_networdType) TextView mNetWorkType;

    private LoginPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mPresenter = new LoginPresenter(this, this);

    }

    /**
     * 获取用户信息
     *
     * @return
     */
    @Override
    public UserInfo getUserInfo() {
        return new UserInfo(mUserName.getText().toString(), mPassword.getText().toString());
    }

    /**
     * 设置用户信息
     *
     * @param userInfo
     */
    @Override
    public void setUserInfo(UserInfo userInfo) {
        mUserName.setText(userInfo.getUsername());
        mPassword.setText(userInfo.getPassword());
    }

    /**
     * 显示网络类型
     *
     * @param string
     */
    @Override
    public void showNetWorkType(String string) {
        mNetWorkType.setText(string);
    }


    /**
     * 显示提示信息
     *
     * @param msg
     */
    @Override
    public void showMessage(String msg) {
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    public void onClick(View view) {
        mPresenter.login();
    }
}
