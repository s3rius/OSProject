package com.example.s3rius.surveyclient.fragments.SurveyCreatorFragments


import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast

import com.example.s3rius.surveyclient.R
import com.example.s3rius.surveyclient.fragments.surveypac.Survey

/**
 * A simple [Fragment] subclass.
 */
class CreateQuestion : Fragment() {

    var survey: Survey = null!!
    internal var button: FloatingActionButton
    internal var questionInt: Int = 0
    internal var question: EditText

    @RequiresApi(Build.VERSION_CODES.ECLAIR_MR1)
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        activity.setTitle(R.string.new_question)
        if (arguments != null) {
            val arguments = arguments
            survey = arguments.getSerializable("survey") as Survey
            if (arguments.containsKey("questionInt")) {
                questionInt = arguments.getInt("questionInt")
                val InputFragmentView = inflater!!.inflate(R.layout.fragment_create_question, container, false)
                button = InputFragmentView.findViewById(R.id.addQuestionDone) as FloatingActionButton
                question = InputFragmentView.findViewById(R.id.newQuestText) as EditText
                button.setOnClickListener {
                    if (question.text.toString() != "") {
                        survey.questions[questionInt].name = question.text.toString()
                        fragmentManager.popBackStack()
                    } else {
                        Toast.makeText(container!!.context, "Please enter the question", Toast.LENGTH_LONG).show()
                    }
                }
                return InputFragmentView
            }
        }
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_create_question, container, false)
    }
}// Required empty public constructor
