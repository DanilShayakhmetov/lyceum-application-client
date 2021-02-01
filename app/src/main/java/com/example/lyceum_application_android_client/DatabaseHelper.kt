package com.example.lyceum_application_android_client

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.lyceum_application_android_client.models.News
import com.example.lyceum_application_android_client.models.Schedules
import com.example.lyceum_application_android_client.models.Users


class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, dbName, factory, version) {

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_USER_TABLE = "CREATE TABLE $tableNameUser " +
                "($ID Integer PRIMARY KEY, $NAME TEXT, $EMAIL TEXT, $PASSWORD TEXT, $CLASS_ID INTEGER, " +
                "$ROLE TEXT, $LAST_NAME TEXT, $FIRST_NAME TEXT, $MIDDLE_NAME TEXT)"

        val CREATE_SCHEDULE_TABLE = "CREATE TABLE $tableNameSchedule " +
                "($ID Integer PRIMARY KEY, $ROOM INTEGER, $SUBJECT_ID INTEGER, $CLASS_ID INTEGER, $TEACHER_ID INTEGER, " +
                "$DAY_WEEK TEXT, $START_T DATETIME, $END_T DATETIME)"

        val CREATE_CLASS_TABLE = "CREATE TABLE $tableNameClass " +
                "($ID Integer PRIMARY KEY, $NUMBER INTEGER, $LETTER TEXT)"

        val CREATE_NEWS_TABLE = "CREATE TABLE $tableNameNews " +
                "($ID Integer PRIMARY KEY, $NAME TEXT, $TITLE TEXT, $MESSAGE TEXT, $CREATION_T DATETIME, " +
                "$IS_APPROVED INTEGER, $IS_HIDE INTEGER)"

        val CREATE_SUBJECT_TABLE = "CREATE TABLE $tableNameSubject " +
                "($ID Integer PRIMARY KEY, $NAME TEXT)"

        db!!.execSQL(CREATE_USER_TABLE)
        db.execSQL(CREATE_SCHEDULE_TABLE)
        db.execSQL(CREATE_CLASS_TABLE)
        db.execSQL(CREATE_NEWS_TABLE)
        db.execSQL(CREATE_SUBJECT_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Called when the database needs to be upgraded
    }

    fun insertSubjects() {
        val db = writableDatabase
        val values: ContentValues = ContentValues()
        val subjects = arrayOf("алгебра", "геометрия", "русс.яз", "ин.яз", "биология", "химия", "физика",
            "литература", "физ.культура", "труд", "история")

        for (subject in subjects) {
            values.put(NAME, subject)
            db.insert(tableNameSubject, null, values)
        }

        db.close()
    }

    fun insertUserData(name: String, email: String, password: String, class_id: String, role_id: String, first_name: String, last_name: String, middle_name: String) {
        val db = writableDatabase
        val values: ContentValues = ContentValues()
        values.put(NAME, name )
        values.put(PASSWORD, password)
        values.put(EMAIL, email )
        values.put(CLASS_ID, class_id )
        values.put(ROLE, role_id )
        values.put(FIRST_NAME, first_name )
        values.put(LAST_NAME, last_name )
        values.put(MIDDLE_NAME, middle_name )
        db.insert(tableNameUser, null, values)
        db.close()
    }

    fun userPresent(name: String, password:String): Boolean {
        val db = writableDatabase
        val  query = "select * from $tableNameUser where $NAME = '$name' and $PASSWORD = '$password';"
        val cursor = db.rawQuery(query, null)
        if (cursor.count <= 0 ) {
            cursor.close()
            return false
        }
        cursor.close()
        return true
    }

    fun getUserByName(name: String) : Users {
        val db = writableDatabase
        val query = "select * from $tableNameUser where $NAME = 'qwe';"
        var user = Users()
        val cursor = db.rawQuery(query, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                user.id = cursor.getInt(cursor.getColumnIndex(ID))
                user.userName = cursor.getString(cursor.getColumnIndex(NAME))
                user.firstName = cursor.getString(cursor.getColumnIndex(FIRST_NAME))
                user.lastName = cursor.getString(cursor.getColumnIndex(LAST_NAME))
                user.middleName = cursor.getString(cursor.getColumnIndex(MIDDLE_NAME))
                user.email = cursor.getString(cursor.getColumnIndex(EMAIL))
                user.roleId = cursor.getString(cursor.getColumnIndex(ROLE))
                user.classId = cursor.getString(cursor.getColumnIndex(CLASS_ID))
            }
        }
        cursor.close()
        db.close()

