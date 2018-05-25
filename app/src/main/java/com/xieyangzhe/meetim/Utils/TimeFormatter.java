package com.xieyangzhe.meetim.Utils;

import com.github.bassaer.chatmessageview.util.ITimeFormatter;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

/**
 * Created by joseph on 5/25/18.
 */

public class TimeFormatter implements ITimeFormatter {

    @NotNull
    @Override
    public String getFormattedTimeText(Calendar calendar) {
        Calendar now = Calendar.getInstance();

        long timeDiff = (now.getTimeInMillis() - calendar.getTimeInMillis()) / 1000;

        if (timeDiff < 3) {
            return "just now";
        } else if (timeDiff < 60) {
            return timeDiff + " second" + getPlural(timeDiff) + " ago";
        }

        long min = timeDiff / 60;
        if (min < 60) {
            return min + " minute" + getPlural(min) + " ago";
        }

        long hour = min / 60;
        if (hour < 24) {
            return hour + " hour" + getPlural(hour) + " ago";
        }

        long day = hour / 24;
        return day + " day" + getPlural(day) + " ago";
    }

    private String getPlural(long time) {
        return time > 1 ? "s" : "";
    }
}
