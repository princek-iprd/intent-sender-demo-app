package com.example.intentsenderdemo

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider


class MainActivity : AppCompatActivity() {

    private lateinit var etFacilityId: EditText
    private lateinit var etFacilityName: EditText
    private lateinit var etUserId: EditText
    private lateinit var etUserName: EditText
    private lateinit var spinnerActivityType: Spinner
    private lateinit var btnSendIntent: Button
    private lateinit var tvResponse: TextView

    private val targetAppLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        // This block executes when the target app finishes and returns control
        if (result.resultCode == Activity.RESULT_OK) {
            // Success! Extract the data from the returned Intent
            val returnedIntent = result.data

            // We'll assume the target app sends back a string under the key "RESPONSE_MESSAGE"
            val responseMsg = returnedIntent?.getStringExtra("RESPONSE_MESSAGE")
                ?: "No message provided by target app"

            tvResponse.text = "Target App Says:\n$responseMsg"
            tvResponse.setTextColor(android.graphics.Color.parseColor("#007F00")) // Dark Green

        } else {
            // The target app crashed, was closed by the user, or returned an error code
            tvResponse.text = "Request Cancelled or Failed (Code: ${result.resultCode})"
            tvResponse.setTextColor(android.graphics.Color.RED)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etFacilityId = findViewById(R.id.etFacilityId)
        etFacilityName = findViewById(R.id.etFacilityName)
        etUserId = findViewById(R.id.etUserId)
        etUserName = findViewById(R.id.etUserName)
        spinnerActivityType = findViewById(R.id.spinnerActivityType)
        btnSendIntent = findViewById(R.id.btnSendIntent)
        tvResponse = findViewById(R.id.tvResponse)

        btnSendIntent.setOnClickListener {
            // We will write the Intent packaging and launching logic here in Phase 4!
        }
    }
}