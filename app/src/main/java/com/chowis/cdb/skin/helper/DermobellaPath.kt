package com.chowis.cdb.skin.helper

import android.content.Context
import android.util.Log
import timber.log.Timber
import java.io.File

object DermobellaPath{
    fun dermobellaPath(context: Context): String? {
        var rootPath = context.getExternalFilesDir(null)?.absolutePath
        Log.d("DermobellaPath", "rootPath $rootPath")
        val extraPortion = "Android/data/" + context.packageName
                .toString() + File.separator.toString() + "files"
        rootPath = rootPath?.replace(extraPortion, "")
        // rootPath += "DermoBella"+File.separator.toString()+"data" + File.separator.toString()
        rootPath += "DermoBella" + File.separator.toString() + "data"
        Timber.d("DermoBella:$rootPath")
        return rootPath
    }

    fun dermobellaSPath(context: Context): String? {
        var rootPath = context.getExternalFilesDir(null)?.absolutePath
        val extraPortion = "Android/data/" + context.packageName
                .toString() + File.separator.toString() + "files"
        rootPath = rootPath?.replace(extraPortion, "")
        //rootPath += "DermoBellaS"+File.separator.toString()+"data" + File.separator.toString()
        rootPath += "DermoBellaS" + File.separator.toString() + "data"
        Timber.d("DermoBellaS:$rootPath")
        return rootPath
    }


    fun isOldDermobellaDBExist(context: Context): Boolean {
        return try {
            val oldDBPath = dermobellaPath(context) + File.separator + "dermobella.db"
            val databaseFile = File(oldDBPath)
            val dbExists = databaseFile.exists()
            if (dbExists) {
                var rootPath = context.getExternalFilesDir(null)?.absolutePath
                var copiedOldDB: File = File(rootPath + File.separator + "dermobella.db")
                Log.d("rootPath", "$rootPath")
                databaseFile.copyTo(copiedOldDB, overwrite = true)
            }

            dbExists
        } catch (ex: Exception) {
            Log.d("isOldDermobellaDBExist error", "ex $ex")
            false
        }
    }

    fun isOldDermobellaSDBExist(context: Context): Boolean {
        return try {
            val oldDBPath = dermobellaSPath(context) + File.separator + "dermobellas.db"
            val databaseFile = File(oldDBPath)
            val dbExists = databaseFile.exists()
            if (dbExists) {
                val rootPath = context.getExternalFilesDir(null)?.absolutePath
                val copiedOldDB = File(rootPath + File.separator + "dermobellas.db")
                Log.d("rootPath", "$rootPath")
                databaseFile.copyTo(copiedOldDB, overwrite = true)
            }
            dbExists
        } catch (ex: Exception) {
            Log.d("isOldDermobellaSDBExist error", "ex $ex")
            false
        }
    }

    fun dermobellaSImagePath(context: Context): String? {
        var rootPath = context.getExternalFilesDir(null)?.absolutePath
        val extraPortion = "Android/data/" + context.packageName
                .toString() + File.separator.toString() + "files"
        rootPath = rootPath?.replace(extraPortion, "")
        rootPath += "DermoBellaS/images/clients"
        return rootPath
    }
}