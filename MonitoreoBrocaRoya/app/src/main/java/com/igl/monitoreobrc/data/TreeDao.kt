package com.igl.monitoreobrc.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TreeDao {
    @Query("SELECT * FROM trees ORDER BY date DESC")
    fun getAll(): Flow<List<TreeRecord>>

    @Query("SELECT * FROM trees WHERE disease != 'ninguna' ORDER BY date DESC")
    fun getDiseased(): Flow<List<TreeRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(record: TreeRecord)

    @Delete
    fun delete(record: TreeRecord)

    @Query("SELECT COUNT(*) FROM trees WHERE disease = :disease")
    fun countByDisease(disease: String): Flow<Int>
}
