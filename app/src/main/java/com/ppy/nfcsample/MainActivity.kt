package com.ppy.nfcsample

import androidx.activity.compose.setContent
import com.ppy.nfcsample.ui.ReadCard

class MainActivity : CommonActivity() {

    override fun setContentUiView() {
        super.setContentUiView()
        setContent {
            ReadCard(isPre.value, mCardInfo.value)
        }
    }
}
