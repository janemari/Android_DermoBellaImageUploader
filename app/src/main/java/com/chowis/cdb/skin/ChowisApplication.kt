package com.chowis.cdb.skin

import android.app.Application
import android.content.Context
import android.widget.Toast
import com.chibatching.kotpref.Kotpref
import org.acra.ACRA
import org.acra.annotation.AcraToast
import org.acra.config.CoreConfigurationBuilder
import org.acra.config.MailSenderConfigurationBuilder
import org.acra.data.StringFormat
import timber.log.Timber

@AcraToast(resText = R.string.application_error, length = Toast.LENGTH_LONG)
class ChowisApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        instance = this
        Kotpref.init(this)

        if (BuildConfig.DEBUG) {
            Timber.plant(object : Timber.DebugTree() {
                // prepend log with class name, function name and line number
                override fun createStackElementTag(element: StackTraceElement): String? {
                    return String.format("[%s##%s:%s]", super.createStackElementTag(element), element.methodName, element.lineNumber)
                }
            })
        }
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        initAcra()
    }

    private fun initAcra() {
        val builder = CoreConfigurationBuilder(this)
                .setBuildConfigClass(BuildConfig::class.java)
                .setReportFormat(StringFormat.JSON)
        builder.getPluginConfigurationBuilder(MailSenderConfigurationBuilder::class.java)
                .setMailTo("marijane@chowis.com")
                .setReportAsFile(true)
                .setSubject(getString(R.string.image_sender_crash_log))
                .setBody("***PLEASE PROVIDE THE FOLLOWING INFO BEFORE CLICKING SEND***\n\nNAME:\nSCENARIO:")
                .setEnabled(true)
        if (BuildConfig.DEBUG) {
            ACRA.DEV_LOGGING = true;
        }
        ACRA.init(this, builder)
    }

    companion object {
        private var instance: ChowisApplication? = null

        fun appContext(): ChowisApplication {
            return instance as ChowisApplication
        }
    }
}