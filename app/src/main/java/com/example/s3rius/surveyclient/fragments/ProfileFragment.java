package com.example.s3rius.surveyclient.fragments;


import android.app.ProgressDialog;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.s3rius.surveyclient.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;

import java.io.File;

import cz.msebera.android.httpclient.Header;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {


    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_profile, container, false);
        // Inflate the layout for this fragment
        getActivity().setTitle("Profile");
        Bundle arguments = getArguments();
        TextView username = (TextView) view.findViewById(R.id.profile_name);
        username.setText(arguments.getString("username"));
        AsyncHttpClient client = new AsyncHttpClient();
        final ProgressDialog[] progressDialog = new ProgressDialog[1];
        client.get(getString(R.string.server) + "img?id=" + arguments.getString("login"), new FileAsyncHttpResponseHandler(container.getContext()) {
            @Override
            public void onStart() {
                super.onStart();
                progressDialog[0] = new ProgressDialog(container.getContext());
                progressDialog[0].setMessage("Please Wait....");
                progressDialog[0].show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                progressDialog[0].dismiss();
                Toast.makeText(container.getContext(), "Error loading profile image", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, File file) {
                progressDialog[0].dismiss();
                ImageView pic = (ImageView) view.findViewById(R.id.profile_pic);
                pic.setImageBitmap(BitmapFactory.decodeFile(file.getPath()));
            }
        });
        return view;
    }
}
