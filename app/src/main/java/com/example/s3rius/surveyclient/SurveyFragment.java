package com.example.s3rius.surveyclient;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class SurveyFragment extends ListFragment {

    public static String LOG_TAG = "my_log";

    String[] lol = null;
    long id;
    static SurveyClass survey;

    public SurveyFragment() {
        // Required empty public constructor
    }

    public static SurveyClass getSurvey() {
        return survey;
    }

    public static void setSurvey(SurveyClass survey) {
        SurveyFragment.survey = survey;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_survey, container, false);

       // if(getArguments()!= null){
       //     id = savedInstanceState.getLong("id");
       // }
        // Inflate the layout for this fragment

    }

    @Override
    public void onStart() {
        super.onStart();
        ParseSurvey parseSurvey;
        parseSurvey = new ParseSurvey(id);

        parseSurvey.execute();
        SurveyListAdapter adapter = new SurveyListAdapter(getActivity(), parseSurvey.getSurvey());
        setListAdapter(adapter);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        //ArrayList<String> content = new ArrayList<>(Arrays.asList(lol));
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
        //       android.R.layout.simple_list_item_1, content);
        //setListAdapter(adapter);
    }
    private class ParseSurvey extends AsyncTask<Void, Void, String> {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";
        long id = 0;
        private SurveyClass survey = new SurveyClass();

        public ParseSurvey(long id){
            this.id = id;
        }

        public SurveyClass getSurvey() {
            return survey;
        }

        public void setSurvey(SurveyClass survey) {
            this.survey = survey;
        }

        @Override
        protected String doInBackground(Void... params) {
            // получаем данные с внешнего ресурса
            try {
                URL url = new URL("http://46.0.77.77:8080/devcolibri-rest/myservice/surveyId=" + id);

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
            Log.d(LOG_TAG, strJson);
            JSONObject dataJsonObj = null;
            SurveyFragment.setSurvey(new SurveyClass());
            try {
                dataJsonObj = new JSONObject(strJson);
                JSONArray questions = dataJsonObj.getJSONArray("questions");
                // 2. перебираем и выводим контакты каждого друга
                for (int i = 0; i < questions.length(); i++) {
                    JSONObject question = questions.getJSONObject(i);
                    String question1 = question.getString("name");
                    JSONArray answers = question.getJSONArray("answers");
                    String[] answers1 = new String[answers.length()];
                    for (int j = 0; j < answers.length(); j++) {
                        JSONObject answer1 = answers.getJSONObject(j);
                        answers1[i] = answer1.getString("name");
                    }
                    SurveyFragment.getSurvey().addquestion(question1, answers1);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
