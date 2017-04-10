package com.example.s3rius.surveyclient;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
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
import com.example.s3rius.surveyclient.fragments.StatisticsFragment;
import com.example.s3rius.surveyclient.fragments.SurveyFragment;
import com.example.s3rius.surveyclient.fragments.TakeSurvey;
import com.example.s3rius.surveyclient.fragments.TakenSurveys;
import com.example.s3rius.surveyclient.fragments.Top100Fragment;

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

        if (id == R.id.my_surveys) {
            TakenSurveys fragment = new TakenSurveys();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
        } else if (id == R.id.category) {
            CategoryFragment fragment = new CategoryFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
        } else if (id == R.id.statistics) {
            StatisticsFragment fragment = new StatisticsFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
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
            }
        } else if (id == R.id.top100) {
            Top100Fragment fragment = new Top100Fragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
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
        ProfileFragment fragment = new ProfileFragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    void saveUser() {
        sPref = getPreferences(MODE_PRIVATE);
        Editor ed = sPref.edit();
        EditText loginField = (EditText) findViewById(R.id.loginlogin);
        EditText passField = (EditText) findViewById(R.id.passpass);

        ed.putString(SAVED_LOGIN, loginField.getText().toString());
        ed.putString(SAVED_PASS, passField.getText().toString());
        ed.apply();
        Toast.makeText(this, "Login and pass saved", Toast.LENGTH_SHORT).show();

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


    public void OnclickLogin(View view) {
        EditText loginField = (EditText) findViewById(R.id.loginlogin);
        EditText passField = (EditText) findViewById(R.id.passpass);
        if (isEmpty(loginField) || isEmpty(passField)) {
            Toast.makeText(this, "Please input the data", Toast.LENGTH_SHORT).show();
        } else {
            if (validateUser()) {

                saveUser();

                // get menu from navigationView
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
            } else {
                Toast.makeText(this, "Incorrect login and password", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean validateUser() {
        // FIXME: 18.03.17 Write server validation
        return true;
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
                // TODO: 05.04.17 Замутить отправку готового опроса на сервак.
            }
        }
    }

}
