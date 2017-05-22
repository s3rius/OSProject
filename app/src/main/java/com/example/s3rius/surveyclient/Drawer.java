package com.example.s3rius.surveyclient;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.s3rius.surveyclient.fragments.CategoryFragment;
import com.example.s3rius.surveyclient.fragments.LoginFragment;
import com.example.s3rius.surveyclient.fragments.ProfileFragment;
import com.example.s3rius.surveyclient.fragments.SurveyCreatorFragments.CreateAnswers;
import com.example.s3rius.surveyclient.fragments.SurveyCreatorFragments.CreateQuestion;
import com.example.s3rius.surveyclient.fragments.SurveyCreatorFragments.CreateSurveyFragment;
import com.example.s3rius.surveyclient.fragments.SurveyFragment;
import com.example.s3rius.surveyclient.fragments.TakeSurvey;
import com.example.s3rius.surveyclient.fragments.Top100Fragment;
import com.example.s3rius.surveyclient.fragments.surveypac.Answer;
import com.example.s3rius.surveyclient.fragments.surveypac.Question;
import com.example.s3rius.surveyclient.fragments.surveypac.Survey;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class Drawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private final String SAVED_LOGIN = "saved_login";
    private final String SAVED_PASS = "saved_pass";
    private SharedPreferences sPref;
    private NavigationView navigationView = null;
    private Toolbar toolbar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        //setting up the fragments
        Top100Fragment fragment = new Top100Fragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        if (isUserExist()) {
            MenuItem loginItem = navigationView.getMenu().findItem(R.id.login);
            loginItem.setTitle("Logout");
        }
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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.drawer, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

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
            } else {
                sPref = getPreferences(MODE_PRIVATE);
                Editor ed = sPref.edit();
                ed.clear();
                ed.apply();

                // get menu from navigationView
                Menu menu = navigationView.getMenu();

                // find MenuItem you want to change
                MenuItem loginItem = menu.findItem(R.id.login);

                // set new title to the MenuItem
                loginItem.setTitle("Login");

                Toast.makeText(this, "Logout successfully", Toast.LENGTH_SHORT).show();
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
        } else if (id == R.id.createSurvey) {
            CreateSurveyFragment fragment = new CreateSurveyFragment();
            android.support.v4.app.FragmentTransaction transaction =
                    getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.commit();
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

    public void profile_on_click(View view) {
        if (isUserExist()) {
            ProfileFragment fragment = new ProfileFragment();
            Bundle bundle = new Bundle();

            bundle.putString("username", sPref.getString(SAVED_LOGIN, null));
            fragment.setArguments(bundle);
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Toast.makeText(this, "Please login to go to profile.", Toast.LENGTH_LONG).show();
        }
    }

    void saveUser() {
        sPref = getPreferences(MODE_PRIVATE);
        Editor ed = sPref.edit();
        EditText loginField = (EditText) findViewById(R.id.loginlogin);
        EditText passField = (EditText) findViewById(R.id.passpass);

        ed.putString(SAVED_LOGIN, loginField.getText().toString());
        ed.putString(SAVED_PASS, passField.getText().toString());
        ed.apply();
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

    private boolean isEmpty(EditText etText) {
        if (etText.getText().toString().trim().length() > 0)
            return false;

        return true;
    }

    boolean isUserExist() {
        sPref = getPreferences(MODE_PRIVATE);
        String login = sPref.getString(SAVED_LOGIN, null);
        String pass = sPref.getString(SAVED_PASS, null);
        if ((pass != null) && (login != null)) {
            return true;
        }
        return false;
    }

    public void OnclickLogin(final View view) {
        final Context context = this;
        String username = ((EditText) findViewById(R.id.loginlogin)).getText().toString();
        String password = ((EditText) findViewById(R.id.passpass)).getText().toString();
        String url = String.format("http://10.60.6.234:8080/survey/client/login?login=%s&password=%s", username, password);
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String lol = new String(responseBody);
                Toast.makeText(context, lol, Toast.LENGTH_LONG).show();
                saveUser();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                switch (statusCode) {
                    case 404:
                        Toast.makeText(context, "boy, ur login is bad", Toast.LENGTH_LONG).show();
                        break;
                    case 400:
                        Toast.makeText(context, "boy, ur pswrd is shit", Toast.LENGTH_LONG).show();
                        break;
                    default:
                        Toast.makeText(context, "Sry, u was a faggt)))", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void OnclickCancelOnLogin(View view) {
        TakeSurvey fragment = new TakeSurvey();
        android.support.v4.app.FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }


    public void onClickSurveyDone(View view) {
        String answered = "";
        boolean isAllChecked = true;
        boolean isChecked = false;
        Fragment surveyFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (surveyFragment instanceof SurveyFragment) {
            ListView listView = ((SurveyFragment) surveyFragment).getListView();
            for (int i = 0; i < listView.getChildCount(); i++) {
                View question = listView.getChildAt(i);
                for (int j = 0; j < ((ViewGroup) question).getChildCount(); j++) {
                    View child = ((ViewGroup) question).getChildAt(j);
                    if (child instanceof RadioGroup) {
                        RadioGroup group = (RadioGroup) child;
                        for (int k = 0; k < group.getChildCount(); k++) {
                            View button = group.getChildAt(k);
                            if (button instanceof RadioButton) {
                                if (((RadioButton) button).isChecked()) {
                                    answered += k + 1;
                                    isChecked = true;
                                    break;
                                }
                            }
                        }
                        if (!isChecked) {
                            isAllChecked = false;
                            break;
                        }
                        isChecked = false;
                    }
                }
            }
            if (!isAllChecked) {
                Toast.makeText(this, "not all question is answered.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, answered, Toast.LENGTH_SHORT).show();
                // TODO: 05.04.17 Make server transaction of done survey.
            }
        }
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    }

    public void onQuestionCancel(View view) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStack();
    }


    public void newSurvey(View view) {
        Fragment newSurvey = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (newSurvey instanceof CreateSurveyFragment) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("survey", ((CreateSurveyFragment) newSurvey).getSurvey());
            CreateQuestion createQuestion = new CreateQuestion();
            createQuestion.setArguments(bundle);
            android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, createQuestion);
            transaction.commit();
            transaction.addToBackStack(null);
        }
    }

    public void SurveyCreatingDone(View view) {
        // TODO: 14.05.17 Server transaction;
    }

    public void addQuestion(View view) {
        Survey survey = null;
        EditText editText = null;
        Fragment newQuestion = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (newQuestion instanceof CreateQuestion) {
            editText = (EditText) newQuestion.getView().findViewById(R.id.newQuestText);
            survey = ((CreateQuestion) newQuestion).getSurvey();
            survey.getQuestions().add(new Question(editText.getText().toString(), new ArrayList<Answer>()));
            Bundle bundle = new Bundle();
            bundle.putSerializable("survey", survey);
            CreateAnswers createAnswers = new CreateAnswers();
            createAnswers.setArguments(bundle);
            getSupportFragmentManager().popBackStack();
            android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, createAnswers);
            transaction.commit();
            transaction.addToBackStack(null);
        }

    }

    public void onOneAnswerCreate(View view) {
        Survey survey = null;
        EditText editText = null;
        Fragment newAnswer = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (newAnswer instanceof CreateAnswers) {
            editText = (EditText) newAnswer.getView().findViewById(R.id.new_Answer);
            if (!editText.getText().toString().equals("")) {
                survey = ((CreateAnswers) newAnswer).getSurvey();
                survey.getQuestions().get(survey.getQuestions().size() - 1).getAnswers().add(new Answer(editText.getText().toString(), 0));
                editText.setText("");
            } else
                Toast.makeText(this, "Please enter the answer", Toast.LENGTH_SHORT).show();
        }
    }

    public void onAnswerCreate(View view) {
        Survey survey = null;
        EditText editText = null;
        Fragment newAnswer = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (newAnswer instanceof CreateAnswers) {
            survey = ((CreateAnswers) newAnswer).getSurvey();
            editText = (EditText) newAnswer.getView().findViewById(R.id.new_Answer);
            if (!editText.getText().toString().equals("")) {
                survey.getQuestions().get(survey.getQuestions().size() - 1).getAnswers().add(new Answer(editText.getText().toString(), 0));
            }
            if (survey.getQuestions().get(survey.getQuestions().size() - 1).getAnswers().size() != 0) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("survey", survey);
                CreateSurveyFragment createSurveyFragment = new CreateSurveyFragment();
                createSurveyFragment.setArguments(bundle);
                getSupportFragmentManager().popBackStack();
                android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, createSurveyFragment);
                transaction.commit();
            } else {
                Toast.makeText(this, "Please add answers for this question", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void onChangeSurvey(final View view) {
        final Fragment surveyFrag = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        Survey survey = ((CreateSurveyFragment) surveyFrag).getSurvey();
        CharSequence[] items;
        items = new CharSequence[survey.getQuestions().size()];
        for (int i = 0; i < survey.getQuestions().size(); i++) {
            items[i] = survey.getQuestions().get(i).getName();
        }
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("What would you like to change?");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                changeChoose(view, item);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void changeChoose(final View view, final int num) {
        final Fragment surveyFrag = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        CharSequence[] which = {"Change Question", "Change or add Answer", "Delete question", "Delete answer"};
        final Survey survey = ((CreateSurveyFragment) surveyFrag).getSurvey();
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("What would you like to do?");
        builder.setItems(which, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                if (item == 0) {
                    CreateQuestion createQuestion = new CreateQuestion();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("survey", survey);
                    bundle.putInt("questionInt", num);
                    createQuestion.setArguments(bundle);
                    android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, createQuestion);
                    transaction.commit();
                    transaction.addToBackStack(null);
                }
                if (item == 1) {
                    changeAns(view, num, false);
                }
                if (item == 2) {
                    survey.getQuestions().remove(num);
                    final android.support.v4.app.FragmentTransaction transaction =
                            getSupportFragmentManager().beginTransaction();
                    transaction.detach(surveyFrag);
                    transaction.attach(surveyFrag);
                    transaction.commit();
                }
                if (item == 3) {
                    changeAns(view, num, true);
                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void changeAns(final View view, final int num, final boolean erase) {
        final Fragment surveyFrag = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        final Survey survey = ((CreateSurveyFragment) surveyFrag).getSurvey();
        CharSequence[] items;
        items = new CharSequence[survey.getQuestions().get(num).getAnswers().size()];
        for (int i = 0; i < survey.getQuestions().get(num).getAnswers().size(); i++) {
            items[i] = survey.getQuestions().get(num).getAnswers().get(i).getName();
        }
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("What would you like to change?");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                if (!erase) {
                    CreateAnswers createQuestion = new CreateAnswers();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("survey", survey);
                    bundle.putInt("questInt", num);
                    bundle.putInt("ansInt", item);
                    createQuestion.setArguments(bundle);
                    android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, createQuestion);
                    transaction.commit();
                    transaction.addToBackStack(null);
                } else {
                    survey.getQuestions().get(num).getAnswers().remove(item);
                    Fragment fragment = null;
                    fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                    final android.support.v4.app.FragmentTransaction transaction =
                            getSupportFragmentManager().beginTransaction();
                    transaction.detach(fragment);
                    transaction.attach(fragment);
                    transaction.commit();
                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

}

