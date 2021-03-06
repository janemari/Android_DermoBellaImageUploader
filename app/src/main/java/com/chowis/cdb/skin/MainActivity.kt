package com.chowis.cdb.skin

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.chowis.cdb.skin.activity.UploadProgressActivity
import com.chowis.cdb.skin.dialog.AppDialog
import com.chowis.cdb.skin.handler.DbSkinAdapter
import com.chowis.cdb.skin.models.Constants
import com.chowis.cdb.skin.models.Constants.BACKUPPATH
import com.chowis.cdb.skin.utils.CoreUtils
import com.chowis.cdb.skin.utils.SharedPref
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import net.lingala.zip4j.ZipFile
import net.lingala.zip4j.model.ZipParameters
import net.lingala.zip4j.model.enums.CompressionLevel
import net.lingala.zip4j.model.enums.CompressionMethod
import timber.log.Timber
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.coroutines.CoroutineContext


class MainActivity : AppCompatActivity() {
    lateinit var loadingLottieDialog: Dialog
    lateinit var appDialog: AppDialog
    lateinit var mDbAdapter: DbSkinAdapter

    private val requestsUploadImages = ArrayList<Deferred<Boolean>>()
    private val mSearchList = ArrayList<String>()
    private var mFileListOnDB = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        mDbAdapter = DbSkinAdapter.getInstance(this@MainActivity)

        /*if (DermobellaPath.isOldDermobellaSDBExist(this)) {
            onCheckFileToSendServer().execute()
        }*/

        btnClose.setOnClickListener {
            requestsUploadImages.forEach {
                if (it.isActive){
                    Timber.d("Cancelling job...")
                    it.cancel()
                }
            }

            this@MainActivity.finishAffinity()
        }

        btnSend.setOnClickListener {
            if (SharedPref.nextUploadedCount > 0){
                showDialogMessage(getString(R.string.backup_file_already_uploaded))
                return@setOnClickListener
            }

            if (editText_OpticNumber.text!!.isEmpty()) {
                showDialogMessage(getString(R.string.fill_up_necessary_fields))
            }else if(editText_BMID.text!!.isEmpty()){
                showDialogMessage(getString(R.string.email_address_required))
            }else {
                if (CoreUtils.isOnline(this)){
                    val imageListToUpload = ArrayList<String>()

                    val dir = File(Constants.SKINLOOKPATH)
                    if (!dir.exists()) dir.mkdirs()

                    val formatter = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
                    val currentTime = Calendar.getInstance().time

                    val folderBackup = BACKUPPATH
//                    //delete the folder first to remove the files inside
//                    val file = File(folderBackup)
//                    file.deleteRecursively()

                    val dirBackup = File(folderBackup)
                    if (!dirBackup.exists()) {
                        dirBackup.mkdirs()
                    }

                    val zipFile = folderBackup + "backup_" + formatter.format(currentTime) + ".zip"

                    lifecycleScope.executeAsyncTask(onPreExecute = {
                        showLottieLoadingDialog()
                    }, doInBackground = {
                        val zip = ZipFile(zipFile)
                        val parameters = ZipParameters()
                        parameters.compressionMethod = CompressionMethod.DEFLATE
                        parameters.compressionLevel = CompressionLevel.NORMAL
                        if (File(Constants.SKINLOOKPATH).exists())
                            zip.addFolder(File(Constants.SKINLOOKPATH), parameters)
                        if (File(Constants.CLIENT_DB_PATH).exists())
                            zip.addFolder(File(Constants.CLIENT_DB_PATH), parameters)
                        if (File(Constants.PRODUCT_DB_PATH).exists())
                            zip.addFolder(File(Constants.PRODUCT_DB_PATH), parameters)
                    }, onPostExecute = {
                        imageListToUpload.add(zipFile)

//                    try {
//
//                        val backupFile = getBackupFileName(BACKUPPATH)
//                        if (!backupFile.isNullOrEmpty())
//                            imageListToUpload.add(backupFile)
//                    } catch (ex: Exception) {
//                        Timber.d("ex=$ex")
//                    }

                        val bmId = editText_BMID.text!!.toString().trim()

                        if (File(zipFile).exists()){
                            val args = Bundle()
                            args.putSerializable("imageList", imageListToUpload)
                            args.putString("ssid", "zip_data") //add string because it will be error from api if empty
                            args.putString("bmId", bmId)
                            args.putString("brandName", "zip_data") //add string because it will be error from api if empty
                            args.putString("optic_number", editText_OpticNumber.text.toString().trim())

                            val uploadActivityIntent = Intent(this, UploadProgressActivity::class.java)
                            uploadActivityIntent.putExtras(args)
                            startActivity(uploadActivityIntent)
                        }
                    })
                } else {
                    showDialogMessage(getString(R.string.turnon_internet_upload_image))
                }
            }
        }

