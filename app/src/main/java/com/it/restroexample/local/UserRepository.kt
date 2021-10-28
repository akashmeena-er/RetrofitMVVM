package com.it.restroexample.local

import android.content.Context
import android.os.AsyncTask

class UserRepository(context: Context) {

    var db: UserDao = AppDatabase.getInstance(context)?.userDao()!!


    //Fetch All the Users
    fun getAllUsers(): List<ArticleResponse> {
        return db.gelAllUsers()
    }

    // Insert new user
    fun insertUser(users: ArticleResponse) {
        insertAsyncTask(db).execute(users)
    }

    // update user
    fun updateUser(users: ArticleResponse) {
        db.updateUser(users)
    }

    // Delete user
    fun deleteUser(users: ArticleResponse) {
        db.deleteUser(users)
    }

    private class insertAsyncTask internal constructor(private val usersDao: UserDao) :
        AsyncTask<ArticleResponse, Void, Void>() {

        override fun doInBackground(vararg params: ArticleResponse): Void? {
            usersDao.insertUser(params[0])
            return null
        }
    }
}