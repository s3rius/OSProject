package com.example.s3rius.surveyclient.fragments


import android.os.Bundle
import android.support.v4.app.ListFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter

import com.example.s3rius.surveyclient.R

import java.util.ArrayList
import java.util.Arrays


class TakenSurveys : ListFragment() {
    private val fuckList = arrayOf("Пластмассовый мир победил", "Макет оказался сильней", "Последний кораблик остыл", "Последний фонарик устал,", "а в горле сопят комья воспоминаний", "Оооооооо моя оборона", "Солнечный зайчик стеклянного глаза", "Оооооооо моя оборона", "Траурный мячик нелепого мира", "Траурный мячик дешёвого мира")


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        activity.title = getString(R.string.takenSurveys)
        return inflater!!.inflate(R.layout.fragment_taken_surveys, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val content = ArrayList(Arrays.asList(*fuckList))
        val adapter = ArrayAdapter(activity,
                android.R.layout.simple_list_item_1, content)
        listAdapter = adapter
    }

    fun takeTakenSurveys() {
        // FIXME: 18.03.17 get TakenSurveys List with links from server.
    }
}// Required empty public constructor
