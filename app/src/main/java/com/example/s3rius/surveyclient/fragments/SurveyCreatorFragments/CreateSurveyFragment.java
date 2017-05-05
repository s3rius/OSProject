package com.example.s3rius.surveyclient.fragments.SurveyCreatorFragments;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.s3rius.surveyclient.R;
import com.example.s3rius.surveyclient.surveypac.Question;
import com.example.s3rius.surveyclient.surveypac.Survey;

import java.util.ArrayList;


public class CreateSurveyFragment extends ListFragment{


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
        View v = inflater.inflate(R.layout.fragment_create_survey, container, false);
        FloatingActionButton button = (FloatingActionButton)v.findViewById(R.id.commitSurvey);
        getActivity().setTitle("new Survey");
        button.setVisibility(View.GONE);
        if(getArguments()!= null){
            button.setVisibility(View.VISIBLE);
            Bundle args = getArguments();
            Survey survey = (Survey)args.getSerializable("survey");
            this.survey = survey;
        }
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(survey == null){
            survey = new Survey();
            survey.setQuestions(new ArrayList<Question>());
        }
        setListAdapter(new SurveyListAdapterforCreation(CreateSurveyFragment.this.getContext(), survey));
    }
}
