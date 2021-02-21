package io.lihongbin.core;

import io.lihongbin.config.Config;
import io.lihongbin.utils.FileUtils;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Core {

    public static List<String> getTextList() throws IOException, FrameGrabber.Exception, InterruptedException {
        // 获取视频
        String videoPath = Config.getVideoPath();
        File file = FileUtils.getVideoFile(videoPath);
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(file);
        grabber.start();
        int frames = grabber.getLengthInFrames();
        Java2DFrameConverter converter = new Java2DFrameConverter();

        // 获取系统分辨率
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int systemWidth = screenSize.width;
        int systemHeight = screenSize.height;
        // 获取一行打印多少个 和 一列打印多少个
        int maxWidth = 236 * systemWidth / 1920;
        int maxHeight = 63 * systemHeight / 1080;

        // 解析图片成文本
        List<String> textList = new ArrayList<>();
        BufferedImage bufferedImage;
        for (int frame = 0; frame < frames; frame++) {
            // 获取图像
            bufferedImage = converter.getBufferedImage(grabber.grabImage());
            // 获取图片分辨率
            int imageWidth = bufferedImage.getWidth();
            int imageHeight = bufferedImage.getHeight();
            // 图片转化文字
            StringBuilder result = new StringBuilder();
            for (int i = 1; i <= maxHeight; i++) {
                for (int j = 1; j <= maxWidth; j++) {

                    // 获取当前位置
                    int currentWidth = j * imageWidth / maxWidth;
                    currentWidth = currentWidth < 0 ? 0 : Math.min(currentWidth, imageWidth - 1);
                    int currentHeight = i * imageHeight / maxHeight;
                    currentHeight = currentHeight < 0 ? 0 : Math.min(currentHeight, imageHeight - 1);

                    // 获取当前位置的像素颜色
                    int rgb = bufferedImage.getRGB(currentWidth, currentHeight);
                    result.append(rgb >= -8388608 ? "8" : " ");

                }
                if (i != maxHeight) {
                    result.append("\n");
                }
            }
            textList.add(result.toString());
        }
        grabber.stop();
        boolean delete = file.delete();
        if (!delete) {
            System.err.println("临时文件删除失败");
        }
        for (int i = 3; i > 0; i--) {
            System.out.println(i);
            Thread.sleep(1000);
        }

        return textList;
    }
}
