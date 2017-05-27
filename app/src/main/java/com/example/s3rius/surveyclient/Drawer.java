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
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.s3rius.surveyclient.fragments.CategoryFragment;
import com.example.s3rius.surveyclient.fragments.LoginFragment;
import com.example.s3rius.surveyclient.fragments.ProfileFragment;
import com.example.s3rius.surveyclient.fragments.RegistrationFragment;
import com.example.s3rius.surveyclient.fragments.SurveyCreatorFragments.CreateAnswers;
import com.example.s3rius.surveyclient.fragments.SurveyCreatorFragments.CreateQuestion;
import com.example.s3rius.surveyclient.fragments.SurveyCreatorFragments.CreateSurveyFragment;
import com.example.s3rius.surveyclient.fragments.SurveyFragment;
import com.example.s3rius.surveyclient.fragments.TakeSurvey;
import com.example.s3rius.surveyclient.fragments.Top100Fragment;
import com.example.s3rius.surveyclient.fragments.surveypac.Answer;
import com.example.s3rius.surveyclient.fragments.surveypac.Question;
import com.example.s3rius.surveyclient.fragments.surveypac.Survey;
import com.example.s3rius.surveyclient.fragments.surveypac.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class Drawer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static final String SAVED_USER = "saved_user";
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private User user = null;
    private SharedPreferences sPref;
    private NavigationView navigationView = null;


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


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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
                fragmentTransaction.addToBackStack(null);
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
            fragmentTransaction.addToBackStack(null);
        } else if (id == R.id.createSurvey) {
            CreateSurveyFragment fragment = new CreateSurveyFragment();
            android.support.v4.app.FragmentTransaction transaction =
                    getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.commit();
            transaction.addToBackStack(null);
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

            bundle.putString("username", new ObjectMapper().readValue(sPref.getString(SAVED_USER, null), User.class).getName());
//            bundle.putString("username", "Tanana");
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

    void saveUser() throws JsonProcessingException {
        sPref = getPreferences(MODE_PRIVATE);
        Editor ed = sPref.edit();
        String JSONUser = new ObjectMapper().writeValueAsString(this.user);
        ed.putString(SAVED_USER, JSONUser);
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
        String url = String.format("%s/survey/client/login?login=%s&password=%s", getString(R.string.server), username, password); // TODO: 22.05.17 Change IP
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String lol = new String(responseBody);
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    user = mapper.readValue(lol, User.class);
                    Toast.makeText(context, "Welcome " + user.getName(), Toast.LENGTH_LONG).show();
                    saveUser();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                switch (statusCode) {
                    case 404:
                        Toast.makeText(context, "Incorrect login", Toast.LENGTH_LONG).show();
                        break;
                    case 400:
                        Toast.makeText(context, "Incorrect password", Toast.LENGTH_LONG).show();
                        break;
                    default:
                        Toast.makeText(context, "Connection error", Toast.LENGTH_LONG).show();
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
                                    answered += k + 1 + " ";
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

    public void uploadNewProfilePhoto(View view) {
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
        startActivityForResult(photoPickerIntent, 1);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        int RESULT_LOAD_IMAGE = 1;
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            final String picturePath = cursor.getString(columnIndex);
            cursor.close();

            // String picturePath contains the path of selected Image

            final ImageView imageView = (ImageView) findViewById(R.id.profile_pic);
            File fileToUpload = new File(picturePath);
//            UploadPhoto upload = new UploadPhoto(BitmapFactory.decodeFile(picturePath));
//            upload.execute();

//            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//            OkHttpClient client = new OkHttpClient.Builder().build();
//
//            Service service = new Retrofit.Builder().baseUrl(getString(R.string.server) + "/client/upload/").client(client).build().create(Service.class);
//
//            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), fileToUpload);
//            MultipartBody.Part body = MultipartBody.Part.createFormData("upload", fileToUpload.getName(), reqFile);
//            RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "upload_test");
//
//            retrofit2.Call<okhttp3.ResponseBody> req = service.postImage(body, name);
//            req.enqueue(new Callback<ResponseBody>() {
//                @Override
//                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
//
//                }
//
//                @Override
//                public void onFailure(Call<ResponseBody> call, Throwable t) {
//                    t.printStackTrace();
//                }
//            });
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            BitmapFactory.decodeFile(picturePath).compress(Bitmap.CompressFormat.JPEG, 100, baos);
//            byte[] imageBytes = baos.toByteArray();
//            String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

            AsyncHttpClient client = new AsyncHttpClient();

            File uploadImg = new File(picturePath);
            RequestParams params = new RequestParams();
            try {
                params.put("profile_picture", uploadImg);
                params.put("userLogin", user.getLogin());

                final ProgressDialog[] progressDialog = new ProgressDialog[1];
                client.post(getString(R.string.server) + "/survey/client/upload/", params, new AsyncHttpResponseHandler() { // TODO: 26.05.17 IP CHANGE

                    @Override
                    public void onStart() {
                        progressDialog[0] = new ProgressDialog(Drawer.this);
                        progressDialog[0].setMessage("Please Wait....");
                        progressDialog[0].show();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        if (progressDialog[0] != null)
                            progressDialog[0].dismiss();
                        imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        if (progressDialog[0] != null)
                            progressDialog[0].dismiss();
                        Toast.makeText(Drawer.this, "Upload to server failed", Toast.LENGTH_SHORT);
                    }
                });

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


//            HttpClient httpClient = AndroidHttpClient.newInstance("App");
//            HttpPost httpPost = new HttpPost(getString(R.string.server) + "/client/upload");
//
//            httpPost.setEntity(new FileEntity(new File(picturePath), "application/octet-stream"));
//
//            try {
//                HttpResponse response = httpClient.execute(httpPost);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }


//            final ProgressDialog[] progressDialog = {null};
//            Ion.with(Drawer.this)
//                    .load(getString(R.string.server) + "/survey/client/upload")
//                    .uploadProgressHandler(new ProgressCallback() {
//                        @Override
//                        public void onProgress(long uploaded, long total) {
//                            progressDialog[0] = new ProgressDialog(Drawer.this);
//                            progressDialog[0].setMessage("Uploading...");
//                            progressDialog[0].show();
//                        }
//                    })
//                    .setTimeout(60 * 60 * 1000)
//                    .setMultipartFile("upload", "image/jpeg/png", fileToUpload)
//                    .asJsonObject()
//                    // run a callback on completion
//                    .setCallback(new FutureCallback<JsonObject>() {
//                        @Override
//                        public void onCompleted(Exception e, JsonObject result) {
//                            // When the loop is finished, updates the notification
//                            if(progressDialog[0]!=null){
//                                progressDialog[0].dismiss();
//                            }
//                            if (e != null) {
//                                Toast.makeText(Drawer.this, "Error uploading file", Toast.LENGTH_LONG).show();
//                                return;
//                            }
//                            Toast.makeText(Drawer.this, "File upload complete", Toast.LENGTH_LONG).show();
//                        }
//                    });


//            UploadPhoto uploadPhoto = new UploadPhoto(picturePath);
//            uploadPhoto.execute();
//            HttpClient httpclient = new DefaultHttpClient();
//            HttpPost httppost = new HttpPost(getString(R.string.server) + "/client/upload");
//
//            MultipartEntityBuilder mpEntity = MultipartEntityBuilder.create();
//            mpEntity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
//                File file = new File(picturePath);
////                Log.d("EDIT USER PROFILE", "UPLOAD: file length = " + file.length());
////                Log.d("EDIT USER PROFILE", "UPLOAD: file exist = " + file.exists());
//                mpEntity.addPart("file", new FileBody(file, "application/octet"));
//            httppost.setEntity(mpEntity.build());
//            try {
//                HttpResponse response = httpclient.execute(httppost);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

        }
    }

    public void onRegistration(View view) {
        Fragment registration = new RegistrationFragment();
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, registration);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void onRegistrationDone(View view) {

    }

//
//    private class UploadPhoto extends AsyncTask<String, Integer, String> {
//
//        ProgressDialog progressDialog;
//        HttpURLConnection urlConnection = null;
//        BufferedReader reader = null;
//        String resultJson = "";
//        long id = 0;
//        String filepath;
//        private String surveyJson;
//        private HashMap<String, String> postDataParams;
//        Bitmap pictureBitmap;
//
//        UploadPhoto(Bitmap bitmap) {
//            pictureBitmap = bitmap;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            progressDialog = new ProgressDialog(Drawer.this);
//            progressDialog.setMessage("Please Wait....");
//            progressDialog.show();
//        }
//
//        @Override
//        protected String doInBackground(String... str) {
//            String line = "";
//            try {
//                URL url = new URL(getString(R.string.server) + "/survey/client/upload");
//
//                urlConnection = (HttpURLConnection) url.openConnection();
//                urlConnection.setRequestMethod("POST");
//                urlConnection.setUseCaches(false);
//                urlConnection.setDoOutput(true);
//
//                urlConnection.connect();
//
//                DataOutputStream request = new DataOutputStream(
//                        urlConnection.getOutputStream());
//                byte[] pixels = new byte[pictureBitmap.getWidth() * pictureBitmap.getHeight()];
//                for (int i = 0; i < pictureBitmap.getWidth(); ++i) {
//                    for (int j = 0; j < pictureBitmap.getHeight(); ++j) {
//                        //we're interested only in the MSB of the first byte,
//                        //since the other 3 bytes are identical for B&W images
//                        pixels[i + j] = (byte) ((pictureBitmap.getPixel(i, j) & 0x80) >> 7);
//                    }
//                }
//
//                request.write(pixels);
//                request.flush();
//                request.close();
//
//
//                InputStream inputStream = urlConnection.getInputStream();
//                StringBuffer buffer = new StringBuffer();
//
//                reader = new BufferedReader(new InputStreamReader(inputStream));
//
//
//                while ((line = reader.readLine()) != null) {
//                    buffer.append(line);
//                }
//                inputStream.close();
//                urlConnection.disconnect();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return line;
//        }
//
//        @Override
//        protected void onPostExecute(String response) {
//            super.onPostExecute(response);
//            if (progressDialog != null)
//                progressDialog.dismiss();
//        }
//    }
}

