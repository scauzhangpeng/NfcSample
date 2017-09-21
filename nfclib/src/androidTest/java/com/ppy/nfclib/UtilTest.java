package com.ppy.peng.nfclib;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by ZP on 2017/9/20.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class UtilTest {

    @Rule
    public ActivityTestRule<AndroidTestActivity> mActivityTestRule = new ActivityTestRule<AndroidTestActivity>(AndroidTestActivity.class);

    @Test
    public void isNfcExits() throws Exception {
        boolean nfcExits = Util.isNfcExits(mActivityTestRule.getActivity());
        Assert.assertTrue(nfcExits);
    }

    @Test
    public void isNfcEnable() throws Exception {
        boolean nfcEnable = Util.isNfcEnable(mActivityTestRule.getActivity());
        Assert.assertTrue(nfcEnable);
    }

}