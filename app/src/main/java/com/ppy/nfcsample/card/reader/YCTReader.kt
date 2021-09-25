package com.ppy.nfcsample.card.reader

import android.util.Log
import com.ppy.nfclib.NfcManagerCompat
import com.ppy.nfcsample.card.*
import java.io.IOException

/**
 * Created by ZP on 2018/1/4.
 */

class YCTReader : BaseReader(), IReader {

    override val type: Int
        get() = 0

    @Throws(IOException::class)
    override fun readCard(nfcCardReaderManager: NfcManagerCompat): DefaultCardInfo? {
        Log.d(TAG, "readCard: ")
        val cardInfo = YangChengTong()
        cardInfo.type = type

//        val dir_pse = Iso7816(Commands.selectByName(*Commands.DFN_PSE))
        val dirSrv = Iso7816(Commands.selectByName(*Commands.DFN_SRV))
        val dirSrvInfo = Iso7816(Commands.readBinary(21))
        val dirSrvS2 = Iso7816(Commands.selectByName(*Commands.DFN_SRV_S2))
//        val dir_srv_s1 = Iso7816(Commands.selectByName(*Commands.DFN_SRV_S1))
        val balance = Iso7816(Commands.getBalance(true))
        val cmdList: MutableList<Iso7816> = mutableListOf()
        cmdList.add(dirSrv)
        cmdList.add(dirSrvInfo)
        cmdList.add(dirSrvS2)
        cmdList.add(balance)
        //read info
        val cmdResp = executeCommands(nfcCardReaderManager, cmdList) ?: return null
        //parse info
        val parseInfo = cardInfo.parseCardInfo(cmdResp[1].resp)
        if (!parseInfo) {
            return null
        }
        //parse balance
        val parseBalance = cardInfo.parseCardBalance(cmdResp[3].resp)
        if (!parseBalance) {
            return null
        }
        //read record
        cmdList.clear()
        for (i in 1..12) {
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
        private const val TAG = "YCTReader"
    }
}
