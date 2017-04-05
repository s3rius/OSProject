package com.example.s3rius.surveyclient.fragments;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.s3rius.surveyclient.R;
import com.example.s3rius.surveyclient.surveypac.Question;
import com.example.s3rius.surveyclient.surveypac.Survey;


public class SurveyListAdapter extends ArrayAdapter<Question> {

    Context context;
    LayoutInflater inflater;
    Survey survey;

    SurveyListAdapter(Context context, Survey survey) {
        super(context, R.layout.survey_rowlayout, survey.getQuestions());
        this.context = context;
        this.survey = survey;
    }

    @Override
    public int getCount() {
        return getSurvey().getQuestions().size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.survey_rowlayout, null);
        TextView surveytext = (TextView) v.findViewById(R.id.questionText);
        RadioGroup answers = (RadioGroup) v.findViewById(R.id.surveyRadioG);

        surveytext.setText(getSurvey().getQuestions().get(position).getName());

        for (int i = 0; i < getSurvey().getQuestions().get(position).getAnswers().size(); i++) {
            RadioButton radioButton = new RadioButton(context);
            radioButton.setText(getSurvey().getQuestions().get(position).getAnswers().get(i).getName());
            answers.addView(radioButton);
        }
        return v;
    }

    public Survey getSurvey() {
        return survey;
    }


}
