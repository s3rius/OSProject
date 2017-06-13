package com.example.s3rius.surveyclient.fragments

import android.content.Context
import android.content.res.Resources
import android.support.annotation.IdRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView

import com.example.s3rius.surveyclient.R
import com.example.s3rius.surveyclient.fragments.surveypac.Question
import com.example.s3rius.surveyclient.fragments.surveypac.Survey


class SurveyListAdapter internal constructor(private val context1: Context, val survey: Survey) : ArrayAdapter<Question>(context1, R.layout.survey_rowlayout, survey.questions) {
    private val inflater: LayoutInflater? = null
    private var nocomment = false

    override fun getCount(): Int {
        if (survey.comment == null) {
            nocomment = true
            return survey.questions.size
        } else {
            return survey.questions.size + 1
        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var v: View = convertView!!
        val inflater = getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        v = inflater.inflate(R.layout.survey_rowlayout, null)
        if (nocomment) {
            val surveytext = v.findViewById(R.id.questionText) as TextView
            val answers = v.findViewById(R.id.surveyRadioG) as RadioGroup
            surveytext.text = survey.questions[position].name
            for (i in 0..survey.questions[position].answers.size - 1) {
                val radioButton = RadioButton(context)
                radioButton.text = survey.questions[position].answers[i].name
                if (survey.questions[position].answers[i].isAnswered)
                    radioButton.isChecked = true
                answers.addView(radioButton)
            }
            answers.setOnCheckedChangeListener { group, checkedId ->
                for (i in 0..group.childCount - 1) {
                    if ((group.getChildAt(i) as RadioButton).isChecked) {
                        survey.questions[position].uncheck()
                        survey.questions[position].answers[i].isAnswered = true
                        break
                    }
                }
            }
            return v
        } else {
            if (position != 0) {
                val surveytext = v.findViewById(R.id.questionText) as TextView
                val answers = v.findViewById(R.id.surveyRadioG) as RadioGroup
                surveytext.text = survey.questions[position - 1].name
                for (i in 0..survey.questions[position - 1].answers.size - 1) {
                    val radioButton = RadioButton(context)
                    radioButton.text = survey.questions[position - 1].answers[i].name
                    if (survey.questions[position - 1].answers[i].isAnswered)
                        radioButton.isChecked = true
                    answers.addView(radioButton)
                }
                answers.setOnCheckedChangeListener { group, checkedId ->
                    for (i in 0..group.childCount - 1) {
                        if ((group.getChildAt(i) as RadioButton).isChecked) {
                            survey.questions[position - 1].uncheck()
                            survey.questions[position - 1].answers[i].isAnswered = true
                            break
                        }
                    }
                }
                return v
            } else {
                val comment = v.findViewById(R.id.questionText) as TextView
                comment.text = context.getString(R.string.inSurveyComment) + survey.comment
                return v
            }
        }
    }


}