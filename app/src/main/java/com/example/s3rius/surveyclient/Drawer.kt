package com.example.s3rius.surveyclient

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.annotation.RequiresApi
import android.support.design.widget.NavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast

import com.example.s3rius.surveyclient.fragments.AboutUsFragment
import com.example.s3rius.surveyclient.fragments.CategoryFragment
import com.example.s3rius.surveyclient.fragments.LoginFragment
import com.example.s3rius.surveyclient.fragments.ProfileFragment
import com.example.s3rius.surveyclient.fragments.RegistrationFragment
import com.example.s3rius.surveyclient.fragments.StatisticFragment
import com.example.s3rius.surveyclient.fragments.SurveyCreatorFragments.CreateAnswers
import com.example.s3rius.surveyclient.fragments.SurveyCreatorFragments.CreateQuestion
import com.example.s3rius.surveyclient.fragments.SurveyCreatorFragments.CreateSurveyFragment
import com.example.s3rius.surveyclient.fragments.SurveyFragment
import com.example.s3rius.surveyclient.fragments.TakeSurvey
import com.example.s3rius.surveyclient.fragments.Top100Fragment
import com.example.s3rius.surveyclient.fragments.surveypac.Answer
import com.example.s3rius.surveyclient.fragments.surveypac.Question
import com.example.s3rius.surveyclient.fragments.surveypac.Survey
import com.example.s3rius.surveyclient.fragments.surveypac.User
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.loopj.android.http.RequestParams
import com.squareup.picasso.Picasso

import org.apache.http.Header
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.util.ArrayList


