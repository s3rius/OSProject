package com.example.s3rius.surveyclient.fragments;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.example.s3rius.surveyclient.R;

import java.io.IOException;

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
        if (getArguments() != null) {
            String filePath = getArguments().getString("imagePath");
            SubsamplingScaleImageView image = (SubsamplingScaleImageView) view.findViewById(R.id.fullscreenImage);
            Bitmap rotated = BitmapFactory.decodeFile(filePath);
            try {
                ExifInterface exif = new ExifInterface(filePath);
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                Matrix matrix = new Matrix();
                switch (orientation) {
                    case 3:
                        matrix.postRotate(180);
                        break;
                    case 6:
                        matrix.postRotate(90);
                        break;
                    case 8:
                        matrix.postRotate(270);
                        break;
                }
                Bitmap srcBitmap = BitmapFactory.decodeFile(filePath);
                rotated = Bitmap.createBitmap(srcBitmap, 0, 0, srcBitmap.getWidth(), srcBitmap.getHeight(), matrix, true);
            } catch (IOException e) {
                e.printStackTrace();
            }
            image.setImage(ImageSource.bitmap(rotated));
        }
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }
}
