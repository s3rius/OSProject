package com.example.s3rius.surveyclient.fragments;


import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.s3rius.surveyclient.R;
import com.example.s3rius.surveyclient.surveypac.Question;
import com.example.s3rius.surveyclient.surveypac.Survey;

import java.util.ArrayList;


public class CreateSurveyFragment extends ListFragment {


    private Survey survey = null;

    public CreateSurveyFragment() {
        // Required empty public constructor
    }

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("new Survey");
        if(getArguments()!= null){
            Bundle args = getArguments();
            Survey survey = (Survey)args.getSerializable("survey");
            this.survey = survey;
        }
        return inflater.inflate(R.layout.fragment_create_survey, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        if(survey == null){
            survey = new Survey();
            survey.setQuestions(new ArrayList<Question>());
        }
        setListAdapter(new SurveyListAdapter(CreateSurveyFragment.this.getContext(), survey));
    }
}
