package com.chowis.cdb.skin

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.chowis.cdb.skin.utils.SharedPref
import com.chowis.cdb.skin.activity.UploadProgressActivity
import com.chowis.cdb.skin.dialog.AppDialog
import com.chowis.cdb.skin.handler.DbSkinAdapter
import com.chowis.cdb.skin.helper.DermobellaPath
import com.chowis.cdb.skin.models.Constants
import com.chowis.cdb.skin.utils.CoreUtils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import timber.log.Timber
import java.io.File
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
        mDbAdapter = DbSkinAdapter.getInstance(this@MainActivity)

        if (DermobellaPath.isOldDermobellaSDBExist(this)) {
            onCheckFileToSendServer().execute()
        }

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
            if (editText_OpticNumber.text!!.isEmpty() || editText_BMID.text!!.isEmpty()) {
                showDialogMessage(getString(R.string.fill_up_necessary_fields))
            }else if(editText_BMID.text!!.toString().trim().toUpperCase() != editText_ConfirmBMID.text!!.toString().trim().toUpperCase()){
                showDialogMessage(getString(R.string.should_match_bmid))
            }else {
                showLottieLoadingDialog()
                CoreUtils.hasInternetConnection().subscribe { hasInternet ->
                    hideLottieLoadingDialog()

                    if (hasInternet) {
                        val imageListToUpload = ArrayList<String>()
                        try {
                            val from = SharedPref.nextUploadedCount //Get next to upload
                            val to = (SharedPref.nextUploadedCount + SharedPref.limitUploadCount) - 1 //Set next limit
                            for (i in from..to) {
                                imageListToUpload.add(mSearchList[i])
                            }
                        } catch (ex: Exception) {
                            Timber.d("ex=$ex")
                        }

                        if (imageListToUpload.size > 0) {
                            val args = Bundle()
                            args.putSerializable("imageList", imageListToUpload)
                            args.putString("ssid", editText_SSID.text.toString().trim())
                            args.putString("bmId", editText_BMID.text.toString().trim())
                            args.putString("brandName", editText_BrandName.text.toString().trim())
                            args.putString("optic_number", editText_OpticNumber.text.toString().trim())

                            val uploadActivityIntent = Intent(this, UploadProgressActivity::class.java)
                            uploadActivityIntent.putExtras(args)
                            startActivity(uploadActivityIntent)
                        }else {
                            showDialogMessage(getString(R.string.no_remaining_image_to_upload))
                        }
                    } else {
                        showDialogMessage(getString(R.string.turnon_internet_upload_image))
                    }
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
                            Timber.d("TEST 추가 2") //Add 2
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
}