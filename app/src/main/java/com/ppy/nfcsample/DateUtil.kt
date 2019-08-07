package com.ppy.nfcsample

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by ZP on 2017/9/16.
 *
 *
 * 日期相关工具类
 *
 */

object DateUtil {

    const val MMddHHmmss = "MMddHHmmss"
    const val MM_dd_HH_mm = "MM-dd HH:mm"

    fun str2str(src: String, format: String, targetFormat: String): String {
        var targetStr = src
        val srcSdf = SimpleDateFormat(format, Locale.CHINA)
        val targetSdf = SimpleDateFormat(targetFormat, Locale.CHINA)
        try {
            targetStr = targetSdf.format(srcSdf.parse(src).time)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return targetStr
    }
}
