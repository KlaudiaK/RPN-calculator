package com.klaudiak.calculator

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.math.pow

class MyApplication : AppCompatActivity() {
    private val REQUEST_CODE = 10000
    private var stack = Stack<String>()
    private var textViewArray = arrayOf<TextView>()
    private var tmp = false
    lateinit var rounding: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textViewArray = arrayOf<TextView>(stackOne, stackTwo, stackThree, stackFour)

    }

    fun openSettings(v: View){
        val i = Intent(this, SettingsActivity::class.java)
        myActionResult.launch(i)
    }

    private val myActionResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        onActivityResult(REQUEST_CODE, result)
    }


    private fun onActivityResult(requestCode: Int, result: ActivityResult) {
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data
            when (requestCode) {
                REQUEST_CODE -> {
                    if (intent != null) {
                        if (intent.hasExtra("color")) {

                            intent.extras?.let { constraintLayout.setBackgroundColor(it.getInt("color")) }
                            intent.extras?.let {
                                rounding = it.getInt("rounding").toString()
                                changeRounding()
                            }
                        }
                    }
                }
            }
        }
    }


    @SuppressLint("SetTextI18n")
    fun insert(v: View) {

        if (!tmp) {
            stackOne.text = ""
        }
        stackOne.text = stackOne.text.toString() + (v as Button).text.toString()

        if (!tmp) {
            one.text = "->"
            two.text = "1"
            three.text = "2"
            four.text = "3"
            tmp = true

            val maxIndex = minOf(stack.size - 1, 2)
            for (i in stack.size downTo stack.size - maxIndex) {
                textViewArray[stack.size - i + 1].text = stack[i - 1]
            }
        }
        insertRounding()
    }

    @SuppressLint("DefaultLocale")
    fun enter(v: View) {
        changeRounding()
        if (!stackOne.text.equals("")) {
            stack.add(stackOne.text.toString())
        }

        val maxIndex = minOf(stack.size - 1, 3)

        for (i in stack.size downTo stack.size - maxIndex) {
            textViewArray[stack.size - i].text = stack[i - 1]
        }
        one.text = "1:"
        two.text = "2:"
        three.text = "3:"
        four.text = "4:"
        tmp = false
        changeRounding()

    }

    private fun changeRounding(){
        if (this::rounding.isInitialized) {
            for (element in textViewArray) {
                if (element.text.isNotEmpty())
                    element.text = String.format("%.${rounding}f", element.text.toString().toDouble())

            }
        }
    }

    private fun insertRounding(){
        if (this::rounding.isInitialized) {
            for (i in 1 until textViewArray.size) {
                if (textViewArray[i].text.isNotEmpty())
                    textViewArray[i].text = String.format("%.${rounding}f", textViewArray[i].text.toString().toDouble())

            }
        }
    }


    fun changeSign(v: View) {
        if (stack.size > 0) {
            val number = stack.pop()
            val firstLetter = number.substring(0, 1)
            if (firstLetter != "-") {
                stack.add("-$number")
            } else {
                stack.add(number.substring(1, number.length))
            }

            if (!tmp) {
                stackOne.text = stack.lastElement()
            } else {
                stackTwo.text = stack.lastElement()
            }
            changeRounding()
        }
    }


    fun allClear(v: View) {
        for (element in textViewArray) {
            element.text = ""
        }
        stack.clear()
    }

    fun drop(v: View) {
        if (stack.size > 0) {
            stack.pop()
            draw()
        }
    }

    fun swap(v: View) {
        if (stack.size > 1) {
            val a = stack.pop()
            val b = stack.pop()
            stack.add(a)
            stack.add(b)
            draw()
        }
    }

    private fun draw() {
        if (tmp) {
            for (i in 1 until textViewArray.size) {
                textViewArray[i].text = ""
            }

            val maxIndex = minOf(stack.size - 1, 2)

            for (i in stack.size downTo stack.size - maxIndex) {
                textViewArray[stack.size - i + 1].text = stack[i - 1]
            }
            changeRounding()
        } else {
            for (element in textViewArray) {
                element.text = ""
            }

            val maxIndex = minOf(stack.size - 1, 3)

            for (i in stack.size downTo stack.size - maxIndex) {
                textViewArray[stack.size - i].text = stack[i - 1]
            }
            changeRounding()
        }
    }

    fun undo(v: View) {
        val number = stackOne.text
        if (tmp && (number.isNotEmpty())) {

            stackOne.text = number.substring(0, number.length - 1)
        }
    }

    fun addition(v: View) {
        if (stack.size > 1) {
            val a = stack.pop()
            val b = stack.pop()
            stack.add((a.toDouble() + b.toDouble()).toString())

            draw()
        }
    }

    fun multiplication(v: View) {
        if (stack.size > 1) {
            val a = stack.pop()
            val b = stack.pop()
            stack.add((a.toDouble() * b.toDouble()).toString())

            draw()
        }
    }

    fun division(v: View) {
        if (stack.size > 1) {
            val a = stack.pop()
            val b = stack.pop()
            stack.add((a.toDouble() / b.toDouble()).toString())

            draw()
        }
    }

    fun subtraction(v: View) {
        if (stack.size > 1) {
            val a = stack.pop()
            val b = stack.pop()
            stack.add((a.toDouble() - b.toDouble()).toString())

            draw()
        }
    }

    fun exponentiation(v: View) {
        if (stack.size > 1) {
            val a = stack.pop()
            val b = stack.pop()
            stack.add((b.toDouble().pow(a.toDouble())).toString())

            draw()
        }
    }

    fun sqrt(v: View) {
        if (stack.size > 0) {
            val a = stack.pop()
            stack.add(kotlin.math.sqrt(a.toDouble()).toString())
            draw()
        }
    }

}