package com.example.s3rius.surveyclient.fragments;


import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.s3rius.surveyclient.R;
import com.example.s3rius.surveyclient.surveypac.Survey;


public class CreateSurveyFragment extends ListFragment {


    private Survey survey;

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
        return inflater.inflate(R.layout.fragment_create_survey, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        survey = new Survey();
        setListAdapter(new SurveyListAdapter(CreateSurveyFragment.this.getContext(), survey));
    }
}
