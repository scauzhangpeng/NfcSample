package com.ppy.nfcsample;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by ZP on 2017/9/16.
 * <p>
 *     日期相关工具类
 * </p>
 */

public class DateUtil {

    public static final String MMddHHmmss = "MMddHHmmss";
    public static final String MM_dd_HH_mm = "MM-dd HH:mm";

    public static String str2str(String src, String format, String targetFormat) {
        String targetStr = src;
        SimpleDateFormat srcSdf  = new SimpleDateFormat(format, Locale.CHINA);
        SimpleDateFormat targetSdf  = new SimpleDateFormat(targetFormat, Locale.CHINA);
        try {
            targetStr = targetSdf.format(srcSdf.parse(src).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return targetStr;
    }
}
