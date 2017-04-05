package com.example.s3rius.surveyclient.fragments;


import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.s3rius.surveyclient.R;

import java.util.ArrayList;
import java.util.Arrays;


public class TakenSurveys extends ListFragment {
    String[] fuckList = {"Пластмассовый мир победил", "Макет оказался сильней", "Последний кораблик остыл",
            "Последний фонарик устал,", "а в горле сопят комья воспоминаний", "Оооооооо моя оборона",
            "Солнечный зайчик стеклянного глаза", "Оооооооо моя оборона", "Траурный мячик нелепого мира",
            "Траурный мячик дешёвого мира"};

    public TakenSurveys() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("Taken Surveys");
        return inflater.inflate(R.layout.fragment_taken_surveys, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ArrayList<String> content = new ArrayList<>(Arrays.asList(fuckList));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, content);
        setListAdapter(adapter);
    }

    public void takeTakenSurveys() {
        // FIXME: 18.03.17 get TakenSurveys List with links from server.
    }
}
