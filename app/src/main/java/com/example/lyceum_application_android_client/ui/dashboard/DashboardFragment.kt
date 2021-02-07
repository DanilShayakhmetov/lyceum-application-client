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
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.fragment_dashboard.view.*

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
        val scheduleMonday = session.userDetails["schedule1"].toString()
        val scheduleTuesday = session.userDetails["schedule2"].toString()
        val scheduleWednesday = session.userDetails["schedule3"].toString()
        val scheduleThursday = session.userDetails["schedule4"].toString()
        val scheduleFriday = session.userDetails["schedule5"].toString()


        dashboardViewModel =
            ViewModelProviders.of(this).get(DashboardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)

        val monView: TextView = root.findViewById(R.id.mon_shed)
        val tueView: TextView = root.findViewById(R.id.tue_shed)
        val wedView: TextView = root.findViewById(R.id.wed_shed)
        val thuView: TextView = root.findViewById(R.id.thu_shed)
        val friView: TextView = root.findViewById(R.id.fri_shed)


        dashboardViewModel.monday.observe(this, Observer {
            monView.text =  scheduleMonday
        })
        dashboardViewModel.tuesday.observe(this, Observer {
            tueView.text =  scheduleTuesday
        })
        dashboardViewModel.wednesday.observe(this, Observer {
            wedView.text =  scheduleWednesday
        })
        dashboardViewModel.thursday.observe(this, Observer {
            thuView.text =  scheduleThursday
        })
        dashboardViewModel.friday.observe(this, Observer {
            friView.text = scheduleFriday
        })


        root.monday.setOnClickListener(){
            root.tue_shed.visibility = View.GONE
            root.wed_shed.visibility = View.GONE
            root.thu_shed.visibility = View.GONE
            root.fri_shed.visibility = View.GONE
            root.mon_shed.visibility = View.VISIBLE
        }

        root.tuesday.setOnClickListener(){
            root.tue_shed.visibility = View.VISIBLE
            root.wed_shed.visibility = View.GONE
            root.thu_shed.visibility = View.GONE
            root.fri_shed.visibility = View.GONE
            root.mon_shed.visibility = View.GONE
        }

        root.wednesday.setOnClickListener(){
            root.tue_shed.visibility = View.GONE
            root.wed_shed.visibility = View.VISIBLE
            root.thu_shed.visibility = View.GONE
            root.fri_shed.visibility = View.GONE
            root.mon_shed.visibility = View.GONE
        }

        root.thursday.setOnClickListener(){
            root.tue_shed.visibility = View.GONE
            root.wed_shed.visibility = View.GONE
            root.thu_shed.visibility = View.VISIBLE
            root.fri_shed.visibility = View.GONE
            root.mon_shed.visibility = View.GONE
        }

        root.friday.setOnClickListener(){
            root.tue_shed.visibility = View.GONE
            root.wed_shed.visibility = View.GONE
            root.thu_shed.visibility = View.GONE
            root.fri_shed.visibility = View.VISIBLE
            root.mon_shed.visibility = View.GONE
        }

        return root
    }
}