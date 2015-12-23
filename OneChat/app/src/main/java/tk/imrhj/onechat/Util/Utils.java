package tk.imrhj.onechat.Util;

import android.annotation.SuppressLint;
import android.util.Log;

import com.avoscloud.leanchatlib.controller.ChatManager;

import java.text.SimpleDateFormat;
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

    @SuppressLint("SimpleDateFormat")
    public static String fromatTime(Date date) {
        Calendar messageCalendar = new GregorianCalendar();
        messageCalendar.setTime(date);
        Calendar nowCalendar = new GregorianCalendar();
        nowCalendar.setTime(new Date(System.currentTimeMillis()));




        int dayOfYear = messageCalendar.get(Calendar.DAY_OF_YEAR);

        if (nowCalendar.get(Calendar.DAY_OF_YEAR) == dayOfYear) {
            SimpleDateFormat fmt = new SimpleDateFormat("HH:mm");
            return fmt.format(messageCalendar.getTime());


        } else if (nowCalendar.get(Calendar.DAY_OF_YEAR) - dayOfYear == 1) {
            return "昨天";
        } else {
            return messageCalendar.get(Calendar.YEAR) + "/"
                    + messageCalendar.get(Calendar.MONTH) + "/"
                    + messageCalendar.get(Calendar.DAY_OF_MONTH);
        }
    }
}
