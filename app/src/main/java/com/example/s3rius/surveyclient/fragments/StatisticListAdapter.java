package com.example.s3rius.surveyclient.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.s3rius.surveyclient.Drawer;
import com.example.s3rius.surveyclient.R;
import com.example.s3rius.surveyclient.fragments.surveypac.Question;
import com.example.s3rius.surveyclient.fragments.surveypac.Survey;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.util.Collections;


public class StatisticListAdapter extends ArrayAdapter<Question> {

    private Context context;
    private Survey survey;
    Bitmap madeByPic = null;

    StatisticListAdapter(Context context, Survey survey) {
        super(context, R.layout.survey_rowlayout, survey.getQuestions());
        this.context = context;
        this.survey = survey;
    }

    StatisticListAdapter(Context context, Survey survey, Bitmap madeByPic) {
        super(context, R.layout.survey_rowlayout, survey.getQuestions());
        this.context = context;
        this.survey = survey;
        this.madeByPic = madeByPic;
    }

    @Override
    public int getCount() {
        return getSurvey().getQuestions().size() + 1;

    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(position < getSurvey().getQuestions().size()) {
            v = inflater.inflate(R.layout.statistic_rowlayout, null);
            TextView questionName = (TextView) v.findViewById(R.id.StatQuestionText);
            final PieChart pieChart = (PieChart) v.findViewById(R.id.graph);
            questionName.setText(getSurvey().getQuestions().get(position).getName());
            Button nextAnswer = (Button) v.findViewById(R.id.nextReview);
            nextAnswer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        pieChart.setCurrentItem(pieChart.getCurrentItem() - 1);
                    } catch (IndexOutOfBoundsException e) {
                        pieChart.setCurrentItem(getSurvey().getQuestions().get(position).getAnswers().size() - 1);
                    }

                }
            });
            Button prevAnswer = (Button) v.findViewById(R.id.prevReview);
            prevAnswer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        pieChart.setCurrentItem(pieChart.getCurrentItem() + 1);
                    } catch (IndexOutOfBoundsException e) {
                        pieChart.setCurrentItem(0);
                    }
                }
            });
            if (getSurvey().getQuestions().get(position).getAnswers().size() == 1) {
                nextAnswer.setVisibility(View.GONE);
                prevAnswer.setVisibility(View.GONE);
            }
            boolean good = true;
            int[] colors = new int[5];
            colors[0] = Color.parseColor("#FE6DA8");
            colors[1] = Color.parseColor("#56B7F1");
            colors[2] = Color.parseColor("#CDA67F");
            colors[3] = Color.parseColor("#FED70E");
            colors[4] = Color.parseColor("#FF8F8F8F");
            int other = 0;
            Collections.sort(getSurvey().getQuestions().get(position).getAnswers());
            for (int i = 0; i < getSurvey().getQuestions().get(position).getAnswers().size(); i++) {
                if (i < 4)
                    pieChart.addPieSlice(new PieModel(
                            getSurvey().getQuestions().get(position).getAnswers().get(i).getName(),
                            getSurvey().getQuestions().get(position).getAnswers().get(i).getUsersAnswered(), colors[i]));
                else {
                    good = false;
                    other += getSurvey().getQuestions().get(position).getAnswers().get(i).getId();
                }
            }
            if (!good)
                pieChart.addPieSlice(new PieModel(context.getResources().getString(R.string.other),
                        other, colors[4]));
            pieChart.startAnimation();
            pieChart.setUsePieRotation(false);
        }else {
            v = inflater.inflate(R.layout.made_by_rowlayout, null);
            if(madeByPic != null) {
                ImageView profilePic = (ImageView) v.findViewById(R.id.made_by_pic);
                profilePic.setImageBitmap(madeByPic);
            }
            TextView name = (TextView)v.findViewById(R.id.made_by_name);
            name.setText(survey.getCreator().getName());
            RelativeLayout paragraph = (RelativeLayout)v.findViewById(R.id.made_by_layout);
            paragraph.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProfileFragment fragment = new ProfileFragment();
                    Bundle bundle = new Bundle();

                    bundle.putString("username", survey.getCreator().getName());
                    bundle.putString("login", survey.getCreator().getLogin());
                    fragment.setArguments(bundle);
                    android.support.v4.app.FragmentTransaction fragmentTransaction = ((Drawer)context).getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, fragment);
                    fragmentTransaction.commit();
                    fragmentTransaction.addToBackStack(null);
                }
            });
        }
        return v;
    }

    public Survey getSurvey() {
        return survey;
    }


}
