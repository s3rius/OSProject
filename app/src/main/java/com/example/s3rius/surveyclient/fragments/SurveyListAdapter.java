package com.example.s3rius.surveyclient.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.s3rius.surveyclient.Drawer;
import com.example.s3rius.surveyclient.R;
import com.example.s3rius.surveyclient.fragments.surveypac.Question;
import com.example.s3rius.surveyclient.fragments.surveypac.Survey;


public class SurveyListAdapter extends ArrayAdapter<Question> {

    boolean done;
    private Context context;
    private LayoutInflater inflater;
    private Survey survey;
    private boolean nocomment = false;

    SurveyListAdapter(Context context, Survey survey, Boolean done) {
        super(context, R.layout.survey_rowlayout, survey.getQuestions());
        this.context = context;
        this.survey = survey;
        this.done = done;
    }

    @Override
    public int getCount() {
        int rows;
        if (getSurvey().getComment() == null) {
            nocomment = true;
            rows = getSurvey().getQuestions().size();
        } else {
            rows = getSurvey().getQuestions().size() + 1;
        }
        if (done)
            rows++;
        return rows;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.survey_rowlayout, null);
        if (nocomment) {
            if (position != survey.getQuestions().size()) {
                TextView surveytext = (TextView) v.findViewById(R.id.questionText);
                RadioGroup answers = (RadioGroup) v.findViewById(R.id.surveyRadioG);
                surveytext.setText(getSurvey().getQuestions().get(position).getName());
                for (int i = 0; i < getSurvey().getQuestions().get(position).getAnswers().size(); i++) {
                    RadioButton radioButton = new RadioButton(context);
                    radioButton.setText(getSurvey().getQuestions().get(position).getAnswers().get(i).getName());
                    if (survey.getQuestions().get(position).getAnswers().get(i).isIsAnswered())
                        radioButton.setChecked(true);
                    answers.addView(radioButton);
                }
                answers.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        for (int i = 0; i < group.getChildCount(); i++) {
                            if (((RadioButton) group.getChildAt(i)).isChecked()) {
                                survey.getQuestions().get(position).uncheck();
                                survey.getQuestions().get(position).getAnswers().get(i).setIsAnswered(true);
                                break;
                            }
                        }
                    }
                });
            }
            if (done) {
                if (position == survey.getQuestions().size()) {
                    v = inflater.inflate(R.layout.already_done_survey_rowlayout, null);
                    Button watchStatistics = (Button) v.findViewById(R.id.complete_in_survey_button);
                    watchStatistics.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            StatisticFragment statisticFragment = new StatisticFragment();
                            Bundle bundle = new Bundle();
                            bundle.putInt("id", survey.getId());
                            statisticFragment.setArguments(bundle);
                            android.support.v4.app.FragmentTransaction transaction = ((Drawer) context).getSupportFragmentManager().beginTransaction();
                            ((Drawer) context).getSupportFragmentManager().popBackStack();
                            transaction.replace(R.id.fragment_container, statisticFragment);
                            transaction.commit();
                            transaction.addToBackStack(null);
                        }
                    });
                }
            }
            return v;
        } else {
            if (position != 0) {
                if (position != survey.getQuestions().size() + 1) {
                    TextView surveytext = (TextView) v.findViewById(R.id.questionText);
                    RadioGroup answers = (RadioGroup) v.findViewById(R.id.surveyRadioG);
                    surveytext.setText(getSurvey().getQuestions().get(position - 1).getName());
                    for (int i = 0; i < getSurvey().getQuestions().get(position - 1).getAnswers().size(); i++) {
                        RadioButton radioButton = new RadioButton(context);
                        radioButton.setText(getSurvey().getQuestions().get(position - 1).getAnswers().get(i).getName());
                        if (survey.getQuestions().get(position - 1).getAnswers().get(i).isIsAnswered())
                            radioButton.setChecked(true);
                        answers.addView(radioButton);
                    }
                    answers.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            for (int i = 0; i < group.getChildCount(); i++) {
                                if (((RadioButton) group.getChildAt(i)).isChecked()) {
                                    survey.getQuestions().get(position - 1).uncheck();
                                    survey.getQuestions().get(position - 1).getAnswers().get(i).setIsAnswered(true);
                                    break;
                                }
                            }
                        }
                    });
                } else {
                    v = inflater.inflate(R.layout.already_done_survey_rowlayout, null);
                    Button watchStatistics = (Button) v.findViewById(R.id.complete_in_survey_button);
                    watchStatistics.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            StatisticFragment statisticFragment = new StatisticFragment();
                            Bundle bundle = new Bundle();
                            bundle.putInt("id", survey.getId());
                            statisticFragment.setArguments(bundle);
                            android.support.v4.app.FragmentTransaction transaction = ((Drawer) context).getSupportFragmentManager().beginTransaction();
                            ((Drawer) context).getSupportFragmentManager().popBackStack();
                            transaction.replace(R.id.fragment_container, statisticFragment);
                            transaction.commit();
                            transaction.addToBackStack(null);
                        }
                    });
                }
                return v;
            } else {
                TextView comment = (TextView) v.findViewById(R.id.questionText);
                comment.setText(context.getString(R.string.inSurveyComment) + survey.getComment());
                return v;
            }
        }
    }

    public Survey getSurvey() {
        return survey;
    }


}