        return user
    }

    fun getUserTeachers(class_id: String) : Map <Int, Users> {
        val db = writableDatabase
        val query = "select * from $tableNameUser where $ROLE = '1' and $CLASS_ID = '$class_id';"
        val cursor = db.rawQuery(query, null)
        var users = mutableMapOf<Int, Users>()
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    var user = Users()
                    val id = cursor.getInt(cursor.getColumnIndex(ID))
                    user.id = id
                    user.firstName = cursor.getString(cursor.getColumnIndex(FIRST_NAME))
                    user.lastName = cursor.getString(cursor.getColumnIndex(LAST_NAME))
                    user.middleName = cursor.getString(cursor.getColumnIndex(MIDDLE_NAME))
                    user.email = cursor.getString(cursor.getColumnIndex(EMAIL))
                    user.classId = cursor.getString(cursor.getColumnIndex(CLASS_ID))
                    users[id] = user
                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        db.close()

        return users
    }

    fun getUserClassmates(class_id: String) : Map <Int, Users> {
        val db = writableDatabase
        val query = "select * from $tableNameUser where $ROLE = '0' and $CLASS_ID = '$class_id';"
        val cursor = db.rawQuery(query, null)
        var users = mutableMapOf<Int, Users>()
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    var user = Users()
                    val id = cursor.getInt(cursor.getColumnIndex(ID))
                    user.id = id
                    user.firstName = cursor.getString(cursor.getColumnIndex(FIRST_NAME))
                    user.lastName = cursor.getString(cursor.getColumnIndex(LAST_NAME))
                    user.middleName = cursor.getString(cursor.getColumnIndex(MIDDLE_NAME))
                    user.email = cursor.getString(cursor.getColumnIndex(EMAIL))
                    user.classId = cursor.getString(cursor.getColumnIndex(CLASS_ID))
                    users[id] = user
                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        db.close()

        return users
    }

    fun getClassSchedule(class_id: String, day: String) : Map<String, Schedules> {
        val db = writableDatabase
        val query = "select * from $tableNameSchedule where $CLASS_ID = '$class_id' and $DAY_WEEK = '$day';"
        val cursor = db.rawQuery(query, null)
        var resultSchedule = mutableMapOf<String, Schedules>()
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    var schedule = Schedules()
                    var start = cursor.getString(cursor.getColumnIndex(START_T))
                    schedule.id = cursor.getInt(cursor.getColumnIndex(ID))
                    schedule.classId = cursor.getString(cursor.getColumnIndex(CLASS_ID))
                    schedule.subjectId = cursor.getString(cursor.getColumnIndex(SUBJECT_ID))
                    schedule.teacherId = cursor.getString(cursor.getColumnIndex(TEACHER_ID))
                    schedule.room = cursor.getString(cursor.getColumnIndex(ROOM))
                    schedule.dayWeek = cursor.getString(cursor.getColumnIndex(DAY_WEEK))
                    schedule.startT = start
                    schedule.endT = cursor.getString(cursor.getColumnIndex(END_T))
                    resultSchedule[start] = schedule
                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        db.close()

        return resultSchedule
    }


    fun getNews() : Map<String, News>{
        val db = writableDatabase
        val query = "select * from $tableNameNews where $IS_HIDE = '0' ;"
        val cursor = db.rawQuery(query, null)
        var newsAll = mutableMapOf<String, News>()
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    var news = News()
                    var time = cursor.getString(cursor.getColumnIndex(CREATION_T))
                    news.id = cursor.getInt(cursor.getColumnIndex(ID))
                    news.title = cursor.getString(cursor.getColumnIndex(TITLE))
                    news.message = cursor.getString(cursor.getColumnIndex(MESSAGE))
                    news.creationTime = time
                    news.isApproved = cursor.getString(cursor.getColumnIndex(IS_APPROVED))
                    news.isHide = cursor.getString(cursor.getColumnIndex(IS_HIDE))
                    newsAll[time] = news
                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        db.close()

        return newsAll
    }

    companion object {
        private const val dbName = "userDB.db"
        private const val tableNameUser = "user"
        private const val tableNameClass = "class"
        private const val tableNameSchedule = "schedule"
        private const val tableNameNews = "news"
        private const val tableNameSubject = "subject"
        private val factory = null
        private const val version = 1
        private const val ID = "id"
        //class_schedule
        private const val TEACHER_ID = "TeacherId"
        private const val CLASS_ID = "ClassId"
        private const val ROOM = "Room"
        private const val SUBJECT_ID = "SubjectId"
        private const val DAY_WEEK = "DayWeek"
        private const val START_T = "start"
        private const val END_T = "end"
        //users
        private const val NAME = "Name"
        private const val EMAIL = "Email"
        private const val PASSWORD = "Password"
        private const val ROLE = "Role"
        private const val LAST_NAME = "LastName"
        private const val FIRST_NAME = "FirstName"
        private const val MIDDLE_NAME = "MiddleName"
        //news
        private const val TITLE = "Title"
        private const val MESSAGE = "Message"
        private const val CREATION_T = "CreationT"
        private const val IS_APPROVED = "Approved"
        private const val IS_HIDE = "Hide"
        //class
        private const val NUMBER = "Number"
        private const val LETTER = "Letter"
    }
}
