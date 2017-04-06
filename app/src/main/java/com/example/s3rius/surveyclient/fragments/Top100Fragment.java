package com.example.s3rius.surveyclient.fragments;


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

import java.util.ArrayList;
import java.util.Arrays;


public class Top100Fragment extends ListFragment {

    private SurveyFragment surveyFragment;

    private String[] testsList = {"Неинтересный опрос", "Неинтересный опрос", "Неинтересный опрос", "Неинтересный опрос",
            "Неинтересный опрос", "Неинтересный опрос",
            "Неинтересный опрос", "Неинтересный опрос",
            "Неинтересный опрос", "Неинтересный опрос",
            "Неинтересный опрос", "Неинтересный опрос",
            "Неинтересный опрос", "Неинтересный опрос"};

    public Top100Fragment() {
        // Required empty public constructor
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
        super.onActivityCreated(savedInstanceState);
        ArrayList<String> content = new ArrayList<>(Arrays.asList(testsList));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, content);
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        startSurvey(id);
    }

    private void startSurvey(long id) {
        surveyFragment = new SurveyFragment();
        Bundle bundle = new Bundle();
        bundle.putLong("id", id);
        bundle.putString("title", testsList[(int) id]);
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
}
