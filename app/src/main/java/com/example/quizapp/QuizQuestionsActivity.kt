package com.example.quizapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_quiz_questions.*

class QuizQuestionsActivity : AppCompatActivity(), View.OnClickListener {

    private var mCurrentPosition : Int = 1;
    private var mSelectedQuestionsList :ArrayList<Questions>? = null
    private var mSelectedOptionPosition : Int = 0
    private var mCorrectAnswers : Int = 0
    private  var mUserName :String? =null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_questions)

        mUserName = intent.getStringExtra(Constants.USER_NAME)

         mSelectedQuestionsList = Constants.getQuestions()
         setQuestions()
        tv_option_one.setOnClickListener(this)
        tv_option_two.setOnClickListener(this)
        tv_option_three.setOnClickListener(this)
        tv_option_four.setOnClickListener(this)
        btn_submit.setOnClickListener(this)



    }

    private fun setQuestions(){

        val questions : Questions? = mSelectedQuestionsList!![mCurrentPosition - 1]

        defaultOptionsView()
        if(mCurrentPosition == mSelectedQuestionsList!!.size){
            btn_submit.text="FINISH"
        }else{
            btn_submit.text="SUBMIT"
        }

        ll_progressBar.progress = mCurrentPosition
        tv_progress.text = "$mCurrentPosition"+"/"+ ll_progressBar.max
        tv_question.text = questions!!.question
        iv_image.setImageResource(questions.image)
        tv_option_one.text=questions.optionOne
        tv_option_two.text=questions.optionTwo
        tv_option_three.text=questions.optionThree
        tv_option_four.text=questions.optionFour
    }
    private fun defaultOptionsView(){
        val options = ArrayList<TextView>()
        options.add(0,tv_option_one)
        options.add(1,tv_option_two)
        options.add(2,tv_option_three)
        options.add(3,tv_option_four)

        for(option in options){
            option.setTextColor(Color.parseColor("#7A8089"))
            option.typeface = Typeface.DEFAULT
            option.background = ContextCompat.getDrawable(this,R.drawable.default_option_border_bg)
        }
    }

    override fun onClick(v: View?) {

        when(v?.id){
            R.id.tv_option_one ->{
                selectedOptionView(tv_option_one,1)
            }
            R.id.tv_option_two ->{
                selectedOptionView(tv_option_two,2)
            }
            R.id.tv_option_three ->{
                selectedOptionView(tv_option_three,3)
            }
            R.id.tv_option_four ->{
                selectedOptionView(tv_option_four,4)
            }
            R.id.btn_submit ->{
                if(mSelectedOptionPosition==0){
                    mCurrentPosition++
                    when{
                        mCurrentPosition <= mSelectedQuestionsList!!.size ->{
                            setQuestions()
                        }
                        else -> {
                           val intent = Intent(this,ResultActivity::class.java)
                            intent.putExtra(Constants.USER_NAME,mUserName)
                            intent.putExtra(Constants.CORRECT_ANSWERS,mCorrectAnswers)
                            intent.putExtra(Constants.TOTAL_QUESTIONS,mSelectedQuestionsList!!.size)
                            startActivity(intent)
                            finish()
                        }
                    }
                }
                else{
                    val question = mSelectedQuestionsList?.get(mCurrentPosition-1)
                     if(question!!.correctAnswer != mSelectedOptionPosition ){
                         answerView((mSelectedOptionPosition),R.drawable.incorrect_option_border_bg)
                     }
                    else{
                         mCorrectAnswers++
                     }
                       answerView(question.correctAnswer,R.drawable.correct_option_border_bg)
                    if(mCurrentPosition == mSelectedQuestionsList!!.size){
                        btn_submit.text="FINISH"
                    }
                    else{
                        btn_submit.text = "GO TO NEXT QUESTION"
                    }
                    mSelectedOptionPosition = 0
                }

            }
        }
    }

    private fun answerView(answer : Int, drawable :Int){
        when(answer){
            1 -> tv_option_one.background  = ContextCompat.getDrawable(this , drawable)
            2 -> tv_option_two.background  = ContextCompat.getDrawable(this , drawable)
            3 -> tv_option_three.background  = ContextCompat.getDrawable(this , drawable)
            4 -> tv_option_four.background  = ContextCompat.getDrawable(this , drawable)

        }
    }


    private fun selectedOptionView(tv:TextView,selectedOptionNum:Int){
        defaultOptionsView()
        mSelectedOptionPosition=selectedOptionNum
        tv.setTextColor(Color.parseColor("#363A43"))
        tv.setTypeface(tv.typeface,Typeface.BOLD)
        tv.background = ContextCompat.getDrawable(this,R.drawable.selected_option_border_bg)

    }
}