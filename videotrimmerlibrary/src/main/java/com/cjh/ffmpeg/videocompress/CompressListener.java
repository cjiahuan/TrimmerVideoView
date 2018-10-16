package com.cjh.ffmpeg.videocompress;

/**
 * Created by karan on 13/2/15.
 */
public abstract class CompressListener{

    public abstract void onExecSuccess(String message);
    public abstract void onExecFail(String reason);
    public abstract void onExecProgress(String cmd, String orginalMessage, int progress);

}