        setupLoadingDialog()
    }

    private fun setupLoadingDialog() {
        loadingLottieDialog = Dialog(this)
        val layout = layoutInflater.inflate(R.layout.layout_loading_arrow_lottie, null)
        loadingLottieDialog.setContentView(layout)
        loadingLottieDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        loadingLottieDialog.setCancelable(false)
    }

    fun showLottieLoadingDialog() {
        Timber.d(" ")
        if (!this.isFinishing)
            loadingLottieDialog.show()
    }

    fun hideLottieLoadingDialog() {
        Timber.d(" ")
        if (loadingLottieDialog.isShowing){
            loadingLottieDialog.dismiss()
        }
    }

    override fun onPause() {
        loadingLottieDialog.dismiss() //Add to avoid leak issue
        super.onPause()
    }
    private fun showDialogMessage(message: String){
        appDialog = AppDialog(this, message)
        appDialog.setOnDismissListener {
            appDialog.cancel()
        }
        appDialog.show()
    }

    private fun SearchFile(path: String): ArrayList<String> {
        val file = File(path)
        if (file.exists()) {
            val childFileList = file.listFiles()
            for (childFile in childFileList) {
                if (childFile.isDirectory) {
                    SearchFile(childFile.absolutePath)
                } else if (childFile.name.contains("jpg")) {
                    val fileName = childFile.name.split("_".toRegex()).toTypedArray()
                    if (fileName.size > 8) {
                        if (mFileListOnDB.size > 0) {
                            var check = false
                            for (fileList in mFileListOnDB) {
                                if (fileList == childFile.absolutePath) {
                                    check = true
                                    continue
                                }
                            }
                            if (!check) {
                                mSearchList.add(childFile.absolutePath)
                            }
                        } else {
                            mSearchList.add(childFile.absolutePath)
                            Timber.d("TEST ?????? 2") //Add 2
                        }
                    }
                }
            }
        }
        return mSearchList
    }

    private var fileCount = 0
    inner class onCheckFileToSendServer : CoroutineScope {
        private var job: Job = Job()
        override val coroutineContext: CoroutineContext
            get() = Dispatchers.Main + job // to run code in Main(UI) Thread

        // call this method to cancel a coroutine when you don't need it anymore,
        // e.g. when user closes the screen
        fun cancel() {
            job.cancel()
        }

        fun execute() = launch {
            onPreExecute()
            val result = doInBackground() // runs in background thread without blocking the Main Thread
            onPostExecute(result)
        }

        private suspend fun doInBackground(): Int = withContext(Dispatchers.IO) { // to run code in Background Thread
            // do async work
            var result = 0

            val path: String = Constants.CLIENT_PATH
            Timber.d("path=$path")

            mSearchList.clear()
            mFileListOnDB.clear()

            if(mDbAdapter == null) {
                mDbAdapter = DbSkinAdapter.getInstance(this@MainActivity)
            }
            mDbAdapter.open()

            mFileListOnDB = mDbAdapter.sendImages

            Timber.d("mFileListOnDB.size(): ${mFileListOnDB.size}")

            mDbAdapter.close()

            SearchFile(path)

            Timber.d("mSearchList.size(): ${mSearchList.size}")

            if (mSearchList.size == 0) {
                result = 1
            } else {
                fileCount = mSearchList.size
                result = 2
            }

            delay(1000) // simulate async work
            return@withContext result
        }

        // Runs on the Main(UI) Thread
        private fun onPreExecute() {
            showLottieLoadingDialog()
        }

        // Runs on the Main(UI) Thread
        private fun onPostExecute(result: Int) {
           hideLottieLoadingDialog()

            when (result) {
                1 -> {
                    showDialogMessage(getString(R.string.not_exist_send_image_files))
                }
                2 -> {
                    SharedPref.limitUploadCount = fileCount //Set limit count to max file count
                    btnSend.isEnabled = true

                    var message = getString(R.string.exist_send_image_files)
                    message += fileCount
                    showDialogMessage(message)
                }
            }
        }
    }

    private fun getBackupFileName(path: String): String? {
        val dirBackup = File(path)
        if (TextUtils.isEmpty(path) || !dirBackup.exists()) {
            showDialogMessage(getString(R.string.the_backup_file))
            return null
        }
        var fileListSrc: Array<File>? = null
        // dirFileSrc = new File( folderBackup );
        fileListSrc = dirBackup.listFiles()
        if (null == fileListSrc || fileListSrc.isEmpty()) {
            showDialogMessage(getString(R.string.the_backup_file))
            return null
        }

        var lp = 0
        var backupFileName: String? = null
        for (one in fileListSrc) {
            if (one.isFile) {
                lp++
                backupFileName = one.parent + File.separator + one.name
            }
        }
        return backupFileName
    }

    fun <R> CoroutineScope.executeAsyncTask(
        onPreExecute: () -> Unit,
        doInBackground: () -> R,
        onPostExecute: (R) -> Unit
    ) = launch {
        onPreExecute()
        val result = withContext(Dispatchers.IO) { // runs in background thread without blocking the Main Thread
            doInBackground()
        }
        onPostExecute(result)
    }
}