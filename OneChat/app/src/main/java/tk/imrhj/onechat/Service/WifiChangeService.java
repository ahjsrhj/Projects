package tk.imrhj.onechat.Service;

import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import tk.imrhj.onechat.R;

/**
 * Created by rhj on 15/12/28.
 * 当Wifi连接发生变化时， 弹出提示是否连接网络。
 */
public class WifiChangeService extends Service {
    private String username;
    private String password;
    private String userLength;
    private String userPost;
    private boolean wifiConnect = false;
    private boolean haveConnect = false;
    private boolean showDialog = true;
    private int isLogin = -1; //-1 默认值， 0 登录成功 1 登录失败

    private KeyguardManager keyguardManager;

    private final int CONTENT_SUCCESS = 1;
    private final int CONTENT_FAILD = 0;


    private MyHandler handler;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MsgBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferences preferences = getSharedPreferences(getString(R.string.string_file_name), MODE_MULTI_PROCESS);
        username = preferences.getString(getString(R.string.string_user_name), "");
        password = preferences.getString(getString(R.string.string_pass_word), "");
        String string = "action=login&username=" + username + "&password=" + password
                + "&ac_id=4&user_ip=&nas_ip=&user_mac=&save_me=0&ajax=1";
        userPost = string.toLowerCase();
        userLength = String.valueOf(userPost.length());

        keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
        handler = new MyHandler();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        WifiInfo info = wifiManager.getConnectionInfo();
        int ipAddress = (info == null ? 0 : info.getIpAddress());
        if (wifiManager.isWifiEnabled() && ipAddress != 0) {
            wifiConnect = true;
            testNetworkConnection();
        } else {
            wifiConnect = false;
            haveConnect = false;
        }

        if (wifiConnect && !haveConnect) {

            String SSID = info.getSSID();
            if (((SSID.equals("\"WXXY\"")) || (SSID.equals("WXXY")))) {
                if (showDialog) {
                    if (!keyguardManager.inKeyguardRestrictedInputMode()) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                        dialog.setTitle("提示")
                                .setMessage("探子来报!WXXY已连接!\n是否登陆?")
                                .setPositiveButton("登陆", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        doLogin(userPost, userLength);
                                        showDialog = true;
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        haveConnect = false;
                                        showDialog = true;
                                    }
                                });
                        AlertDialog ad = dialog.create();
                        ad.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                        ad.setCanceledOnTouchOutside(false);
                        ad.show();
                        showDialog = false;
                    }
                }

            } else if (intent.getBooleanExtra("bool_login", false)){
                showToast("首次使用，请连接到WXXY无线网络!");
            }
        }
        flags = START_STICKY;
        return super.onStartCommand(intent, flags, startId);
    }


    public void doLogin(final String post, final String content_length) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL("http://211.70.160.3/include/auth_action.php");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    connection.setUseCaches(false);

                    //设置连接属性
                    connection.setRequestProperty("Host", "211.70.160.3");
                    connection.setRequestProperty("Origin", "http://211.70.160.3");
                    connection.setRequestProperty("Referer", "http://211.70.160.3/srun_portal_pc.php?url=&ac_id=4");
                    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    connection.setRequestProperty("Content-Length", content_length);
                    connection.setRequestProperty("Charset", "UTF-8");

                    DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                    out.writeBytes(post);
                    out.close();

                    int reponseCode = connection.getResponseCode();
                    Log.e(this.toString(), "run " + reponseCode);
                    if (HttpURLConnection.HTTP_OK == reponseCode) {
                        StringBuffer buffer = new StringBuffer();
                        String line;
                        BufferedReader responseReader = new BufferedReader(
                                new InputStreamReader(connection.getInputStream())
                        );
                        while ((line = responseReader.readLine()) != null) {
                            buffer.append(line);
                        }
                        responseReader.close();
                        System.out.println("服务器返回消息" + buffer.toString());
                        Message message = new Message();
                        Bundle bundle = new Bundle();
                        bundle.putString("string", buffer.toString());
                        message.setData(bundle);
                        if (buffer.toString().matches("login_ok,[\\w\\d,%]+")) {
                            System.out.println("TRUE");
                            message.what = CONTENT_SUCCESS;
                        } else {
                            Log.e(this.toString(), "run " + buffer.toString());
                            System.out.println("FALSE");
                            message.what = CONTENT_FAILD;
                        }
                        handler.sendMessage(message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    /**
     * 检测网络连通情况
     */
    public void testNetworkConnection() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://www.baidu.com");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(3000);
                    BufferedReader reader =
                            new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder builder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }
                    if (builder.toString().contains("211.70.160.3")) {
                        haveConnect = false;
                    } else {
                        haveConnect = true;
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }


    public void showToast(String string) {
        Toast toast = Toast.makeText(getApplicationContext(), string, Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * 在MainAcitivity中获取Service实例
     */
    private class MsgBinder extends Binder {
        public WifiChangeService getService() {
            return WifiChangeService.this;
        }
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CONTENT_SUCCESS:
                    haveConnect = true;
                    isLogin = 0;
                    System.out.println("登陆成功");
                    showToast("登陆成功");
                    break;
                case CONTENT_FAILD:
                    System.out.println("登陆失败");
                    haveConnect = false;
                    isLogin = 1;
                    showToast("登陆失败\n" + msg.getData().getString("string"));
                    System.out.println(msg.getData().getString("string"));
                    break;
            }
        }
    }

    public int isLogin() {
        return isLogin;
    }
}
