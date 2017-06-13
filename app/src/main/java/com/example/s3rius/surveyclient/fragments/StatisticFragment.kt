package com.example.s3rius.surveyclient.fragments


import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.support.v4.app.ListFragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.s3rius.surveyclient.R
import com.example.s3rius.surveyclient.fragments.surveypac.Question
import com.example.s3rius.surveyclient.fragments.surveypac.Survey
import com.fasterxml.jackson.databind.ObjectMapper

import org.json.JSONObject

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class StatisticFragment : ListFragment() {

    internal var survey: Survey = null!!
    internal var id: Int = 0

    fun setSurvey(survey: Survey) {
        this.survey = survey
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        activity.setTitle(R.string.statistics)
        if (arguments != null) {
            id = arguments.getInt("id")
        }
        return inflater!!.inflate(R.layout.fragment_statistic, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.CUPCAKE)
    override fun onStart() {
        super.onStart()
        val statistics = ParseStatistics(id.toLong())
        statistics.execute()
    }

    private inner class ParseStatistics internal constructor(id: Long) : AsyncTask<Void, Void, String>() {

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
                val url = URL(getString(R.string.server) + "survey?id=" + id)

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

            } catch (e: Exception) {
                e.printStackTrace()
            }

            return resultJson
        }

        @RequiresApi(Build.VERSION_CODES.CUPCAKE)
        override fun onPostExecute(strJson: String) {
            super.onPostExecute(strJson)
            // выводим целиком полученную json-строку
            Log.d("LOG_TAG", strJson)
            val dataJsonObj: JSONObject
            val questions = ArrayList<Question>()
            try {
                val survey = ObjectMapper().readValue(resultJson, Survey::class.java)
                setSurvey(survey)
            } catch (e: IOException) {
                e.printStackTrace()
            }

            listAdapter = StatisticListAdapter(this@StatisticFragment.context, this@StatisticFragment.survey)
        }
    }

}// Required empty public constructor
