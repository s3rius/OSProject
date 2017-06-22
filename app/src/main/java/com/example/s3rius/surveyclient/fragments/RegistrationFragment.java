package com.example.s3rius.surveyclient.fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.s3rius.surveyclient.Drawer;
import com.example.s3rius.surveyclient.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegistrationFragment extends Fragment {

    String filePath;
    String name;
    String surname;
    String login;
    String password;

    public RegistrationFragment() {
        // Required empty public constructor
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("filepath", filePath);
//        outState.putString("name", name);
//        outState.putString("surname", surname);
//        outState.putString("login", login);
//        outState.putString("password", password);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registration, container, false);
        getActivity().setTitle(R.string.registration);
//        if(savedInstanceState!= null) {
//           EditText name = (EditText) view.findViewById(R.id.newName);
//           EditText surname = (EditText) view.findViewById(R.id.newSurname);
//           EditText login = (EditText) view.findViewById(R.id.newLogin);
//           EditText pass = (EditText) view.findViewById(R.id.newPassword);
//            name.setText(savedInstanceState.getString("name"));
//            surname.setText(savedInstanceState.getString("surname"));
//            login.setText(savedInstanceState.getString("login"));
//            pass.setText(savedInstanceState.getString("password"));

//        }
        if(filePath!=null){
            final ImageView proPic = (ImageView) view.findViewById(R.id.reg_profile_pic);
            Bitmap bitmap = BitmapFactory.decodeFile(filePath);
            proPic.setImageBitmap(bitmap);
            proPic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(container.getContext());
                    builder.setTitle(getString(R.string.chooseAct))
                            .setItems(new String[]{getString(R.string.open_in_fullscreen), getString(R.string.delete_profile_picture)}, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which){
                                        case 0 :
                                            FullscreenImageFragment fullscreen = new FullscreenImageFragment();
                                            Bundle bundle = new Bundle();
                                            bundle.putString("imagePath", filePath);
                                            fullscreen.setArguments(bundle);
                                            android.support.v4.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                            transaction.replace(R.id.fragment_container, fullscreen);
                                            transaction.commit();
                                            transaction.addToBackStack(null);
                                        case 1 :
                                            proPic.setImageBitmap(null);
                                            proPic.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {

                                                }
                                            });
                                    }
                                }
                            }).setCancelable(true).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });
        }
        return view;
    }

}
