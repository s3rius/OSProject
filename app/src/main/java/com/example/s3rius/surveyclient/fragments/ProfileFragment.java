package com.example.s3rius.surveyclient.fragments;


import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.s3rius.surveyclient.Drawer;
import com.example.s3rius.surveyclient.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;

import java.io.File;


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
        getActivity().setTitle(getString(R.string.profile));
        Bundle arguments = getArguments();
        TextView username = (TextView) view.findViewById(R.id.profile_name);
        username.setText(arguments.getString("username"));
//        ImageView profilePic = (ImageView)view.findViewById(R.id.profile_pic);
//        Picasso.with(container.getContext()).load(getString(R.string.server) + "img?id=" + arguments.getString("username")).into(profilePic);

        AsyncHttpClient client = new AsyncHttpClient();
        final ProgressDialog[] progressDialog = new ProgressDialog[1];
        client.get(getString(R.string.server) + "img?id=" + arguments.getString("login"), new FileAsyncHttpResponseHandler(container.getContext()) {
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                progressDialog[0].dismiss();
            }

            @Override
            public void onStart() {
                super.onStart();
                progressDialog[0] = new ProgressDialog(container.getContext());
                progressDialog[0].setMessage(getString(R.string.please_wait));
                progressDialog[0].show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, final File file) {
                progressDialog[0].dismiss();
                ImageView pic = (ImageView) view.findViewById(R.id.profile_pic);
                pic.setImageBitmap(BitmapFactory.decodeFile(file.getPath()));
                pic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FullscreenImageFragment fullscreen = new FullscreenImageFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("imagePath", file.getPath());
                        fullscreen.setArguments(bundle);
                        android.support.v4.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment_container, fullscreen);
                        transaction.commit();
                        transaction.addToBackStack(null);
                    }
                });
            }
        });

        return view;
    }
}
