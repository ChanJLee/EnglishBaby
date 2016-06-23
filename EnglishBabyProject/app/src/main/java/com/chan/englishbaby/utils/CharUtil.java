package com.chan.englishbaby.utils;

/**
 * Created by chan on 16/6/23.
 */
public class CharUtil {
    public static boolean isAscii(char c) {
        return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z');
    }
}
