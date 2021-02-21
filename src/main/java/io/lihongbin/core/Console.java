package io.lihongbin.core;

import java.io.IOException;
import java.util.List;

public class Console {

    private final static ProcessBuilder processBuilder = new ProcessBuilder("cmd", "/c", "cls").inheritIO();

    public static void plain(List<String> textList) throws IOException, InterruptedException {
        Console.processBuilder.start().waitFor();
        for (String s : textList) {
            // 打印
            System.out.print(s);

            // 睡眠
            try {
                Thread.sleep(24);
            } catch (InterruptedException e) {
                System.out.println("\n打印线程被停止");
                e.printStackTrace();
                return;
            }

            // 清屏
            Console.processBuilder.start().waitFor();
        }

        System.out.println("\n打印完成");
    }

}
