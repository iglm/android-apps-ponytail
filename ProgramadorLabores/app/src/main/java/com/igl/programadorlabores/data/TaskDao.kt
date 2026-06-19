package com.igl.programadorlabores.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

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
