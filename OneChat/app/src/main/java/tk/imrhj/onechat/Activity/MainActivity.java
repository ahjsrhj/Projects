package tk.imrhj.onechat.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.avoscloud.leanchatlib.activity.AVChatActivity;
import com.avoscloud.leanchatlib.utils.Constants;

import java.util.Arrays;
import java.util.List;

import de.greenrobot.event.EventBus;
import tk.imrhj.onechat.Fragment.ContactFragment;
import tk.imrhj.onechat.Fragment.ConversationFragment;
import tk.imrhj.onechat.Fragment.PersonalProfileFragment;
import tk.imrhj.onechat.R;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        viewPager = (ViewPager)findViewById(R.id.pager);
        tabLayout = (TabLayout)findViewById(R.id.tablayout);
        initToolbar();
        initTabLayout();
    }

    /**
     * 初始化Toolbar
     */
    private void initToolbar() {
        setTitle("OneChat");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);


//        toolbar.setOnMenuItemClickListener(this);
    }

    private void initTabLayout() {
        String[] tabList = new String[]{"会话", "朋友", "我"};
        final Fragment[] fragmentList = new Fragment[] {new ConversationFragment(),
                new ContactFragment(), new PersonalProfileFragment()};

        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        for (int i = 0; i < tabList.length; i++) {
            tabLayout.addTab(tabLayout.newTab().setText(tabList[i]));
        }

        TabFragmentAdapter adapter = new TabFragmentAdapter(getSupportFragmentManager(),
                Arrays.asList(fragmentList), Arrays.asList(tabList));
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (0 == position) {
                    EventBus.getDefault().post(new ConversationFragmentUpdateEvent());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabsFromPagerAdapter(adapter);
    }

    public class TabFragmentAdapter extends FragmentStatePagerAdapter {

        private List<Fragment> mFragments;
        private List<String> mTitles;

        public TabFragmentAdapter(FragmentManager fm, List<Fragment> fragments, List<String> titles) {
            super(fm);
            mFragments = fragments;
            mTitles = titles;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles.get(position);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add) {
            showEditTextDialog();
        }
        return true;
    }

    private void showEditTextDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.dialog_edit_view, null);
        builder.setView(layout);
        final EditText editText = (EditText) layout.findViewById(R.id.edtTxt_addChat);
        builder.setTitle("开启对话");
        builder.setPositiveButton("对话", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String userID = editText.getText().toString();
                if (userID.equals("")) {
                    Toast.makeText(MainActivity.this, "请输入ID", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(MainActivity.this, AVChatActivity.class);
                intent.putExtra(Constants.CONVERSATION_ID, userID);
                startActivity(intent);

            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();

    }
}
