package com.example.s3rius.surveyclient.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.loopj.android.http.*;
import com.example.s3rius.surveyclient.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {


    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        Button login = (Button) view.findViewById(R.id.buttonOkLogin);
        final String username = ((EditText) view.findViewById(R.id.loginlogin)).getText().toString();
        final String userpass = ((EditText) view.findViewById(R.id.passpass)).getText().toString();
        getActivity().setTitle(getString(R.string.login));
        return view;
    }
}

