package com.example.lyceum_application_android_client

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.lyceum_application_android_client.models.*
import kotlin.random.Random


class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, dbName, factory, version) {

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_USER_TABLE = "CREATE TABLE $tableNameUser " +
                "($ID Integer PRIMARY KEY AUTOINCREMENT, $NAME TEXT, $EMAIL TEXT, $PASSWORD TEXT, $CLASS_ID INTEGER, " +
                "$ROLE TEXT, $LAST_NAME TEXT, $FIRST_NAME TEXT, $MIDDLE_NAME TEXT)"

        val CREATE_SCHEDULE_TABLE = "CREATE TABLE $tableNameSchedule " +
                "($ID Integer PRIMARY KEY AUTOINCREMENT, $SUBJECT_ID INTEGER, $CLASS_ID INTEGER, " +
                "$DAY_ID INTEGER, $INTERVAL_ID INTEGER)"

        val CREATE_CLASS_TABLE = "CREATE TABLE $tableNameClass " +
                "($ID Integer PRIMARY KEY AUTOINCREMENT, $NUMBER INTEGER, $LETTER TEXT)"

        val CREATE_NEWS_TABLE = "CREATE TABLE $tableNameNews " +
                "($ID Integer PRIMARY KEY AUTOINCREMENT, $NAME TEXT, $TITLE TEXT, $MESSAGE TEXT, $CREATION_T TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "$IS_APPROVED INTEGER, $IS_HIDE INTEGER)"

        val CREATE_SUBJECT_TABLE = "CREATE TABLE $tableNameSubject " +
                "($ID Integer PRIMARY KEY AUTOINCREMENT, $NAME TEXT, $TEACHER_ID INTEGER)"

        val CREATE_IMAGE_TABLE = "CREATE TABLE $tableNameImages " +
                "($ID Integer PRIMARY KEY AUTOINCREMENT, $IMG TEXT,  $USER_ID INTEGER)"

        val CREATE_INTERVALS_TABLE = "CREATE TABLE $tableNameInterval " +
                "($ID Integer PRIMARY KEY AUTOINCREMENT, $FROM_TO TEXT, $ROOM INTEGER, $SERIAL_NUM INTEGER)"

        val CREATE_DAYS_TABLE = "CREATE TABLE $tableNameDays " +
                "($ID Integer PRIMARY KEY, $NAME TEXT)"

        val CREATE_NEWS_CLASS_TABLE = "CREATE TABLE $tableNameNewsClass " +
                "($ID Integer PRIMARY KEY AUTOINCREMENT, $CLASS_ID INTEGER, $NEWS_ID INTEGER)"

        db!!.execSQL(CREATE_USER_TABLE)
        db.execSQL(CREATE_SCHEDULE_TABLE)
        db.execSQL(CREATE_CLASS_TABLE)
        db.execSQL(CREATE_NEWS_TABLE)
        db.execSQL(CREATE_NEWS_CLASS_TABLE)
        db.execSQL(CREATE_SUBJECT_TABLE)
        db.execSQL(CREATE_INTERVALS_TABLE)
        db.execSQL(CREATE_DAYS_TABLE)
        db.execSQL(CREATE_IMAGE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Called when the database needs to be upgraded
    }

    fun insertClasses() {
        val db = writableDatabase
        val values: ContentValues = ContentValues()
        val letters: MutableList<String> = mutableListOf("A","Б","В","Г")
        for (letter in letters) {
            var number: Int = 1
            while (number < 12) {
                values.put(NUMBER, number)
                values.put(LETTER, letter)
                db.insert(tableNameClass, null, values)
                number++
            }
        }
        db.close()
    }

    fun insertUsers() {
        val db = writableDatabase
        val values: ContentValues = ContentValues()
        val query = "select * from $tableNameClass ;"
        val cursor = db.rawQuery(query, null)
        val name = "username"
        var counter = 0
        if (cursor != null) {
            for(i in 1..cursor.count) {
                //После тестов можно увеличить
                for (j in 1..5) {
                    val person = getPerson()
                    values.put(NAME, person.userName)
                    values.put(EMAIL, person.email)
                    values.put(PASSWORD, person.password)
                    values.put(CLASS_ID, i)
                    values.put(ROLE, "0")
                    values.put(FIRST_NAME, person.firstName)
                    values.put(LAST_NAME, person.lastName)
                    values.put(MIDDLE_NAME, person.middleName)
                    counter++
                    db.insert(tableNameUser, null, values)
                }
                val person = getPerson()
                values.put(NAME, name.plus(i))
                values.put(EMAIL, person.email)
                values.put(PASSWORD, person.password)
                values.put(CLASS_ID, i)
                values.put(ROLE, "1")
                values.put(FIRST_NAME, person.firstName)
                values.put(LAST_NAME, person.lastName)
                values.put(MIDDLE_NAME, person.middleName)
                counter++
                db.insert(tableNameUser, null, values)
            }
        }
        cursor.close()
        db.close()
    }

    fun insertSubjects() {
        val db = writableDatabase
        val values: ContentValues = ContentValues()
        val query = "select * from $tableNameUser where $ROLE = '1' ;"
        val cursor = db.rawQuery(query, null)
        val subjects = arrayOf("алгебра", "геометрия", "русс.яз", "ин.яз", "биология", "химия", "физика",
            "литература", "физ.культура", "труд", "история")
        var i = 0
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    if (i == 11) i = 0
                    val id = cursor.getInt(cursor.getColumnIndex(ID))
                    val subj = subjects[i]
                    values.put(NAME, subj)
                    values.put(TEACHER_ID, id)
                    db.insert(tableNameSubject, null, values)
                    i++
                } while (cursor.moveToNext())
            }
        }

        cursor.close()
        db.close()
    }

    fun insertIntervals() {
        val db = writableDatabase
        val values: ContentValues = ContentValues()
        val intervals = mutableMapOf<Int, String>(1 to "8:00 - 8:45", 2 to  "9:00 - 9:45", 3 to "10:00 - 10:45", 4 to "11:00 - 11:45", 5 to "12:00 - 12:45", 6 to "13:00 - 13:45", 7 to "14:00 - 14:45")

        for (i in 1..25) {
            for (key in intervals.keys) {
                values.put(ROOM, Random.nextInt(0,44))
                values.put(SERIAL_NUM, key)
                values.put(FROM_TO, intervals[key])
                db.insert(tableNameInterval, null, values)
            }
        }

        db.close()
    }

    fun insertDays() {
        val db = writableDatabase
        val values: ContentValues = ContentValues()
        val days = mutableMapOf<Int, String>(1 to "Понедельник", 2 to  "Вторинк", 3 to "Среда", 4 to "Четверг", 5 to "Пятница")
        for (key in days.keys) {
            values.put(ID, key)
            values.put(NAME, days[key])
            db.insert(tableNameDays, null, values)
        }

        db.close()
    }

    fun insertNews() {
        val db = writableDatabase
        val values: ContentValues = ContentValues()
        val query = "select * from $tableNameUser where $ROLE = '1' ;"
        val cursor = db.rawQuery(query, null)
        var i = 0
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    val key = i.toString()
                    val rand = Random.nextInt(0, TITLES.size)
                    values.put(NAME, "username".plus(key))
                    values.put(TITLE, TITLES.get(rand))
                    values.put(MESSAGE, NEWS.get(rand))
                    values.put(IS_APPROVED, "1")
                    values.put(IS_HIDE, "0")
                    db.insert(tableNameNews, null, values)
                    i++
                } while (cursor.moveToNext())
            }
        }

        cursor.close()
        db.close()
    }

    fun insertNewsClass() {
        val db = writableDatabase
        val values: ContentValues = ContentValues()
        val newsQuery = "select * from $tableNameNews;"
        val classQuery = "select * from $tableNameClass;"
        val newsCursor = db.rawQuery(newsQuery, null)
        val classCursor = db.rawQuery(classQuery, null)
        var i = 0
        if (newsCursor != null && classCursor != null) {
            if (classCursor.moveToFirst()) {
                do {
                    for (j in 0..10) {
                        val key = i.toString()
                        val rand = Random.nextInt(0, newsCursor.count)
                        values.put(NEWS_ID, rand)
                        values.put(CLASS_ID, classCursor.getInt(classCursor.getColumnIndex(ID)))
                        db.insert(tableNameNewsClass, null, values)
                        i++
                    }
                } while (classCursor.moveToNext())
            }
        }

        classCursor.close()
        newsCursor.close()
        db.close()
    }

    fun insertSchedule() {
        val db = writableDatabase
        val queryDays = "select * from $tableNameDays ;"
        val queryClasses = "select * from $tableNameClass ;"
        val queryIntervals = "select * from $tableNameInterval ;"
        val cursorDays = db.rawQuery(queryDays, null)
        val cursorClasses = db.rawQuery(queryClasses, null)
        val cursorIntervals = db.rawQuery(queryIntervals, null)

        if (cursorDays != null && cursorClasses != null && cursorIntervals != null) {
            if (cursorDays.moveToFirst()) {
                do {
                    val dayID = cursorDays.getInt(cursorDays.getColumnIndex(ID))
                    if (cursorClasses.moveToFirst()) {
                        do {
                            val classID = cursorClasses.getInt(cursorDays.getColumnIndex(ID))
                            if (cursorIntervals.moveToFirst()) {
                                do {
                                    val values: ContentValues = ContentValues()
                                    val intervalId = cursorIntervals.getInt(cursorIntervals.getColumnIndex(ID))
                                    values.put(SUBJECT_ID, Random.nextInt(0,44))
                                    values.put(CLASS_ID, classID)
                                    values.put(DAY_ID, dayID)
                                    values.put(INTERVAL_ID, intervalId)
                                    db.insert(tableNameSchedule, null, values)
                                } while (cursorIntervals.moveToNext())
                            }
                        } while (cursorClasses.moveToNext())
                    }
                } while (cursorDays.moveToNext())
            }
        }
        cursorDays.close()
        cursorClasses.close()
        cursorIntervals.close()
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

    fun insertNewsData(name: String, title: String, message: String, class_id: String) {
        val db = writableDatabase
        val newsContentValues = ContentValues()
        val classContentValues = ContentValues()
        newsContentValues.put(NAME, name )
        newsContentValues.put(TITLE, title)
        newsContentValues.put(MESSAGE, message )
        newsContentValues.put(IS_APPROVED, "1" )
        newsContentValues.put(IS_HIDE, "0" )
        db.insert(tableNameNews, null, newsContentValues)
        val query = "select last_insert_rowid();"
        val cur: Cursor = db.rawQuery(query, null)
        cur.moveToFirst()
        val newsId: Int = cur.getInt(0)
        classContentValues.put(NEWS_ID, newsId)
        classContentValues.put(CLASS_ID, class_id)
        db.insert(tableNameNewsClass, null, classContentValues)
        db.close()
    }

    fun createImage(id: String, image: String) {
        val db = writableDatabase
        val values: ContentValues = ContentValues()
        values.put(USER_ID, id )
        values.put(IMG, image)
        db.insert(tableNameImages, null, values)
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

    fun getImage(user_id: String) : Images {
        val db = writableDatabase
        val query = "select * from $tableNameImages where $USER_ID = '$user_id';"
        val image = Images()
        val cursor = db.rawQuery(query, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                image.id = cursor.getInt(cursor.getColumnIndex(ID))
                image.userId = cursor.getInt(cursor.getColumnIndex(USER_ID))
                image.image = cursor.getString(cursor.getColumnIndex(IMG))
            }
        }
        cursor.close()
        db.close()
        return image
    }

    fun getClassName(class_id: String) : String {
        val db = writableDatabase
        val query = "select * from $tableNameClass where $ID = '$class_id';"
        val cursor = db.rawQuery(query, null)
        var resultString = ""
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                 resultString = resultString.plus(cursor.getString(cursor.getColumnIndex(NUMBER)))
                     .plus(cursor.getString(cursor.getColumnIndex(LETTER)))
            }
        }
        cursor.close()
        db.close()
        return resultString
    }
    
    fun getTeachers() : MutableMap  <Int, Users> {
        val db = writableDatabase
        val query = "select * from $tableNameUser where $ROLE = '1';"
        val teachers = mutableMapOf<Int, Users>()
        val cursor = db.rawQuery(query, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    var user = Users()
                    val id = cursor.getInt(cursor.getColumnIndex(ID))
                    user.id = id
                    user.userName = cursor.getString(cursor.getColumnIndex(NAME))
                    user.firstName = cursor.getString(cursor.getColumnIndex(FIRST_NAME))
                    user.lastName = cursor.getString(cursor.getColumnIndex(LAST_NAME))
                    user.middleName = cursor.getString(cursor.getColumnIndex(MIDDLE_NAME))
                    user.email = cursor.getString(cursor.getColumnIndex(EMAIL))
                    user.classId = cursor.getString(cursor.getColumnIndex(CLASS_ID))
                    user.roleId = cursor.getString(cursor.getColumnIndex(ROLE))
                    teachers[id] = user
                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        db.close()
        return teachers
    }

    fun getClassMates(class_id: String) : MutableMap  <Int, Users> {
        val db = writableDatabase
        val query = "select * from $tableNameUser where $ROLE = '0' and $CLASS_ID = '$class_id';"
        val teachers = mutableMapOf<Int, Users>()
        val cursor = db.rawQuery(query, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    var user = Users()
                    val id = cursor.getInt(cursor.getColumnIndex(ID))
                    user.id = id
                    user.userName = cursor.getString(cursor.getColumnIndex(NAME))
                    user.firstName = cursor.getString(cursor.getColumnIndex(FIRST_NAME))
                    user.lastName = cursor.getString(cursor.getColumnIndex(LAST_NAME))
                    user.middleName = cursor.getString(cursor.getColumnIndex(MIDDLE_NAME))
                    user.email = cursor.getString(cursor.getColumnIndex(EMAIL))
                    user.classId = cursor.getString(cursor.getColumnIndex(CLASS_ID))
                    user.roleId = cursor.getString(cursor.getColumnIndex(ROLE))
                    teachers[id] = user
                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        db.close()
        return teachers
    }

    fun getClasses() : MutableMap  <Int, Classes> {
        val db = writableDatabase
        val query = "select * from $tableNameClass;"
        val classes = mutableMapOf<Int, Classes>()
        val cursor = db.rawQuery(query, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    val classItem = Classes()
                    val id = cursor.getInt(cursor.getColumnIndex(ID))
                    classItem.id = id
                    classItem.number = cursor.getString(cursor.getColumnIndex(NUMBER))
                    classItem.letter = cursor.getString(cursor.getColumnIndex(LETTER))
                    classes[id] = classItem
                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        db.close()
        return classes
    }

    fun getDays() : MutableMap  <Int, Days> {
        val db = writableDatabase
        val query = "select * from $tableNameDays;"
        val days = mutableMapOf<Int, Days>()
        val cursor = db.rawQuery(query, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    val day = Days()
                    val id = cursor.getInt(cursor.getColumnIndex(ID))
                    day.id = id
                    day.name = cursor.getString(cursor.getColumnIndex(NAME))
                    days[id] = day
                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        db.close()
        return days
    }

    fun getIntervals() : MutableMap  <Int, Intervals> {
        val db = writableDatabase
        val query = "select * from $tableNameInterval;"
        val intervals = mutableMapOf<Int, Intervals>()
        val cursor = db.rawQuery(query, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    val interval = Intervals()
                    val id = cursor.getInt(cursor.getColumnIndex(ID))
                    interval.id = id
                    interval.room = cursor.getString(cursor.getColumnIndex(ROOM))
                    interval.fromTo = cursor.getString(cursor.getColumnIndex(FROM_TO))
                    interval.serialNum = cursor.getString(cursor.getColumnIndex(SERIAL_NUM))
                    intervals[id] = interval
                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        db.close()
        return intervals
    }

    fun getUserByName(name: String) : Users {
        val db = writableDatabase
        val query = "select * from $tableNameUser where $NAME = '$name';"
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
        val users = mutableMapOf<Int, Users>()
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    val user = Users()
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

    fun getClassSchedule(class_id: String, day_id: String) : Map<String, Schedules> {
        val db = writableDatabase
        val query = "select * from $tableNameSchedule where $CLASS_ID = '$class_id' and $DAY_ID = '$day_id';"
        val cursor = db.rawQuery(query, null)
        val resultSchedule = mutableMapOf<String, Schedules>()
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    val schedule = Schedules()
                    val interval = cursor.getString(cursor.getColumnIndex(INTERVAL_ID))
                    schedule.id = cursor.getInt(cursor.getColumnIndex(ID))
                    schedule.classId = cursor.getString(cursor.getColumnIndex(CLASS_ID))
                    schedule.subjectId = cursor.getString(cursor.getColumnIndex(SUBJECT_ID))
                    schedule.dayId = cursor.getString(cursor.getColumnIndex(DAY_ID))
                    schedule.intervalId = interval
                    resultSchedule[interval] = schedule
                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        db.close()

        return resultSchedule
    }

    fun getInterval(interval_id: String): Intervals {
        val db = writableDatabase
        val query = "select * from $tableNameInterval where $ID = '$interval_id';"
        val cursor = db.rawQuery(query, null)
        var interval = Intervals()
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                interval.id = cursor.getInt(cursor.getColumnIndex(ID))
                interval.room = cursor.getString(cursor.getColumnIndex(ROOM))
                interval.fromTo = cursor.getString(cursor.getColumnIndex(FROM_TO))
                interval.serialNum = cursor.getString(cursor.getColumnIndex(SERIAL_NUM))
            }
        }
        cursor.close()
        db.close()

        return interval
    }

    fun getDay(day_id: String): String {
        val db = writableDatabase
        val query = "select * from $tableNameDays where $ID = '$day_id';"
        val cursor = db.rawQuery(query, null)
        var day = "err"
        if (cursor.count <= 0 ) {
            day = cursor.getString(cursor.getColumnIndex(NAME))
            cursor.close()
            return day
        }
        cursor.close()
        return day
    }

    fun getSubject(day_id: String, interval_id: String): String {
        val db = writableDatabase
        val query = "select name from $tableNameSubject as sub inner join $tableNameSchedule as sch on sub.id = sch.$SUBJECT_ID where $DAY_ID = '$day_id' and $INTERVAL_ID= '$interval_id';"
        val cursor = db.rawQuery(query, null)
        var subjectName = ""
        if (cursor.count != 0 ) {
            if (cursor.moveToFirst()) {
                subjectName = cursor.getString(cursor.getColumnIndex(NAME))
                cursor.close()
                return subjectName
            }
        }
        cursor.close()
        return "_____"
    }

    fun getScheduleResultMap(class_id: String, day_id: String): MutableMap<String, ScheduleData> {
        val schedules = getClassSchedule(class_id, day_id)
        val resultSchedule = mutableMapOf<String, ScheduleData>()
        if (schedules.isNotEmpty()) {
            for (schedule in schedules) {
                val scheduleData = ScheduleData()
                val interval = getInterval(schedule.key)
                val day = schedule.value.dayId
                val subject = getSubject(day, interval.id.toString())
                val serial = interval.serialNum
                scheduleData.room = interval.room
                scheduleData.interval = interval.fromTo
                scheduleData.subject = subject
                scheduleData.day = day
                resultSchedule[serial] = scheduleData
            }
        }

        return resultSchedule
    }

    fun getSchedule(class_id: String): ArrayList<String> {
        val resultArr = arrayListOf<String>("","","","","")
        for(i in 1..5) {
            var resultString = ""
            val schedule = getScheduleResultMap(class_id, i.toString())
            val keys = schedule.toSortedMap().keys
            for (key in keys) {
                val item = schedule[key]
                if (item != null) {
                    resultString = resultString.plus(String.format("%s%n%s%n%s%n", item.subject, item.interval, "КАБ.".plus(item.room)))
                }
            }
            resultArr[i-1] = resultString
        }

        return resultArr
    }

    fun getNews(class_id: String) : Map<Int, News>{
        val db = writableDatabase
        val query = "select * from $tableNameNews as n inner join $tableNameNewsClass as cn on n.id = cn.NewsId where cn.ClassId = '$class_id' order by id desc;"
        val cursor = db.rawQuery(query, null)
        val newsAll = mutableMapOf<Int, News>()
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    val news = News()
                    val id = cursor.getInt(cursor.getColumnIndex(ID))
                    news.id = id
                    news.title = cursor.getString(cursor.getColumnIndex(TITLE))
                    news.message = cursor.getString(cursor.getColumnIndex(MESSAGE))
                    news.isApproved = cursor.getString(cursor.getColumnIndex(IS_APPROVED))
                    news.isHide = cursor.getString(cursor.getColumnIndex(IS_HIDE))
                    newsAll[id] = news
                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        db.close()

        return newsAll
    }

    fun getPerson(): Users {
        val gender = Random.nextBoolean()
        val person = Users()
        if (gender) {
            var size  = female_firstname.size - 1
            val randomStr = getRandomString(5)
            person.userName = randomStr
            person.firstName = female_firstname.get(Random.nextInt(0,size))
            size  = female_middlename.size - 1
            person.middleName =  female_middlename.get(Random.nextInt(0, size))
            size  = LASTNAMES.size - 1
            person.lastName = LASTNAMES.get(Random.nextInt(0, size)).plus("а")
            person.email = randomStr.plus("@email.com")
            person.password = "qwe"
        } else {
            var size  = male_firstname.size - 1
            val randomStr = getRandomString(10)
            person.userName = randomStr
            person.firstName = male_firstname.get(Random.nextInt(0,size))
            size  = male_middlename.size - 1
            person.middleName =  male_middlename.get(Random.nextInt(0, size))
            size  = LASTNAMES.size - 1
            person.lastName = LASTNAMES.get(Random.nextInt(0, size))
            person.email = randomStr.plus("@email.com")
            person.password = "qwe"
        }
        return person
    }

    fun getRandomString(length: Int) : String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }

    fun testSchedule(): String {
        val db = writableDatabase
        val query = "select * from $tableNameSubject;"
        val cursor = db.rawQuery(query, null)
        var counter = ""
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    counter = counter.plus(cursor.getString(cursor.getColumnIndex(FROM_TO)))
                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        db.close()

        return counter
    }

    fun testSchedule2(class_id: String, day_id: String): String {
        val db = writableDatabase
        val classs_id = 2
        val days_id = 2
        val query = "select * from $tableNameSchedule where $CLASS_ID = '$classs_id' AND $DAY_ID = '$days_id';"
        val cursor = db.rawQuery(query, null)
        var counter = 0
        var resultString = ""
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    var resp =  cursor.getInt(cursor.getColumnIndex(ID))
                    resultString = resultString.plus(resp)
                    resp = cursor.getInt(cursor.getColumnIndex(CLASS_ID))
                    resultString = resultString.plus(resp)
                    resp = cursor.getInt(cursor.getColumnIndex(DAY_ID))
                    resultString = resultString.plus(resp)
                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        db.close()

        return resultString
    }


    companion object {
        private const val dbName = "userDB.db"
        private const val tableNameUser = "user"
        private const val tableNameClass = "class"
        private const val tableNameSchedule = "schedule"
        private const val tableNameNews = "news"
        private const val tableNameNewsClass = "news_class"
        private const val tableNameSubject = "subject"
        private const val tableNameInterval = "interval"
        private const val tableNameDays = "days"
        private const val tableNameImages = "image"
        private val factory = null
        private const val version = 1
        private const val ID = "id"
        private val female_firstname = arrayListOf<String>("Анна", "Ольга", "Елена", "Ксения", "Татьяна", "Надежда", "Вера", "Любовь", "Мария", "Светлана", "Наталья", "Валерия", "София", "Ульяна")
        private val female_middlename = arrayListOf<String>("Николаевна", "Ивановна", "Петровна", "Сидоровна", "Викторовна", "Анатольевна", "Генадьевна", "Игоревна", "Владимировна", "Аркадьевна", "Михайловна", "Васильевна", "Олеговна", "Семеновна")
        private val LASTNAMES = arrayListOf<String>("Смирнов",
                "Иванов",
                "Кузнецов",
                "Соколов",
                "Попов",
                "Лебедев",
                "Козлов",
                "Новиков",
                "Морозов",
                "Петров",
                "Волков",
                "Соловьёв",
                "Васильев",
                "Зайцев",
                "Павлов",
                "Семёнов",
                "Голубев",
                "Виноградов",
                "Богданов",
                "Воробьёв",
                "Фёдоров",
                "Михайлов",
                "Беляев",
                "Тарасов",
                "Белов",
                "Комаров",
                "Орлов",
                "Киселёв",
                "Макаров",
                "Андреев",
                "Ковалёв",
                "Ильин",
                "Гусев",
                "Титов",
                "Кузьмин",
                "Кудрявцев",
                "Баранов",
                "Куликов",
                "Алексеев",
                "Степанов",
                "Яковлев")
        private val male_firstname = arrayListOf<String>("Николай", "Иван", "Петр", "Сидор", "Виктор", "Анатолий", "Генадий", "Игорь", "Владимир", "Аркадий", "Михаил", "Василий", "Олег", "Семен", "Артемий", "Степан", "Андрей", "Эдуард", "Сергей", "Алексей")
        private val male_middlename = arrayListOf<String>("Николаевич", "Иванович", "Петрович", "Сидорович", "Викторович", "Анатольевич", "Генадьевич", "Игорьевич", "Владимирович", "Аркадьевич", "Михайлович", "Васильевич", "Олегович", "Семенович", "Артемьевич", "Степанович", "Андреевич", "Эдуардович", "Сергеевич", "Алексеевич")
        private val TITLES = arrayListOf<String>("Мой маленький сад", "Зимняя зелень",
            "Цитрусовые",
            "Легенда о клюкве",
            "Английский завтрак",
            "Полезные сладости",
            "Продукты",
            "Здоровое питание",
            "Мой завтрак")
        private val NEWS = arrayListOf<String>("Совсем недавно я решила завести свой собственный сад. Теперь там растут яблони, вишни, кустики крыжовника, смородины и, конечно же, малины! В скором времени мой маленький сад запестрит яркими красками, а стол не обойдётся без блюд из этих замечательных фруктов и ягод.\n" +
                "\n", "Очень важно зимой есть овощи и фрукты, свежую зелень. Они полезные. Зимой люди очень часто болеют, потому что им не хватает витаминов. Зимой мало светит солнце, а сырая погода разносит болезни.\n" +
                "\n" +
                "Хорошо будет покрошить в суп зубок чеснока, потому что чеснок укрепляет иммунитет. В творог можно добавить мелко порубленную петрушку, так ужин станет вкуснее и полезнее.\n" +
                "\n" +
                "К обеду можно нарезать свежий огурец вместо солёного из банки. Салат из тёртого яблока и тёртой моркови – отличный завтрак. Поддерживать своё здоровье просто, если делать это постоянно. Не болейте зимой!",
            "Цитрусовые фрукты – это очень полезные фрукты, потому что в них много витамина C. Витамин C укрепляет здоровье. Какие мы знаем цитрусовые фрукты?\n" +
                    "\n" +
                    "Во-первых, апельсины. Апельсиновые деревья растут в Испании везде. Точно так же, как в наших городах растут яблони. Апельсиновые рощи окружают испанские дворцы. Деревья шелестят листьями, плоды распространяют свежий запах. Можно протянуть руку и сорвать с дерева оранжевый апельсин с шершавой кожурой. Маленькое солнце!\n" +
                    "\n" +
                    "Без мандаринов нельзя представить Новый год. Сезон мандаринов начинается зимой. Они появляются на прилавках магазинов в конце ноября, и люди начинают улыбаться. Скоро праздник!\n" +
                    "\n" +
                    "Лимон никто не станет есть просто так. Его кладут в чай. Нужно отрезать ломтик лимона и растолочь его с сахаром в чашке, потом нужно заварить чёрный чай. Чай должен быть крепкий и сладкий.\n" +
                    "\n",
            "Все знают, что клюква растёт на болоте. Чтобы найти её, нужно постараться. Но почему клюква растёт на болоте? Есть легенда.\n" +
                    "\n" +
                    "Давным-давно жили в деревне люди. Деревня их стояла совсем рядом с лесом, а в лесу было болото. Люди пахали свои поля, растили на огородах овощи и фрукты. Однажды в их страну пришли захватчики, началась война. Люди решили спрятаться в лесу, на болоте. Они боялись, что враги придут в их деревню и всех убьют. Так и сделали.\n" +
                    "\n" +
                    "Вражеские воины на самом деле нашли деревню, но не нашли в ней людей. Тогда они стали искать в лесу и пришли к болоту. А на болоте жили журавли. Журавли дружили с людьми из деревни, вили гнёзда на крышах их домов. Но что же делать? Воины уже достали свои мечи и готовились убить несчастных крестьян.\n" +
                    "\n" +
                    "Тогда журавлиная стая взлетела в небо. Они закрыли людей своими большими крыльями. Стрелы попадали в белых птиц, и на болотные кочки падали красные капли крови. Эти капли превратились в ягоды. В клюкву.",
            "Англичане завтракают со знанием дела. В Англии принято вставать очень рано, в шесть утра. Ещё в постели англичане выпивают чашку чая, а только потом начинают одеваться.\n" +
                    "\n" +
                    "Англичане завтракают в восемь утра. Им нужно плотно поесть, потому что обед будет нескоро. На столе стоит много разной еды. Обязательно англичане съедают на завтрак вареное яйцо. Яйцо может быть сварено вкрутую или всмятку. Его подают на специальной подставке.\n" +
                    "\n" +
                    "Ещё на завтрак едят яичницу, к которой подают жареные сардельки или бекон. Англичане очень любят овсяную кашу, в неё добавляют масло и какое-нибудь варенье. Вместо каши англичане могут есть на завтрак пудинг.\n" +
                    "\n" +
                    "Ещё англичане едят тосты с джемом. Раньше в богатых домах всегда была специальная кухарка, которая умела правильно поджаривать хлеб для тостов.\n" +
                    "\n" +
                    "На завтрак англичане обязательно выпивают стакан апельсинового сока, чашку чая или кофе.",

            "Наверное, нет такого человека, который не любит сладкое. Все хотят съесть десерт после обеда или после ужина. Так приятно заварить ароматный чай или сварить горячее какао и побаловать себя. Но ещё все знают, что есть слишком много сладкого – это вредно.\n" +
                    "\n" +
                    "Но есть полезные сладости! Например, чёрный шоколад. Чёрный шоколад называют горьким шоколадом. Он полезен для сердца и для нервов. Кроме того, чёрный шоколад помогает крови легче бежать по венам.\n" +
                    "\n" +
                    "Мармелад делают из слив или из яблок. Мармелад тоже полезен. Он укрепляет желудок. Врачи советуют есть мармелад после болезней и после нагрузок.",
            "Еда — основа нашей жизни, потому что человеческий организм не может существовать без неё. Разнообразие продуктов, доступных в современных супермаркетах, впечатляет. В разных отделах магазина мы можем купить различные виды продуктов, например фрукты, овощи, молочные продукты, хлебобулочные изделия, бакалейные товары, напитки.",
            "Если мы хотим прожить долгую жизнь, нам следует следить за тем, что мы едим. Здоровое питание обычно включает в себя большое количество фруктов и овощей, регулярное употребление рыбы и белого мяса, злаков, орехов и несколько литров воды в день. Лучше выбирать продукты, не содержащие искусственные добавки, и с минимальным количеством сахара.",
            "Я завтракаю в 7 часов. Мой завтрак всегда плотный и обычно состоит из яичницы и бекона, бутербродов с маслом и сыром или колбасой. Потом я пью чай или кофе и иду в школу.\n" +
                    "\n")

        
        
        //class_schedule
        private const val TEACHER_ID = "TeacherId"
        private const val CLASS_ID = "ClassId"
        private const val ROOM = "Room"
        private const val SUBJECT_ID = "SubjectId"
        private const val DAY_ID = "DayId"
        private const val INTERVAL_ID = "IntervalId"
        private const val NEWS_ID = "NewsId"
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
        //intervals
        private const val FROM_TO = "FromTo"
        private const val SERIAL_NUM = "SerialNumber"
        //images
        private const val IMG = "Image"
        private const val USER_ID = "UserId"
    }
}
