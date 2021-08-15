package com.ppy.nfcsample.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ppy.nfclib.util.Util
import com.ppy.nfcsample.DateUtil
import com.ppy.nfcsample.R
import com.ppy.nfcsample.card.DefaultCardInfo
import com.ppy.nfcsample.card.DefaultCardRecord

@Composable
fun ShowCardInfo(defaultCardInfo: DefaultCardInfo) {
    Column (
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()){
        Column(horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .height(60.dp)
                .background(brand)
                .fillMaxWidth()) {
            val cardNumber = if (defaultCardInfo.type == 0) {
                stringResource(R.string.card_number_yc, defaultCardInfo.cardNumber!!)
            } else {
                stringResource(R.string.card_number_sz, defaultCardInfo.cardNumber!!)
            }
            Text(text = cardNumber, fontSize = 18.sp)

            Text(text = stringResource(R.string.card_balance, Util.toAmountString(defaultCardInfo.balance)),
                fontSize = 14.sp)
        }
        LazyColumn(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()) {
            items(defaultCardInfo.records) { record ->
                ItemCardRecord(record)
                Divider()
            }
        }
    }
}

@Composable
fun ItemCardRecord(record: DefaultCardRecord) {
    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(start = 10.dp, end = 10.dp)) {
        Column {
            Text(text = record.typeName.toString())
            Text(text = DateUtil.str2str(record.date!!, DateUtil.MMddHHmmss, DateUtil.MM_dd_HH_mm))
        }
        val price = Util.toAmountString(record.price)
        val priceShow = if ("09" == record.typeCode) {
            "-$price 元"
        } else {
            "+$price 元"
        }
        Spacer(modifier = Modifier.weight(1.0f))
        Text(text = priceShow)
    }
}

//@Preview
//@Composable
//fun PreviewItemCardRecord() {
//    val defaultCardRecord = DefaultCardRecord()
//    defaultCardRecord.price = 900L
//    defaultCardRecord.typeName = "消费"
//    defaultCardRecord.typeCode = "09"
//    ItemCardRecord(record = defaultCardRecord)
//}

@Composable
fun PreReadCard() {
    Column(horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()) {
        Spacer(Modifier.height(30.dp))
        Text(text = "将卡片贴于手机背面", fontSize = 20.sp, color = Color.Black)
        Spacer(modifier = Modifier.height(15.dp))
        Text(text = "移动卡片直到读卡成功", fontSize = 16.sp, color = Color.Gray)
        Spacer(modifier = Modifier.padding(top = 30.dp))
        Image(painter = painterResource(id = R.drawable.nfc_read_card), contentDescription = "")
    }
}

@Composable
fun ReadCard(isPre: Boolean, defaultCardInfo: DefaultCardInfo? = null) {
    if (isPre) {
        PreReadCard()
    } else {
        defaultCardInfo?.let {
            ShowCardInfo(it)
        }
    }
}

@Preview
@Composable
fun PreviewReadCard() {
    MaterialTheme {
        Surface {
            ReadCard(false)
        }
    }
}