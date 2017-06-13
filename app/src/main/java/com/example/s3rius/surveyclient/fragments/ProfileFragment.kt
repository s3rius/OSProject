package com.example.s3rius.surveyclient.fragments


import android.app.FragmentTransaction
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.example.s3rius.surveyclient.Drawer
import com.example.s3rius.surveyclient.R
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.FileAsyncHttpResponseHandler
import com.squareup.picasso.Picasso

import org.apache.http.Header

import java.io.File


/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_profile, container, false)
        // Inflate the layout for this fragment
        activity.title = getString(R.string.profile)
        val arguments = arguments
        val username = view.findViewById(R.id.profile_name) as TextView
        username.text = arguments.getString("username")
        //        ImageView profilePic = (ImageView)view.findViewById(R.id.profile_pic);
        //        Picasso.with(container.getContext()).load(getString(R.string.server) + "img?id=" + arguments.getString("username")).into(profilePic);

        val client = AsyncHttpClient()
        val progressDialog = arrayOfNulls<ProgressDialog>(1)
        client.get(getString(R.string.server) + "img?id=" + arguments.getString("login"), object : FileAsyncHttpResponseHandler(container!!.context) {
            override fun onFailure(statusCode: Int, headers: Array<Header>, throwable: Throwable, file: File) {
                progressDialog[0]!!.dismiss()
            }

            override fun onStart() {
                super.onStart()
                progressDialog[0] = ProgressDialog(container!!.context)
                progressDialog[0]!!.setMessage(getString(R.string.please_wait))
                progressDialog[0]!!.show()
            }

            override fun onSuccess(statusCode: Int, headers: Array<Header>, file: File) {
                progressDialog[0]!!.dismiss()
                val pic = view.findViewById(R.id.profile_pic) as ImageView
                pic.setImageBitmap(BitmapFactory.decodeFile(file.path))
                pic.setOnClickListener {
                    val fullscreen = FullscreenImageFragment()
                    val bundle = Bundle()
                    bundle.putString("imagePath", file.path)
                    fullscreen.arguments = bundle
                    val transaction = fragmentManager.beginTransaction()
                    transaction.replace(R.id.fragment_container, fullscreen)
                    transaction.commit()
                    transaction.addToBackStack(null)
                }
            }
        })

        return view
    }
}// Required empty public constructor
