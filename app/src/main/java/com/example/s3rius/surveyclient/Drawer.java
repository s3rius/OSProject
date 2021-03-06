package com.example.s3rius.surveyclient;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.s3rius.surveyclient.fragments.AboutUsFragment;
import com.example.s3rius.surveyclient.fragments.CategoryFragment;
import com.example.s3rius.surveyclient.fragments.FullscreenImageFragment;
import com.example.s3rius.surveyclient.fragments.LoginFragment;
import com.example.s3rius.surveyclient.fragments.ProfileFragment;
import com.example.s3rius.surveyclient.fragments.RegistrationFragment;
import com.example.s3rius.surveyclient.fragments.StatisticFragment;
import com.example.s3rius.surveyclient.fragments.SurveyCreatorFragments.CreateSurveyReborn;
import com.example.s3rius.surveyclient.fragments.SurveyFragment;
import com.example.s3rius.surveyclient.fragments.TakeSurvey;
import com.example.s3rius.surveyclient.fragments.Top100Fragment;
import com.example.s3rius.surveyclient.fragments.surveypac.Survey;
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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//import com.example.s3rius.surveyclient.fragments.SurveyCreatorFragments.CreateAnswers;
//import com.example.s3rius.surveyclient.fragments.SurveyCreatorFragments.CreateQuestion;
//import com.example.s3rius.surveyclient.fragments.SurveyCreatorFragments.CreateSurveyFragment;


public class Drawer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String SAVED_USER = "saved_user";
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private final int NEW_PROFILE_PICTURE = 1;
    private final int REGISTRATION_NEW_PIC = 2;
    private User user = null;
    private SharedPreferences sPref;
    private NavigationView navigationView = null;
    private ImageView profileIcon;
    private File regPhoto;
    private int questionsQuan = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        Thread.setDefaultUncaughtExceptionHandler(new ExeptionsHandler(this));

        //setting up the fragments
        TakeSurvey fragment = new TakeSurvey();
        android.support.v4.app.FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_drawer);
        profileIcon = (ImageView) headerView.findViewById(R.id.profile_image);
        if (isUserExist()) {
            MenuItem loginItem = navigationView.getMenu().findItem(R.id.login);
            loginItem.setTitle(R.string.logout);
//            Picasso.with(this).load(getString(R.string.server) + "img?id=" + user.getLogin()).into(profileIcon);
            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            params.put("id", user.getLogin());
            client.setResponseTimeout(15000);
            client.get(getString(R.string.server) + "img", params, new FileAsyncHttpResponseHandler(Drawer.this) {
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, File file) {
//                    profileIcon.setImageBitmap(BitmapFactory.decodeFile(file.getPath()));
//                    Picasso.with(Drawer.this).load(file).resize(100, 100).onlyScaleDown().centerInside().into(profileIcon);
                    Glide.with(Drawer.this).load(file).fitCenter().centerCrop().into(profileIcon);
                }
            });
        }
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.category) {
            CategoryFragment fragment = new CategoryFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
            fragmentTransaction.addToBackStack(null);
//        } else if (id == R.id.statistics) {
//            StatisticsFragment fragment = new StatisticsFragment();
//            android.support.v4.app.FragmentTransaction fragmentTransaction =
//                    getSupportFragmentManager().beginTransaction();
//            fragmentTransaction.replace(R.id.fragment_container, fragment);
//            fragmentTransaction.commit();
        } else if (id == R.id.login) {
            if (!isUserExist()) {
                LoginFragment fragment = new LoginFragment();
                android.support.v4.app.FragmentTransaction fragmentTransaction =
                        getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.commit();
                fragmentTransaction.addToBackStack("login");
            } else {
                sPref = getPreferences(MODE_PRIVATE);
                Editor ed = sPref.edit();
                ed.clear();
                ed.apply();
                Toast.makeText(this, getString(R.string.logout_succ), Toast.LENGTH_SHORT).show();
                profileIcon.setImageBitmap(null);
                // get menu from navigationView
                Menu menu = navigationView.getMenu();

                // find MenuItem you want to change
                MenuItem loginItem = menu.findItem(R.id.login);

                // set new title to the MenuItem
                loginItem.setTitle(getString(R.string.login));

                TakeSurvey fragment = new TakeSurvey();
                android.support.v4.app.FragmentTransaction fragmentTransaction =
                        getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.commit();
                fragmentTransaction.addToBackStack(null);
            }
        } else if (id == R.id.top100) {
            Top100Fragment fragment = new Top100Fragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
            fragmentTransaction.addToBackStack(null);
        } else if (id == R.id.createSurvey) {
            if (isUserExist()) {
//                CreateSurveyFragment fragment = new CreateSurveyFragment();
//                android.support.v4.app.FragmentTransaction transaction =
//                        getSupportFragmentManager().beginTransaction();
//                transaction.replace(R.id.fragment_container, fragment);
//                transaction.commit();
//                transaction.addToBackStack(null);
                CreateSurveyReborn fragment = new CreateSurveyReborn();
                android.support.v4.app.FragmentTransaction transaction =
                        getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.commit();
                transaction.addToBackStack(null);
            } else {
                Toast.makeText(this, getString(R.string.please_login_to_create), Toast.LENGTH_LONG).show();
            }
        } else if (id == R.id.newestSurveys) {
            TakeSurvey fragment = new TakeSurvey();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
            fragmentTransaction.addToBackStack(null);
        } else if (id == R.id.aboutUs) {
            AboutUsFragment fragment = new AboutUsFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
            fragmentTransaction.addToBackStack(null);
        }
