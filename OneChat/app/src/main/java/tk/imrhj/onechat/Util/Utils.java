package tk.imrhj.onechat.Util;

import android.util.Log;

import com.avoscloud.leanchatlib.controller.ChatManager;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by rhj on 15/12/22.
 */
public class Utils {
    private static final String TAG = "Utils";

    public static String getConversationUserID(String selfID, List<String> userList) {
        return selfID.equals(userList.get(0)) ? userList.get(1) : userList.get(0);
    }

    public static String fromatTime(Date date) {
        Calendar messageCalendar = new GregorianCalendar();
        messageCalendar.setTime(date);
        Calendar nowCalendar = new GregorianCalendar();
        nowCalendar.setTime(new Date(System.currentTimeMillis()));


        if (nowCalendar.get(Calendar.DAY_OF_YEAR) == messageCalendar.get(Calendar.DAY_OF_YEAR)) {
            return messageCalendar.get(Calendar.HOUR) + ":" + messageCalendar.get(Calendar.MINUTE);
        } else if (nowCalendar.get(Calendar.DAY_OF_YEAR)
                - messageCalendar.get(Calendar.DAY_OF_YEAR) == 1) {
            return "昨天";
        } else {
            return messageCalendar.get(Calendar.YEAR) + "/"
                    + messageCalendar.get(Calendar.MONTH) + "/"
                    + messageCalendar.get(Calendar.DAY_OF_MONTH);
        }
    }
}
