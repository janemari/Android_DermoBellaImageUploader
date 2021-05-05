package cdb.skin.helper

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import timber.log.Timber
import java.util.*

class LanguageHelper : AppCompatActivity() {
    companion object {
        const val PREF_LANGUAGE = "LANGUAGE"
        const val LANGUAGE_KOREAN = "ko"
        const val LANGUAGE_RUSSIAN = "ru"
        const val LANGUAGE_DEUTSCH = "de"
        const val LANGUAGE_JAPANESE = "ja"
        const val LANGUAGE_FRENCH = "fr"
        const val LANGUAGE_ITALIANO = "it"
        const val LANGUAGE_CHINESE = "zh"
        const val LANGUAGE_CHINESE_TRADITIONAL = "zh-rTW"
        const val LANGUAGE_CHINESE_SIMPLIFIED = "zh-rCN"
        const val LANGUAGE_ENGLISH = "en"
        const val LANGUAGE_DUTCH = "nl"
        const val LANGUAGE_THAI = "th"
        const val LANGUAGE_ESPANOL = "es"
        const val LANGUAGE_NORWEGIAN = "no"
        const val LANGUAGE_HUNGARIAN = "hu"
    }

    fun loadLanguage(context: Context) {
        var pref: SharedPreferences = context.getSharedPreferences(PREF_LANGUAGE, Context.MODE_PRIVATE)
        var language = pref.getString(PREF_LANGUAGE, "").toString()

        var prefEditor: SharedPreferences.Editor = pref.edit()
        prefEditor.putString(PREF_LANGUAGE, language)
        prefEditor.commit()

        val config: Configuration = context.resources.configuration
        var locale = Locale(language)
        config.setLocale(locale)
        Locale.setDefault(locale)

//        config.setLayoutDirection(locale)
//        context.createConfigurationContext(config)
        // temporary since deprecated
        context.resources.updateConfiguration(config, null)
    }

    fun changeLanguage(language: String, context: Context) {
        Timber.d("changeLanguage=$language")
        val pref: SharedPreferences = context.getSharedPreferences(PREF_LANGUAGE, Context.MODE_PRIVATE)
//        Log.d("EXFIL LANGUAGE",pref.getString(PREF_LANGUAGE,"").toString())
        val prefEditor: SharedPreferences.Editor = pref.edit()
        prefEditor.putString(PREF_LANGUAGE, language)
        prefEditor.commit()

        val config: Configuration = context.resources.configuration

        when (language.toLowerCase()) {
            LANGUAGE_CHINESE_TRADITIONAL.toLowerCase() -> {
                val locale = Locale(LANGUAGE_CHINESE, "TW")
                config.setLocale(locale)
                Locale.setDefault(locale)
            }
            LANGUAGE_CHINESE_SIMPLIFIED.toLowerCase() -> {
                val locale = Locale(LANGUAGE_CHINESE, "CN")
                config.setLocale(locale)
                Locale.setDefault(locale)
            }
            else -> {
                val locale = Locale(language)
                config.setLocale(locale)
                Locale.setDefault(locale)
            }
        }
        //requireContext().resources.configuration.updateFrom(config)
        // temporary since deprecated
        context.resources.updateConfiguration(config, null)
    }
}