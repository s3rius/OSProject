package com.example.s3rius.surveyclient.fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.s3rius.surveyclient.R;
import com.example.s3rius.surveyclient.fragments.surveypac.Answer;
import com.example.s3rius.surveyclient.fragments.surveypac.Question;
import com.example.s3rius.surveyclient.fragments.surveypac.Survey;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class SurveyFragment extends ListFragment {

    public static String LOG_TAG = "my_log";
    private static Survey survey;
    private String[] lol = null;
    private long id;
    private String title;
    private String connectURL = null; // TODO: 22.05.17 Change IP.


    public SurveyFragment() {
        // Required empty public constructor
    }

    public static Survey getSurvey() {
        return survey;
    }


    public static void setSurvey(Survey survey) {
        SurveyFragment.survey = survey;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (getArguments() != null) {
            Bundle arguments = getArguments();
            id = arguments.getLong("id");
            title = arguments.getString("title");
            getActivity().setTitle(title);
        }
        return inflater.inflate(R.layout.fragment_survey, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        connectURL = getString(R.string.server) + "survey?id=";
        ParseSurvey parseSurvey;
        parseSurvey = new ParseSurvey(id);
        parseSurvey.execute();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        //ArrayList<String> content = new ArrayList<>(Arrays.asList(lol));
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
        //       android.R.layout.simple_list_item_1, content);
        //setListAdapter(adapter);
    }

    @Nullable
    @Override
    public View getView() {
        return super.getView();
    }


    private class ParseSurvey extends AsyncTask<Void, Void, String> {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";
        long id = 0;

        private ParseSurvey(long id) {
            this.id = id;
        }

        @Override
        protected String doInBackground(Void... params) {
            // получаем данные с внешнего ресурса
            try {
                URL url = new URL(connectURL + id);

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
//                resultJson = "{\"name\":\"test1\",\"questions\":[{\"name\":\"question1\",\"answers\":[{\"name\":\"answer11\",\"answered\":\"2\"},{\"name\":\"answer12\",\"answered\":\"1\"},{\"name\":\"answer13\",\"answered\":\"2\"},{\"name\":\"answer14\",\"answered\":\"1\"}]},{\"name\":\"question2\",\"answers\":[{\"name\":\"answer21\",\"answered\":\"2\"},{\"name\":\"answer22\",\"answered\":\"1\"},{\"name\":\"answer23\",\"answered\":\"2\"},{\"name\":\"answer24\",\"answered\":\"1\"}]},{\"name\":\"question3\",\"answers\":[{\"name\":\"answer31\",\"answered\":\"2\"},{\"name\":\"answer32\",\"answered\":\"1\"},{\"name\":\"answer33\",\"answered\":\"2\"},{\"name\":\"answer34\",\"answered\":\"1\"}]},{\"name\":\"question4\",\"answers\":[{\"name\":\"answer41\",\"answered\":\"2\"},{\"name\":\"answer42\",\"answered\":\"1\"},{\"name\":\"answer43\",\"answered\":\"2\"},{\"name\":\"answer44\",\"answered\":\"1\"}]}]}";

            } catch (Exception e) {
                e.printStackTrace();
            }
            return resultJson;
        }

        @Override
        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);
            // выводим целиком полученную json-строку
            Log.d(LOG_TAG, strJson);
            JSONObject dataJsonObj;
            List<Question> questions = new ArrayList<>();
            //List<User> users = new ArrayList<>();
            try {
                dataJsonObj = new JSONObject(strJson);
                String surveyName = dataJsonObj.getString("name");
                // JSONArray usersArray = dataJsonObj.getJSONArray("users");
                JSONArray questionsJson = dataJsonObj.getJSONArray("questions");
//                for (int i = 0; i < usersArray.length(); i++) {
//                    User user = new User();
//                    JSONObject userObj = usersArray.getJSONObject(i);
//                    user.setName(userObj.getString("name"));
//                    users.add(user);
//                }
                for (int i = 0; i < questionsJson.length(); i++) {
                    JSONObject question = questionsJson.getJSONObject(i);
                    String questionName = question.getString("name");
                    JSONArray answersJson = question.getJSONArray("answers");
                    List<Answer> answers = new ArrayList<>();
                    for (int j = 0; j < answersJson.length(); j++) {
                        JSONObject answer1 = answersJson.getJSONObject(j);
                        answers.add(new Answer(answer1.getString("name"), null));
                    }
                    questions.add(new Question(questionName, answers));
                }
                Survey survey1 = new Survey(surveyName, "comment", questions, null);
                setSurvey(survey1);
                setListAdapter(new SurveyListAdapter(SurveyFragment.this.getContext(), SurveyFragment.getSurvey()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
