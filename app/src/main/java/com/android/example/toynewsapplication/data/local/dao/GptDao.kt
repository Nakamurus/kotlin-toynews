package com.android.example.toynewsapplication.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.android.example.toynewsapplication.data.local.model.GptEntity

@Dao
interface GptDao {
    @Query("SELECT * FROM gpt WHERE news_id = :newsId")
    fun getGptByNewsId(newsId: Int): GptEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGpt(response: GptEntity)

    @Query("SELECT * FROM gpt")
    suspend fun getAllGpt(): List<GptEntity>
}
