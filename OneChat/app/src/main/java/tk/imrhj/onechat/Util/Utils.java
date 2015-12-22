package tk.imrhj.onechat.Util;

import com.avoscloud.leanchatlib.controller.ChatManager;

import java.util.List;

/**
 * Created by rhj on 15/12/22.
 */
public class Utils {
    public static String getConversationUserID(String selfID, List<String> userList) {
        return selfID.equals(userList.get(0)) ? userList.get(1) : userList.get(0);
    }
}
