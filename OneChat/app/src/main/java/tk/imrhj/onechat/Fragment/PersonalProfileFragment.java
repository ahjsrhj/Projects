package tk.imrhj.onechat.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avoscloud.leanchatlib.controller.ChatManager;
import com.avoscloud.leanchatlib.utils.PhotoUtils;
import com.avoscloud.leanchatlib.utils.ThirdPartUserUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tk.imrhj.onechat.Activity.LoginActivity;
import tk.imrhj.onechat.R;

/**
 * Created by rhj on 15/12/16.
 */
public class PersonalProfileFragment extends Fragment {

    @Bind(R.id.profile_avatar_view)
    protected ImageView avavtarView;

    @Bind(R.id.profile_username_view)
    protected TextView nameView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal_profile, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        bindData();
    }

    private void bindData() {
        ThirdPartUserUtils.ThirdPartUser thirdPartUser = ThirdPartUserUtils.getInstance().getSelf();
        nameView.setText(ChatManager.getInstance().getSelfId());
        ImageLoader.getInstance().displayImage(thirdPartUser.avatarUrl, avavtarView, PhotoUtils.avatarImageOptions);
    }

    @OnClick(R.id.profile_logout_btn)
    protected void onLogoutClick(View view) {
        SharedPreferences.Editor editor = getActivity().getSharedPreferences(
                getString(R.string.string_file_name),
                Context.MODE_MULTI_PROCESS).edit();
        editor.putBoolean(getString(R.string.string_login_succeed), false);
        editor.apply();



        ChatManager.getInstance().closeWithCallback(new AVIMClientCallback() {
            @Override
            public void done(AVIMClient avimClient, AVIMException e) {
                getActivity().finish();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                getActivity().startActivity(intent);
            }
        });
    }
}
