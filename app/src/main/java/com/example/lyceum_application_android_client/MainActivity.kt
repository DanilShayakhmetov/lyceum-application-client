package com.example.lyceum_application_android_client

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.add_news.*
import kotlinx.android.synthetic.main.add_news.view.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_notifications.*
import kotlinx.android.synthetic.main.login.*
import kotlinx.android.synthetic.main.register.*


class MainActivity : AppCompatActivity() {

    lateinit var handler: DatabaseHelper
    lateinit var pref: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    lateinit var session: SessionManager
    lateinit var context: Context

    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        // Session Manager
        context = applicationContext
        session = SessionManager(context);

        handler = DatabaseHelper(this)
        pref = session.pref
        editor = pref.edit();


        if (handler.getClasses().isEmpty()) {
            handler.insertVisibility()
            handler.insertClasses()
            handler.insertUsers()
            handler.insertIntervals()
            handler.insertDays()
            handler.insertSubjects()
            handler.insertSchedule()
            handler.insertNews()
            handler.insertNewsClass()
        }

        showHome()

        login.setOnClickListener() {
            showLogin()
        }

        register.setOnClickListener() {
            showRegister()
        }


        register_button.setOnClickListener() {
            handler.insertUserData(register_name.text.toString(), register_email.text.toString(), register_password.text.toString(),
                register_class.text.toString(),  register_role.text.toString(), register_first.text.toString(),
                register_middle.text.toString(), register_last.text.toString())
                showHome()
        }

        login_button.setOnClickListener() {
            val handler = Handler()
            progressBar.visibility = View.VISIBLE
            closeKeyBoard()
            handler.postDelayed({onLoginAction()}, 1000)
        }


        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun showLogin() {
        login_layout.visibility= View.VISIBLE
        home_ll.visibility= View.GONE
        navigation.visibility = View.GONE
    }

    private fun showRegister() {
        register_layout.visibility= View.VISIBLE
        home_ll.visibility= View.GONE
        navigation.visibility = View.GONE
    }

    private fun showHome() {
        login_layout.visibility= View.GONE
        register_layout.visibility= View.GONE
        home_ll.visibility= View.VISIBLE
        navigation.visibility = View.GONE
    }

    private fun onLoginAction() {
        if (handler.userPresent(login_name.text.toString(), login_password.text.toString())) {
            val navView: BottomNavigationView = findViewById(R.id.nav_view)
            val name = login_name.text.toString()
            val user = handler.getUserByName(name)
            val classId = user.classId
            val schedule = handler.getSchedule(classId)
//            if (!session.isLoggedIn) {
                session.createLoginSession(login_name.text.toString(), user.id.toString(), schedule[0], schedule[1], schedule[2], schedule[3], schedule[4], classId)
                Toast.makeText(this, "login ${user.userName} success!",  Toast.LENGTH_SHORT).show()
//            }
            showMain()
            navView.menu.performIdentifierAction(R.id.navigation_home, 0)
        } else {
            Toast.makeText(this, "username or password is incorrect", Toast.LENGTH_SHORT).show()
            showRegister()
        }
    }

    private fun closeKeyBoard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun showMain() {
        login_layout.visibility = View.GONE
        register_layout.visibility = View.GONE
        home_ll.visibility = View.GONE
        navigation.visibility = View.VISIBLE
    }


    private fun showProgress() {
        login_layout.visibility = View.GONE
        register_layout.visibility = View.GONE
        home_ll.visibility = View.GONE
        navigation.visibility = View.VISIBLE
    }

    private fun showSchedule() {
        register_layout.visibility= View.VISIBLE
        home_ll.visibility= View.GONE
        navigation.visibility = View.GONE
    }

    private fun showNews() {
        register_layout.visibility= View.VISIBLE
        home_ll.visibility= View.GONE
        navigation.visibility = View.GONE
    }

}
