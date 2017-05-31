package com.example.s3rius.surveyclient.fragments;


import android.app.ProgressDialog;
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

public class CategoryFragment extends ListFragment {
    ArrayList<String> content = new ArrayList<>();
    ViewGroup container;

    public CategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("Categories");
        this.container = container;
        return inflater.inflate(R.layout.fragment_category, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        GetCats getCats = new GetCats();
        getCats.execute();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        takeSurveysByCategory(content.get(((int) id)), (int) id);
    }

    public void takeSurveysByCategory(String category, int id) {
        TakeSurvey surveyFragment = new TakeSurvey();
        Bundle bundle = new Bundle();
        bundle.putString("title", category);
        bundle.putInt("CatInt", id);
        surveyFragment.setArguments(bundle);
        // Create new fragment and transaction
        // consider using Java coding conventions (upper first char class names!!!)
        FragmentTransaction transaction1 = getFragmentManager().beginTransaction();
        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack
        transaction1.replace(R.id.fragment_container, surveyFragment);
        // Commit the transaction
        transaction1.commit();
        transaction1.addToBackStack(null);
    }

    private class GetCats extends AsyncTask<Void, Void, String> {

        ProgressDialog progressDialog;
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";
        long id = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            content.clear();
            progressDialog = new ProgressDialog(container.getContext());
            progressDialog.setMessage("Please Wait....");
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
                for (int i = 0; i < dataJsonObj.length(); i++) {
                    JSONObject obj = dataJsonObj.getJSONObject(i);
                    content.add(obj.get("name").toString());
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
