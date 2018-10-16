package com.cjh.ffmpeg.videocompress;

public interface GetCommandListener {

    void checkSourceSuccess(String sourceVideoPath, String cmd);

    void errorSourcePath(String sourceVideoPath, String reason);
}
