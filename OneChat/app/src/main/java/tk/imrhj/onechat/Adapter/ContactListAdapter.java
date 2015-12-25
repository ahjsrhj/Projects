package tk.imrhj.onechat.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.avoscloud.leanchatlib.utils.PhotoUtils;
import com.avoscloud.leanchatlib.utils.ThirdPartUserUtils;
import com.makeramen.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import tk.imrhj.onechat.R;

/**
 * Created by rhj on 15/12/25.
 * 联系人列表适配器
 */
public class ContactListAdapter extends BaseAdapter {
    private LayoutInflater  mInflater;
    private List<User> mUserList;


    /**
     * 构造方法
     * @param context 传递Context对象构造LayoutInflater对象
     */
    public ContactListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }


    /**
     * 构造方法
     * @param context 传递Context对象构造LayoutInflater对象
     * @param userList 初始化mUserList数据源
     */
    public ContactListAdapter(Context context, List<User> userList) {
        mInflater = LayoutInflater.from(context);
        mUserList = userList;
    }

    @Override
    public int getCount() {
        return mUserList.size();
    }

    @Override
    public Object getItem(int position) {
        return mUserList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (null == convertView) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.list_view_contact_list, null);
            viewHolder.TVUserID = (TextView) convertView.findViewById(R.id.tv_contact_list_userid);
            viewHolder.TVLastLogin =
                    (TextView) convertView.findViewById(R.id.tv_contact_list_last_login);
            viewHolder.IVAvatarIcon =
                    (RoundedImageView) convertView.findViewById(R.id.tv_contact_list_avatar_icon);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        User user = mUserList.get(position);
        viewHolder.TVUserID.setText(user.userID);
        viewHolder.TVLastLogin.setText(user.lastLogin);
        String avatar = ThirdPartUserUtils.getInstance().getUserAvatar(user.userID);
        ImageLoader.getInstance().displayImage(avatar, viewHolder.IVAvatarIcon, PhotoUtils.avatarImageOptions);
        return convertView;
    }

    public final class ViewHolder {
        public TextView TVUserID;
        public TextView TVLastLogin;
        public RoundedImageView IVAvatarIcon;
    }


    public static class User{
        public String userID;
        public String lastLogin;
        public String userName;
    }
}
