package com.example.s3rius.surveyclient.fragments.SurveyCreatorFragments


import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast

import com.example.s3rius.surveyclient.R
import com.example.s3rius.surveyclient.fragments.surveypac.Answer
import com.example.s3rius.surveyclient.fragments.surveypac.Survey

/**
 * A simple [Fragment] subclass.
 */
class CreateAnswers : Fragment() {

    var survey: Survey = null!!
    internal var questInt: Int = 0
    internal var ansInt: Int = 0
    internal var button: FloatingActionButton
    internal var addMore: FloatingActionButton
    internal var backButton: FloatingActionButton
    internal var answer: EditText


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        activity.setTitle(R.string.new_answer)
        if (arguments != null) {
            val arguments = arguments
            survey = arguments.getSerializable("survey") as Survey
            if (arguments.containsKey("questInt")) {
                questInt = arguments.getInt("questInt")
                val act = arguments.getInt("act")
                val InputFragmentView = inflater!!.inflate(R.layout.fragment_create_answers, container, false)
                button = InputFragmentView.findViewById(R.id.addAnsButtonDone) as FloatingActionButton
                addMore = InputFragmentView.findViewById(R.id.addOneAnsButtonDone) as FloatingActionButton
                backButton = InputFragmentView.findViewById(R.id.answerBackButton) as FloatingActionButton
                answer = InputFragmentView.findViewById(R.id.new_Answer) as EditText
                if (act == 0) {
                    ansInt = arguments.getInt("ansInt")
                    addMore.hide()
                    button.setOnClickListener {
                        if (answer.text.toString() != "") {
                            survey.questions[questInt].answers[ansInt].name = answer.text.toString()
                            fragmentManager.popBackStack()
                        } else {
                            Toast.makeText(container!!.context, "Please enter the answer", Toast.LENGTH_LONG).show()
                        }
                    }
                    backButton.setOnClickListener { fragmentManager.popBackStack() }
                } else {
                    button.setOnClickListener {
                        if (answer.text.toString() != "") {
                            survey.questions[questInt].answers.add(Answer(answer.text.toString(), 0))
                            fragmentManager.popBackStack()
                        } else {
                            Toast.makeText(container!!.context, "Please enter the answer", Toast.LENGTH_LONG).show()
                        }
                    }

                    backButton.setOnClickListener { fragmentManager.popBackStack() }

                    addMore.setOnClickListener {
                        if (answer.text.toString() != "") {
                            survey.questions[questInt].answers.add(Answer(answer.text.toString(), 0))
                            answer.setText("")
                        } else {
                            Toast.makeText(container!!.context, "Please enter the answer", Toast.LENGTH_LONG).show()
                        }
                    }
                }
                return InputFragmentView
            }
        }
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_create_answers, container, false)
    }
}// Required empty public constructor
