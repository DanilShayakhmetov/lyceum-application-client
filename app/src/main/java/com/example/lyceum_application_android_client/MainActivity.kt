package com.example.lyceum_application_android_client

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
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
import kotlinx.android.synthetic.main.image.*
import kotlinx.android.synthetic.main.login.*
import kotlinx.android.synthetic.main.register.*


class MainActivity : AppCompatActivity() {

    lateinit var handler: DatabaseHelper
    lateinit var pref: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        // Session Manager
        val context = applicationContext
        val session = SessionManager(context);

        handler = DatabaseHelper(this)
        pref = session.pref
        editor = pref.edit();

//        val pref = session.pref
//        val editor = pref.edit();
//        editor.putString("username", ); // Storing string
//        editor.putInt("key_name", "int value"); // Storing integer
//        editor.putFloat("key_name", "float value"); // Storing float
//        val name = pref.getInt("asd", 1)

        showHome()

        login.setOnClickListener() {
            handler.insertSubjects()
            showLogin()
        }

        register.setOnClickListener() {
            showRegister()
        }

        img.setOnClickListener(){
            showImage()
        }

        capture_btn.setOnClickListener {
            //if system os is Marshmallow or Above, we need to request runtime permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (checkSelfPermission(Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_DENIED ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED){
                    //permission was not enabled
                    val permission = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    //show popup to request permission
                    requestPermissions(permission, PERMISSION_CODE)
                }
                else{
                    //permission already granted
                    openCamera()
                }
            }
            else{
                //system os is < marshmallow
                openCamera()
            }
            save_cont_btn.visibility = View.VISIBLE
            val values: ContentValues = ContentValues()


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
                session.createLoginSession(login_name.text.toString(), "")
//                editor.putString("name", name)
                val t = session.userDetails.get("name")
//                    pref.getString("name", "").toString()

                Toast.makeText(this, "login ${t} success!",  Toast.LENGTH_SHORT).show()
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

    private fun showImage() {
        image_layout.visibility = View.VISIBLE
        home_ll.visibility= View.GONE
        navigation.visibility = View.GONE
    }

    private fun getUser() : String {
        return pref.getString("name", "").toString()
    }

    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
        image_uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        //camera intent
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
        handler.createImage("qwe", image_uri.toString())
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE)
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        //called when user presses ALLOW or DENY from Permission Request Popup
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    //permission from popup was granted
                    openCamera()
                }
                else{
                    //permission from popup was denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //called when image was captured from camera intent
        if (resultCode == Activity.RESULT_OK){
            //set image captured to image view
            image_view.setImageURI(image_uri)
        }
    }

    companion object {
        const val REQUEST_CODE = 100
        var IMAGE_DATA: Intent? = null
        private val PERMISSION_CODE = 1000;
        private val IMAGE_CAPTURE_CODE = 1001
        var image_uri: Uri? = null
    }
}
