package com.android.example.toynewsapplication.data.local.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "gpt",
    foreignKeys = [
        ForeignKey(
            entity = NewsEntity::class,
            parentColumns = ["id"],
            childColumns = ["news_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class GptEntity(
    @PrimaryKey(autoGenerate = false)
    val news_id: Int,
    val context: String,
)

fun LiveData<GptEntity>.extractContext(): LiveData<String> {
    return Transformations.map(this) {
        it.context
    }
}