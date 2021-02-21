package io.lihongbin.utils;

import java.io.*;
import java.net.URL;
import java.util.UUID;

public class FileUtils {

    public static File getVideoFile(String path) throws IOException {
        int i = Integer.max(path.lastIndexOf("/"), path.lastIndexOf("\\"));
        String fileName = UUID.randomUUID().toString() + path.substring(i + 1);
        if (path.startsWith("/") || path.indexOf(":") > 0) {
            FileUtils.copy(new FileInputStream(path), new FileOutputStream(fileName));
            return new File(fileName);
        }
        URL resource = FileUtils.class.getClassLoader().getResource(path);
        if (null != resource) {
            FileUtils.copy(resource.openStream(), new FileOutputStream(fileName));
            return new File(fileName);
        }
        throw new FileNotFoundException("没有找到文件: " + path);
    }

    public static void copy(InputStream inputStream, OutputStream outputStream) throws IOException {
        try {
            byte[] bytes = new byte[1024 * 64];
            int len;
            while ((len = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
            }
        } finally {
            close(inputStream, outputStream);
        }
    }

    public static void close(Closeable ... closeables) {
        if (null != closeables && closeables.length > 0) {
            for (Closeable closeable : closeables) {
                if (null != closeable) {
                    try {
                        closeable.close();
                    } catch (IOException ignored) { }
                }
            }
        }
    }

}
