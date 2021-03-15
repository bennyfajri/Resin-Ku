package com.benny.resin_ku

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class Fragment_Stamina : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment__stamina, container, false)
    }

    companion object {
        fun newInstance(): Fragment{
            val fragment = Fragment_Stamina()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}