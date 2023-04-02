package com.android.example.toynewsapplication.data.local.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "gpt",
    foreignKeys = [
        ForeignKey(
            entity = NewsEntity::class,
            parentColumns = ["id"],
            childColumns = ["newsId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class GptEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val newsId: Int,
    val context: String,
)