package com.trimmerdemo;

import android.util.Log;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * 很好用的Log打印类，自动输出方法和行数
 * Created by jameson on 12/19/15.
 */
public class LogUtils {

    // 上线时为 false 不打印 log; 开发时为 true 打印 log
    public static boolean mLogEnable = true;
    public static final String PREFIX = "LogUtils--";

    private static String mClassname;
    private static ArrayList<String> mMethods;

    static {
        mClassname = LogUtils.class.getName();
        mMethods = new ArrayList<>();

        Method[] ms = LogUtils.class.getDeclaredMethods();
        for (Method m : ms) {
            mMethods.add(m.getName());
        }
    }

    public static void init(boolean logEnable) {
        mLogEnable = logEnable;
    }

    public static void d(String tag, String msg) {
        if (mLogEnable) {
            Log.d(tag, getMsgWithLineNumber(msg));
        }
    }

    public static void e(String tag, String msg) {
        if (mLogEnable) {
            Log.e(tag, getMsgWithLineNumber(msg));
        }
    }

    public static void i(String tag, String msg) {
        if (mLogEnable) {
            Log.i(tag, getMsgWithLineNumber(msg));
        }
    }

    public static void w(String tag, String msg) {
        if (mLogEnable) {
            Log.w(tag, getMsgWithLineNumber(msg));
        }
    }

    public static void v(String tag, String msg) {
        if (mLogEnable) {
            Log.v(tag, getMsgWithLineNumber(msg));
        }
    }


    public static void d(String msg) {
        if (mLogEnable) {
            String[] content = getMsgAndTagWithLineNumber(msg);
            Log.d(content[0], content[1]);
        }
    }

    public static void e(String msg) {
        if (mLogEnable) {
            String[] content = getMsgAndTagWithLineNumber(msg);
            Log.e(content[0], content[1]);
        }
    }

    public static void i(String msg) {
        if (mLogEnable) {
            String[] content = getMsgAndTagWithLineNumber(msg);
            Log.i(content[0], content[1]);
        }
    }

    public static void i() {
        if (mLogEnable) {
            String[] content = getMsgAndTagWithLineNumber("");
            Log.i(content[0], content[1]);
        }
    }

    public static void w(String msg) {
        if (mLogEnable) {
            String[] content = getMsgAndTagWithLineNumber(msg);
            Log.w(content[0], content[1]);
        }
    }

    public static void v(String msg) {
        if (mLogEnable) {
            String[] content = getMsgAndTagWithLineNumber(msg);
            Log.v(content[0], content[1]);
        }
    }

    public static String getMsgWithLineNumber(String msg) {
        try {
            for (StackTraceElement st : (new Throwable()).getStackTrace()) {
                if (mClassname.equals(st.getClassName()) || mMethods.contains(st.getMethodName())) {
                    continue;
                } else {
                    int b = st.getClassName().lastIndexOf(".") + 1;
                    String message = new StringBuilder(st.getClassName().substring(b)).append("->").append(st.getMethodName())
                            .append("():").append(st.getLineNumber()).append(msg).toString();
                    return message;
                }

            }
        } catch (Exception e) {

        }
        return msg;
    }

    public static String[] getMsgAndTagWithLineNumber(String msg) {
        try {
            for (StackTraceElement st : (new Throwable()).getStackTrace()) {
                if (mClassname.equals(st.getClassName()) || mMethods.contains(st.getMethodName())) {
                    continue;
                } else {
                    int b = st.getClassName().lastIndexOf(".") + 1;
                    String TAG = PREFIX + st.getClassName().substring(b);
                    String message = st.getMethodName() + "():" + st.getLineNumber() + "->" + msg;
                    String[] content = new String[]{TAG, message};
                    return content;
                }

            }
        } catch (Exception e) {

        }
        return new String[]{"universal tag", msg};
    }


}