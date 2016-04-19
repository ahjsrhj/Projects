package cn.imrhj.newlogin.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import cn.imrhj.newlogin.service.LoginService;

/**
 * Created by rhj on 16/4/17.
 * todo 关闭或者开启wifi,会有多条广播,如何解决冲突
 */
public class WifiReceiver extends BroadcastReceiver{


    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, LoginService.class));
    }

}
