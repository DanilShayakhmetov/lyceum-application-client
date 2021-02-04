package com.example.lyceum_application_android_client.ui.dashboard

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
import kotlinx.android.synthetic.main.fragment_home.view.*


class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel
    lateinit var handler: DatabaseHelper

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val context = inflater.context
        handler = DatabaseHelper(context)
        val session = SessionManager(context)
        val name: String = session.userDetails.get("name").toString()
        val user: Users = handler.getUserByName(name)
        val monday = handler.getClassSchedule(user.classId, "1")
        val tuesday = handler.getClassSchedule(user.classId, "2")
        val wednesday = handler.getClassSchedule(user.classId, "3")
        val thursday = handler.getClassSchedule(user.classId, "4")
        val friday = handler.getClassSchedule(user.classId, "5")
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        root.img_button.setOnClickListener() {
            root.image_layout.visibility = View.VISIBLE
        }

        if (user.userName.isNotEmpty()) {
            dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel::class.java)
            val mondFirst: TextView = root.findViewById(R.id.first_mond_subj)
            val mondSecond: TextView = root.findViewById(R.id.second_mond_subj)
            val mondThird: TextView = root.findViewById(R.id.third_mond_subj)
            val mondFourth: TextView = root.findViewById(R.id.fourth_mond_subj)
            val mondFifth: TextView = root.findViewById(R.id.fifth_mond_subj)
            val mondSixth: TextView = root.findViewById(R.id.sixth_mond_subj)
            val mondSeventh: TextView = root.findViewById(R.id.seventh_mond_subj)
            val mondEight: TextView = root.findViewById(R.id.eight_mond_subj)
            val tuesdFirs: TextView = root.findViewById(R.id.first_tuesd_subj)
            val tuesdSec: TextView = root.findViewById(R.id.second_tuesd_subj)
            val tuesdThird: TextView = root.findViewById(R.id.third_tuesd_subj)
            val tuesdFourth: TextView = root.findViewById(R.id.fourth_tuesd_subj)
            val tuesdFifth: TextView = root.findViewById(R.id.fifth_tuesd_subj)
            val tuesdSixth: TextView = root.findViewById(R.id.sixth_tuesd_subj)
            val tuesdSeventh: TextView = root.findViewById(R.id.seventh_tuesd_subj)
            val tuesdEight: TextView = root.findViewById(R.id.eight_tuesd_subj)
            val wedFirs: TextView = root.findViewById(R.id.first_wed_subj)
            val wedSec: TextView = root.findViewById(R.id.second_wed_subj)
            val wedThird: TextView = root.findViewById(R.id.third_wed_subj)
            val wedFourth: TextView = root.findViewById(R.id.fourth_wed_subj)
            val wedFifth: TextView = root.findViewById(R.id.fifth_wed_subj)
            val wedSixth: TextView = root.findViewById(R.id.sixth_wed_subj)
            val wedSeventh: TextView = root.findViewById(R.id.seventh_wed_subj)
            val wedEight: TextView = root.findViewById(R.id.eight_wed_subj)
            val thusdFirs: TextView = root.findViewById(R.id.first_thursd_subj)
            val thusdSec: TextView = root.findViewById(R.id.second_thursd_subj)
            val thusdThird: TextView = root.findViewById(R.id.third_thursd_subj)
            val thusdFourth: TextView = root.findViewById(R.id.fourth_thursd_subj)
            val thusdFifth: TextView = root.findViewById(R.id.fifth_thursd_subj)
            val thusdSixth: TextView = root.findViewById(R.id.sixth_thursd_subj)
            val thusdSeventh: TextView = root.findViewById(R.id.seventh_thursd_subj)
            val thusdEight: TextView = root.findViewById(R.id.eight_thursd_subj)
            val fridFirs: TextView = root.findViewById(R.id.first_friday_subj)
            val fridSec: TextView = root.findViewById(R.id.second_friday_subj)
            val fridThird: TextView = root.findViewById(R.id.third_friday_subj)
            val fridFourth: TextView = root.findViewById(R.id.fourth_friday_subj)
            val fridFifth: TextView = root.findViewById(R.id.fifth_friday_subj)
            val fridSixth: TextView = root.findViewById(R.id.sixth_friday_subj)
            val fridSeventh: TextView = root.findViewById(R.id.seventh_friday_subj)
            val fridEight: TextView = root.findViewById(R.id.eight_friday_subj)

            dashboardViewModel.text.observe(this, Observer {
                mondFirst.text = handler.getSubject("1", "1")
            })


            return root
        } else {
            dashboardViewModel =
                ViewModelProviders.of(this).get(dashboardViewModel::class.java)
            val textViewName: TextView = root.findViewById(R.id.text_name)
            val textViewFull: TextView = root.findViewById(R.id.text_fullName)
            val textViewEmail: TextView = root.findViewById(R.id.text_email)
            val textViewClass: TextView = root.findViewById(R.id.text_Class_id)
            dashboardViewModel.text.observe(this, Observer {
                textViewName.text = user.userName
                textViewFull.text = user.userName
                textViewEmail.text = user.email
                textViewClass.text = user.classId
            })
            return root
        }

    }
}