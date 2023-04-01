package com.android.example.toynewsapplication.data.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Context(
    val title: String,
    val context: String,
    val created_at: String
): Parcelable