//            else if(id==R.id.profile_image){
//                ProfileFragment fragment = new ProfileFragment();
//                android.support.v4.app.FragmentTransaction fragmentTransaction =
//                        getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.replace(R.id.fragment_container, fragment);
//                fragmentTransaction.commit();
//            }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void profile_on_click(View view) throws IOException {
        if (isUserExist()) {
            ProfileFragment fragment = new ProfileFragment();
            Bundle bundle = new Bundle();
            User user = new ObjectMapper().readValue(sPref.getString(SAVED_USER, null), User.class);
            bundle.putString("username", user.getName());
            bundle.putString("login", user.getLogin());
            bundle.putString("surname", user.getLastName());
            fragment.setArguments(bundle);
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
            fragmentTransaction.addToBackStack(null);
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Toast.makeText(this, getString(R.string.please_login_to_profile), Toast.LENGTH_LONG).show();
        }
    }

    private void saveUser() throws JsonProcessingException {
        sPref = getPreferences(MODE_PRIVATE);
        Editor ed = sPref.edit();
        String JSONUser = new ObjectMapper().writeValueAsString(this.user);
        ed.putString(SAVED_USER, JSONUser);
        ed.apply();
//        Picasso.with(this).load(getString(R.string.server) + "img?id=" + user.getLogin()).into(profileIcon);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("id", user.getLogin());
        client.setResponseTimeout(15000);
        client.get(getString(R.string.server) + "img", params, new FileAsyncHttpResponseHandler(Drawer.this) {
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, File file) {
//                profileIcon.setImageBitmap(BitmapFactory.decodeFile(file.getPath()));
//                Picasso.with(Drawer.this).load(file).resize(100, 100).onlyScaleDown().centerInside().into(profileIcon);
                Glide.with(Drawer.this).load(file).fitCenter().centerCrop().into(profileIcon);

            }
        });
        Menu menu = navigationView.getMenu();

        // find MenuItem you want to change
        MenuItem loginItem = menu.findItem(R.id.login);
        // set new title to the MenuItem
        loginItem.setTitle("Logout");

        TakeSurvey fragment = new TakeSurvey();
        android.support.v4.app.FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    public boolean isUserExist() {
        sPref = getPreferences(MODE_PRIVATE);
        String Juser = sPref.getString(SAVED_USER, null);
        if (Juser != null) {
            try {
                user = new ObjectMapper().readValue(sPref.getString(SAVED_USER, null), User.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
//        return true;
    }

    public void OnclickLogin(final View view) {
        final Context context = this;
        final String username = ((EditText) findViewById(R.id.loginlogin)).getText().toString();
        String password = ((EditText) findViewById(R.id.passpass)).getText().toString();
        String url = String.format("%slogin?login=%s&password=%s", getString(R.string.server), username, password);
        AsyncHttpClient client = new AsyncHttpClient();
        client.setResponseTimeout(20000);
        client.post(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String lol = new String(responseBody);
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    user = mapper.readValue(lol, User.class);
                    Toast.makeText(context, getString(R.string.welcome) + user.getName(), Toast.LENGTH_LONG).show();
                    saveUser();

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(context, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                switch (statusCode) {
                    case 404:
                        Toast.makeText(context, getString(R.string.incorrect_login), Toast.LENGTH_LONG).show();
                        break;
                    case 400:
                        Toast.makeText(context, getString(R.string.incorrect_password), Toast.LENGTH_LONG).show();
                        break;
                    default:
                        Toast.makeText(context, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void OnclickCancelOnLogin(View view) {
        Top100Fragment fragment = new Top100Fragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    public void onClickSurveyDone(View view) {
        final List<Integer> answered = new ArrayList<>();
        boolean isAllChecked = true;
        boolean isChecked = false;
        final long surveyId;
        Fragment surveyFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (surveyFragment instanceof SurveyFragment) {
            surveyId = ((SurveyFragment) surveyFragment).getSurveyId();
            Survey survey = ((SurveyFragment) surveyFragment).getSurvey();
//            ListView listView = ((SurveyFragment) surveyFragment).getListView();
//            for (int i = 0; i < listView.getChildCount(); i++) {
//                View question = listView.getChildAt(i);
//                for (int j = 0; j < ((ViewGroup) question).getChildCount(); j++) {
//                    View child = ((ViewGroup) question).getChildAt(j);
//                    if (child instanceof RadioGroup) {
//                        RadioGroup group = (RadioGroup) child;
//                        for (int k = 0; k < group.getChildCount(); k++) {
//                            View button = group.getChildAt(k);
//                            if (button instanceof RadioButton) {
//                                if (((RadioButton) button).isChecked()) {
//                                    answered.add(k);
//                                    isChecked = true;
//                                    break;
//                                }
//                            }
//                        }
//                        if (!isChecked) {
//                            isAllChecked = false;
//                            break;
//                        }
//                        isChecked = false;
//                    }
//                }
//            }
            if (!survey.isAllAnswered()) {
                Toast.makeText(this, getString(R.string.not_all_quest_ans), Toast.LENGTH_SHORT).show();
            } else {
                for (int i = 0; i < survey.getQuestions().size(); i++) {
                    for (int j = 0; j < survey.getQuestions().get(i).getAnswers().size(); j++) {
                        if (survey.getQuestions().get(i).getAnswers().get(j).isIsAnswered()) {
                            answered.add(j);
                        }
                    }
                }
                AsyncHttpClient client = new AsyncHttpClient();
                client.setResponseTimeout(20000);
                final RequestParams params = new RequestParams();
                try {
                    params.put("answers", new ObjectMapper().writeValueAsString(answered));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                params.put("id", surveyId);
                params.put("login", user.getLogin());
                final ProgressDialog[] progressDialog = {new ProgressDialog(this)};
                client.post(getString(R.string.server) + "doneSurvey/", params, new AsyncHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        progressDialog[0] = new ProgressDialog(Drawer.this);
                        progressDialog[0].setMessage(getString(R.string.please_wait));
                        progressDialog[0].show();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        if (progressDialog[0] != null)
                            progressDialog[0].dismiss();
                        StatisticFragment statisticFragment = new StatisticFragment();
                        Bundle bundle = new Bundle();
                        bundle.putInt("id", (int) surveyId);
                        statisticFragment.setArguments(bundle);
                        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        getSupportFragmentManager().popBackStack();
                        transaction.replace(R.id.fragment_container, statisticFragment);
                        transaction.commit();
                        transaction.addToBackStack(null);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        if (progressDialog[0] != null)
                            progressDialog[0].dismiss();
                        if (statusCode == 450) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(Drawer.this);
                            builder.setTitle(getString(R.string.alert)).
                                    setMessage(getString(R.string.survey_already_complete)).
                                    setCancelable(true).
                                    setPositiveButton(getString(R.string.save_new_answers), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(final DialogInterface dialog, int which) {
                                            final ProgressDialog[] progressDialog1 = {null};
                                            AsyncHttpClient httpClient = new AsyncHttpClient();
                                            httpClient.setResponseTimeout(20000);
                                            RequestParams rParams = new RequestParams();
                                            rParams.put("id", surveyId);
                                            rParams.put("login", user.getLogin());
                                            httpClient.delete(getString(R.string.server) + "doneSurvey/", rParams, new AsyncHttpResponseHandler() {
                                                @Override
                                                public void onStart() {
                                                    super.onStart();
                                                    progressDialog1[0] = new ProgressDialog(Drawer.this);
                                                    progressDialog1[0].setMessage(getString(R.string.please_wait));
                                                    progressDialog1[0].show();
                                                }

                                                @Override
                                                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                                    AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
                                                    asyncHttpClient.setResponseTimeout(20000);
                                                    RequestParams requestParams = new RequestParams();
                                                    try {
                                                        requestParams.put("answers", new ObjectMapper().writeValueAsString(answered));
                                                    } catch (JsonProcessingException e) {
                                                        e.printStackTrace();
                                                    }
                                                    requestParams.put("id", surveyId);
                                                    requestParams.put("login", user.getLogin());
                                                    asyncHttpClient.post(getString(R.string.server) + "doneSurvey/", params, new AsyncHttpResponseHandler() {
                                                                @Override
                                                                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                                                    if (progressDialog1[0] != null)
                                                                        progressDialog1[0].dismiss();
                                                                    StatisticFragment statisticFragment = new StatisticFragment();
                                                                    Bundle bundle = new Bundle();
                                                                    bundle.putInt("id", (int) surveyId);
                                                                    statisticFragment.setArguments(bundle);
                                                                    android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                                                                    getSupportFragmentManager().popBackStack();
                                                                    transaction.replace(R.id.fragment_container, statisticFragment);
                                                                    transaction.commit();
                                                                    transaction.addToBackStack(null);
                                                                }

                                                                @Override
                                                                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                                                    if (progressDialog1[0] != null)
                                                                        progressDialog1[0].dismiss();
                                                                    dialog.cancel();
                                                                    Toast.makeText(Drawer.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                    );
                                                }

                                                @Override
                                                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                                    dialog.cancel();
                                                    Toast.makeText(Drawer.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    }).
                                    setNegativeButton(getString(R.string.leave_old_answers), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                            StatisticFragment statisticFragment = new StatisticFragment();
                                            Bundle bundle = new Bundle();
                                            bundle.putInt("id", (int) surveyId);
                                            statisticFragment.setArguments(bundle);
                                            android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                                            getSupportFragmentManager().popBackStack();
                                            transaction.replace(R.id.fragment_container, statisticFragment);
                                            transaction.commit();
                                            transaction.addToBackStack(null);
//                                            getSupportFragmentManager().popBackStack();
                                        }
                                    }).setIcon(R.drawable.alert);
                            AlertDialog alert = builder.create();
                            alert.show();
                        } else
                            Toast.makeText(Drawer.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
//
//    public void newSurvey(View view) {
//        Fragment newSurvey = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
//        if (newSurvey instanceof CreateSurveyFragment) {
//            Bundle bundle = new Bundle();
//            bundle.putSerializable("survey", ((CreateSurveyFragment) newSurvey).getSurvey());
//            CreateQuestion createQuestion = new CreateQuestion();
//            createQuestion.setArguments(bundle);
//            android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//            transaction.replace(R.id.fragment_container, createQuestion);
//            transaction.commit();
//            transaction.addToBackStack(null);
//        }
//    }
//
//    public void SurveyCreatingDone(View view) {
//        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
//        if (fragment instanceof CreateSurveyFragment) {
//            final Survey doneSurvey = ((CreateSurveyFragment) fragment).getSurvey();
//            if (!(doneSurvey.getQuestions().size() == 0)) {
//                LayoutInflater li = LayoutInflater.from(this);
//                View promptsView = li.inflate(R.layout.custom_alert_done_survey, null);
//                AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(this);
//                mDialogBuilder.setView(promptsView);
//                final EditText surveyName = (EditText) promptsView.findViewById(R.id.surveyName);
//                mDialogBuilder
//                        .setCancelable(false)
//                        .setPositiveButton("OK",
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(final DialogInterface dialog, int id) {
//                                        doneSurvey.setName(surveyName.getText().toString());
//                                        doneSurvey.setCreator(user);
//                                        doneSurvey.setUsers(new ArrayList<User>());
//                                        final AlertDialog.Builder builder = new AlertDialog.Builder(Drawer.this);
//                                        final String[][] cats = new String[1][1];
//                                        AsyncHttpClient client1 = new AsyncHttpClient();
//                                        final ProgressDialog[] progressDialog = {null};
//                                        client1.setResponseTimeout(20000);
//                                        client1.get(getString(R.string.server) + "topics/", new AsyncHttpResponseHandler() {
//                                            @Override
//                                            public void onStart() {
//                                                super.onStart();
//                                                progressDialog[0] = new ProgressDialog(Drawer.this);
//                                                progressDialog[0].setMessage(getString(R.string.please_wait));
//                                                progressDialog[0].show();
//                                            }
//
//                                            @Override
//                                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                                                if (progressDialog[0] != null)
//                                                    progressDialog[0].dismiss();
//                                                String responce = new String(responseBody);
//                                                JSONArray dataJsonObj = null;
//                                                try {
//                                                    dataJsonObj = new JSONArray(responce);
//                                                    cats[0] = new String[dataJsonObj.length()];
//                                                    for (int i = 0; i < dataJsonObj.length(); i++) {
//                                                        JSONObject obj = null;
//                                                        obj = dataJsonObj.getJSONObject(i);
//                                                        cats[0][i] = (obj.get("name").toString());
//                                                    }
//                                                } catch (JSONException e) {
//                                                    e.printStackTrace();
//                                                }
//                                                builder.setTitle(getString(R.string.chooseCat));
//                                                builder.setItems(cats[0], new DialogInterface.OnClickListener() {
//                                                    @Override
//                                                    public void onClick(DialogInterface dialog, int which) {
//                                                        doneSurvey.setCategory(new Category().setName(cats[0][which]));
//                                                        String createdSurvey = "";
//                                                        try {
//                                                            createdSurvey = new ObjectMapper().writeValueAsString(doneSurvey);
//                                                        } catch (JsonProcessingException e) {
//                                                            e.printStackTrace();
//                                                        }
//                                                        AsyncHttpClient client = new AsyncHttpClient();
//                                                        client.setResponseTimeout(20000);
//                                                        RequestParams params = new RequestParams();
//                                                        params.put("createdSurvey", createdSurvey);
//                                                        final ProgressDialog[] progressDialog = new ProgressDialog[1];
//                                                        client.post(getString(R.string.server) + "createdSurvey/", params, new AsyncHttpResponseHandler() {
//                                                            @Override
//                                                            public void onStart() {
//                                                                super.onStart();
//                                                                progressDialog[0] = new ProgressDialog(Drawer.this);
//                                                                progressDialog[0].setMessage(getString(R.string.please_wait));
//                                                                progressDialog[0].show();
//                                                            }
//
//                                                            @Override
//                                                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                                                                if (progressDialog[0] != null)
//                                                                    progressDialog[0].dismiss();
//                                                                Toast.makeText(Drawer.this, getString(R.string.succsessfullySent), Toast.LENGTH_SHORT).show();
//                                                                TakeSurvey fragment = new TakeSurvey();
//                                                                android.support.v4.app.FragmentTransaction fragmentTransaction =
//                                                                        getSupportFragmentManager().beginTransaction();
//                                                                fragmentTransaction.replace(R.id.fragment_container, fragment);
//                                                                fragmentTransaction.commit();
//                                                                fragmentTransaction.addToBackStack(null);
//                                                            }
//
//                                                            @Override
//                                                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                                                                if (progressDialog[0] != null)
//                                                                    progressDialog[0].dismiss();
//                                                                Toast.makeText(Drawer.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
//                                                            }
//                                                        });
//                                                    }
//                                                });
//                                                AlertDialog dialog1 = builder.create();
//                                                dialog1.show();
//                                            }
//
//                                            @Override
//                                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                                                if (progressDialog[0] != null)
//                                                    progressDialog[0].dismiss();
//                                                Toast.makeText(Drawer.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
//                                            }
//                                        });
//                                    }
//                                })
//                        .setNegativeButton(getString(R.string.cancel),
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int id) {
//                                        dialog.cancel();
//                                    }
//                                });
//                AlertDialog alertDialog = mDialogBuilder.create();
//                alertDialog.show();
//            } else {
//                Toast.makeText(this, getString(R.string.survey_is_empty), Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//    public void addQuestion(View view) {
//        Survey survey = null;
//        EditText editText = null;
//        Fragment newQuestion = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
//        if (questionsQuan < 50) {
//            if (newQuestion instanceof CreateQuestion) {
//                editText = (EditText) newQuestion.getView().findViewById(R.id.newQuestText);
//                survey = ((CreateQuestion) newQuestion).getSurvey();
//                if (!editText.getText().toString().equals("")) {
//                    survey.getQuestions().add(new Question(editText.getText().toString(), new ArrayList<Answer>()));
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("survey", survey);
//                    CreateAnswers createAnswers = new CreateAnswers();
//                    createAnswers.setArguments(bundle);
//                    getSupportFragmentManager().popBackStack();
//                    android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                    transaction.replace(R.id.fragment_container, createAnswers);
//                    transaction.commit();
//                    transaction.addToBackStack(null);
//                } else
//                    Toast.makeText(this, R.string.empty_question, Toast.LENGTH_SHORT).show();
//            }
//        } else {
//            Toast.makeText(this, R.string.max_quantity_ques, Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    public void onOneAnswerCreate(View view) {
//        Survey survey = null;
//        int ansQuan = 0;
//        EditText editText = null;
//        Fragment newAnswer = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
//        if (ansQuan < 50) {
//            if (newAnswer instanceof CreateAnswers) {
//                editText = (EditText) newAnswer.getView().findViewById(R.id.new_Answer);
//                if (!editText.getText().toString().equals("")) {
//                    survey = ((CreateAnswers) newAnswer).getSurvey();
//                    survey.getQuestions().get(survey.getQuestions().size() - 1).getAnswers().add(new Answer(editText.getText().toString(), 0));
//                    editText.setText("");
//                    ansQuan++;
//                } else
//                    Toast.makeText(this, R.string.enterAns, Toast.LENGTH_SHORT).show();
//            }
//        } else {
//            Toast.makeText(this, R.string.max_quantity_ans, Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    public void onAnswerCreate(View view) {
//        Survey survey = null;
//        EditText editText = null;
//        Fragment newAnswer = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
//        if (newAnswer instanceof CreateAnswers) {
//            survey = ((CreateAnswers) newAnswer).getSurvey();
//            editText = (EditText) newAnswer.getView().findViewById(R.id.new_Answer);
//            ++questionsQuan;
//            if (!editText.getText().toString().equals("")) {
//                survey.getQuestions().get(survey.getQuestions().size() - 1).getAnswers().add(new Answer(editText.getText().toString(), 0));
//            }
//            if (survey.getQuestions().get(survey.getQuestions().size() - 1).getAnswers().size() != 0) {
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("survey", survey);
//                CreateSurveyFragment createSurveyFragment = new CreateSurveyFragment();
//                createSurveyFragment.setArguments(bundle);
//                getSupportFragmentManager().popBackStack();
//                android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                transaction.replace(R.id.fragment_container, createSurveyFragment);
//                transaction.commit();
//            } else {
//                Toast.makeText(this, R.string.add_answers, Toast.LENGTH_LONG).show();
//            }
//        }
//    }
//
//    public void onChangeSurvey(final View view) {
//        final Fragment surveyFrag = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
//        final Survey survey = ((CreateSurveyFragment) surveyFrag).getSurvey();
//        CharSequence[] items;
//        items = new CharSequence[survey.getQuestions().size() + 1];
//        items[0] = getString(R.string.comment);
//        for (int i = 0; i < survey.getQuestions().size(); i++) {
//            items[i + 1] = survey.getQuestions().get(i).getName();
//        }
//        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle(R.string.what_want_change);
//        builder.setItems(items, new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int item) {
//                if (item == 0) {
//                    LayoutInflater li = LayoutInflater.from(Drawer.this);
//                    View promptsView = li.inflate(R.layout.custom_alert_done_survey, null);
//                    AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(Drawer.this);
//                    mDialogBuilder.setView(promptsView);
//                    final EditText comment = (EditText) promptsView.findViewById(R.id.surveyName);
//                    if (survey.getComment() != null)
//                        comment.setHint(survey.getComment());
//                    mDialogBuilder
//                            .setCancelable(false)
//                            .setPositiveButton("OK",
//                                    new DialogInterface.OnClickListener() {
//                                        public void onClick(final DialogInterface dialog, int id) {
//                                            if (comment.getText().toString().isEmpty()) {
//                                                Toast.makeText(Drawer.this, R.string.empty_comment, Toast.LENGTH_SHORT).show();
//                                            } else {
//                                                survey.setComment(comment.getText().toString());
//                                            }
//                                        }
//                                    })
//                            .setNegativeButton(getString(R.string.cancel),
//                                    new DialogInterface.OnClickListener() {
//                                        public void onClick(DialogInterface dialog, int id) {
//                                            dialog.cancel();
//                                        }
//                                    });
//                    AlertDialog alertDialog = mDialogBuilder.create();
//                    alertDialog.show();
//                } else {
//                    changeChoose(view, item - 1);
//                }
//            }
//        });
//        AlertDialog alert = builder.create();
//        alert.show();
//    }
//
//    public void changeChoose(final View view, final int num) {
//        final Fragment surveyFrag = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
//        CharSequence[] which = {getString(R.string.change_question), getString(R.string.change_answer), getString(R.string.add_answer), getString(R.string.delete_question), getString(R.string.delete_answer)};
//        final Survey survey = ((CreateSurveyFragment) surveyFrag).getSurvey();
//        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle(getString(R.string.what_want_do));
//        builder.setItems(which, new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int item) {
//                if (item == 0) {
//                    CreateQuestion createQuestion = new CreateQuestion();
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("survey", survey);
//                    bundle.putInt("questionInt", num);
//                    createQuestion.setArguments(bundle);
//                    android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                    transaction.replace(R.id.fragment_container, createQuestion);
//                    transaction.commit();
//                    transaction.addToBackStack(null);
//                }
//                if (item == 1) {
//                    changeAns(view, num, false, 0);
//                }
//                if (item == 2) {
//                    changeAns(view, num, false, 1);
//                }
//                if (item == 3) {
//                    survey.getQuestions().remove(num);
//                    final android.support.v4.app.FragmentTransaction transaction =
//                            getSupportFragmentManager().beginTransaction();
//                    transaction.detach(surveyFrag);
//                    transaction.attach(surveyFrag);
//                    transaction.commit();
//                }
//                if (item == 4) {
//                    changeAns(view, num, true, -1);
//                }
//            }
//        });
//        AlertDialog alert = builder.create();
//        alert.show();
//    }
//
//    public void changeAns(final View view, final int num, final boolean erase, final int act) {
//        final Fragment surveyFrag = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
//        final Survey survey = ((CreateSurveyFragment) surveyFrag).getSurvey();
//        if (act == 1) {
//            CreateAnswers createAnswers = new CreateAnswers();
//            Bundle bundle = new Bundle();
//            bundle.putSerializable("survey", survey);
//            bundle.putInt("act", act);
//            bundle.putInt("questInt", num);
//            createAnswers.setArguments(bundle);
//            android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//            transaction.replace(R.id.fragment_container, createAnswers);
//            transaction.commit();
//            transaction.addToBackStack(null);
//        } else {
//            CharSequence[] items;
//            items = new CharSequence[survey.getQuestions().get(num).getAnswers().size()];
//            for (int i = 0; i < survey.getQuestions().get(num).getAnswers().size(); i++) {
//                items[i] = survey.getQuestions().get(num).getAnswers().get(i).getName();
//            }
//            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setTitle(R.string.what_want_change);
//            builder.setItems(items, new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int item) {
//                    if (!erase) {
//                        CreateAnswers createAnswers = new CreateAnswers();
//                        Bundle bundle = new Bundle();
//                        bundle.putSerializable("survey", survey);
//                        bundle.putInt("act", act);
//                        bundle.putInt("questInt", num);
//                        bundle.putInt("ansInt", item);
//                        createAnswers.setArguments(bundle);
//                        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                        transaction.replace(R.id.fragment_container, createAnswers);
//                        transaction.commit();
//                        transaction.addToBackStack(null);
//                    } else {
//                        survey.getQuestions().get(num).getAnswers().remove(item);
//                        Fragment fragment = null;
//                        --questionsQuan;
//                        fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
//                        final android.support.v4.app.FragmentTransaction transaction =
//                                getSupportFragmentManager().beginTransaction();
//                        transaction.detach(fragment);
//                        transaction.attach(fragment);
//                        transaction.commit();
//                    }
//                }
//            });
//            AlertDialog alert = builder.create();
//            alert.show();
//        }
//    }

    public void uploadNewProfilePhoto(View view) {
        photoChooser(NEW_PROFILE_PICTURE);
    }

    public void photoChooser(int request_code) {
        if (Build.VERSION.SDK_INT >= 23) {
            // Here, thisActivity is the current activity
            if (ContextCompat.checkSelfPermission(Drawer.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(Drawer.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {

                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(Drawer.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

                    // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            } else {
                ActivityCompat.requestPermissions(Drawer.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            }
        }
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, request_code);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_PROFILE_PICTURE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            final String picturePath = cursor.getString(columnIndex);
            cursor.close();

            // String picturePath contains the path of selected Image

            final ImageView imageView = (ImageView) findViewById(R.id.profile_pic);

            AsyncHttpClient client = new AsyncHttpClient();
            client.setResponseTimeout(20000);
            final File uploadImg = new File(picturePath);
            RequestParams params = new RequestParams();
            try {
                params.put("profile_picture", uploadImg);
                params.put("userLogin", user.getLogin());

                final ProgressDialog[] progressDialog = new ProgressDialog[1];
                client.post(getString(R.string.server) + "/img/upload/", params, new AsyncHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        progressDialog[0] = new ProgressDialog(Drawer.this);
                        progressDialog[0].setMessage(getString(R.string.please_wait));
                        progressDialog[0].show();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        if (progressDialog[0] != null)
                            progressDialog[0].dismiss();
                        //imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
//                        profileIcon.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                        Glide.with(Drawer.this).load(uploadImg).fitCenter().centerCrop().into(profileIcon);
//                        Picasso.with(Drawer.this).load(uploadImg).resize(100, 100).onlyScaleDown().centerInside().into(profileIcon);
                        Fragment profile = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                        android.support.v4.app.FragmentTransaction transaction
                                = getSupportFragmentManager().beginTransaction();
                        transaction.detach(profile);
                        transaction.attach(profile);
                        transaction.commit();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        if (progressDialog[0] != null)
                            progressDialog[0].dismiss();
                        Toast.makeText(Drawer.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
        if (requestCode == REGISTRATION_NEW_PIC && resultCode == RESULT_OK && null != data) {

            final Fragment loginFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            final String picturePath = cursor.getString(columnIndex);

            final ImageView view = (ImageView) loginFragment.getView().findViewById(R.id.reg_profile_pic);
//            view.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            cursor.close();
            regPhoto = new File(picturePath);
//            Picasso.with(Drawer.this).load(regPhoto).resize(100, 100).onlyScaleDown().centerInside().into(view);
            Glide.with(Drawer.this).load(regPhoto).fitCenter().centerCrop().into(view);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Drawer.this);
                    builder.setTitle(getString(R.string.chooseAct))
                            .setItems(new String[]{getString(R.string.open_in_fullscreen), getString(R.string.delete_profile_picture)}, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case 0:
                                            ((RegistrationFragment) loginFragment).setFilePath(picturePath);
                                            FullscreenImageFragment fullscreen = new FullscreenImageFragment();
                                            Bundle bundle = new Bundle();
                                            bundle.putString("imagePath", picturePath);
                                            fullscreen.setArguments(bundle);
                                            android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                                            transaction.replace(R.id.fragment_container, fullscreen);
                                            transaction.commit();
                                            transaction.addToBackStack(null);
                                        case 1:
                                            view.setImageBitmap(null);
                                            view.setOnClickListener(new View.OnClickListener() {
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
    }

    public void registrationPhoto(View view) {
        photoChooser(REGISTRATION_NEW_PIC);
    }

    public void onRegistration(View view) {
        Fragment registration = new RegistrationFragment();
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, registration);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void onRegistrationDone(View view) {
        EditText name, surname, login, pass, passRep;
        name = (EditText) findViewById(R.id.newName);
        surname = (EditText) findViewById(R.id.newSurname);
        login = (EditText) findViewById(R.id.newLogin);
        pass = (EditText) findViewById(R.id.newPassword);
        passRep = (EditText) findViewById(R.id.newPasswordRepeat);
        if (isOnlySpacesOrEmpty(name.getText().toString()) ||
                isOnlySpacesOrEmpty(surname.getText().toString()) ||
                isOnlySpacesOrEmpty(login.getText().toString()) ||
                isOnlySpacesOrEmpty(pass.getText().toString()) ||
                isOnlySpacesOrEmpty(passRep.getText().toString())) {
            Toast.makeText(this, getString(R.string.not_all_fields_filled), Toast.LENGTH_SHORT).show();
        } else if (!pass.getText().toString().equals(passRep.getText().toString())) {
            Toast.makeText(this, getString(R.string.password_dont_match), Toast.LENGTH_SHORT).show();
        } else {
            final User newUser = new User();
            newUser.setName(name.getText().toString());
            newUser.setLastName(surname.getText().toString());
            newUser.setLogin(login.getText().toString());
            newUser.setPassword(pass.getText().toString());

            AsyncHttpClient client = new AsyncHttpClient();
            client.setResponseTimeout(20000);
            RequestParams params = new RequestParams();

            try {
                params.put("newUser", new ObjectMapper().writeValueAsString(newUser));

                final ProgressDialog[] progressDialog = new ProgressDialog[1];
                client.post(getString(R.string.server) + "registration/", params, new AsyncHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        progressDialog[0] = new ProgressDialog(Drawer.this);
                        progressDialog[0].setMessage(getString(R.string.please_wait));
                        progressDialog[0].show();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        AsyncHttpClient client = new AsyncHttpClient();
                        client.setResponseTimeout(20000);
                        RequestParams params = new RequestParams();
                        Fragment regFrag = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                        if (((RegistrationFragment) regFrag).getFilePath() != null) {
                            try {
                                params.put("profile_picture", new File(((RegistrationFragment) regFrag).getFilePath()));
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            params.put("userLogin", newUser.getLogin());
                            client.post(getString(R.string.server) + "img/upload/", params, new AsyncHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                    if (progressDialog[0] != null)
                                        progressDialog[0].dismiss();
                                    Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                                    if (fragment instanceof LoginFragment) {
                                        EditText login = (EditText) fragment.getView().findViewById(R.id.loginlogin);
                                        EditText pass = (EditText) fragment.getView().findViewById(R.id.passpass);
                                        login.setText(newUser.getLogin());
                                        pass.setText(newUser.getPassword());
                                    }
                                    Toast.makeText(Drawer.this, getString(R.string.registration_complete), Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                    if (progressDialog[0] != null)
                                        progressDialog[0].dismiss();
                                    Toast.makeText(Drawer.this, getString(R.string.picture_upload_error), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        if (progressDialog[0] != null)
                            progressDialog[0].dismiss();
                        onBackPressed();
                        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                        if (fragment instanceof LoginFragment) {
                            EditText login = (EditText) fragment.getView().findViewById(R.id.loginlogin);
                            EditText pass = (EditText) fragment.getView().findViewById(R.id.passpass);
                            login.setText(newUser.getLogin());
                            pass.setText(newUser.getPassword());
                        }
                        Toast.makeText(Drawer.this, getString(R.string.registration_complete), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        if (progressDialog[0] != null)
                            progressDialog[0].dismiss();
                        switch (statusCode) {
                            case 420:
                                Toast.makeText(Drawer.this, new String(responseBody), Toast.LENGTH_SHORT).show();
                                break;
                            case 421:
                                Toast.makeText(Drawer.this, getString(R.string.short_password), Toast.LENGTH_SHORT).show();
                                break;
                            case 422:
                                Toast.makeText(Drawer.this, getString(R.string.short_login), Toast.LENGTH_SHORT).show();
                            default:
                                Toast.makeText(Drawer.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });

            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteProfilePicture(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Drawer.this);
        builder.setIcon(R.drawable.alert)
                .setTitle(getString(R.string.alert))
                .setMessage(getString(R.string.sure_deletePicture))
                .setCancelable(true)
                .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AsyncHttpClient client = new AsyncHttpClient();
                client.setResponseTimeout(20000);
                RequestParams params = new RequestParams();
                params.put("login", user.getLogin());
                client.delete(getString(R.string.server) + "img/delete", params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                        if (fragment instanceof ProfileFragment) {
                            ImageView profilePic = (ImageView) fragment.getView().findViewById(R.id.profile_pic);
                            profilePic.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.no_image));
                            profileIcon.setImageBitmap(null);
                            profilePic.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Toast.makeText(Drawer.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        AlertDialog alert = builder.create();
        alert.show();

    }

//    public void onAnswerBack(View view) {
//        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
//        Survey survey = ((CreateAnswers) fragment).getSurvey();
//        survey.getQuestions().remove(survey.getQuestions().size() - 1);
//        ((CreateAnswers) fragment).setSurvey(survey);
//        getSupportFragmentManager().popBackStack();
//    }
//
//    public void onQuestionBack(View view) {
//        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
//        if (!((CreateQuestion) fragment).getSurvey().getQuestions().isEmpty()) {
//            if (((CreateQuestion) fragment)
//                    .getSurvey()
//                    .getQuestions()
//                    .get(((CreateQuestion) fragment).getSurvey().getQuestions().size() - 1)
//                    .getAnswers().size() == 0) {
//                ((CreateAnswers) fragment).getSurvey().getQuestions()
//                        .remove(((CreateAnswers) fragment).getSurvey().getQuestions().size() - 1);
//            }
//        }
//        getSupportFragmentManager().popBackStack();
//    }

    public void deleteProfile(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Drawer.this);
        builder.setIcon(R.drawable.alert)
                .setTitle(getString(R.string.alert))
                .setMessage(getString(R.string.sure_deleteProfile))
                .setCancelable(true)
                .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AsyncHttpClient client = new AsyncHttpClient();
                client.setResponseTimeout(20000);
                RequestParams params = new RequestParams();
                params.put("login", user.getLogin());
                final ProgressDialog[] progressDialog = {null};
                client.delete(getString(R.string.server) + "user/", params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        progressDialog[0] = new ProgressDialog(Drawer.this);
                        progressDialog[0].setMessage(getString(R.string.please_wait));
                        progressDialog[0].show();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        if (progressDialog[0] != null)
                            progressDialog[0].dismiss();
                        sPref = getPreferences(MODE_PRIVATE);
                        Editor ed = sPref.edit();
                        ed.clear();
                        ed.apply();
                        Toast.makeText(Drawer.this, getString(R.string.delete_profile_succ), Toast.LENGTH_SHORT).show();
                        profileIcon.setImageBitmap(null);
                        // get menu from navigationView
                        Menu menu = navigationView.getMenu();

                        // find MenuItem you want to change
                        MenuItem loginItem = menu.findItem(R.id.login);

                        // set new title to the MenuItem
                        loginItem.setTitle(getString(R.string.login));

                        TakeSurvey fragment = new TakeSurvey();
                        android.support.v4.app.FragmentTransaction fragmentTransaction =
                                getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_container, fragment);
                        fragmentTransaction.commit();
                        fragmentTransaction.addToBackStack(null);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        if (progressDialog[0] != null)
                            progressDialog[0].dismiss();
                        Toast.makeText(Drawer.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public boolean isOnlySpacesOrEmpty(CharSequence sequence) {
        if (sequence != null) {
            if (!sequence.toString().trim().isEmpty()) {
                return false;
            } else
                return true;
        } else
            return true;
    }

    public void onLoginBack(View view) {
        getSupportFragmentManager().popBackStack();
    }
}

