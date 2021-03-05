package com.example.lyceum_application_android_client.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.lyceum_application_android_client.DatabaseHelper
import com.example.lyceum_application_android_client.MainActivity
import com.example.lyceum_application_android_client.R
import com.example.lyceum_application_android_client.SessionManager
import com.example.lyceum_application_android_client.models.Images
import com.example.lyceum_application_android_client.models.Users
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.image.*
import kotlinx.android.synthetic.main.image.view.*
import kotlinx.android.synthetic.main.image.view.capture_btn


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
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
        val id: String = session.userDetails.get("id").toString()
        val user: Users  = handler.getUserByName(name)
        val role: String = user.roleId
        val classMates = handler.getClassMates(user.classId)
        val teachers = handler.getTeachers()
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val classMatesLayout: TableLayout = root.findViewById(R.id.classmates_layout)
        val teachersLayout: TableLayout = root.findViewById(R.id.tutors_layout)
        val image =  Uri.parse(handler.getImage(id).image)

        if (role == "1") {
            root.show_tutors_button.text = "Коллеги"
            root.show_classmates_button.text = "Ученники"
        } else {
            root.show_tutors_button.text = "Учителя"
            root.show_classmates_button.text = "Одноклассники"
        }

        root.img_button.setOnClickListener() {
            root.image_layout.visibility = View.VISIBLE
            root.img_button.visibility = View.GONE
            root.show_tutors_button.visibility = View.GONE
            root.show_classmates_button.visibility = View.GONE
        }

        root.logout_button.setOnClickListener() {
            session.logoutUser()
        }

        root.capture_btn.setOnClickListener() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (checkSelfPermission(context, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_DENIED ||
                    checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
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
        }

        root.save_cont_btn.setOnClickListener() {
            val source = handler.createImage(id, image_uri.toString())
            Toast.makeText(context, "Изображение: ".plus(source), Toast.LENGTH_SHORT).show()
            root.image_layout.visibility = View.GONE
        }

        root.show_classmates_button.setOnClickListener() {
            root.home_fragment_layout.visibility = View.GONE
            root.classmates_layout.visibility = View.VISIBLE

        }

        root.show_tutors_button.setOnClickListener() {
            root.home_fragment_layout.visibility = View.GONE
            root.tutors_layout.visibility = View.VISIBLE
        }

//        if (classMates.isNotEmpty()) {
//            for (i in classMates) {
//                val username = TextView(context)
//                val email = TextView(context)
//                val item = i.value
//                username.text = "".plus(String.format("%s%n", item.lastName.plus(" ").plus(item.firstName).plus(" ").plus(item.middleName)))
//                username.textSize = 20.23F
//                email.text = "".plus(String.format("%s%n", item.email))
//                email.textSize = 15.23F
//                classMatesLayout.addView(username)
//                classMatesLayout.addView(email)
//            }
//
//        }
//
//        if (teachers.isNotEmpty()) {
//            for (i in teachers) {
//                val row = TableRow(context)
//                val username = TextView(context)
//                val email = TextView(context)
//                val item = i.value
//                username.text = "".plus(String.format("%s%n%s%n%s%n", item.lastName, item.firstName, item.middleName))
//                username.textSize = 10.23F
//                email.text = "".plus(String.format("%s%n", item.email))
//                email.textSize = 10.23F
//                row.addView(username)
//                row.addView(email)
//                teachersLayout.addView(row)
//            }
//
//        }

        initLayout(classMates, classMatesLayout)
        initLayout(teachers, teachersLayout)

        if (user.userName.isNotEmpty()) {
            homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel::class.java)
            val textViewName: TextView = root.findViewById(R.id.text_name)
            val textViewFull: TextView = root.findViewById(R.id.text_fullName)
            val textViewEmail: TextView = root.findViewById(R.id.text_email)
            val textViewClass: TextView = root.findViewById(R.id.text_Class_id)
            val profilePicViewClass: ImageView = root.findViewById(R.id.profile_pic)
            homeViewModel.name.observe(this, Observer {
                profilePicViewClass.setImageURI(image)
            })
            homeViewModel.name.observe(this, Observer {
                textViewName.text = "USERNAME:  ".plus(user.userName)
            })
            homeViewModel.first.observe(this, Observer {
                textViewFull.text = "Ф.И.О:  "
                    .plus(user.lastName)
                    .plus("  ")
                    .plus(user.firstName)
                    .plus("  ")
                    .plus(user.middleName)
            })
            homeViewModel.classId.observe(this, Observer {
                textViewClass.text = "КЛАСС:  ".plus(handler.getClassName(user.classId))
            })
            homeViewModel.email.observe(this, Observer {
                textViewEmail.text = "Email:  ".plus(user.email)
            })

            return root
        } else {
            homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel::class.java)
            val textViewName: TextView = root.findViewById(R.id.text_name)
            val textViewFull: TextView = root.findViewById(R.id.text_fullName)
            val textViewEmail: TextView = root.findViewById(R.id.text_email)
            val textViewClass: TextView = root.findViewById(R.id.text_Class_id)
            homeViewModel.text.observe(this, Observer {
                textViewName.text = user.userName
                textViewFull.text = user.userName
                textViewEmail.text = user.email
                textViewClass.text = user.classId
            })
            return root
        }

    }

    private fun openCamera() {
        val values = ContentValues()
        val resolver = requireActivity().contentResolver
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
        image_uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        //camera intent
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
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
//                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
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
    private fun initLayout(items: MutableMap<Int, Users>, layout: TableLayout) {
        if (items.isNotEmpty()) {
            for (i in items) {
                val row = TableRow(context)
                val username = TextView(context)
                val email = TextView(context)
                val item = i.value
                username.text = "".plus(String.format("%s%n%s%n%s%n", item.lastName, item.firstName, item.middleName))
                username.textSize = 15.23F
                email.text = "".plus(String.format("%s%n", item.email))
                email.textSize = 15.23F
                row.addView(username)
                row.addView(email)
                layout.addView(row)
            }

        }
    }

    companion object {
        private val PERMISSION_CODE = 1000;
        private val IMAGE_CAPTURE_CODE = 1001
        var image_uri: Uri? = null
    }

}