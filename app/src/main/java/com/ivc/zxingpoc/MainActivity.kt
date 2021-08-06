package com.ivc.zxingpoc

import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.net.wifi.WifiManager
import android.nfc.NfcAdapter
import android.os.BatteryManager
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.zxing.integration.android.IntentIntegrator


class MainActivity : BaseActivity() {

    lateinit var tvResultQR: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tvResultQR = findViewById(R.id.tvResult)
        // init QR
        val intentIntegrator = IntentIntegrator(this).apply {
            setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
            setPrompt("")
            setCameraId(0)
            setBeepEnabled(true)
            setOrientationLocked(true)
        }
        // load web view
        findViewById<WebView>(R.id.webViewMain).loadUrl("https://www.google.com/")

        // button QR
        findViewById<Button>(R.id.btQR).setOnClickListener {
            // clear text result
            tvResultQR.text = ""
            zxingActivityResultLauncher.launch(intentIntegrator.createScanIntent())
        }

        // button Send
        (findViewById<Button>(R.id.btSend)).setOnClickListener {
            Toast.makeText(this, "Button SEND is clicked", Toast.LENGTH_SHORT).show()
        }


        Log.d("MainActivity", "Wifi is ${isWiFiEnable()}")
        Log.d("MainActivity", "NFC is ${isNFCEnable()}")
        Log.d("MainActivity", "Bluetooth is ${isBluetoothEnable()}")
        Log.d("MainActivity", "Battery is ${currentBatteryLevel()}%")
        Log.d("MainActivity", "Wifi Signal Level is ${currentWifiLevel()}")
    }

    private val zxingActivityResultLauncher  = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        val intentResult = IntentIntegrator.parseActivityResult(it.resultCode, it.data)
        if(intentResult.contents != null) {
            tvResultQR.text = intentResult.contents
        }
    }

    private fun isWiFiEnable(): Boolean {
        return (getSystemService(WIFI_SERVICE) as WifiManager).isWifiEnabled
    }

    private fun isNFCEnable(): Boolean {
        val nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        nfcAdapter?.run {
            return this.isEnabled
        }
        return false
    }

    private fun isBluetoothEnable(): Boolean {
        BluetoothAdapter.getDefaultAdapter()?.run {
            return this.isEnabled
        }
        return false
    }

    private fun currentBatteryLevel(): Int {
        val bm = applicationContext.getSystemService(BATTERY_SERVICE) as BatteryManager
        return bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
    }

    private fun currentWifiLevel() : Int {
        val wifiInfo = (getSystemService(Context.WIFI_SERVICE) as WifiManager).connectionInfo
        // A level of the signal, given in the range of 0 to numLevels-1
        return WifiManager.calculateSignalLevel(wifiInfo.rssi, 5)
    }

}