package com.chowis.cdb.skin.dialog

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.chowis.cdb.skin.R

class AppDialog(context: Context, private val message: String) : BaseDialog(context) {
    override val layoutId: Int = R.layout.layout_alert_dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val btnOk = findViewById<Button>(R.id.btn_ok)
        btnOk.setOnClickListener {
            dismiss()
        }
        findViewById<TextView>(R.id.tv_message).text = message
    }
}