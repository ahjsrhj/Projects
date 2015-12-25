package tk.imrhj.onechat.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMConversationQuery;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback;
import com.avoscloud.leanchatlib.activity.AVChatActivity;
import com.avoscloud.leanchatlib.controller.ChatManager;
import com.avoscloud.leanchatlib.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import tk.imrhj.onechat.Adapter.ContactListAdapter;
import tk.imrhj.onechat.R;
import tk.imrhj.onechat.Util.Utils;

import static tk.imrhj.onechat.Adapter.ContactListAdapter.*;

/**
 * Created by rhj on 15/12/16.
 * 联系人Fragment,里面显示所有使用过这个产品的用户.
 */
public class ContactFragment extends Fragment implements AdapterView.OnItemClickListener {
    private static final String TAG = "ContactFragment";
    private ListView mContactList;
    private List<ContactListAdapter.User> mContactItem;
    private ContactListAdapter mAdapter;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_list, container, false);
        mContactList = (ListView) view.findViewById(R.id.lv_contact_list);
        initListView();
        return view;
    }

    /**
     * 初始化ListView
     */
    private void initListView() {
        mContactItem = new ArrayList<>();
        mAdapter = new ContactListAdapter(getActivity(), mContactItem);
        mContactList.setAdapter(mAdapter);
        mContactList.setOnItemClickListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        updateContactList();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateContactList();
    }

    /**
     * 更新用户列表
     */
    private void updateContactList() {
        ChatManager.getInstance().openClient(new AVIMClientCallback() {
            @Override
            public void done(AVIMClient avimClient, AVIMException e) {
                AVIMConversationQuery query = avimClient.getQuery();
                query.whereEqualTo("objectId", Utils.ALL_USER);
                query.findInBackground(new AVIMConversationQueryCallback() {
                    @Override
                    public void done(List<AVIMConversation> list, AVIMException e) {
                        if(e==null) {
                            if (null != list && !list.isEmpty()) {
                                mContactItem.clear();
                                List<String> userList = list.get(0).getMembers();
                                for (String userID : list.get(0).getMembers()) {
                                    User user = new User();
                                    user.userID = userID;

                                    mContactItem.add(user);
                                }
                                mAdapter.notifyDataSetChanged();

                            }
                        }

                    }
                });

            }
        });

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String userID = mContactItem.get(position).userID;
        Intent intent = new Intent(getActivity(), AVChatActivity.class);
        intent.putExtra(Constants.MEMBER_ID, userID);
        startActivity(intent);
    }
}
