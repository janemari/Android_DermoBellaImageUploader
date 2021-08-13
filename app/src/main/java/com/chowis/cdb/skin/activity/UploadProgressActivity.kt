package com.chowis.cdb.skin.activity

import android.content.res.Configuration
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.chowis.cdb.skin.R
import com.chowis.cdb.skin.dialog.AppDialog
import com.chowis.cdb.skin.models.Constants
import com.chowis.cdb.skin.service.CloudService
import com.chowis.cdb.skin.utils.SharedPref
import kotlinx.android.synthetic.main.dialog_progress_upload_image.*
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.io.File
import java.io.IOException
import java.net.SocketTimeoutException
import java.util.*
import kotlin.collections.ArrayList


class UploadProgressActivity : AppCompatActivity() {

    var sizeToUpload = 0
    var optic_number = ""
    var ssid = ""
    var bmId = ""
    var brandName = ""
    var isCancelUpload = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFinishOnTouchOutside(false)
        setContentView(R.layout.dialog_progress_upload_image)

        setWindowSize()

        var imageList = ArrayList<String>()
        val extras = intent.extras
        if (extras != null) {
            imageList = extras.getSerializable("imageList") as ArrayList<String>
            ssid = extras.getString("ssid", "")
            bmId = extras.getString("bmId", "")
            brandName = extras.getString("brandName", "")
            optic_number = extras.getString("optic_number", "")
        }
        Timber.d("imageList=${imageList.size}")

        sizeToUpload = if (imageList.size > 100){
            100
        }else{
            imageList.size
        }

//        txt_upload_progress_text.text = "0 / $sizeToUpload"
//        progress_bar_loading.max = sizeToUpload

        btn_upload_cancel.setOnClickListener {
            isCancelUpload = true
            this.finish()
        }

        Timber.d("imageList=${imageList.size}")

        showLottieProgress(true, false)
        var isSuccess = true
        val lastImageCount = SharedPref.nextUploadedCount
        CoroutineScope(Dispatchers.IO).launch {
            imageList.forEachIndexed { index, it ->
                if (!isCancelUpload){
                    runBlocking {
                        try {
                            Timber.d("uploading file=${it}")

                            isSuccess = uploadImage(it.trim())
                        } catch (se: SocketTimeoutException) {
                            Timber.d("Error: $se")
                            isSuccess = false
                            runOnUiThread {
                                txt_download_progress.visibility = View.VISIBLE
                                txt_download_progress.text = se.toString()
                                showLottieProgress(false, false)
                            }
                            // display timeout alert to user
                        } catch (e: IOException) {
                            Timber.d("Error: $e")
                            isSuccess = false
                            runOnUiThread {
                                txt_download_progress.visibility = View.VISIBLE
                                txt_download_progress.text = e.toString()
                                showLottieProgress(false, false)
                            }
                            // handle general IO error
                        } catch (e: Exception) {
                            Timber.d("Error: $e")
                            isSuccess = false
                            runOnUiThread {
                                txt_download_progress.visibility = View.VISIBLE
                                txt_download_progress.text = e.toString()
                                showLottieProgress(false, false)
                            }
                            // just in case you missed anything else
                        } finally {}
                    }

                    withContext(Dispatchers.Main) {
                        if (isSuccess) {
                            SharedPref.nextUploadedCount = lastImageCount + (index + 1)
                            txt_download_progress.visibility = View.VISIBLE
                            txt_download_progress.text = getString(R.string.upload_complete)
                            showLottieProgress(false, true)

//                            progress_bar_loading.progress = index + 1
//                        txt_download_progress.text = "${getString(R.string.total_uploaded)} ${SharedPref.nextUploadedCount}"
//                            txt_upload_progress_text.text = "${progress_bar_loading.progress} / $sizeToUpload"

//                            if (imageList.lastIndex == index) {
//                                GlobalScope.launch {
//                                    txt_download_progress.text = getString(R.string.upload_complete)
//                                    delay(1000L)
//                                    this@UploadProgressActivity.finish()
//                                }
//                            }
                        }
                    }
                }else {
                    return@forEachIndexed
                }
            }
        }
    }

    private fun uploadImage(path: String): Boolean {
        var isSuccess = false
        val retrofit = Retrofit.Builder()
                .baseUrl(Constants.REST_API_UPLOAD_IMAGE)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val cloudService = retrofit.create(CloudService::class.java)
        val paramsHeader: MutableMap<String, Any> = HashMap()
        paramsHeader["access_token"] = Constants.TOKEN

        val file = File(path)
        val fileUpload = file.asRequestBody("application/zip".toMediaType())
        val filePart = createPart(file, fileUpload)

        val device = ssid.toPlainTextBody()
        val programBefore = "zip_data".toPlainTextBody()
        val programAfter = "zip_data".toPlainTextBody()

        val call = cloudService.uploadImage(optic_number, paramsHeader, filePart, device,
                bmId.toPlainTextBody(), brandName.toPlainTextBody(), programBefore, programAfter)

        val response = call.execute()
        val responseString = response.body()!!.string()
        Timber.d("response.body()=%s", response)
        val obj = JSONObject(responseString)
        Timber.d("obj=%s", obj)
        val status = obj.getInt("status")
        if (status == 200) {
            Timber.d("uploadImage=Success Upload")
            isSuccess = true

//                val body = obj.getJSONObject("body")
//                token = body.getString("access_token")
//                Timber.d("accessToken=%s", token)
        }

        return isSuccess
    }

    private fun createPart(file: File, requestBody: RequestBody): MultipartBody.Part {
        return MultipartBody.Part.createFormData("fileupload", file.name, requestBody)
    }

    private fun String.toPlainTextBody() = toRequestBody("text/plain".toMediaType())

    private fun setWindowSize() {
        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)
        val width = dm.widthPixels
        val height = dm.heightPixels
        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            window.setLayout((width * 0.65).toInt(), (height * 0.85).toInt())
        } else {
            window.setLayout((width * 0.95).toInt(), (height * 0.75).toInt())
        }
        window.setBackgroundDrawableResource(android.R.color.transparent)
    }

    private fun showLottieProgress(showLoading: Boolean, isSuccess: Boolean){
        if (showLoading){
            lottieAnimationView.setAnimation("loading_arrow.json")
            lottieAnimationView.playAnimation()
        }else{
            if (isSuccess) {
                lottieAnimationView.setAnimation("checkmark.json")
                lottieAnimationView.playAnimation()
            } else {
                lottieAnimationView.setAnimation("wrongmark.json")
                lottieAnimationView.playAnimation()
            }
        }
    }
}