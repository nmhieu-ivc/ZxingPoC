package com.ivc.zxingpoc

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.webkit.WebView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
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
    }

    private val zxingActivityResultLauncher  = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        val intentResult = IntentIntegrator.parseActivityResult(it.resultCode, it.data)
        if(intentResult.contents != null) {
            tvResultQR.text = intentResult.contents
        }
    }

}