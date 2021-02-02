package com.example.lyceum_application_android_client.ui.home

import android.annotation.SuppressLint
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
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.view.*


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
        val name: String = session.userDetails.get("name").toString()
        val user: Users  = handler.getUserByName(name)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val imgView = inflater.inflate(R.layout.image, container, false)
        root.img_button.setOnClickListener() {
            showImage(root)
        }
        if (user.userName.isNotEmpty()) {
            homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel::class.java)
            val textViewName: TextView = root.findViewById(R.id.text_name)
            val textViewFull: TextView = root.findViewById(R.id.text_fullName)
            val textViewEmail: TextView = root.findViewById(R.id.text_email)
            val textViewClass: TextView = root.findViewById(R.id.text_Class_id)

            homeViewModel.name.observe(this, Observer {
                textViewName.text = user.userName
            })
            homeViewModel.first.observe(this, Observer {
                textViewFull.text = user.lastName
                    .plus(" ")
                    .plus(user.firstName)
                    .plus(" ")
                    .plus(user.middleName)
                    .plus(" ")
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
            val textViewName: TextView = root.findViewById(R.id.text_name)
            val textViewFull: TextView = root.findViewById(R.id.text_fullName)
            val textViewEmail: TextView = root.findViewById(R.id.text_email)
            val textViewClass: TextView = root.findViewById(R.id.text_Class_id)
            homeViewModel.text.observe(this, Observer {
                textViewName.text = user.userName
                textViewFull.text = user.userName
                textViewEmail.text = user.email
                textViewClass.text = user.classId
            })
            return root
        }

    }

    private fun showImage(image: View) {
        image.image_layout.visibility = View.VISIBLE
    }

}