class Drawer : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private val NEW_PROFILE_PICTURE = 1
    private val REGISTRATION_NEW_PIC = 2
    private var user: User? = null
    private var sPref: SharedPreferences? = null
    private var navigationView: NavigationView? = null
    private var profileIcon: ImageView? = null
    private var regPhoto: File? = null
    private var questionsQuan = 0


    @RequiresApi(Build.VERSION_CODES.ECLAIR_MR1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawer)

        //setting up the fragments
        val fragment = Top100Fragment()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.commit()


        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.setDrawerListener(toggle)
        toggle.syncState()

        navigationView = findViewById(R.id.nav_view) as NavigationView
        val headerView = navigationView!!.inflateHeaderView(R.layout.nav_header_drawer)
        profileIcon = headerView.findViewById(R.id.profile_image) as ImageView
        if (isUserExist) {
            val loginItem = navigationView!!.menu.findItem(R.id.login)
            loginItem.setTitle(R.string.logout)
            Picasso.with(this).load(getString(R.string.server) + "img?id=" + user!!.login).into(profileIcon)
        }
        navigationView!!.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId

        if (id == R.id.category) {
            val client = AsyncHttpClient()
            val fragment = CategoryFragment()
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_container, fragment)
            fragmentTransaction.commit()
            fragmentTransaction.addToBackStack(null)
            //        } else if (id == R.id.statistics) {
            //            StatisticsFragment fragment = new StatisticsFragment();
            //            android.support.v4.app.FragmentTransaction fragmentTransaction =
            //                    getSupportFragmentManager().beginTransaction();
            //            fragmentTransaction.replace(R.id.fragment_container, fragment);
            //            fragmentTransaction.commit();
        } else if (id == R.id.login) {
            if (!isUserExist) {
                val fragment = LoginFragment()
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.fragment_container, fragment)
                fragmentTransaction.commit()
                fragmentTransaction.addToBackStack("login")
            } else {
                sPref = getPreferences(Context.MODE_PRIVATE)
                val ed = sPref!!.edit()
                ed.clear()
                ed.apply()
                Toast.makeText(this, getString(R.string.logout_succ), Toast.LENGTH_SHORT).show()
                profileIcon!!.setImageBitmap(null)
                // get menu from navigationView
                val menu = navigationView!!.menu

                // find MenuItem you want to change
                val loginItem = menu.findItem(R.id.login)

                // set new title to the MenuItem
                loginItem.title = getString(R.string.login)

                val fragment = Top100Fragment()
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.fragment_container, fragment)
                fragmentTransaction.commit()
                fragmentTransaction.addToBackStack(null)
            }
        } else if (id == R.id.top100) {
            val fragment = Top100Fragment()
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_container, fragment)
            fragmentTransaction.commit()
            fragmentTransaction.addToBackStack(null)
        } else if (id == R.id.createSurvey) {
            if (isUserExist) {
                val fragment = CreateSurveyFragment()
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragment_container, fragment)
                transaction.commit()
                transaction.addToBackStack(null)
            } else {
                Toast.makeText(this, getString(R.string.please_login_to_create), Toast.LENGTH_LONG).show()
            }
        } else if (id == R.id.newestSurveys) {
            val fragment = TakeSurvey()
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_container, fragment)
            fragmentTransaction.commit()
            fragmentTransaction.addToBackStack(null)
        } else if (id == R.id.aboutUs) {
            val fragment = AboutUsFragment()
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_container, fragment)
            fragmentTransaction.commit()
            fragmentTransaction.addToBackStack(null)
        }
        //            else if(id==R.id.profile_image){
        //                ProfileFragment fragment = new ProfileFragment();
        //                android.support.v4.app.FragmentTransaction fragmentTransaction =
        //                        getSupportFragmentManager().beginTransaction();
        //                fragmentTransaction.replace(R.id.fragment_container, fragment);
        //                fragmentTransaction.commit();
        //            }
        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    @RequiresApi(Build.VERSION_CODES.DONUT)
    @Throws(IOException::class)
    fun profile_on_click(view: View) {
        if (isUserExist) {
            val fragment = ProfileFragment()
            val bundle = Bundle()

            bundle.putString("username", ObjectMapper().readValue<User>(sPref!!.getString(SAVED_USER, null), User::class.java!!).getName())
            bundle.putString("login", ObjectMapper().readValue<User>(sPref!!.getString(SAVED_USER, null), User::class.java!!).getLogin())
            fragment.arguments = bundle
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_container, fragment)
            fragmentTransaction.commit()
            fragmentTransaction.addToBackStack(null)
            val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
            drawer.closeDrawer(GravityCompat.START)
        } else {
            Toast.makeText(this, getString(R.string.please_login_to_profile), Toast.LENGTH_LONG).show()
        }
    }

    @Throws(JsonProcessingException::class)
    internal fun saveUser() {
        sPref = getPreferences(Context.MODE_PRIVATE)
        val ed = sPref!!.edit()
        val JSONUser = ObjectMapper().writeValueAsString(this.user)
        ed.putString(SAVED_USER, JSONUser)
        ed.apply()
        Picasso.with(this).load(getString(R.string.server) + "img?id=" + user!!.login).into(profileIcon)
        val menu = navigationView!!.menu

        // find MenuItem you want to change
        val loginItem = menu.findItem(R.id.login)
        // set new title to the MenuItem
        loginItem.title = "Logout"

        val fragment = TakeSurvey()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.commit()
    }

    //        return true;
    val isUserExist: Boolean
        get() {
            sPref = getPreferences(Context.MODE_PRIVATE)
            val Juser = sPref!!.getString(SAVED_USER, null)
            if (Juser != null) {
                try {
                    user = ObjectMapper().readValue<User>(sPref!!.getString(SAVED_USER, null), User::class.java!!)
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                return true
            }
            return false
        }

    fun OnclickLogin(view: View) {
        val context = this
        val username = (findViewById(R.id.loginlogin) as EditText).text.toString()
        val password = (findViewById(R.id.passpass) as EditText).text.toString()
        val url = String.format("%slogin?login=%s&password=%s", getString(R.string.server), username, password) // TODO: 22.05.17 Change IP
        val client = AsyncHttpClient()
        client.post(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                val lol = String(responseBody)
                try {
                    val mapper = ObjectMapper()
                    user = mapper.readValue<User>(lol, User::class.java!!)
                    Toast.makeText(context, getString(R.string.welcome) + user!!.name, Toast.LENGTH_LONG).show()
                    saveUser()

                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }

            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {

                when (statusCode) {
                    404 -> Toast.makeText(context, getString(R.string.incorrect_login), Toast.LENGTH_LONG).show()
                    400 -> Toast.makeText(context, getString(R.string.incorrect_password), Toast.LENGTH_LONG).show()
                    else -> Toast.makeText(context, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    fun OnclickCancelOnLogin(view: View) {
        val fragment = Top100Fragment()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.commit()
        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
    }

    fun onClickSurveyDone(view: View) {
        val answered = ArrayList<Int>()
        val isAllChecked = true
        val isChecked = false
        val surveyId: Long
        val surveyFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (surveyFragment is SurveyFragment) {
            surveyId = surveyFragment.surveyId
            val survey = surveyFragment.getSurvey()
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
            if (!survey.isAllAnswered) {
                Toast.makeText(this, getString(R.string.not_all_quest_ans), Toast.LENGTH_SHORT).show()
            } else {
                for (i in 0..survey.questions.size - 1) {
                    for (j in 0..survey.questions[i].answers.size - 1) {
                        if (survey.questions[i].answers[j].isAnswered) {
                            answered.add(j)
                        }
                    }
                }
                val client = AsyncHttpClient()
                val params = RequestParams()
                try {
                    params.put("answers", ObjectMapper().writeValueAsString(answered))
                } catch (e: JsonProcessingException) {
                    e.printStackTrace()
                }

                params.put("id", surveyId)
                params.put("login", user!!.login)
                val progressDialog = arrayOf(ProgressDialog(this))
                client.post(getString(R.string.server) + "doneSurvey/", params, object : AsyncHttpResponseHandler() {

                    override fun onStart() {
                        super.onStart()
                        progressDialog[0] = ProgressDialog(this@Drawer)
                        progressDialog[0].setMessage(getString(R.string.please_wait))
                        progressDialog[0].show()
                    }

                    override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                        if (progressDialog[0] != null)
                            progressDialog[0].dismiss()
                        val statisticFragment = StatisticFragment()
                        val bundle = Bundle()
                        bundle.putInt("id", surveyId.toInt())
                        statisticFragment.arguments = bundle
                        val transaction = supportFragmentManager.beginTransaction()
                        supportFragmentManager.popBackStack()
                        transaction.replace(R.id.fragment_container, statisticFragment)
                        transaction.commit()
                        transaction.addToBackStack(null)
                    }

                    override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                        if (progressDialog[0] != null)
                            progressDialog[0].dismiss()
                        Toast.makeText(this@Drawer, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
    }

    fun newSurvey(view: View) {
        val newSurvey = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (newSurvey is CreateSurveyFragment) {
            val bundle = Bundle()
            bundle.putSerializable("survey", newSurvey.survey)
            val createQuestion = CreateQuestion()
            createQuestion.arguments = bundle
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, createQuestion)
            transaction.commit()
            transaction.addToBackStack(null)
        }
    }

    fun SurveyCreatingDone(view: View) {
        val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (fragment is CreateSurveyFragment) {
            val doneSurvey = fragment.survey
            if (doneSurvey!!.questions.size != 0) {
                val li = LayoutInflater.from(this)
                val promptsView = li.inflate(R.layout.custom_alert_done_survey, null)
                val mDialogBuilder = AlertDialog.Builder(this)
                mDialogBuilder.setView(promptsView)
                val surveyName = promptsView.findViewById(R.id.surveyName) as EditText
                mDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK"
                        ) { dialog, id ->
                            doneSurvey.name = surveyName.text.toString()
                            doneSurvey.setMadeByUser(user)
                            doneSurvey.setUsers(ArrayList<User>())
                            val builder = AlertDialog.Builder(this@Drawer)
                            val cats = Array<Array<String>>(1) { Array<String>(1,{i -> ""}) }
                            val client1 = AsyncHttpClient()
                            client1.get(getString(R.string.server) + "topics/", object : AsyncHttpResponseHandler() {
                                override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                                    val responce = String(responseBody)
                                    var dataJsonObj: JSONArray? = null
                                    try {
                                        dataJsonObj = JSONArray(responce)
                                        cats[0] =  Array<String>(dataJsonObj.length(), {i -> "" })
                                        for (i in 0..dataJsonObj.length() - 1) {
                                            var obj: JSONObject? = null
                                            obj = dataJsonObj.getJSONObject(i)
                                            cats[0][i] = obj!!.get("name").toString()
                                        }
                                    } catch (e: JSONException) {
                                        e.printStackTrace()
                                    }

                                    builder.setTitle(getString(R.string.chooseCat))
                                    builder.setItems(cats[0]) { dialog, which ->
                                        doneSurvey.setCategory(cats[0][which])
                                        var createdSurvey = ""
                                        try {
                                            createdSurvey = ObjectMapper().writeValueAsString(doneSurvey)
                                        } catch (e: JsonProcessingException) {
                                            e.printStackTrace()
                                        }

                                        val client = AsyncHttpClient()
                                        val params = RequestParams()
                                        params.put("createdSurvey", createdSurvey)
                                        val progressDialog = arrayOfNulls<ProgressDialog>(1)
                                        client.post(getString(R.string.server) + "createdSurvey/", params, object : AsyncHttpResponseHandler() {
                                            override fun onStart() {
                                                super.onStart()
                                                progressDialog[0] = ProgressDialog(this@Drawer)
                                                progressDialog[0]!!.setMessage(getString(R.string.please_wait))
                                                progressDialog[0]!!.show()
                                            }

                                            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                                                if (progressDialog[0] != null)
                                                    progressDialog[0]!!.dismiss()
                                                Toast.makeText(this@Drawer, getString(R.string.succsessfullySent), Toast.LENGTH_SHORT).show()
                                                val fragment = TakeSurvey()
                                                val fragmentTransaction = supportFragmentManager.beginTransaction()
                                                fragmentTransaction.replace(R.id.fragment_container, fragment)
                                                fragmentTransaction.commit()
                                                fragmentTransaction.addToBackStack(null)
                                            }

                                            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                                                if (progressDialog[0] != null)
                                                    progressDialog[0]!!.dismiss()
                                                Toast.makeText(this@Drawer, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show()
                                            }
                                        })
                                    }
                                    val dialog1 = builder.create()
                                    dialog1.show()
                                }

                                override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                                    Toast.makeText(this@Drawer, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show()
                                }
                            })
                        }
                        .setNegativeButton(getString(R.string.cancel)
                        ) { dialog, id -> dialog.cancel() }
                val alertDialog = mDialogBuilder.create()
                alertDialog.show()
            } else {
                Toast.makeText(this, getString(R.string.survey_is_empty), Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun addQuestion(view: View) {
        var survey: Survey? = null
        var editText: EditText? = null
        val newQuestion = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (questionsQuan < 50) {
            if (newQuestion is CreateQuestion) {
                editText = newQuestion.getView()!!.findViewById(R.id.newQuestText) as EditText
                survey = newQuestion.survey
                if (editText.text.toString() != "") {
                    survey!!.questions.add(Question(editText.text.toString(), ArrayList<Answer>()))
                    val bundle = Bundle()
                    bundle.putSerializable("survey", survey)
                    val createAnswers = CreateAnswers()
                    createAnswers.arguments = bundle
                    supportFragmentManager.popBackStack()
                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.fragment_container, createAnswers)
                    transaction.commit()
                    transaction.addToBackStack(null)
                } else
                    Toast.makeText(this, R.string.empty_question, Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, R.string.max_quantity_ques, Toast.LENGTH_SHORT).show()
        }
    }

    fun onOneAnswerCreate(view: View) {
        var survey: Survey? = null
        var ansQuan = 0
        var editText: EditText? = null
        val newAnswer = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (ansQuan < 50) {
            if (newAnswer is CreateAnswers) {
                editText = newAnswer.getView()!!.findViewById(R.id.new_Answer) as EditText
                if (editText.text.toString() != "") {
                    survey = newAnswer.survey
                    survey!!.questions[survey.questions.size - 1].answers.add(Answer(editText.text.toString(), 0))
                    editText.setText("")
                    ansQuan++
                } else
                    Toast.makeText(this, R.string.enterAns, Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, R.string.max_quantity_ans, Toast.LENGTH_SHORT).show()
        }
    }

    fun onAnswerCreate(view: View) {
        var survey: Survey? = null
        var editText: EditText? = null
        val newAnswer = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (newAnswer is CreateAnswers) {
            survey = newAnswer.survey
            editText = newAnswer.getView()!!.findViewById(R.id.new_Answer) as EditText
            ++questionsQuan
            if (editText.text.toString() != "") {
                survey!!.questions[survey.questions.size - 1].answers.add(Answer(editText.text.toString(), 0))
            }
            if (survey!!.questions[survey.questions.size - 1].answers.size != 0) {
                val bundle = Bundle()
                bundle.putSerializable("survey", survey)
                val createSurveyFragment = CreateSurveyFragment()
                createSurveyFragment.arguments = bundle
                supportFragmentManager.popBackStack()
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragment_container, createSurveyFragment)
                transaction.commit()
            } else {
                Toast.makeText(this, R.string.add_answers, Toast.LENGTH_LONG).show()
            }
        }
    }

    fun onChangeSurvey(view: View) {
        val surveyFrag = supportFragmentManager.findFragmentById(R.id.fragment_container)
        val survey = (surveyFrag as CreateSurveyFragment).survey
        val items: Array<CharSequence>
        items = Array<CharSequence>(survey!!.questions.size + 1, {i -> ""})
        items[0] = getString(R.string.comment)
        for (i in 0..survey!!.questions.size - 1) {
            items[i + 1] = survey.questions[i].name
        }
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.what_want_change)
        builder.setItems(items) { dialog, item ->
            if (item == 0) {
                val li = LayoutInflater.from(this@Drawer)
                val promptsView = li.inflate(R.layout.custom_alert_done_survey, null)
                val mDialogBuilder = AlertDialog.Builder(this@Drawer)
                mDialogBuilder.setView(promptsView)
                val comment = promptsView.findViewById(R.id.surveyName) as EditText
                if (survey.comment != null)
                    comment.hint = survey.comment
                mDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK"
                        ) { dialog, id ->
                            if (comment.text.toString()=="") {
                                Toast.makeText(this@Drawer, R.string.empty_comment, Toast.LENGTH_SHORT).show()
                            } else {
                                survey.comment = comment.text.toString()
                            }
                        }
                        .setNegativeButton(getString(R.string.cancel)
                        ) { dialog, id -> dialog.cancel() }
                val alertDialog = mDialogBuilder.create()
                alertDialog.show()
            } else {
                changeChoose(view, item - 1)
            }
        }
        val alert = builder.create()
        alert.show()
    }

    fun changeChoose(view: View, num: Int) {
        val surveyFrag = supportFragmentManager.findFragmentById(R.id.fragment_container)
        val which = arrayOf<CharSequence>(getString(R.string.change_question), getString(R.string.change_answer), getString(R.string.add_answer), getString(R.string.delete_question), getString(R.string.delete_answer))
        val survey = (surveyFrag as CreateSurveyFragment).survey
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.what_want_do))
        builder.setItems(which) { dialog, item ->
            if (item == 0) {
                val createQuestion = CreateQuestion()
                val bundle = Bundle()
                bundle.putSerializable("survey", survey)
                bundle.putInt("questionInt", num)
                createQuestion.arguments = bundle
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragment_container, createQuestion)
                transaction.commit()
                transaction.addToBackStack(null)
            }
            if (item == 1) {
                changeAns(view, num, false, 0)
            }
            if (item == 2) {
                changeAns(view, num, false, 1)
            }
            if (item == 3) {
                survey!!.questions.removeAt(num)
                val transaction = supportFragmentManager.beginTransaction()
                transaction.detach(surveyFrag)
                transaction.attach(surveyFrag)
                transaction.commit()
            }
            if (item == 4) {
                changeAns(view, num, true, -1)
            }
        }
        val alert = builder.create()
        alert.show()
    }

    fun changeAns(view: View, num: Int, erase: Boolean, act: Int) {
        val surveyFrag = supportFragmentManager.findFragmentById(R.id.fragment_container)
        val survey = (surveyFrag as CreateSurveyFragment).survey
        if (act == 1) {
            val createAnswers = CreateAnswers()
            val bundle = Bundle()
            bundle.putSerializable("survey", survey)
            bundle.putInt("act", act)
            bundle.putInt("questInt", num)
            createAnswers.arguments = bundle
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, createAnswers)
            transaction.commit()
            transaction.addToBackStack(null)
        } else {
            val items: Array<CharSequence>
            items = Array<CharSequence>(survey!!.questions[num].answers.size, {i -> ""})
            for (i in 0..survey.questions[num].answers.size - 1) {
                items[i] = survey.questions[num].answers[i].name
            }
            val builder = AlertDialog.Builder(this)
            builder.setTitle(R.string.what_want_change)
            builder.setItems(items) { dialog, item ->
                if (!erase) {
                    val createAnswers = CreateAnswers()
                    val bundle = Bundle()
                    bundle.putSerializable("survey", survey)
                    bundle.putInt("act", act)
                    bundle.putInt("questInt", num)
                    bundle.putInt("ansInt", item)
                    createAnswers.arguments = bundle
                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.fragment_container, createAnswers)
                    transaction.commit()
                    transaction.addToBackStack(null)
                } else {
                    survey.questions[num].answers.removeAt(item)
                    var fragment: Fragment? = null
                    --questionsQuan
                    fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.detach(fragment)
                    transaction.attach(fragment)
                    transaction.commit()
                }
            }
            val alert = builder.create()
            alert.show()
        }
    }

    fun uploadNewProfilePhoto(view: View) {
        photoChooser(NEW_PROFILE_PICTURE)
    }

    fun photoChooser(request_code: Int) {
        if (Build.VERSION.SDK_INT >= 23) {
            // Here, thisActivity is the current activity
            if (ContextCompat.checkSelfPermission(this@Drawer,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this@Drawer,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {

                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(this@Drawer,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE)

                    // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            } else {
                ActivityCompat.requestPermissions(this@Drawer,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE)
            }
        }

        val photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"
        startActivityForResult(photoPickerIntent, request_code)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == NEW_PROFILE_PICTURE && resultCode == Activity.RESULT_OK && null != data) {
            val selectedImage = data.data
            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)

            val cursor = contentResolver.query(selectedImage, filePathColumn, null, null, null)
            cursor.moveToFirst()

            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
            val picturePath = cursor.getString(columnIndex)
            cursor.close()

            // String picturePath contains the path of selected Image

            val imageView = findViewById(R.id.profile_pic) as ImageView

            val client = AsyncHttpClient()

            val uploadImg = File(picturePath)
            val params = RequestParams()
            try {
                params.put("profile_picture", uploadImg)
                params.put("userLogin", user!!.login)

                val progressDialog = arrayOfNulls<ProgressDialog>(1)
                client.post(getString(R.string.server) + "/img/upload/", params, object : AsyncHttpResponseHandler() { // TODO: 26.05.17 IP CHANGE

                    override fun onStart() {
                        progressDialog[0] = ProgressDialog(this@Drawer)
                        progressDialog[0]!!.setMessage(getString(R.string.please_wait))
                        progressDialog[0]!!.show()
                    }

                    override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                        if (progressDialog[0] != null)
                            progressDialog[0]!!.dismiss()
                        //imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                        profileIcon!!.setImageBitmap(BitmapFactory.decodeFile(picturePath))
                        val profile = supportFragmentManager.findFragmentById(R.id.fragment_container)
                        val transaction = supportFragmentManager.beginTransaction()
                        transaction.detach(profile)
                        transaction.attach(profile)
                        transaction.commit()
                    }

                    override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                        if (progressDialog[0] != null)
                            progressDialog[0]!!.dismiss()
                        Toast.makeText(this@Drawer, R.string.something_went_wrong, Toast.LENGTH_SHORT).show()
                    }
                })
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }

        }
        if (requestCode == REGISTRATION_NEW_PIC && resultCode == Activity.RESULT_OK && null != data) {
            val selectedImage = data.data
            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)

            val cursor = contentResolver.query(selectedImage, filePathColumn, null, null, null)
            cursor.moveToFirst()

            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
            val picturePath = cursor.getString(columnIndex)
            cursor.close()
            regPhoto = File(picturePath)
        }
    }

    fun registrationPhoto(view: View) {
        photoChooser(REGISTRATION_NEW_PIC)
    }

    fun onRegistration(view: View) {
        val registration = RegistrationFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, registration)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun onRegistrationDone(view: View) {
        val name: EditText
        val surname: EditText
        val login: EditText
        val pass: EditText
        val passRep: EditText
        name = findViewById(R.id.newName) as EditText
        surname = findViewById(R.id.newSurname) as EditText
        login = findViewById(R.id.newLogin) as EditText
        pass = findViewById(R.id.newPassword) as EditText
        passRep = findViewById(R.id.newPasswordRepeat) as EditText
        if (name.text.toString() == "" ||
                surname.text.toString() == "" ||
                login.text.toString() == "" ||
                pass.text.toString() == ""||
                passRep.text.toString() == "") {
            Toast.makeText(this, getString(R.string.not_all_fields_filled), Toast.LENGTH_SHORT).show()
        } else if (pass.text.toString() != passRep.text.toString()) {
            Toast.makeText(this, getString(R.string.password_dont_match), Toast.LENGTH_SHORT).show()
        } else {
            val newUser = User()
            newUser.name = name.text.toString()
            newUser.lastName = surname.text.toString()
            newUser.login = login.text.toString()
            newUser.password = pass.text.toString()

            val client = AsyncHttpClient()

            val params = RequestParams()

            try {
                params.put("newUser", ObjectMapper().writeValueAsString(newUser))

                val progressDialog = arrayOfNulls<ProgressDialog>(1)
                client.post(getString(R.string.server) + "registration/", params, object : AsyncHttpResponseHandler() { // TODO: 26.05.17 IP CHANGE

                    override fun onStart() {
                        progressDialog[0] = ProgressDialog(this@Drawer)
                        progressDialog[0]!!.setMessage(getString(R.string.please_wait))
                        progressDialog[0]!!.show()
                    }

                    override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                        val client = AsyncHttpClient()

                        val params = RequestParams()
                        if (regPhoto != null) {
                            try {
                                params.put("profile_picture", regPhoto)
                            } catch (e: FileNotFoundException) {
                                e.printStackTrace()
                            }

                            params.put("userLogin", newUser.login)
                            client.post(getString(R.string.server) + "img/upload/", params, object : AsyncHttpResponseHandler() {
                                override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                                    if (progressDialog[0] != null)
                                        progressDialog[0]!!.dismiss()
                                    val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
                                    if (fragment is LoginFragment) {
                                        val login = fragment.getView()!!.findViewById(R.id.loginlogin) as EditText
                                        val pass = fragment.getView()!!.findViewById(R.id.passpass) as EditText
                                        login.setText(newUser.login)
                                        pass.setText(newUser.password)
                                    }
                                    Toast.makeText(this@Drawer, getString(R.string.registration_complete), Toast.LENGTH_SHORT).show()
                                }

                                override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                                    if (progressDialog[0] != null)
                                        progressDialog[0]!!.dismiss()
                                    Toast.makeText(this@Drawer, getString(R.string.picture_upload_error), Toast.LENGTH_SHORT).show()
                                }
                            })
                        }
                        if (progressDialog[0] != null)
                            progressDialog[0]!!.dismiss()
                        onBackPressed()
                        val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
                        if (fragment is LoginFragment) {
                            val login = fragment.getView()!!.findViewById(R.id.loginlogin) as EditText
                            val pass = fragment.getView()!!.findViewById(R.id.passpass) as EditText
                            login.setText(newUser.login)
                            pass.setText(newUser.password)
                        }
                        Toast.makeText(this@Drawer, getString(R.string.registration_complete), Toast.LENGTH_SHORT).show()
                    }

                    override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                        if (progressDialog[0] != null)
                            progressDialog[0]!!.dismiss()
                        when (statusCode) {
                            400 -> Toast.makeText(this@Drawer, R.string.login_owned, Toast.LENGTH_SHORT).show()
                            else -> Toast.makeText(this@Drawer, R.string.something_went_wrong, Toast.LENGTH_SHORT).show()
                        }
                    }
                })

            } catch (e: JsonProcessingException) {
                e.printStackTrace()
            }

        }
    }

    fun deleteProfilePicture(view: View) {
        val client = AsyncHttpClient()
        val params = RequestParams()
        params.put("login", user!!.login)
        client.delete(getString(R.string.server) + "img/delete", params, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
                if (fragment is ProfileFragment) {
                    val profilePic = fragment.getView()!!.findViewById(R.id.profile_pic) as ImageView
                    profilePic.setImageBitmap(BitmapFactory.decodeResource(resources, R.drawable.no_image))
                    profileIcon!!.setImageBitmap(null)
                    profilePic.setOnClickListener { }
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                Toast.makeText(this@Drawer, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun onAnswerBack(view: View) {
        val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        val survey = (fragment as CreateAnswers).survey
        survey.questions.removeAt(survey.questions.size - 1)
        fragment.survey = survey
        supportFragmentManager.popBackStack()
    }

    fun onQuestionBack(view: View) {
        val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (!(fragment as CreateQuestion).survey.questions.isEmpty()) {
            if (fragment
                    .survey
                    .questions[fragment.survey.questions.size - 1]
                    .answers.size == 0) {
                val at = fragment.survey.questions.size - 1;
                (fragment as CreateAnswers).survey.questions
                        .removeAt(at)
            }
        }
        supportFragmentManager.popBackStack()
    }

    fun getCompletedSurveys(view: View) {
        getProfileSurveys("user/doneSurveys")
    }

    fun getMadeSurveys(view: View) {
        getProfileSurveys("user/madeSurveys")
    }

    fun getProfileSurveys(url: String) {
        val client = AsyncHttpClient()
        val params = RequestParams()
        params.put("login", user!!.login)
        val dialog = arrayOf<ProgressDialog>()
        client.get(getString(R.string.server) + url, params, object : AsyncHttpResponseHandler() {
            override fun onStart() {
                super.onStart()
                dialog[0] = ProgressDialog(this@Drawer)
                dialog[0].setMessage(getString(R.string.please_wait))
                dialog[0].show()
            }

            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                if (dialog[0] != null)
                    dialog[0].dismiss()
                val surveys = responseBody.toString()
                if (surveys != "null") {
                    val bundle = Bundle()
                    val fragment = TakeSurvey()
                    bundle.putInt("act", 1)
                    bundle.putString("surveys list", surveys)
                    fragment.arguments = bundle
                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.fragment_container, fragment)
                    transaction.commit()
                    transaction.addToBackStack(null)
                } else {
                    Toast.makeText(this@Drawer, R.string.no_done_surveys, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                if (dialog[0] != null)
                    dialog[0].dismiss()
                Toast.makeText(this@Drawer, R.string.something_went_wrong, Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun deleteProfile(view: View) {
        val client = AsyncHttpClient()
        val params = RequestParams()
        params.put("login", user!!.login)
        val progressDialog = arrayOf<ProgressDialog>()
        client.delete(getString(R.string.server) + "deleteProfile/", params, object : AsyncHttpResponseHandler() {
            override fun onStart() {
                super.onStart()
                progressDialog[0] = ProgressDialog(this@Drawer)
                progressDialog[0].setMessage(getString(R.string.please_wait))
                progressDialog[0].show()
            }

            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                if (progressDialog[0] != null)
                    progressDialog[0].dismiss()
                sPref = getPreferences(Context.MODE_PRIVATE)
                val ed = sPref!!.edit()
                ed.clear()
                ed.apply()
                Toast.makeText(this@Drawer, getString(R.string.delete_profile_succ), Toast.LENGTH_SHORT).show()
                profileIcon!!.setImageBitmap(null)
                // get menu from navigationView
                val menu = navigationView!!.menu

                // find MenuItem you want to change
                val loginItem = menu.findItem(R.id.login)

                // set new title to the MenuItem
                loginItem.title = getString(R.string.login)

                val fragment = Top100Fragment()
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.fragment_container, fragment)
                fragmentTransaction.commit()
                fragmentTransaction.addToBackStack(null)
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                if (progressDialog[0] != null)
                    progressDialog[0].dismiss()
                Toast.makeText(this@Drawer, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show()
            }
        })
    }

    companion object {
        private val SAVED_USER = "saved_user"
        private val MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1
    }
}

