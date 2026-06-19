package com.igl.monitoreobrc.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trees")
data class TreeRecord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val plot: String,
    val treeNumber: Int,
    val disease: String,
    val severity: String = "leve",
    val date: Long = System.currentTimeMillis(),
    val notes: String = ""
)
