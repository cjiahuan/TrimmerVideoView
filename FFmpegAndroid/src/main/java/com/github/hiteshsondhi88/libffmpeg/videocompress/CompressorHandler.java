package com.github.hiteshsondhi88.libffmpeg.videocompress;

import com.github.hiteshsondhi88.libffmpeg.FileUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CompressorHandler {


    public static String getCommand(String inputVideoPath, String outputVideoPath,String start , String trimmer,int width, int height)throws IllegalArgumentException  {
        if(inputVideoPath.contains(" ")){
            throw new IllegalArgumentException("inputVideoPath can not have space characters");
        }
        clearTempFile(outputVideoPath);
        int[] endWH = getRatioWH(width, height);
        String cmd = "-y -ss " + start + " -t " + trimmer + " -i " + inputVideoPath + " -strict -2 -vcodec libx264 -preset ultrafast " +
                "-crf 24 -maxrate 2000000 -acodec aac -ar 44100 -ac 2 -b:a 96k -s " + endWH[1] + "x" + endWH[0] + " " + outputVideoPath;
        com.github.hiteshsondhi88.libffmpeg.Log.e(cmd);
        return cmd;
    }

    public static void clearTempFile(String outputVideoPath) {
        boolean createNewFile = FileUtils.createFileByDeleteOldFile(outputVideoPath);
        com.github.hiteshsondhi88.libffmpeg.Log.e("createNewFile " + createNewFile);
    }

    public static int[] getRatioWH(int width, int height) {
        if (width > 640 || height > 1080) {
            double ratioW = width / 640d;
            double ratioH = height / 1080d;
            double endRatio = Math.max(ratioW, ratioH);
            com.github.hiteshsondhi88.libffmpeg.Log.e("ratioW " + ratioW + "   ratioH " + ratioH + "   endRatio " + endRatio);
            return new int[]{(int) Math.ceil(width / endRatio), (int)Math.ceil(height / endRatio)};
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
                com.github.hiteshsondhi88.libffmpeg.Log.e("result " + result + "  taskLength  " + taskLength + "   start " + start + "   handled " + handled + "   progress " + progress);
                return (int) Math.ceil(progress);
            }
        }
        return preProgress;
    }

}
