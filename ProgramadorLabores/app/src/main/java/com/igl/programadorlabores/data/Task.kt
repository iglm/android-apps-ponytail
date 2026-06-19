package com.igl.programadorlabores.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val type: String,
    val status: String = "pendiente",
    val date: Long = System.currentTimeMillis(),
    val notes: String = "",
    val workers: Int = 1
)
