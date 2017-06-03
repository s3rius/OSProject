package com.example.s3rius.surveyclient.fragments.SurveyCreatorFragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.s3rius.surveyclient.R;
import com.example.s3rius.surveyclient.fragments.surveypac.Question;
import com.example.s3rius.surveyclient.fragments.surveypac.Survey;

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle args = getArguments();
            Survey survey = (Survey) args.getSerializable("survey");
            this.survey = survey;
        }
            if (survey!= null && this.survey.getQuestions().get(this.survey.getQuestions().size() - 1).getAnswers().size() == 0) {
            this.survey.getQuestions().remove(this.survey.getQuestions().size() - 1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_survey, container, false);
        FloatingActionButton completeButton = (FloatingActionButton) view.findViewById(R.id.commitSurvey);
        FloatingActionButton changeButton = (FloatingActionButton) view.findViewById(R.id.changeSurvey);
        getActivity().setTitle(R.string.new_survey);
        completeButton.setVisibility(View.GONE);
        changeButton.setVisibility(View.GONE);
        if (getArguments() != null) {
            completeButton.setVisibility(View.VISIBLE);
            changeButton.setVisibility(View.VISIBLE);
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (survey == null) {
            survey = new Survey();
            survey.setQuestions(new ArrayList<Question>());
        }
        setListAdapter(new SurveyListAdapterforCreation(CreateSurveyFragment.this.getContext(), survey));
    }
}
