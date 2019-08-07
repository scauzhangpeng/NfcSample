package com.ppy.nfcsample.card

import java.util.*

/**
 * Created by ZP on 2017/9/21.
 *
 *
 * 城市一卡通卡片实体
 *
 */

open class DefaultCardInfo {

    /**
     * 卡片类型，例如：羊城通，深圳通.
     */
    var type: Int = 0

    /**
     * 卡号.
     */
    var cardNumber: String? = null
        protected set
    /**
     * 卡片版本.
     */
    var version: Int = 0
        protected set
    /**
     * 卡片余额.
     */
    var balance: Long = 0
        protected set
    /**
     * 卡片生效日期.
     */
    var effectiveDate: String? = null
        protected set
    /**
     * 卡片失效日期.
     */
    var expiredDate: String? = null
        protected set
    /**
     * 卡片交易记录.
     */
    var records: MutableList<DefaultCardRecord> = ArrayList(16)
        protected set
}
