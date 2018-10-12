package com.cjh.ffmpeg.utils;

import android.text.TextUtils;

public class TrimVideoUtil {

    public static long coverStringToMillis(String time) {
        // 00:00:00
        if (!TextUtils.isEmpty(time)) {
            String[] split = time.split(":");
            if (split.length == 3) {
                return getHourMillis(split[0]) + getMinuteMillis(split[1]) + getSecondMillis(split[2]);
            } else if (split.length == 2) {
                return getHourMillis(split[0]) + getMinuteMillis(split[1]);
            } else {
                return getHourMillis(split[0]);
            }
        }
        return 0l;
    }

    private static long getHourMillis(String s) {
        try {
            return Integer.parseInt(s) * 60 * 60 * 1000;
        } catch (Exception e) {
            return 0;
        }
    }

    private static long getMinuteMillis(String s) {
        try {
            return Integer.parseInt(s) * 60 * 1000;
        } catch (Exception e) {
            return 0;
        }
    }

    private static long getSecondMillis(String s) {
        try {
            return Integer.parseInt(s) * 1000;
        } catch (Exception e) {
            return 0;
        }
    }

    public static String convertSecondsToTime(long seconds) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (seconds <= 0)
            return "00:00";
        else {
            minute = (int) seconds / 60;
            if (minute < 60) {
                second = (int) seconds % 60;
                timeStr = "00:" + unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = (int) (seconds - hour * 3600 - minute * 60);
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }

    private static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }

}
