package com.example.s3rius.surveyclient;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;


public class TakeSurvey extends ListFragment {

    String[] testsList = {"Тест0", "Тест1", "Опросы","Которые",
            "Доступны", "Для", "Выбора","Пока","Здесь", "Пусто",
            "Но", "Тестовый тест","Уже готов", "Тестовый тест"};

    public TakeSurvey() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Take a Survey");
        return inflater.inflate(R.layout.fragment_take_survey, container, false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ArrayList<String> content = new ArrayList<>(Arrays.asList(testsList));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, content);
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        SurveyFragment surveyFragment = new SurveyFragment();
        startSurvey(id);
    }

    private void startSurvey(long id) {
        Fragment surveyFragment = new SurveyFragment();
        Bundle bundle = new Bundle();
        bundle.putLong("id", id);
        bundle.putString("title", testsList[(int)id]);
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

    public void takeTakeSurveys(){
        // FIXME: 18.03.17 get Surveys that can be taken List with links from server.
    }
}
