package tk.imrhj.onechat;

import android.app.Application;
import android.content.Context;

import com.avos.avoscloud.AVOSCloud;
import com.avoscloud.leanchatlib.controller.ChatManager;
import com.avoscloud.leanchatlib.utils.ThirdPartUserUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import tk.imrhj.onechat.Provider.CustomUserProvider;

/**
 * Created by rhj on 15/12/14.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AVOSCloud.initialize(this, "07gOGEDXPxscEGjQ3GxLlw1h", "J3ImbK0wurReQWGpNpEOYEhV");
        ChatManager.setDebugEnabled(true);// tag leanchatlib
        AVOSCloud.setDebugLogEnabled(true);  // set false when release
        ChatManager.getInstance().init(this);
        initImageLoader(this);

        ThirdPartUserUtils.setThirdPartUserProvider(new CustomUserProvider());
    }


    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPoolSize(3).threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .build();
        ImageLoader.getInstance().init(config);
    }
}
