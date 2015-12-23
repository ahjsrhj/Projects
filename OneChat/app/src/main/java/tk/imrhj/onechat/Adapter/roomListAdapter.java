package tk.imrhj.onechat.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.Conversation;
import com.avoscloud.leanchatlib.model.Room;
import com.avoscloud.leanchatlib.utils.PhotoUtils;
import com.avoscloud.leanchatlib.utils.ThirdPartUserUtils;
import com.makeramen.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import tk.imrhj.onechat.R;

/**
 * Created by rhj on 15/12/16.
 */
public class roomListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<User> mUserList;

    //构造方法
    public roomListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    //构造方法
    public roomListAdapter(Context context, List<User> userList) {
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
        //判断是否是缓存
        if (convertView == null) {
            viewHolder = new ViewHolder();
            //通过LayoutInlfater实例化布局
            convertView = mInflater.inflate(R.layout.list_view_chat_list, null);
            viewHolder.TVUserID = (TextView) convertView.findViewById(R.id.tv_chat_lsit_userid);
            viewHolder.TVLastMSG = (TextView) convertView.findViewById(R.id.tv_chat_list_lastmsg);
            viewHolder.TVLastTime = (TextView) convertView.findViewById(R.id.tv_chat_list_last_time);
            viewHolder.imageView = (RoundedImageView) convertView.findViewById(R.id.tv_chat_list_avatar_icon);
            convertView.setTag(viewHolder);
        } else {
            //通过tag找到缓存的布局
            viewHolder = (ViewHolder) convertView.getTag();
        }
        User user = mUserList.get(position);
        viewHolder.TVUserID.setText(user.mUserID);
        viewHolder.TVLastMSG.setText(user.mLastMSG);
        viewHolder.TVLastTime.setText(user.mLastTime);

        String avatar = ThirdPartUserUtils.getInstance().getUserAvatar(user.mUserID);
        ImageLoader.getInstance().displayImage(avatar, viewHolder.imageView, PhotoUtils.avatarImageOptions);

        AVIMConversation conversation = user.room.getConversation();
        if (null != conversation) {
        }

        return convertView;
    }

    public final class ViewHolder {
        public TextView TVUserID;
        public TextView TVLastMSG;
        public TextView TVLastTime;
        public RoundedImageView imageView;
    }

    public static class User {
        public Room room;
        public String mUserID;
        public String mLastMSG;
        public String mLastTime;
        public String mConversationID;
//        public Bitmap mAvatarIcon;

    }
}
