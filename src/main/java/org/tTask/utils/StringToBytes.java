package org.tTask.utils;

import java.nio.charset.Charset;

public class StringToBytes {

    public static String getCodeString(String input) {
        String result = "";
        byte[] bytes = input.getBytes(Charset.defaultCharset());
        for (byte b : bytes) {
            result += (b + "-");
        }
        return result.substring(0, result.length() - 1);
    }
}
