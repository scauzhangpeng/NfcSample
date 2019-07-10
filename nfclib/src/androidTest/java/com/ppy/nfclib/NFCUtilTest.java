package com.ppy.nfclib;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by ZP on 2017/9/20.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class NFCUtilTest {

    @Test
    public void isNfcExits() throws Exception {
        Context context = InstrumentationRegistry.getTargetContext();
        boolean nfcExits = Util.isNfcExits(context);
        Assert.assertTrue(nfcExits);
    }

    @Test
    public void isNfcEnable() throws Exception {
        Context context = InstrumentationRegistry.getTargetContext();
        boolean nfcEnable = Util.isNfcEnable(context);
        Assert.assertTrue(nfcEnable);
    }

    @Test
    public void isAndroidBeamEnable() throws Exception {
        Context context = InstrumentationRegistry.getTargetContext();
        boolean androidBeamEnable = Util.isAndroidBeamEnable(context);
        Assert.assertTrue(androidBeamEnable);
    }

}