package com.igl.gastosmoto

import androidx.room.*

@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val amount: Double,
    val concept: String,
    val date: Long = System.currentTimeMillis(),
    val km: Int = 0
)

@Dao
interface ExpenseDao {
    @Query("SELECT * FROM expenses ORDER BY date DESC")
    fun getAll(): Flow<List<Expense>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(expense: Expense)

    @Delete
    suspend fun delete(expense: Expense)

    @Query("SELECT COALESCE(SUM(amount), 0) FROM expenses")
    fun total(): Flow<Double>
}
