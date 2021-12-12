package com.example.kotlinteach

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders

private const val TAG = "MainActivity"
private const val KEY_INDEX = "index"
private const val COUNT_INDEX = "count"
private const val REQUEST_CODE_CHEAT = 0

class MainActivity : AppCompatActivity() {
    private lateinit var trueButton : Button
    private lateinit var falseButton : Button
    private lateinit var nextButton : Button
    private lateinit var previousButton : Button
    private lateinit var cheatButton : Button
    private lateinit var questionTextView : TextView


    private val quizViewModel : QuizViewModel by lazy {
        ViewModelProviders.of(this).get(QuizViewModel::class.java)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG,"onCreate() called")

        quizViewModel.currentIndex = savedInstanceState?.getInt(KEY_INDEX) ?: 0
        quizViewModel.currentCountAnswersProp = savedInstanceState?.getInt(COUNT_INDEX) ?: 0

        this.trueButton = findViewById(R.id.true_button)
        this.falseButton = findViewById(R.id.false_button)
        this.nextButton = findViewById(R.id.next_button)
        this.previousButton = findViewById(R.id.previous_button)
        this.cheatButton = findViewById(R.id.cheat_button)
        this.questionTextView = findViewById(R.id.question_text)
        this.trueButton.setOnClickListener {
            checkAnswer(true)
            updateAnswerButtons()
            checkQuestionsComplete()
        }
        this.falseButton.setOnClickListener {
            checkAnswer(false)
            updateAnswerButtons()
            checkQuestionsComplete()
        }
        this.nextButton.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()
            updateAnswerButtons()
        }
        this.previousButton.setOnClickListener{
            quizViewModel.moveToLast()
            updateQuestion()
            updateAnswerButtons()
        }
        this.cheatButton.setOnClickListener{
            val intent = CheatActivity.newIntent(this, this.quizViewModel.currentQuestionAnswer)
            val options = ActivityOptions
                .makeClipRevealAnimation(it, 0, 0, it.width, it.height)
            startActivityForResult(intent, REQUEST_CODE_CHEAT, options.toBundle())
        }
        this.questionTextView.setOnClickListener{
            quizViewModel.moveToNext()
            updateQuestion()
            updateAnswerButtons()
        }

        updateQuestion()
        updateAnswerButtons()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK){
            return
        }
        if (requestCode == REQUEST_CODE_CHEAT){
            quizViewModel.isCheater[quizViewModel.currentIndex] = data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG,"onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG,"onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG,"onPause() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG,"onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG,"onDestroy() called")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(TAG,"onStayInstanceState() called")
        outState.putInt(KEY_INDEX, quizViewModel.currentIndex)
        outState.putInt(COUNT_INDEX, quizViewModel.currentCountAnswersProp)
    }

    private fun updateQuestion(){
        val questionTextResId = quizViewModel.currentQuestionText
        questionTextView.setText(questionTextResId)
    }

    private fun checkAnswer(usersAnswer : Boolean){
        val correctAnswer = quizViewModel.currentQuestionAnswer
        this.quizViewModel.currentUserQuestionAnswer = usersAnswer
        this.quizViewModel.currentCountAnswersProp += 1
        val messageResId = when{
            quizViewModel.isCheater[quizViewModel.currentIndex] -> R.string.judgment_toast
            usersAnswer == correctAnswer -> R.string.AnswerTrue
            else -> R.string.AnswerFalse
        }
        var toast : Toast = Toast.makeText(
            this,
            messageResId,
            Toast.LENGTH_LONG)
        toast.setGravity(Gravity.TOP,
            0,
            0)
        toast.show()
    }
    private fun updateAnswerButtons(){
        if (this.quizViewModel.currentUserQuestionAnswer != null){
            this.trueButton.isEnabled = false
            this.falseButton.isEnabled = false
        }
        else{
            this.trueButton.isEnabled = true
            this.falseButton.isEnabled = true
        }
    }

    private fun checkQuestionsComplete(){
        if (this.quizViewModel.currentCountAnswersProp == this.quizViewModel.getQuestionsCount()){
            var result = (this.quizViewModel.getTrueCountAnswers() / this.quizViewModel.getQuestionsCount().toDouble()) * 100
            var toast : Toast = Toast.makeText(
                this,
                "Результат : $result%",
                Toast.LENGTH_LONG)
            toast.setGravity(Gravity.TOP,
                0,
                0)
            toast.show()
        }
    }

}
