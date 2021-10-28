package com.it.restroexample.local

import androidx.room.*

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)  // or OnConflictStrategy.IGNORE
    fun insertUser(articleResponse: ArticleResponse)

    @Query("Select * from user")
    fun gelAllUsers(): List<ArticleResponse>

    @Update
    fun updateUser(articleResponse: ArticleResponse)

    @Delete
    fun deleteUser(articleResponse: ArticleResponse)

}