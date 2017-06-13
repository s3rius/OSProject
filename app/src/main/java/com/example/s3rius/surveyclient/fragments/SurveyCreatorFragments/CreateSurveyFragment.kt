package com.example.s3rius.surveyclient.fragments.SurveyCreatorFragments


import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.ListFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.s3rius.surveyclient.R
import com.example.s3rius.surveyclient.fragments.surveypac.Question
import com.example.s3rius.surveyclient.fragments.surveypac.Survey

import java.util.ArrayList


class CreateSurveyFragment : ListFragment() {


    var survey: Survey? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            val args = arguments
            val survey = args.getSerializable("survey") as Survey
            this.survey = survey
        }
        if (survey != null && this.survey!!.questions[this.survey!!.questions.size - 1].answers.size == 0) {
            this.survey!!.questions.removeAt(this.survey!!.questions.size - 1)
        }
    }

    @RequiresApi(Build.VERSION_CODES.ECLAIR_MR1)
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater!!.inflate(R.layout.fragment_create_survey, container, false)
        val completeButton = view.findViewById(R.id.commitSurvey) as FloatingActionButton
        val changeButton = view.findViewById(R.id.changeSurvey) as FloatingActionButton
        activity.setTitle(R.string.new_survey)
        completeButton.visibility = View.GONE
        changeButton.visibility = View.GONE
        if (arguments != null) {
            completeButton.visibility = View.VISIBLE
            changeButton.visibility = View.VISIBLE
        }
        return view
    }

    override fun onStart() {
        super.onStart()

        if (survey == null) {
            survey = Survey()
            survey!!.questions = ArrayList<Question>()
        }
        listAdapter = SurveyListAdapterforCreation(this@CreateSurveyFragment.context, survey!!)
    }
}// Required empty public constructor
