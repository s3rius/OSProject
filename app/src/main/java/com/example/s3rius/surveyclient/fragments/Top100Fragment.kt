package com.example.s3rius.surveyclient.fragments


import android.app.ProgressDialog
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.FragmentTransaction
import android.support.v4.app.ListFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast

import com.example.s3rius.surveyclient.Drawer
import com.example.s3rius.surveyclient.R

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.ArrayList
import java.util.Arrays
import java.util.Collections


class Top100Fragment : ListFragment() {

    protected var surveyJson: String = ""

    private var container: ViewGroup? = null

    internal var surveyNames = ArrayList<String>()
    internal var surveIds = ArrayList<Int>()
    private var urlOfSurveys: String? = null // TODO: 21.05.17 IP CHANGER
    private var surveyFragment: SurveyFragment? = null

    @RequiresApi(Build.VERSION_CODES.CUPCAKE)
    override fun onStart() {
        super.onStart()
        urlOfSurveys = getString(R.string.server) + "topSurveys"
        val getSurveys: GetSurveys
        getSurveys = GetSurveys()
        getSurveys.execute()

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        this.container = container
        // Inflate the layout for this fragment
        activity.setTitle(R.string.top_surveys)
        return inflater!!.inflate(R.layout.fragment_top100, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onListItemClick(l: ListView?, v: View?, position: Int, id: Long) {
        super.onListItemClick(l, v, position, id)
        startSurvey(id)
    }

    private fun startSurvey(id: Long) {
        if ((activity as Drawer).isUserExist) {
            surveyFragment = SurveyFragment()
            val bundle = Bundle()
            bundle.putLong("id", surveIds[id.toInt()].toLong())
            bundle.putString("title", surveyNames[id.toInt()])
            surveyFragment!!.arguments = bundle
            // Create new fragment and transaction
            // consider using Java coding conventions (upper first char class names!!!)
            val transaction1 = fragmentManager.beginTransaction()
            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack
            transaction1.replace(R.id.fragment_container, surveyFragment)
            transaction1.addToBackStack(null)
            // Commit the transaction
            transaction1.commit()
        } else {
            Toast.makeText(container!!.context, getString(R.string.please_login_to_access), Toast.LENGTH_LONG).show()

        }
    }

    private inner class GetSurveys : AsyncTask<Void, Void, String>() {

        internal var urlConnection: HttpURLConnection? = null
        internal var reader: BufferedReader? = null
        internal var resultJson = ""
        internal var id: Long = 0

        override fun onPreExecute() {
            super.onPreExecute()
            surveyNames.clear()
            surveIds.clear()
        }

        override fun doInBackground(vararg params: Void): String {
            // получаем данные с внешнего ресурса
            try {
                val url = URL(urlOfSurveys)

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

            surveyJson = resultJson
            return resultJson
        }

        @RequiresApi(Build.VERSION_CODES.CUPCAKE)
        override fun onPostExecute(strJson: String) {

            super.onPostExecute(strJson)
            val dataJsonObj: JSONArray
            try {
                dataJsonObj = JSONArray(resultJson)
                for (i in 0..dataJsonObj.length() - 1) {
                    val obj = dataJsonObj.getJSONObject(i)
                    surveyNames.add(obj.getString("name"))
                    surveIds.add(obj.getInt("id"))
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            val adapter = ArrayAdapter(activity,
                    android.R.layout.simple_list_item_1, surveyNames)
            listAdapter = adapter
        }
    }
}// Required empty public constructor
