package com.cjh.ffmpeg;

abstract interface ResponseHandler {

    /**
     * on Start
     */
    public void onStart();

    /**
     * on Finish
     */
    public void onFinish();

}
