package com.example.lyceum_application_android_client.ui.notifications

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProviders
import com.example.lyceum_application_android_client.DatabaseHelper
import com.example.lyceum_application_android_client.MainActivity
import com.example.lyceum_application_android_client.R
import com.example.lyceum_application_android_client.SessionManager
import com.example.lyceum_application_android_client.models.News
import kotlinx.android.synthetic.main.add_news.view.*
import kotlinx.android.synthetic.main.change_news_visibility.view.*
import kotlinx.android.synthetic.main.fragment_notifications.view.*


class NotificationsFragment : Fragment() {

    private lateinit var notificationsViewModel: NotificationsViewModel
    lateinit var handler: DatabaseHelper
    lateinit var visible_g: String

    @SuppressLint("ResourceType")
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_notifications, container, false)
        val newsLayout: LinearLayout = root.findViewById(R.id.root_layout)
        val changeVisibilityLayout: LinearLayout = root.findViewById(R.id.change_news_visibility_layout)
        val context = inflater.context
        handler = DatabaseHelper(context)
        val session = SessionManager(context)
        val name: String = session.userDetails.get("name").toString()
        val classId: String = session.userDetails.get("class_id").toString()
        val role = handler.getUserByName(name).roleId
        var news = handler.getNews(role)


        newsLayout.setPadding(20,100,0,0)

        if (role == "0") {
//            root.add_news_button.visibility = View.GONE
            news = getFiltered(news,classId)
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
                addNews(root, name, classId, role)
                hideKeyboardFrom(context, root)
                val transaction: FragmentTransaction = fragmentManager!!.beginTransaction()
                if (Build.VERSION.SDK_INT >= 26) {
                    transaction.setReorderingAllowed(false)
                }
                transaction.detach(this).attach(this).commit()
            }
        }


        if (news.isNotEmpty()) {
            var counter = 0
             for (i in news) {
                val newsTitle = TextView(context)
                val newsContent = TextView(context)
                val newsStatus = TextView(context)
                val visibilityStatus = TextView(context)
                val changeVisibility = Button(context)
                val newsItem = i.value
                newsTitle.text = "".plus(String.format("%s%n", newsItem.title))
                newsTitle.textSize = 35.23F
                newsContent.text = "".plus(String.format("%s%n%s%n%n", newsItem.message, newsItem.creationTime))
                newsContent.textSize = 20.23F
                newsStatus.text = "".plus(String.format("%s%n", STATUSES[newsItem.visibilityId]))
                newsStatus.textSize = 20.23F
                visibilityStatus.text = "".plus(String.format("%s%n%n%n%n", STATUSES[newsItem.visibilityId]))
                visibilityStatus.textSize = 30.23F
                newsLayout.addView(newsTitle)
                newsLayout.addView(newsContent)
                newsLayout.addView(newsStatus)
                if (role == "1") {
                    changeVisibility.text = "изменить видимость"
                    changeVisibility.textSize = 15.23F
                    changeVisibility.width = 15
                    changeVisibility.id = newsItem.id
                    newsLayout.addView(changeVisibility)
                    if (counter == 0) {
                        changeVisibilityLayout.addView(visibilityStatus)
                        counter++
                    }
                    changeVisibility.setOnClickListener() {
//                        root.change_news_visibility_layout.visibility = View.VISIBLE
//                        root.root_layout.visibility = View.GONE
//                        root.add_news_button.visibility = View.GONE
                        showButtons(root)
                        Log.d("TAG", changeVisibility.id.toString())
                        visible_g = changeVisibility.id.toString()
                    }
                    root.news_show_all.setOnClickListener() {
                        handler.updateNewsVisibility(visible_g, "1")
                        Log.d("TAG", visible_g)
                        hideButtons(root)
                    }
                    root.news_show_teachers.setOnClickListener() {
                        handler.updateNewsVisibility(visible_g, "2")
                        Log.d("TAG", visible_g)
                        hideButtons(root)
                    }
                    root.news_show_students.setOnClickListener() {
                        handler.updateNewsVisibility(visible_g, "3")
                        Log.d("TAG", visible_g)
                        hideButtons(root)
                    }
                    root.news_show_my_students.setOnClickListener() {
                        handler.updateNewsVisibility(visible_g, "4")
                        Log.d("TAG", visible_g)
                        hideButtons(root)
                    }
                }
            }
        }


        return root
    }

    fun addNews(root: View, name: String, class_id: String, role: String) {
        val addNews: LinearLayout = root.findViewById(R.id.add_news_layout)
        Log.d("TAG", handler.insertNewsData(name, addNews.add_news_title.text.toString(), addNews.add_news_body.text.toString(), class_id, role).toString())
    }

    fun getFiltered(news: Map <Int, News>, class_id: String): MutableMap<Int, News> {
        val newsId = handler.getNewsID(class_id)
        val newsFiltered = mutableMapOf<Int, News>()
        for (newsItem in news) {
            val id = newsItem.value.id
            val visibility = newsItem.value.visibilityId
            if (newsId.contains(id) && visibility == "4") {
                newsFiltered[id] = newsItem.value
            }
        }
        return newsFiltered
    }

    fun hideKeyboardFrom(context: Context, view: View) {
        val imm: InputMethodManager = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun showButtons(root: View) {
        root.change_news_visibility_layout.visibility = View.VISIBLE
        root.root_layout.visibility = View.GONE
        root.add_news_button.visibility = View.GONE
    }

    fun hideButtons(root: View) {
        root.change_news_visibility_layout.visibility = View.GONE
        root.root_layout.visibility = View.VISIBLE
        root.add_news_button.visibility = View.VISIBLE
        val transaction: FragmentTransaction = fragmentManager!!.beginTransaction()
        if (Build.VERSION.SDK_INT >= 26) {
            transaction.setReorderingAllowed(false)
        }
        transaction.detach(this).attach(this).commit()
    }

    private val STATUSES = mutableMapOf<String, String>("1" to "Всем", "2" to "Учителям", "3" to "Ученникам", "4" to "Ученникам класса")
}

