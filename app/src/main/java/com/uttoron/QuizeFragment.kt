package com.uttoron

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.quize_layout.*
import android.util.Log
import android.view.KeyEvent
import android.widget.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.uttoron.model.QuizeResponse
import com.uttoron.utils.AlertMessage
import com.uttoron.utils.AppConstant
import kotlinx.android.synthetic.main.quiz_result.*
import java.io.IOException
import android.content.res.ColorStateList
import android.graphics.BlendMode
import android.graphics.Color.red
import android.graphics.PorterDuff
import android.os.Build
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import com.google.android.material.radiobutton.MaterialRadioButton


class QuizeFragment : Fragment(){
    var alldata: QuizeResponse? = null
    private var totalAnsCount = 0
    private var rightAnsCount = 0
    var forwardCount = 0
    var isChecked = false
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.quize_layout,container,false)
    }


    fun getJsonDataFromAsset(context: Context, fileName: String): String? {
        val jsonString: String
        try {
            jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }
    @SuppressLint("RestrictedApi", "ResourceType")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        initQuiz()

        val myColorStateList = ColorStateList(
            arrayOf(intArrayOf(resources.getColor(R.color.purple_700))), intArrayOf(
                resources.getColor(R.color.purple_700)
            )
        )
        imgBtnQuesForward.setOnClickListener {

            if(isChecked){
                forwardCount++
                Log.e("forwardCound","forwardCound: "+forwardCount)
                //var quescount = forwardCound +1
                if(forwardCount < alldata!!.questions.size ){
                    tvQuestionName.text = "????????????????????? "+alldata!!.questions[forwardCount].question

                    radioGroup.removeAllViews()
                    alldata!!.questions[forwardCount].options.forEachIndexed { index, element ->
                        val rdbtn = AppCompatRadioButton(requireContext())
                        rdbtn.id = View.generateViewId()
                        rdbtn.text = element.name
                        rdbtn.setTextColor(Color.BLACK)
                        rdbtn.setTypeface(null, Typeface.BOLD)
                        rdbtn.setSupportButtonTintList(
                                ContextCompat.getColorStateList(requireActivity(),
                                    R.drawable.single_choice_state_list))
                        //radioColor(rdbtn)
                        //rdbtn.setCircleColor(Color.parseColor("#FFBA00")) rdbtn.buttonTintList=ColorStateList.valueOf(getColor(requireContext(),R.color.purple_500))
                        //rdbtn.setOnClickListener(requireActivity())
                        radioGroup.addView(rdbtn)
                    }
                }

                var quescount = forwardCount +1
                if(quescount == alldata!!.questions.size){
                    imgBtnQuesForward.visibility = View.GONE
                }

                textViewAlertMsg.visibility = View.GONE
                textViewRightAns.visibility = View.GONE
                textViewRightAns.text = ""
                isChecked = false
            }else{
                AlertMessage.showMessage(requireContext(),"?????????????????????","????????????????????? ????????????????????? ???????????? ?????? ???????????????????????? ??????????????? ????????????")

            }

        }



        fun RadioButton.setCircleColor(color: Int){
            val colorStateList = ColorStateList(
                arrayOf(
                    intArrayOf(-android.R.attr.state_checked), // unchecked
                    intArrayOf(android.R.attr.state_checked) // checked
                ), intArrayOf(
                    Color.GRAY, // unchecked color
                    color // checked color
                )
            )

            // finally, set the radio button's button tint list
            buttonTintList = colorStateList

            // optionally set the button tint mode or tint blend mode
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                buttonTintBlendMode = BlendMode.SRC_IN
            }else{
                buttonTintMode = PorterDuff.Mode.SRC_IN
            }

            invalidate() //could not be necessary
        }

        radioGroup.setOnCheckedChangeListener(
            RadioGroup.OnCheckedChangeListener { group, checkedId ->
                val radio: RadioButton = requireActivity().findViewById(checkedId)

                alldata!!.questions[forwardCount].options.forEachIndexed { index, element ->
                    if(element.name.equals(radio.text)){
                        if (element.answer_status){
                            alldata!!.questions[forwardCount].isRightAnswere = true
                        }else{
                            alldata!!.questions[forwardCount].isRightAnswere = false
                        }


                    }

                }

                isChecked = true
                alldata!!.questions[forwardCount].isAnswered = true
                Log.e("forwardCound","forwardCound: "+forwardCount)

            })


        buttonAnswer.setOnClickListener {
                if(!alldata!!.questions[forwardCount].isAnswered){
                    textViewAlertMsg.visibility = View.VISIBLE
                    textViewRightAns.visibility = View.GONE
                    textViewRightAns.text = ""
                }else{

                    alldata!!.questions[forwardCount].options.forEachIndexed { index, option ->

                        if (option.answer_status){
                            textViewRightAns.visibility = View.VISIBLE
                            textViewRightAns.text = "???????????????: "+option.name
                        }


                    }

                    textViewAlertMsg.visibility = View.GONE

                    for (i in 0 until radioGroup.childCount) {
                        radioGroup.getChildAt(i).setEnabled(false)
                    }

                }



        }


        buttonEnd.setOnClickListener {
            isChecked = false
            forwardCount = 0
            totalAnsCount = 0
            rightAnsCount = 0

            alldata!!.questions.forEachIndexed { index, question ->

                if (question.isAnswered){
                    totalAnsCount++
                }

                if (question.isRightAnswere){
                    rightAnsCount++
                }

            }

            if(totalAnsCount >0){
                quizView.visibility = View.GONE
                linResult.visibility = View.VISIBLE
                imgBtnQuesForward.visibility = View.VISIBLE
                tvResult1.text = "???????????? ????????? ???????????????????????? ??????????????? ????????????????????? "+AppConstant.getBngnumber(totalAnsCount.toString())+" ??????"
                tvResult2.text = "???????????? ??????????????? ????????????????????? "+AppConstant.getBngnumber(rightAnsCount.toString())+" ??????"
                var vulCount = totalAnsCount - rightAnsCount
                tvResult3.text = "????????? ??????????????? ????????????????????? "+AppConstant.getBngnumber(vulCount.toString())+" ??????"
            }else{
                Toast.makeText(requireContext(),"???????????? ???????????? ???????????? ??????????????????", Toast.LENGTH_SHORT).show()
            }



            Log.e("totalAnsCount","totalAnsCount: "+totalAnsCount)
            Log.e("rightAnsCount","rightAnsCount: "+rightAnsCount)
        }

        btnDashBoard.setOnClickListener {
//            quizView.visibility = View.VISIBLE
//            linResult.visibility = View.GONE
//
//            initQuiz()

            //requireActivity().onBackPressed()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.container, HomeFragmentOffline())
            transaction.addToBackStack(null)
            transaction.commit()
        }
        var pressCount = 0
        imgBack.setOnClickListener {
            totalAnsCount = 0
            rightAnsCount = 0
            alldata!!.questions.forEachIndexed { index, question ->

                if (question.isAnswered){
                    totalAnsCount++
                }

                if (question.isRightAnswere){
                    rightAnsCount++
                }

            }


            if(totalAnsCount >0){
                pressCount++
                if (pressCount >1){
                    pressCount = 0
                    //requireActivity().onBackPressed()
                    val transaction = requireActivity().supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.container, HomeFragmentOffline())
                    transaction.addToBackStack(null)
                    transaction.commit()
                }else{
                    Toast.makeText(requireContext(),"???????????? ???????????? ?????????????????? ??????????????????, ????????? ????????? ??????????????? ???????????? ??????????????????",Toast.LENGTH_SHORT).show()
                }
            }else{

                //requireActivity().onBackPressed()
                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                transaction.replace(R.id.container, HomeFragmentOffline())
            transaction.addToBackStack(null)
            transaction.commit()
            }

