package com.chowis.cdb.skin.models

import android.os.Environment
import java.io.File

object Constants {
    private const val PROJECT_NAME = "MySkin-Choice-FFA"
    const val CLOUD_SERVER = "analysis.chowis.com"
    const val PORT = "3021"

    const val REST_API_UPLOAD_IMAGE = "http://$CLOUD_SERVER:$PORT"

    const val TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkZXZfaWQiOiJieWw0aDJxNWN2YnB5dTN5IiwiaWF0IjoxNTk3NjA0NDMxfQ.6-dj8Bl0lH8x0jQG0xG7uyFnOYt9k1RMxpnEvWDJ5CI"

    const val CLIENT_DBNAME = "DermoBella"
    const val PROJECTNAME = "DermoBellaS"
    var SKINLOOKPATH = Environment.getExternalStorageDirectory().toString() + File.separator + PROJECTNAME
    var IMAGEPATH = SKINLOOKPATH + File.separator + "images"
    var CLIENT_PATH = IMAGEPATH + File.separator + "clients"
}