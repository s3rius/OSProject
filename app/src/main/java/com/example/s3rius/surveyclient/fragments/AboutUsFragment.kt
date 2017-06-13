package com.example.s3rius.surveyclient.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.example.s3rius.surveyclient.R


/**
 * A simple [Fragment] subclass.
 */
class AboutUsFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        activity.setTitle(R.string.about_us)
        val view = inflater!!.inflate(R.layout.fragment_about_us, container, false)
        val aboutUs = view.findViewById(R.id.textAboutUs) as TextView
        val text = getString(R.string.about_us_text)
        aboutUs.text = Html.fromHtml(text)
        return view
    }

}// Required empty public constructor
