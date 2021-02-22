package com.example.lyceum_application_android_client.ui.notifications

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.lyceum_application_android_client.DatabaseHelper
import com.example.lyceum_application_android_client.R
import com.example.lyceum_application_android_client.SessionManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.add_news.view.*
import kotlinx.android.synthetic.main.fragment_notifications.view.*


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
        val session = SessionManager(context)
        val name: String = session.userDetails.get("name").toString()
        val classId: String = session.userDetails.get("class_id").toString()
        val news = handler.getNews(classId)
        val role = handler.getUserByName(name).roleId

        newsLayout.setPadding(20,100,0,0)

        if (role == "0") {
            root.add_news_button.visibility = View.GONE
        } else {
            root.add_news_button.setOnClickListener() {
                root.root_layout.visibility = View.GONE
                root.add_news_button.visibility = View.GONE
                root.add_news_layout.visibility = View.VISIBLE

            }

            root.add_news_save.setOnClickListener() {
                root.root_layout.visibility = View.VISIBLE
                root.add_news_button.visibility = View.VISIBLE
                root.add_news_layout.visibility = View.GONE
                addNews(root, name, classId)
                hideKeyboardFrom(context, root)

            }
        }
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

    fun addNews(root: View, name: String, class_id: String) {
        val addNews: LinearLayout = root.findViewById(R.id.add_news_layout)
        handler.insertNewsData(name, addNews.add_news_title.text.toString(), addNews.add_news_body.text.toString(), class_id)
    }

    fun hideKeyboardFrom(context: Context, view: View) {
        val imm: InputMethodManager = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

