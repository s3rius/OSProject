package com.example.s3rius.surveyclient.fragments.SurveyCreatorFragments;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.s3rius.surveyclient.R;
import com.example.s3rius.surveyclient.fragments.surveypac.Survey;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateQuestion extends Fragment {

    Survey survey;
    FloatingActionButton button;
    int questionInt;
    EditText question;
    public CreateQuestion() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle(R.string.new_question);
        if (getArguments() != null) {
            Bundle arguments = getArguments();
            survey = (Survey) arguments.getSerializable("survey");
            if (arguments.containsKey("questionInt")) {
                questionInt = arguments.getInt("questionInt");
                View InputFragmentView = inflater.inflate(R.layout.fragment_create_question, container, false);
                button = (FloatingActionButton) InputFragmentView.findViewById(R.id.addQuestionDone);
                question = (EditText) InputFragmentView.findViewById(R.id.newQuestText);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!question.getText().toString().equals("")) {
                            survey.getQuestions().get(questionInt).setName(question.getText().toString());
                            getFragmentManager().popBackStack();
                        } else {
                            Toast.makeText(container.getContext(), "Please enter the question", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                return InputFragmentView;
            }
        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_question, container, false);
    }

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }
}
