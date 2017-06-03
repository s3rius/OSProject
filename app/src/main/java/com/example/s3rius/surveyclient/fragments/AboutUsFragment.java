package com.example.s3rius.surveyclient.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.s3rius.surveyclient.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutUsFragment extends Fragment {


    public AboutUsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle(R.string.about_us);
        View view = inflater.inflate(R.layout.fragment_about_us, container, false);
        TextView aboutUs = (TextView)view.findViewById(R.id.textAboutUs);
        String text = getString(R.string.about_us_text);
        aboutUs.setText(Html.fromHtml(text));
        return view;
    }

}
