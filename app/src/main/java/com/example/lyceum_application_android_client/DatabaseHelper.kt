package com.example.lyceum_application_android_client

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.ContactsContract


class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, dbName, factory, version) {

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE = "CREATE TABLE $tableName " +
                "($ID Integer PRIMARY KEY, $NAME TEXT, $EMAIL TEXT, $PASSWORD TEXT, $CLASS TEXT, " +
                "$ROLE TEXT, $LAST_NAME TEXT, $FIRST_NAME TEXT, $MIDDLE_NAME TEXT)"
        db?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Called when the database needs to be upgraded
    }

    fun insertUserData(name: String, email: String, password: String, class_id: String, role_id: String, first_name: String, last_name: String, middle_name: String) {
        val db = writableDatabase
        val values: ContentValues = ContentValues()
        values.put(NAME, name )
        values.put(PASSWORD, password)
        values.put(EMAIL, email )
        values.put(CLASS, class_id )
        values.put(ROLE, role_id )
        values.put(FIRST_NAME, first_name )
        values.put(LAST_NAME, last_name )
        values.put(MIDDLE_NAME, middle_name )

        db.insert("user", null, values)
        db.close()
    }

    fun userPresent(name: String, password:String): Boolean {
        val db = writableDatabase
        val  query = "select * from user where $NAME = '$name' and $PASSWORD = '$password';"
        val cursor = db.rawQuery(query, null)
        if (cursor.count <= 0 ) {
            cursor.close()
            return false
        }
        cursor.close()
        return true
    }
    companion object {
        private const val dbName = "userDB.db"
        private const val tableName = "user"
        private val factory = null
        private const val version = 1
        private const val ID = "id"
        private const val NAME = "Name"
        private const val EMAIL = "Email"
        private const val PASSWORD = "Password"
        private const val ROLE = "Role"
        private const val CLASS = "Class"
        private const val LAST_NAME = "LastName"
        private const val FIRST_NAME = "FirstName"
        private const val MIDDLE_NAME = "MiddleName"
    }
}
