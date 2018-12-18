package com.hfad.information

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.TextView
import android.net.wifi.WifiManager
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import java.net.Inet4Address
import java.util.*
import java.util.logging.Formatter

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    lateinit var text: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        text = findViewById(R.id.sys_info)

        if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_WIFI_STATE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_WIFI_STATE),
                    1)
        } else {
            text.text = getSystemInfo(this)
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            1 -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    text.text = getSystemInfo(this)
                } else {
                    finishAffinity()
                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }

    fun getSystemInfo(a: Activity) : String {
        var s = ""
        var wifiManager: WifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        var ipAdress = android.text.format.Formatter.formatIpAddress(wifiManager.connectionInfo.ipAddress)

        try {
            var pInfo = a.packageManager.getPackageInfo(a.packageName, PackageManager.GET_META_DATA)
            s += "\n APP Package Name: " + a.packageName
            s += "\n APP Version Name: " + pInfo.versionName
            s += "\n APP Version Code: " + pInfo.versionCode
            s += "\n"
        } catch (e: PackageManager.NameNotFoundException){
        }

        s+= "\n Ip Adress: $ipAdress"
        s += "\n OS Version: " + System.getProperty("os.version") + " (" + Build.VERSION.INCREMENTAL + ")"
        s += "\n OS API Level: " + Build.VERSION.SDK
        s += "\n Device: " + Build.DEVICE
        s += "\n Model (and Product): " + Build.MODEL + " (" + Build.PRODUCT + ")"


        s += "\n Manufacturer: " + Build.MANUFACTURER
        s += "\n Other TAGS: " + Build.TAGS

        s += "\n screenWidth: " + a.window.windowManager.defaultDisplay.width
        s += "\n screenHeight: " + a.window.windowManager.defaultDisplay.width
        s += "\n Keyboard available: " + (a.resources.configuration.navigation == Configuration.NAVIGATION_TRACKBALL)
        s += "\n SD CArd state: " + Environment.getExternalStorageState()

        var p = System.getProperties()
        var keys = p.keys()
        var key: String

        while (keys.hasMoreElements()) {
            key = keys.nextElement().toString()
            s += "\n > " + key + " = " + p[key].toString()
        }

        return s

    }
}
