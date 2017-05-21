package com.example.s3rius.surveyclient.fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.s3rius.surveyclient.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class Top100Fragment extends ListFragment {

    protected String surveyJson;
    //    private String[] surveyNames = {"Неинтересный опрос", "Неинтересный опрос", "Неинтересный опрос", "Неинтересный опрос",
//            "Неинтересный опрос", "Неинтересный опрос",
//            "Неинтересный опрос", "Неинтересный опрос",
//            "Неинтересный опрос", "Неинтересный опрос",
//            "Неинтересный опрос", "Неинтересный опрос",
//            "Неинтересный опрос", "Неинтересный опрос"};
    ArrayList<String> surveyNames = new ArrayList<>();
    ArrayList<Integer> surveIds = new ArrayList<>();
    private String urlOfSurveys = "http://10.0.1.7:8080/survey/client/topSurveys"; // TODO: 21.05.17 IP CHANGER
    private SurveyFragment surveyFragment;

    public Top100Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        GetSurveys getSurveys;
        getSurveys = new GetSurveys();
        getSurveys.execute();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("Top surveys");
        return inflater.inflate(R.layout.fragment_top100, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        startSurvey(id);
    }

    private void startSurvey(long id) {
        surveyFragment = new SurveyFragment();
        Bundle bundle = new Bundle();
        bundle.putLong("id", surveIds.get((int) id));
        bundle.putString("title", surveyNames.get((int) id));
        surveyFragment.setArguments(bundle);
        // Create new fragment and transaction
        // consider using Java coding conventions (upper first char class names!!!)
        FragmentTransaction transaction1 = getFragmentManager().beginTransaction();
        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack
        transaction1.replace(R.id.fragment_container, surveyFragment);
        transaction1.addToBackStack(null);
        // Commit the transaction
        transaction1.commit();
    }

    private class GetSurveys extends AsyncTask<Void, Void, String> {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";
        long id = 0;

        @Override
        protected String doInBackground(Void... params) {
            // получаем данные с внешнего ресурса
            try {
                URL url = new URL(urlOfSurveys);

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
            surveyJson = resultJson;
            return resultJson;
        }

        @Override
        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);
            JSONArray dataJsonObj;
            try {
                dataJsonObj = new JSONArray(resultJson);
                for (int i = 0; i < dataJsonObj.length(); i++) {
                    JSONObject obj = dataJsonObj.getJSONObject(i);
                    surveyNames.add(obj.getString("name"));
                    surveIds.add(obj.getInt("id"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_list_item_1, surveyNames);
            setListAdapter(adapter);
        }
    }
}
