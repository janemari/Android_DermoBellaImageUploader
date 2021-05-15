package com.chowis.cdb.skin.utils

import com.chibatching.kotpref.KotprefModel
import java.util.*

object SharedPref : KotprefModel() {
    var selectedLanguage by stringPref(Locale.getDefault().language)

    var nextUploadedCount by intPref(0)
    var limitUploadCount by intPref(0)
}