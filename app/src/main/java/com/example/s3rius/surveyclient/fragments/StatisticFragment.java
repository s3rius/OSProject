package com.example.s3rius.surveyclient.fragments;


import android.app.ProgressDialog;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.s3rius.surveyclient.R;
import com.example.s3rius.surveyclient.fragments.surveypac.Survey;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A simple {@link Fragment} subclass.
 */
public class StatisticFragment extends ListFragment {

    Survey survey;
    int id;
    ViewGroup container;

    public StatisticFragment() {
        // Required empty public constructor
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.container = container;
        getActivity().setTitle(R.string.statistics);
        if (getArguments() != null) {
            id = getArguments().getInt("id");
        }
        return inflater.inflate(R.layout.fragment_statistic, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        try {
            params.put("id", id);
            params.put("options", new ObjectMapper().writeValueAsString(new String[]{"USERS", "QUESTIONS", "CREATOR", "CATEGORY"}));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        client.get(getString(R.string.server) + "survey", params, new AsyncHttpResponseHandler() {
            ProgressDialog progressDialog = null;

            @Override
            public void onStart() {
                super.onStart();
                progressDialog = new ProgressDialog(container.getContext());
                progressDialog.setMessage(getString(R.string.please_wait));
                progressDialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(progressDialog!=null)
                    progressDialog.dismiss();
//                try {
//                    Survey survey = new ObjectMapper().readValue(new String(responseBody), Survey.class);
//                    setSurvey(survey);
//                    setListAdapter(new StatisticListAdapter(StatisticFragment.this.getContext(), survey));
//                } catch (IOException e) {
//                    Toast.makeText(container.getContext(), getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
//                    getFragmentManager().popBackStack();
//                    e.printStackTrace();
//                }
                try {
                Survey survey = new ObjectMapper().readValue(new String(responseBody), Survey.class);
                setSurvey(survey);
                AsyncHttpClient client = new AsyncHttpClient();

                client.get(getString(R.string.server) + "img?id=" + survey.getCreator().getLogin(), new FileAsyncHttpResponseHandler(container.getContext()) {
                    ProgressDialog progressDialog = null;
                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                        if(progressDialog!=null)
                            progressDialog.dismiss();
                        setListAdapter(new StatisticListAdapter(StatisticFragment.this.getContext(), StatisticFragment.this.survey));
                    }

                    @Override
                    public void onStart() {
                        super.onStart();
                        progressDialog = new ProgressDialog(container.getContext());
                        progressDialog.setMessage(getString(R.string.please_wait));
                        progressDialog.show();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, final File file) {
                        if(progressDialog!=null)
                            progressDialog.dismiss();
                        setListAdapter(new StatisticListAdapter(StatisticFragment.this.getContext(), StatisticFragment.this.survey, BitmapFactory.decodeFile(file.getPath())));
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
            setListAdapter(new StatisticListAdapter(StatisticFragment.this.getContext(), StatisticFragment.this.survey));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if(progressDialog!=null)
                    progressDialog.dismiss();
                Toast.makeText(container.getContext(), getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                getFragmentManager().popBackStack();
            }
        });
//        ParseStatistics statistics = new ParseStatistics(id);
//        statistics.execute();
    }

//    private class ParseStatistics extends AsyncTask<Void, Void, String> {
//
//        HttpURLConnection urlConnection = null;
//        BufferedReader reader = null;
//        String resultJson = "";
//        long id = 0;
//
//        private ParseStatistics(long id) {
//            this.id = id;
//        }
//
//        @Override
//        protected String doInBackground(Void... params) {
//            // получаем данные с внешнего ресурса
//            try {
//                URL url = new URL(getString(R.string.server) + "survey?id=" + id);
//
//                urlConnection = (HttpURLConnection) url.openConnection();
//                urlConnection.setRequestMethod("GET");
//                urlConnection.connect();
//
//                InputStream inputStream = urlConnection.getInputStream();
//                StringBuffer buffer = new StringBuffer();
//
//                reader = new BufferedReader(new InputStreamReader(inputStream));
//
//                String line;
//
//                while ((line = reader.readLine()) != null) {
//                    buffer.append(line);
//                }
//
//                resultJson = buffer.toString();
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return resultJson;
//        }
//
//        @Override
//        protected void onPostExecute(String strJson) {
//            super.onPostExecute(strJson);
//            try {
//                Survey survey = new ObjectMapper().readValue(resultJson, Survey.class);
//                setSurvey(survey);
//                AsyncHttpClient client = new AsyncHttpClient();
//                client.get(getString(R.string.server) + "img?id=" + survey.getCreator().getLogin(), new FileAsyncHttpResponseHandler(container.getContext()) {
//                    @Override
//                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
//                        setListAdapter(new StatisticListAdapter(StatisticFragment.this.getContext(), StatisticFragment.this.survey));
//                    }
//
//                    @Override
//                    public void onStart() {
//                        super.onStart();
//                    }
//
//                    @Override
//                    public void onSuccess(int statusCode, Header[] headers, final File file) {
//                        setListAdapter(new StatisticListAdapter(StatisticFragment.this.getContext(), StatisticFragment.this.survey, BitmapFactory.decodeFile(file.getPath())));
//                    }
//                });
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            setListAdapter(new StatisticListAdapter(StatisticFragment.this.getContext(), StatisticFragment.this.survey));
//        }
//    }

}
