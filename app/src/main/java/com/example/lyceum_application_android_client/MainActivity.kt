package com.example.lyceum_application_android_client

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.lyceum_application_android_client.models.Users
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.login.*
import kotlinx.android.synthetic.main.register.*


class MainActivity : AppCompatActivity() {

    lateinit var handler: DatabaseHelper

    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        // Session Manager
        val context = applicationContext
        val session = SessionManager(context);

        handler = DatabaseHelper(this)
        val pref = session.pref


//        val pref = session.pref
//        val editor = pref.edit();
//        editor.putString("username", ); // Storing string
//        editor.putInt("key_name", "int value"); // Storing integer
//        editor.putFloat("key_name", "float value"); // Storing float
//        val name = pref.getInt("asd", 1)

        showHome()

        login.setOnClickListener() {
            handler.insertSubjects()
            handler.insertIntervals()
            handler.insertDays()
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
            if (handler.userPresent(login_name.text.toString(), login_password.text.toString())) {
                val name = login_name.text.toString()
                val user = handler.getUserByName(name)
                session.createLoginSession("Username", name);
                val editor = pref.edit();
                editor.putString("username", name)
                Toast.makeText(this, "login ${user.userName} success!",  Toast.LENGTH_SHORT).show()
                showMain()
            } else {
                Toast.makeText(this, "username or password is incorrect", Toast.LENGTH_SHORT).show()
                showRegister()
            }
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

    private fun showMain() {
        login_layout.visibility= View.GONE
        register_layout.visibility= View.GONE
        home_ll.visibility= View.GONE
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
