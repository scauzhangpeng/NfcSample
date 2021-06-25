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

        val dir_pse = Iso7816(Commands.selectByName(*Commands.DFN_PSE))
        val dir_srv = Iso7816(Commands.selectByName(*Commands.DFN_SRV))
        val dir_srv_info = Iso7816(Commands.readBinary(21))
        val dir_srv_s2 = Iso7816(Commands.selectByName(*Commands.DFN_SRV_S2))
        val dir_srv_s1 = Iso7816(Commands.selectByName(*Commands.DFN_SRV_S1))
        val balance = Iso7816(Commands.getBalance(true))
        val cmds: MutableList<Iso7816> = mutableListOf()
        cmds.add(dir_srv)
        cmds.add(dir_srv_info)
        cmds.add(dir_srv_s2)
        cmds.add(balance)
        //read info
        val cmdResp = executeCommands(nfcCardReaderManager, cmds) ?: return null
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
        cmds.clear()
        for (i in 1..12) {
            val dir_srv_record = Iso7816(Commands.readRecord(24, i), true)
            cmds.add(dir_srv_record)
        }
        for (i in cmds.indices) {
            val iso7816 = cmds[i]
            print("指令:" + Commands.ByteArrayToHexString(iso7816.cmd))
            val resp = nfcCardReaderManager.tranceive(iso7816.cmd)
            iso7816.resp = resp
            println("  响应:" + Commands.ByteArrayToHexString(resp))
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
