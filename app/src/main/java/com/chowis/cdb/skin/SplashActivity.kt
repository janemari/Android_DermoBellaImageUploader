package cdb.skin

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.chowis.cdb.skin.R
import cdb.skin.helper.LanguageHelper
import timber.log.Timber
import com.chowis.cdb.skin.utils.SharedPref
import com.chowis.cdb.skin.MainActivity
import java.util.*

class SplashActivity : AppCompatActivity() {
    private val splashTime = 4800L // 4 seconds
    private lateinit var handler: Handler

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }

    override fun onResume() {
        super.onResume()

        if (!checkPermission()) {
            grantPermissionAndUI()
        } else {
            val splashLogo = findViewById<AppCompatImageView>(R.id.splash_logo)
            val tween: Animation = AnimationUtils.loadAnimation(
                    this@SplashActivity,
                    R.anim.splash_fade_in_out
            )
            splashLogo.startAnimation(tween)
            splashLogo.visibility = View.VISIBLE
            handler = Handler()
            handler.postDelayed({
                splashLogo.visibility = View.INVISIBLE
                goToDermobellaSkinCloudMainActivity()
            }, splashTime)
        }
    }

    private fun goToDermobellaSkinCloudMainActivity() {
        Timber.d("SharedPref.languageSelected=${SharedPref.selectedLanguage}")
        if (SharedPref.selectedLanguage.isEmpty()) {
            val systemLanguage = Locale.getDefault().language
            SharedPref.selectedLanguage = systemLanguage

            val languageHelper = LanguageHelper()
            languageHelper.changeLanguage(SharedPref.selectedLanguage, this)
        } else {
            val languageHelper = LanguageHelper()
            languageHelper.changeLanguage(SharedPref.selectedLanguage, this)
        }

        val mainActivityIntent = Intent(
                applicationContext,
                MainActivity::class.java
        )
        startActivity(mainActivityIntent)
        finish()
    }

    private fun checkPermission(): Boolean {
        var permission = false
        val permissionLocation: Int =
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        val permissionCoarseLocation: Int =
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
        val permissionAccessWifiState: Int =
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE)
        val permissionChangeWifiState: Int =
                ContextCompat.checkSelfPermission(this, Manifest.permission.CHANGE_WIFI_STATE)
        val permissionReadStorage: Int =
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        val permissionWriteStorage: Int =
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val permissionReadPhoneState: Int =
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
        if (permissionLocation == PackageManager.PERMISSION_GRANTED && permissionCoarseLocation == PackageManager.PERMISSION_GRANTED && permissionAccessWifiState == PackageManager.PERMISSION_GRANTED && permissionChangeWifiState == PackageManager.PERMISSION_GRANTED && permissionReadStorage == PackageManager.PERMISSION_GRANTED && permissionReadPhoneState == PackageManager.PERMISSION_GRANTED && permissionWriteStorage == PackageManager.PERMISSION_GRANTED
        ) {
            permission = true
        }
        return permission
    }

    private fun grantPermissionAndUI() {
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        + ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE)
                        + ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                )
                        + ContextCompat.checkSelfPermission(this, Manifest.permission.CHANGE_WIFI_STATE)
                        + ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                )
                        + ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                        + ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                        + ContextCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS)
                        )
                != PackageManager.PERMISSION_GRANTED
        ) { // Do something, when permissions not granted
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    )
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                            this,
                            Manifest.permission.ACCESS_WIFI_STATE
                    )
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                            this,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                            this,
                            Manifest.permission.CHANGE_WIFI_STATE
                    )
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                            this,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                            this,
                            Manifest.permission.READ_PHONE_STATE
                    )
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                            this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                            this,
                            Manifest.permission.GET_ACCOUNTS
                    )
            ) {
                ActivityCompat.requestPermissions(
                        this, arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_WIFI_STATE,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.CHANGE_WIFI_STATE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.GET_ACCOUNTS
                ),
                        101
                )
            } else { // Directly request for required permissions, without explanation
                ActivityCompat.requestPermissions(
                        this, arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_WIFI_STATE,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.CHANGE_WIFI_STATE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.GET_ACCOUNTS
                ),
                        101
                )
            }
        } else { // Do something, when permissions are already granted
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            101 -> {
                var permissionOk = true
                var position = 0
                if (grantResults.isNotEmpty()) {
                    for (grantResult in grantResults) {
                        if (grantResult == PackageManager.PERMISSION_DENIED) {
                            if (position != 7) //7 == GetAccounts, disregard
                                permissionOk = false
                        }

                        position += 1
                    }

                    if (permissionOk) {
                        onResume()
                    } else {
                        this.finishAffinity()
                    }
                }
            }
        }
    }
}