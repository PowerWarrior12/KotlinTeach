package com.example.kotlinteach

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

private const val EXTRA_ANSWER_IS_TRUE = "com.example.kotlinteach.answer_is_true"
private const val IS_ANSWER_SHOWN = "is_answer_shown"
const val EXTRA_ANSWER_SHOWN = "com.example.kotlinteach.answer_shown"

class CheatActivity : AppCompatActivity() {

    private var answerIsTrue = false
    private var answerIsShown = false
    private lateinit var answerTextView : TextView
    private lateinit var showAnswerTextButton : Button
    private lateinit var apiLevelTextView : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)

        answerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)

        answerIsShown = savedInstanceState?.getBoolean(IS_ANSWER_SHOWN, false) ?: false

        setAnswerShownResult(answerIsShown)

        answerTextView = findViewById(R.id.answer_text)
        showAnswerTextButton = findViewById(R.id.show_answer_button)
        apiLevelTextView = findViewById(R.id.api_level_text)

        val apiLevel = Build.VERSION.SDK_INT
        apiLevelTextView.text = "API Level $apiLevel"

        showAnswerTextButton.setOnClickListener{
            val answerText = when{
                answerIsTrue -> R.string.TrueButton
                else -> R.string.FalseButton
            }
            this.answerTextView.setText(answerText)
            setAnswerShownResult(true)
        }
    }

    private fun setAnswerShownResult(isAnswerShown : Boolean){
        this.answerIsShown = isAnswerShown
        val data = Intent().apply {
            putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown)
        }
        setResult(Activity.RESULT_OK, data)
    }

    companion object {
        fun newIntent(packageContext: Context, answerIsTrue: Boolean): Intent {
            return Intent(packageContext, CheatActivity::class.java).apply {
                putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(IS_ANSWER_SHOWN, this.answerIsShown)
    }
}