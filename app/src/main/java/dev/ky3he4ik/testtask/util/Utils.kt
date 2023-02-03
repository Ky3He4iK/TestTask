package dev.ky3he4ik.testtask.util

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.telephony.TelephonyManager
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.util.*

object Utils {
    private fun checkIsEmu(): Boolean {
        val phoneModel = Build.MODEL
        val buildProduct = Build.PRODUCT
        val buildHardware = Build.HARDWARE
        if (Build.FINGERPRINT.startsWith("generic")
            || phoneModel.contains("google_sdk")
            || phoneModel.lowercase(Locale.getDefault()).contains("droid4x")
            || phoneModel.contains("Emulator")
            || phoneModel.contains("Android SDK built for x86")
            || Build.MANUFACTURER.contains("Genymotion")
            || buildHardware == "goldfish"
            || Build.BRAND.contains("google")
            || buildHardware == "vbox86"
            || buildProduct == "sdk"
            || buildProduct == "google_sdk"
            || buildProduct == "sdk_x86"
            || buildProduct == "vbox86p"
            || Build.BOARD.lowercase(Locale.getDefault()).contains("nox")
            || Build.BOOTLOADER.lowercase(Locale.getDefault()).contains("nox")
            || buildHardware.lowercase(Locale.getDefault()).contains("nox")
            || buildProduct.lowercase(Locale.getDefault()).contains("nox")
        )
            return true
        if (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
            return true
        return "google_sdk" == buildProduct
    }

    private fun isSimAbsent(context: Context): Boolean {
        // add all sim states that can be "no SIM"
        val telephonyManager =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val simAbsentStates = mutableListOf(
            TelephonyManager.SIM_STATE_ABSENT,
            TelephonyManager.SIM_STATE_UNKNOWN,
        )
        if (Build.VERSION.SDK_INT >= 26)
            simAbsentStates.add(TelephonyManager.SIM_STATE_PERM_DISABLED)
        return simAbsentStates.contains(telephonyManager.simState)
    }

    fun isTestScenario(context: Context): Boolean {
        return checkIsEmu() || isSimAbsent(context)
    }

    fun hasInternet(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
                ?: return false

        if (Build.VERSION.SDK_INT >= 23) {
            connectivityManager.getLinkProperties(
                connectivityManager.activeNetwork ?: return false
            ) ?: return false
            return true
        } else {
            return connectivityManager.activeNetworkInfo?.isConnected ?: false
        }
    }

    fun urlEncode(url: String): String = URLEncoder.encode(url, StandardCharsets.UTF_8.toString())

    fun urlDecode(encodedUrl: String?): String? {
        encodedUrl ?: return null
        return URLDecoder.decode(encodedUrl, StandardCharsets.UTF_8.toString())
    }
}
