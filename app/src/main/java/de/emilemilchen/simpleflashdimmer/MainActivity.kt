package de.emilemilchen.simpleflashdimmer

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.topjohnwu.superuser.Shell

class MainActivity : AppCompatActivity() {

    private lateinit var seekBar: SeekBar
    private lateinit var seekBarValue: TextView
    private lateinit var requestRootButton: Button
    private lateinit var inputMaxValue: EditText
    private lateinit var confirmMaxButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        seekBar = findViewById(R.id.seekBar)
        seekBarValue = findViewById(R.id.seekBarValue)
        requestRootButton = findViewById(R.id.requestRootButton)
        inputMaxValue = findViewById(R.id.inputMaxValue)
        confirmMaxButton = findViewById(R.id.confirmMaxButton)

        // Set default max value of SeekBar
        seekBar.max = 16

        // Request root permission when the app is launched
        requestRootPermission()

        requestRootButton.setOnClickListener {
            requestRootPermission()
        }

        confirmMaxButton.setOnClickListener {
            setSeekBarMaxValue()
        }

        inputMaxValue.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                setSeekBarMaxValue()
                true
            } else {
                false
            }
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                seekBarValue.text = progress.toString()
                writeBrightnessValue(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
    }

    private fun requestRootPermission() {
        Shell.getShell {
            if (it.isRoot) {
                // Root access granted
                runOnUiThread {
                    requestRootButton.text = "Root Permission Granted"
                }
            } else {
                // Root access denied
                runOnUiThread {
                    requestRootButton.text = "Root Permission Denied"
                }
            }
        }
    }

    private fun setSeekBarMaxValue() {
        val value = inputMaxValue.text.toString()
        if (value.isNotEmpty()) {
            try {
                val intValue = value.toInt()
                if (intValue > 0) {
                    seekBar.max = intValue
                    Toast.makeText(this, "Max value set to $intValue", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Please enter a value greater than 0", Toast.LENGTH_SHORT).show()
                }
            } catch (e: NumberFormatException) {
                Toast.makeText(this, "Invalid input. Please enter a valid number.", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Input field cannot be empty", Toast.LENGTH_SHORT).show()
        }
    }

    private fun writeBrightnessValue(value: Int) {
        if (Shell.rootAccess()) {
            val command = "echo $value > /sys/class/leds/torch-light0/brightness"
            Shell.cmd(command).exec()
        } else {
            runOnUiThread {
                requestRootButton.text = "Root Permission Required"
            }
        }
    }
}
