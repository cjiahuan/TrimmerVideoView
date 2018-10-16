package com.cjh.ffmpeg.videocompress;

import android.app.Activity;
import android.content.Context;

import com.cjh.ffmpeg.ExecuteBinaryResponseHandler;
import com.cjh.ffmpeg.FFmpeg;
import com.cjh.ffmpeg.LoadBinaryResponseHandler;
import com.cjh.ffmpeg.Log;
import com.cjh.ffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.cjh.ffmpeg.exceptions.FFmpegNotSupportedException;
import com.cjh.ffmpeg.utils.TrimVideoUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by karan on 13/2/15
 */
public class Compressor {

    public static FFmpeg ffmpeg;

    public static Compressor getInstance(Activity context) {
        if (compressor == null) {
            compressor = new Compressor(context);
        }
        return compressor;
    }

    private boolean isLoadBinary;

    private static Compressor compressor;

    private Compressor(Activity context) {
        ffmpeg = FFmpeg.getInstance(context);
    }

    public void init(InitListener initListener) {
        if (!isLoadBinary) {
            loadBinary(initListener);
        } else {
            Log.e("already load binary success");
            initListener.onLoadSuccess();
        }
    }

    private void loadBinary(final InitListener mListener) {
        try {
            ffmpeg.loadBinary(new LoadBinaryResponseHandler() {
                @Override
                public void onStart() {
                }

                @Override
                public void onFailure() {
                    isLoadBinary = false;
                    mListener.onLoadFail("incompatible with this device");
                }

                @Override
                public void onSuccess() {
                    isLoadBinary = true;
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

    public void execCommand(final String cmd, final float start, final float editTime, final CompressListener mListener) {
        try {
            String[] cmds = cmd.split(" ");
            ffmpeg.execute(cmds, new ExecuteBinaryResponseHandler() {

                @Override
                public void onStart() {
                }

                @Override
                public void onProgress(String message) {
                    int progress = CompressorHandler.getProgress(message, editTime, (long) start);
                    mListener.onExecProgress(cmd, message, progress);
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
}
