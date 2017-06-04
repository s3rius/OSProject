package com.example.s3rius.surveyclient.fragments;


import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.example.s3rius.surveyclient.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FullscreenImageFragment extends Fragment {


    public FullscreenImageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fullscreen_image, container, false);
        // Inflate the layout for this fragment
        getActivity().setTitle(getString(R.string.profile_picture));
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        if(getArguments()!=null){
            String filePath = getArguments().getString("imagePath");
            SubsamplingScaleImageView image = (SubsamplingScaleImageView)view.findViewById(R.id.fullscreenImage);
            image.setImage(ImageSource.bitmap(BitmapFactory.decodeFile(filePath)));
        }
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }
}
