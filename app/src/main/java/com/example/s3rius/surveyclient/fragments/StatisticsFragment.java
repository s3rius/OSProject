package com.example.s3rius.surveyclient.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.s3rius.surveyclient.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class StatisticsFragment extends Fragment {


    public StatisticsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        ImageView imageView = (ImageView)().findViewById(R.id.image_on_statistics);
//        imageView.setImageResource(R.drawable.statistics);
        getActivity().setTitle("Statistics");
        return inflater.inflate(R.layout.fragment_statistics, container, false);
    }
}
