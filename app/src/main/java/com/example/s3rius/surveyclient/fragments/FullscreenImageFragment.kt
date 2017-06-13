package com.example.s3rius.surveyclient.fragments


import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import com.example.s3rius.surveyclient.R


/**
 * A simple [Fragment] subclass.
 */
class FullscreenImageFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_fullscreen_image, container, false)
        // Inflate the layout for this fragment
        activity.title = getString(R.string.profile_picture)
        (activity as AppCompatActivity).supportActionBar!!.hide()
        if (arguments != null) {
            val filePath = arguments.getString("imagePath")
            val image = view.findViewById(R.id.fullscreenImage) as SubsamplingScaleImageView
            image.setImage(ImageSource.bitmap(BitmapFactory.decodeFile(filePath)))
        }
        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as AppCompatActivity).supportActionBar!!.show()
    }
}// Required empty public constructor
