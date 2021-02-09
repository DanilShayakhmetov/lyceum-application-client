package com.example.lyceum_application_android_client.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.lyceum_application_android_client.DatabaseHelper
import com.example.lyceum_application_android_client.R
import com.example.lyceum_application_android_client.SessionManager
import com.example.lyceum_application_android_client.models.News


class NotificationsFragment : Fragment() {

    private lateinit var notificationsViewModel: NotificationsViewModel
    lateinit var handler: DatabaseHelper

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_notifications, container, false)
        val newsLayout: LinearLayout = root.findViewById(R.id.root_layout)
        val context = inflater.context
        handler = DatabaseHelper(context)
        val news = handler.getNews()
        if (news.isNotEmpty()) {
            for (i in news) {
                val newsTitle = TextView(context)
                val newsContent = TextView(context)
                val newsItem = i.value
                newsTitle.text = "".plus(String.format("%s%n", newsItem.title))
                newsTitle.textSize = 35.23F
                newsContent.text = "".plus(String.format("%s%n%s%n%n", newsItem.message, newsItem.creationTime))
                newsContent.textSize = 20.23F
                newsLayout.addView(newsTitle)
                newsLayout.addView(newsContent)
            }

        }
        return root
    }

}

