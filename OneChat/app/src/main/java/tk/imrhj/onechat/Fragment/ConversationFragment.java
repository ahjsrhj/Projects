package tk.imrhj.onechat.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;

import java.util.List;

import tk.imrhj.onechat.Adapter.UserListAdapter;
import tk.imrhj.onechat.R;

/**
 * Created by rhj on 15/12/16.
 */
public class ConversationFragment extends Fragment {
    private ListView mChatList;
    private AVIMClient mClient;
    private List<AVIMConversation> mConversationList;
    private List<UserListAdapter.User> mUserList;
    private UserListAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_list, container, false);
        mChatList = (ListView) view.findViewById(R.id.lv_chat_list);
        mChatList.setEmptyView(view.findViewById(R.id.tv_chat_list_empty_view));


        //首先为ListView设置默认的Adapter
//        initListView();

        //为ListView添加用户数据
//        initUserList();
        return view;
    }
}
