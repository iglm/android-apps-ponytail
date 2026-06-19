package com.igl.gastosmoto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val amount: Double,
    val concept: String,
    val date: Long = System.currentTimeMillis(),
    val km: Int = 0
)
