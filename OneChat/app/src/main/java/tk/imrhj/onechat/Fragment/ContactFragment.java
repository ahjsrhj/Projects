package tk.imrhj.onechat.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import tk.imrhj.onechat.R;

/**
 * Created by rhj on 15/12/16.
 * 联系人Fragment,里面显示所有使用过这个产品的用户.
 */
public class ContactFragment extends Fragment {
    private ListView mContactList;



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

    }
}
