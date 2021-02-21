package io.lihongbin.config;

import io.lihongbin.Main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public class Config {

    private final static String config_path  = "-config=";
    private final static String video_path  = "video.path";
    private final static String DEFAULT_VIDEO_PATH  = "bad apple.mp4";
    private final static String DEFAULT_CONFIG_PATH  = "config.properties";

    public static String getVideoPath() throws IOException {
        Properties properties = new Properties();
        boolean read = false;
        // 读取自定义配置文件
        for (String arg : Main.args) {
            if (null != arg && arg.startsWith(Config.config_path) && arg.length() > Config.config_path.length()) {
                String configPath = arg.substring(Config.config_path.length());
                File file = new File(configPath);
                if (!file.exists()) {
                    throw new RuntimeException("没有获取到配置文件: " + file.getCanonicalPath());
                }
                properties.load(new FileInputStream(file));
                read = true;
            }
        }
        // 读取默认配置文件
        if (!read) {
            URL resource = Config.class.getClassLoader().getResource(Config.DEFAULT_CONFIG_PATH);
            assert resource != null;
            properties.load(resource.openStream());
        }

        String result = Config.DEFAULT_VIDEO_PATH;
        boolean change = false;
        String configVideoPath = properties.getProperty(Config.video_path);
        if (null != configVideoPath && !configVideoPath.isEmpty()) {
            result = configVideoPath;
            change = true;
        }
        if (!change) {
            System.err.println("没有获取到配置文件, 采用默认配置");
        }
        return result;
    }

}
