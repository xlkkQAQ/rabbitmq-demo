package com.xlkk.utils;

/**
 * 沉睡工具类（模拟处理消息的快慢）
 * @author xlkk
 * @date 2022/7/22 0022 14:31
 */
public class SleepUtil {
    public static void sleep(int second){
        try {
            Thread.sleep(second*1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
