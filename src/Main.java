import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Main {

    // TODO 修改这个文件夹的路径
    private final static String IMAGES_DIRECTORY = "D:\\java\\code\\hongbin-bad-apple\\images";

    public static void main(String[] args) throws IOException, InterruptedException {

        // 获取所有图片文件
        File directory = new File(IMAGES_DIRECTORY);
        if (!directory.exists()) {
            throw new RuntimeException("图片保存的文件夹路径不存在: " + directory.getCanonicalPath());
        }
        if (!directory.isDirectory()) {
            throw new RuntimeException(directory.getCanonicalPath() + " 非文件夹路径");
        }
        File[] files = directory.listFiles();
        if (null == files || files.length == 0) {
            throw new RuntimeException("没有获取到图片文件");
        }
        List<File> fileList =  Arrays.asList(files);

        // 文件排序
        fileList.sort(Comparator.comparing(File::getName));

        // 文件转图片
        List<BufferedImage> imageList = new ArrayList<>();
        for (File file : fileList) {
            imageList.add(ImageIO.read(file));
        }

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
        for (BufferedImage bufferedImage : imageList) {
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

        // 打印文本
        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        for (int i = 0; i < textList.size(); i++) {
            // 打印
            System.out.print(textList.get(i));

            // 睡眠
            try {
                Thread.sleep(24);
            } catch (InterruptedException e) {
                System.out.println("\n打印线程被停止");
                e.printStackTrace();
                return;
            }

            // 清屏
            if (i != textList.size() - 1) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            }
        }

        System.out.println("\n打印完成");

    }

}
