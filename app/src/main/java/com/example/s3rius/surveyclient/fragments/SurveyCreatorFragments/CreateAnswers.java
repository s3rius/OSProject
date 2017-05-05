package com.example.s3rius.surveyclient.fragments.SurveyCreatorFragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.s3rius.surveyclient.R;
import com.example.s3rius.surveyclient.surveypac.Survey;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateAnswers extends Fragment {

Survey survey;


    public CreateAnswers() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("new Answer");
        if(getArguments()!=null){
            Bundle arguments = getArguments();
            survey = (Survey)arguments.getSerializable("survey");
        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_answers, container, false);
    }

    public Survey getSurvey() {
        return survey;
    }
}
