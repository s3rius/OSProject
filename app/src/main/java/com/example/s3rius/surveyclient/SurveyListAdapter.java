package com.example.s3rius.surveyclient;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.s3rius.surveyclient.SurveyClass.Question;

public class SurveyListAdapter extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    SurveyClass survey;

    SurveyListAdapter(Context context, SurveyClass survey){
        this.context = context;
        this.survey = survey;
    }

    @Override
    public int getCount() {
        return survey.getQuantityOfQuestions();
    }

    @Override
    public Object getItem(int position) {
        return survey.getQuestionAt(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    Question getQuestion(int position){
        return ((Question)getItem(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null){
            view = inflater.inflate(R.layout.survey_rowlayout, parent, false);
        }

        Question question = getQuestion(position);

        ((TextView)view.findViewById(R.id.questionText)).setText(question.getQuestion());
        RadioGroup rGroup = ((RadioGroup)view.findViewById(R.id.surveyRadioG));
        RadioButton radioButton;

        for (String s:
             survey.getQuestionAt(position).getAnswers()) {
            radioButton = new RadioButton(context);
            radioButton.setText(s);
            rGroup.addView(radioButton);
        }

        return view;
    }
}
