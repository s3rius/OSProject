package com.example.s3rius.surveyclient.fragments


import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.ListFragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.s3rius.surveyclient.R
import com.example.s3rius.surveyclient.fragments.surveypac.Answer
import com.example.s3rius.surveyclient.fragments.surveypac.Question
import com.example.s3rius.surveyclient.fragments.surveypac.Survey
import com.fasterxml.jackson.databind.ObjectMapper

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.ArrayList


class SurveyFragment : ListFragment() {
    private val lol: Array<String>? = null

    var surveyId: Long = 0
        private set
    private var title: String? = null
    private var connectURL: String? = null // TODO: 22.05.17 Change IP.

    fun getSurvey(): Survey {
        return survey!!
    }


    fun setSurvey(survey: Survey) {
        SurveyFragment.survey = survey
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        if (arguments != null) {
            val arguments = arguments
            surveyId = arguments.getLong("id")
            title = arguments.getString("title")
            activity.title = title
        }
        return inflater!!.inflate(R.layout.fragment_survey, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.CUPCAKE)
    override fun onStart() {
        super.onStart()
        connectURL = getString(R.string.server) + "survey?id="
        val parseSurvey: ParseSurvey
        parseSurvey = ParseSurvey(surveyId)
        parseSurvey.execute()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        //ArrayList<String> content = new ArrayList<>(Arrays.asList(lol));
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
        //       android.R.layout.simple_list_item_1, content);
        //setListAdapter(adapter);
    }

    override fun getView(): View? {
        return super.getView()
    }


    private inner class ParseSurvey internal constructor(id: Long) : AsyncTask<Void, Void, String>() {

        internal var urlConnection: HttpURLConnection? = null
        internal var reader: BufferedReader? = null
        internal var resultJson = ""
        internal var id: Long = 0

        init {
            this.id = id
        }

        override fun doInBackground(vararg params: Void): String {
            // получаем данные с внешнего ресурса
            try {
                val url = URL(connectURL!! + id)

                urlConnection = url.openConnection() as HttpURLConnection
                urlConnection!!.requestMethod = "GET"
                urlConnection!!.connect()

                val inputStream = urlConnection!!.inputStream
                val buffer = StringBuffer()

                reader = BufferedReader(InputStreamReader(inputStream))

                var line: String = reader!!.readLine()
                while ((line) != null) {
                    buffer.append(line)
                }

                resultJson = buffer.toString()
                //                resultJson = "{\"name\":\"test1\",\"questions\":[{\"name\":\"question1\",\"answers\":[{\"name\":\"answer11\",\"answered\":\"2\"},{\"name\":\"answer12\",\"answered\":\"1\"},{\"name\":\"answer13\",\"answered\":\"2\"},{\"name\":\"answer14\",\"answered\":\"1\"}]},{\"name\":\"question2\",\"answers\":[{\"name\":\"answer21\",\"answered\":\"2\"},{\"name\":\"answer22\",\"answered\":\"1\"},{\"name\":\"answer23\",\"answered\":\"2\"},{\"name\":\"answer24\",\"answered\":\"1\"}]},{\"name\":\"question3\",\"answers\":[{\"name\":\"answer31\",\"answered\":\"2\"},{\"name\":\"answer32\",\"answered\":\"1\"},{\"name\":\"answer33\",\"answered\":\"2\"},{\"name\":\"answer34\",\"answered\":\"1\"}]},{\"name\":\"question4\",\"answers\":[{\"name\":\"answer41\",\"answered\":\"2\"},{\"name\":\"answer42\",\"answered\":\"1\"},{\"name\":\"answer43\",\"answered\":\"2\"},{\"name\":\"answer44\",\"answered\":\"1\"}]}]}";

            } catch (e: Exception) {
                e.printStackTrace()
            }

            return resultJson
        }

        @RequiresApi(Build.VERSION_CODES.CUPCAKE)
        override fun onPostExecute(strJson: String) {
            super.onPostExecute(strJson)
            // выводим целиком полученную json-строку
            Log.d(LOG_TAG, strJson)
            val dataJsonObj: JSONObject
            val questions = ArrayList<Question>()
            try {
                val survey = ObjectMapper().readValue(resultJson, Survey::class.java)
                setSurvey(survey)
            } catch (e: IOException) {
                e.printStackTrace()
            }

            //List<User> users = new ArrayList<>();
            //            try {
            //                dataJsonObj = new JSONObject(strJson);
            //                String surveyName = dataJsonObj.getString("name");
            //                // JSONArray usersArray = dataJsonObj.getJSONArray("users");
            //                JSONArray questionsJson = dataJsonObj.getJSONArray("questions");
            ////                for (int i = 0; i < usersArray.length(); i++) {
            //                    User user = new User();
            //                    JSONObject userObj = usersArray.getJSONObject(i);
            //                    user.setName(userObj.getString("name"));
            //                    users.add(user);
            //                }
            //                for (int i = 0; i < questionsJson.length(); i++) {
            //                    JSONObject question = questionsJson.getJSONObject(i);
            //                    String questionName = question.getString("name");
            //                    JSONArray answersJson = question.getJSONArray("answers");
            //                    List<Answer> answers = new ArrayList<>();
            //                    for (int j = 0; j < answersJson.length(); j++) {
            //                        JSONObject answer1 = answersJson.getJSONObject(j);
            //                        answers.add(new Answer(answer1.getString("name"), null));
            //                    }
            //                    questions.add(new Question(questionName, answers));
            //                }
            //                Survey survey1 = new Survey(surveyName, "comment", questions, null);
            //                setSurvey(survey1);
            listAdapter = SurveyListAdapter(this@SurveyFragment.context, survey!!)
            //            } catch (JSONException e) {
            //                e.printStackTrace();
            //            }
        }
    }

    companion object {

        var LOG_TAG = "my_log"
        private var survey: Survey? = null
    }
}// Required empty public constructor
