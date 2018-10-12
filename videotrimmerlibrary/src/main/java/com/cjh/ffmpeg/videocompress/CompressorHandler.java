package com.cjh.ffmpeg.videocompress;

import com.cjh.ffmpeg.FileUtils;
import com.cjh.ffmpeg.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class CompressorHandler {

    public static void getCommand(final String inputVideoPath, final String outputVideoPath, final String start, final String trimmer, final int width, final int height, final GetCommandListener getCommandListener) {
        if (getCommandListener != null) {
            if (inputVideoPath.contains(" ")) {
                Observable.just(inputVideoPath)
                        .subscribeOn(Schedulers.io())
                        .map(new Function<String, String>() {
                            @Override
                            public String apply(String s) {

                                return null;
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<String>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                            }

                            @Override
                            public void onNext(String source) {
                                getCommandListener.checkSourceSuccess(source, getCommand(source, outputVideoPath, start, trimmer, width, height));
                            }

                            @Override
                            public void onError(Throwable e) {
                                getCommandListener.errorSourcePath(inputVideoPath, e.getMessage());
                            }

                            @Override
                            public void onComplete() {
                            }
                        });
            } else {
                getCommandListener.checkSourceSuccess(inputVideoPath, getCommand(inputVideoPath, outputVideoPath, start, trimmer, width, height));
            }
        }
    }

    private static String getCommand(String inputVideoPath, String outputVideoPath, String start, String trimmer, int width, int height) {
        clearTempFile(outputVideoPath);
        int[] endWH = getRatioWH(width, height);
        String cmd = "-y -ss " + start + " -t " + trimmer + " -i " + inputVideoPath + " -strict -2 -vcodec libx264 -preset ultrafast " +
                "-crf 24 -maxrate 2000000 -acodec aac -ar 44100 -ac 2 -b:a 96k -s " + endWH[0] + "x" + endWH[1] + " " + outputVideoPath;
        Log.e(cmd);
        return cmd;
    }

    public static void clearTempFile(String outputVideoPath) {
        boolean createNewFile = FileUtils.createFileByDeleteOldFile(outputVideoPath);
        Log.e("createNewFile " + createNewFile);
    }

    public static int[] getRatioWH(int width, int height) {
        if (width > 640 || height > 1080) {
            double ratioW = width / 640d;
            double ratioH = height / 1080d;
            double endRatio = Math.max(ratioW, ratioH);
            Log.e("ratioW " + ratioW + "   ratioH " + ratioH + "   endRatio " + endRatio);
            return new int[]{(int) Math.ceil(width / endRatio), (int) Math.ceil(height / endRatio)};
        } else return new int[]{width, height};
    }

    public static int getProgress(String message, double taskLength, long start, int preProgress) {
        //   frame=  272 fps= 17 q=32.0 size=    1526kB time=00:00:09.56 bitrate=1306.7kbits/s speed=0.594x
        if (message.contains("frame=") && message.contains("speed=")) {
            Pattern p = Pattern.compile("00:\\d{2}:\\d{2}");
            Matcher m = p.matcher(message);
            if (m.find()) {
                String result = m.group(0);
                float handled = Float.valueOf(result.split(":")[2]) * 1000;
                double progress = handled / taskLength * 100;
                Log.e("result " + result + "  taskLength  " + taskLength + "   start " + start + "   handled " + handled + "   progress " + progress);
                return (int) Math.ceil(progress);
            }
        }
        return preProgress;
    }

}
