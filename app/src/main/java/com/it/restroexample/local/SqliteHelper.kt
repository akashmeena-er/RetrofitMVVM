package com.it.restroexample.local
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.lang.NullPointerException
import java.util.*
/**
 * Created by Akash on 24/12/2020.
 */
class SqliteHelper(context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "hacker.db"
        const val DATABASE_VERSION = 1
        const val TABLE_NEWS = "hackernews"
        const val KEY_ID = "id"
        const val KEY_TITLE = "title"
        const val KEY_URL = "url"
        const val KEY_SCORE = "score"
        const val SQL_TABLE_EVENT = ("CREATE TABLE " + TABLE_NEWS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_TITLE + " TEXT, "
                + KEY_URL + " TEXT,"
                + KEY_SCORE + " INTEGER "
                + " ) ")
    }

    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_TABLE_EVENT)
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, i: Int, i1: Int) {
        sqLiteDatabase.execSQL(" DROP TABLE IF EXISTS $TABLE_NEWS")
        onCreate(sqLiteDatabase)
    }

    fun addTask(articleResponse: ArticleResponse) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(KEY_TITLE, articleResponse.title)
        values.put(KEY_URL, articleResponse.url)
        values.put(KEY_SCORE, articleResponse.score)
        db.insert(TABLE_NEWS, null, values)
        db.close()
    }

    /**
     * getAllAllTopNews from local database
     */
    val getAllTopNews: ArrayList<ArticleResponse>
        get() {
            val newsList = ArrayList<ArticleResponse>()
            val db = this.readableDatabase
            val columns = arrayOf(KEY_ID, KEY_TITLE, KEY_URL, KEY_SCORE)
            val cursor = db.query(TABLE_NEWS, columns, null, null, null, null,  KEY_SCORE+" ASC")
            if (cursor.moveToFirst()) {
                do {
                    try {
//                        val articleResponse = ArticleResponse()
//                        articleResponse.id = cursor.getInt(0)
//                        articleResponse.title = cursor.getString(1)
//                        articleResponse.url = cursor.getString(2)
//                        articleResponse.score = cursor.getInt(3)
//                        newsList.add(articleResponse)
                    }
                    catch (e:NullPointerException)
                    {

                    }
                    } while (cursor.moveToNext())
            }
            cursor.close()
            db.close()
            return newsList
        }

    fun deletePreviousData()
    {
        val db = this.writableDatabase
        db.execSQL("delete from $TABLE_NEWS")
    }


}