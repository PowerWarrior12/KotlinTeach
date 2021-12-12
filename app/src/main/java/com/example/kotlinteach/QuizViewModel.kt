package com.example.kotlinteach

import androidx.lifecycle.ViewModel

private const val TAG = "QuizViewModel"

class QuizViewModel : ViewModel(){
    private val questionBank = listOf(
        Question(R.string.australia_question, false),
        Question(R.string.england_question, true),
        Question(R.string.russia_question, false),
        Question(R.string.usa_question, true),
    )

    var currentIndex = 0

    var isCheater = Array<Boolean>(questionBank.size) { false }

    private var currentCountAnswers = 0

    private var checkQuestionAnswerArray = Array<Boolean?>( questionBank.size) { null }

    val currentQuestionAnswer : Boolean
        get() = questionBank[currentIndex].answer

    val currentQuestionText : Int
        get() = questionBank[currentIndex].textResId

    var currentCountAnswersProp : Int
        get() {
            return this.currentCountAnswers
        }
        set(value) {
            this.currentCountAnswers = value
        }

    var currentUserQuestionAnswer : Boolean?
        get() = this.checkQuestionAnswerArray[currentIndex]
        set(value) {
            this.checkQuestionAnswerArray[currentIndex] = value
        }

    fun moveToNext(){
        this.currentIndex = (this.currentIndex + 1) % questionBank.size
    }
    fun moveToLast(){
        this.currentIndex = if (this.currentIndex - 1 >= 0) { this.currentIndex - 1 } else { questionBank.size - 1 }
    }

    fun getQuestionsCount() : Int{
        return this.questionBank.size
    }

    fun getTrueCountAnswers() : Int{
        return checkQuestionAnswerArray.count { it == true }
    }
}