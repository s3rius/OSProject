package com.example.s3rius.surveyclient.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.s3rius.surveyclient.Drawer;
import com.example.s3rius.surveyclient.R;
import com.example.s3rius.surveyclient.fragments.surveypac.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.io.File;
import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    ViewGroup container;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        this.container = container;
        final View view = inflater.inflate(R.layout.fragment_profile, container, false);
        // Inflate the layout for this fragment
        getActivity().setTitle(getString(R.string.profile));
        final Bundle arguments = getArguments();
        SharedPreferences sPref = this.getActivity().getPreferences(Context.MODE_PRIVATE);
        User savedUser = null;
        String Juser = sPref.getString("saved_user", null);
        if (Juser != null) {
            try {
                savedUser = new ObjectMapper().readValue(Juser, User.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (!savedUser.getLogin().equals(arguments.getString("login"))) {
            Button deleteImg = (Button) view.findViewById(R.id.delete_profile_pic);
            Button newImg = (Button) view.findViewById(R.id.newPhoto);
            Button deletePrfl = (Button) view.findViewById(R.id.delete_profile);
            deleteImg.setVisibility(View.GONE);
            newImg.setVisibility(View.GONE);
            deletePrfl.setVisibility(View.GONE);
        }
        Button completedSurveysButton = (Button)view.findViewById(R.id.completed_surveys_prof);
        Button madeSurveysButton= (Button)view.findViewById(R.id.made_surveys);
        completedSurveysButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getProfileSurveys("user/doneSurveys", arguments.getString("login"));
            }
        });
        madeSurveysButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getProfileSurveys("user/madeSurveys", arguments.getString("login"));
            }
        });

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

    public void getProfileSurveys(String url , String login) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("login", login);
        final ProgressDialog[] dialog = {null};
        client.get(getString(R.string.server) + url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                dialog[0] = new ProgressDialog(container.getContext());
                dialog[0].setMessage(getString(R.string.please_wait));
                dialog[0].show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (dialog[0] != null)
                    dialog[0].dismiss();
                String surveys = new String(responseBody);
                if (!surveys.equals("null")) {
                    Bundle bundle = new Bundle();
                    TakeSurvey fragment = new TakeSurvey();
                    bundle.putInt("act", 1);
                    bundle.putString("surveys list", surveys);
                    fragment.setArguments(bundle);
                    android.support.v4.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, fragment);
                    transaction.commit();
                    transaction.addToBackStack(null);
                } else {
                    Toast.makeText(container.getContext(), R.string.no_done_surveys, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if (dialog[0] != null)
                    dialog[0].dismiss();
                Toast.makeText(container.getContext(), R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
            }
        });
    }

}

