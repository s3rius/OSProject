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

class CategoryFragment : ListFragment() {
    internal var content = ArrayList<String>()
    internal var container: ViewGroup = null!!


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        activity.setTitle(R.string.categories)
        this.container = container!!
        return inflater!!.inflate(R.layout.fragment_category, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.CUPCAKE)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val getCats = GetCats()
        getCats.execute()
    }

    override fun onListItemClick(l: ListView?, v: View?, position: Int, id: Long) {
        super.onListItemClick(l, v, position, id)
        takeSurveysByCategory(content[id.toInt()], id.toInt())
    }

    fun takeSurveysByCategory(category: String, id: Int) {
        val surveyFragment = TakeSurvey()
        val bundle = Bundle()
        bundle.putString("title", category)
        bundle.putInt("CatInt", id)
        bundle.putSerializable("act", 0)
        surveyFragment.arguments = bundle
        // Create new fragment and transaction
        // consider using Java coding conventions (upper first char class names!!!)
        val transaction1 = fragmentManager.beginTransaction()
        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack
        transaction1.replace(R.id.fragment_container, surveyFragment)
        // Commit the transaction
        transaction1.commit()
        transaction1.addToBackStack(null)
    }

    private inner class GetCats : AsyncTask<Void, Void, String>() {

        internal var progressDialog: ProgressDialog? = null
        internal var urlConnection: HttpURLConnection? = null
        internal var reader: BufferedReader? = null
        internal var resultJson = ""
        internal var id: Long = 0

        override fun onPreExecute() {
            super.onPreExecute()
            content.clear()
            progressDialog = ProgressDialog(container.context)
            progressDialog!!.setMessage("Please Wait....")
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
                for (i in 0..dataJsonObj.length() - 1) {
                    val obj = dataJsonObj.getJSONObject(i)
                    content.add(obj.get("name").toString())
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
