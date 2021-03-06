package com.example.s3rius.surveyclient.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class Top100Fragment extends ListFragment {

    protected String surveyJson;
    ArrayList<String> surveyNames = new ArrayList<>();
    ArrayList<Integer> surveIds = new ArrayList<>();
    private ViewGroup container;

    public Top100Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        String urlOfSurveys = getString(R.string.server) + "topSurveys";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("sortBy", "users");
        params.put("limit", 20);
        client.get(urlOfSurveys, params, new AsyncHttpResponseHandler() {
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
                if (progressDialog != null)
                    progressDialog.dismiss();

                JSONArray dataJsonObj;
                try {
                    surveyNames.clear();
                    surveIds.clear();
                    dataJsonObj = new JSONArray(new String(responseBody));
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

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if (progressDialog != null)
                    progressDialog.dismiss();
                Toast.makeText(container.getContext(),
                        getString(R.string.something_went_wrong),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.container = container;
        // Inflate the layout for this fragment
        getActivity().setTitle(R.string.top_surveys);
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
        if (((Drawer) getActivity()).isUserExist()) {
            SurveyFragment surveyFragment = new SurveyFragment();
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
        } else {
            Toast.makeText(container.getContext(), getString(R.string.please_login_to_access), Toast.LENGTH_LONG).show();

        }
    }

//    private class GetSurveys extends AsyncTask<Void, Void, String> {
//
//        ProgressDialog progressDialog;
//        HttpURLConnection urlConnection = null;
//        BufferedReader reader = null;
//        String resultJson = "";
//        long id = 0;
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            surveyNames.clear();
//            surveIds.clear();
//            progressDialog = new ProgressDialog(container.getContext());
//            progressDialog.setMessage(getString(R.string.please_wait));
//            progressDialog.show();
//        }
//
//        @Override
//        protected String doInBackground(Void... params) {
//            // получаем данные с внешнего ресурса
//            try {
//                URL url = new URL(urlOfSurveys);
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
//            surveyJson = resultJson;
//            return resultJson;
//        }
//
//        @Override
//        protected void onPostExecute(String strJson) {
//            if(progressDialog!=null)
//                progressDialog.dismiss();
//            super.onPostExecute(strJson);
//            JSONArray dataJsonObj;
//            try {
//                dataJsonObj = new JSONArray(resultJson);
//                for (int i = 0; i < dataJsonObj.length(); i++) {
//                    JSONObject obj = dataJsonObj.getJSONObject(i);
//                    surveyNames.add(obj.getString("name"));
//                    surveIds.add(obj.getInt("id"));
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
//                    android.R.layout.simple_list_item_1, surveyNames);
//            setListAdapter(adapter);
//        }
//    }
}
