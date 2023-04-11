package com.android.example.toynewsapplication.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.android.example.toynewsapplication.data.local.model.GptEntity
import com.android.example.toynewsapplication.data.local.model.NewsEntity

@Dao
interface NewsDao {
    @Query("SELECT * FROM news")
    fun getAllNews(): LiveData<List<NewsEntity>>

    @Query("SELECT * FROM news WHERE id = :id")
    fun getNewsById(id: Int): NewsEntity?

    @Query("SELECT * FROM news WHERE title = :title")
    fun getNewsByTitle(title: String): NewsEntity

    @Insert(onConflict=OnConflictStrategy.REPLACE)
    suspend fun insertAll(news: List<NewsEntity>): List<Long>

    @Update(onConflict=OnConflictStrategy.REPLACE)
    suspend fun update(news: NewsEntity)
}
