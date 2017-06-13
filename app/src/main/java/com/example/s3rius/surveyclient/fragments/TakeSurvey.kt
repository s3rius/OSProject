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
import com.example.s3rius.surveyclient.fragments.surveypac.Survey
import com.fasterxml.jackson.core.type.TypeReference
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


class TakeSurvey : ListFragment() {

    internal var content = ArrayList<String>()
    internal var ids = ArrayList<Int>()
    internal var fromCategory = false
    internal var act = -1
    var surveyFragment: SurveyFragment? = null
        private set
    private var categoryNo: Int = 0
    private var surveys: String? = null
    private var container: ViewGroup? = null


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        activity.setTitle(R.string.takeSurveys)
        this.container = container
        if (arguments != null) {
            if (arguments.getInt("act") == 0) {
                act = 0
                activity.title = arguments.getString("title")
                categoryNo = arguments.getInt("CatInt")
            }
            if (arguments.getInt("act") == 1) {
                act = 1
                surveys = arguments.getString("surveys list")
            }
        }
        return inflater!!.inflate(R.layout.fragment_take_survey, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.CUPCAKE)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (act == 0) {
            val getCatSurveys = GetCatSurveys()
            getCatSurveys.execute()
        }
        if (act == 1) {
            try {
                val surveys = ObjectMapper().readValue<ArrayList<Survey>>(this.surveys, object : TypeReference<List<Survey>>() {

                })
                for (i in surveys.indices) {
                    content.add(surveys[i].name)
                    ids.add(surveys[i].id)
                }
                val adapter = ArrayAdapter(activity,
                        android.R.layout.simple_list_item_1, content)
                listAdapter = adapter
            } catch (e: IOException) {
                e.printStackTrace()
            }

        } else {
            val getNewSurveys = GetNewSurveys()
            getNewSurveys.execute()
        }
    }

    override fun onListItemClick(l: ListView?, v: View?, position: Int, id: Long) {
        super.onListItemClick(l, v, position, id)
        startSurvey(id)
    }

    private fun startSurvey(id: Long) {
        if ((activity as Drawer).isUserExist) {
            surveyFragment = SurveyFragment()
            val bundle = Bundle()
            bundle.putLong("id", ids[id.toInt()].toLong())
            bundle.putString("title", content[id.toInt()])
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

    private inner class GetCatSurveys : AsyncTask<Void, Void, String>() {

        internal var progressDialog: ProgressDialog? = null
        internal var urlConnection: HttpURLConnection? = null
        internal var reader: BufferedReader? = null
        internal var resultJson = ""
        internal var id: Long = 0

        override fun onPreExecute() {
            super.onPreExecute()
            content.clear()
            ids.clear()
            progressDialog = ProgressDialog(container!!.context)
            progressDialog!!.setMessage(getString(R.string.please_wait))
            progressDialog!!.show()
        }

        override fun doInBackground(vararg params: Void): String {
            // получаем данные с внешнего ресурса
            try {
                val url = URL(getString(R.string.server) + "topics/")

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
            if (progressDialog != null)
                progressDialog!!.dismiss()
            super.onPostExecute(strJson)
            val dataJsonObj: JSONArray
            try {
                dataJsonObj = JSONArray(resultJson)
                val surveys = dataJsonObj.getJSONObject(categoryNo).getJSONArray("surveys")
                for (i in 0..surveys.length() - 1) {
                    val obj = surveys.getJSONObject(i)
                    content.add(obj.get("name").toString())
                    ids.add(obj.getInt("id"))
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            val adapter = ArrayAdapter(activity,
                    android.R.layout.simple_list_item_1, content)
            listAdapter = adapter
        }
    }

    private inner class GetNewSurveys : AsyncTask<Void, Void, String>() {

        internal var progressDialog: ProgressDialog? = null
        internal var urlConnection: HttpURLConnection? = null
        internal var reader: BufferedReader? = null
        internal var resultJson = ""
        internal var id: Long = 0

        override fun onPreExecute() {
            super.onPreExecute()
            content.clear()
            ids.clear()
            progressDialog = ProgressDialog(container!!.context)
            progressDialog!!.setMessage(getString(R.string.please_wait))
            progressDialog!!.show()
        }

        override fun doInBackground(vararg params: Void): String {
            // получаем данные с внешнего ресурса
            try {
                val url = URL(getString(R.string.server) + "topSurveys?sortBytime&limit=20")

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
            if (progressDialog != null)
                progressDialog!!.dismiss()
            super.onPostExecute(strJson)
            val dataJsonObj: JSONArray
            try {
                dataJsonObj = JSONArray(resultJson)
                for (i in 0..dataJsonObj.length() - 1) {
                    val obj = dataJsonObj.getJSONObject(i)
                    content.add(obj.get("name").toString())
                    ids.add(obj.getInt("id"))
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            val adapter = ArrayAdapter(activity,
                    android.R.layout.simple_list_item_1, content)
            listAdapter = adapter
        }
    }

}// Required empty public constructor

