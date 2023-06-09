package com.android.example.toynewsapplication.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.android.example.toynewsapplication.data.local.dao.GptDao
import com.android.example.toynewsapplication.data.local.dao.NewsDao
import com.android.example.toynewsapplication.data.local.model.GptEntity
import com.android.example.toynewsapplication.data.local.model.NewsEntity

@Database(entities = [GptEntity::class, NewsEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    /**
     * Connects the database to NewsDao
     */
    abstract fun newsDao(): NewsDao

    /**
     * Connects the database to GptDao
     */
    abstract fun gptDao(): GptDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
