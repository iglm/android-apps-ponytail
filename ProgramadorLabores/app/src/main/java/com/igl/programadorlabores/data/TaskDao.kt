package com.igl.programadorlabores

import androidx.room.*

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

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks ORDER BY date DESC")
    fun getAll(): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE status = :status ORDER BY date DESC")
    fun getByStatus(status: String): Flow<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: Task)

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)

    @Query("SELECT COUNT(*) FROM tasks WHERE status = :status")
    fun countByStatus(status: String): Flow<Int>
}
