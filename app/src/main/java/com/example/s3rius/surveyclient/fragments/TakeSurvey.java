package com.example.s3rius.surveyclient.fragments;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.s3rius.surveyclient.Drawer;
import com.example.s3rius.surveyclient.R;
import com.example.s3rius.surveyclient.fragments.surveypac.Survey;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class TakeSurvey extends ListFragment {

    ArrayList<String> content = new ArrayList<>();
    ArrayList<Integer> ids = new ArrayList<>();
    boolean fromCategory = false;
    int act = -1;
    private SurveyFragment surveyFragment;
    private int categoryNo;
    private String surveys;
    private ViewGroup container;

    public TakeSurvey() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle(R.string.newest_surveys);
        this.container = container;
        if (getArguments() != null) {
            if (getArguments().getInt("act") == 0) {
                act = 0;
                categoryNo = getArguments().getInt("CatInt");
            }
            if (getArguments().getInt("act") == 1) {
                act = 1;
                surveys = getArguments().getString("surveys list");
            }
            getActivity().setTitle(getArguments().getString("title"));
        }
        return inflater.inflate(R.layout.fragment_take_survey, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (act == 0) {
            GetCatSurveys getCatSurveys = new GetCatSurveys();
            getCatSurveys.execute();
        }else if (act == 1) {
            try {
                ArrayList<Survey> surveys = new ObjectMapper().readValue(this.surveys, new TypeReference<List<Survey>>() {});
                content.clear();
                ids.clear();
                for (int i = 0; i <surveys.size() ; i++) {
                    content.add(surveys.get(i).getName());
                    ids.add(surveys.get(i).getId());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_list_item_1, content);
                setListAdapter(adapter);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            GetNewSurveys getNewSurveys = new GetNewSurveys();
            getNewSurveys.execute();
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        startSurvey(id);
    }

    private void startSurvey(long id) {
        if (((Drawer) getActivity()).isUserExist()) {
            surveyFragment = new SurveyFragment();
            Bundle bundle = new Bundle();
            bundle.putLong("id", ids.get((int) id));
            bundle.putString("title", content.get((int) id));
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
        } else {
            Toast.makeText(container.getContext(), getString(R.string.please_login_to_access), Toast.LENGTH_LONG).show();
        }
    }

    private class GetCatSurveys extends AsyncTask<Void, Void, String> {

        ProgressDialog progressDialog;
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";
        long id = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            content.clear();
            ids.clear();
            progressDialog = new ProgressDialog(container.getContext());
            progressDialog.setMessage(getString(R.string.please_wait));
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            // получаем данные с внешнего ресурса
            try {
                URL url = new URL(getString(R.string.server) + "topics/");

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
            if (progressDialog != null)
                progressDialog.dismiss();
            super.onPostExecute(strJson);
            JSONArray dataJsonObj;
            try {
                dataJsonObj = new JSONArray(resultJson);
                JSONArray surveys = dataJsonObj.getJSONObject(categoryNo).getJSONArray("surveys");
                content.clear();
                ids.clear();
                for (int i = 0; i < surveys.length(); i++) {
                    JSONObject obj = surveys.getJSONObject(i);
                    content.add(obj.get("name").toString());
                    ids.add(obj.getInt("id"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_list_item_1, content);
            setListAdapter(adapter);
        }
    }

    private class GetNewSurveys extends AsyncTask<Void, Void, String> {

        ProgressDialog progressDialog;
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";
        long id = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            content.clear();
            ids.clear();
            progressDialog = new ProgressDialog(container.getContext());
            progressDialog.setMessage(getString(R.string.please_wait));
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            // получаем данные с внешнего ресурса
            try {
                URL url = new URL(getString(R.string.server) + "topSurveys?sortBy=time&limit=20");

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
            if (progressDialog != null)
                progressDialog.dismiss();
            super.onPostExecute(strJson);
            JSONArray dataJsonObj;
            try {
                dataJsonObj = new JSONArray(resultJson);
                content.clear();
                ids.clear();
                for (int i = 0; i < dataJsonObj.length(); i++) {
                    JSONObject obj = dataJsonObj.getJSONObject(i);
                    content.add(obj.get("name").toString());
                    ids.add(obj.getInt("id"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_list_item_1, content);
            setListAdapter(adapter);
        }
    }

}

