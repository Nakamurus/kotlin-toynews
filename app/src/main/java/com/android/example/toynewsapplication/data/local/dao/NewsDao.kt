package com.android.example.toynewsapplication.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android.example.toynewsapplication.data.local.model.NewsEntity

@Dao
interface NewsDao {
    @Query("SELECT * FROM news")
    fun getAllNews(): LiveData<List<NewsEntity>>

    @Query("SELECT * FROM news WHERE id = :id")
    fun getNewsById(id: Int): LiveData<NewsEntity>

    @Query("SELECT * FROM news WHERE title = :title")
    fun getNewsByTitle(title: String): NewsEntity

    @Insert(onConflict=OnConflictStrategy.REPLACE)
    suspend fun insertAll(news: List<NewsEntity>)
}