//            val transaction = requireActivity().supportFragmentManager.beginTransaction()
//           // if (AppConstant.isHome){
//                transaction.replace(R.id.container, HomeFragment())
////            }else{
////                transaction.replace(R.id.container, VideoFragment())
////            }
//            //transaction.replace(R.id.container, VideoFragment())
//            transaction.addToBackStack(null)
//            transaction.commit()
        }



    }

    private fun radioColor(rdbtn: RadioButton) {
        if (Build.VERSION.SDK_INT >= 21) {
            val colorStateList = ColorStateList(
                arrayOf(
                    intArrayOf(-android.R.attr.state_enabled),
                    intArrayOf(android.R.attr.state_enabled)
                ), intArrayOf(
                    Color.BLACK,  // disabled
                    Color.BLUE // enabled
                )
            )
            rdbtn.setButtonTintList(colorStateList) // set the color tint list
            rdbtn.invalidate() // Could not be necessary
        }
    }

    @SuppressLint("RestrictedApi", "ResourceType")
    private fun initQuiz() {
        //alldata!!.questions.cle
        val jsonFileString = getJsonDataFromAsset(requireContext(), "quiz.json")
        Log.e("data", jsonFileString.toString())

        val gson = Gson()
        val listPersonType = object : TypeToken<QuizeResponse>() {}.type
        //var alldata: QuizeResponse = gson.fromJson(jsonFileString, listPersonType)
        alldata = gson.fromJson(jsonFileString, listPersonType)

        tvQuestionName.text = "????????????????????? "+alldata!!.questions[0].question

        radioGroup.removeAllViews()
        alldata!!.questions[0].options.forEachIndexed { index, element ->
            val rdbtn = AppCompatRadioButton(requireContext())
            rdbtn.id = View.generateViewId()
            rdbtn.text = element.name
            rdbtn.setTextColor(Color.BLACK)
            rdbtn.setTypeface(null, Typeface.BOLD)
            rdbtn.setSupportButtonTintList(
                ContextCompat.getColorStateList(requireActivity(),
                    R.drawable.single_choice_state_list))
            //rdbtn.setOnClickListener(requireActivity())
            radioGroup.addView(rdbtn)
        }
    }


    override fun onResume() {
        super.onResume()

        totalAnsCount = 0
        rightAnsCount = 0

        var pressCount = 0
        requireView().isFocusableInTouchMode = true
        requireView().requestFocus()
        requireView().setOnKeyListener { v, keyCode, event ->
            if (event.action === KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                alldata!!.questions.forEachIndexed { index, question ->

                    if (question.isAnswered){
                        totalAnsCount++
                    }

                    if (question.isRightAnswere){
                        rightAnsCount++
                    }

                }


                if(totalAnsCount >0){
                    pressCount++
                    if (pressCount >1){
                        pressCount = 0
                        requireActivity().onBackPressed()
                    }else{
                        Toast.makeText(requireContext(),"???????????? ???????????? ?????????????????? ??????????????????, ????????? ????????? ??????????????? ???????????? ??????????????????",Toast.LENGTH_SHORT).show()
                    }
                }else{
                    requireActivity().onBackPressed()
                }
                // handle back button
                true
            } else false
        }
    }

}