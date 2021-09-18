package com.ppy.nfcsample.card.reader

import android.util.Log
import com.ppy.nfclib.NfcManagerCompat
import com.ppy.nfcsample.card.*
import java.io.IOException
import java.util.*

/**
 * Created by ZP on 2018/1/4.
 */

class SZTReader : BaseReader(), IReader {

    override val type: Int
        get() = 1

    @Throws(IOException::class)
    override fun readCard(nfcCardReaderManager: NfcManagerCompat): DefaultCardInfo? {
        Log.d(TAG, "readCard: ")
        val cardInfo = ShenZhenTong()
        cardInfo.type = type

        val dir1001 = Iso7816(Commands.select1001())
        val dirSrvInfo = Iso7816(Commands.readBinary(21))
        val balance = Iso7816(Commands.getBalance(true))
        val cmdList: MutableList<Iso7816> = ArrayList()
        cmdList.add(dir1001)
        cmdList.add(dirSrvInfo)
        cmdList.add(balance)

        val cmdResp = executeCommands(nfcCardReaderManager, cmdList) ?: return null
        if (cmdResp.size < 3) {
            return null
        }

        val parseCardInfoSuccess = cardInfo.parseCardInfo(cmdResp[1].resp)
        if (!parseCardInfoSuccess) {
            return null
        }
        val parseBalanceSuccess = cardInfo.parseCardBalance(cmdResp[2].resp)
        if (!parseBalanceSuccess) {
            return null
        }
        //read record
        cmdList.clear()
        for (i in 1..10) {
            val dirSrvRecord = Iso7816(Commands.readRecord(24, i), true)
            cmdList.add(dirSrvRecord)
        }
        for (i in cmdList.indices) {
            val iso7816 = cmdList[i]
            print("指令:" + Commands.byteArrayToHexString(iso7816.cmd))
            val resp = nfcCardReaderManager.tranceive(iso7816.cmd)
            iso7816.resp = resp
            println("  响应:" + Commands.byteArrayToHexString(resp))
            if (!isSuccess(resp) && !iso7816.isContinue) {
                return null
            }
            val record = DefaultCardRecord()
            try {
                record.readRecord(resp)
                cardInfo.records.add(record)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        return cardInfo
    }

    companion object {
        private const val TAG = "SZTReader"
    }
}
