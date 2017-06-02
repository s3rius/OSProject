package com.example.s3rius.surveyclient.fragments;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.s3rius.surveyclient.R;
import com.example.s3rius.surveyclient.fragments.surveypac.Question;
import com.example.s3rius.surveyclient.fragments.surveypac.Survey;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;


public class StatisticListAdapter extends ArrayAdapter<Question> {

    private Context context;
    private Survey survey;

    StatisticListAdapter(Context context, Survey survey) {
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
        v = inflater.inflate(R.layout.statistic_rowlayout, null);
        TextView questionName = (TextView)v.findViewById(R.id.StatQuestionText);
        PieChart pieChart = (PieChart)v.findViewById(R.id.graph);
        questionName.setText(getSurvey().getQuestions().get(position).getName());
        boolean good = true;
        int[] colors = new int[5];
        colors[0] = Color.parseColor("#FE6DA8");
        colors[1] = Color.parseColor("#56B7F1");
        colors[2] = Color.parseColor("#CDA67F");
        colors[3] = Color.parseColor("#FED70E");
        colors[4] = Color.parseColor("#FF8F8F8F");
        int other = 0;
        for (int i = 0; i < getSurvey().getQuestions().get(position).getAnswers().size(); i++) {
            if(i < 4)
            pieChart.addPieSlice(new PieModel(
                    getSurvey().getQuestions().get(position).getAnswers().get(i).getName(),
                    getSurvey().getQuestions().get(position).getAnswers().get(i).getId(),colors[i]));
            else {
                good = false;
                other += getSurvey().getQuestions().get(position).getAnswers().get(i).getId();
            }
        }
        if(!good)
            pieChart.addPieSlice(new PieModel(context.getResources().getString(R.string.other),
                    other, colors[4]));
        pieChart.startAnimation();
       return v;
    }

    public Survey getSurvey() {
        return survey;
    }


}
