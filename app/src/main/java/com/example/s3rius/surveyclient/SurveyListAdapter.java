package com.example.s3rius.surveyclient;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.s3rius.surveyclient.SurveyClass.Question;

public class SurveyListAdapter extends ArrayAdapter<Question> {

    Context context;
    LayoutInflater inflater;
    SurveyClass survey;

    SurveyListAdapter(Context context, SurveyClass survey){
        super(context, R.layout.survey_rowlayout, survey.getQuestions());
        this.context = context;
        this.survey = survey;
    }

    @Override
    public int getCount() {
        return getSurvey().getQuantityOfQuestions();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.survey_rowlayout, null);
        TextView surveytext = (TextView)v.findViewById(R.id.questionText);
        RadioGroup answers = (RadioGroup)v.findViewById(R.id.surveyRadioG);
        surveytext.setText(getSurvey().getQuestionAt(position).getQuestion());
        for (int i = 0; i < getSurvey().getQuestionAt(position).getQuantityOfAnswers(); i++) {
            RadioButton radioButton = new RadioButton(context);
            radioButton.setText(getSurvey().getQuestionAt(position).getAnswerAt(i));
            answers.addView(radioButton);
        }
        return v;
    }

    public SurveyClass getSurvey() {
        return survey;
    }

}
