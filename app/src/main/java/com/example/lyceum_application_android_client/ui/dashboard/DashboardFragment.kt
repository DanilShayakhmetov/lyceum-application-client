package com.example.lyceum_application_android_client.ui.dashboard

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

class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel
    lateinit var handler: DatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val context = inflater.context
        handler = DatabaseHelper(context)
        val session = SessionManager(context)
        val class_id: String = session.userDetails.get("class_id").toString()
        val scheduleMonday = handler.getScheduleResultString("1", "1")

//        val scheduleMonday = getScheduleResultString(user.classId, "1")
//        val scheduleTuesday = getScheduleResultString(user.classId, "2")
//        val scheduleWednesday = getScheduleResultString(user.classId, "3")
//        val scheduleThursday = getScheduleResultString(user.classId, "4")
//        val scheduleFriday = getScheduleResultString(user.classId, "5")


        dashboardViewModel =
            ViewModelProviders.of(this).get(DashboardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        val textView: TextView = root.findViewById(R.id.text_dashboard)
        dashboardViewModel.text.observe(this, Observer {
            textView.text = scheduleMonday.toString()
        })
        return root
    }

}