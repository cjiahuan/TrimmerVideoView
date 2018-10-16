# TrimmerVideoView

Visiting WeChat trimmer video view. For now, this is a beta version 0.0.X.


### ScreenShots

<img src="https://github.com/cjhandroid/TrimmerVideoView/blob/master/ezgif.com-video-to-gif.gif" width="30%" />

### v0.1.0
provide TrimmerVideoView and compressor video functions
```
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```

```
dependencies {
   implementation 'com.github.cjiahuan:TrimmerVideoView:0.1.0'
}
```
you can use TrimmerVideoView like v0.0.5, and compressor video :
```
 @SuppressLint("SetTextI18n")
    private fun precompressor() {
        val trimmerPosArray = mVideoTrimmerView.getTrimmerPos()
        trimmerPos.text = trimmerPosArray[0].toString() + " ::::::: " + trimmerPosArray[1].toString()
        compressor(trimmerPosArray[0], trimmerPosArray[1])
    }

    private fun compressor(start: Long, endPos: Long) {
        MOUTPUTVIDEOPATH = VIDEOCOMPRESSORDIR + "/" + System.currentTimeMillis() + ".mp4"
        val configVo = mVideoTrimmerView.getConfigVo()

        val lastTime = System.currentTimeMillis()

        CompressorHandler.excute(true,
                this,
                configVo.videoPath,
                MOUTPUTVIDEOPATH,
                start,
                endPos,
                configVo.width,
                configVo.height,
                object : CompressListener() {
                    @SuppressLint("SetTextI18n")
                    override fun onExecSuccess(message: String?) {
                        LogUtils.e("MOUTPUTVIDEOPATH -> $MOUTPUTVIDEOPATH ||| size -> ${FileUtils.getFileSize(MOUTPUTVIDEOPATH)}")
                        msg.text = "${msg.text} + \n\n" + "MOUTPUTVIDEOPATH -> $MOUTPUTVIDEOPATH ||| size -> ${FileUtils.getFileSize(MOUTPUTVIDEOPATH)}"
                    }

                    override fun onExecFail(reason: String?) {
                    }

                    override fun onExecProgress(cmd: String, orginalMessage: String, progress: Int) {
                        msg.visibility = View.VISIBLE
                        LogUtils.e("onExecProgress -> $progress")
                        val sb = StringBuilder("cmd -> $cmd")
                                .append("\n\n")
                                .append("progress -> $progress")
                                .append("\n\n")
                                .append(orginalMessage)
                                .append("\n\n")
                                .append("time -> ${(System.currentTimeMillis() - lastTime) / 1000f}")
                                .toString()
                        msg.text = sb
                    }

                })

    }
```

### v0.0.5

Only to provide the control of the video, but did not achieve its cut, compression and other functions, which will be achieved in the next update one by one.

### How to use

#### gradle

```
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```

```
dependencies {
	        implementation 'com.github.cjiahuan:TrimmerVideoView:0.0.5'
}
```
  
  ### details
  
  #### XML
 ```
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">

    ...

    <com.cjh.videotrimmerlibrary.VideoTrimmerView
        android:id="@+id/mVideoTrimmerView"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />

</LinearLayout>

```

#### set video path
```
mVideoTrimmerView?.setVideoPath(videoPath!!)?.handle()
```

#### get trimmer time: start position ::: end position
```
mVideoTrimmerView.getTrimmerPos()[0].toString() + " ::::::: " + mVideoTrimmerView.getTrimmerPos()[1].toString()
```

#### release: if activity finish you should call release()
```
override fun finish() {
        super.finish()
        mVideoTrimmerView.release()
    }
```

### Config

TrimmerVideoView provides a set of default control configuration, you can not set the configuration, of course, you can also set their own desired configuration, only need to integrate IConfig (Interface) or you want to change a part of the configuration, then you can inherit DefaultConfig (class) , It should be noted that this configuration needs to be configured before setVideoPath (), otherwise some of the configuration will not take effect.


|参数名|参数含义|
|--- |:--- |
|getTrimmerTime() |the maximum video cut length, default config is 10 * 1000ms|
|getVisiableThumbCount() |recyclerView visiable items, default config is 10 |
|getThumbListUpdateCount() | whenever get i(updatecount) frames, notify recyclerView|
|getTrimmerSeekBarShaowColor() | custom seek bar shaow color|
|getTrimmerSeekBarTrimmerStrokeColor() | custom seek bar stroke color|
|getTrimmerSeekBarTrimmerStrokeWidth() | custom seek bar stroke width|
|getTrimmerOffsetValue() | this attr help move trimmerview cursor to move, you can ignore this attr|
|getMinTrimmerThumbCount() | min trimmer thumb count, if your trimmertime is 10, visiable thumbcount is 10,your min trimmertime is 3,you can set this attr 3 |
|isShowTrimmerTextViews() | is show trimmer textViews|


#### config attrs



#### if you want set your configuration

Complete custom configuration

```
class CustomConfig : IConfig {
    override fun isShowTrimmerTextViews(): Boolean = false

    override fun getMinTrimmerThumbCount(): Int = 3

    override fun getTrimmerOffsetValue(): Int = 5

    override fun getTrimmerTime(): Long = 20 * 1000L

    override fun getVisiableThumbCount(): Int = 10

    override fun getThumbListUpdateCount(): Int = 5

    override fun getTrimmerSeekBarShaowColor(): String = "#99ff0000"

    override fun getTrimmerSeekBarTrimmerStrokeColor(): String = "#ff0000"

    override fun getTrimmerSeekBarTrimmerStrokeWidth(): Int = 5
}
```

just want to change some attrs

```
class CustomDefaultConfig : DefaultConfig() {

    override fun getMinTrimmerThumbCount(): Int = 3

}
```

### at last 
this library is preview version, still have few bugs, I would fix bugs every day.But these bugs can not lead to crash.

### Issue: videoview memory leak
<a href="https://github.com/JakeWharton/butterknife/issues/585" target="blank">issues</a>

<a href="https://medium.com/@chauyan/confirmed-videoview-leak-on-android-ac502856a6cf" target="blank">Confirmed VideoView Leak on Android</a>

<a href="http://blog.csdn.net/acerhphp/article/details/62889468" target="blank">关于Android VideoView导致的内存泄漏的问题</a>




  
