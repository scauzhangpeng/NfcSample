package com.ppy.nfclib;

import android.app.Activity;
import android.os.Build;

/**
 * Created by ZP on 2017/9/20.
 * <p>
 *     读卡器模式工厂类，根据系统版本生产相应CardReader
 * </p>
 */

public class CardReaderFactory {

    public static CardReader productCardReader(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return new KikKatCardReader(activity);
        } else {
            return new JellyBeanCardReader(activity);
        }
    }
}
