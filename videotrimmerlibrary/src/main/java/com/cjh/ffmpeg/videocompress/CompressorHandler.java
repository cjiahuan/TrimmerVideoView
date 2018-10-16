package com.cjh.ffmpeg.videocompress;

import android.app.Activity;
import android.text.TextUtils;

import com.cjh.ffmpeg.FileUtils;
import com.cjh.ffmpeg.Log;
import com.cjh.ffmpeg.utils.TrimVideoUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class CompressorHandler {

    private static void getCommand(final Activity activity,
                                   final String inputVideoPath,
                                   final String outputVideoPath,
                                   final long startPos,
                                   final long endPos,
                                   final int width,
                                   final int height,
                                   final GetCommandListener getCommandListener) {
        if (getCommandListener != null) {
            final String start = TrimVideoUtil.convertSecondsToTime(startPos / 1000);
            final String editTime = TrimVideoUtil.convertSecondsToTime((long) Math.ceil((endPos - startPos) / 1000));
            if (inputVideoPath.contains(" ")) {
                Observable.just(inputVideoPath)
                        .subscribeOn(Schedulers.io())
                        .map(new Function<String, String>() {
                            @Override
                            public String apply(String s) {
                                String newInputVideoPath = "/data/data/" + activity.getPackageName() + "/" + System.currentTimeMillis() + ".mp4";
                                boolean isSuccess = FileUtils.copyFile(inputVideoPath, newInputVideoPath);
                                if (isSuccess) {
                                    return newInputVideoPath;
                                }
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
                                if (TextUtils.isEmpty(source)) {
                                    getCommandListener.errorSourcePath(inputVideoPath, "inputVideoPath have space character,and copyFile logic fail");
                                } else {
                                    getCommandListener.checkSourceSuccess(inputVideoPath, getCommand(source, outputVideoPath, start, editTime, width, height));
                                }
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
                getCommandListener.checkSourceSuccess(inputVideoPath, getCommand(inputVideoPath, outputVideoPath, start, editTime, width, height));
            }
        } else {
            Log.e("getCommandListener can not be null!!! ");
        }
    }

    public static void excute(boolean isOpenDebugLog,
                              final Activity activity,
                              final String inputVideoPath,
                              final String outputVideoPath,
                              final long startPos,
                              final long endPos,
                              final int width,
                              final int height,
                              final CompressListener compressListener) throws IllegalArgumentException {
        Log.setDEBUG(isOpenDebugLog);
        if (compressListener == null) {
            throw new IllegalArgumentException("compressListener can not be null");
        }

        if (activity == null) {
            compressListener.onExecFail("activity can not be null");
            return;
        }

        if (TextUtils.isEmpty(inputVideoPath)) {
            compressListener.onExecFail("inputVideoPath can not be empty");
            return;
        }

        if (TextUtils.isEmpty(outputVideoPath)) {
            compressListener.onExecFail("outputVideoPath can not be empty");
            return;
        }

        if (startPos < 0) {
            compressListener.onExecFail("editTime is wrong: startPos = " + startPos + ", must >= 0");
            return;
        }

        if (startPos >= endPos) {
            compressListener.onExecFail("editTime is wrong: startPos = " + startPos + ", endPos = " + endPos + ", endPos must > startPos");
            return;
        }

        if (height <= 0) {
            compressListener.onExecFail("height is wrong: height = " + height + ", must > 0");
            return;
        }

        if (width <= 0) {
            compressListener.onExecFail("width is wrong: width = " + width + ", must > 0");
            return;
        }

        getCommand(activity, inputVideoPath, outputVideoPath, startPos, endPos, width, height, new GetCommandListener() {
            @Override
            public void checkSourceSuccess(String sourceVideoPath, final String cmd) {
                final Compressor compressor = Compressor.getInstance(activity);
                compressor.init(new InitListener() {
                    @Override
                    public void onLoadSuccess() {
                        Log.e("compressor loadbinary success");
                        compressor.execCommand(cmd, startPos, endPos - startPos, compressListener);
                    }

                    @Override
                    public void onLoadFail(String reason) {
                        Log.e("compressor loadbinary fail");
                    }
                });

            }

            @Override
            public void errorSourcePath(String sourceVideoPath, String reason) {
                compressListener.onExecFail("sourceVideoPath = " + sourceVideoPath + " -> " + reason);
            }
        });
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

    public static int getProgress(String message, float taskLength, long start) {
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
        return -1;
    }

}
