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

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.callback.AVIMSingleMessageQueryCallback;
import com.avoscloud.leanchatlib.activity.AVChatActivity;
import com.avoscloud.leanchatlib.controller.ChatManager;
import com.avoscloud.leanchatlib.event.ImTypeMessageEvent;
import com.avoscloud.leanchatlib.model.Room;
import com.avoscloud.leanchatlib.utils.Constants;
import com.avoscloud.leanchatlib.utils.ConversationManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import de.greenrobot.event.EventBus;
import tk.imrhj.onechat.Util.ConversationFragmentUpdateEvent;
import tk.imrhj.onechat.Adapter.roomListAdapter;
import tk.imrhj.onechat.R;
import tk.imrhj.onechat.Util.Utils;

/**
 * Created by rhj on 15/12/16.
 */
public class ConversationFragment extends Fragment implements AdapterView.OnItemClickListener {
    private static final String TAG = "ConversationFragment";
    private ListView mChatList;
    private List<AVIMConversation> mConversationList;
    private List<roomListAdapter.User> mRoomItem;
    private roomListAdapter mAdapter;
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
        initListView();

        return view;
    }

    /**
     * 初始化ListView
     */
    private void initListView() {
        mRoomItem = new ArrayList<>();
        mAdapter = new roomListAdapter(getActivity(), mRoomItem);
        mChatList.setAdapter(mAdapter);
        mChatList.setOnItemClickListener(this);
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
                    List<Room> sortedRooms = sortRooms(roomList);
                    updateLastMessage(sortedRooms);
                    addToRoomList(sortedRooms);
                    mAdapter.notifyDataSetChanged();

                } else {
                    Log.e(TAG, "done: " + exception.getMessage());
                }
            }
        });

    }

    private void addToRoomList(List<Room> roomList) {
        mRoomItem.clear();
        String selfID = ChatManager.getInstance().getSelfId();
        Date date;
        for (Room room : roomList) {
            roomListAdapter.User user = new roomListAdapter.User();
            user.mUserID = Utils.getConversationUserID(selfID, room.getConversation().getMembers());
            user.mLastTime = Utils.fromatTime(new Date(room.getLastModifyTime()));
            Log.d(TAG, "addToRoomList: " + user.mLastTime);
            user.mConversationID = room.getConversationId();
            if (room.getLastMessage() != null) {
                user.mLastMSG = room.getLastMessage().getContent();
            }
            user.mAvatarIcon = ConversationManager.getConversationIcon(room.getConversation());
            mRoomItem.add(user);
        }

    }

    /**
     * 更新消息
     * @param roomList
     */
    private void updateLastMessage(final List<Room> roomList) {
        for (final Room room : roomList) {
            AVIMConversation conversation = room.getConversation();
            if (null != conversation) {
                conversation.getLastMessage(new AVIMSingleMessageQueryCallback() {
                    @Override
                    public void done(AVIMMessage avimMessage, AVIMException e) {
                        if (null != e && null != avimMessage) {
                            room.setLastMessage(avimMessage);
                        }
                    }
                });
            }
        }

    }


    /**
     * 给房间按时间排序
     * @param roomList
     * @return
     */
    private List<Room> sortRooms(final List<Room> roomList) {
        List<Room> sortedList = new ArrayList<Room>();
        if (null != roomList) {
            sortedList.addAll(roomList);
            Collections.sort(sortedList, new Comparator<Room>() {
                @Override
                public int compare(Room lhs, Room rhs) {
                    long value = lhs.getLastModifyTime() - rhs.getLastModifyTime();
                    if (value > 0) {
                        return -1;
                    } else if (value < 0) {
                        return 1;
                    } else {
                        return 0;
                    }
                }
            });
        }
        return sortedList;
    }

    public void onEvent(ImTypeMessageEvent event) {
        updateConversationList();
    }

    public void onEvent(ConversationFragmentUpdateEvent updateEvent) {
        updateConversationList();
    }

    /**
     * Callback method to be invoked when an item in this AdapterView has
     * been clicked.
     * <p>
     * Implementers can call getItemAtPosition(position) if they need
     * to access the data associated with the selected item.
     *
     * @param parent   The AdapterView where the click happened.
     * @param view     The view within the AdapterView that was clicked (this
     *                 will be a view provided by the adapter)
     * @param position The position of the view in the adapter.
     * @param id       The row id of the item that was clicked.
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getContext(), AVChatActivity.class);
        intent.putExtra(Constants.CONVERSATION_ID, mRoomItem.get(position).mConversationID);
        getContext().startActivity(intent);

    }
}
