package com.example.s3rius.surveyclient.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.s3rius.surveyclient.R


/**
 * A simple [Fragment] subclass.
 */
class RegistrationFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        activity.setTitle(R.string.registration)
        return inflater!!.inflate(R.layout.fragment_registration, container, false)
    }

}// Required empty public constructor
