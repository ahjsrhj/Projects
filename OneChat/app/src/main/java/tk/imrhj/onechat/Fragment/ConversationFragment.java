package tk.imrhj.onechat.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.avoscloud.leanchatlib.utils.ConversationManager;

import tk.imrhj.onechat.R;

public class ConversationFragment extends Fragment {
    private ListView mChatListView;
    private ConversationManager conversationManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conversation, container, false);
        mChatListView = (ListView) view.findViewById(R.id.lv_chat_list);
        mChatListView.setEmptyView(view.findViewById(R.id.tv_chat_list_empty_view));

        conversationManager = ConversationManager.getInstance();


        return view;
    }
}
