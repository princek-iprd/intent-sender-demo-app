package com.example.intentsenderdemo

import android.app.Activity
import android.content.Intent
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
            // Step A: Extract data from the UI fields
            val facilityId = etFacilityId.text.toString().trim()
            val facilityName = etFacilityName.text.toString().trim()
            val userId = etUserId.text.toString().trim()
            val userName = etUserName.text.toString().trim()
            val activityType = spinnerActivityType.selectedItem.toString()

            // Optional: Basic validation to ensure fields aren't empty
            if (facilityId.isEmpty() || userId.isEmpty()) {
                tvResponse.text = "Please fill in at least Facility ID and User ID."
                tvResponse.setTextColor(android.graphics.Color.RED)
                return@setOnClickListener // Stop execution if validation fails
            }

            // Step B: Create the Intent
            // You MUST coordinate this action string with the target app's developer (or yourself in Phase 5)
            val requestIntent = Intent("com.example.targetapp.ACTION_PROCESS_ACTIVITY")

            // To ensure it only goes to your specific target app (security best practice)
            // Replace with the actual package name of your target app
            requestIntent.setPackage("com.example.targetapp")

            // Step C: Package the data as Intent Extras
            requestIntent.putExtra("EXTRA_FACILITY_ID", facilityId)
            requestIntent.putExtra("EXTRA_FACILITY_NAME", facilityName)
            requestIntent.putExtra("EXTRA_USER_ID", userId)
            requestIntent.putExtra("EXTRA_USER_NAME", userName)
            requestIntent.putExtra("EXTRA_ACTIVITY_TYPE", activityType)

            // Step D: Fire the Intent and wait for the result
            try {
                tvResponse.text = "Sending request to target app..."
                tvResponse.setTextColor(android.graphics.Color.BLUE)

                // Launch the intent using the launcher we defined in Phase 3
                targetAppLauncher.launch(requestIntent)

            } catch (e: android.content.ActivityNotFoundException) {
                // This catches the error if the target app is not installed on the device
                tvResponse.text = "Error: Target app is not installed or the Action string is wrong."
                tvResponse.setTextColor(android.graphics.Color.RED)
            }
        }
    }
}