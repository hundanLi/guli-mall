package com.hundanli.common.utils;

/**
 * @author li
 * @version 1.0
 * @date 2020-05-22 14:36
 **/
public class Utils {
    public static boolean isNumeric(String str) {
        if (str == null || "".equals(str.trim())) {
            return false;
        }
        int sz = str.length();
        for (int i = 0; i < sz; i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
