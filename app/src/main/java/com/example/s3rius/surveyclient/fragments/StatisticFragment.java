package com.example.s3rius.surveyclient.fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.s3rius.surveyclient.R;
import com.example.s3rius.surveyclient.fragments.surveypac.Question;
import com.example.s3rius.surveyclient.fragments.surveypac.Survey;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class StatisticFragment extends ListFragment {

    Survey survey;
    int id;

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    public StatisticFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(getArguments()!=null){
            id = getArguments().getInt("id");
        }
        return inflater.inflate(R.layout.fragment_statistic, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        ParseStatistics statistics = new ParseStatistics(id);
        statistics.execute();
    }

    private class ParseStatistics extends AsyncTask<Void, Void, String> {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";
        long id = 0;

        private ParseStatistics(long id) {
            this.id = id;
        }

        @Override
        protected String doInBackground(Void... params) {
            // получаем данные с внешнего ресурса
            try {
                URL url = new URL(getString(R.string.server) + "survey?id=" + id);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;

                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                resultJson = buffer.toString();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return resultJson;
        }

        @Override
        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);
            // выводим целиком полученную json-строку
            Log.d("LOG_TAG", strJson);
            JSONObject dataJsonObj;
            List<Question> questions = new ArrayList<>();
            try {
                Survey survey = new ObjectMapper().readValue(resultJson, Survey.class);
                setSurvey(survey);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //List<User> users = new ArrayList<>();
//            try {
//                dataJsonObj = new JSONObject(strJson);
//                String surveyName = dataJsonObj.getString("name");
//                // JSONArray usersArray = dataJsonObj.getJSONArray("users");
//                JSONArray questionsJson = dataJsonObj.getJSONArray("questions");
////                for (int i = 0; i < usersArray.length(); i++) {
//                    User user = new User();
//                    JSONObject userObj = usersArray.getJSONObject(i);
//                    user.setName(userObj.getString("name"));
//                    users.add(user);
//                }
//                for (int i = 0; i < questionsJson.length(); i++) {
//                    JSONObject question = questionsJson.getJSONObject(i);
//                    String questionName = question.getString("name");
//                    JSONArray answersJson = question.getJSONArray("answers");
//                    List<Answer> answers = new ArrayList<>();
//                    for (int j = 0; j < answersJson.length(); j++) {
//                        JSONObject answer1 = answersJson.getJSONObject(j);
//                        answers.add(new Answer(answer1.getString("name"), null));
//                    }
//                    questions.add(new Question(questionName, answers));
//                }
//                Survey survey1 = new Survey(surveyName, "comment", questions, null);
//                setSurvey(survey1);
            setListAdapter(new StatisticListAdapter(StatisticFragment.this.getContext(), StatisticFragment.this.survey));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
        }
    }

}
