package com.cjh.ffmpeg.videocompress;

import android.app.Activity;

import com.cjh.ffmpeg.ExecuteBinaryResponseHandler;
import com.cjh.ffmpeg.FFmpeg;
import com.cjh.ffmpeg.LoadBinaryResponseHandler;
import com.cjh.ffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.cjh.ffmpeg.exceptions.FFmpegNotSupportedException;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by karan on 13/2/15
 */
public class Compressor {

    public Activity a;
    public FFmpeg ffmpeg;

    public Compressor(Activity activity) {
        a = activity;
        ffmpeg = FFmpeg.getInstance(a);
        loadBinary(new InitListener() {
            @Override
            public void onLoadSuccess() {

            }

            @Override
            public void onLoadFail(String reason) {

            }
        });
    }

    public void loadBinary(final InitListener mListener) {
        try {
            ffmpeg.loadBinary(new LoadBinaryResponseHandler() {
                @Override
                public void onStart() {
                }

                @Override
                public void onFailure() {
                    mListener.onLoadFail("incompatible with this device");
                }

                @Override
                public void onSuccess() {
                    mListener.onLoadSuccess();
                }

                @Override
                public void onFinish() {

                }
            });
        } catch (FFmpegNotSupportedException e) {
            e.printStackTrace();
        }
    }

    public void execCommand(String cmd, final CompressListener mListener) {
        try {
            String[] cmds = cmd.split(" ");
            ffmpeg.execute(cmds, new ExecuteBinaryResponseHandler() {

                @Override
                public void onStart() {
                }

                @Override
                public void onProgress(String message) {
                    mListener.onExecProgress(message);
                }

                @Override
                public void onFailure(String message) {
                    mListener.onExecFail(message);
                }

                @Override
                public void onSuccess(String message) {
                    mListener.onExecSuccess(message);
                }

                @Override
                public void onFinish() {
                }
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
            e.printStackTrace();
        }
    }

    public String[] getSpecialCommand(String cmd) {
        String[] cmds = new String[]{};
        List<String> result = new ArrayList<String>();
        String tmp = "";
        boolean inMode = false;
        for (int i = 0; i < cmd.length(); i++) {
            if ((cmd.charAt(i) == ' ' && !inMode) || cmd.charAt(i) == '"') {
                if (tmp.trim().length() > 0) {
                    result.add(tmp);
                }
                tmp = "";
            }


            if (cmd.charAt(i) == '"') {
                inMode = !inMode;
            } else {
                tmp += cmd.charAt(i);
            }

        }
        return result.toArray(cmds);
    }
}
