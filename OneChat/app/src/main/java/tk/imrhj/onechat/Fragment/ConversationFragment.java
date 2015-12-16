package tk.imrhj.onechat.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avoscloud.leanchatlib.model.Room;
import com.avoscloud.leanchatlib.utils.ConversationManager;

import java.util.List;

import de.greenrobot.event.EventBus;
import tk.imrhj.onechat.Activity.ConversationFragmentUpdateEvent;
import tk.imrhj.onechat.Adapter.UserListAdapter;
import tk.imrhj.onechat.R;

/**
 * Created by rhj on 15/12/16.
 */
public class ConversationFragment extends Fragment {
    private static final String TAG = "ConversationFragment";
    private ListView mChatList;
    private AVIMClient mClient;
    private List<AVIMConversation> mConversationList;
    private List<UserListAdapter.User> mUserList;
    private UserListAdapter mAdapter;
    private ConversationManager mConversationManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_list, container, false);
        mChatList = (ListView) view.findViewById(R.id.lv_chat_list);
        mChatList.setEmptyView(view.findViewById(R.id.tv_chat_list_empty_view));
        mConversationManager = ConversationManager.getInstance();
        EventBus.getDefault().register(this);
        //首先为ListView设置默认的Adapter
//        initListView();

        //为ListView添加用户数据
//        initUserList();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        updateConversationList();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateConversationList();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 更新列表
     */
    private void updateConversationList() {
        mConversationManager.findAndCacheRooms(new Room.MultiRoomsCallback() {
            @Override
            public void done(List<Room> roomList, AVException exception) {
                if (null == exception) {
                    Log.d(TAG, "done: " + roomList.size());
                } else {
                    Log.e(TAG, "done: " + exception.getMessage());
                }
            }
        });

    }

    public void onEvent(ConversationFragmentUpdateEvent updateEvent) {
        updateConversationList();
    }
}
