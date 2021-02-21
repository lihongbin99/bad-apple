package io.lihongbin;

import io.lihongbin.core.Console;
import io.lihongbin.core.Core;

import java.util.List;

public class Main {

    public static String[] args;

    public static void main(String[] args) throws Exception {
        System.out.println("开始准备, 请等待~");
        Main.args = args;
        List<String> textList = Core.getTextList();
        System.gc();
        Console.plain(textList);
    }

}
