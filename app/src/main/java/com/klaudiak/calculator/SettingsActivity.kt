package com.klaudiak.calculator

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
    }

    override fun finish() {
        val data = Intent()
        var color = android.graphics.Color.WHITE
        var rounding = 1

        color = when (true) {
            yellow.isChecked -> resources.getColor(R.color.yellow)
            green.isChecked -> resources.getColor(R.color.green)
            white.isChecked -> android.graphics.Color.WHITE
            red.isChecked -> resources.getColor(R.color.red);
            else -> {
                android.graphics.Color.WHITE
            }
        }

        rounding = when(true){
            rounding0.isChecked -> 0
            rounding1.isChecked -> 1
            rounding2.isChecked -> 2
            rounding3.isChecked -> 3
            else -> 0
        }


        data.putExtra("color", color)
        data.putExtra("rounding", rounding)
        setResult(Activity.RESULT_OK,data)
        super.finish()
    }
}