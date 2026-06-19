package com.igl.monitoreobrc

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trees")
data class TreeRecord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val plot: String,
    val treeNumber: Int,
    val disease: String, // broca, roya, ninguna
    val severity: String = "leve", // leve, moderada, severa
    val date: Long = System.currentTimeMillis(),
    val notes: String = ""
)

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
