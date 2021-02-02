package com.example.lyceum_application_android_client.ui.home

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.lyceum_application_android_client.DatabaseHelper
import com.example.lyceum_application_android_client.R
import com.example.lyceum_application_android_client.SessionManager
import com.example.lyceum_application_android_client.models.Users

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel


    @SuppressLint("SetTextI18n")
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val context = inflater.context
        val handler: DatabaseHelper = DatabaseHelper(context)
        val session = SessionManager(context);
        super.onCreate(savedInstanceState)
        val pref: SharedPreferences = session.pref
        val name: String = pref.getString("username", "qwe").toString()
        var user = Users()
        user = handler.getUserByName(name)
        if (user.userName.isNotEmpty()) {
            homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel::class.java)
            val root = inflater.inflate(R.layout.fragment_home, container, false)
            val textViewName: TextView = root.findViewById(R.id.text_name)
            val textViewFirst: TextView = root.findViewById(R.id.text_firstName)
            val textViewLast: TextView = root.findViewById(R.id.text_lastName)
            val textViewMiddle: TextView = root.findViewById(R.id.text_middleName)
            val textViewEmail: TextView = root.findViewById(R.id.text_email)
            val textViewClass: TextView = root.findViewById(R.id.text_Class_id)

            homeViewModel.name.observe(this, Observer {
                textViewName.text = user.userName
            })
            homeViewModel.first.observe(this, Observer {
                textViewFirst.text = user.firstName
            })
            homeViewModel.last.observe(this, Observer {
                textViewLast.text = user.lastName
            })
            homeViewModel.middle.observe(this, Observer {
                textViewMiddle.text = user.middleName
            })
            homeViewModel.classId.observe(this, Observer {
                textViewClass.text = user.classId
            })
            homeViewModel.email.observe(this, Observer {
                textViewEmail.text = user.email
            })

            return root
        } else {
            homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel::class.java)
            val root = inflater.inflate(R.layout.fragment_home, container, false)
            val textViewName: TextView = root.findViewById(R.id.text_name)
            val textViewFirst: TextView = root.findViewById(R.id.text_firstName)
            val textViewLast: TextView = root.findViewById(R.id.text_lastName)
            val textViewMiddle: TextView = root.findViewById(R.id.text_middleName)
            val textViewEmail: TextView = root.findViewById(R.id.text_email)
            val textViewClass: TextView = root.findViewById(R.id.text_Class_id)
            homeViewModel.text.observe(this, Observer {
                textViewName.text = user.userName
                textViewFirst.text = user.firstName
                textViewLast.text = user.lastName
                textViewMiddle.text = user.middleName
                textViewEmail.text = user.email
                textViewClass.text = user.classId
            })
            return root
        }

    }
}