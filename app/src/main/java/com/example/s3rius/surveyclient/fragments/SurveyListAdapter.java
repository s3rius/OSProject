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
import com.example.s3rius.surveyclient.fragments.surveypac.Question;
import com.example.s3rius.surveyclient.fragments.surveypac.Survey;


public class SurveyListAdapter extends ArrayAdapter<Question> {

    private Context context;
    private LayoutInflater inflater;
    private Survey survey;
    private boolean nocomment = false;
    SurveyListAdapter(Context context, Survey survey) {
        super(context, R.layout.survey_rowlayout, survey.getQuestions());
        this.context = context;
        this.survey = survey;
    }

    @Override
    public int getCount() {
        if (getSurvey().getComment() == null) {
            nocomment = true;
            return getSurvey().getQuestions().size();
        }
        else {
            return getSurvey().getQuestions().size() + 1;
        }
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.survey_rowlayout, null);
        if (nocomment){
            TextView surveytext = (TextView) v.findViewById(R.id.questionText);
            RadioGroup answers = (RadioGroup) v.findViewById(R.id.surveyRadioG);
            surveytext.setText(getSurvey().getQuestions().get(position).getName());
            for (int i = 0; i < getSurvey().getQuestions().get(position).getAnswers().size(); i++) {
                RadioButton radioButton = new RadioButton(context);
                radioButton.setText(getSurvey().getQuestions().get(position).getAnswers().get(i).getName());
                if(survey.getQuestions().get(position).getAnswers().get(i).isIsAnswered())
                    radioButton.setChecked(true);
                answers.addView(radioButton);
            }
            answers.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    for (int i = 0; i < group.getChildCount(); i++) {
                        if(((RadioButton)group.getChildAt(i)).isChecked()){
                            survey.getQuestions().get(position).uncheck();
                            survey.getQuestions().get(position).getAnswers().get(i).setIsAnswered(true);
                            break;
                        }
                    }
                }
            });
            return v;
        }
        else {
            if(position != 0) {
                TextView surveytext = (TextView) v.findViewById(R.id.questionText);
                RadioGroup answers = (RadioGroup) v.findViewById(R.id.surveyRadioG);
                surveytext.setText(getSurvey().getQuestions().get(position - 1).getName());
                for (int i = 0; i < getSurvey().getQuestions().get(position - 1).getAnswers().size(); i++) {
                    RadioButton radioButton = new RadioButton(context);
                    radioButton.setText(getSurvey().getQuestions().get(position - 1).getAnswers().get(i).getName());
                    if(survey.getQuestions().get(position - 1).getAnswers().get(i).isIsAnswered())
                        radioButton.setChecked(true);
                    answers.addView(radioButton);
                }
                answers.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        for (int i = 0; i < group.getChildCount(); i++) {
                            if(((RadioButton)group.getChildAt(i)).isChecked()){
                                survey.getQuestions().get(position-1).uncheck();
                                survey.getQuestions().get(position -1).getAnswers().get(i).setIsAnswered(true);
                                break;
                            }
                        }
                    }
                });
                return v;
            }else{
                TextView comment = (TextView)v.findViewById(R.id.questionText);
                comment.setText(context.getString(R.string.inSurveyComment) + survey.getComment());
                return v;
            }
        }
    }

    public Survey getSurvey() {
        return survey;
    }


}