package com.example.hopeless.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "task_table")
@Parcelize
data class Task(
    val details: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
) : Parcelable
