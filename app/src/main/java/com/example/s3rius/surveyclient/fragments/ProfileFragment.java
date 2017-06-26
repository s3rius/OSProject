package com.example.s3rius.surveyclient.fragments;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.s3rius.surveyclient.Drawer;
import com.example.s3rius.surveyclient.R;
import com.example.s3rius.surveyclient.fragments.surveypac.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;

import java.io.File;
import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private ViewGroup container;
    private LayoutInflater inflater;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        this.container = container;
        this.inflater = inflater;
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

        Button deleteImg = (Button) view.findViewById(R.id.delete_profile_pic);
        Button newImg = (Button) view.findViewById(R.id.newPhoto);
        Button deletePrfl = (Button) view.findViewById(R.id.delete_profile);
        Button changeName = (Button) view.findViewById(R.id.changeName);
        Button changeSurname = (Button) view.findViewById(R.id.changeSurname);
        Button completedSurveysButton = (Button) view.findViewById(R.id.completed_surveys_prof);
        Button madeSurveysButton = (Button) view.findViewById(R.id.made_surveys);
        if (!savedUser.getLogin().equals(arguments.getString("login"))) {
            deleteImg.setVisibility(View.GONE);
            changeName.setVisibility(View.GONE);
            changeSurname.setVisibility(View.GONE);
            newImg.setVisibility(View.GONE);
            deletePrfl.setVisibility(View.GONE);
        }

        changeSurname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        completedSurveysButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getProfileSurveys("user/doneSurveys", arguments.getString("login"), getString(R.string.done_surveys));
            }
        });
        madeSurveysButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getProfileSurveys("user/madeSurveys", arguments.getString("login"), getString(R.string.made_surveys));
            }
        });


        final TextView username = (TextView) view.findViewById(R.id.profile_name);
        final TextView userSurname = (TextView) view.findViewById(R.id.profile_surname);
        username.setText(arguments.getString("username"));
        userSurname.setText(arguments.getString("surname"));

        changeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeNameOrUsername("user/changeName", "newName", username);
            }
        });

        changeSurname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeNameOrUsername("user/changeSurname", "newSurname", userSurname);
            }
        });
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
//                pic.setImageBitmap(BitmapFactory.decodeFile(file.getPath()));
//                Picasso.with(container.getContext()).load(file).resize(200,200).onlyScaleDown().centerInside().into(pic);
                Glide.with(container.getContext()).load(file).fitCenter().centerCrop().into(pic);

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

    public void getProfileSurveys(String url, String login, final String title) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("login", login);
        final ProgressDialog[] dialog = {null};
        client.setResponseTimeout(20000);
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
                if (!surveys.equals("[]")) {
                    Bundle bundle = new Bundle();
                    TakeSurvey fragment = new TakeSurvey();
                    bundle.putInt("act", 1);
                    bundle.putString("surveys list", surveys);
                    bundle.putString("title", title);
                    fragment.setArguments(bundle);
                    android.support.v4.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, fragment);
                    transaction.commit();
                    transaction.addToBackStack(null);
                } else {
                    if (title.equals(getString(R.string.done_surveys)))
                        Toast.makeText(container.getContext(), R.string.no_done_surveys, Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(container.getContext(), R.string.no_made_surveys, Toast.LENGTH_SHORT).show();
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


    public void changeNameOrUsername(final String url, final String paramName, final TextView textView) {
        View changeNameProps = inflater.inflate(R.layout.custom_alert_done_survey, null);
        final EditText newNameText = (EditText) changeNameProps.findViewById(R.id.surveyName);
        TextView text = (TextView) changeNameProps.findViewById(R.id.tv);
        if (paramName.equals("newName"))
            text.setText(getString(R.string.enter_new_name));
        else
            text.setText(getString(R.string.enter_new_surname));
        AlertDialog.Builder changeBuilder = new AlertDialog.Builder(container.getContext());
        changeBuilder.setView(changeNameProps).setCancelable(true)
                .setPositiveButton(getString(R.string.save), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!newNameText.getText().toString().trim().isEmpty()) {
                            AsyncHttpClient client = new AsyncHttpClient();
                            RequestParams params = new RequestParams();
                            params.put("login", getArguments().getString("login"));
                            params.put(paramName, newNameText.getText().toString());
                            client.post(getString(R.string.server) + url, params, new AsyncHttpResponseHandler() {
                                ProgressDialog progressDialog = null;

                                @Override
                                public void onStart() {
                                    super.onStart();
                                    progressDialog = new ProgressDialog(container.getContext());
                                    progressDialog.setMessage(getString(R.string.please_wait));
                                    progressDialog.show();
                                }

                                @Override
                                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                    if (progressDialog != null)
                                        progressDialog.dismiss();
                                    textView.setText(newNameText.getText().toString());
                                    SharedPreferences sPref = ProfileFragment.this.getActivity().getPreferences(Context.MODE_PRIVATE);
                                    String Juser = sPref.getString("saved_user", null);
                                    User savedUser = null;
                                    if (Juser != null) {
                                        try {
                                            savedUser = new ObjectMapper().readValue(Juser, User.class);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    if (paramName.equals("newName"))
                                        savedUser.setName(newNameText.getText().toString());
                                    else
                                        savedUser.setLastName(newNameText.getText().toString());
                                    SharedPreferences.Editor ed = sPref.edit();
                                    String JSONUser = null;
                                    try {
                                        JSONUser = new ObjectMapper().writeValueAsString(savedUser);
                                    } catch (JsonProcessingException e) {
                                        e.printStackTrace();
                                        Toast.makeText(container.getContext(), getString(R.string.error_saving_user), Toast.LENGTH_SHORT).show();
                                    }
                                    ed.putString("saved_user", JSONUser);
                                    ed.apply();
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                    if (progressDialog != null)
                                        progressDialog.dismiss();
                                    Toast.makeText(container.getContext(), getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(container.getContext(), getString(R.string.field_is_empty), Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog changeDialog = changeBuilder.create();
        changeDialog.show();
    }
}


