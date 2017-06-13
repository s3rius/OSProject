package com.example.s3rius.surveyclient.fragments.SurveyCreatorFragments

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast

import com.example.s3rius.surveyclient.R
import com.example.s3rius.surveyclient.fragments.surveypac.Question
import com.example.s3rius.surveyclient.fragments.surveypac.Survey


class SurveyListAdapterforCreation internal constructor(private val context1: Context, val survey: Survey) : ArrayAdapter<Question>(context1, R.layout.survey_rowlayout, survey.questions), AdapterView.OnItemLongClickListener {
    private val inflater: LayoutInflater? = null

    override fun getCount(): Int {
        return survey.questions.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var v: View = convertView!!
        val inflater = getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        v = inflater.inflate(R.layout.survey_rowlayout, null)
        val surveytext = v.findViewById(R.id.questionText) as TextView
        val answers = v.findViewById(R.id.surveyRadioG) as RadioGroup

        surveytext.text = survey.questions[position].name

        for (i in 0..survey.questions[position].answers.size - 1) {
            val radioButton = RadioButton(context)
            radioButton.isClickable = false
            radioButton.text = survey.questions[position].answers[i].name
            answers.addView(radioButton)
        }
        return v
    }


    override fun onItemLongClick(parent: AdapterView<*>, view: View, position: Int, id: Long): Boolean {
        Toast.makeText(context, "LongClick", Toast.LENGTH_LONG).show()
        return true
    }
}