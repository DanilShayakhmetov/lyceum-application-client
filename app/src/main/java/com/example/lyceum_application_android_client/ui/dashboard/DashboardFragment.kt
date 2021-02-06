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
        val textView: TextView = root.findViewById(R.id.text_dashboard)
        val mondayView: TextView = root.findViewById(R.id.monday_schedule)
        val tuesdayView: TextView = root.findViewById(R.id.tuesday_schedule)
        val wednesdayView: TextView = root.findViewById(R.id.wednesday_schedule)
        val thursdayView: TextView = root.findViewById(R.id.thursday_schedule)
        val fridayView: TextView = root.findViewById(R.id.friday_schedule)

        root.monday.setOnClickListener(){
            showMonday(root)
        }
        root.tuesday.setOnClickListener(){
            showTuesday(root)
        }
        root.wednesday.setOnClickListener(){
            showWednesday(root)
        }
        root.thursday.setOnClickListener(){
            showThursday(root)
        }
        root.friday.setOnClickListener(){
            showFriday(root)
        }



        dashboardViewModel.text.observe(this, Observer {
            textView.text = ""
        })

        dashboardViewModel.monday.observe(this, Observer {
            mondayView.text = scheduleMonday
        })

        dashboardViewModel.tuesday.observe(this, Observer {
            tuesdayView.text = scheduleTuesday
        })

        dashboardViewModel.wednesday.observe(this, Observer {
            wednesdayView.text = scheduleWednesday
        })

        dashboardViewModel.thursday.observe(this, Observer {
            thursdayView.text = scheduleThursday
        })

        dashboardViewModel.friday.observe(this, Observer {
            fridayView.text = scheduleFriday
        })
        return root
    }

    private fun showMonday(root: View) {
        root.monday_schedule.visibility = View.VISIBLE
        root.tuesday_schedule.visibility = View.GONE
        root.wednesday_schedule.visibility = View.GONE
        root.thursday_schedule.visibility = View.GONE
        root.friday_schedule.visibility = View.GONE
    }

    private fun showTuesday(root: View) {
        root.monday_schedule.visibility = View.GONE
        root.tuesday_schedule.visibility = View.VISIBLE
        root.wednesday_schedule.visibility = View.GONE
        root.thursday_schedule.visibility = View.GONE
        root.friday_schedule.visibility = View.GONE
    }

    private fun showWednesday(root: View) {
        root.monday_schedule.visibility = View.GONE
        root.tuesday_schedule.visibility = View.GONE
        root.wednesday_schedule.visibility = View.VISIBLE
        root.thursday_schedule.visibility = View.GONE
        root.friday_schedule.visibility = View.GONE
    }

    private fun showThursday(root: View) {
        root.monday_schedule.visibility = View.GONE
        root.tuesday_schedule.visibility = View.GONE
        root.wednesday_schedule.visibility = View.GONE
        root.thursday_schedule.visibility = View. VISIBLE
        root.friday_schedule.visibility = View.GONE
    }

    private fun showFriday(root: View) {
        root.monday_schedule.visibility = View.GONE
        root.tuesday_schedule.visibility = View.GONE
        root.wednesday_schedule.visibility = View.GONE
        root.thursday_schedule.visibility = View.GONE
        root.friday_schedule.visibility = View.VISIBLE
    }

}