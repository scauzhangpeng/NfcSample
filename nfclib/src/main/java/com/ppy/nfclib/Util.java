package com.ppy.nfclib;

import android.content.Context;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.provider.Settings;

import java.util.Locale;

/**
 * Created by ZP on 2017/9/20.
 * NFC相关工具类
 */

public class Util {

    /**
     * Utility class to convert a byte array to a hexadecimal string.
     *
     * @param bytes Bytes to convert
     * @return String, containing hexadecimal representation.
     */
    public static String ByteArrayToHexString(byte[] bytes) {
        final char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for ( int j = 0; j < bytes.length; j++ ) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    /**
     * Utility class to convert a hexadecimal string to a byte string.
     *
     * <p>Behavior with input strings containing non-hexadecimal characters is undefined.
     *
     * @param s String containing hexadecimal characters to convert
     * @return Byte array generated from input
     */
    public static byte[] HexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    /**
     * 金额单位转换，分转元
     *
     * @param value 金额，单位分
     * @return 金额， 单位元
     */
    public static String toAmountString(long value) {
        return String.format(Locale.CHINA, "%.2f", value / 100.0f);
    }

    /**
     * 16进制转10进制
     *
     * @param hex 16进制
     * @param startIndex 起始下标
     * @param len 长度, 按字符算
     * @return 10进制
     */
    public static int hexToInt(byte[] hex, int startIndex, int len) {
        int ret = 0;

        final int e = startIndex + len;
        for (int i = startIndex; i < e; ++i) {
            ret <<= 8;
            ret |= hex[i] & 0xFF;
        }
        return ret;
    }
    /**
     * 16进制转10进制 (小端模式)
     *
     * @param b 16进制
     * @param s 起始下标
     * @param n 长度, 按字符算
     * @return 10进制
     */
    public static int hexToIntLittleEndian(byte[] b, int s, int n) {
        int ret = 0;
        for (int i = s; (i >= 0 && n > 0); --i, --n) {
            ret <<= 8;
            ret |= b[i] & 0xFF;
        }
        return ret;
    }


    /**
     * 判断手机是否具备NFC功能
     *
     * @param context {@link Context}
     * @return {@code true}: 具备 {@code false}: 不具备
     */
    public static boolean isNfcExits(Context context) {
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(context);
        return nfcAdapter != null;
    }

    /**
     * 判断手机NFC是否开启
     * <p>
     *     OPPO A37m 发现必须同时开启NFC以及Android Beam才可以使用
     *     20180108 发现OPPO单独打开NFC即可读取标签，不清楚是否是系统更新
     * </p>
     *
     * @param context {@link Context}
     * @return {@code true}: 已开启 {@code false}: 未开启
     */
    public static boolean isNfcEnable(Context context) {
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(context);
//        if (Build.MANUFACTURER.toUpperCase().contains("OPPO")) {
//            return nfcAdapter.isEnabled() && isAndroidBeamEnable(context);
//        }
        return nfcAdapter != null && nfcAdapter.isEnabled();
    }

    /**
     * 判断手机NFC的Android Beam是否开启，在API 16之后才有
     *
     * @param context {@link Context}
     * @return {@code true}: 已开启 {@code false}: 未开启
     */
    public static boolean isAndroidBeamEnable(Context context) {
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(context);
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && nfcAdapter != null && nfcAdapter.isNdefPushEnabled();
    }

    /**
     * 判断手机是否具备Android Beam
     *
     * @param context {@link Context}
     * @return {@code true}:具备 {@code false}:不具备
     */
    public static boolean isAndroidBeamExits(Context context) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && isNfcExits(context);
    }

    /**
     * 跳转至系统NFC设置界面，不准确
     *
     * @param context {@link Context}
     */
    public static void intentToNfcSetting(Context context) {
        if (isNfcExits(context)) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                Intent intent = new Intent(Settings.ACTION_NFC_SETTINGS);
                context.startActivity(intent);
            }
        }

    }

    /**
     * 跳转至系统NFC Android Beam设置界面，同页面基本都有NFC开关，建议采用
     *
     * @param context {@link Context}
     */
    public static void intentToNfcShare(Context context) {
        if (isAndroidBeamExits(context)) {
            Intent intent = new Intent(Settings.ACTION_NFCSHARING_SETTINGS);
            context.startActivity(intent);
        }
    }

}
