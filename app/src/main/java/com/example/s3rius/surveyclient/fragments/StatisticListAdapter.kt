package com.example.s3rius.surveyclient.fragments

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView

import com.example.s3rius.surveyclient.R
import com.example.s3rius.surveyclient.fragments.surveypac.Answer
import com.example.s3rius.surveyclient.fragments.surveypac.Question
import com.example.s3rius.surveyclient.fragments.surveypac.Survey

import org.eazegraph.lib.charts.PieChart
import org.eazegraph.lib.models.PieModel

import java.util.Collections


class StatisticListAdapter internal constructor( val context1: Context , val survey: Survey) : ArrayAdapter<Question>(context1, R.layout.survey_rowlayout, survey.questions) {

    override fun getCount(): Int {
        return survey.questions.size

    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var v: View = convertView!!
        val inflater = getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        v = inflater.inflate(R.layout.statistic_rowlayout, null)
        val questionName = v.findViewById(R.id.StatQuestionText) as TextView
        val pieChart = v.findViewById(R.id.graph) as PieChart
        questionName.text = survey.questions[position].name
        val nextAnswer = v.findViewById(R.id.nextReview) as Button
        nextAnswer.setOnClickListener {
            try {
                pieChart.currentItem = pieChart.currentItem - 1
            } catch (e: IndexOutOfBoundsException) {
                pieChart.currentItem = survey.questions[position].answers.size - 1
            }
        }
        val prevAnswer = v.findViewById(R.id.prevReview) as Button
        prevAnswer.setOnClickListener {
            try {
                pieChart.currentItem = pieChart.currentItem + 1
            } catch (e: IndexOutOfBoundsException) {
                pieChart.currentItem = 0
            }
        }
        if (survey.questions[position].answers.size == 1) {
            nextAnswer.visibility = View.GONE
            prevAnswer.visibility = View.GONE
        }
        var good = true
        val colors = IntArray(5)
        colors[0] = Color.parseColor("#FE6DA8")
        colors[1] = Color.parseColor("#56B7F1")
        colors[2] = Color.parseColor("#CDA67F")
        colors[3] = Color.parseColor("#FED70E")
        colors[4] = Color.parseColor("#FF8F8F8F")
        var other = 0
        Collections.sort<Answer>(survey.questions[position].answers)
        for (i in 0..survey.questions[position].answers.size - 1) {
            if (i < 4)
                pieChart.addPieSlice(PieModel(
                        survey.questions[position].answers[i].name,
                        survey.questions[position].answers[i].id!!.toFloat(), colors[i]))
            else {
                good = false
                other += survey.questions[position].answers[i].id!!
            }
        }
        if (!good)
            pieChart.addPieSlice(PieModel(context.resources.getString(R.string.other),
                    other.toFloat(), colors[4]))
        pieChart.startAnimation()
        pieChart.isUsePieRotation = false
        return v
    }


}
