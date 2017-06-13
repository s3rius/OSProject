package com.example.s3rius.surveyclient.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.loopj.android.http.*
import com.example.s3rius.surveyclient.R


/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater!!.inflate(R.layout.fragment_login, container, false)
        val login = view.findViewById(R.id.buttonOkLogin) as Button
        val username = (view.findViewById(R.id.loginlogin) as EditText).text.toString()
        val userpass = (view.findViewById(R.id.passpass) as EditText).text.toString()
        activity.title = getString(R.string.login)
        return view
    }
}// Required empty public constructor

